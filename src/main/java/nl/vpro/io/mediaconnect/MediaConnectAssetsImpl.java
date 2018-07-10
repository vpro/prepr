package nl.vpro.io.mediaconnect;

import com.google.api.client.http.GenericUrl;

import nl.vpro.io.mediaconnect.domain.MCAsset;
import nl.vpro.io.mediaconnect.domain.MCItems;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
public class MediaConnectAssetsImpl implements MediaConnectAssets {

    private  final MediaConnectRepositoryImpl impl;

    public MediaConnectAssetsImpl(MediaConnectRepositoryImpl impl) {
        this.impl = impl;
    }



    @SuppressWarnings("unchecked")
    @Override
    public MCItems<MCAsset> getAssets(Paging paging) {
        GenericUrl url = impl.createUrl("assets");
        impl.addListParameters(url, paging);
        url.set("fields", "name,body,reference,source_file,duration");
        return impl.get(url, MCItems.class);

    }
}
