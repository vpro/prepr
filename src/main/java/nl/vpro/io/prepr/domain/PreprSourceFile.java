package nl.vpro.io.prepr.domain;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@JsonTypeName(PreprSourceFile.LABEL)
@JsonIgnoreProperties({"url", "url_msg"})   // deprecated
@Slf4j
public class PreprSourceFile extends PreprAbstractObject {

    public static final String LABEL = "SourceFile";

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

    String extension;

    String aws_bucket;

    String aws_file;

    public String getUrl() {
        if (resized != null && resized.has("picture")) {
            return resized.get("picture").textValue();
        } else {
            log.warn("No picture found. Trying to make from {}", cdn_url);
            return cdn_url.replace("{format}", "w_1920");
        }
    }




}
