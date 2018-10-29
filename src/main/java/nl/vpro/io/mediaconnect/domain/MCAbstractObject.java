package nl.vpro.io.mediaconnect.domain;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Data
@JsonTypeInfo(
    visible = true,
    include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
    use = JsonTypeInfo.Id.NAME, property="label")
@JsonSubTypes({
    @JsonSubTypes.Type(value = MCWebhook.class, name = "Webhook"),
    @JsonSubTypes.Type(value = MCTimeline.class, name = "Timeline"),
    @JsonSubTypes.Type(value = MCShow.class, name = "Show"),
    @JsonSubTypes.Type(value = MCShowDetail.class, name = "ShowDetail"),
    @JsonSubTypes.Type(value = MCPhoto.class, name = "Photo"),
    @JsonSubTypes.Type(value = MCVideo.class, name = "Video"),
    @JsonSubTypes.Type(value = MCAudio.class, name = "Audio"),
    @JsonSubTypes.Type(value = MCPost.class, name = "Post"),
    @JsonSubTypes.Type(value = MCHeading.class, name = "Heading"),
    @JsonSubTypes.Type(value = MCText.class, name = "Text"),
    @JsonSubTypes.Type(value = MCMedia.class, name = "Media"),
    @JsonSubTypes.Type(value = MCTrackPlay.class, name = "TrackPlay"),
    @JsonSubTypes.Type(value = MCChannel.class, name = "Channel"),
    @JsonSubTypes.Type(value = MCNewsCast.class, name = "NewsCast"),
    @JsonSubTypes.Type(value = MCTag.class, name = "Tag"),
    @JsonSubTypes.Type(value = MCTagGroup.class, name = "TagGroup"),
    @JsonSubTypes.Type(value = MCGuide.class, name = "Guide"),
    @JsonSubTypes.Type(value = MCImaging.class, name = "Imaging"),
    @JsonSubTypes.Type(value = MCTalk.class, name = "Talk"),
    @JsonSubTypes.Type(value = MCTrafficTalk.class, name = "TrafficTalk"),
    @JsonSubTypes.Type(value = MCWeatherTalk.class, name = "WeatherTalk"),
    @JsonSubTypes.Type(value = MCCommercial.class, name = "Commercial"),
    @JsonSubTypes.Type(value = MCNewsBulletin.class, name = "NewsBulletin")






})
public class MCAbstractObject  {

    public static String CRID_PREFIX =  "crid://mediaconnect.io/";

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

    public String getCrid() {
        String label = getLabel();
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
