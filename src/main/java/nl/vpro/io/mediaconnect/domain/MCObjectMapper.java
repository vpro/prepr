package nl.vpro.io.mediaconnect.domain;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
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
public class MCObjectMapper extends ObjectMapper {

    public static final MCObjectMapper INSTANCE = new MCObjectMapper();

    static {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        INSTANCE.registerModule(javaTimeModule);
        INSTANCE.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        INSTANCE.configure(JsonParser.Feature.ALLOW_COMMENTS, true); // always nice for examples
        INSTANCE.configure(JsonParser.Feature.IGNORE_UNDEFINED, true); // forward compatibility
        // We sometimes see for arrays : ""
        INSTANCE.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        INSTANCE.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);


    }
}
