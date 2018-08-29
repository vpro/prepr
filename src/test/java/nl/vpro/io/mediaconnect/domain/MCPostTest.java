package nl.vpro.io.mediaconnect.domain;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.Duration;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Slf4j
public class MCPostTest {

    @Test
    public void unmarshal() throws IOException {
        MCPost post = MCObjectMapper.INSTANCE.readerFor(MCPost.class)
             .readValue(getClass().getResourceAsStream("/mcpost.json"));
        log.info("{}", post);



    }


    @Test
    public void unmarshal2() throws IOException {
        MCPost post = MCObjectMapper.INSTANCE.readerFor(MCPost.class)
             .readValue(getClass().getResourceAsStream("/mcpost2.json"));
        log.info("{}", post);



    }


    @Test
    public void unmarshal3() throws IOException {
        MCPost post = MCObjectMapper.INSTANCE.readerFor(MCPost.class)
             .readValue(getClass().getResourceAsStream("/mcpost3.json"));
        log.info("{}", post);
        MCAudio audio = (MCAudio) ((MCMedia) post.getElements().get(2)).getContent().get(0);
        assertThat(audio.getDuration()).isEqualTo(Duration.ofMinutes(1));


    }
}
