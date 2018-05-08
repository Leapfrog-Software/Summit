package leapfrog_inc.summit.Fragment.Schedule;

import android.os.Bundle;
import android.support.v4.app.FragmentController;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import leapfrog_inc.summit.Fragment.BaseFragment;
import leapfrog_inc.summit.Http.Requester.ScheduleRequester;
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

        initAction(view);

        return view;
    }

    private void initAction(View view) {

        ((Button)view.findViewById(R.id.backButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popFragment(AnimationType.horizontal);
            }
        });
    }
}
