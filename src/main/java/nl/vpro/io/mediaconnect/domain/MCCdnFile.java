package nl.vpro.io.mediaconnect.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Michiel Meeuwissen
 * @since 0.3
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonTypeName("CdnFile")
public class MCCdnFile extends MCAbstractObject {

    String cdn;
    String bucket;
    String profile;
    String file;
    String url;

    /**
     * wtf?
     */
    String c_d_n;

}
