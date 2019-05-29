package nl.vpro.io.prepr.rs;

import org.junit.Test;

import nl.vpro.io.prepr.PreprRepositoryImpl;
import nl.vpro.io.prepr.StandalonePreprRepositories;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
public class WebhookIdsRegisterITest {


    StandalonePreprRepositories repositories = StandalonePreprRepositories
        .builder()
        .repository("RAD2",
            PreprRepositoryImpl.configuredInUserHome("RAD2"))
        .build();


    WebhookIdsRegister register = new WebhookIdsRegister(repositories, "");

    @Test
    public void init() {
        register.init();
    }


}
