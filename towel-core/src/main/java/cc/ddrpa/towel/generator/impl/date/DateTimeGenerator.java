package cc.ddrpa.towel.generator.impl.date;

import cc.ddrpa.towel.generator.IGenerator;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class DateTimeGenerator implements IGenerator {
    private final SecureRandom random = new SecureRandom();
    private final DateTimeFormatter formatter;
    private final Long notBeforeAsEpochSecond;
    private final Long notAfterAsEpochSecond;

    public DateTimeGenerator(DateTimeFormatter formatter, Long notBeforeAsEpochSecond, Long notAfterAsEpochSecond) {
        this.formatter = formatter;
        this.notBeforeAsEpochSecond = notBeforeAsEpochSecond;
        this.notAfterAsEpochSecond = notAfterAsEpochSecond;
    }

    @Override
    public String next() {
        return formatter.format(
                LocalDateTime.ofEpochSecond(
                        random.nextLong(notBeforeAsEpochSecond, notAfterAsEpochSecond),
                        0,
                        ZoneOffset.UTC));
    }
}