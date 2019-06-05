package nl.vpro.io.prepr;

import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;

import com.google.api.client.http.GenericUrl;

import nl.vpro.io.prepr.domain.PreprTimeline;

/**
 * @author Michiel Meeuwissen
 * @since 0.3
 */
@Named
public class PreprContainersImpl implements PreprContainers {

    public static Fields TIMELINE_FIELDS = Fields.builder()
        .f("publications")
        .field(Fields.ASSETS)
        .build();

    private  final PreprRepositoryClient impl;

    @Inject
    public PreprContainersImpl(PreprRepositoryClient impl) {
        this.impl = impl;
    }

    @Override
    public PreprTimeline getTimeline(UUID id) {
        GenericUrl url = impl.createUrl("containers", id);
        url.set("fields", TIMELINE_FIELDS);
        return impl.get(url, PreprTimeline.class);
    }
}
