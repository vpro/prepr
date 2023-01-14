package nl.vpro.io.prepr.domain;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Slf4j
public class PreprEventTest {


    @BeforeEach
    public void init() {
        PreprObjectMapper.configureInstance(false);
    }

    @Test
    public void unmarshal() throws IOException {
        PreprEvent event  = PreprObjectMapper.INSTANCE.readerFor(PreprEvent.class).readValue(getClass().getResourceAsStream("/event.json"));


        assertThat(event.getTimelines()).hasSize(2);


        log.info("{}", event);
    }



    @Test
    public void unmarshal2() throws IOException {
        PreprEvent event  = PreprObjectMapper.INSTANCE.readerFor(PreprEvent.class).readValue(getClass().getResourceAsStream("/event2.json"));


        assertThat(event.getTimelines()).hasSize(2);

        PreprAbstractContent abstractPreprContent = event.getTimelines().get(1).getPublications().get(7);
        assertThat(abstractPreprContent).isInstanceOf(PreprTalk.class);
        assertThat(((PreprTalk) abstractPreprContent).getPrivate()).isEqualTo("0");
        log.info("{}", event);
    }


}
