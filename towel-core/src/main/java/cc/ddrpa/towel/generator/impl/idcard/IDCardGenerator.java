package cc.ddrpa.towel.generator.impl.idcard;

import cc.ddrpa.towel.generator.IGenerator;

import java.security.SecureRandom;
import java.util.Arrays;

public class IDCardGenerator implements IGenerator {
    private static final String[] addressCode = {
            "110100", "110101", "110102", "110106", "130302", "130303", "130534", "140110", "140121", "140122", "210700",
            "210701", "210702", "211302", "211303", "220422", "220681", "220700", "230305", "230601", "231101", "232722",
            "232723", "310000", "320105", "330203", "330322", "330482", "340403", "340700", "340824", "341000", "341001",
            "350626", "350627", "350628", "350629", "360402", "360403", "370303", "410323", "410324", "430802", "430811",
            "430821", "430822", "441501", "441502", "445101", "445201", "445202", "445221", "452428", "452600", "452601",
            "452622", "452623", "452624", "452625", "452727", "452728", "452729", "511028", "511100", "511101", "522227",
            "522228", "530101", "530102", "542333", "542334", "542335", "542336", "542429", "542626", "542627", "610000",
            "610100", "612728", "612729", "612730", "620100", "620101", "620102", "620103", "620402", "620421", "620422",
            "623001", "623021", "623022", "623027", "630000", "630100", "630101", "654202", "654325", "654326", "659000",
            "659001"
    };
    private static final int addressCodeCount = addressCode.length;
//    private static final int[] addressCodeValidateSum =;


    private static final int[] factor = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
    private static final Character[] lastChar = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
    private final SecureRandom random = new SecureRandom();

    private Character getLastCharacterOfIDCard(int[] digitArray) {
        int sum = 0;
        for (int sequenceIndex = 0; sequenceIndex < 17; sequenceIndex++) {
            sum += digitArray[sequenceIndex] * factor[sequenceIndex];
        }
        return lastChar[sum % 11];
    }

    @Override
    public String next() {
        int[] addressDigit = random.ints(0, 10).limit(6).toArray();
        int[] birthdayDigit = random.ints(0, 10).limit(8).toArray();
        int[] sequenceDigit = random.ints(0, 10).limit(3).toArray();
        int[] digitArray = new int[17];
        System.arraycopy(addressDigit, 0, digitArray, 0, 6);
        System.arraycopy(birthdayDigit, 0, digitArray, 6, 8);
        System.arraycopy(sequenceDigit, 0, digitArray, 14, 3);
        return Arrays.stream(digitArray).mapToObj(String::valueOf).reduce("", String::concat) + getLastCharacterOfIDCard(digitArray);
    }
}