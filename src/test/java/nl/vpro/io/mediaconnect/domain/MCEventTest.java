package nl.vpro.io.mediaconnect.domain;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Slf4j
public class MCEventTest {




    @Test
    public void unmarshal() throws IOException {
        MCEvent event  = MCObjectMapper.INSTANCE.readerFor(MCEvent.class).readValue(getClass().getResourceAsStream("/mcevent.json"));


        assertThat(event.getTimelines()).hasSize(2);


        log.info("{}", event);
    }


}
