package nl.vpro.io.prepr.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

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
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonTypeName(PreprTalk.LABEL)
public class PreprTalk extends PreprContent {

    public static final String LABEL = "Talk";

    private String note;

    private String name;

    private Duration length;

    private String tease;

    private PreprChannel channel;

    private List<PreprTag> tags;
}
