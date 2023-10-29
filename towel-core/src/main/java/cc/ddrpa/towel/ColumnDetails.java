package cc.ddrpa.towel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class ColumnDetails {
    private final String columnName;
    private final String generatorIdentifier;
    private final Boolean hasPrefix;
    private final String fieldPrefix;
    private final Boolean hasSuffix;
    private final String fieldSuffix;
    private final Map<String, Object> additionalConfig;

    public ColumnDetails(@NotBlank String columnName,
                         @NotBlank String generatorIdentifier,
                         @NotBlank String fieldPrefix,
                         @NotBlank String fieldSuffix,
                         @NotNull Map<String, Object> additionalConfig) {
        this.columnName = columnName;
        this.generatorIdentifier = generatorIdentifier;
        this.fieldPrefix = fieldPrefix;
        this.hasPrefix = !fieldPrefix.isEmpty();
        this.fieldSuffix = fieldSuffix;
        this.hasSuffix = !fieldSuffix.isEmpty();
        this.additionalConfig = additionalConfig;
    }

    Function<String, String> getValueWrapper() {
        if (hasPrefix && hasSuffix) {
            return (s) -> fieldPrefix + s + fieldSuffix;
        } else if (hasPrefix) {
            return (s) -> fieldPrefix + s;
        } else if (hasSuffix) {
            return (s) -> s + fieldSuffix;
        } else {
            return (s) -> s;
        }
    }

    public String getColumnName() {
        return columnName;
    }

    public String getGeneratorIdentifier() {
        return generatorIdentifier;
    }

    public Map<String, Object> getAdditionalConfigMap() {
        return additionalConfig;
    }

    public <T> Optional<T> getAdditionalConfig(String key, Class<T> clazz) {
        if (!additionalConfig.containsKey(key)) {
            return Optional.empty();
        }
        return Optional.of(clazz.cast(additionalConfig.get(key)));
    }
}