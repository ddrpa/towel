package cc.ddrpa.towel.configuration;

import cc.ddrpa.towel.ExportDetails;
import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Pattern;

public class ExportConfiguration {
    // 匹配 %c，但排除 %%c
    private static final Pattern columnPattern = Pattern.compile("(?<=[^%])%c");

    @JsonAlias("preset")
    public String preset = "";
    @JsonAlias("start-of-file")
    public String startOfFile = "";
    @JsonAlias("end-of-file")
    public String endOfFile = "";
    @JsonAlias("start-of-row")
    public String startOfRow = "";
    @JsonAlias("end-of-row")
    public String endOfRow = "";
    @JsonAlias("field-delimiter")
    public String fieldDelimiter = ", ";
    @JsonAlias("row-delimiter")
    public String rowDelimiter = "\n";
    @JsonAlias("field-open-quote")
    public String fieldOpenQuote = "";
    @JsonAlias("field-close-quote")
    public String fieldCloseQuote = "";

    public ExportDetails details() {
        BiFunction<String, String, String> fieldWrapper;
        Function<String, String> openQuoteSetter;
        Function<String, String> closeQuoteSetter;
        if (fieldOpenQuote.contains("%")) {
            openQuoteSetter = (column) -> fieldOpenQuote.replaceAll(columnPattern.pattern(), column);
        } else {
            openQuoteSetter = (s) -> fieldOpenQuote;
        }
        if (fieldCloseQuote.contains("%")) {
            closeQuoteSetter = (column) -> fieldCloseQuote.replaceAll(columnPattern.pattern(), column);
        } else {
            closeQuoteSetter = (s) -> fieldCloseQuote;
        }
        fieldWrapper = (column, field) -> openQuoteSetter.apply(column).replaceAll("%%", "%") + field + closeQuoteSetter.apply(column).replaceAll("%%", "%");
        return new ExportDetails(startOfFile, endOfFile, startOfRow, endOfRow, fieldDelimiter, rowDelimiter, fieldWrapper);
    }
}