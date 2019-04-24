package nl.vpro.io.mediaconnect;

import java.time.LocalDateTime;
import java.util.UUID;

import nl.vpro.io.mediaconnect.domain.MCContent;
import nl.vpro.io.mediaconnect.domain.MCItems;
import nl.vpro.io.mediaconnect.domain.MCStatus;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public interface MediaConnectContent {


    MCItems<?> getPublicationsForChannel(
        Paging paging,
        UUID channel,
        LocalDateTime event_from,
        LocalDateTime event_utils
    );

    <T extends MCContent> T getPublication(
        UUID id,
        MCStatus... status
    );


    MCItems<?> getChannels(Paging paging);


    MCItems<?> getContainers(Paging paging);


    <T extends MCContent> T getContainer(
        UUID id,
        MCStatus... status
    );




}
