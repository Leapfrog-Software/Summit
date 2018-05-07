package leapfrog_inc.summit.Function;

/**
 * Created by Leapfrog-Software on 2018/05/08.
 */

public class Constants {

    //public static String ServerRootUrl = "http://appfac.net/";
    public static String ServerRootUrl = "http://10.0.2.2/summit/";
    public static String ServerApiUrl = Constants.ServerRootUrl + "srv.php";
    public static String UserImageDirectory = Constants.ServerRootUrl + "data/img/user/";

    public static int HttpConnectTimeout = 10000;
    public static int HttpReadTimeout = 10000;

    public static class SharedPreferenceKey {
        public static String Key = "Summit";
        public static String pushSetting = "PushSetting";
    }

    // TODO
    public static class WebPageUrl {
        public static String terms = "http://lfg.co.jp/";
    }
}
