package nl.vpro.io.mediaconnect.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonTypeName("SourceFile")
public class MCSourceFile extends MCAbstractObject {

    String original_name;

    String mime_type;

    Long file_size;

    URI file;

    String cdn_url;

}
