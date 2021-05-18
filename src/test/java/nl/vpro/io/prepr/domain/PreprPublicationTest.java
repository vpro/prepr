package nl.vpro.io.prepr.domain;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import org.junit.jupiter.api.Test;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
@Slf4j
class PreprPublicationTest {

	static {
		PreprObjectMapper.configureInstance(false);
	}
	@Test
    public void unmarshal() throws IOException {
        PreprPublication post = PreprObjectMapper.INSTANCE.readerFor(PreprPublication.class)
             .readValue(getClass().getResourceAsStream("/publication.json"));
        log.info("{}", post);



    }


}
