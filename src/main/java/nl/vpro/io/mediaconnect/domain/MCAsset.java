package nl.vpro.io.mediaconnect.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MCAsset extends MCAbstractObject {

    String name;
    String body;
    String reference;
    String author;

    MCSourceFile source_file;
}
