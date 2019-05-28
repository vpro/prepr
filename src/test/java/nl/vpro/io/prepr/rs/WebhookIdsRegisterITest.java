package nl.vpro.io.prepr.rs;

import org.junit.Test;

import nl.vpro.io.prepr.MediaConnectRepositoryImpl;
import nl.vpro.io.prepr.StandaloneMediaConnectRepositories;

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
