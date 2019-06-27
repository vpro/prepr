package nl.vpro.io.prepr.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonTypeName(PreprText.LABEL)
public class PreprText extends PreprAbstractObject {

    public static final String LABEL = "Text";


}
