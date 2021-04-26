package nl.vpro.io.prepr.domain;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Michiel Meeuwissen
 * @since 0.9
 */
@JsonTypeName(PreprCover.LABEL)
public class PreprCover extends PreprAsset {

    public static final String LABEL = "Cover";


}
