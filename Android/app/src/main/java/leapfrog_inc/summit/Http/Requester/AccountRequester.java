package leapfrog_inc.summit.Http.Requester;

import org.json.JSONObject;

import leapfrog_inc.summit.Function.Constants;
import leapfrog_inc.summit.Http.HttpManager;

/**
 * Created by Leapfrog-Software on 2018/05/08.
 */

public class AccountRequester {

    public static void updateUser(UserRequester.UserData userData, final UpdateUserCallback callback){

        HttpManager httpManager = new HttpManager(new HttpManager.HttpCallback() {
            @Override
            public void didReceiveData(boolean result, String data) {
                if (result) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        String ret = jsonObject.getString("result");
                        if (ret.equals("0")) {
                            callback.didReceiveData(true);
                            return;
                        }
                    } catch(Exception e) {}
                }
                callback.didReceiveData(false);
            }
        });
        StringBuffer param = new StringBuffer();
        param.append("command=updateUser");
        param.append("&");
        param.append(("userId=") + userData.userId);
        param.append("&");
        param.append(("nameLast=" + userData.nameLast));
        param.append("&");
        param.append(("nameFirst=" + userData.nameFirst));
        param.append("&");
        param.append(("kanaLast=") + userData.kanaLast);
        param.append("&");
        param.append(("kanaFirst=" + userData.kanaFirst));
        param.append("&");
        param.append(("job=") + userData.job);
        param.append("&");
        param.append(("position=") + userData.position);
        param.append("&");

        String reserves = "";
        for (int i = 0; i < userData.reserves.size(); i++) {
            if (reserves.length() > 0) {
                reserves += "-";
            }
            reserves += userData.reserves.get(i);
        }
        param.append(("reserves=") + reserves);
        param.append("&");

        String cards = "";
        for (int i = 0; i < userData.cards.size(); i++) {
            if (cards.length() > 0) {
                cards += "-";
            }
            cards += userData.cards.get(i);
        }
        param.append(cards);

        httpManager.execute(Constants.ServerRootUrl, "POST", param.toString());
    }

    public interface UpdateUserCallback {
        void didReceiveData(boolean result);
    }
}
