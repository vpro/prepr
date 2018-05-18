package nl.vpro.io.mediaconnect;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Properties;
import java.util.UUID;

import org.junit.Test;

import nl.vpro.io.mediaconnect.domain.MCWebhook;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Slf4j
public class MediaConnectRepositoryImplITest {


    MediaConnectRepositoryImpl impl;

    {

        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(System.getProperty("user.home") + File.separator + "conf" + File.separator + "mediaconnect.properties"));
            impl = MediaConnectRepositoryImpl
                .builder()
                .clientId(properties.getProperty("client_id"))
                .clientSecret(properties.getProperty("client_secret"))
                .build();
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }


    @Test
    public void authenticate() throws IOException {
        impl.authenticate();
        log.info("Token: {}", impl.getTokenResponse());
    }

    @Test
    public void getSchedule() throws IOException {
        log.info("schedule: {}",
            impl.getSchedule(UUID.fromString("59ad94c1-7dec-4ea0-a9b4-b9eb4b6cfb16") // Channel.RAD5)
                , LocalDate.of(2018, 5, 7), LocalDate.of(2018, 5, 8))
        );
    }



    @Test
    public void getWebhooks() throws IOException {
        log.info("webhooks: {}", impl.getWebhooks());

        log.info("token {},",  impl.getTokenResponse());
    }


     @Test
    public void createWebhook() throws IOException {

         MCWebhook webhook = MCWebhook.builder()
             .callback_url("https://api-dev.poms.omroep.nl/mediaconnect/RAD5")
             .event("showschedule.created")
             .event("showschedule.changed")
             .event("showschedule.deleted")
             .build();
         impl.createWebhook(webhook);
    }
}
