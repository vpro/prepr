package nl.vpro.io.prepr.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Nonnull;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public abstract class MCAsset extends MCAbstractObject {

    public static String REFERENCE_CRID_PREFIX =  CRID_PREFIX + "reference/";

    String name;

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
    MCSourceFile source_file;

    String rel_description;

    MCCustom custom;

    List<MCCdnFile> cdn_files;

    String custom_type;

    Map<String, MCMediaFile> media;

    MCStatus status;

    List<MCTag> tags;


    public Optional<String> getCridForReference(@Nonnull MCPost post) {
        if (reference_id == null) {
            log.info("Asset {} has no reference_id", this);
        }
        return Optional.ofNullable(reference_id == null ? null : REFERENCE_CRID_PREFIX + reference_id + "/post/" + post.getId());
    }


    @Override
    public String getCrid() {
        throw new IllegalStateException("Please use #getCrid(MCPost)");
    }

    public String getCrid(MCPost post) {
        if (post != null) {
            return super.getCrid() + "/post/" + post.getId();
        } else {
            return super.getCrid();
        }
    }


    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
            "name='" + name + '\'' +
            ", body='" + body + '\'' +
            ", reference='" + reference + '\'' +
            ", author='" + author + '\'' +
            ", cdn_files=" + cdn_files +
            ", id=" + id +
            '}';
    }
}
