package nl.vpro.io.mediaconnect.domain;

import java.io.IOException;

import org.junit.Test;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class MCPostTest {

    @Test
    public void unmarshal() throws IOException {
         MCPost post = MCObjectMapper.INSTANCE.readerFor(MCPost.class)
             .readValue(getClass().getResourceAsStream("/mcpost.json"));




    }
}
