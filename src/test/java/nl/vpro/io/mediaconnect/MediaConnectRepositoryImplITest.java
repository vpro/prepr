package nl.vpro.io.mediaconnect;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    MediaConnectRepositoryImpl fnxa = MediaConnectRepositoryImpl.configuredInUserHome("FNXA");
    MediaConnectRepositoryImpl fnxar = MediaConnectRepositoryImpl.configuredInUserHome("FNXAR");
    MediaConnectRepositoryImpl fnxhh = MediaConnectRepositoryImpl.configuredInUserHome("FNXHH");
    MediaConnectRepositoryImpl fnxre = MediaConnectRepositoryImpl.configuredInUserHome("FNXRE");
    MediaConnectRepositoryImpl fnxr = MediaConnectRepositoryImpl.configuredInUserHome("FNXR");



    //MediaConnectRepositoryImpl rad5 = MediaConnectRepositoryImpl.configuredInUserHome("RAD5");
    {
        MCObjectMapper.configureInstance(false);
    }
/*

    @Test
    public void authenticate() throws IOException {
        rad2.getContent().getToken();
        log.info("Token: {}", rad2.getTokenResponse());
    }
*/

    @Test
    public void localdatetime() {
        DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        log.info("{}", LocalDateTime.parse("2007-04-05T24:00", FORMATTER));

    }

    @Test
    public void getSchedules() {
        //getSchedule(rad2);
        LocalDate date = LocalDate.of(2018, 12, 18);
        getSchedule(fnxre, date);

        getSchedule(fnxa, date);

    }

    @Test
    public void getSchedule() {
        getSchedule(funx, LocalDate.of(2019, 1, 2));
    }



    protected void getSchedule(MediaConnectRepository impl,  LocalDate date) {

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
        MCContent publications = funx.getContent().getPublication(
            UUID.fromString("9a38cc38-8b23-48dc-80ae-b37ae6821988") // an post?
        );
        log.info("publications : {}", publications);
    }


    @Test
    public void getArchivedPublication() {
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
    public void getPost() {
        log.info("{}",
            funx.getContent().getPublication(UUID.fromString("ba1d6137-99fe-4aba-b92e-2090ee614b93")));

    }



    @Test
    public void getShow() {
        log.info("{}",
            funx.getContent().getPublication(UUID.fromString("a30ebc44-6832-4e68-91c7-3a7e668375db")));

    }


    @Test
    public void getTag() {
        log.info("{}",
            rad2.getContent().getPublication(UUID.fromString("717a29f6-930e-4988-9a03-872b404cf4af"))
        );
    }


     @Test
    public void getTagFunx() {
        log.info("{}",
            funx.getTags().getTag(UUID.fromString("233238cb-5b35-499f-919e-a59f20ec9b83"))
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
            funx.getTags().getGroups(Paging.builder().build(), "Rollen")
        );



    }

    @Test
    public void getChannels() {
        MCItems<?> publications = funx.getContent()
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
        MediaConnectRepository repo = fnxr;
        MCItems<MCWebhook> webhooks = repo.getWebhooks().get(limit(100L));
        log.info("webhooks: {}", webhooks);
        for (MCWebhook webhook : webhooks) {
            log.info("Found webook {}", webhook);
            if (webhook.getCallback_url().startsWith("https://api-proxy")) {
                log.info("Deleting {}", webhook);
                //repo.getWebhooks().delete(webhook.getUUID());
            }

        }

    }


    @Test
    public void createWebhook() {
         String url = "https://api-itest.poms.omroep.nl/mediaconnect/FUNX";
         log.info("new webook {}", funx.getWebhooks().create(url,  "showschedule.created",
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


    @Test
    public void getPerson() {
        MCPerson person = funx.getPersons().get(UUID.fromString("6ab9b623-6815-4696-b276-de43cde4d06f"));
        log.info("psers: {}", person);


    }
}
