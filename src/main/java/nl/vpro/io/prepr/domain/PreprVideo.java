package nl.vpro.io.prepr.domain;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@JsonTypeName(PreprVideo.LABEL)
public class PreprVideo extends PreprAbstractMedia {
    public static final String LABEL = "Video";




}
