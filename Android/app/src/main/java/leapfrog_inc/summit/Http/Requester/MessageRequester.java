package leapfrog_inc.summit.Http.Requester;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import leapfrog_inc.summit.Function.Constants;
import leapfrog_inc.summit.Function.SaveData;
import leapfrog_inc.summit.Http.HttpManager;

/**
 * Created by Leapfrog-Software on 2018/05/08.
 */

public class MessageRequester {

    public static class MessageData {

        public String senderId;
        public String receiverId;
        public String message;
        public Date datetime;

        static public MessageData create(JSONObject json) {

            try {
                String senderId = json.getString("senderId");
                String receiverId = json.getString("receiverId");
                String message = json.getString("message");

                String datetimeStr = json.getString("datetime");
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                Date datetime = format.parse(datetimeStr);

                MessageData messageData = new MessageData();
                messageData.senderId = senderId;
                messageData.receiverId = receiverId;
                messageData.message = message;
                messageData.datetime = datetime;
                return messageData;

            } catch(Exception e) {}

            return null;
        }
    }

    private static MessageRequester requester = new MessageRequester();

    private MessageRequester(){}

    public static MessageRequester getInstance(){
        return requester;
    }

    private ArrayList<MessageData> mDataList = new ArrayList<MessageData>();

    public void fetch(final MessageRequesterCallback callback){

        HttpManager httpManager = new HttpManager(new HttpManager.HttpCallback() {
            @Override
            public void didReceiveData(boolean result, String data) {
                if (result) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        String ret = jsonObject.getString("result");
                        if (ret.equals("0")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            ArrayList<MessageData> dataList = new ArrayList<MessageData>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                MessageData messageData = MessageData.create(jsonArray.getJSONObject(i));
                                if (messageData != null) {
                                    dataList.add(messageData);
                                }
                            }
                            mDataList = dataList;
                            callback.didReceiveData(true);
                            return;
                        }
                    } catch(Exception e) {}
                }
                callback.didReceiveData(false);
            }
        });
        StringBuffer param = new StringBuffer();
        param.append("command=getMessage");
        param.append("&");
        param.append(("userId=" + SaveData.getInstance().userId));
        httpManager.execute(Constants.ServerRootUrl, "POST", param.toString());
    }

    public interface MessageRequesterCallback {
        void didReceiveData(boolean result);
    }

    public void post(String targetId, String message, final MessagePostRequesterCallback callback) {

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
        param.append("command=postMessage");
        param.append("&");
        param.append(("senderId=" + SaveData.getInstance().userId));
        param.append("&");
        param.append(("receiverId=" + targetId));
        param.append("&");
        param.append(("message=" + message));
        httpManager.execute(Constants.ServerRootUrl, "POST", param.toString());
    }

    public interface MessagePostRequesterCallback {
        void didReceiveData(boolean result);
    }
}