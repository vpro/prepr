package nl.vpro.io.mediaconnect.rs;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nonnull;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.slf4j.MDC;

import static org.apache.http.HttpHeaders.USER_AGENT;


/**
 * This can be used to verify webhook calls made by mediaconnect to your server
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
@MediaConnectEndPoint
@Provider
public class SignatureValidatorInterceptor implements ContainerRequestFilter {

    public static final String[] SIGNATURES = {"Mediaconnect-Signature", "Prepr-Signature"};
    public static final Map<String, List<UUID>> WEBHOOK_IDS = new ConcurrentHashMap<>();


    private static boolean ready = false;

    public static boolean put(@Nonnull String channel, @Nonnull UUID webhookId) {
        List<UUID> uuids = WEBHOOK_IDS.computeIfAbsent(channel, (i) -> Collections.synchronizedList(new ArrayList<>()));
        if (uuids.contains(webhookId)) {
            log.debug("webhook for {} was registered already {}", channel, webhookId);
            return false;
        } else {
            uuids.add(webhookId);
            log.info("Registered webook {} -> {}", channel, webhookId);
            return true;
        }
    }

    public static void readyForRequests() {
        ready = true;
    }


    @Override
    public void filter(@Nonnull ContainerRequestContext requestContext) throws IOException {

        if (! ready) {
            log.info("Received webhook while we are not yet ready and can't validate it yet");
            throw new ServerErrorException( "Received webhook while we are not yet ready and can't validate it yet", HttpStatus.SC_SERVICE_UNAVAILABLE);
        }
        String userAgent = requestContext.getHeaderString(USER_AGENT);

        // TODO maybe it's better to use the userAgent to check the actual prepr API version.

        String signature = null;
        for (String SIGNATURE : SIGNATURES) {
            signature = requestContext.getHeaderString(SIGNATURE);
            if (signature != null) {
                break;
            }
        }
        if (signature == null) {
            throw new SecurityException("No signature found");
        }
        String[] split = requestContext.getUriInfo().getPath().split("/");
        String channel = split[split.length - 1];

        ByteArrayOutputStream payload = new ByteArrayOutputStream();
        IOUtils.copy(requestContext.getEntityStream(), payload);
        requestContext.setEntityStream(new ByteArrayInputStream(payload.toByteArray()));
        try {
            validate(signature, payload.toByteArray(), channel);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            log.error(e.getMessage(), e);
        }

    }


    protected void validate(
        @Nonnull String signature,
        @Nonnull byte[] payload,
        @Nonnull String channel) throws NoSuchAlgorithmException, InvalidKeyException {
        List<UUID> uuids = WEBHOOK_IDS.get(channel);
        if (uuids== null)  {
            log.warn("No webhookId found for {} (Only known for {})", channel, WEBHOOK_IDS.keySet());
            throw new SecurityException("Webhook id currently not registered for " + channel);
        }
        UUID matched = null;
        for (UUID webhookId : uuids) {
            String sign = sign(webhookId, payload);

            if (!Objects.equals(sign, signature)) {
                log.warn("Incoming signature {} didn't match {} (payload (signed with{}):\n{}", signature, sign, webhookId, new String(payload, StandardCharsets.UTF_8));
            } else {
                log.debug("Validated {}", signature);
                matched = webhookId;
                break;
            }
        }
        if ( matched == null) {
            log.error("No webhook ids matched. We only see for channel " + uuids + " " + channel);
            throw new SecurityException("Signature didn't match");
        } else {
            MDC.put("userName", "webhook:" + matched.toString());
            if (uuids.size() > 1) {
                Iterator<UUID> i = uuids.iterator();
                while (i.hasNext()) {
                    UUID n = i.next();
                    if (n.equals(matched)) {
                        break;
                    } else {

                        //log.info("Removed {}, because now {} is matching", n, matched);
                        //i.remove();
                    }
                }
            }
        }
    }

    String sign(UUID webhookId, byte[] json) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(webhookId.toString().getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        return new String(Hex.encodeHex(sha256_HMAC.doFinal(json)));
    }

}
