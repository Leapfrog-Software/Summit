package leapfrog_inc.summit.Http.Requester;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import leapfrog_inc.summit.Function.Constants;
import leapfrog_inc.summit.Http.HttpManager;

/**
 * Created by Leapfrog-Software on 2018/05/08.
 */

public class ScheduleRequester {

    public enum ScheduleType {
        party,
        seminar,
        meeting;

        static ScheduleType init(String value) {
            switch (value) {
                case "0":
                    return ScheduleType.party;
                case "1":
                    return ScheduleType.seminar;
                case "2":
                    return ScheduleType.meeting;
            }
            return null;
        }
    }

    public static class ScheduleData {

        public String id = "";
        public ScheduleType type;
        public String title = "";
        public Calendar datetime;
        public int timeLength = 0;
        public String provider = "";
        public String description = "";
        public String filter = "";

        static public ScheduleData create(JSONObject json) {

            try {
                String id = json.getString("id");
                ScheduleType type = ScheduleType.init(json.getString("type"));
                if (type == null) return null;

                String title = json.getString("title");

                String datetimeStr = json.getString("datetime");
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                Date datetime = format.parse(datetimeStr);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(datetime);

                int timeLength = Integer.valueOf(json.getString("timeLength"));

                String provider = json.getString("provider");
                String description = json.getString("description");
                String filter = json.getString("filter");

                ScheduleData scheduleData = new ScheduleData();
                scheduleData.id = id;
                scheduleData.type = type;
                scheduleData.title = title;
                scheduleData.datetime = calendar;
                scheduleData.timeLength = timeLength;
                scheduleData.provider = provider;
                scheduleData.description = description;
                scheduleData.filter = filter;
                return scheduleData;

            } catch(Exception e) {}

            return null;
        }
    }

    private static ScheduleRequester requester = new ScheduleRequester();

    private ScheduleRequester(){}

    public static ScheduleRequester getInstance(){
        return requester;
    }

    private ArrayList<ScheduleData> mDataList = new ArrayList<ScheduleData>();

    public void fetch(final ScheduleRequesterCallback callback){

        HttpManager httpManager = new HttpManager(new HttpManager.HttpCallback() {
            @Override
            public void didReceiveData(boolean result, String data) {
                if (result) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        String ret = jsonObject.getString("result");
                        if (ret.equals("0")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            ArrayList<ScheduleData> dataList = new ArrayList<ScheduleData>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                ScheduleData scheduleData = ScheduleData.create(jsonArray.getJSONObject(i));
                                if (scheduleData != null) {
                                    dataList.add(scheduleData);
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
        param.append("command=getSchedule");
        httpManager.execute(Constants.ServerApiUrl, "POST", param.toString());
    }

    public interface ScheduleRequesterCallback {
        void didReceiveData(boolean result);
    }

    public ArrayList<ScheduleData> getDataList() {
        return mDataList;
    }

    public ArrayList<ScheduleData> query(Calendar calendar) {

        ArrayList<ScheduleData> ret = new ArrayList<ScheduleData>();

        for (int i = 0; i < mDataList.size(); i++) {
            ScheduleData scheduleData = mDataList.get(i);
            if ((scheduleData.datetime.get(Calendar.YEAR) == calendar.get(Calendar.YEAR))
                    && (scheduleData.datetime.get(Calendar.MONTH) == calendar.get(Calendar.MONTH))
                    && (scheduleData.datetime.get(Calendar.DATE) == calendar.get(Calendar.DATE))) {
                ret.add(scheduleData);
            }
        }
        return ret;
    }

}
