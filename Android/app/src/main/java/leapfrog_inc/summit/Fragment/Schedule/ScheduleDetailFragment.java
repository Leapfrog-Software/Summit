package leapfrog_inc.summit.Fragment.Schedule;

import android.os.Bundle;
import android.support.v4.app.FragmentController;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import leapfrog_inc.summit.Fragment.BaseFragment;
import leapfrog_inc.summit.Function.Constants;
import leapfrog_inc.summit.Function.DeviceUtility;
import leapfrog_inc.summit.Function.PicassoUtility;
import leapfrog_inc.summit.Http.Requester.ScheduleRequester;
import leapfrog_inc.summit.Http.Requester.UserRequester;
import leapfrog_inc.summit.R;

/**
 * Created by Leapfrog-Software on 2018/05/08.
 */

public class ScheduleDetailFragment extends BaseFragment {

    private ScheduleRequester.ScheduleData mScheduleData;

    public void set(ScheduleRequester.ScheduleData scheduleData) {
        mScheduleData = scheduleData;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_schedule_detail, null);

        initContents(view);
        initAction(view);

        return view;
    }

    private void initContents(View view) {

        ImageView scheduleImageView = (ImageView)view.findViewById(R.id.scheduleImageView);
        ViewGroup.LayoutParams params = scheduleImageView.getLayoutParams();
        params.height = DeviceUtility.getWindowSize(getActivity()).x * 2 / 3;
        scheduleImageView.setLayoutParams(params);
        PicassoUtility.getScheduleImage(getActivity(), Constants.ScheduleImageDirectory + mScheduleData.id, (ImageView)view.findViewById(R.id.scheduleImageView));

        LinearLayout memberLayout = (LinearLayout)view.findViewById(R.id.memberLayout);
        memberLayout.addView(createPaddingView(16));
        ArrayList<UserRequester.UserData> userList = UserRequester.getInstance().queryReservedUser(mScheduleData.id);
        for (int i = 0; i < userList.size(); i++) {
            ImageView imageView = new ImageView(getActivity());
            float density = DeviceUtility.getDeviceDensity(getActivity());
            imageView.setLayoutParams(new ViewGroup.LayoutParams((int)(34 * density), (int)(34 * density)));
            memberLayout.addView(imageView);
            PicassoUtility.getUserImage(getActivity(), Constants.UserImageDirectory + userList.get(i).userId, imageView);

            memberLayout.addView(createPaddingView(8));
        }
        memberLayout.addView(createPaddingView(16));
    }

    private View createPaddingView(int width) {
        View view = new View(getActivity());
        float density = DeviceUtility.getDeviceDensity(getActivity());
        view.setLayoutParams(new ViewGroup.LayoutParams((int)((float)width * density), 1));
        return view;
    }

    private void initAction(View view) {

        ((ImageButton)view.findViewById(R.id.backButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popFragment(AnimationType.horizontal);
            }
        });
    }
}
