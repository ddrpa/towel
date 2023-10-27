package cc.ddrpa.towel.generator.impl.address;

import cc.ddrpa.towel.generator.IGenerator;

import java.security.SecureRandom;

/**
 * 地址生成器的 省份/城市/区县/街道 名词池属于 AIGC，不保证完整性和/或真实有效性
 * 可能需要性能优化
 */
public class AddressGenerator implements IGenerator {
    private static final String[] province = {
            "北京市", "天津市", "上海市", "重庆市", "河北省", "山西省", "辽宁省", "吉林省", "黑龙江省", "江苏省", "浙江省",
            "安徽省", "福建省", "江西省", "山东省", "河南省", "湖北省", "湖南省", "广东省", "海南省", "四川省", "贵州省",
            "云南省", "陕西省", "甘肃省", "青海省", "台湾省"
    };
    private static final int provinceCount = province.length;
    private static final String[] city = {
            "石家庄市", "唐山市", "秦皇岛市", "保定市", "张家口市", "承德市", "沧州市", "廊坊市", "衡水市", "太原市", "大同市",
            "阳泉市", "长治市", "晋城市", "晋中市", "运城市", "临汾市", "吕梁市", "呼和浩特市", "包头市", "乌海市", "赤峰市",
            "通辽市", "鄂尔多斯市", "呼伦贝尔市", "乌兰察布市", "兴安盟", "阿拉善盟", "沈阳市", "大连市", "鞍山市",
            "抚顺市", "本溪市", "丹东市", "锦州市", "营口市", "辽阳市", "盘锦市", "铁岭市"
    };
    private static final int cityCount = city.length;
    private static final String[] district = {
            "和平区", "河东区", "河西区", "南开区", "红桥区", "东丽区", "西青区", "津南区", "北辰区", "武清区", "滨海新区",
            "宁河区", "静海区", "长安区", "桥西区", "新华区", "裕华区", "正定县", "深泽县", "平山县", "路南区", "路北区",
            "开平区", "丰南区", "迁西县", "玉田县", "遵化市", "迁安市", "海港区", "抚宁区", "昌黎县", "复兴区", "成安县"
    };
    private static final int districtCount = district.length;
    private static final String[] street = {
            "和平路", "河东路", "河西路", "南开路", "河北路", "红桥路", "东丽路", "西青路", "津南路", "北辰路", "武清路",
            "滨海新路", "宁河路", "静海路", "长安路", "桥西路", "新华路", "裕华路", "鹿泉路", "正定路", "深泽路", "平山路",
            "开平路", "丰南路", "迁西路", "玉田路", "遵化路", "迁安路", "海港路", "抚宁路", "复兴路", "成安路"
    };
    private static final int streetCount = street.length;
    private final SecureRandom random = new SecureRandom();
    private final boolean requireProvince;
    private final boolean requireCity;
    private final boolean requireDistrict;
    private final boolean requireStreet;
    private final boolean requireStreetNumbers;

    public AddressGenerator(boolean requireProvince, boolean requireCity, boolean requireDistrict, boolean requireStreet, boolean requireStreetNumbers) {
        this.requireProvince = requireProvince;
        this.requireCity = requireCity;
        this.requireDistrict = requireDistrict;
        this.requireStreet = requireStreet;
        this.requireStreetNumbers = requireStreetNumbers;
    }

    @Override
    public String next() {
        StringBuilder sb = new StringBuilder();
        if (requireProvince) {
            sb.append(province[random.nextInt(provinceCount)]);
        }
        if (requireCity) {
            sb.append(city[random.nextInt(cityCount)]);
        }
        if (requireDistrict) {
            sb.append(district[random.nextInt(districtCount)]);
        }
        if (requireStreet) {
            sb.append(street[random.nextInt(streetCount)]);
        }
        if (requireStreetNumbers) {
            sb.append(random.nextInt(1000));
            sb.append("号");
        }
        return sb.toString();
    }
}