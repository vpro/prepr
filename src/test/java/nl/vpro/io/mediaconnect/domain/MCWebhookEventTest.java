package nl.vpro.io.mediaconnect.domain;

import java.io.IOException;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
public class MCWebhookEventTest {


    @Test
    public void unmarshalChanged() throws IOException {


        MCWebhookEvent webhook = MCObjectMapper.INSTANCE.readerFor(MCWebhookEvent.class)
            .readValue(getClass().getResourceAsStream("/webhook.scheduleevent.changed.json"));


    }


    @Test
    public void unmarshalCreated() throws IOException {


        MCWebhookEvent webhook = MCObjectMapper.INSTANCE.readerFor(MCWebhookEvent.class)
            .readValue(getClass().getResourceAsStream("/webhook.scheduleevent.created.json"));


    }

     @Test
    public void unmarshalAnother() throws IOException {


        MCWebhookEvent webhook = MCObjectMapper.INSTANCE.readerFor(MCWebhookEvent.class)
            .readValue(getClass().getResourceAsStream("/mcwebhookevent.json"));

        assertThat(webhook.getPayload().get("label").textValue()).isEqualTo("TrackPlay");


    }

}
