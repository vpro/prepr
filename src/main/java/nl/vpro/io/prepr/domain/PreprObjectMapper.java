package nl.vpro.io.prepr.domain;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import org.checkerframework.checker.nullness.qual.NonNull;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import nl.vpro.jackson2.*;

/**
 * Provides a jackson2 {@link ObjectMapper} configured to correctly read in the actually provided json by Prepr.
 *
 * Use {@link #INSTANCE}
 *
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Slf4j
public class PreprObjectMapper extends ObjectMapper {

    public static final PreprObjectMapper INSTANCE = new PreprObjectMapper();

    static {
        INSTANCE.registerModule(new JavaTimeModule());
        INSTANCE.registerModule(new PreprModule());
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

    public static class PreprModule extends SimpleModule {

        private static final long serialVersionUID = 1L;

        public PreprModule() {
            super(new Version(0, 3, 0, "", "nl.vpro", "prepr"));

            addDeserializer(Duration.class, DurationToJsonTimestamp.Deserializer.INSTANCE);
            addSerializer(Duration.class, DurationToJsonTimestamp.Serializer.INSTANCE);

            addDeserializer(LocalDateTime.class, LocalDateTimeToJsonDateWithSpace.Deserializer.INSTANCE);
            addSerializer(LocalDateTime.class, LocalDateTimeToJsonDateWithSpace.Serializer.INSTANCE);

            addDeserializer(Boolean.class, LenientBooleanDeserializer.INSTANCE);
            addDeserializer(boolean.class, LenientBooleanDeserializer.INSTANCE);

        }
    }

    @NonNull
    public static <T> Optional<T> unmap(@NonNull JsonNode payload, @NonNull Class<T> clazz) {
        try {
            return Optional.ofNullable(INSTANCE.treeToValue(payload, clazz));
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
