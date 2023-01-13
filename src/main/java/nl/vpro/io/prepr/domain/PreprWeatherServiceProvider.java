package nl.vpro.io.prepr.domain;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonTypeName;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@JsonTypeName(PreprWeatherServiceProvider.LABEL)
public class PreprWeatherServiceProvider extends PreprAbstractObject {

    public static final String LABEL = "WeatherServiceProvider";

    private String name;

}
