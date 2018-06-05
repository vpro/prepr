package nl.vpro.io.mediaconnect.domain;

import java.io.IOException;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class MCItemsTest {

    @Test
    public void unmarshal() throws IOException {
         MCItems<MCWebhook> webhooks = MCObjectMapper.INSTANCE.readerFor(MCItems.class)
             .readValue(getClass().getResourceAsStream("/webhooks.json"));


        assertThat(webhooks.getItems()).hasSize(9);


    }
}
