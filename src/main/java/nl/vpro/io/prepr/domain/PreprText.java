package nl.vpro.io.prepr.domain;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.annotations.Beta;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@JsonTypeName(PreprText.LABEL)
public class PreprText extends PreprAbstractObject {

    public static final String LABEL = "Text";


    @Beta
    JsonNode format;

}
