package nl.vpro.io.prepr;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import nl.vpro.io.prepr.domain.*;

import static nl.vpro.io.prepr.Paging.limit;
import static nl.vpro.logging.Slf4jHelper.debugOrInfo;


/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Slf4j
public class PreprRepositoryImplITest {


    static PreprRepositoryImpl rad1 = PreprRepositoryImpl.configuredInUserHome("RAD1");
    static PreprRepositoryImpl rad2 = PreprRepositoryImpl.configuredInUserHome("RAD2");
    static PreprRepositoryImpl rad3 = PreprRepositoryImpl.configuredInUserHome("RAD3");
    static PreprRepositoryImpl rad4 = PreprRepositoryImpl.configuredInUserHome("RAD4");
    static PreprRepositoryImpl rad5 = PreprRepositoryImpl.configuredInUserHome("RAD5");
    static PreprRepositoryImpl funx = PreprRepositoryImpl.configuredInUserHome("FUNX");
    static PreprRepositoryImpl test = PreprRepositoryImpl.configuredInUserHome("XXXX");

    //PreprRepositoryImpl fnxa = PreprRepositoryImpl.configuredInUserHome("FNXA");
    //PreprRepositoryImpl fnxar = PreprRepositoryImpl.configuredInUserHome("FNXAR");
    //PreprRepositoryImpl fnxhh = PreprRepositoryImpl.configuredInUserHome("FNXHH");
    //PreprRepositoryImpl fnxre = PreprRepositoryImpl.configuredInUserHome("FNXRE");
    //PreprRepositoryImpl fnxr = PreprRepositoryImpl.configuredInUserHome("FNXR");

    static {
        PreprObjectMapper.configureInstance(false);
    }
/*

    @Test
    public void authenticate() throws IOException {
        rad2.getContent().getToken();
        log.info("Token: {}", rad2.getTokenResponse());
    }
*/

    @Test
    void localdatetime() {
        DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        log.info("{}", LocalDateTime.parse("2007-04-05T24:00", FORMATTER));

    }

    @Test
    void getSchedules() {
        //getSchedule(rad2);
        LocalDate date = LocalDate.of(2019, 6, 4);
        getSchedule(test, date);
        //getSchedule(fnxa, date);
    }

    @Test
    void getSchedule() {
        LocalDate firstDate = LocalDate.of(2020, 11, 13);
        PreprSchedule schedule1 = getSchedule(test, firstDate);
        Optional<PreprEvent> lastEvent = schedule1.getDays().values().stream().reduce((a, b) -> b).map(l -> l.stream().reduce((a, b) -> b)).get();
        PreprSchedule schedule2 = getSchedule(test, firstDate.plusDays(1));
        Optional<PreprEvent> firstEvent = schedule2.getDays().values().stream().reduce((a, b) -> a).map(l -> l.stream().reduce((a, b) -> a)).get();

        log.info("last: {}", lastEvent);
        log.info("first: {}", firstEvent);
    }

    protected PreprSchedule getSchedule(PreprRepository impl, LocalDate date) {

        PreprSchedule schedule = impl.getGuides().getSchedule(date);
        log.info("schedule: {}", schedule);
        for (Map.Entry<LocalDate, List<PreprEvent>> e : schedule) {
            log.info("{}", e.getKey());
            for (PreprEvent event : e.getValue()) {
                log.info("  {}", event);
            }
        }
        return schedule;
    }

    @Test
    void getGuides() {
        PreprItems<PreprGuide> result = test.getGuides().getGuides(null);
        for (PreprGuide guide : result) {
            log.info("guide: {}, {}", guide, guide.getUUID());
        }
    }

    @Test
    public void getGuidesFunx() {
        PreprItems<PreprGuide> result = funx.getGuides().getGuides(null);
        for (PreprGuide guide : result) {
            log.info("guide: {}", guide);
        }
    }

    @Test
    public void getPublicationsForChannel() {
        PreprItems<?> publications = rad2.getContent().getPublicationsForChannel(Paging.builder().build(),
            UUID.fromString("8efcb3c7-8b23-4520-9d59-0c076d89ff01"),  // Channel.RAD5)
                null, null);
        log.info("publications : {}", publications);
    }



    @Test
    public void getPublication() {
        PreprAbstractContent publications = funx.getContent().getPublication(
            UUID.fromString("0c83faf9-2524-4ebe-a26f-070be9ec4415") // an post?
        );
        log.info("publications : {}", publications);
    }


    @Test
    public void getArchivedPublication() {
        PreprAbstractContent publications = rad2.getContent().getPublication(
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
            rad3.getContent().getPublication(UUID.fromString("5ca9fb3b-9fcb-4666-8f5f-6673acabd11f")));
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
        PreprItems<?> publications = funx.getContent()
            .getChannels(Paging.builder().build());
        log.info("channels : {}", publications);
    }




    @Test
    public void getContainers() {
        PreprItems<?> publications = rad2.getContent()
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


    public static Stream<PreprRepository> getRepos() {
        return Stream.of(rad1, rad2, rad3, rad4, rad5, funx);
    }

    @ParameterizedTest
    @MethodSource("getRepos")
    public void getWebhooksAndDelete(PreprRepository repo) {
        PreprItems<PreprWebhook> webhooks = repo.getWebhooks().get(limit(100L));
       //log.info("webhooks: {}", webhooks);
        for (PreprWebhook webhook : webhooks) {
            debugOrInfo(log, webhook.getCallback_url().contains("poms") || webhook.getCallback_url().contains("meeuw"), "Found webhook {} {}:, {}", webhook.getId(),  webhook, webhook.getCreated_on());
            if (webhook.getCallback_url().startsWith("https://proxy.meeuw.org")) {
                //
                // log.info("{}", webhook);
                //webhook.setActive(true);
                log.info("Deleting {}", webhook);
                //log.info("{}", repo.getWebhooks().put(webhook));
                repo.getWebhooks().delete(webhook.getUUID());
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
    public void getAssets() {
        PreprItems<PreprAsset> assets  = rad2.getAssets().get(limit(100L));
        log.info("assets: {}", assets);
    }

    @Test
    public void getTimeline() {
        PreprTimeline timeline = test.getContainers().getTimeline(UUID.fromString("7c94ebfe-3555-4ea7-a4e1-69c6db06c415"));
        log.info("timeline: {}", timeline);
    }


    @Test
    public void getPerson() {
        PreprPerson person = funx.getPersons().get(UUID.fromString("6ab9b623-6815-4696-b276-de43cde4d06f"));
        log.info("psers: {}", person);
    }
}
