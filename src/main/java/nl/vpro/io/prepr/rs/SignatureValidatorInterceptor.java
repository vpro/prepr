package nl.vpro.io.prepr.rs;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.MDC;

import nl.vpro.jmx.MBeans;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.http.HttpHeaders.USER_AGENT;


/**
 * This can be used to verify webhook calls made by prepr to your server
 *
 * See
 * https://developers.mediaconnect.io/docs/webhooks
 * It supposes an url of the form <code>../<channel id></code>
 *
 * It needs to know the webhook id, which must be registered via {#put(String channel, UUID)}
 *
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Slf4j
@PreprEndPoint
@Provider
public class SignatureValidatorInterceptor implements  SignatureValidatorInterceptorMXBean, ContainerRequestFilter {

    private static final String[] SIGNATURES = {"Mediaconnect-Signature", "Prepr-Signature"};
    static final Map<String, List<UUID>> WEBHOOK_IDS = new ConcurrentHashMap<>();

    private static Instant ready = null;

    public static boolean put(@NonNull String channel, @NonNull UUID webhookId) {
        List<UUID> uuids = WEBHOOK_IDS.computeIfAbsent(channel, (i) -> Collections.synchronizedList(new ArrayList<>()));
        if (uuids.contains(webhookId)) {
            log.debug("webhook for {} was registered already {}", channel, webhookId);
            return false;
        } else {
            uuids.add(webhookId);
            log.info("Registered webhook {} -> {}", channel, webhookId);
            return true;
        }
    }

    public static void readyForRequests() {
        ready = Instant.now();
    }

    @Getter
    @Setter
    private InvalidSignatureAction invalidSignatureAction = InvalidSignatureAction.UNAUTHORIZED;


    //@PostConstruct
    public void registerBean() {
        MBeans.registerBean(MBeans.getObjectNameWithName(this, "signatureinterceptor"), this);
    }


    @Override
    public void filter(@NonNull ContainerRequestContext requestContext) throws IOException {
        if (ready == null) {
            log.info("Received webhook while we are not yet ready and can't validate it yet");
            throw new ServerErrorException( "Received webhook while we are not yet ready and can't validate it yet", HttpStatus.SC_SERVICE_UNAVAILABLE);
        }
        try {
            String userAgent = requestContext.getHeaderString(USER_AGENT);
            if (userAgent != null) {
                MDC.put("userAgent", userAgent);
                // TODO maybe it's better to use the userAgent to check the actual prepr API version.
            }

            String signature = null;
            for (String s : SIGNATURES) {
                signature = requestContext.getHeaderString(s);
                if (signature != null) {
                    log.debug("Found signature at {}", s);
                    break;
                }
            }
            String[] split = requestContext.getUriInfo().getPath().split("/");
            String channel = split[split.length - 1];

            if (signature == null) {
                log.warn("No signature found {} in ({})", SIGNATURES, requestContext.getHeaders().keySet());
                throw new NoSignatureException(invalidSignatureAction, channel, "No signature found", null);
            }

            ByteArrayOutputStream payload = new ByteArrayOutputStream();
            IOUtils.copy(requestContext.getEntityStream(), payload);
            requestContext.setEntityStream(new ByteArrayInputStream(payload.toByteArray()));
            try {
                validate(signature, payload.toByteArray(), channel);
            } catch (NoSuchAlgorithmException | InvalidKeyException e) {
                log.error(e.getMessage(), e);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }


    protected void validate(
        @NonNull String signature,
        byte @NonNull[] payload,
        @NonNull String channel) throws NoSuchAlgorithmException, InvalidKeyException, SignatureMatchException {
        final List<UUID> webhookuuids = WEBHOOK_IDS.get(channel);
        if (webhookuuids== null || webhookuuids.isEmpty())  {
            log.warn("No webhookId found for {} (Only known for {})", channel, WEBHOOK_IDS.keySet());
            throw new NotRegisteredSignatureException(invalidSignatureAction, channel, "Webhook id currently not registered for " + channel,  payload);
        }
        UUID matched = null;
        List<Runnable> warns = new ArrayList<>();
        for (UUID webhookId : webhookuuids) {
            String sign = sign(webhookId, payload);

            if (!Objects.equals(sign, signature)) {
                warns.add(() -> log.warn("Incoming signature {} didn't match {} (payload (signed with webookid: {}):\n{}", signature, sign, webhookId, new String(payload, UTF_8)));
            } else {
                log.debug("Validated {}", signature);
                matched = webhookId;
                break;
            }
        }
        if ( matched == null) {
            warns.forEach(Runnable::run);
            if (! invalidSignatureAction.test(Optional.ofNullable(ready).orElse(Instant.now()).plus(Duration.ofMinutes(5)))) {
                if (webhookuuids.size() == 1) {
                    throw new SignatureMatchException(invalidSignatureAction, channel, webhookuuids.get(0), "Validation for failed for " + channel + " webhook id: " + webhookuuids, payload);
                } else {
                    throw new SignatureMatchException(invalidSignatureAction, channel, webhookuuids.get(0), "No signing webhook ids matched. For channel " + channel + "  we see the following webhook ids:  " + webhookuuids, payload);
                }
            } else {
                log.info("Not matched call for channel {} but accepting any ways because {}", channel, invalidSignatureAction);
            }
        }
        MDC.put("userName", "webhook:" + matched);
        if (webhookuuids.size() > 1) {
            for (UUID n : webhookuuids) {
                if (n.equals(matched)) {
                    break;
                } else {
                    //log.info("Removed {}, because now {} is matching", n, matched);
                    //i.remove();
                }
                }
        }
    }

    String sign(UUID webhookId, byte[] json) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(webhookId.toString().getBytes(UTF_8), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        return new String(Hex.encodeHex(sha256_HMAC.doFinal(json)));
    }

    public static Optional<UUID> getWebhookIdForChannel(String channel) {
        return Optional.ofNullable(WEBHOOK_IDS.get(channel)).map(e -> e.get(0));
    }

    public static PreprWebhookAnswer createAnswer(String channel, String message) {
        return new PreprWebhookAnswer(message, getWebhookIdForChannel(channel).orElse(null), channel);
    }


}
