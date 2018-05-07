package leapfrog_inc.summit.Http.Requester;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import leapfrog_inc.summit.Function.Constants;
import leapfrog_inc.summit.Function.KanaUtils;
import leapfrog_inc.summit.Function.SaveData;
import leapfrog_inc.summit.Http.HttpManager;

/**
 * Created by Leapfrog-Software on 2018/05/08.
 */

public class UserRequester {

    public static class UserData {

        public String userId = "";
        public String nameFirst = "";
        public String nameLast = "";
        public String kanaFirst = "";
        public String kanaLast = "";
        public String job = "";
        public String position = "";
        public ArrayList<String> reserves = new ArrayList<String>();
        public ArrayList<String> cards = new ArrayList<String>();

        static public UserData create(JSONObject json) {

            try {
                String userId = json.getString("id");
                String nameFirst = json.getString("nameFirst");
                String nameLast = json.getString("nameLast");
                String kanaFirst = json.getString("kanaFirst");
                String kanaLast = json.getString("kanaLast");
                String job = json.getString("job");
                String position = json.getString("position");
                JSONArray reserves = json.getJSONArray("reserves");
                JSONArray cards = json.getJSONArray("cards");

                UserData userData = new UserData();
                userData.userId = userId;
                userData.nameFirst = nameFirst;
                userData.nameLast = nameLast;
                userData.kanaFirst = kanaFirst;
                userData.kanaLast = kanaLast;
                userData.job = job;
                userData.position = position;

                for (int i = 0; i < reserves.length(); i++) {
                    String reserve = reserves.getString(i);
                    if (!userData.reserves.contains(reserve)) {
                        userData.reserves.add(reserve);
                    }
                }

                for (int i = 0; i < cards.length(); i++) {
                    String card = cards.getString(i);
                    if (!userData.cards.contains(card)) {
                        userData.cards.add(card);
                    }
                }

                return userData;

            } catch(Exception e) {}

            return null;
        }
    }

    private static UserRequester requester = new UserRequester();

    private UserRequester(){}

    public static UserRequester getInstance(){
        return requester;
    }

    private ArrayList<UserData> mDataList = new ArrayList<UserData>();

    public void fetch(final UserRequesterCallback callback){

        HttpManager httpManager = new HttpManager(new HttpManager.HttpCallback() {
            @Override
            public void didReceiveData(boolean result, String data) {
                if (result) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        String ret = jsonObject.getString("result");
                        if (ret.equals("0")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            ArrayList<UserData> dataList = new ArrayList<UserData>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                UserData userData = UserData.create(jsonArray.getJSONObject(i));
                                if (userData != null) {
                                    dataList.add(userData);
                                }
                            }
                            mDataList = sortUserList(dataList);
                            callback.didReceiveData(true);
                            return;
                        }
                    } catch(Exception e) {}
                }
                callback.didReceiveData(false);
            }
        });
        StringBuffer param = new StringBuffer();
        param.append("command=getUser");
        httpManager.execute(Constants.ServerRootUrl, "POST", param.toString());
    }

    private ArrayList<UserData> sortUserList(ArrayList<UserData> userList) {

        ArrayList<UserData> src = new ArrayList<>(userList);
        ArrayList<UserData> dst = new ArrayList<UserData>();

        int count = src.size();
        for (int i = count - 1; i >= 0; i--) {
            int minIndex = -1;
            String minUserKana = "";

            for (int j = 0; j < src.size(); j++) {
                if (minIndex == -1) {
                    minIndex = j;
                    UserData userData = src.get(j);
                    minUserKana = userData.kanaLast + userData.kanaFirst;
                }
                UserData currentUserData = src.get(j);
                String kana = currentUserData.kanaLast + currentUserData.kanaFirst;
                if (KanaUtils.compare(kana, minUserKana) == true) {
                    minIndex = j;
                    minUserKana = kana;
                }
            }
            dst.add(src.get(minIndex));
            src.remove(minIndex);
        }
        return dst;
    }

    public interface UserRequesterCallback {
        void didReceiveData(boolean result);
    }

    public ArrayList<UserData> getUserList() {
        return mDataList;
    }

    public UserData myUserData() {

        String userId = SaveData.getInstance().userId;
        if (userId.length() == 0) return null;

        for (int i = 0; i < mDataList.size(); i++) {
            if (mDataList.get(i).userId.equals(userId)) {
                return mDataList.get(i);
            }
        }
        return null;
    }
}
