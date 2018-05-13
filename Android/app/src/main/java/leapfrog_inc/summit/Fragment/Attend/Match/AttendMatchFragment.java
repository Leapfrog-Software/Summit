package leapfrog_inc.summit.Fragment.Attend.Match;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import leapfrog_inc.summit.Fragment.BaseFragment;
import leapfrog_inc.summit.Fragment.Common.Dialog;
import leapfrog_inc.summit.Fragment.Common.Loading;
import leapfrog_inc.summit.Function.Constants;
import leapfrog_inc.summit.Function.DeviceUtility;
import leapfrog_inc.summit.Function.PicassoUtility;
import leapfrog_inc.summit.Function.SaveData;
import leapfrog_inc.summit.Http.Requester.AttendRequester;
import leapfrog_inc.summit.Http.Requester.ScheduleRequester;
import leapfrog_inc.summit.Http.Requester.UserRequester;
import leapfrog_inc.summit.R;

/**
 * Created by Leapfrog-Software on 2018/05/11.
 */

public class AttendMatchFragment extends BaseFragment {

    private ScheduleRequester.ScheduleData mScheduleData;
    private UserRequester.UserData mUserData;
    private int mTableIndex;
    private AttendMatchFragmentCallback mCallback;

    public void set(ScheduleRequester.ScheduleData scheduleData, UserRequester.UserData userData, int tableIndex, AttendMatchFragmentCallback callback) {
        mScheduleData = scheduleData;
        mUserData = userData;
        mTableIndex = tableIndex;
        mCallback = callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_attend_match, null);

        initAction(view);
        initContent(view);

        return view;
    }

    private void initAction(View view) {

        ((Button)view.findViewById(R.id.sendButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View view = getView();
                if (view == null) return;

                String chat = ((EditText)view.findViewById(R.id.messageEditText)).getText().toString();

                Loading.start(getActivity());

                AttendRequester requester = new AttendRequester();
                requester.initialize(mScheduleData.id);

                requester.postChat("", String.valueOf(mTableIndex), chat, new AttendRequester.PostChatCallback() {
                    @Override
                    public void didReceiveData(boolean result) {
                        Loading.stop(getActivity());

                        if (result) {
                            View view = getView();
                            if (view == null) return;

                            SaveData saveData = SaveData.getInstance();
                            saveData.sentFirstMessageScheduleIds.add(mScheduleData.id);
                            saveData.save();

                            mCallback.didSendMessage();
                            close((LinearLayout)view.findViewById(R.id.contentLayout));

                        } else {
                            Dialog.show(getActivity(), Dialog.Style.error, "エラー", "通信に失敗しました", null);
                        }
                    }
                });
            }
        });

        ((EditText)view.findViewById(R.id.messageEditText)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                String message = editable.toString();
                View view = getView();
                if (view == null) return;
                Button sendButton = (Button)view.findViewById(R.id.sendButton);
                setSendButtonEnabled(sendButton, message.length() > 0);
            }
        });
    }

    private void initContent(View view) {

        ((TextView)view.findViewById(R.id.titleTextView)).setText(mScheduleData.title + "の開始");

        PicassoUtility.getUserImage(getActivity(), Constants.UserImageDirectory + mUserData.userId, (ImageView)view.findViewById(R.id.faceImageView));

        ((TextView)view.findViewById(R.id.targetNameTextView)).setText(mUserData.nameLast + mUserData.nameFirst + "さんとマッチしました！");

        Button sendButton = (Button)view.findViewById(R.id.sendButton);
        setSendButtonEnabled(sendButton, false);

        LinearLayout contentLayout = (LinearLayout)view.findViewById(R.id.contentLayout);
        int toYdelta = (int)(DeviceUtility.getWindowSize(getActivity()).y);
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, toYdelta);
        animation.setDuration(200);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                popFragment(AnimationType.none);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        contentLayout.startAnimation(animation);
    }

    private void setSendButtonEnabled(Button button, boolean enabled) {

        if (enabled) {
            button.setBackgroundResource(R.drawable.shape_schedule_reserve_button);
            button.setEnabled(true);
        } else {
            button.setBackgroundResource(R.drawable.shape_schedule_reserve_button_disable);
            button.setEnabled(false);
        }
    }

    private void close(LinearLayout contentLayout) {

        int fromYdelta = (int)(DeviceUtility.getWindowSize(getActivity()).y);
        TranslateAnimation animation = new TranslateAnimation(0, 0, fromYdelta, 0);
        animation.setDuration(200);
        animation.setFillAfter(true);
        contentLayout.startAnimation(animation);
    }

    public interface AttendMatchFragmentCallback {
        void didSendMessage();
    }
}
