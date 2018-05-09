package leapfrog_inc.summit.Http.Requester.Enum;

/**
 * Created by Leapfrog-Software on 2018/05/09.
 */

public enum AgeType {

    u20,
    s20,
    s30,
    s40,
    s50,
    o60,
    unknown;

    public static AgeType create(String value) {

        switch (value) {
            case "0":
                return AgeType.u20;
            case "1":
                return AgeType.s20;
            case "2":
                return AgeType.s30;
            case "3":
                return AgeType.s40;
            case "4":
                return AgeType.s50;
            case "5":
                return AgeType.o60;
            default:
                return AgeType.unknown;
        }
    }

    public String toValue() {

        switch (this) {
            case u20:
                return "0";
            case s20:
                return "1";
            case s30:
                return "2";
            case s40:
                return "3";
            case s50:
                return "4";
            case o60:
                return "5";
            default:
                return "";
        }
    }

    public String toText() {

        switch (this) {
            case u20:
                return "20歳未満";
            case s20:
                return "20代";
            case s30:
                return "30代";
            case s40:
                return "40代";
            case s50:
                return "50代";
            case o60:
                return "60代";
            default:
                return "(未設定)";
        }
    }
}
