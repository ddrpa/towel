package cc.ddrpa.towel;

import java.util.function.BiFunction;

/**
 * @param fieldWrapper Field wrapper function
 *                     <p>
 *                     <code>(columnName, fieldValue) -> wrappedFieldContent</code>
 *                     <p>
 *                     useful for export key-value pair
 */
public record ExportDetail(String startOfFile, String endOfFile, String startOfRow, String endOfRow,
                           String fieldDelimiter, String rowDelimiter,
                           BiFunction<String, String, String> fieldWrapper) {

    public static ExportDetail csv() {
        return new ExportDetail("", "", "", "", ", ", "\n", (column, field) -> field);
    }

    public static ExportDetail json() {
        BiFunction<String, String, String> fieldWrapper = (column, field) -> String.format("    \"%s\": \"%s\"", column, field);
        return new ExportDetail("[\n  {\n", "\n  }\n]", "", "", ",\n", "\n  }, {\n", fieldWrapper);
    }

    public static ExportDetail sql() {
        return new ExportDetail("", "\n", "INSERT INTO $table_name ('", "');", "', '", "\n", (column, field) -> field);
    }
}