package nl.vpro.io.mediaconnect.domain;

import lombok.Data;

import java.util.List;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Data
public class MCItems<T extends MCAbstractObject> {

    List<T> items;
    Integer total;

    String after;

    String before;

    Integer skip;

    Integer limit;

}
