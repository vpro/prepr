package nl.vpro.io.prepr.domain;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.fasterxml.jackson.core.JsonProcessingException;

import static nl.vpro.io.prepr.domain.PreprObjectMapper.INSTANCE;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 */
@Slf4j
public class PreprWebhookEventTest {


    @BeforeAll
    public static void init() {
        PreprObjectMapper.configureInstance(false);
    }

    @Test
    public void unmarshalChanged() throws IOException {
        PreprWebhookEvent webhook = PreprObjectMapper.INSTANCE.readerFor(PreprWebhookEvent.class)
            .readValue(getClass().getResourceAsStream("/webhook.scheduleevent.changed.json"));
    }


    @Test
    public void unmarshalCreated() throws IOException {
        PreprWebhookEvent webhook = PreprObjectMapper.INSTANCE.readerFor(PreprWebhookEvent.class)
            .readValue(getClass().getResourceAsStream("/webhook.scheduleevent.created.json"));
    }

     @Test
    public void unmarshalAnother() throws IOException {
         PreprWebhookEvent webhook = PreprObjectMapper.INSTANCE.readerFor(PreprWebhookEvent.class)
             .readValue(getClass().getResourceAsStream("/webhookevent.json"));

         assertThat(webhook.getPayload().get("label").textValue()).isEqualTo("TrackPlay");
     }



    @Test
    public void unmarshalV6() throws IOException {
        PreprWebhookEvent webhook = PreprObjectMapper.INSTANCE.readerFor(PreprWebhookEvent.class)
            .readValue(getClass().getResourceAsStream("/webhook.v6.asset.changed.json"));

        assertThat(webhook.getPayload().get("label").textValue()).isEqualTo("Audio");
        assertThat(webhook.getDc()).isNotNull();

        PreprAudio audio = PreprObjectMapper.INSTANCE.treeToValue(webhook.getPayload(), PreprAudio.class);

        assertThat(audio.getAuthor()).isEqualTo("adf");
    }


    @ParameterizedTest
    @MethodSource("webhookExamples")
    public void testAllWebhooks(String fileName, String resource) throws JsonProcessingException {
        log.info("{}", fileName);
        PreprWebhookEvent webhook = INSTANCE.readerFor(PreprWebhookEvent.class)
            .readValue(resource);
        PreprAbstractObject object = INSTANCE.treeToValue(webhook.getPayload(), PreprAbstractObject.class);
        log.info("{}", object);
    }



    @SuppressWarnings("ConstantConditions")
    public static Stream<Arguments> webhookExamples() {

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource("webhooks");
        String path = url.getPath();
        return Arrays.stream(new File(path).listFiles())
            .sorted()
            .map(fn -> Arguments.of(fn.getName(), read(fn)));
    }

    @SneakyThrows
    private static String read(File f) {
        return Files.readString(f.toPath());
    }

}
