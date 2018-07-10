package nl.vpro.io.mediaconnect;

import nl.vpro.io.mediaconnect.domain.MCAsset;
import nl.vpro.io.mediaconnect.domain.MCItems;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
public interface MediaConnectAssets {


    MCItems<MCAsset> getAssets(Paging paging);

    default MCItems<MCAsset> getAssets() {
        return getAssets(Paging.builder().build());
    }

}
