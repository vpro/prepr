package nl.vpro.io.prepr.domain;

import lombok.*;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.annotations.Beta;

/**
 * @author Michiel Meeuwissen
 * @since 1.6
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@JsonTypeName(PreprPublicationModel.LABEL)
public  class PreprPublicationModel extends PreprAbstractObject {

    public static final String LABEL = "PublicationModel";

    private String body_singular;
    private String body_plural;

    private PreprStatus status;

    private Boolean stories;
    private Boolean timelines;
    private Boolean allow_stories;
    private Boolean channels;
    private Boolean allow_channels;
    private Boolean channels_required;
    private Boolean container_required;
    private Boolean seo_score;
    private Boolean ab_testing;
    private Boolean versioning;

    private Instant until;

    private JsonNode slug;

    @JsonProperty("for")
    @Beta
    private JsonNode for_;





}
