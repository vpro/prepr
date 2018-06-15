package nl.vpro.io.mediaconnect;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

import org.junit.Test;

import nl.vpro.io.mediaconnect.domain.MCAsset;
import nl.vpro.io.mediaconnect.domain.MCItems;
import nl.vpro.io.mediaconnect.domain.MCSchedule;
import nl.vpro.io.mediaconnect.domain.MCWebhook;

import static nl.vpro.io.mediaconnect.Paging.limit;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Slf4j
public class MediaConnectRepositoryImplITest {


    MediaConnectRepositoryImpl impl = MediaConnectRepositoryImpl.configuredInUserHome();
    {
        impl.setLogAsCurl(true);
    }

    @Test
    public void authenticate() throws IOException {
        impl.getToken();
        log.info("Token: {}", impl.getTokenResponse());
    }

    @Test
    public void getSchedule() throws IOException {
        MCSchedule schedule = impl.getSchedule(UUID.fromString("59ad94c1-7dec-4ea0-a9b4-b9eb4b6cfb16") // Channel.RAD5)
            , LocalDate.of(2018, 5, 7), LocalDate.of(2018, 5, 8));
        log.info("schedule: {}", schedule);
    }



    @Test
    public void getPublications() throws IOException {
        MCItems<?> publications = impl.getPublications(Paging.builder().build(),
            UUID.fromString("028b041f-7951-45f4-a83f-cd88bdb336c0"),  // Channel.RAD5)
                null, null);
        log.info("publications : {}", publications);
    }



    @Test
    public void getChannels() throws IOException {
        MCItems<?> publications = impl.getChannels(Paging.builder().build());
        log.info("publications : {}", publications);
    }



    @Test
    public void getWebhooksAndDelete() throws IOException {
        MCItems<MCWebhook> webhooks = impl.getWebhooks(limit(100L));
        log.info("webhooks: {}", webhooks);
        for (MCWebhook webhook : webhooks) {
            log.info("Found webook {}", webhook);
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


    @Test
    public void getAsssets() throws IOException {
        MCItems<MCAsset> assets  = impl.getAssets(limit(100L));
        log.info("assets: {}", assets);


    }
}
