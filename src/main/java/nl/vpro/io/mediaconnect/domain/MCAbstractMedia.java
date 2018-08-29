package nl.vpro.io.mediaconnect.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class MCAbstractMedia extends MCAsset {

    String duration;

    String custom_type;

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
