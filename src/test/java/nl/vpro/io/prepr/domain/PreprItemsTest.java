package nl.vpro.io.prepr.domain;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class PreprItemsTest {

    @Test
    public void unmarshal() throws IOException {
         PreprItems<PreprWebhook> webhooks = PreprObjectMapper.INSTANCE.readerFor(PreprItems.class)
             .readValue(getClass().getResourceAsStream("/webhooks.json"));


        assertThat(webhooks.getItems()).hasSize(9);


    }
}
