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
public class PreprShow extends PreprContent {

    String name;

    List<PreprTag> tags;

    PreprPhoto cover;

    String reference;

    List<PreprPerson> scheduled_users;

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
