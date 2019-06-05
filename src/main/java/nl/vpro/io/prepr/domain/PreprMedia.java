package nl.vpro.io.prepr.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PreprMedia extends PreprAbstractObject {

    List<PreprAsset> content;

    public boolean hasSegmentsOrClips() {
        return ! getMedia().isEmpty();
    }


    public List<PreprPhoto> getPhotos() {
        return content == null ?
            Collections.emptyList() :
            content.stream().filter(a -> a instanceof PreprPhoto).map(a -> (PreprPhoto) a).collect(Collectors.toList());
    }


    public List<PreprAbstractMedia> getMedia() {
        return content == null ?
            Collections.emptyList() :
            content.stream().filter(a -> a instanceof PreprAbstractMedia).map(a -> (PreprAbstractMedia) a).collect(Collectors.toList());
    }


}
