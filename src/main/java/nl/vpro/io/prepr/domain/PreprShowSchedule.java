package nl.vpro.io.prepr.domain;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@JsonTypeName(PreprShowSchedule.LABEL)
public class PreprShowSchedule extends PreprAbstractObject {

    public static final String LABEL = "ShowSchedule";

    Integer offset;

    Integer limit;

    LocalDate valid_from;

    LocalDate valid_until;

    List<Integer> days;

    boolean exception;
}
