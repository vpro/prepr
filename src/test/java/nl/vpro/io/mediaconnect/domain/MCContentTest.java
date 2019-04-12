package nl.vpro.io.mediaconnect.domain;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import org.junit.Test;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Slf4j
public class MCContentTest {

    {
        MCObjectMapper.configureInstance(false);
    }
    @Test
    public void unmarshal() throws IOException {
        MCPost post = MCObjectMapper.INSTANCE.readerFor(MCContent.class)
             .readValue(getClass().getResourceAsStream("/mccontent.json"));
        log.info("{}", post);



    }

}
