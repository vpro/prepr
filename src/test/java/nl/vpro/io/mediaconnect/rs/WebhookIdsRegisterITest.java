package nl.vpro.io.mediaconnect.rs;

import org.junit.Test;

import nl.vpro.io.mediaconnect.MediaConnectRepositories;
import nl.vpro.io.mediaconnect.MediaConnectRepositoryImpl;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
public class WebhookIdsRegisterITest {


    MediaConnectRepositories repositories = MediaConnectRepositories
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
