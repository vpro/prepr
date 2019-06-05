package nl.vpro.io.prepr.domain;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Slf4j
public class PreprTalkTest {

    @Before
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

