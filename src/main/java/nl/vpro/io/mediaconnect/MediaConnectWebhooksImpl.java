package nl.vpro.io.mediaconnect;

import lombok.SneakyThrows;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;

import nl.vpro.io.mediaconnect.domain.MCItems;
import nl.vpro.io.mediaconnect.domain.MCObjectMapper;
import nl.vpro.io.mediaconnect.domain.MCWebhook;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
public class MediaConnectWebhooksImpl implements MediaConnectWebhooks {

    private  final MediaConnectRepositoryImpl impl;

    public MediaConnectWebhooksImpl(MediaConnectRepositoryImpl impl) {
        this.impl = impl;
    }

    @SuppressWarnings("unchecked")
    @Override
    public MCItems<MCWebhook> get(Paging paging) {
        GenericUrl url = impl.createUrl("webhooks");
        impl.addListParameters(url, paging);
        return impl.get(url, MCItems.class);
    }

    @Override
    @SneakyThrows(IOException.class)
    public MCWebhook create(String callback_url, String... events)  {
        GenericUrl url = impl.createUrl("webhooks");
        Map<String, Object> post = new HashMap<>();
        post.put("callback_url", callback_url);
        post.put("events", events);

        HttpResponse response = impl.post(url, post);
        return MCObjectMapper.INSTANCE.readerFor(MCWebhook.class).readValue(response.getContent());
    }

    @Override
    public void delete(UUID webhook) {
        GenericUrl url = impl.createUrl("webhooks", webhook);
        impl.delete(url);
    }
}
