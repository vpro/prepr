package nl.vpro.io.prepr;

import java.time.LocalDateTime;
import java.util.UUID;

import nl.vpro.io.prepr.domain.AbstractPreprContent;
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

    <T extends AbstractPreprContent> T getPublication(
        UUID id
    );


    PreprItems<?> getChannels(Paging paging);


    PreprItems<?> getContainers(Paging paging);


    <T extends AbstractPreprContent> T getContainer(
        UUID id
    );




}
