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

        MCEvent next = schedule.getDays().get(LocalDate.of(2018, 5, 7)).iterator().next();
        assertThat(next.getFrom()).isEqualTo(LocalTime.of(0, 0));
        assertThat(next.getShow().getBody()).isEqualTo("Een ontbijt van lekkere muziek en actuele gesprekken met Manuëla Kemp en Henk Mouwe.");
        assertThat(next.getShow().getCrid()).isEqualTo("crid://mediaconnect.io/show/098cdfa6-5f13-4534-a013-d94956ad63f2");





    }


}
