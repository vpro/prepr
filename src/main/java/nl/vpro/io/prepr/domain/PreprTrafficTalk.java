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
@JsonTypeName(PreprTrafficTalk.LABEL)
public class PreprTrafficTalk extends AbstractPreprContent {

    public static final String LABEL = "TrafficTalk";

}
