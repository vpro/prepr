package nl.vpro.io.mediaconnect.rs;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 5.7
 */
public class SignatureValidatorInterceptorTest {


    byte[] example = "{\"id\":\"08bc6cd2-7da5-4708-8e61-6754ee0c158c\",\"created_on\":1525703268,\"event\":\"showschedule.deleted\",\"payload\":{\"from\":\"00:10:00\",\"until\":\"06:00:00\",\"offset\":600,\"limit\":21000,\"rules\":{\"id\":\"34c1ec2e-0f62-4f9f-8585-b7c09b040014\",\"created_on\":\"2018-05-07T14:27:42+00:00\",\"changed_on\":null,\"label\":\"ShowSchedule\",\"offset\":600,\"limit\":21000,\"valid_from\":\"2018-05-10\",\"valid_until\":\"2018-05-10\",\"exception\":true,\"days\":[\"1\",\"2\",\"3\",\"4\",\"5\",\"7\"]}}}".getBytes();
    String signature = "24388cb0112ac5448ba4da3079260c3d28fdbb2a7ea59f1e791d7c09843c0ad8";

    SignatureValidatorInterceptor impl = new SignatureValidatorInterceptor();

    {
        SignatureValidatorInterceptor.put("RAD5", UUID.fromString("62d77be4-d41d-4878-bffc-ed4a047a9101"));
    }

    @Test
    public void sign() throws NoSuchAlgorithmException, InvalidKeyException {
        String signed = impl.sign(UUID.fromString("62d77be4-d41d-4878-bffc-ed4a047a9101"), example);
        assertThat(signed).isEqualTo(signature);
    }


    @Test
    public void validate() throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        impl.validate(signature, example, "RAD5");

    }


    @Test(expected = SecurityException.class)
    public void validateInvalid() throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        impl.validate("signature doesn't match", example, "RAD5");

    }
}
