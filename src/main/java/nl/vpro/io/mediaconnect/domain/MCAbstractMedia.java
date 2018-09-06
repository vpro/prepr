package nl.vpro.io.mediaconnect.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Duration;
import java.time.Instant;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.DurationSerializer;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class MCAbstractMedia extends MCAsset {

    @JsonSerialize(using = DurationSerializer.class)
    Duration duration;

    Instant started_on;
    Instant ended_on;

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
            "duration='" + duration + '\'' +
            ", name='" + name + '\'' +
            ", body='" + body + '\'' +
            ", source_file=" + source_file +
            ", reference_id=" + reference_id +

            ", id=" + id +
            '}';
    }
}
