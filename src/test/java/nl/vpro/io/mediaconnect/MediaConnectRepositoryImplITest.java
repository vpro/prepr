package nl.vpro.io.mediaconnect;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
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


    MediaConnectRepositoryImpl rad2 = MediaConnectRepositoryImpl.configuredInUserHome("RAD2");
    MediaConnectRepositoryImpl funx = MediaConnectRepositoryImpl.configuredInUserHome("FUNX");
    //MediaConnectRepositoryImpl rad5 = MediaConnectRepositoryImpl.configuredInUserHome("RAD5");
    {
        rad2.setLogAsCurl(true);
        funx.setLogAsCurl(true);

        MCObjectMapper.configureInstance(false);
    }

    @Test
    public void authenticate() throws IOException {
        rad2.getToken();
        log.info("Token: {}", rad2.getTokenResponse());
    }


    @Test
    public void getSchedules() {
        getSchedule(rad2);
        getSchedule(funx);

    }



    protected void getSchedule(MediaConnectRepositoryImpl impl) {
        LocalDate date = LocalDate.of(2018, 9, 26);
        MCSchedule schedule = impl.getGuides().getSchedule(
          date);
        log.info("schedule: {}", schedule);
        for (Map.Entry<LocalDate, List<MCEvent>> e : schedule) {
            log.info("{}", e.getKey());
            for (MCEvent event : e.getValue()) {
                log.info("  {}", event);
            }
        }
    }




    @Test
    public void getGuides() {
        MCItems<MCGuide> result = rad2.getGuides().getGuides(null);
        for (MCGuide guide : result) {
            log.info("guide: {}", guide);
        }
    }




    @Test
    public void getGuidesFunx() {
        MCItems<MCGuide> result = funx.getGuides().getGuides(null);
        for (MCGuide guide : result) {
            log.info("guide: {}", guide);
        }
    }



    @Test
    public void getPublicationsForChannel() {
        MCItems<?> publications = rad2.getContent().getPublicationsForChannel(Paging.builder().build(),
            UUID.fromString("8efcb3c7-8b23-4520-9d59-0c076d89ff01"),  // Channel.RAD5)
                null, null);
        log.info("publications : {}", publications);
    }



    @Test
    public void getPublication() {
        MCContent publications = rad2.getContent().getPublication(
            UUID.fromString("bbf74244-9c76-4588-aa96-cf6e89671801") // an post?
        );
        log.info("publications : {}", publications);
    }

    @Test
    public void getProgram() {
        log.info("{}",
            rad2.getContent().getPublication(UUID.fromString("cf80db4c-40d7-40ee-9f25-4bfbcfd1e351")) // a showdetails. This is deprecated!
        );

    }


    @Test
    public void getProgram2() {
        log.info("{}",
            rad2.getContent().getPublication(UUID.fromString("0033f611-188e-438f-878b-ca976dfa18dd")) // a showdetails. This is deprecated!
        );

    }


    @Test
    public void getTag() {
        log.info("{}",
            rad2.getContent().getPublication(UUID.fromString("717a29f6-930e-4988-9a03-872b404cf4af"))
        );
    }


    @Test
    public void getTagGroups() {
        log.info("{}",
            rad2.getTags().getGroups(Paging.builder().build())
        );



    }


    @Test
    public void getTagGroup() {
        log.info("{}",
            rad2.getTags().getGroups(Paging.builder().build(), "Rollen")
        );



    }

    @Test
    public void getChannels() {
        MCItems<?> publications = rad2.getContent()
            .getChannels(Paging.builder().build());
        log.info("channels : {}", publications);
    }




    @Test
    public void getContainers() {
        MCItems<?> publications = rad2.getContent()
            .getContainers(Paging.builder().build());
        log.info("channels : {}", publications);
    }
    @Test
    public void getContentContainer() {

          log.info("{}",
            rad2.getContent().getContainer(UUID.fromString("d82fb840-cd42-4eea-b11c-d24d809f1a47"))
        );
    }


     @Test
    public void getContainer2() {

          log.info("{}",
            rad2.getContent().getContainer(UUID.fromString("5d3e2d15-7bfa-4ab2-96d8-20a4f86847cf"))
        );
    }



    @Test
    public void getWebhooksAndDelete() {
        MCItems<MCWebhook> webhooks = rad2.getWebhooks().get(limit(100L));
        log.info("webhooks: {}", webhooks);
        for (MCWebhook webhook : webhooks) {
            log.info("Found webook {}", webhook);
            if (webhook.getCallback_url().startsWith("https://proxy.meeuw")) {
                log.info("Deleting {}", webhook);
                rad2.getWebhooks().delete(webhook.getUUID());
            }

        }

    }


    @Test
    public void createWebhook() {
         String url = "https://api-itest.poms.omroep.nl/mediaconnect/RAD5";
         log.info("new webook {}", rad2.getWebhooks().create(url,  "showschedule.created",
             "showschedule.changed",
             "showschedule.deleted"));
    }


    @Test
    public void getAsssets() {
        MCItems<MCAsset> assets  = rad2.getAssets().get(limit(100L));
        log.info("assets: {}", assets);


    }

    @Test
    public void getTimeline() {
        MCTimeline timeline = rad2.getContainers().getTimeline(UUID.fromString("bbf74244-9c76-4588-aa96-cf6e89671801"));
        log.info("timeline: {}", timeline);


    }
}
