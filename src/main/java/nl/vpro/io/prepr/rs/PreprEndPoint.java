package nl.vpro.io.prepr.rs;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.ws.rs.NameBinding;

/**
 *
 * This annotation can be used on your JAXRS implementation of a webhook callback. Because {@link SignatureValidatorInterceptor} has it too, the signatures will be validated implicitly then.
 * <p>
 * E.g.:
 *<pre>{@code
@literal @Path("/prepr")
 public class PreprWebhookEndpoint {
   @literal @POST
   @literal @Path("/{channel}")
   @literal @PreprEndPoint
    public String post(
       @literal @PathParam("channel") Channel channel,
        JsonNode payload,
       @literal @Context HttpServletRequest request
    ) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        PreprObjectMapper.INSTANCE.writeValue(output, payload);

        jms.convertAndSend(jmsQueue, output.toByteArray(), new MessagePostProcessor() {
        return "Accepted to queue " + jmsQueue;
    }
})
}
}</pre>
 *
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@NameBinding
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PreprEndPoint {

}
