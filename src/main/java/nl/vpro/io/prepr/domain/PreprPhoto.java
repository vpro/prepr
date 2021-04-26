package nl.vpro.io.prepr.domain;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@JsonTypeName(PreprPhoto.LABEL)
public class PreprPhoto extends PreprAsset {

    public static final String LABEL = "Photo";

    int height;

    int width;

}
