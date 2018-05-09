package leapfrog_inc.summit.Http.Requester.Enum;

/**
 * Created by Leapfrog-Software on 2018/05/09.
 */

public enum GenderType {

    male,
    female,
    unknown;

    public static GenderType create(String value) {

        switch (value) {
            case "0":
                return GenderType.male;
            case "1":
                return GenderType.female;
            default:
                return GenderType.unknown;
        }
    }

    public String toValue() {

        switch (this) {
            case male:
                return "0";
            case female:
                return "1";
            default:
                return "";
        }
    }

    public String toText() {

        switch (this) {
            case male:
                return "男性";
            case female:
                return "女性";
            default:
                return "(未設定)";
        }
    }
}
