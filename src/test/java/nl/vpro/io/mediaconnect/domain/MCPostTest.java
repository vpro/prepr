package nl.vpro.io.mediaconnect.domain;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import org.junit.Test;

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



    }
}
