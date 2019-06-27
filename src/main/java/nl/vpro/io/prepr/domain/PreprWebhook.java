package nl.vpro.io.prepr.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Singular;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import nl.vpro.jackson2.LenientBooleanDeserializer;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Data
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
    Boolean active;


    public PreprWebhook() {

    }
}
