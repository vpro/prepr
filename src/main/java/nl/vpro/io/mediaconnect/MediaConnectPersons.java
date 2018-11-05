package nl.vpro.io.mediaconnect;

import java.util.UUID;

import nl.vpro.io.mediaconnect.domain.MCItems;
import nl.vpro.io.mediaconnect.domain.MCPerson;

/**
 * @author Michiel Meeuwissen
 * @since 0.3
 */
public interface MediaConnectPersons {


    MCPerson get(UUID uuid);

    MCItems<MCPerson> list(String q);
}
