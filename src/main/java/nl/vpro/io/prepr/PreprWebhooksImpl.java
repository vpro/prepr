package nl.vpro.io.prepr;

import lombok.SneakyThrows;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;

import nl.vpro.io.prepr.domain.PreprItems;
import nl.vpro.io.prepr.domain.PreprObjectMapper;
import nl.vpro.io.prepr.domain.PreprWebhook;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
@Named
public class PreprWebhooksImpl implements PreprWebhooks {

    private  final PreprRepositoryClient impl;

    @Inject
    public PreprWebhooksImpl(PreprRepositoryClient impl) {
        this.impl = impl;
    }

    @SuppressWarnings("unchecked")
    @Override
    public PreprItems<PreprWebhook> get(Paging paging) {
        GenericUrl url = impl.createUrl("webhooks");
        impl.addListParameters(url, paging);
        return impl.get(url, PreprItems.class);
    }

    @Override
    @SneakyThrows(IOException.class)
    public PreprWebhook create(String callback_url, String... events)  {
        GenericUrl url = impl.createUrl("webhooks");
        Map<String, Object> post = new HashMap<>();
        post.put("callback_url", callback_url);
        post.put("events", events);
        post.put("active", "1");



        HttpResponse response = impl.post(url, post);
        return PreprObjectMapper.INSTANCE.readerFor(PreprWebhook.class)
            .readValue(response.getContent());
    }

    @Override
    public void delete(UUID webhook) {
        GenericUrl url = impl.createUrl("webhooks", webhook);
        impl.delete(url);
    }
}
