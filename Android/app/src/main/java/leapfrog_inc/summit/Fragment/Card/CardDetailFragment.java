package leapfrog_inc.summit.Fragment.Card;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;

import leapfrog_inc.summit.Fragment.BaseFragment;
import leapfrog_inc.summit.Http.Requester.ScheduleRequester;
import leapfrog_inc.summit.Http.Requester.UserRequester;
import leapfrog_inc.summit.R;

/**
 * Created by Leapfrog-Software on 2018/05/08.
 */

public class CardDetailFragment extends BaseFragment {

    private UserRequester.UserData mUserData;

    public void set(UserRequester.UserData userData) {
        mUserData = userData;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_card_detail, null);

        initListView(view);

        return view;
    }

    // 0508
    private void initListView(View view) {

        CardDetailAdapater adapter = new CardDetailAdapater(getActivity());

        CardDetailAdapterData userAdapterData = new CardDetailAdapterData();
        userAdapterData.userData = mUserData;
        adapter.add(userAdapterData);

        Date current = new Date();

        ArrayList<ScheduleRequester.ScheduleData> allSchedules = ScheduleRequester.getInstance().getDataList();
        ArrayList<ScheduleRequester.ScheduleData> futureSchedules = new ArrayList<ScheduleRequester.ScheduleData>();
        ArrayList<ScheduleRequester.ScheduleData> pastSchedules = new ArrayList<ScheduleRequester.ScheduleData>();
        for (int i = 0; i < allSchedules.size(); i++) {
            ScheduleRequester.ScheduleData scheduleData = allSchedules.get(i);
            if (mUserData.reserves.contains(scheduleData.id)) {
                Date scheduleDate = scheduleData.datetime.getTime();
                long timeDiff = scheduleDate.getTime() - current.getTime();
                if (timeDiff > 0) {
                    futureSchedules.add(scheduleData);
                } else {
                    pastSchedules.add(scheduleData);
                }
            }
        }

        CardDetailAdapterData futureTitleAdapterData = new CardDetailAdapterData();
        futureTitleAdapterData.scheduleTitle = "参加予定のイベント";
        futureTitleAdapterData.scheduleTitleCount = futureSchedules.size();
        adapter.add(futureTitleAdapterData);

        if (futureSchedules.size() == 0) {
            adapter.add(new CardDetailAdapterData());
        } else {
            for (int i = 0; i < futureSchedules.size(); i++) {
                CardDetailAdapterData futureAdapterData = new CardDetailAdapterData();
                futureAdapterData.scheduleData = futureSchedules.get(i);
                adapter.add(futureAdapterData);
            }
        }

        CardDetailAdapterData pastTitleAdapterData = new CardDetailAdapterData();
        futureTitleAdapterData.scheduleTitle = "過去に参加したイベント";
        futureTitleAdapterData.scheduleTitleCount = pastSchedules.size();
        adapter.add(futureTitleAdapterData);

        if (pastSchedules.size() == 0) {
            adapter.add(new CardDetailAdapterData());
        } else {
            for (int i = 0; i < pastSchedules.size(); i++) {
                CardDetailAdapterData pastAdapterData = new CardDetailAdapterData();
                pastAdapterData.scheduleData = pastSchedules.get(i);
                adapter.add(pastAdapterData);
            }
        }

        ListView listView = (ListView)view.findViewById(R.id.listView);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
    }


    private class CardDetailAdapterData {
        UserRequester.UserData userData;
        String scheduleTitle;
        int scheduleTitleCount;
        ScheduleRequester.ScheduleData scheduleData;
    }

    private class CardDetailAdapater extends ArrayAdapter<CardDetailAdapterData> {

        LayoutInflater mInflater;
        Context mContext;

        public CardDetailAdapater(Context context){
            super(context, 0);
            mInflater = LayoutInflater.from(context);
            mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            CardDetailAdapterData data = getItem(position);
            if (data.userData != null) {
                convertView = mInflater.inflate(R.layout.adapter_card_detail_user, parent, false);


                return convertView;
            } else if (data.scheduleTitle != null) {
                convertView = mInflater.inflate(R.layout.adapter_card_detail_schedule_title, parent, false);


                return convertView;
            } else if (data.scheduleData != null) {
                convertView = mInflater.inflate(R.layout.adapter_card_detail_schedule, parent, false);


                return convertView;
            } else {
                convertView = mInflater.inflate(R.layout.adapter_card_detail_nodata, parent, false);


                return convertView;
            }
        }
    }
}
