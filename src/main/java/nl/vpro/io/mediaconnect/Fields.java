package nl.vpro.io.mediaconnect;

import lombok.Singular;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

/**
 * @author Michiel Meeuwissen
 * @since 0.3
 */
public class Fields {

    static final Field SOURCEFILE_FIELD = Field.builder("source_file")
        .field(Field.builder("resized").f("picture.width(1920)")
            .build())
        .build();
    static final Field COVER = Field.builder("cover").field(SOURCEFILE_FIELD).build();

    static final Field ASSETS = Field.builder("assets").fs("custom", "source_file", "media").build();

    private final List<Field> fields;

    @lombok.Builder(builderClassName = "Builder")
    public Fields(@Singular  @Nonnull  List<Field> fields) {
        this.fields = fields;
    }

    @Override
    public String toString() {
        return fields.stream().map(Field::toString).collect(Collectors.joining(","));
    }

    public static class Builder {

        public Builder f(String name) {
            return field(Field.builder(name).build());
        }

        public Builder fs(String... name) {
            return fields(Arrays.stream(name).map(n -> new Field(n, null)).collect(Collectors.toList()));
        }
        public Builder fs(Field... fields) {
            return fields(Arrays.asList(fields));
        }


    }
}
