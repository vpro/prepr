package nl.vpro.io.prepr.domain;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Seems to exist too. * @author Michiel Meeuwissen
 * @since 1.0
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@JsonTypeName(PreprTrack.LABEL)
public class PreprTrack extends PreprAbstractContent {

    public static final String LABEL = "Track";

}
