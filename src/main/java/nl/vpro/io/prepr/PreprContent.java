package nl.vpro.io.prepr;

import java.time.LocalDateTime;
import java.util.UUID;

import nl.vpro.io.prepr.domain.PreprAbstractContent;
import nl.vpro.io.prepr.domain.PreprItems;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public interface PreprContent {


    PreprItems<?> getPublicationsForChannel(
        Paging paging,
        UUID channel,
        LocalDateTime event_from,
        LocalDateTime event_utils
    );

    <T extends PreprAbstractContent> T getPublication(
        UUID id
    );


    PreprItems<?> getChannels(Paging paging);


    PreprItems<?> getContainers(Paging paging);


    <T extends PreprAbstractContent> T getContainer(
        UUID id
    );




}
