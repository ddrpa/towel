package cc.ddrpa.towel.generator.impl.date;

import cc.ddrpa.towel.ColumnDetails;
import cc.ddrpa.towel.exception.MisconfigurationException;
import cc.ddrpa.towel.generator.IGenerator;
import cc.ddrpa.towel.generator.IGeneratorFactory;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * 1970-01-01 00:00:00 之后的某个时间
 * 2077-10-24 00:00:00 之前的某个时间
 */
public class DateTimeGeneratorFactory implements IGeneratorFactory {
    private static final String name = "date";
    private static final String description = "日期与时间";

    // default min datetime is 1453-05-29T00:00
    private static final Long defaultEarliestEpochSecond = -16302124800L;
    // default max datetime is 2077-12-31T23:59:59
    private static final Long defaultLatestEpochSecond = 3408220799L;
    private static final DateTimeFormatter defaultDatetimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String usage = """
            给出一个随机的日期时间
            format 输出字符串格式，传递 java.time.format.DateTimeFormatter.ofPattern(java.lang.String) 支持的参数，默认为 yyyy-MM-dd HH:mm:ss
            not-before 给定一个 yyyy-MM-dd 格式的日期字符串，不会生成该日 0 点之前的时间，默认为 1453年5月29日
            not-after 给定一个 yyyy-MM-dd 格式的日期字符串，不会生成该日 0 点之后的时间，默认为 2077年12月31日
            
            该生成器是通过 java.time 包的 ofEpochSecond 方法实现的，理论范围为 -365243219162L ~ 365241780471L
            """;

    @Override
    public IGenerator build(ColumnDetails columnDetails) {
        // setup datetime format config
        var datetimeFormatter = defaultDatetimeFormatter;
        var dateTimeFormat = columnDetails.getAdditionalConfig("format", String.class);
        if (dateTimeFormat.isPresent() && !dateTimeFormat.get().isBlank()) {
            try {
                datetimeFormatter = DateTimeFormatter.ofPattern(dateTimeFormat.get());
            } catch (Exception e) {
                throw new MisconfigurationException("创建 DateTimeFormatter 失败，请检查 format 参数格式是否支持：" + e.getMessage());
            }
        }
        // setup earliest datetime
        var notBeforeAsEpochSecond = defaultEarliestEpochSecond;
        var minDateConfig = columnDetails.getAdditionalConfig("not-before", String.class);
        if (minDateConfig.isPresent() && !minDateConfig.get().isBlank()) {
            try {
                var notBefore = LocalDate.parse(minDateConfig.get());
                notBeforeAsEpochSecond = OffsetDateTime.of(notBefore.atStartOfDay(), ZoneOffset.UTC).toInstant().getEpochSecond();
            } catch (Exception e) {
                throw new MisconfigurationException("无法从给定数据创建日期实例，请确认使用 yyyy-MM-dd 格式的字符串指定日期且日期在可用范围内：" + e.getMessage());
            }
        }
        // setup latest datetime
        var notAfterAsEpochSecond = defaultLatestEpochSecond;
        var maxDateConfig = columnDetails.getAdditionalConfig("not-after", String.class);
        if (maxDateConfig.isPresent() && !maxDateConfig.get().isBlank()) {
            try {
                var notAfter = LocalDate.parse(maxDateConfig.get());
                notAfterAsEpochSecond = OffsetDateTime.of(notAfter.atStartOfDay(), ZoneOffset.UTC).toInstant().getEpochSecond();
            } catch (Exception e) {
                throw new MisconfigurationException("无法从给定数据创建日期实例，请确认使用 yyyy-MM-dd 格式的字符串指定日期且日期在可用范围内：" + e.getMessage());
            }
        }
        if (notBeforeAsEpochSecond > notAfterAsEpochSecond) {
            throw new MisconfigurationException("not-before 必须早于 not-after");
        }
        return new DateTimeGenerator(datetimeFormatter, notBeforeAsEpochSecond, notAfterAsEpochSecond);
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
}