package cc.ddrpa.towel.generator.impl;

import cc.ddrpa.towel.ColumnDetail;
import cc.ddrpa.towel.generator.IGenerator;
import cc.ddrpa.towel.generator.IGeneratorFactory;

import java.security.SecureRandom;

/**
 * 姓名生成器候选池属于 AIGC，不保证完整性和/或真实有效性
 * 可能需要性能优化
 */
public class PersonNameGeneratorFactory implements IGeneratorFactory {
    private static final String name = "person-name";
    private static final String description = "姓名";
    private static final String usage = "随机生成两个字或三个字的名字";

    @Override
    public IGenerator build(ColumnDetail columnDetail) {
        return new PersonNameGenerator();
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

    public static class PersonNameGenerator implements IGenerator {
        private final SecureRandom random = new SecureRandom();
        private static final String[] familyName = {
                "赵", "钱", "孙", "李", "周", "吴", "郑", "王", "冯", "陈", "褚", "卫", "蒋", "沈", "韩", "杨", "朱", "秦",
                "许", "何", "吕", "施", "张", "孔", "曹", "严", "华", "金", "魏", "陶", "姜", "章", "苏", "潘", "葛", "范",
                "彭", "郎", "鲁", "韦", "马", "苗", "方", "俞", "任", "袁", "柳", "史", "费", "薛", "雷", "贺", "汤", "罗",
                "毕", "安", "常", "时", "傅", "皮", "齐", "余", "元", "顾", "孟", "黄", "和", "穆", "萧", "尹", "姚", "邵",
                "汪", "毛", "狄", "米", "贝", "明", "计", "成", "戴", "谈", "宋", "茅", "熊", "纪", "舒", "项", "祝", "梁"
        };
        private static final int familyNameCount = familyName.length;
        private static final String[] givenName = {
                "子", "璇", "国", "云", "鹏", "伟", "力", "建", "明", "永", "健", "世", "广", "志", "义", "兴", "良",
                "海", "山", "仁", "波", "宁", "贵", "福", "生", "龙", "元", "全", "胜", "学", "祥", "才", "发", "武", "新",
                "利", "清", "飞", "彬", "富", "顺", "信", "子", "杰", "涛", "昌", "成", "康", "星", "光", "天", "达", "安",
                "岩", "中", "茂", "进", "林", "有", "坚", "和", "彪", "博", "诚", "先", "敬", "震", "振", "壮", "会", "思",
                "群", "豪", "心", "邦", "承", "乐", "绍", "功", "松", "善", "厚", "庆", "磊", "民", "友", "裕", "河", "哲",
                "江", "超", "浩", "亮", "政", "谦", "亨", "奇", "固", "之", "轮", "翰", "朗", "伯", "宏", "言", "若", "鸣",
                "朋", "斌", "梦", "龙", "磊", "文", "辉", "力", "明", "永", "健", "世", "广", "志", "义", "兴", "良", "海",
                "山", "仁", "波", "宁", "贵", "福", "生", "龙", "元", "全", "胜", "学", "祥", "才", "发", "武", "新", "利",
                "清", "飞", "彬", "富", "顺", "信", "子", "杰", "涛", "昌", "成", "康", "星", "光", "天", "达", "安", "岩"
        };
        private static final int givenNameCount = givenName.length;

        @Override
        public String next() {
            return random.nextFloat() > 0.5f ?
                    familyName[random.nextInt(familyNameCount)] + givenName[random.nextInt(givenNameCount)] :
                    familyName[random.nextInt(familyNameCount)] + givenName[random.nextInt(givenNameCount)] + givenName[random.nextInt(givenNameCount)];
        }
    }
}