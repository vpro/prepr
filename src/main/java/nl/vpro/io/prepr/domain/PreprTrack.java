package nl.vpro.io.prepr.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Seems to exist too. * @author Michiel Meeuwissen
 * @since 0.15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonTypeName(PreprTrack.LABEL)
public class PreprTrack extends AbstractPreprContent {
    public static final String LABEL = "Track";


}
