package nl.vpro.io.mediaconnect;

import java.util.UUID;

import nl.vpro.io.mediaconnect.domain.MCItems;
import nl.vpro.io.mediaconnect.domain.MCTag;
import nl.vpro.io.mediaconnect.domain.MCTagGroup;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
public interface MediaConnectTags {


    MCItems<MCTagGroup> getGroups(Paging paging, String name);

    MCItems<MCTagGroup> getGroups(Paging paging);


    MCItems<MCTag> getTags(UUID tagGroup);




}
