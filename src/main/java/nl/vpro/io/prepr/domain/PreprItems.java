package nl.vpro.io.prepr.domain;

import lombok.Data;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Data
public class PreprItems<T extends PreprAbstractObject> implements Iterable<T> {

    List<T> items;

    Integer total;

    String after;

    String before;

    Integer skip;

    Integer limit;

    @Override
    @Nonnull
    public Iterator<T> iterator() {
        return items == null ? Collections.emptyIterator() : items.iterator();

    }
}