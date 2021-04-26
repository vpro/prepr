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
@JsonTypeName(PreprShow.LABEL)
public class PreprShow extends AbstractPreprContent {
    public static final String LABEL = "Show";

    String name;

    List<PreprTag> tags;

    PreprPhoto cover;

    String reference;

    List<PreprPerson> scheduled_users;

    @Override
    public String toString() {
        return stringBuilder()
            .append("slug", slug)
            .append("name", name)
            .append("body", body)
            .append("tags", tags)
            .append("status", status)
            .append("crid", getCrid())
            .append("label", label)
            .toString();
    }
}
