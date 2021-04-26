package nl.vpro.io.prepr.domain;

import lombok.*;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.base.MoreObjects;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Getter
@Setter
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
    MoreObjects.ToStringHelper toStringHelper() {
        return super.toStringHelper()
            .add("slug", slug)
            .add("name", name)
            .add("body", body)
            .add("tags", tags)
            .add("status", status)
            .add("crid", getCrid())
            .add("label", label);
    }
}
