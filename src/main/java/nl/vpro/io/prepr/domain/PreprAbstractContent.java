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
public abstract class PreprAbstractContent extends PreprAbstractObject {

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
     *
     */
    String slug;

    /**
     *
     * This custom field may for example contain radiobox 'times' objects.
     */
    JsonNode custom;

    @JsonProperty("element")
    List<PreprAbstractObject> elements;

    /**
     * Around jan 2023 I noticed that the json property renamed from 'element' to 'elements'. I don't know if
     * the 'element' version can still happen too?
     *
     */
   /* @JsonProperty("elements")
    @Deprecated
    public void setElements_(List<PreprAbstractObject> elements) {
        this.elements = elements;
    }
*/

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
