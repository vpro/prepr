package nl.vpro.io.prepr.domain;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;



/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Slf4j
public class PreprScheduleTest {



    @Before
    public void init() {
        PreprObjectMapper.configureInstance(true);

    }

    @Test
    public void unmarshal() throws IOException {
        PreprSchedule schedule  = PreprObjectMapper.INSTANCE.readerFor(PreprSchedule.class)
            .readValue(getClass().getResourceAsStream("/schedule.json"));

        PreprEvent next = schedule.getDays().get(LocalDate.of(2018, 9, 3)).iterator().next();
        assertThat(next.getFrom()).isEqualTo(LocalTime.of(0, 0));
        assertThat(next.getShow().getName()).isEqualTo("Thijs Maalderink");
        assertThat(next.getShow().getCrid()).isEqualTo("crid://prepr.io/show/259bb118-fc8b-4333-90e5-5d176f8b3260");
        assertThat(next.getShow().getTags().get(0).getSlug()).isEqualTo("bnnvara");

        log.info("{}", schedule);

    }



    @Test
    public void unmarshal2() throws IOException {
        PreprSchedule schedule  = PreprObjectMapper.INSTANCE.readerFor(PreprSchedule.class)
            .readValue(getClass().getResourceAsStream("/guides.json"));

        PreprEvent next = schedule.getDays().get(LocalDate.of(2020, 2, 7)).iterator().next();
        assertThat(next.getFrom()).isEqualTo(LocalTime.of(0, 0));
        assertThat(next.getShow().getName()).isEqualTo("Kevin");
        List<PreprContent> publications = next.getTimelines().get(0).getPublications();

        assertThat(publications).hasSize(13); // actually 15, but there is two 'Publication_NLUNKNOWN" that could not be read.

        log.info("{}", schedule);

    }



}
