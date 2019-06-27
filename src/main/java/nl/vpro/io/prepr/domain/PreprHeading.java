package nl.vpro.io.prepr.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@JsonTypeName(PreprHeading.LABEL)
public class PreprHeading extends PreprAbstractObject {

    public static final String LABEL = "Heading";


}
