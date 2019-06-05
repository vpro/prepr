package nl.vpro.io.prepr;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import nl.vpro.io.prepr.domain.PreprItems;
import nl.vpro.io.prepr.domain.PreprTag;
import nl.vpro.io.prepr.domain.PreprTagGroup;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public interface PreprTags {


    PreprItems<PreprTagGroup> getGroups(Paging paging, String q);

    default PreprItems<PreprTagGroup> getGroups(Paging paging) {
        return getGroups(paging, null);
    }

    default Optional<List<PreprTag>> getTagsInGroup(String name) {
        return getGroups(Paging.one(), name).getItems().stream().map(PreprTagGroup::getTags).findFirst();
    }

    PreprItems<PreprTag> getTags(UUID tagGroup);

    PreprTag getTag(UUID tag);




}
