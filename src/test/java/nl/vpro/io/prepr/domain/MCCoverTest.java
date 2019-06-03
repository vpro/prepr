package nl.vpro.io.prepr.domain;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import org.junit.Test;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Slf4j
public class MCCoverTest {

    static {
        MCObjectMapper.configureInstance(false);
    }

    @Test
    public void unmarshal() throws IOException {
        MCCover cover = MCObjectMapper.INSTANCE.readerFor(MCCover.class)
             .readValue(getClass().getResourceAsStream("/mccover.json"));
        log.info("{}", cover);



    }

}
