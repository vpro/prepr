package nl.vpro.io.prepr.domain;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import org.junit.jupiter.api.Test;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Slf4j
public class AbstractPreprContentTest {

    static {
        PreprObjectMapper.configureInstance(false);
    }
    @Test
    public void unmarshal() throws IOException {
        PreprPost post = PreprObjectMapper.INSTANCE.readerFor(AbstractPreprContent.class)
             .readValue(getClass().getResourceAsStream("/content.json"));
        log.info("{}", post);



    }

}
