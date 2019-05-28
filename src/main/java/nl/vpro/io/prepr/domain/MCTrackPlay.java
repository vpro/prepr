package nl.vpro.io.prepr.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Duration;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonTypeName("TrackPlay")
public class MCTrackPlay extends MCContent {

    private String artist;

    private String artist_text;

    private String reference_id;

    private String note;

    private Duration duration;



}
