package nl.vpro.io.prepr.domain;

import lombok.EqualsAndHashCode;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Michiel Meeuwissen
 * @since 0.13
 */
@EqualsAndHashCode(callSuper = true)
@JsonTypeName(PreprProfilePhoto.LABEL)
public class PreprProfilePhoto extends PreprAsset {

    public static final String LABEL = "ProfilePhoto";

}
