package nl.vpro.io.prepr.domain;

import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.annotations.Beta;

/**
 * No Idea what this is. it seems pretty messy like this.
 *
 * See {@link PreprPublished_NLNL}
 *
 * @author Michiel Meeuwissen
 * @since 0.13
 */
@Beta
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@JsonTypeName(PreprNLNL.LABEL)
public  class PreprNLNL extends AbstractPreprContent {

    public static final String LABEL = "NLNL";

    /**
     * especially this, wtf?
     */
    private Instant publish_on__n_l_n_l;

    private List<Locale> locales;
}
