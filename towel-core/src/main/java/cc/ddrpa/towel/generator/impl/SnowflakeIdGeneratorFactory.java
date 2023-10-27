package cc.ddrpa.towel.generator.impl;

import cc.ddrpa.repack.sequence.Sequence;
import cc.ddrpa.towel.ColumnDetails;
import cc.ddrpa.towel.generator.IGenerator;
import cc.ddrpa.towel.generator.IGeneratorFactory;

import java.util.Optional;

public class SnowflakeIdGeneratorFactory implements IGeneratorFactory {
    private static final String name = "snowflake-id";
    private static final String description = "雪花 ID";
    private static final String usage = "Just use it.";

    @Override
    public IGenerator build(ColumnDetails columnDetails) {
        Optional<Long> workerId = Optional.ofNullable((Long) columnDetails.getAdditionalConfig().get("workerId"));
        Optional<Long> datacenterId = Optional.ofNullable((Long) columnDetails.getAdditionalConfig().get("datacenterId"));
        if (workerId.isPresent() && datacenterId.isPresent()) {
            return new SnowflakeIdGenerator(new Sequence(workerId.get(), datacenterId.get()));
        } else {
            return new SnowflakeIdGenerator(new Sequence());
        }
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

    public static class SnowflakeIdGenerator implements IGenerator {
        private Sequence sequence;

        public SnowflakeIdGenerator(Sequence sequence) {
            this.sequence = sequence;
        }

        @Override
        public String next() {
            return String.valueOf(sequence.nextId());
        }
    }
}