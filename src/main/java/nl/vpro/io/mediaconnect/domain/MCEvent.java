package nl.vpro.io.mediaconnect.domain;

import lombok.Data;

import java.time.LocalTime;
import java.util.List;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Data
public class MCEvent {


    LocalTime from;
    LocalTime until;

    // pleonastic
    Integer offset;
    Integer limit;

    MCRule rules;

    MCShow show;

    MCUsers users;

    List<MCTimeline> timelines;

}
