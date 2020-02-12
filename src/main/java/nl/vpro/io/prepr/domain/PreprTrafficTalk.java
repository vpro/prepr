package nl.vpro.io.prepr.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Michiel Meeuwissen
 * @since 0.3
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonTypeName(PreprTrafficTalk.LABEL)
public class PreprTrafficTalk extends AbstractPreprContent {

    public static final String LABEL = "TrafficTalk";

}
