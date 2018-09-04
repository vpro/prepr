package nl.vpro.io.mediaconnect.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonTypeName("Timeline")
public class MCTimeline extends MCContent {

    String reference_id;

    String timecode;

    LocalDateTime from;

    LocalDateTime until;

    List<MCAbstractMedia> assets;

    List<MCAbstractObject> publications;
}
