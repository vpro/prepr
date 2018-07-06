package nl.vpro.io.mediaconnect.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

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

    String from; // formatted time, in Europe/Amsterdam

    String until;

    @Override
    public String getCrid() {
        return CRID_PREFIX + getLabel().toLowerCase() + "/" + getReference_id();
    }
}
