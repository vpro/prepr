package nl.vpro.io.prepr;

import nl.vpro.io.prepr.domain.MCAsset;
import nl.vpro.io.prepr.domain.MCItems;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
public interface PreprAssets {


    MCItems<MCAsset> get(Paging paging);

    default MCItems<MCAsset> get() {
        return get(Paging.builder().build());
    }

}
