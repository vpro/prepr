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

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.slf4j.MDC;


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

    public static final String SIGNATURE = "Mediaconnect-Signature";

    public static final Map<String, List<UUID>> WEBHOOK_IDS = new ConcurrentHashMap<>();


    public static void put(String channel, UUID webhookId) {
        List<UUID> uuids = WEBHOOK_IDS.computeIfAbsent(channel, (i) -> Collections.synchronizedList(new ArrayList<>()));
        if (uuids.contains(webhookId)) {
            log.debug("webhook for {} was registered already {}", channel, webhookId);
        } else {
            uuids.add(webhookId);
            log.info("Registered webook {} -> {}", channel, webhookId);
        }
    }


    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        ByteArrayOutputStream payload = new ByteArrayOutputStream();
        String signature = requestContext.getHeaderString(SIGNATURE);
        String[] split = requestContext.getUriInfo().getPath().split("/");
        String channel = split[split.length - 1];
        IOUtils.copy(requestContext.getEntityStream(), payload);
        requestContext.setEntityStream(new ByteArrayInputStream(payload.toByteArray()));
        try {
            validate(signature, payload.toByteArray(), channel);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            log.error(e.getMessage(), e);
        }

    }


    protected void validate(
        String signature,
        byte[] payload,
        String channel) throws NoSuchAlgorithmException, InvalidKeyException {
        List<UUID> uuids = WEBHOOK_IDS.get(channel);
        if (uuids== null)  {
            log.warn("No webhookId found for {} (Only known for {})", channel, WEBHOOK_IDS.keySet());
            throw new SecurityException("Webhook id currently not registered for " + channel);
        }
        if (signature == null) {
            throw new SecurityException("No signature given");
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
            MDC.put("webhookid", matched.toString());
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
