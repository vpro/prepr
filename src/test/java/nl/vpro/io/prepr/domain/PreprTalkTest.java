package nl.vpro.io.prepr.domain;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Slf4j
public class PreprTalkTest {

    @BeforeEach
    public void setup() {
        PreprObjectMapper.configureInstance(false);
    }

    @Test
    public void unmarshal() throws IOException {
        PreprTalk talk = PreprObjectMapper.INSTANCE.readerFor(PreprTalk.class)
            .readValue(getClass().getResourceAsStream("/mctalk.json"));
        log.info("{}", talk);
    }
}

