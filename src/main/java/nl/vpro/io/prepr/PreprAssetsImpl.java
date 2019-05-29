package nl.vpro.io.prepr;

import javax.inject.Inject;
import javax.inject.Named;

import com.google.api.client.http.GenericUrl;

import nl.vpro.io.prepr.domain.MCAsset;
import nl.vpro.io.prepr.domain.MCItems;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Named
public class PreprAssetsImpl implements PreprAssets {

    private  final PreprRepositoryClient impl;

    @Inject
    public PreprAssetsImpl(PreprRepositoryClient impl) {
        this.impl = impl;
    }



    @SuppressWarnings("unchecked")
    @Override
    public MCItems<MCAsset> get(Paging paging) {
        GenericUrl url = impl.createUrl("assets");
        impl.addListParameters(url, paging);
        url.set("fields", "name,body,reference,source_file,duration");
        return impl.get(url, MCItems.class);

    }
}
