package nl.vpro.io.prepr.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.*;

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
@JsonTypeName(PreprTimeline.LABEL)
@JsonDeserialize(converter= PreprTimeline.Deserializer.class)
@Slf4j
public class PreprTimeline extends AbstractPreprContent {
    public static final String LABEL = "Timeline";

    String timecode;

    LocalDateTime from;

    LocalDateTime prev_from;

    LocalDateTime until;

    LocalDateTime prev_until;

    List<PreprAbstractMedia> assets;

    List<AbstractPreprContent> publications;

    String show_id;

    Long offset;

    String timeline_hash;

    String prev_reference_id;

    String prev_show_id;

    String rule_id;

    String timeline_constraint_hash;

    String clock_id;

    PreprPhoto cover;


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
                    try {
                        mcTimeline.publications.removeIf(Objects::isNull);
                        mcTimeline.publications.sort(Comparator.nullsLast(Comparator.comparing(AbstractPreprContent::getPublished_on, Comparator.nullsLast(Comparator.naturalOrder()))));
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
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
