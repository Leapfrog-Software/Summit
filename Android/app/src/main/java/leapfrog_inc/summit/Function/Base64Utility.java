package leapfrog_inc.summit.Function;

import android.util.Base64;

/**
 * Created by Leapfrog-Software on 2018/05/08.
 */

public class Base64Utility {

    public static String encode(String string) {
        String encoded = new String(Base64.encode(string.getBytes(), Base64.URL_SAFE | Base64.NO_WRAP));
        return (encoded == null) ? "" : encoded;
    }

    public static String decode(String string) {
        String decoded = new String(Base64.decode(string, Base64.URL_SAFE | Base64.NO_WRAP));
        return (decoded == null) ? "" : decoded;
    }
}
