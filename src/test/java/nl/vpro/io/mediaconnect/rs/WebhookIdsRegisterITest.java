package nl.vpro.io.mediaconnect.rs;

import org.junit.Test;

import nl.vpro.io.mediaconnect.MediaConnectRepositoryImpl;

import static org.junit.Assert.*;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
public class WebhookIdsRegisterITest {


    MediaConnectRepositoryImpl impl = MediaConnectRepositoryImpl.configuredInUserHome();


    WebhookIdsRegister register = new WebhookIdsRegister(impl, "");

    @Test
    public void init() {
        register.init();
    }


}
