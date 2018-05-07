package leapfrog_inc.summit.Function;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Leapfrog-Software on 2018/05/08.
 */

public class SaveData {

    private static SaveData container = null;

    public Context mContext;
    public String userId;
    public boolean pushSetting = true;

    private SaveData(){}

    public static SaveData getInstance(){
        if(container == null){
            container = new SaveData();
        }
        return container;
    }

    public void initialize(Context context) {

        mContext = context;

        SharedPreferences data = context.getSharedPreferences(Constants.SharedPreferenceKey.Key, Context.MODE_PRIVATE);

        userId = data.getString(Constants.SharedPreferenceKey.userId, "");
        pushSetting = data.getBoolean(Constants.SharedPreferenceKey.pushSetting, true);
    }

    public void save() {

        SharedPreferences data = mContext.getSharedPreferences(Constants.SharedPreferenceKey.Key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();

        editor.putBoolean(Constants.SharedPreferenceKey.pushSetting, pushSetting);
        editor.putString(Constants.SharedPreferenceKey.userId, userId);

        editor.apply();
    }
}
