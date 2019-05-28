package nl.vpro.io.prepr;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.cache.annotation.CacheResult;
import javax.inject.Inject;
import javax.inject.Named;

import com.google.api.client.http.GenericUrl;

import nl.vpro.io.prepr.domain.MCItems;
import nl.vpro.io.prepr.domain.MCTag;
import nl.vpro.io.prepr.domain.MCTagGroup;

/**
 * TODO Cacheable annotations need to be implemented by proxy. That does not really work togehter with {@link StandaloneMediaConnectRepositories} we now use to implement a bunch of these.
 * @author Michiel Meeuwissen
 * @since 0.3
 */
@Named
public class MediaConnectTagsImpl implements MediaConnectTags {


    private final MediaConnectRepositoryClient impl;

    @Inject
    public MediaConnectTagsImpl(MediaConnectRepositoryClient impl) {
        this.impl = impl;
    }


    public MCItems<MCTagGroup> createGroup(Paging paging, String name) {
        GenericUrl url = impl.createUrl("taggroups");
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("visible", true);

        return impl.post(url, map, MCItems.class);


    }


    @Override
    public MCItems<MCTagGroup> getGroups(Paging paging, String q) {
        GenericUrl url = impl.createUrl("taggroups");
        if (q != null) {
            url.set("q", q);
        }
        url.set("fields", "tags");
        return impl.get(url, MCItems.class);


    }

    @Override
    @CacheResult(cacheName = "MediaConnectTagsImpl.getTags")
    public MCItems<MCTag> getTags(UUID tagGroup) {
        GenericUrl url = impl.createUrl("taggroups", tagGroup.toString());
        //uri.addParameter("environment_id", "45ed5691-8bc1-4018-9d67-242150cff944");
        url.set("fields", "tags");
        return impl.get(url, MCItems.class);


    }


    @Override
    @CacheResult(cacheName = "MediaConnectTagsImpl.getTag")
    public MCTag getTag(UUID tag) {
        GenericUrl url = impl.createUrl("tags", tag.toString());
        //uri.addParameter("environment_id", "45ed5691-8bc1-4018-9d67-242150cff944");
        url.set("fields", "tag_groups,tag_types");
        return impl.get(url, MCTag.class);


    }
}
