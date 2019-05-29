package nl.vpro.io.prepr.domain;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;



/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Slf4j
public class MCScheduleTest {



    @Before
    public void init() {
        MCObjectMapper.configureInstance(false);

    }

    @Test
    public void unmarshal() throws IOException {
        MCSchedule schedule  = MCObjectMapper.INSTANCE.readerFor(MCSchedule.class)
            .readValue(getClass().getResourceAsStream("/mcschedule.json"));

        MCEvent next = schedule.getDays().get(LocalDate.of(2018, 9, 3)).iterator().next();
        assertThat(next.getFrom()).isEqualTo(LocalTime.of(0, 0));
        assertThat(next.getShow().getName()).isEqualTo("Thijs Maalderink");
        assertThat(next.getShow().getCrid()).isEqualTo("crid://prepr.io/show/259bb118-fc8b-4333-90e5-5d176f8b3260");
        assertThat(next.getShow().getTags().get(0).getSlug()).isEqualTo("bnnvara");

        log.info("{}", schedule);

    }


}
