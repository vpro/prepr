package nl.vpro.io.prepr.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Duration;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Michiel Meeuwissen
 * @since 0.3
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonTypeName(PreprImaging.LABEL)
public class PreprImaging extends PreprContent {
    public static final String LABEL = "Imaging";


    Duration duration;






}
