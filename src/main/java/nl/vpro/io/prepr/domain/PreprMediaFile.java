package nl.vpro.io.prepr.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.net.URI;
import java.time.Duration;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
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
