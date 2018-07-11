package nl.vpro.io.mediaconnect;

import nl.vpro.io.mediaconnect.domain.MCAsset;
import nl.vpro.io.mediaconnect.domain.MCItems;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
public interface MediaConnectAssets {


    MCItems<MCAsset> get(Paging paging);

    default MCItems<MCAsset> get() {
        return get(Paging.builder().build());
    }

}
