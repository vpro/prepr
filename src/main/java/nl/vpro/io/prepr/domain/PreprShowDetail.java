package nl.vpro.io.prepr.domain;

import lombok.*;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@JsonTypeName(PreprShowDetail.LABEL)
public class PreprShowDetail extends PreprAbstractContent {

    public static final String LABEL = "ShowDetail";

    String slug;

    String name;

    PreprShow container;

    String element;

    List<PreprTag> tags;

    PreprChannel channel;

}
