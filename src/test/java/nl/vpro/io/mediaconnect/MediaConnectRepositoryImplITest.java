package nl.vpro.io.mediaconnect;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

import org.junit.Test;

import nl.vpro.io.mediaconnect.domain.*;

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
    public void getSchedule() {
        MCSchedule schedule = impl.getSchedule(
            UUID.fromString("8efcb3c7-8b23-4520-9d59-0c076d89ff01"), // Guide ID van Channel.RAD2
            LocalDate.of(2018, 7, 4),
            LocalDate.of(2018, 7, 4));
        log.info("schedule: {}", schedule);
    }



    @Test
    public void getPublicationsForChannel() {
        MCItems<?> publications = impl.getPublicationsForChannel(Paging.builder().build(),
            UUID.fromString("028b041f-7951-45f4-a83f-cd88bdb336c0"),  // Channel.RAD5)
                null, null);
        log.info("publications : {}", publications);
    }



    @Test
    public void getPublication() {
        MCPost publications = impl.getPublication(
            UUID.fromString("bd6bdae7-24c5-4185-90f8-005b1e8b0e83")
        );
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
