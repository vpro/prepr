package nl.vpro.io.mediaconnect.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;
import com.google.common.collect.Range;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonTypeName("Timeline")
@JsonDeserialize(converter= MCTimeline.Deserializer.class)
public class MCTimeline extends MCContent {

    String timecode;

    LocalDateTime from;

    LocalDateTime until;

    List<MCAbstractMedia> assets;

    List<MCContent> publications;

    String show_id;

    Long offset;

    String timeline_hash;

    String prev_reference_id;

    String prev_show_id;


    public Range<LocalDateTime> asRange() {
        return Range.closedOpen(from, until);
    }


    /**
     * We want the publication to be in a logical order. That is, the first one first.
     */

    public static class Deserializer extends StdConverter<MCTimeline, MCTimeline> {

        @Override
        public MCTimeline convert(MCTimeline mcTimeline) {
            if (mcTimeline != null) {
                if (mcTimeline.publications != null) {
                    mcTimeline.publications.sort((t1, t2) -> Objects.compare(t1.getPublished_on(), t2.getPublished_on(), Comparator.naturalOrder()));
                }
                if (mcTimeline.assets != null) {
                    mcTimeline.assets.sort((t1, t2) -> {
                        if (t1.getCustom() != null && t2.getCustom() != null) {
                            int result = Objects.compare(t1.getCustom().getType(), t2.getCustom().getType(), Comparator.naturalOrder());
                            if (result != 0) {
                                return result;
                            }
                        }
                        return Objects.compare(t1.getCreated_on(), t2.getChanged_on(), Comparator.naturalOrder());
                    });
                }
            }
            return mcTimeline;
        }
    }
}
