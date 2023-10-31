package cc.ddrpa.towel.generator;

import cc.ddrpa.towel.ColumnDetail;

import java.security.SecureRandom;
import java.util.stream.Collectors;

public class CellPhoneGeneratorFactory implements IGeneratorFactory {
    private static final String name = "cell-phone";
    private static final String description = "手机号";
    private static final String usage = "随机生成一个手机号";

    @Override
    public IGenerator build(ColumnDetail columnDetail) {
        return new CellPhoneGenerator();
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

    public static class CellPhoneGenerator implements IGenerator {
        private static final String[] ServiceProviderPrefix = {
                "133", "153", "189", "180", "181", "134", "139", "187", "188", "157", "152", "151", "150", "158",
                "182", "183", "147", "159", "130", "131", "132", "145", "155", "156", "185", "186"
        };
        private static final int ServiceProviderPrefixCount = ServiceProviderPrefix.length;
        private final SecureRandom random = new SecureRandom();

        @Override
        public String next() {
            return random.ints(0, 10)
                    .limit(8)
                    .mapToObj(String::valueOf)
                    .collect(Collectors.joining("", ServiceProviderPrefix[random.nextInt(ServiceProviderPrefixCount)], ""));
        }
    }
}