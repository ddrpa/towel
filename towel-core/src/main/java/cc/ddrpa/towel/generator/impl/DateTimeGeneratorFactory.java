package cc.ddrpa.towel.generator.impl;

import cc.ddrpa.towel.ColumnDetails;
import cc.ddrpa.towel.generator.IGenerator;
import cc.ddrpa.towel.generator.IGeneratorFactory;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * 1970-01-01 00:00:00 之后的某个时间
 * 2077-10-24 00:00:00 之前的某个时间
 */
public class DateTimeGeneratorFactory implements IGeneratorFactory {
    private static final String name = "date";
    private static final String description = "日期与时间";
    private static final String usage = """
            """;

    @Override
    public IGenerator build(ColumnDetails columnDetails) {
        var additionalConfig = columnDetails.getAdditionalConfig();
        var dateTimeFormat = additionalConfig.getOrDefault("format", "yyyy-MM-dd HH:mm:ss").toString();
        return new DateTimeGenerator(dateTimeFormat);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getUsage() {
        return usage;
    }

    public static class DateTimeGenerator implements IGenerator {
        private final SecureRandom random = new SecureRandom();
        private final DateTimeFormatter formatter;

        public DateTimeGenerator(String dateTimeFormat) {
            this.formatter = DateTimeFormatter.ofPattern(dateTimeFormat);
        }

        @Override
        public String next() {
            return formatter.format(
                    LocalDateTime.ofEpochSecond(
                            random.nextLong(0, 3402259200L),
                            0,
                            ZoneOffset.UTC));
        }
    }
}