package nl.vpro.io.prepr;

import java.time.LocalDateTime;
import java.util.UUID;

import org.checkerframework.checker.nullness.qual.NonNull;

import com.google.api.client.http.GenericUrl;

import nl.vpro.io.prepr.domain.PreprContent;
import nl.vpro.io.prepr.domain.PreprItems;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class PreprContentImpl implements nl.vpro.io.prepr.PreprContent {

    public static final Fields PUBLICATION_FIELDS = Fields.builder()
        .fs("container", "tags", "custom", "channel")
        .field(Field.builder("element")
            .fs("custom", "tags")
            .field(
                Field.builder("media")
                    .field(Fields.SOURCEFILE_FIELD)
                    .field(Fields.CDN_FILES)
                    .fs("custom", "tags")
                    .build()
            )
            .build()
        )
    .build();

    private  final PreprRepositoryClient impl;

    public PreprContentImpl(PreprRepositoryClient impl) {
        this.impl = impl;
    }

    @Override
    public PreprItems<?> getPublicationsForChannel(
        Paging paging, @NonNull UUID channel, LocalDateTime event_from, LocalDateTime event_until) {
        GenericUrl url = impl.createUrl("publications");
        impl.addListParameters(url, paging);
        url.set("channel_id", channel);
        //url.set("label", "Post");
        if (event_from != null) {
            url.set("event_from", event_from);
        }
        if (event_until != null) {
            url.set("event_until", event_until);
        }
        return impl.get(url, PreprItems.class);
    }



    @SuppressWarnings("unchecked")
    @Override
    public  <T extends PreprContent> T getPublication(
        @NonNull  UUID id) {
        GenericUrl url = impl.createUrl("publications", id.toString());

        url.set("fields", PUBLICATION_FIELDS);
        T result = (T) impl.get(url, PreprContent.class);
        return result;
    }




    @Override
    public PreprItems<?> getChannels(Paging paging) {
        GenericUrl url = impl.createUrl("channels");
        impl.addListParameters(url, paging);
        return impl.get(url, PreprItems.class);


    }

    @Override
    public PreprItems<?> getContainers(Paging paging) {
        GenericUrl url = impl.createUrl("containers");
        impl.addListParameters(url, paging);
        url.set("fields", PUBLICATION_FIELDS);

        return impl.get(url, PreprItems.class);
    }

    @Override
    public <T extends PreprContent> T getContainer(UUID id) {
        GenericUrl url = impl.createUrl("containers", id.toString());
        //url.set("label", "Post");

        url.set("fields", PUBLICATION_FIELDS);
        T result = (T) impl.get(url, PreprContent.class);
        return result;

    }

}
