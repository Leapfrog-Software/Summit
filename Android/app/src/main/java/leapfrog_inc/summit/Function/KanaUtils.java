package leapfrog_inc.summit.Function;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Leapfrog-Software on 2018/05/08.
 */

public class KanaUtils {

    public static ArrayList<ArrayList<String>> kanas() {

        ArrayList<ArrayList<String>> ret = new ArrayList<ArrayList<String>>();

        ret.add(new ArrayList<String>(Arrays.asList("あ", "い", "う", "え", "お")));
        ret.add(new ArrayList<String>(Arrays.asList("か", "き", "く", "け", "こ", "が", "ぎ", "ぐ", "げ", "ご")));
        ret.add(new ArrayList<String>(Arrays.asList("さ", "し", "す", "せ", "そ", "ざ", "じ", "ず", "ぜ", "ぞ")));
        ret.add(new ArrayList<String>(Arrays.asList("た", "ち", "つ", "っ", "て", "と", "だ", "ぢ", "づ", "で", "ど")));
        ret.add(new ArrayList<String>(Arrays.asList("な", "に", "ぬ", "ね", "の")));
        ret.add(new ArrayList<String>(Arrays.asList("は", "ひ", "ふ", "へ", "ほ", "ば", "び", "ぶ", "べ", "ぼ", "ぱ", "ぴ", "ぷ", "ぺ", "ぽ")));
        ret.add(new ArrayList<String>(Arrays.asList("ま", "み", "む", "め", "も")));
        ret.add(new ArrayList<String>(Arrays.asList("や", "ゃ", "ゆ", "ゅ", "よ", "ょ")));
        ret.add(new ArrayList<String>(Arrays.asList("ら", "り", "る", "れ", "ろ")));
        ret.add(new ArrayList<String>(Arrays.asList("わ", "を", "ん")));

        return ret;
    }

    public static int columnIndex(String s) {

        ArrayList<ArrayList<String>> kanas = KanaUtils.kanas();

        for (int i = 0; i < kanas.size(); i++) {
            if (kanas.get(i).get(0).equals(s)) {
                return i;
            }
        }
        return -1;
    }

    private static int compareChar(String c1, String c2) {

        int column1 = KanaUtils.columnIndex(c1);
        if (column1 == -1) {
            return -1;
        }
        int column2 = KanaUtils.columnIndex(c2);
        if (column2 == -1) {
            return 1;
        }

        if (column1 < column2) {
            return 1;
        } else if (column1 > column2) {
            return -1;
        } else {
            ArrayList<String> kanas1 = KanaUtils.kanas().get(column1);
            ArrayList<String> kanas2 = KanaUtils.kanas().get(column2);

            int i1 = kanas1.indexOf(c1);
            int i2 = kanas2.indexOf(c2);
            if (i1 < i2) {
                return 1;
            } else if (i1 > i2) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    public static boolean compare(String s1, String s2) {

        for (int i = 0; i < s1.length(); i++) {
            if (s2.length() <= i) {
                return false;
            }
            String c1 = String.valueOf(s1.charAt(i));
            String c2 = String.valueOf(s2.charAt(i));
            int res = KanaUtils.compareChar(c1, c2);
            if (res == 1) {
                return true;
            } else if (res == -1) {
                return false;
            }
        }
        return true;
    }
}
