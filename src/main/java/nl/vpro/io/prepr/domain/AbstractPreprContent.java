package nl.vpro.io.prepr.domain;

import lombok.*;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractPreprContent extends PreprAbstractObject {

    Instant published_on;

    Instant unpublish_on;

    /**
     * Not known what this is. Introduced to fix warnings.
     * @since 2.0
     */
    long published_on_int;

    /**
     * Not known what this is. Introduced to fix warnings.
     * @since 2.0
     */
    long unpublish_on_int;

    String reference_id;

    String reference_unique;

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

    String name;

    String note;

    String artist_text;

    PreprChannel channel;

    List<PreprTag> tags;


    @JsonProperty("private")
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private String private_;

    public String getPrivate() {
        return private_;
    }

    public void setPrivate(String private_) {
        this.private_ = private_;
    }

}
