package nl.vpro.io.prepr.domain;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@JsonTypeName(PreprAudio.LABEL)
public class PreprAudio extends PreprAbstractMedia {

    public static final String LABEL = "Audio";


}
