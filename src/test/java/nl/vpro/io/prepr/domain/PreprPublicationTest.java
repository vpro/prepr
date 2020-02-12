package nl.vpro.io.prepr.domain;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
@Slf4j
class PreprPublicationTest {


	@Test
    public void unmarshal() throws IOException {
        PreprPublication post = PreprObjectMapper.INSTANCE.readerFor(PreprPublication.class)
             .readValue(getClass().getResourceAsStream("/publication.json"));
        log.info("{}", post);



    }


}
