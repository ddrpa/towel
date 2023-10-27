package cc.ddrpa.towel.generator.impl.idcard;

import cc.ddrpa.towel.ColumnDetails;
import cc.ddrpa.towel.generator.IGenerator;
import cc.ddrpa.towel.generator.IGeneratorFactory;

/**
 * 身份证号码生成
 * 地区码没有对应，需改进
 * 出生日期随机数字，需改进
 * 校验码的计算应该是对的
 */
public class IDCardGeneratorFactory implements IGeneratorFactory {
    private static final String name = "idcard";
    private static final String description = "身份证号码";
    private static final String usage = """
            """;

    @Override
    public IGenerator build(ColumnDetails columnDetails) {
        return new IDCardGenerator();
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