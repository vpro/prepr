package nl.vpro.io.prepr;

import nl.vpro.io.prepr.domain.PreprAsset;
import nl.vpro.io.prepr.domain.PreprItems;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
public interface PreprAssets {


    PreprItems<PreprAsset> get(Paging paging);

    default PreprItems<PreprAsset> get() {
        return get(Paging.builder().build());
    }

}
