package leapfrog_inc.summit.Http.Requester;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import leapfrog_inc.summit.Function.Constants;
import leapfrog_inc.summit.Function.SaveData;
import leapfrog_inc.summit.Http.HttpManager;

/**
 * Created by Leapfrog-Software on 2018/05/08.
 */

public class AttendRequester {

    public static class AttendData {

        public String tableId;
        public ArrayList<String> userIds = new ArrayList<String>();

        static public AttendData create(JSONObject json) {

            try {
                String tableId = json.getString("tableId");
                JSONArray userIds = json.getJSONArray("userIds");

                AttendData attendData = new AttendData();
                attendData.tableId = tableId;
                for (int i = 0; i < userIds.length(); i++) {
                    attendData.userIds.add(userIds.getString(i));
                }
                return attendData;

            } catch(Exception e) {}

            return null;
        }
    }

    public static class ChatData {

        public String id;
        public String senderId;
        public String tableId;
        public Calendar datetime;
        public String chat;

        static public ChatData create(JSONObject json) {

            try {
                String id = json.getString("id");
                String senderId = json.getString("senderId");
                String tableId = json.getString("tableId");

                String datetimeStr = json.getString("datetime");
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddkkmmss");
                format.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
                Date datetime = format.parse(datetimeStr);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(datetime);

                String chat = json.getString("chat");

                ChatData chatData = new ChatData();
                chatData.id = id;
                chatData.senderId = senderId;
                chatData.tableId = tableId;
                chatData.datetime = calendar;
                chatData.chat = chat;
                return chatData;

            } catch(Exception e) {}

            return null;
        }
    }

    private String mScheduleId;
    private ArrayList<AttendData> mAttendList = new ArrayList<AttendData>();
    private ArrayList<ChatData> mChatList = new ArrayList<ChatData>();

    public void initialize(String scheduleId) {
        mScheduleId = scheduleId;
    }

    public void attend(String tableId, final AttendRequesterCallback callback){

        mAttendList.clear();
        mChatList.clear();

        HttpManager httpManager = new HttpManager(new HttpManager.HttpCallback() {
            @Override
            public void didReceiveData(boolean result, String data) {
                if (result) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        String ret = jsonObject.getString("result");
                        if (ret.equals("0")) {
                            JSONArray attends = jsonObject.getJSONArray("attends");
                            for (int i = 0; i < attends.length(); i++) {
                                AttendData attendData = AttendData.create(attends.getJSONObject(i));
                                if (attendData != null) {
                                    mAttendList.add(attendData);
                                }
                            }
                            JSONArray chats = jsonObject.getJSONArray("chats");
                            for (int i = 0; i < chats.length(); i++) {
                                ChatData chatData = ChatData.create(chats.getJSONObject(i));
                                if (chatData != null) {
                                    mChatList.add(chatData);
                                }
                            }
                            callback.didReceiveData(true);
                            return;
                        }
                    } catch(Exception e) {}
                }
                callback.didReceiveData(false);
            }
        });
        StringBuffer param = new StringBuffer();
        param.append("command=attend");
        param.append("&");
        param.append(("scheduleId=" + mScheduleId));
        param.append("&");
        param.append(("tableId=" + tableId));
        param.append("&");
        param.append(("userId=" + SaveData.getInstance().userId));
        httpManager.execute(Constants.ServerApiUrl, "POST", param.toString());
    }

    public interface AttendRequesterCallback {
        void didReceiveData(boolean result);
    }

    public void postChat(String chatId, String tableId, String chat, final PostChatCallback callback) {

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
        param.append("command=postChat");
        param.append("&");
        param.append(("scheduleId=" + mScheduleId));
        param.append("&");
        param.append(("chatId=" + chatId));
        param.append("&");
        param.append(("tableId=" + tableId));
        param.append("&");
        param.append(("senderId=" + SaveData.getInstance().userId));
        param.append("&");
        param.append(("chat=" + chat));
        httpManager.execute(Constants.ServerApiUrl, "POST", param.toString());
    }

    public interface PostChatCallback {
        void didReceiveData(boolean result);
    }

    public ArrayList<ChatData> queryChatList(int tableIndex) {

        ArrayList<ChatData> ret = new ArrayList<ChatData>();
        for (int i = 0; i < mChatList.size(); i++) {
            ChatData chatData = mChatList.get(i);
            if (chatData.tableId.equals(String.format("%d", tableIndex))) {
                ret.add(chatData);
            }
        }
        return ret;
    }

    public ArrayList<AttendData> getAttendList() {
        return mAttendList;
    }

    public ArrayList<String> queryAttendUserIds(int tableIndex) {

        for (int i = 0; i < mAttendList.size(); i++) {
            AttendData attendData = mAttendList.get(i);
            if (attendData.tableId.equals(String.format("%d", tableIndex))) {
                return attendData.userIds;
            }
        }
        return new ArrayList<String>();
    }
}

