package nl.vpro.io.prepr.domain;

import lombok.Data;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author Michiel Meeuwissen
* @since 0.1
 */
@Data
public class PreprError {

    int code;

    String message;

    JsonNode errors;
}
