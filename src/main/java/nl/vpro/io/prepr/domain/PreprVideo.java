package nl.vpro.io.prepr.domain;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@JsonTypeName(PreprVideo.LABEL)
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class PreprVideo extends PreprAbstractMedia {

    public static final String LABEL = "Video";

    int height;

    int width;


}
