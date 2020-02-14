package nl.vpro.io.prepr.domain;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.UUID;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Data
@JsonTypeInfo(
    visible = true,
    include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
    use = JsonTypeInfo.Id.NAME, property="label",
    defaultImpl = Void.class
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = PreprWebhook.class,     name = PreprWebhook.LABEL),
    @JsonSubTypes.Type(value = PreprTimeline.class,    name = PreprTimeline.LABEL),
    @JsonSubTypes.Type(value = PreprShow.class,        name = PreprShow.LABEL),
    @JsonSubTypes.Type(value = PreprShowDetail.class,  name = PreprShowDetail.LABEL),
    @JsonSubTypes.Type(value = PreprPhoto.class,       name = PreprPhoto.LABEL),
    @JsonSubTypes.Type(value = PreprProfilePhoto.class,name = PreprProfilePhoto.LABEL),
    @JsonSubTypes.Type(value = PreprVideo.class,       name = PreprVideo.LABEL),
    @JsonSubTypes.Type(value = PreprAudio.class,       name = PreprAudio.LABEL),
    @JsonSubTypes.Type(value = PreprCover.class,       name = PreprCover.LABEL),
    @JsonSubTypes.Type(value = PreprPost.class,        name = PreprPost.LABEL),
    @JsonSubTypes.Type(value = PreprHeading.class,     name = PreprHeading.LABEL),
    @JsonSubTypes.Type(value = PreprText.class,        name = PreprText.LABEL),
    @JsonSubTypes.Type(value = PreprMedia.class,       name = PreprMedia.LABEL),
    @JsonSubTypes.Type(value = PreprTrackPlay.class,   name = PreprTrackPlay.LABEL),
    @JsonSubTypes.Type(value = PreprChannel.class,     name = PreprChannel.LABEL),
    @JsonSubTypes.Type(value = PreprNewsCast.class,    name = PreprNewsCast.LABEL),
    @JsonSubTypes.Type(value = PreprTag.class,         name = PreprTag.LABEL),
    @JsonSubTypes.Type(value = PreprTagGroup.class,    name = PreprTagGroup.LABEL),
    @JsonSubTypes.Type(value = PreprGuide.class,       name = PreprGuide.LABEL),
    @JsonSubTypes.Type(value = PreprImaging.class,     name = PreprImaging.LABEL),
    @JsonSubTypes.Type(value = PreprTalk.class,        name = PreprTalk.LABEL),
    @JsonSubTypes.Type(value = PreprTrafficTalk.class, name = PreprTrafficTalk.LABEL),
    @JsonSubTypes.Type(value = PreprWeatherTalk.class, name = PreprWeatherTalk.LABEL),
    @JsonSubTypes.Type(value = PreprCommercial.class,  name = PreprCommercial.LABEL),
    @JsonSubTypes.Type(value = PreprNewsBulletin.class,name = PreprNewsBulletin.LABEL),
    @JsonSubTypes.Type(value = PreprPublication.class,name = PreprPublication.LABEL),
    @JsonSubTypes.Type(value = PreprPublished_NLNL.class,name = PreprPublished_NLNL.LABEL),
    @JsonSubTypes.Type(value = PreprNLNL.class,name = PreprNLNL.LABEL)


})
@Slf4j
public class PreprAbstractObject {

    public static final String CRID_PREFIX =  "crid://prepr.io/";

    /**
     * Ik dacht dat ids altijd UUID's waren?
     *
     *
     * Tim Hanssen [10:28 AM]
     * In principe wel, tenzij ze uit een import komen. Bij FunX is dat het geval.
     */
    String id;

    Instant created_on;

    Instant changed_on;

    Instant last_seen;

    String label;

    String body;

    String description;

    public String getCrid() {
        String label = getLabel();
        if (label == null) {
            log.warn("No label for {}", this);
        }
        return label == null ? null : CRID_PREFIX + getLabel().toLowerCase() + "/" + getId();
    }


    public UUID getUUID() {
        return UUID.fromString(id);
    }

    @Override
    public String toString() {
        return getCrid();
    }
}
