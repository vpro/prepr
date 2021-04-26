package nl.vpro.io.prepr.domain;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@JsonTypeName(PreprPublication.LABEL)
public  class PreprPublication extends PreprAbstractObject {

    public static final String LABEL = "Publication";

    private JsonNode slug;
    private JsonNode publish_on;
}
