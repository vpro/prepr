package nl.vpro.io.prepr.domain;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
@Slf4j
class PreprVideoTest {

    @BeforeEach
    public void setup() {
        PreprObjectMapper.configureInstance(false);
    }

    @Test
    public void json() throws IOException {
        PreprVideo video = PreprObjectMapper.INSTANCE.readerFor(PreprVideo.class)
            .readValue(getClass().getResourceAsStream("/video.json"));
        log.info("{}", video);

    }

}
