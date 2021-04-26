package nl.vpro.io.prepr.domain;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@JsonTypeName(PreprGuide.LABEL)
public class PreprGuide extends PreprAbstractObject {

    public static final String LABEL = "Guide";

    String body;
}
