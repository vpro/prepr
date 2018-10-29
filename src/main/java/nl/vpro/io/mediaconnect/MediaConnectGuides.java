package nl.vpro.io.mediaconnect;

import java.time.LocalDate;

import nl.vpro.io.mediaconnect.domain.MCGuide;
import nl.vpro.io.mediaconnect.domain.MCItems;
import nl.vpro.io.mediaconnect.domain.MCSchedule;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public interface MediaConnectGuides {




    MCSchedule getSchedule(LocalDate from, LocalDate until, boolean exceptions);

    default MCSchedule getSchedule(LocalDate from, LocalDate until) {
        return getSchedule(from, until, true);
    }

    default MCSchedule getSchedule(LocalDate from) {
        return getSchedule(from, from);
    }

     MCItems<MCGuide> getGuides(String q);



}
