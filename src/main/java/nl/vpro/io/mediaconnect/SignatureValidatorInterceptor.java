package nl.vpro.io.mediaconnect;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.Properties;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;


/**
 * This can be used to verify webhook calls made by mediaconnect to your server
 *
 * It supposes an url of the form <code>../<channel id|webhook id></code>
 *
 *
 * TODO: perhaps it's clearer to simply suppose an url <code>/<webhook id></code> and ditch stuff with webhookids.properties.
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Provider
@PreMatching
@Slf4j
public class SignatureValidatorInterceptor implements ContainerRequestFilter {


    public static final String SIGNATURE = "Mediaconnect-Signature";


    Properties properties = new Properties();

    {
        try {
            properties.load(getClass().getResourceAsStream("/webhookids.properties"));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }


    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        StringWriter payload = new StringWriter();
        String signature = requestContext.getHeaderString(SIGNATURE);
        String[] split = requestContext.getUriInfo().getPath().split("/");
        String channel = split[split.length - 1];
        IOUtils.copy(requestContext.getEntityStream(), payload, "UTF-8");
        try {
            validate(signature, payload.toString(), channel);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            log.error(e.getMessage(), e);
        }

    }


    protected void validate(String signature, String payload, String channel) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        String webhookId = properties.getProperty(channel + ".webhookid");
        String sign = sign(webhookId, payload);
        if (! Objects.equals(sign, signature)) {
            throw new SecurityException("Signature didn't match");
        }
    }

    String sign(String webhookId, String json) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {

        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(webhookId.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);

        return new String(Hex.encodeHex(sha256_HMAC.doFinal(json.getBytes("UTF-8"))));


    }

}
