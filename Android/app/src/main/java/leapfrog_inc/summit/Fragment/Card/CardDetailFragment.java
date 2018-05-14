package leapfrog_inc.summit.Fragment.Card;

import android.accounts.Account;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import leapfrog_inc.summit.Fragment.BaseFragment;
import leapfrog_inc.summit.Fragment.Common.Dialog;
import leapfrog_inc.summit.Fragment.Common.Loading;
import leapfrog_inc.summit.Function.Constants;
import leapfrog_inc.summit.Function.PicassoUtility;
import leapfrog_inc.summit.Function.SaveData;
import leapfrog_inc.summit.Http.Requester.AccountRequester;
import leapfrog_inc.summit.Http.Requester.ScheduleRequester;
import leapfrog_inc.summit.Http.Requester.UserRequester;
import leapfrog_inc.summit.R;

/**
 * Created by Leapfrog-Software on 2018/05/08.
 */

public class CardDetailFragment extends BaseFragment {

    private UserRequester.UserData mUserData;
    private boolean mNeedSendCard;

    public void set(UserRequester.UserData userData, boolean needSendCard) {
        mUserData = userData;
        mNeedSendCard = needSendCard;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_card_detail, null);

        initAction(view);
        initListView(view);

        if ((mNeedSendCard) && (mUserData.cards.contains(SaveData.getInstance().userId))) {
            setSendCardButtonDisable(view);
        }

        return view;
    }

    private void initAction(View view) {

        ((ImageButton)view.findViewById(R.id.backButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popFragment(AnimationType.horizontal);
            }
        });

        if (mNeedSendCard) {
            view.findViewById(R.id.sendCardLayout).setVisibility(View.VISIBLE);
            ((Button)view.findViewById(R.id.sendCardButton)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onTapSendCard();
                }
            });
        } else {
            view.findViewById(R.id.sendCardLayout).setVisibility(View.GONE);
        }
    }

    private void onTapSendCard() {

        mUserData.cards.add(SaveData.getInstance().userId);

        Loading.start(getActivity());

        AccountRequester.updateUser(mUserData, new AccountRequester.UpdateUserCallback() {
            @Override
            public void didReceiveData(boolean result) {

                Loading.stop(getActivity());

                if (result) {
                    Dialog.show(getActivity(), Dialog.Style.success, "確認", "名刺を送信しました", null);
                    setSendCardButtonDisable(getView());
                } else {
                    Dialog.show(getActivity(), Dialog.Style.success, "エラー", "通信に失敗しました", null);
                }
            }
        });
    }

    private void setSendCardButtonDisable(View view) {
        Button button = (Button)view.findViewById(R.id.sendCardButton);
        button.setText("名刺は送信済みです");
        button.setEnabled(false);
        button.setBackgroundResource(R.drawable.shape_card_detail_card_button_disable);
    }

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
        futureTitleAdapterData.scheduleTitle = String.format("参加予定のイベント(%d)", futureSchedules.size());
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
        pastTitleAdapterData.scheduleTitle = String.format("過去に参加したイベント(%d)", pastSchedules.size());
        adapter.add(pastTitleAdapterData);

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

                PicassoUtility.getUserImage(mContext, Constants.UserImageDirectory + data.userData.userId, (ImageView)convertView.findViewById(R.id.faceImageView));

                ((TextView)convertView.findViewById(R.id.nameTextView)).setText(data.userData.nameLast + " " + data.userData.nameFirst);
                ((TextView)convertView.findViewById(R.id.companyTextView)).setText(data.userData.company);
                ((TextView)convertView.findViewById(R.id.positionTextView)).setText(data.userData.position);
                ((TextView)convertView.findViewById(R.id.ageTextView)).setText(data.userData.age.toText());
                ((TextView)convertView.findViewById(R.id.genderTextView)).setText(data.userData.gender.toText());
                ((TextView)convertView.findViewById(R.id.messageTextView)).setText(data.userData.message);

                return convertView;

            } else if (data.scheduleTitle != null) {
                convertView = mInflater.inflate(R.layout.adapter_card_detail_schedule_title, parent, false);
                ((TextView)convertView.findViewById(R.id.titleTextView)).setText(data.scheduleTitle);
                return convertView;

            } else if (data.scheduleData != null) {
                convertView = mInflater.inflate(R.layout.adapter_card_detail_schedule, parent, false);
                ((TextView)convertView.findViewById(R.id.titleTextView)).setText(data.scheduleData.title);

                SimpleDateFormat format = new SimpleDateFormat("yyyy/M/d");
                String datetime = format.format(data.scheduleData.datetime.getTime());
                ((TextView)convertView.findViewById(R.id.dateTextView)).setText(datetime);

                ((TextView)convertView.findViewById(R.id.providerTextView)).setText(data.scheduleData.provider);
                ((TextView)convertView.findViewById(R.id.descriptionTextView)).setText(data.scheduleData.description);

                return convertView;

            } else {
                return mInflater.inflate(R.layout.adapter_card_detail_nodata, parent, false);
            }
        }
    }
}
