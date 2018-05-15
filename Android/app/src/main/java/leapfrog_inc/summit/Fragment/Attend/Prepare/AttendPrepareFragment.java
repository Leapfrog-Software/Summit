package leapfrog_inc.summit.Fragment.Attend.Prepare;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import leapfrog_inc.summit.Fragment.Attend.Attend.AttendFragment;
import leapfrog_inc.summit.Fragment.Attend.Match.AttendMatchFragment;
import leapfrog_inc.summit.Fragment.BaseFragment;
import leapfrog_inc.summit.Function.Constants;
import leapfrog_inc.summit.Function.DeviceUtility;
import leapfrog_inc.summit.Function.PicassoUtility;
import leapfrog_inc.summit.Function.SaveData;
import leapfrog_inc.summit.Http.Requester.ScheduleRequester;
import leapfrog_inc.summit.Http.Requester.UserRequester;
import leapfrog_inc.summit.R;

/**
 * Created by Leapfrog-Software on 2018/05/11.
 */

public class AttendPrepareFragment extends BaseFragment {

    private ScheduleRequester.ScheduleData mScheduleData;
    private UserRequester.UserData mMatchUserData;
    private int mTableIndex;
    private boolean didStackFragment = false;

    public void set(ScheduleRequester.ScheduleData scheduleData) {
        mScheduleData = scheduleData;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_attend_prepare, null);

        initAction(view);
        initContent(view);

        return view;
    }

    private void initAction(View view) {

        ((ImageButton)view.findViewById(R.id.backButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popFragment(AnimationType.vertical);
            }
        });
    }

    private void initContent(View view) {

        // 画像
        ImageView scheduleImageView = (ImageView)view.findViewById(R.id.scheduleImageView);
        PicassoUtility.getScheduleImage(getActivity(), Constants.ScheduleImageDirectory + mScheduleData.id, scheduleImageView);
        ViewGroup.LayoutParams imageViewParams = scheduleImageView.getLayoutParams();
        imageViewParams.height = (int)(DeviceUtility.getWindowSize(getActivity()).x * 2 / 3);
        scheduleImageView.setLayoutParams(imageViewParams);

        // タイトル
        ((TextView)view.findViewById(R.id.nameTextView)).setText("【" + mScheduleData.title + "】");

        // 日付
        SimpleDateFormat format = new SimpleDateFormat("yyyy年M月d日 hh:mm");
        format.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
        ((TextView)view.findViewById(R.id.dateTextView)).setText(format.format(mScheduleData.datetime.getTime()));

        ArrayList<UserRequester.UserData> userList = UserRequester.getInstance().queryReservedUser(mScheduleData.id);

        // 参加者数
        ((TextView)view.findViewById(R.id.memberCountTextView)).setText(String.format("(%d名)", userList.size()));

        // 提供
        ((TextView)view.findViewById(R.id.providerTextView)).setText(mScheduleData.provider);

        // 詳細
        ((TextView)view.findViewById(R.id.descriptionTextView)).setText(mScheduleData.description);

        // 参加者リスト
        LinearLayout memberLayout = (LinearLayout)view.findViewById(R.id.memberLayout);
        memberLayout.addView(createPaddingView(16));
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).userId.equals(SaveData.getInstance().userId)) continue;
            ImageView imageView = new ImageView(getActivity());
            float density = DeviceUtility.getDeviceDensity(getActivity());
            imageView.setLayoutParams(new ViewGroup.LayoutParams((int)(34 * density), (int)(34 * density)));
            memberLayout.addView(imageView);
            PicassoUtility.getUserImage(getActivity(), Constants.UserImageDirectory + userList.get(i).userId, imageView);

            memberLayout.addView(createPaddingView(8));
        }
        memberLayout.addView(createPaddingView(16));

        createInitialInfo(userList);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!SaveData.getInstance().sentFirstMessageScheduleIds.contains(mScheduleData.id)) {
                    stackMatch();
                } else if (mScheduleData.datetime.getTime().getTime() < (new Date()).getTime()) {
                    stackAttend();
                }
            }
        }, 2000);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (!didStackFragment) {
                    timerProc();
                }
                new Handler().postDelayed(this, 1000);
            }
        };
        new Handler().post(runnable);
    }

    private View createPaddingView(int width) {
        View view = new View(getActivity());
        float density = DeviceUtility.getDeviceDensity(getActivity());
        view.setLayoutParams(new ViewGroup.LayoutParams((int)((float)width * density), 1));
        return view;
    }

    private void createInitialInfo(ArrayList<UserRequester.UserData> userList) {

        int targetIndex = -1;
        int tableIndex = -1;

        int index = -1;
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).userId.equals(SaveData.getInstance().userId)) {
                index = i;
            }
        }
        if (index == -1) return;

        if (index % 2 == 1) {
            targetIndex = index - 1;
            tableIndex = (int)((index - 1) / 2);
        } else {
            if (index + 1 >= userList.size()) {
                if (userList.size() >= 2) {
                    targetIndex = index - 1;
                    tableIndex = (int)((index - 1) / 2);
                } else {
                    return;
                }
            } else {
                targetIndex = index + 1;
                tableIndex = (int)(index / 2);
            }
        }

        mMatchUserData = userList.get(targetIndex);
        mTableIndex = tableIndex;
    }

    private void stackMatch() {

        didStackFragment = true;

        AttendMatchFragment fragment = new AttendMatchFragment();
        fragment.set(mScheduleData, mMatchUserData, mTableIndex, new AttendMatchFragment.AttendMatchFragmentCallback() {
            @Override
            public void didSendMessage() {
                if (mScheduleData.datetime.getTime().getTime() - (new Date()).getTime() >= 0) {
                    stackAttend();
                }
                didStackFragment = false;
            }
        });
        stackFragment(fragment, AnimationType.none);
    }

    private void stackAttend() {

        if (didStackFragment) {
            return;
        }
        didStackFragment = true;

        View view = getView();
        if (view == null) return;

        AlphaAnimation alpha = new AlphaAnimation(0, 1);
        alpha.setDuration(300);
        View blackView = view.findViewById(R.id.blackView);
        blackView.setVisibility(View.VISIBLE);
        blackView.startAnimation(alpha);

        AttendFragment fragment = new AttendFragment();
        fragment.set(mScheduleData, mTableIndex);
        stackFragment(fragment, AnimationType.none);
    }

    private void timerProc() {

        View view = getView();
        if (view == null) return;

        int remainTime = (int)(mScheduleData.datetime.getTime().getTime() - (new Date()).getTime()) / 1000;
        if (remainTime > 0) {
            String remainMinutes = String.format("%02d", remainTime / 60);
            String remainSecondss = String.format("%02d", remainTime % 60);
            ((TextView)view.findViewById(R.id.remainTimeTextView)).setText(remainMinutes + ":" + remainSecondss);
        } else {
            ((TextView)view.findViewById(R.id.remainTimeTitleTextView)).setText("");
            ((TextView)view.findViewById(R.id.remainTimeTextView)).setText("開催中");

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    stackAttend();
                }
            }, 2000);
        }
    }
}
