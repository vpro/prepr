package nl.vpro.io.prepr.domain;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Michiel Meeuwissen
 * @since 0.3
 */
@EqualsAndHashCode(callSuper = true)
@JsonTypeName(PreprNewsBulletin.LABEL)
public class PreprNewsBulletin extends AbstractPreprContent {

    public static final String LABEL = "NewsBulletin";


}
