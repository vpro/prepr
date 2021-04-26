package nl.vpro.io.prepr.domain;

import lombok.*;

import java.net.URI;
import java.time.Duration;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Getter
@Setter
@JsonTypeName(PreprMediaFile.LABEL)

public class PreprMediaFile extends PreprAbstractObject {

    public static final String LABEL = "MediaFile";

    String profile;

    URI url;

    String cdn;

    String bucket;

    Duration duration;

    Float fps;

    Integer height;
    Integer width;

    URI thumbnail_url;

}
