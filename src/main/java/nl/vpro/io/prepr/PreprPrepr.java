package nl.vpro.io.prepr;

import java.time.LocalDate;
import java.util.UUID;

import nl.vpro.io.prepr.domain.PreprSchedule;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public interface PreprPrepr {

    @Deprecated
    PreprSchedule getSchedule(UUID channel, LocalDate from, LocalDate until);

    @Deprecated
    default PreprSchedule getSchedule(UUID channel, LocalDate from) {
        return getSchedule(channel, from, from);
    }

}
