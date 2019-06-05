package nl.vpro.io.prepr.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonTypeName("Post")
public class PreprPost extends PreprContent {



     /**
     * What does this mean?
     */
    String length;

     /**
     * What does this mean?
     */
    String tease;

     /**
     * We map the Timeline with a Broadcast (a Program of ProgramType.BROADCAST)
     */
    PreprTimeline container;

    PreprChannel channel;

    List<PreprTag> tags;
}
