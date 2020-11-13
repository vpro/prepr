package nl.vpro.io.prepr.domain;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import org.junit.jupiter.api.Test;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
@Slf4j
class PreprPhotoTest {

    static {
        PreprObjectMapper.configureInstance(false);
    }
    @Test
    public void unmarshal() throws IOException {
        PreprPhoto post = PreprObjectMapper.INSTANCE.readerFor(PreprPhoto.class)
             .readValue(getClass().getResourceAsStream("/photo.json"));
        log.info("{}", post);



    }

}
