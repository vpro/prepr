package nl.vpro.io.prepr.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractPreprContent extends PreprAbstractObject {

    Instant published_on;

    Instant unpublish_on;

    String reference_id;

    PreprStatus status;

    /**
     * What does this mean?
     */
    String slug;

    /**
     * What does this mean?
     *
     * This custom field may for example contains radiobox 'times' objects.
     */
    JsonNode custom;

    @JsonProperty("element")
    List<PreprAbstractObject> elements;

    Duration duration;


}
