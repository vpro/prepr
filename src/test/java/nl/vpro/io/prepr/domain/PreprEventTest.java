package nl.vpro.io.prepr.domain;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Slf4j
public class PreprEventTest {




    @Test
    public void unmarshal() throws IOException {
        PreprEvent event  = PreprObjectMapper.INSTANCE.readerFor(PreprEvent.class).readValue(getClass().getResourceAsStream("/mcevent.json"));


        assertThat(event.getTimelines()).hasSize(2);


        log.info("{}", event);
    }


}
