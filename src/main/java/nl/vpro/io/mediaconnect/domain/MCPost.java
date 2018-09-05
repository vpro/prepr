package nl.vpro.io.mediaconnect.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonTypeName("Post")
public class MCPost extends MCContent {




    /**
     * This seems to contain MID's.
     */
    String reference_id;


     /**
     * What does this mean?
     */
    String length;

     /**
     * What does this mean?
     */
    String tease;

     /**
     * What does this mean?
     */
    MCTimeline container;

    @JsonProperty("element")
    List<MCAbstractObject> elements;

    MCChannel channel;

    List<MCTag> tags;
}
