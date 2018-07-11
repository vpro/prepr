package nl.vpro.io.mediaconnect;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.google.api.client.http.GenericUrl;

import nl.vpro.io.mediaconnect.domain.MCItems;
import nl.vpro.io.mediaconnect.domain.MCTag;
import nl.vpro.io.mediaconnect.domain.MCTagGroup;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
public class MediaConnectTagsImpl implements MediaConnectTags {


    private  final MediaConnectRepositoryImpl impl;

    public MediaConnectTagsImpl(MediaConnectRepositoryImpl impl) {
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
    public MCItems<MCTag> getTags(UUID tagGroup) {
        GenericUrl url = impl.createUrl("taggroups", tagGroup.toString());
        //uri.addParameter("environment_id", "45ed5691-8bc1-4018-9d67-242150cff944");
        url.set("fields", "tags");
        return impl.get(url, MCItems.class);


    }
}
