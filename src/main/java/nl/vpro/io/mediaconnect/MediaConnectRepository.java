package nl.vpro.io.mediaconnect;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.UUID;

import nl.vpro.io.mediaconnect.domain.MCSchedule;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public interface MediaConnectRepository {


    MCSchedule getSchedule(UUID channel, LocalDate from, LocalDate until) throws IOException, URISyntaxException;

}
