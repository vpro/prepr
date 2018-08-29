package nl.vpro.io.mediaconnect.domain;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Provides a jackson2 {@link ObjectMapper} configured to correctly read in the acutally provided json by MediaConnect.
 *
 * Use {@link #INSTANCE}
 *
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Slf4j
public class MCObjectMapper extends ObjectMapper {

    public static final MCObjectMapper INSTANCE = new MCObjectMapper();

    private static  boolean lenient = false;

    static {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        INSTANCE.registerModule(javaTimeModule);
        INSTANCE.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        INSTANCE.configure(JsonParser.Feature.ALLOW_COMMENTS, true); // always nice for examples
        if (lenient) {
            INSTANCE.configure(JsonParser.Feature.IGNORE_UNDEFINED, true); // forward compatibility
            INSTANCE.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);// forward compatibility
        }
        // We sometimes see for arrays : ""
        INSTANCE.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        INSTANCE.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);


    }

    public static <T> Optional<T> unmap(JsonNode payload, Class<T> clazz) {
        try {
            return Optional.of(INSTANCE.treeToValue(payload, clazz));
        } catch (JsonProcessingException e) {
            log.error("Could not unmap {} to {}: {}", payload, clazz, e.getMessage(), e);
            return Optional.empty();
        }

    }
}
