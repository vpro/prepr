package nl.vpro.io.prepr.domain;

import lombok.*;

import java.time.Duration;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * A talk seems to a kind 'segment' in the show. It seems that it at last approximately know when it was, but we don't get it back.
 * So for now we ignore it.
 *
 * @author Michiel Meeuwissen
 * @since 0.3
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@JsonTypeName(PreprTalk.LABEL)
public class PreprTalk extends PreprAbstractContent {

    public static final String LABEL = "Talk";

    private String note;

    private String name;

    private Duration length;

    private String tease;

    private PreprChannel channel;

    private List<PreprTag> tags;

    /**
     * No idea what this is.
     * @since 2.3
     */
    private String type;

}
