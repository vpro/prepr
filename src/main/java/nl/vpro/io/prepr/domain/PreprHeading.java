package nl.vpro.io.prepr.domain;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Getter
@Setter
@JsonTypeName(PreprHeading.LABEL)
public class PreprHeading extends PreprAbstractObject {

    public static final String LABEL = "Heading";


}
