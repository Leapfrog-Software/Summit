package leapfrog_inc.summit.Http.Requester;

import org.json.JSONArray;
import org.json.JSONObject;

import leapfrog_inc.summit.Function.Base64Utility;
import leapfrog_inc.summit.Function.Constants;
import leapfrog_inc.summit.Http.HttpManager;

/**
 * Created by Leapfrog-Software on 2018/05/08.
 */

public class AccountRequester {

    public static void createUser(final CreateUserCallback callback) {

        HttpManager httpManager = new HttpManager(new HttpManager.HttpCallback() {
            @Override
            public void didReceiveData(boolean result, String data) {
                if (result) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        String ret = jsonObject.getString("result");
                        if (ret.equals("0")) {
                            JSONObject dataObject = jsonObject.getJSONObject("data");
                            String userId = dataObject.getString("userId");
                            callback.didReceiveData(true, userId);
                            return;
                        }
                    } catch(Exception e) {}
                }
                callback.didReceiveData(false, null);
            }
        });

        StringBuffer param = new StringBuffer();
        param.append("command=createUser");
        httpManager.execute(Constants.ServerApiUrl, "POST", param.toString());
    }

    public interface CreateUserCallback {
        void didReceiveData(boolean result, String userId);
    }

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
        param.append(("nameLast=" + Base64Utility.encode(userData.nameLast)));
        param.append("&");
        param.append(("nameFirst=" + Base64Utility.encode(userData.nameFirst)));
        param.append("&");
        param.append(("kanaLast=") + Base64Utility.encode(userData.kanaLast));
        param.append("&");
        param.append(("kanaFirst=" + Base64Utility.encode(userData.kanaFirst)));
        param.append("&");
        param.append(("age=" + userData.age.toValue()));
        param.append("&");
        param.append(("gender=" + userData.gender.toValue()));
        param.append("&");
        param.append(("job=") + Base64Utility.encode(userData.job));
        param.append("&");
        param.append(("company=") + Base64Utility.encode(userData.company));
        param.append("&");
        param.append(("position=") + Base64Utility.encode(userData.position));
        param.append("&");

        String reserves = "";
        for (int i = 0; i < userData.reserves.size(); i++) {
            if (reserves.length() > 0) {
                reserves += "-";
            }
            reserves += userData.reserves.get(i);
        }
        param.append(("reserves=" + reserves));
        param.append("&");

        String cards = "";
        for (int i = 0; i < userData.cards.size(); i++) {
            if (cards.length() > 0) {
                cards += "-";
            }
            cards += userData.cards.get(i);
        }
        param.append(("cards=" + cards));
        param.append("&");

        param.append(("message=" + Base64Utility.encode(userData.message)));

        httpManager.execute(Constants.ServerApiUrl, "POST", param.toString());
    }

    public interface UpdateUserCallback {
        void didReceiveData(boolean result);
    }
}
