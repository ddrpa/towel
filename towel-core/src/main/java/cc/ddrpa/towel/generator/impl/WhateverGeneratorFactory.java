package cc.ddrpa.towel.generator.impl;

import cc.ddrpa.towel.ColumnDetails;
import cc.ddrpa.towel.exception.MisconfigurationException;
import cc.ddrpa.towel.generator.IGenerator;
import cc.ddrpa.towel.generator.IGeneratorFactory;

import java.security.SecureRandom;
import java.util.List;

public class WhateverGeneratorFactory implements IGeneratorFactory {
    private static final String name = "whatever";
    private static final String description = "提供一个枚举列表，我们会随机从里面抽取";
    private static final String usage = """
            提供一个枚举列表，towel 会随机从里面抽取
            如果你想增加某些元素出现的概率，可以多复制几个。示例：
            - name: 性别
              type: whatever
              enums: ['男', '女', '男', '女', '男', '女', '武装直升机']
              """;

    @Override
    public IGenerator build(ColumnDetails columnDetails) {
        if (!columnDetails.getAdditionalConfigMap().containsKey("enums")) {
            throw new MisconfigurationException("需要提供 enums 列表参数");
        }
        List<String> enums;
        try {
            enums = (List<String>) columnDetails.getAdditionalConfigMap().get("enums");
        } catch (ClassCastException e) {
            throw new MisconfigurationException("enums 列表必须是字符串列表");
        }
        if (enums == null || enums.isEmpty()) {
            throw new MisconfigurationException("enums 列表不能为空");
        }
        return new WhateverGenerator(enums);
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

    public static class WhateverGenerator implements IGenerator {
        private static final SecureRandom random = new SecureRandom();
        private static List<String> enums;
        private static int enumsCount;

        public WhateverGenerator(List<String> enums) {
            this.enums = enums;
            this.enumsCount = enums.size();
        }

        @Override
        public String next() {
            return enums.get(random.nextInt(enumsCount));
        }
    }
}