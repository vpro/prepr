package nl.vpro.io.mediaconnect;

import java.time.LocalDate;
import java.util.UUID;

import nl.vpro.io.mediaconnect.domain.MCSchedule;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public interface MediaConnectPrepr {

    MCSchedule getSchedule(UUID channel, LocalDate from, LocalDate until);

    default MCSchedule getSchedule(UUID channel, LocalDate from) {
        return getSchedule(channel, from, from);
    }

}
