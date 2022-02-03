package nl.vpro.io.prepr;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.cache.annotation.CacheResult;
import javax.inject.Inject;
import javax.inject.Named;

import com.google.api.client.http.GenericUrl;

import nl.vpro.io.prepr.domain.PreprItems;
import nl.vpro.io.prepr.domain.PreprTag;
import nl.vpro.io.prepr.domain.PreprTagGroup;

/**
 * TODO Cacheable annotations need to be implemented by proxy. That does not really work together with {@link StandalonePreprRepositories} we now use to implement a bunch of these.
 * @author Michiel Meeuwissen
 * @since 0.3
 */
@Named
public class PreprTagsImpl implements PreprTags {


    private final PreprRepositoryClient impl;

    @Inject
    public PreprTagsImpl(PreprRepositoryClient impl) {
        this.impl = impl;
    }


    public PreprItems<PreprTagGroup> createGroup(Paging paging, String name) {
        GenericUrl url = impl.createUrl("taggroups");
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("visible", true);

        return impl.post(url, map, PreprItems.class);


    }


    @Override
    public PreprItems<PreprTagGroup> getGroups(Paging paging, String q) {
        GenericUrl url = impl.createUrl("taggroups");
        if (q != null) {
            url.set("q", q);
        }
        url.set("fields", "tags");
        return impl.get(url, PreprItems.class);


    }

    @Override
    @CacheResult(cacheName = "PreprTagsImpl.getTags")
    public PreprItems<PreprTag> getTags(UUID tagGroup) {
        GenericUrl url = impl.createUrl("taggroups", tagGroup.toString());
        //uri.addParameter("environment_id", "45ed5691-8bc1-4018-9d67-242150cff944");
        url.set("fields", "tags");
        return impl.get(url, PreprItems.class);


    }


    @Override
    @CacheResult(cacheName = "PreprTagsImpl.getTag")
    public PreprTag getTag(UUID tag) {
        GenericUrl url = impl.createUrl("tags", tag.toString());
        //uri.addParameter("environment_id", "45ed5691-8bc1-4018-9d67-242150cff944");
        url.set("fields", "tag_groups,tag_types");
        return impl.get(url, PreprTag.class);


    }
}
