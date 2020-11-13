package nl.vpro.io.prepr.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonTypeName(PreprPhoto.LABEL)
public class PreprPhoto extends PreprAsset {
    public static final String LABEL = "Photo";

    String extension;

    String mime_type;

    int height;

    int width;


}
