package nl.vpro.io.mediaconnect;

import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;

import com.google.api.client.http.GenericUrl;

import nl.vpro.io.mediaconnect.domain.MCTimeline;

/**
 * @author Michiel Meeuwissen
 * @since 0.3
 */
@Named
public class MediaConnectContainersImpl implements MediaConnectContainers {

    public static Fields TIMELINE_FIELDS = Fields.builder()
        .f("publications")
        .field(Fields.ASSETS)
        .build();

    private  final MediaConnectRepositoryClient impl;

    @Inject
    public MediaConnectContainersImpl(MediaConnectRepositoryClient impl) {
        this.impl = impl;
    }

    @Override
    public MCTimeline getTimeline(UUID id) {
        GenericUrl url = impl.createUrl("containers", id);
        url.set("fields", TIMELINE_FIELDS);
        return impl.get(url, MCTimeline.class);
    }
}
