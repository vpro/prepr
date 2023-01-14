package nl.vpro.io.prepr.domain;

import lombok.*;

import java.time.Duration;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@JsonTypeName(PreprTrackPlay.LABEL)
public class PreprTrackPlay extends PreprAbstractContent {

    public static final String LABEL = "TrackPlay";

    private String artist;

    private String artist_text;

    private String reference_id;

    private String note;

    private Duration duration;

}
