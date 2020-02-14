package nl.vpro.io.prepr.domain;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;
import com.google.common.collect.Range;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonTypeName(PreprTimeline.LABEL)
@JsonDeserialize(converter= PreprTimeline.Deserializer.class)
public class PreprTimeline extends PreprContent {
    public static final String LABEL = "Timeline";

    String timecode;

    LocalDateTime from;

    LocalDateTime until;

    List<PreprAbstractMedia> assets;

    List<PreprContent> publications;

    String show_id;

    Long offset;

    String timeline_hash;

    String prev_reference_id;

    String prev_show_id;


    public Range<LocalDateTime> asRange() {
        return Range.closedOpen(from, until);
    }

    @Override
    public String toString() {
        return "PreprTimeline{" +
            "timecode='" + timecode + '\'' +
            ", from=" + from +
            ", until=" + until +
            ", show_id='" + show_id + '\'' +
            ", offset=" + offset +
            ", timeline_hash='" + timeline_hash + '\'' +
            ", reference_id='" + reference_id + '\'' +
            ", status=" + status +
            ", duration=" + duration +
            ", id='" + id + '\'' +
            ", created_on=" + created_on +
            ", changed_on=" + changed_on +
            ", body='" + body + '\'' +
            ", description='" + description + '\'' +
            '}';
    }

    /**
     * We want the publication to be in a logical order. That is, the first one first.
     */

    public static class Deserializer extends StdConverter<PreprTimeline, PreprTimeline> {

        @Override
        public PreprTimeline convert(PreprTimeline mcTimeline) {
            if (mcTimeline != null) {
                if (mcTimeline.publications != null) {
                    mcTimeline.publications.removeIf(Objects::isNull);
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
