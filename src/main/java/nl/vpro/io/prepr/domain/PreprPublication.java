package nl.vpro.io.prepr.domain;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonTypeName(PreprPublication.LABEL)
public  class PreprPublication extends PreprAbstractObject {

    public static final String LABEL = "Publication";

    private JsonNode slug;
    private JsonNode publish_on;
}
