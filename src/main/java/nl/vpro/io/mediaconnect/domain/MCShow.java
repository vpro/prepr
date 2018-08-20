package nl.vpro.io.mediaconnect.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonTypeName("Show")
public class MCShow extends MCContent {

    String slug;

    String name;

    String body;

    List<MCTag> tags;

    MCPhoto cover;

    String reference_id;

    String reference;


    @Override
    public String toString() {
        return "MCShow{" +
            "slug='" + slug + '\'' +
            ", name='" + name + '\'' +
            ", body='" + body + '\'' +
            ", tags=" + tags +
            ", cover=" + cover +
            ", status=" + status +
            ", crid=" + getCrid() +
            ", label='" + label + '\'' +
            '}';
    }
}
