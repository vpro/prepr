package nl.vpro.io.prepr.domain;

import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.annotations.Beta;

/**
 * No Idea what this is. it seems pretty messy like this. Added it for now to avoid warnings in the log.
 *
 * But it seems that the label can be postfixed by a Locale ('NLNL').
 *
 * This would be unmaintainabled than, we need something with dynamic label resolution to avoid having a class for for every single Locale in the world (which is in principle an unlimited list).
 *
 * @author Michiel Meeuwissen
 * @since 0.13
 */
@Beta
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@JsonTypeName(PreprPublished_NLNL.LABEL)
public  class PreprPublished_NLNL extends AbstractPreprContent {

    public static final String LABEL = "Published_NLNL";


    /**
     * especially this, wtf?
     */
    private Instant publish_on__n_l_n_l;

    private List<Locale> locales;
}
