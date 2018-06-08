package nl.vpro.io.mediaconnect.domain;

import java.io.IOException;

import org.junit.Test;

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

}
