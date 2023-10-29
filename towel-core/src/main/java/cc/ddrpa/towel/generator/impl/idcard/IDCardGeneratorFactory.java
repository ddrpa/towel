package cc.ddrpa.towel.generator.impl.idcard;

import cc.ddrpa.towel.ColumnDetail;
import cc.ddrpa.towel.generator.IGenerator;
import cc.ddrpa.towel.generator.IGeneratorFactory;

/**
 * 身份证号码生成，可通过 <a href="https://uutool.cn/id-card/">uutool</a> 等在线工具验证
 */
public class IDCardGeneratorFactory implements IGeneratorFactory {
    private static final String name = "idcard";
    private static final String description = "身份证号码";
    private static final String usage = """
            采用一组预定义在代码中的地区码/出生年份/出生日期集合加上随机生成的三位序列码计算
            可通过 https://uutool.cn/id-card/ 等在线工具验证
            如匹配真实身份纯属巧合
            """;

    @Override
    public IGenerator build(ColumnDetail columnDetail) {
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