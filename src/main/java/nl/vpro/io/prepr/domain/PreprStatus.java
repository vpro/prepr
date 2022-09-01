package nl.vpro.io.prepr.domain;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */

public enum PreprStatus {

    published,
    pending,
    approval,
    draft,
    unpublished,
    expired,
    archived,
    @JsonProperty("arla completed")
    arla_completed,
    @JsonEnumDefaultValue
    UNRECOGNIZED
}
