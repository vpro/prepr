package nl.vpro.io.prepr.domain;

import lombok.*;

import java.time.Duration;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Michiel Meeuwissen
 * @since 0.3
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@JsonTypeName(PreprImaging.LABEL)
public class PreprImaging extends PreprAbstractContent {

    public static final String LABEL = "Imaging";

    Duration duration;

}
