package nl.vpro.io.mediaconnect;

import lombok.Data;

/**
 * https://developers.mediaconnect.io/docs/fields-explained#section-offset-based-pagination-limits
 *
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Data
@lombok.Builder
public class Paging {

    Long skip;
    Long limit;

    String after;
    String before;

    public static Paging limit(long limit) {
        return Paging.builder().limit(limit).build();
    }

    public static Paging one() {
        return limit(1);
    }


}
