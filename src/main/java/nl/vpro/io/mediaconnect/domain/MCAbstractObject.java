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
    @JsonSubTypes.Type(value = MCPhoto.class, name = "Photo"),
    @JsonSubTypes.Type(value = MCVideo.class, name = "Video"),
    @JsonSubTypes.Type(value = MCAudio.class, name = "Audio"),
    @JsonSubTypes.Type(value = MCPost.class, name = "Post")


})
public class MCAbstractObject  {

    public static String CRID_PREFIX =  "crid://mediaconnect.io/";

    UUID id;

    Instant created_on;

    Instant changed_on;

    Instant last_seen;

    String label;

    public String getCrid() {
        String label = getLabel();
        return label == null ? null : CRID_PREFIX + getLabel().toLowerCase() + "/" + getId();
    }
}
