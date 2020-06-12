package nl.vpro.io.prepr.domain;

import lombok.*;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;

import nl.vpro.jackson2.LenientBooleanDeserializer;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@lombok.Builder
@JsonTypeName(PreprWebhook.LABEL)
public class PreprWebhook extends PreprAbstractObject {
    public static final String LABEL = "Webhook";

    String callback_url;

    @Singular
    List<String> events;

    @JsonDeserialize(using = LenientBooleanDeserializer.class)
    @Setter
    Boolean active;


    public PreprWebhook() {

    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .omitNullValues()
            .add("callback_url", callback_url)
            .add("id", id)
            .add("created_on", created_on)
            .add("active", active != null ? (active ? null : active) : null)
            .toString();
    }
}
