package nl.vpro.io.mediaconnect.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonTypeName("Photo")

public class MCAsset extends MCAbstractObject {

    String name;
    String body;
    String reference;
    String author;

    MCSourceFile source_file;
}
