package nl.vpro.io.prepr.domain;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import nl.vpro.jackson2.DurationToJsonTimestamp;
import nl.vpro.jackson2.LocalDateTimeToJsonDateWithSpace;

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

    static {
        INSTANCE.registerModule(new JavaTimeModule());
        INSTANCE.registerModule(new MCModule());
        configureInstance(true);
    }

    public static void configureInstance(boolean lenient) {

        INSTANCE.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        INSTANCE.configure(JsonParser.Feature.ALLOW_COMMENTS, true); // always nice for examples
        if (lenient) {
            INSTANCE.configure(JsonParser.Feature.IGNORE_UNDEFINED, true); // forward compatibility
            INSTANCE.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);// forward compatibility
        } else {
            INSTANCE.configure(JsonParser.Feature.IGNORE_UNDEFINED, false);
            INSTANCE.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        }
        // We sometimes see for arrays : ""
        INSTANCE.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        INSTANCE.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

    }

    public static class MCModule extends SimpleModule {

        private static final long serialVersionUID = 1L;

        public MCModule() {
            super(new Version(0, 3, 0, "", "nl.vpro", "prepr"));

            addDeserializer(Duration.class, DurationToJsonTimestamp.Deserializer.INSTANCE);
            addSerializer(Duration.class, DurationToJsonTimestamp.Serializer.INSTANCE);

            addDeserializer(LocalDateTime.class, LocalDateTimeToJsonDateWithSpace.Deserializer.INSTANCE);
            addSerializer(LocalDateTime.class, LocalDateTimeToJsonDateWithSpace.Serializer.INSTANCE);
        }
    }

    public static <T> Optional<T> unmap(JsonNode payload, Class<T> clazz) {
        try {
            return Optional.of(INSTANCE.treeToValue(payload, clazz));
        } catch (JsonProcessingException e) {
            if (log.isDebugEnabled()) {
                log.warn("Could not unmap \n{}\n to {}: {}", payload, clazz, e.getMessage(), e);
            } else {
                log.warn("Could not unmap \n{}\n to {}: {}", payload, clazz, e.getMessage());
            }
            return Optional.empty();
        }

    }
}
