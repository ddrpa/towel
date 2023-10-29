package cc.ddrpa.towel.generator.impl.idcard;

import cc.ddrpa.towel.generator.IGenerator;
import org.apache.commons.lang3.tuple.Pair;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Stream;

public class IDCardGenerator implements IGenerator {
    private static final List<Pair<String, Integer>> addressCodeWithValidateSum = List.of(
            Pair.of("110100", 21), Pair.of("110101", 25), Pair.of("110102", 29),
            Pair.of("110106", 45), Pair.of("130302", 57), Pair.of("130303", 61),
            Pair.of("130534", 99), Pair.of("140122", 72), Pair.of("210700", 58),
            Pair.of("210701", 62), Pair.of("210702", 66), Pair.of("211302", 56),
            Pair.of("211303", 60), Pair.of("220422", 76), Pair.of("220681", 130),
            Pair.of("220700", 67), Pair.of("230305", 76), Pair.of("230601", 75),
            Pair.of("310000", 30), Pair.of("320105", 64), Pair.of("330203", 70),
            Pair.of("330322", 87), Pair.of("330482", 140), Pair.of("340403", 89),
            Pair.of("340700", 92), Pair.of("341001", 71), Pair.of("350628", 144),
            Pair.of("350629", 148), Pair.of("360402", 103), Pair.of("360403", 107),
            Pair.of("370303", 111), Pair.of("410323", 80), Pair.of("410324", 84),
            Pair.of("430802", 103), Pair.of("430822", 119), Pair.of("441501", 103),
            Pair.of("441502", 107), Pair.of("445101", 123), Pair.of("452622", 147),
            Pair.of("452623", 151), Pair.of("452624", 155), Pair.of("452625", 159),
            Pair.of("511028", 102), Pair.of("522227", 127), Pair.of("522228", 131),
            Pair.of("530101", 71), Pair.of("530102", 75), Pair.of("542333", 142),
            Pair.of("542334", 146), Pair.of("542335", 150), Pair.of("542336", 154),
            Pair.of("542429", 163), Pair.of("612728", 154), Pair.of("612729", 158),
            Pair.of("612730", 130), Pair.of("620100", 65), Pair.of("620421", 100),
            Pair.of("620422", 104), Pair.of("623001", 94), Pair.of("623021", 110),
            Pair.of("623022", 114), Pair.of("623027", 134), Pair.of("630000", 69),
            Pair.of("659000", 177), Pair.of("659001", 181));
    private static final int addressCodeCount = addressCodeWithValidateSum.size();
    private static final List<Pair<String, Integer>> birthYearWithValidateSum = List.of(
            Pair.of("1949", 62), Pair.of("1953", 50), Pair.of("1957", 62),
            Pair.of("1961", 50), Pair.of("1965", 62), Pair.of("1969", 74),
            Pair.of("1973", 62), Pair.of("1977", 74), Pair.of("1981", 62),
            Pair.of("1985", 74), Pair.of("1989", 86), Pair.of("1993", 74),
            Pair.of("1997", 86), Pair.of("2001", 7), Pair.of("2005", 19),
            Pair.of("2009", 31), Pair.of("2013", 19), Pair.of("2017", 31),
            Pair.of("2021", 19));
    private static final int birthYearCount = birthYearWithValidateSum.size();
    private static final List<Pair<String, Integer>> birthDateWithValidateSum = List.of(
            Pair.of("0110", 19), Pair.of("0124", 49), Pair.of("0202", 28),
            Pair.of("0215", 53), Pair.of("0226", 68), Pair.of("0311", 42),
            Pair.of("0315", 62), Pair.of("0317", 72), Pair.of("0320", 47),
            Pair.of("0322", 57), Pair.of("0402", 46), Pair.of("0405", 61),
            Pair.of("0415", 71), Pair.of("0417", 81), Pair.of("0428", 96),
            Pair.of("0508", 85), Pair.of("0516", 85), Pair.of("0518", 95),
            Pair.of("0527", 100), Pair.of("0601", 59), Pair.of("0609", 99),
            Pair.of("0617", 99), Pair.of("0627", 109), Pair.of("0630", 84),
            Pair.of("0709", 108), Pair.of("0721", 88), Pair.of("0727", 118),
            Pair.of("0805", 97), Pair.of("0807", 107), Pair.of("0816", 112),
            Pair.of("0829", 137), Pair.of("0908", 121), Pair.of("0919", 136),
            Pair.of("0924", 121), Pair.of("1008", 47), Pair.of("1015", 42),
            Pair.of("1021", 32), Pair.of("1028", 67), Pair.of("1110", 26),
            Pair.of("1120", 36), Pair.of("1126", 66), Pair.of("1201", 30),
            Pair.of("1211", 40), Pair.of("1218", 75), Pair.of("1221", 50),
            Pair.of("1228", 85), Pair.of("1231", 60));
    private static final int birthDateCount = birthDateWithValidateSum.size();

    private static final int[] factor = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

    private static final Character[] lastChar = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
    private final SecureRandom random = new SecureRandom();

    private Character getLastCharacterOfIDCard(int addressCodeValidateSum, int birthYearValidateSum, int birthDateValidateSum, int[] sequence) {
        int sum = addressCodeValidateSum + birthYearValidateSum + birthDateValidateSum;
        for (int sequenceIndex = 0; sequenceIndex < 3; sequenceIndex++) {
            sum += sequence[sequenceIndex] * factor[sequenceIndex + 14];
        }
        return lastChar[sum % 11];
    }

    @Override
    public String next() {
        var randomAddressCodePair = addressCodeWithValidateSum.get(random.nextInt(addressCodeCount));
        var randomBirthYearPair = birthYearWithValidateSum.get(random.nextInt(birthYearCount));
        var randomBirthDatePair = birthDateWithValidateSum.get(random.nextInt(birthDateCount));
        int[] sequence = random.ints(0, 10).limit(3).toArray();
        return Stream.of(randomAddressCodePair.getLeft(),
                        randomBirthYearPair.getLeft(),
                        randomBirthDatePair.getLeft(),
                        String.valueOf(sequence[0]),
                        String.valueOf(sequence[1]),
                        String.valueOf(sequence[2]))
                .reduce("", String::concat) +
                getLastCharacterOfIDCard(randomAddressCodePair.getRight(),
                        randomBirthYearPair.getRight(),
                        randomBirthDatePair.getRight(),
                        sequence);
    }
}