package nl.vpro.io.prepr.domain;

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
public class PreprPostTest {

    @Test
    public void unmarshal() throws IOException {
        PreprPost post = PreprObjectMapper.INSTANCE.readerFor(PreprPost.class)
             .readValue(getClass().getResourceAsStream("/post.json"));
        log.info("{}", post);



    }


    @Test
    public void unmarshal2() throws IOException {
        PreprPost post = PreprObjectMapper.INSTANCE.readerFor(PreprPost.class)
             .readValue(getClass().getResourceAsStream("/post2.json"));
        log.info("{}", post);



    }


    @Test
    public void unmarshal3() throws IOException {
        PreprPost post = PreprObjectMapper.INSTANCE.readerFor(PreprPost.class)
             .readValue(getClass().getResourceAsStream("/post3.json"));
        log.info("{}", post);
        PreprAudio audio = (PreprAudio) ((PreprMedia) post.getElements().get(2)).getContent().get(0);
        assertThat(audio.getDuration()).isEqualTo(Duration.ofMinutes(1));


    }
}
