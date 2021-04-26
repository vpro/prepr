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
@JsonTypeName(PreprNewsCast.LABEL)
public class PreprNewsCast extends AbstractPreprContent {

    public static final String LABEL = "NewsCast";


}
