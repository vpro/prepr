package nl.vpro.io.prepr.rs;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Michiel Meeuwissen
 * @since 5.7
 */
public class SignatureValidatorInterceptorTest {


    byte[] example = "{\"id\":\"6079a3de-6387-4891-96dc-626c659a4269\",\"created_on\":1591959460,\"dc\":\"api.eu1.prepr.io\",\"event\":\"asset.created\",\"payload\":{\"id\":\"d7fdb12e-d244-4576-a10d-ffc4c60dd5f3\",\"created_on\":\"2020-06-11T22:51:31+00:00\",\"changed_on\":\"2020-06-11T22:51:31+00:00\",\"label\":\"Cover\",\"name\":\"1591915891\",\"body\":null,\"author\":null,\"reference_id\":null}}".getBytes();
    String signature = "93c76a323a95f1b4d992cad66480cebb5f022358809d9669c1172f26152cae38";

    SignatureValidatorInterceptor impl = new SignatureValidatorInterceptor();

    {
        SignatureValidatorInterceptor.put("RAD5", UUID.fromString("62d77be4-d41d-4878-bffc-ed4a047a9101"));
        SignatureValidatorInterceptor.put("RAD3", UUID.fromString("ea4ae64f-a80e-4325-9d6e-f21322961ae8"));
    }

    @Test
    public void sign() throws NoSuchAlgorithmException, InvalidKeyException {
        String signed = impl.sign(UUID.fromString("62d77be4-d41d-4878-bffc-ed4a047a9101"), example);
        assertThat(signed).isEqualTo(signature);
    }


    @Test
    public void validate() throws NoSuchAlgorithmException, InvalidKeyException {
        impl.validate(signature, example, "RAD3");

    }


    @Test
    public void validateInvalid() throws InvalidKeyException {
        assertThrows(SecurityException.class, () -> {
            impl.validate("signature doesn't match", example, "RAD5");
        });

    }
}
