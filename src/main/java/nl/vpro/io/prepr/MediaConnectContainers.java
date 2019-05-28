package nl.vpro.io.prepr;

import java.util.UUID;

import nl.vpro.io.prepr.domain.MCTimeline;

/**
 * https://developers.mediaconnect.io/docs/getting-started-timeline-assets
 * @author Michiel Meeuwissen
 * @since 0.3
 */
public interface MediaConnectContainers {


    MCTimeline getTimeline(
        UUID id
    );

}
