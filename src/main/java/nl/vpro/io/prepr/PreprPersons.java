package nl.vpro.io.prepr;

import java.util.UUID;

import nl.vpro.io.prepr.domain.PreprItems;
import nl.vpro.io.prepr.domain.PreprPerson;

/**
 * @author Michiel Meeuwissen
 * @since 0.3
 */
public interface PreprPersons {


    PreprPerson get(UUID uuid);

    PreprItems<PreprPerson> list(String q);
}
