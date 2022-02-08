package nl.vpro.io.prepr.domain;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.checkerframework.checker.nullness.qual.NonNull;

import com.google.common.base.MoreObjects;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@Slf4j
public abstract class PreprAsset extends PreprAbstractObject {

    public static final String REFERENCE_CRID_PREFIX =  CRID_PREFIX + "reference/";

    String name;

    String original_name;

    String reference;

    String reference_id;

    String author;

    /**
     * Tim: nee, source_file moet je gewoon vergeten. Alleen kijken naar cdn_fules.
     *
     * //  "cdn_files": null
     * --> Tim Hanssen [12:09 PM]
     * Dan is er waarschijnlijk iets mis met die foto, wellicht is die al gemaakt voor de cdn_files uberhaupt bestonden.
     * // Dan is is source_file toch maar nodig dus.
     */
    PreprSourceFile source_file;

    String rel_description;

    PreprCustom custom;

    List<PreprCdnFile> cdn_files;

    String custom_type;

    Map<String, PreprMediaFile> media;

    PreprStatus status;

    List<PreprTag> tags;

    String extension;

    String mime_type;

    Boolean replaceable;

    public Optional<String> getCridForReference(@NonNull PreprPost post) {
        if (reference_id == null) {
            log.info("Asset {} has no reference_id", this);
        }
        return Optional.ofNullable(reference_id == null ? null : REFERENCE_CRID_PREFIX + reference_id + "/post/" + post.getId());
    }


    @Override
    public String getCrid() {
        throw new IllegalStateException("Please use #getCrid(PreprPost)");
    }

    public String getCrid(PreprPost post) {
        if (post != null) {
            return super.getCrid() + "/post/" + post.getId();
        } else {
            return super.getCrid();
        }
    }


    @Override
    MoreObjects.ToStringHelper toStringHelper() {
        return MoreObjects.toStringHelper(this)
            .omitNullValues()
            .add("id", id)
            .add("body", body)
            .add("name", name)
            .add("reference", reference)
            .add("author", author)
            .add("cdn_file", cdn_files)
            .add("source_file", source_file)
            .add("reference_id", reference_id)
            .add("custom_type", custom_type)
            ;
    }
}
