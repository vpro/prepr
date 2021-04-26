package nl.vpro.io.prepr.domain;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author Michiel Meeuwissen
 * @since 0.3
 */
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@JsonTypeName(PreprCdnFile.LABEL)
public class PreprCdnFile extends PreprAbstractObject {

    public static final String LABEL = "CdnFile";

    String cdn;
    String bucket;
    String profile;
    String file;
    String url;
    JsonNode resized;

}
