package nl.vpro.io.prepr.domain;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import org.junit.Test;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Slf4j
public class PreprCoverTest {

    static {
        PreprObjectMapper.configureInstance(false);
    }

    @Test
    public void unmarshal() throws IOException {
        PreprCover cover = PreprObjectMapper.INSTANCE.readerFor(PreprCover.class)
             .readValue(getClass().getResourceAsStream("/mccover.json"));
        log.info("{}", cover);



    }

}
