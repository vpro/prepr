package nl.vpro.io.prepr.domain;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Michiel Meeuwissen
 * @since 0.3
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@JsonTypeName(PreprTagType.LABEL)
public class PreprTagType extends PreprAbstractObject {

    public static final String LABEL = "TagType";

    String name;

}
