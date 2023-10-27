package cc.ddrpa.towel.configuration;

import cc.ddrpa.towel.ExportDetails;
import cc.ddrpa.towel.TableDetails;
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

    public TableDetails tableDetails() {
        return new TableDetails(columns.stream().map(ColumnConfiguration::details).toList(), rows);
    }

    public ExportDetails exportDetails() {
        String exportPreset = export.preset;
        switch (exportPreset) {
            case "csv":
                return ExportDetails.csv();
            case "json":
                return ExportDetails.json();
            case "sql":
                return ExportDetails.sql();
            default:
                return export.details();
        }
    }
}