package nl.vpro.io.mediaconnect.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonTypeName("SourceFile")
@JsonIgnoreProperties({"url", "url_msg"})   // deprecated
public class MCSourceFile extends MCAbstractObject {

    String original_name;

    String mime_type;

    Long file_size;

    URI file;

    String cdn_url;

    String waveform_url;

    String watermarked_url;

    String chunks;

    // betekent niks.
    Long start_offset;

    Long end_offset;

    JsonNode resized;

    public String getUrl() {
        if (resized != null && resized.has("picture")) {
            return resized.get("picture").textValue();
        } else {
            return cdn_url;
        }
    }




}
