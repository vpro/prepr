package nl.vpro.io.mediaconnect.domain;

import lombok.Data;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author Michiel Meeuwissen
* @since 0.1
 */
@Data
public class MCError {

    int code;

    String message;

    JsonNode errors;
}
