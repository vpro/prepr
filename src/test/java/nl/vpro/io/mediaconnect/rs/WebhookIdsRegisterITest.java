package nl.vpro.io.mediaconnect.rs;

import org.junit.Test;

import nl.vpro.io.mediaconnect.MediaConnectRepositoryImpl;
import nl.vpro.io.mediaconnect.StandaloneMediaConnectRepositories;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
public class WebhookIdsRegisterITest {


    StandaloneMediaConnectRepositories repositories = StandaloneMediaConnectRepositories
        .builder()
        .repository("RAD2",
            MediaConnectRepositoryImpl.configuredInUserHome("RAD2"))
        .build();


    WebhookIdsRegister register = new WebhookIdsRegister(repositories, "");

    @Test
    public void init() {
        register.init();
    }


}
