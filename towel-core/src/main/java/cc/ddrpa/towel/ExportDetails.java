package cc.ddrpa.towel;

import java.util.function.BiFunction;

public class ExportDetails {
    private final String startOfFile;
    private final String endOfFile;
    private final String startOfRow;
    private final String endOfRow;
    private final String fieldDelimiter;
    private final String rowDelimiter;

    /**
     * Field wrapper function
     * <p>
     * <code>(columnName, fieldValue) -> wrappedFieldContent</code>
     * <p>
     * useful for export key-value pair
     */
    private final BiFunction<String, String, String> fieldWrapper;

    public ExportDetails(String startOfFile,
                         String endOfFile,
                         String startOfRow,
                         String endOfRow, String fieldDelimiter,
                         String rowDelimiter,
                         BiFunction<String, String, String> fieldWrapper) {
        this.startOfFile = startOfFile;
        this.endOfFile = endOfFile;
        this.startOfRow = startOfRow;
        this.endOfRow = endOfRow;
        this.fieldDelimiter = fieldDelimiter;
        this.rowDelimiter = rowDelimiter;
        this.fieldWrapper = fieldWrapper;
    }

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

    public BiFunction<String, String, String> getFieldWrapper() {
        return fieldWrapper;
    }

    public String getStartOfFile() {
        return startOfFile;
    }

    public String getEndOfFile() {
        return endOfFile;
    }

    public String getStartOfRow() {
        return startOfRow;
    }

    public String getEndOfRow() {
        return endOfRow;
    }

    public String getFieldDelimiter() {
        return fieldDelimiter;
    }

    public String getRowDelimiter() {
        return rowDelimiter;
    }
}