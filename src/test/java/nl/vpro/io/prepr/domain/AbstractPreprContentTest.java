package nl.vpro.io.prepr.domain;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Slf4j
public class AbstractPreprContentTest {

    {
        PreprObjectMapper.configureInstance(false);
    }
    @Test
    public void unmarshal() throws IOException {
        PreprPost post = PreprObjectMapper.INSTANCE.readerFor(AbstractPreprContent.class)
             .readValue(getClass().getResourceAsStream("/content.json"));
        log.info("{}", post);



    }

}
