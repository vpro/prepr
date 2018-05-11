package nl.vpro.io.mediaconnect.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MCRule extends MCAbstractObject {

    Integer offset;
    Integer limit;
    LocalDate valid_from;
    LocalDate valid_until;
    List<Integer> days;
    boolean exception;
}
