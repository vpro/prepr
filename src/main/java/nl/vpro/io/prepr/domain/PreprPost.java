package nl.vpro.io.prepr.domain;

import lombok.*;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@JsonTypeName(PreprPost.LABEL)
public class PreprPost extends PreprAbstractContent {

    public static final String LABEL = "Post";

     /**
     * What does this mean?
     */
    String length;

     /**
     * What does this mean?
     */
    String tease;

     /**
     * We map the Timeline with a Broadcast (a {@code Program} of {@code ProgramType.BROADCAST})
     */
    PreprTimeline container;

    /**
     * Seems to be the same thing as container
     */
    PreprTimeline timeline;

    PreprChannel channel;

    List<PreprTag> tags;



}
