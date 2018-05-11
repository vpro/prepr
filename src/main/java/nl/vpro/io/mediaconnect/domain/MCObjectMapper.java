package nl.vpro.io.mediaconnect.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * @author Michiel Meeuwissen
* @since 0.1
 */
public class MCObjectMapper extends ObjectMapper {

    public static final MCObjectMapper INSTANCE = new MCObjectMapper();

    static {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        INSTANCE.registerModule(javaTimeModule);
    }
}
