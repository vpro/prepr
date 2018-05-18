package nl.vpro.io.mediaconnect;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.UUID;

import nl.vpro.io.mediaconnect.domain.MCItems;
import nl.vpro.io.mediaconnect.domain.MCSchedule;
import nl.vpro.io.mediaconnect.domain.MCWebhook;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public interface MediaConnectRepository {


    MCSchedule getSchedule(UUID channel, LocalDate from, LocalDate until) throws IOException, URISyntaxException;


    MCItems<MCWebhook> getWebhooks() throws IOException;

    void createWebhook(MCWebhook webhook) throws IOException;

    void deleteWebhook(UUID webhook) throws IOException;


}
