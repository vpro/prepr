package nl.vpro.io.mediaconnect.domain;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;



/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class MCScheduleTest {




    @Test
    public void unmarshal() throws IOException {
        MCSchedule schedule  = MCObjectMapper.INSTANCE.readerFor(MCSchedule.class).readValue(getClass().getResourceAsStream("/mcschedule.json"));

        MCEvent next = schedule.getDays().get(LocalDate.of(2018, 7, 6)).iterator().next();
        assertThat(next.getFrom()).isEqualTo(LocalTime.of(0, 0));
        assertThat(next.getShow().getBody()).isEqualTo("'t Wordt nu laat");
        assertThat(next.getShow().getCrid()).isEqualTo("crid://mediaconnect.io/show/57b397a5-4049-414f-bab2-771f60943321");
        //assertThat(next.getShow().getTags().get(0).getSlug()).isEqualTo("omroep-max");






    }


}
