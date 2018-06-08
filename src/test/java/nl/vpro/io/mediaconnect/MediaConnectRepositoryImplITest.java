package nl.vpro.io.mediaconnect;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

import org.junit.Test;

import nl.vpro.io.mediaconnect.domain.MCItems;
import nl.vpro.io.mediaconnect.domain.MCWebhook;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Slf4j
public class MediaConnectRepositoryImplITest {


    MediaConnectRepositoryImpl impl = MediaConnectRepositoryImpl.configuredInUserHome();

    @Test
    public void authenticate() throws IOException {
        impl.getToken();
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
    public void getWebhooksAndDelete() throws IOException {
        MCItems<MCWebhook> webhooks = impl.getWebhooks();
        log.info("webhooks: {}", webhooks);
        for (MCWebhook webhook : webhooks) {
            if (webhook.getCallback_url().startsWith("https://api-itest")) {
                log.info("Deleting {}", webhook);

                impl.deleteWebhook(webhook.getId());
            }
        }

    }


     @Test
    public void createWebhook() throws IOException {
         String url = "https://api-itest.poms.omroep.nl/mediaconnect/RAD5";
         log.info("new webook {}", impl.createWebhook(url,  "showschedule.created",
             "showschedule.changed",
             "showschedule.deleted"));
    }
}
