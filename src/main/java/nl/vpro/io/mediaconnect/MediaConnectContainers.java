package nl.vpro.io.mediaconnect;

import java.util.UUID;

import nl.vpro.io.mediaconnect.domain.MCTimeline;

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
