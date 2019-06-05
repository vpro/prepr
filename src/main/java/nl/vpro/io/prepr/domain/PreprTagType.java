package nl.vpro.io.prepr.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Michiel Meeuwissen
 * @since 0.3
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonTypeName("TagType")
public class PreprTagType extends PreprAbstractObject {

    String name;


}
