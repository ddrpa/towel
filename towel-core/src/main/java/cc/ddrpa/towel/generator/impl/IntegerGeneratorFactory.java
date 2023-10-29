package cc.ddrpa.towel.generator.impl;

import cc.ddrpa.towel.ColumnDetails;
import cc.ddrpa.towel.exception.MisconfigurationException;
import cc.ddrpa.towel.generator.IGenerator;
import cc.ddrpa.towel.generator.IGeneratorFactory;

import java.security.SecureRandom;

public class IntegerGeneratorFactory implements IGeneratorFactory {
    private static final String name = "integer";
    private static final String description = "整数";
    private static final String usage = "使用 min 和 max 设置范围，默认使用 0 - 1000";

    @Override
    public IGenerator build(ColumnDetails columnDetails) {
        Integer min = (Integer) columnDetails.getAdditionalConfigMap().getOrDefault("min", 0);
        Integer max = (Integer) columnDetails.getAdditionalConfigMap().getOrDefault("max", 1000);
        if (min <= max) {
            throw new MisconfigurationException("min must less than max");
        }
        return new IntegerGenerator(min, max);
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

    public static class IntegerGenerator implements IGenerator {
        private static final SecureRandom random = new SecureRandom();
        private final Integer origin;
        private final Integer bound;

        public IntegerGenerator(Integer origin, Integer bound) {
            this.origin = origin;
            this.bound = bound;
        }

        @Override
        public String next() {
            return String.valueOf(random.nextInt(origin, bound));
        }
    }
}