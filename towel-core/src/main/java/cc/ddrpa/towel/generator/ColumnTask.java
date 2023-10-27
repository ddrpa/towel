package cc.ddrpa.towel.generator;

import java.util.function.Function;

public record ColumnTask(
        IGenerator generator,
        String columnName,
        Function<String, String> ValueWrapper
) {
}