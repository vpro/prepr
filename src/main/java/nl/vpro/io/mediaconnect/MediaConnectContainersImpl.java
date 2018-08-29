package nl.vpro.io.mediaconnect;

import java.util.UUID;

import com.google.api.client.http.GenericUrl;

import nl.vpro.io.mediaconnect.domain.MCTimeline;

/**
 * @author Michiel Meeuwissen
 * @since 0.3
 */
public class MediaConnectContainersImpl implements MediaConnectContainers {

    private  final MediaConnectRepositoryImpl impl;

    public MediaConnectContainersImpl(MediaConnectRepositoryImpl impl) {
        this.impl = impl;
    }

    @Override
    public MCTimeline getTimeline(UUID id) {
        GenericUrl url = impl.createUrl("containers", id);
        url.set("fields", "assets{custom,source_file}");
        return impl.get(url, MCTimeline.class);
    }
}
