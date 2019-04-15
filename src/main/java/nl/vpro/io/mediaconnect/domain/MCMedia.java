package nl.vpro.io.mediaconnect.domain;

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
public class MCMedia extends MCAbstractObject {

    List<MCAsset> content;

    public boolean hasSegments() {
        return ! getMedia().isEmpty();
    }


    public List<MCPhoto> getPhotos() {
        return content == null ?
            Collections.emptyList() :
            content.stream().filter(a -> a instanceof MCPhoto).map(a -> (MCPhoto) a).collect(Collectors.toList());
    }


    public List<MCAbstractMedia> getMedia() {
        return content == null ?
            Collections.emptyList() :
            content.stream().filter(a -> a instanceof MCAbstractMedia).map(a -> (MCAbstractMedia) a).collect(Collectors.toList());
    }


}
