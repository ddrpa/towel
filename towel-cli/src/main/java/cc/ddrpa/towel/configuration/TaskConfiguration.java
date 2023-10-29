package cc.ddrpa.towel.configuration;

import cc.ddrpa.towel.ExportDetail;
import cc.ddrpa.towel.TableDetail;
import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class TaskConfiguration {
    @JsonAlias("rows")
    public Integer rows = 10;
    @JsonAlias("columns")
    @NotEmpty
    public List<ColumnConfiguration> columns;
    @JsonAlias("export")
    public ExportConfiguration export;

    public TableDetail tableDetail() {
        return new TableDetail(columns.stream().map(ColumnConfiguration::detail).toList(), rows);
    }

    public ExportDetail exportDetail() {
        String exportPreset = export.preset;
        return switch (exportPreset) {
            case "csv" -> ExportDetail.csv();
            case "json" -> ExportDetail.json();
            case "sql" -> ExportDetail.sql();
            default -> export.detail();
        };
    }
}