package nl.vpro.io.prepr;

import java.util.UUID;

import nl.vpro.io.prepr.domain.PreprTimeline;

/**
 * https://developers.prepr.io/docs/getting-started-timeline-assets
 * @author Michiel Meeuwissen
 * @since 0.3
 */
public interface PreprContainers {


    PreprTimeline getTimeline(
        UUID id
    );

}
