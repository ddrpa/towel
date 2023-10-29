package cc.ddrpa.towel.configuration;

import cc.ddrpa.towel.ColumnDetail;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import jakarta.validation.constraints.NotBlank;

import java.util.LinkedHashMap;
import java.util.Map;

public class ColumnConfiguration {
    @JsonAlias("name")
    @NotBlank(message = "column name is required")
    public String columnName;
    @JsonAlias("type")
    @NotBlank(message = "generator type is required")
    public String generatorIdentifier;
    @JsonAlias("prefix")
    public String fieldPrefix = "";
    @JsonAlias("suffix")
    public String fieldSuffix = "";
    @JsonAnyGetter
    @JsonAnySetter
    public Map<String, Object> additional = new LinkedHashMap<>();

    public ColumnDetail detail() {
        return new ColumnDetail(columnName, generatorIdentifier, fieldPrefix, fieldSuffix, additional);
    }
}