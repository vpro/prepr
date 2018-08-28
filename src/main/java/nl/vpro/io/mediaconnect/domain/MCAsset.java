package nl.vpro.io.mediaconnect.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class MCAsset extends MCAbstractObject {

    public static String REFERENCE_CRID_PREFIX =  "crid://mediaconnect.io/reference/";


    String name;

    String reference;

    String reference_id;


    String author;

    MCSourceFile source_file;

    String rel_description;

    public String getCridForReference() {
        return reference_id == null ? null : REFERENCE_CRID_PREFIX + reference_id;
    }


    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
            "name='" + name + '\'' +
            ", body='" + body + '\'' +
            ", reference='" + reference + '\'' +
            ", author='" + author + '\'' +
            ", source_file=" + source_file +
            ", id=" + id +
            '}';
    }
}
