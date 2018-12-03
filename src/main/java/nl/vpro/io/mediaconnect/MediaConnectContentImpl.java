package nl.vpro.io.mediaconnect;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.annotation.Nonnull;

import com.google.api.client.http.GenericUrl;

import nl.vpro.io.mediaconnect.domain.MCContent;
import nl.vpro.io.mediaconnect.domain.MCItems;

import static nl.vpro.io.mediaconnect.Fields.CDN_FILES;
import static nl.vpro.io.mediaconnect.Fields.SOURCEFILE_FIELD;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class MediaConnectContentImpl implements MediaConnectContent {

    public static Fields PUBLICATION_FIELDS = Fields.builder()
        .fs("container", "tags", "custom")
        //.field(ASSETS)
        .field(Field.builder("element")
            .f("custom")
            .field(
                Field.builder("media")
                    .field(SOURCEFILE_FIELD)
                    .field(CDN_FILES)
                    .fs("custom")
                    .build()
            )
            .build()
        )
    .build();

    private  final MediaConnectRepositoryClient impl;

    public MediaConnectContentImpl(MediaConnectRepositoryClient impl) {
        this.impl = impl;
    }

    @Override
    public MCItems<?> getPublicationsForChannel(
        Paging paging, @Nonnull UUID channel, LocalDateTime event_from, LocalDateTime event_until) {
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
        return impl.get(url, MCItems.class);
    }



    @Override
    public  <T extends MCContent> T getPublication(
        @Nonnull  UUID id) {
        GenericUrl url = impl.createUrl("publications", id.toString());
        //url.set("label", "Post");
        url.set("status", "published");
        url.set("fields", PUBLICATION_FIELDS);
        return (T) impl.get(url, MCContent.class);
    }




    @Override
    public MCItems<?> getChannels(Paging paging) {
        GenericUrl url = impl.createUrl("channels");
        impl.addListParameters(url, paging);
        return impl.get(url, MCItems.class);


    }

    @Override
    public MCItems<?> getContainers(Paging paging) {
        GenericUrl url = impl.createUrl("containers");
        impl.addListParameters(url, paging);
        url.set("fields", PUBLICATION_FIELDS);

        return impl.get(url, MCItems.class);
    }

    @Override
    public <T extends MCContent> T getContainer(UUID id) {
        GenericUrl url = impl.createUrl("containers", id.toString());
        //url.set("label", "Post");
        url.set("status", "published");
        url.set("fields", PUBLICATION_FIELDS);
        return (T) impl.get(url, MCContent.class);
    }

}
