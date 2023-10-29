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
        switch (exportPreset) {
            case "csv":
                return ExportDetail.csv();
            case "json":
                return ExportDetail.json();
            case "sql":
                return ExportDetail.sql();
            default:
                return export.detail();
        }
    }
}