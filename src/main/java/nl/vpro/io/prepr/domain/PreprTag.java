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
@JsonTypeName("Tag")
public class PreprTag extends PreprAbstractObject {

    String name;

    String slug;

    String color;


    List<PreprTagType> tag_types;

    List<PreprTagGroup> tag_groups;
}
