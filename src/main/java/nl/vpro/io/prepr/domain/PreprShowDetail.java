package nl.vpro.io.prepr.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonTypeName(PreprShowDetail.LABEL)
public class PreprShowDetail extends AbstractPreprContent {
    public static final String LABEL = "ShowDetail";


    String slug;
    String name;

    PreprShow container;

    String element;

    List<PreprTag> tags;


}
