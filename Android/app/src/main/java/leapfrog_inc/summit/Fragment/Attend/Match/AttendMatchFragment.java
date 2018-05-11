package leapfrog_inc.summit.Fragment.Attend.Match;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import leapfrog_inc.summit.Fragment.BaseFragment;
import leapfrog_inc.summit.Function.Constants;
import leapfrog_inc.summit.Function.PicassoUtility;
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
            public void onClick(View view) {
                // TODO
            }
        });
    }

    private void initContent(View view) {

        ((TextView)view.findViewById(R.id.titleTextView)).setText(mScheduleData.title + "の開始");

        PicassoUtility.getUserImage(getActivity(), Constants.UserImageDirectory + mUserData.userId, (ImageView)view.findViewById(R.id.faceImageView));

        ((TextView)view.findViewById(R.id.titleTextView)).setText(mUserData.nameLast + mUserData.nameFirst + "さんとマッチしました！");

        // TODO 送信ボタンの非活性化
        // TODO 下から跳ねる

    }

    public interface AttendMatchFragmentCallback {
        void didSendMessage();
    }
}
