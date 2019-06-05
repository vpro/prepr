package nl.vpro.io.prepr.domain;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import org.junit.Test;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Slf4j
public class PreprContentTest {

    {
        PreprObjectMapper.configureInstance(false);
    }
    @Test
    public void unmarshal() throws IOException {
        PreprPost post = PreprObjectMapper.INSTANCE.readerFor(PreprContent.class)
             .readValue(getClass().getResourceAsStream("/mccontent.json"));
        log.info("{}", post);



    }

}
