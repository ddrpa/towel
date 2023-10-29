package cc.ddrpa.towel.generator.impl;

import cc.ddrpa.repack.sequence.Sequence;
import cc.ddrpa.towel.ColumnDetail;
import cc.ddrpa.towel.exception.MisconfigurationException;
import cc.ddrpa.towel.generator.IGenerator;
import cc.ddrpa.towel.generator.IGeneratorFactory;

public class SnowflakeIdGeneratorFactory implements IGeneratorFactory {
    private static final String name = "snowflake-id";
    private static final String description = "雪花 ID";
    private static final String usage = """
            随机生成雪花 ID，可以自定义 workerID 和 datacenterID，使用：
            worker-id
            datacenter-id
            注意，根据雪花算法，workerID 和 datacenterID 必须在 0 - 31 之间
            注意，需要同时指定 workerID 和 datacenterID，否则会使用 Sequence 计算的默认值
            注意，根据 towel 计算列的运行机制，有大概率得到连续的 ID
            """;

    @Override
    public IGenerator build(ColumnDetail columnDetail) {
        var workerId = columnDetail.getAdditionalConfig("worker-id", Long.class);
        var datacenterId = columnDetail.getAdditionalConfig("datacenter-id", Long.class);
        if (workerId.isPresent() && datacenterId.isPresent()) {
            if (workerId.get() < 0 || workerId.get() > 31 || datacenterId.get() < 0 || datacenterId.get() > 31) {
                throw new MisconfigurationException("workerId and datacenterId must between 0 and 31");
            }
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