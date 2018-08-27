package nl.vpro.io.mediaconnect.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonTypeName("Post")
public class MCPost extends MCContent {

    String slug;

    JsonNode custom;

    String length;

    String tease;

    MCTimeline container;

    @JsonProperty("element")
    List<MCAbstractObject> elements;

    MCChannel channel;

    List<MCTag> tags;
}
