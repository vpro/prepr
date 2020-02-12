package nl.vpro.io.prepr.domain;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.EqualsAndHashCode;

/**
 * @author Michiel Meeuwissen
 * @since 0.13
 */
@EqualsAndHashCode(callSuper = true)
@JsonTypeName(PreprProfilePhoto.LABEL)
public class PreprProfilePhoto extends PreprAsset {
    public static final String LABEL = "ProfilePhoto";





}
