package cc.ddrpa.towel;

import java.util.function.BiFunction;

/**
 * @param fieldWrapper Field wrapper function
 *                     <p>
 *                     <code>(columnName, fieldValue) -> wrappedFieldContent</code>
 *                     <p>
 *                     useful for export key-value pair
 */
public record ExportDetails(String startOfFile, String endOfFile, String startOfRow, String endOfRow,
                            String fieldDelimiter, String rowDelimiter,
                            BiFunction<String, String, String> fieldWrapper) {

    public static ExportDetails csv() {
        return new ExportDetails("", "", "", "", ", ", "\n", (column, field) -> field);
    }

    public static ExportDetails json() {
        BiFunction<String, String, String> fieldWrapper = (column, field) -> String.format("    \"%s\": \"%s\"", column, field);
        return new ExportDetails("[\n  {\n", "\n  }\n]", "", "", ",\n", "\n  }, {\n", fieldWrapper);
    }

    public static ExportDetails sql() {
        return new ExportDetails("", "\n", "INSERT INTO $table_name ('", "');", "', '", "\n", (column, field) -> field);
    }
}