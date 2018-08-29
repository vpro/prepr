package nl.vpro.io.mediaconnect;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Ignore;
import org.junit.Test;

import nl.vpro.io.mediaconnect.domain.*;

import static nl.vpro.io.mediaconnect.Paging.limit;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Slf4j
public class MediaConnectRepositoryImplITest {


    MediaConnectRepositoryImpl impl = MediaConnectRepositoryImpl.configuredInUserHome("RAD2");
    {
        impl.setLogAsCurl(true);
    }

    @Test
    public void authenticate() throws IOException {
        impl.getToken();
        log.info("Token: {}", impl.getTokenResponse());
    }


    // werkt met account 1
    @Test
    public void getSchedule1() {
        LocalDate date = LocalDate.of(2018, 5, 7);
        MCSchedule schedule = impl.getPrepr().getSchedule(
            UUID.fromString("59ad94c1-7dec-4ea0-a9b4-b9eb4b6cfb16") // Channel.RAD5)
            ,
          date);
        log.info("schedule: {}", schedule);
    }



    // werkt met account 2
    @Test
    public void getSchedule2() {
        LocalDate date = LocalDate.of(2018, 7, 10);
        MCSchedule schedule = impl.getPrepr().getSchedule(
            UUID.fromString("8efcb3c7-8b23-4520-9d59-0c076d89ff01") // Guide ID van Channel.RAD2
            ,
          date);
        log.info("schedule: {}", schedule);
    }
     // werkt met account 2
    @Test
    public void getSchedule3() {
        LocalDate date = LocalDate.of(2018, 7, 4);
        MCSchedule schedule = impl.getPrepr().getSchedule(
            UUID.fromString("8efcb3c7-8b23-4520-9d59-0c076d89ff01") // Guide ID van Channel.RAD2
            ,
          date);
        log.info("schedule: {}", schedule);
        for (Map.Entry<LocalDate, List<MCEvent>> e : schedule) {
            log.info("{}", e.getKey());
            for (MCEvent event : e.getValue()) {
                log.info("  {}", event);
            }
        }
    }



       // werkt met account 2
    @Test
    public void getGuides() {
        MCItems<MCGuide> result = impl.getGuides().getGuides(null);
        for (MCGuide guide : result) {
            log.info("guide: {}", guide);
        }
    }
    // werkt met account 2
    @Test
    public void getSchedule4() {
        LocalDate date = LocalDate.of(2018, 7, 7);
        MCSchedule schedule = impl.getGuides().getSchedule(
            UUID.fromString("8efcb3c7-8b23-4520-9d59-0c076d89ff01") // Guide ID van Channel.RAD2
            ,
          date);
        log.info("schedule: {}", schedule);
    }



    @Test
    public void getPublicationsForChannel() {
        MCItems<?> publications = impl.getContent().getPublicationsForChannel(Paging.builder().build(),
            UUID.fromString("028b041f-7951-45f4-a83f-cd88bdb336c0"),  // Channel.RAD5)
                null, null);
        log.info("publications : {}", publications);
    }



    @Test
    public void getPublication() {
        MCContent publications = impl.getContent().getPublication(
            UUID.fromString("bd6bdae7-24c5-4185-90f8-005b1e8b0e83") // an post?
        );
        log.info("publications : {}", publications);
    }

    @Test
    public void getProgram() {
        log.info("{}",
            impl.getContent().getPublication(UUID.fromString("cf80db4c-40d7-40ee-9f25-4bfbcfd1e351")) // a showdetails. This is deprecated!
        );

    }


    @Test
    public void getProgram2() {
        log.info("{}",
            impl.getContent().getPublication(UUID.fromString("0033f611-188e-438f-878b-ca976dfa18dd")) // a showdetails. This is deprecated!
        );

    }


    @Test
    public void getTag() {
        log.info("{}",
            impl.getContent().getPublication(UUID.fromString("717a29f6-930e-4988-9a03-872b404cf4af"))
        );
    }


    @Test
    public void getTagGroups() {
        log.info("{}",
            impl.getTags().getGroups(Paging.builder().build())
        );



    }


    @Test
    public void getTagGroup() {
        log.info("{}",
            impl.getTags().getGroups(Paging.builder().build(), "Rollen")
        );



    }

    @Test
    @Ignore("Our account gives 401 only.")
    public void getChannels() {
        MCItems<?> publications = impl.getContent()
            .getChannels(Paging.builder().build());
        log.info("channels : {}", publications);
    }



    @Test
    public void getContainers() {
        MCItems<?> publications = impl.getContent()
            .getContainers(Paging.builder().build());
        log.info("channels : {}", publications);
    }
    @Test
    public void getContainer() {

          log.info("{}",
            impl.getContent().getContainer(UUID.fromString("d82fb840-cd42-4eea-b11c-d24d809f1a47"))
        );
    }

     @Test
    public void getContainer2() {

          log.info("{}",
            impl.getContent().getContainer(UUID.fromString("5d3e2d15-7bfa-4ab2-96d8-20a4f86847cf"))
        );
    }



    @Test
    public void getWebhooksAndDelete() {
        MCItems<MCWebhook> webhooks = impl.getWebhooks().get(limit(100L));
        log.info("webhooks: {}", webhooks);
        for (MCWebhook webhook : webhooks) {
            log.info("Found webook {}", webhook);
            if (webhook.getCallback_url().startsWith("https://api-itest")) {
                log.info("Deleting {}", webhook);

                //impl.getWebhooks().delete(webhook.getId());
            }

        }

    }


     @Test
    public void createWebhook() {
         String url = "https://api-itest.poms.omroep.nl/mediaconnect/RAD5";
         log.info("new webook {}", impl.getWebhooks().create(url,  "showschedule.created",
             "showschedule.changed",
             "showschedule.deleted"));
    }


    @Test
    public void getAsssets() {
        MCItems<MCAsset> assets  = impl.getAssets().get(limit(100L));
        log.info("assets: {}", assets);


    }
}
