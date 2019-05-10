package nl.vpro.io.mediaconnect.rs;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;


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

    public static final Map<String, UUID> WEBHOOK_IDS = new ConcurrentHashMap<>();


    public static void put(String channel, UUID webhookId) {
        if (WEBHOOK_IDS.put(channel, webhookId) == null) {
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
        UUID webhookId = WEBHOOK_IDS.get(channel);
        if (webhookId == null)  {
            log.warn("No webhookId found for {} (Only known for {})", channel, WEBHOOK_IDS.keySet());
            throw new SecurityException("Webhook id currently not registered for " + channel);
        }
         if (signature == null) {
             throw new SecurityException("No signature given");
         }
        String sign = sign(webhookId, payload);

        if (! Objects.equals(sign, signature)) {
            log.warn("Incoming signature {} didn't match {}", signature, sign);
            throw new SecurityException("Signature didn't match");
        } else {
            log.debug("Validated {}", signature);
        }
    }

    String sign(UUID webhookId, byte[] json) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(webhookId.toString().getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        return new String(Hex.encodeHex(sha256_HMAC.doFinal(json)));
    }

}
