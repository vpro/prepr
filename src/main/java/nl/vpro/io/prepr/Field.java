package nl.vpro.io.prepr;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Michiel Meeuwissen
 * @since 0.3
 */
public class Field {

    private final String name;

    private final List<Field> fields;

    @lombok.Builder(builderClassName = "Builder", builderMethodName = "_builder")
    public Field(String name,
                 @lombok.Singular  List<Field> fields) {
        this.name = name;
        this.fields = fields;
    }

    public static Builder builder(String name) {
        return _builder().name(name);
    }


    @Override
    public String toString() {
        return name + (fields == null || fields.isEmpty() ? "" :  "{" + fields.stream().map(Field::toString).collect(Collectors.joining(",")) + "}");
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
