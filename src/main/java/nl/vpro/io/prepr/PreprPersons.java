package nl.vpro.io.prepr;

import java.util.UUID;

import nl.vpro.io.prepr.domain.MCItems;
import nl.vpro.io.prepr.domain.MCPerson;

/**
 * @author Michiel Meeuwissen
 * @since 0.3
 */
public interface PreprPersons {


    MCPerson get(UUID uuid);

    MCItems<MCPerson> list(String q);
}
