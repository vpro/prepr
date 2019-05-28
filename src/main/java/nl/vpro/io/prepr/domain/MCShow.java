package nl.vpro.io.prepr.domain;

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

    String name;

    List<MCTag> tags;

    MCPhoto cover;

    String reference;

    List<MCPerson> scheduled_users;

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