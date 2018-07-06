package nl.vpro.io.mediaconnect.domain;

import java.io.IOException;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
public class MCWebhookTest {


    @Test
    public void unmarshal() throws IOException {
        String example = " {\n" +
            "      \"id\": \"c2f84c7f-c50d-4088-a74e-84116b81e753\",\n" +
            "      \"created_on\": \"2018-06-05T18:07:46+00:00\",\n" +
            "      \"changed_on\": null,\n" +
            "      \"label\": \"Webhook\",\n" +
            "      \"callback_url\": \"https:\\/\\/api-dev.poms.omroep.nl\\/mediaconnect\\/RAD5\",\n" +
            "      \"events\": [\n" +
            "        \"showschedule.deleted\",\n" +
            "        \"showschedule.created\",\n" +
            "        \"showschedule.changed\"\n" +
            "      ]\n" +
            "    }";

        MCWebhook webhook = MCObjectMapper.INSTANCE.readerFor(MCWebhook.class)
            .readValue(example);
        assertThat(webhook.getEvents()).hasSize(3);


    }


}
