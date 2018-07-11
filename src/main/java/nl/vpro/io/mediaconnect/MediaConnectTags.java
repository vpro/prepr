package nl.vpro.io.mediaconnect;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import nl.vpro.io.mediaconnect.domain.MCItems;
import nl.vpro.io.mediaconnect.domain.MCTag;
import nl.vpro.io.mediaconnect.domain.MCTagGroup;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public interface MediaConnectTags {


    MCItems<MCTagGroup> getGroups(Paging paging, String q);

    default MCItems<MCTagGroup> getGroups(Paging paging) {
        return getGroups(paging, null);
    }

    default Optional<List<MCTag>> getTagsInGroup(String name) {
        return getGroups(Paging.one(), name).getItems().stream().map(MCTagGroup::getTags).findFirst();
    }

    MCItems<MCTag> getTags(UUID tagGroup);




}
