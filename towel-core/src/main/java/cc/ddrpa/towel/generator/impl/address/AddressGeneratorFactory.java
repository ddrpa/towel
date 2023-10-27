package cc.ddrpa.towel.generator.impl.address;

import cc.ddrpa.towel.ColumnDetails;
import cc.ddrpa.towel.generator.IGenerator;
import cc.ddrpa.towel.generator.IGeneratorFactory;

public class AddressGeneratorFactory implements IGeneratorFactory {
    private static final String name = "address";
    private static final String description = "省份/城市/区县/街道/门牌号";
    private static final String usage = """
            生成地址，可以指定是否包含省份、城市、区县、街道、门牌号。
            由于省份/城市/区县/街道的 token 池是分别随机生成的，可能会出现不合理的组合。
                        
            province: Boolean # 是否包含省份，默认为 true
            city: Boolean # 是否包含城市，默认为 true
            district: Boolean # 是否包含区县，默认为 true
            street: Boolean # 是否包含街道，默认为 true
            streetNumbers: Boolean # 是否包含门牌号，默认为 true
                        
            要把地址限定在某个区域，可以使用 prefix 参数搭配部分生成。示例：
            - name: 地址
              type: address
              prefix: 北京市海淀区
              province: false
              city: false
            """;

    @Override
    public IGenerator build(ColumnDetails columnDetails) {
        return new AddressGenerator(
                (Boolean) columnDetails.getAdditionalConfig().getOrDefault("province", true),
                (Boolean) columnDetails.getAdditionalConfig().getOrDefault("city", true),
                (Boolean) columnDetails.getAdditionalConfig().getOrDefault("district", true),
                (Boolean) columnDetails.getAdditionalConfig().getOrDefault("street", true),
                (Boolean) columnDetails.getAdditionalConfig().getOrDefault("streetNumbers", true));
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