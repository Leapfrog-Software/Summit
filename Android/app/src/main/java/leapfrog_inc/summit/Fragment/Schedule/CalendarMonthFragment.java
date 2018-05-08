package leapfrog_inc.summit.Fragment.Schedule;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import leapfrog_inc.summit.Function.DeviceUtility;
import leapfrog_inc.summit.Http.Requester.ScheduleRequester;
import leapfrog_inc.summit.R;

/**
 * Created by Leapfrog-Software on 2018/05/08.
 */

public class CalendarMonthFragment extends Fragment {

    private int mMonthOffset;
    private CalendarMonthCallback mCallback;

    public void set(int monthOffset, CalendarMonthCallback callback) {
        mMonthOffset = monthOffset;
        mCallback = callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_calendar_month, null);

        initDays(view);

        return view;
    }

    private void initDays(View view) {

        Point windowSize = DeviceUtility.getWindowSize(getActivity());
        float density = DeviceUtility.getDeviceDensity(getActivity());

        Calendar today = Calendar.getInstance();
        Calendar currentDay = getFirstDay();

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                View dayView = LayoutInflater.from(getActivity()).inflate(R.layout.view_calendar_day, null);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams((int)(windowSize.x / 7), (int)(44 * density));
                dayView.setLayoutParams(params);

                LinearLayout baseLayout;
                if (i == 0)          baseLayout = (LinearLayout)view.findViewById(R.id.w1Layout);
                else if (i == 1)     baseLayout = (LinearLayout)view.findViewById(R.id.w2Layout);
                else if (i == 2)     baseLayout = (LinearLayout)view.findViewById(R.id.w3Layout);
                else if (i == 3)     baseLayout = (LinearLayout)view.findViewById(R.id.w4Layout);
                else if (i == 4)     baseLayout = (LinearLayout)view.findViewById(R.id.w5Layout);
                else                 baseLayout = (LinearLayout)view.findViewById(R.id.w6Layout);
                baseLayout.addView(dayView);

                TextView dayTextView = (TextView)dayView.findViewById(R.id.dayTextView);
                dayTextView.setText(String.valueOf(currentDay.get(Calendar.DATE)));
                if (currentDay.get(Calendar.MONTH) == today.get(Calendar.MONTH)) {
                    dayTextView.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.basicBlack));
                } else {
                    dayTextView.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.passiveGray));
                }

                ArrayList<ScheduleRequester.ScheduleData> scheduleList = ScheduleRequester.getInstance().query(currentDay);
                if (scheduleList.size() >= 1) {
                    ((LinearLayout)dayView.findViewById(R.id.schedule1Layout)).setVisibility(View.VISIBLE);
                    ScheduleRequester.ScheduleData scheduleData = scheduleList.get(0);
                    if (scheduleData.type == ScheduleRequester.ScheduleType.party)      ((View)dayView.findViewById(R.id.schedule1View)).setBackgroundResource(R.drawable.shape_schedule_party);
                    else if (scheduleData.type == ScheduleRequester.ScheduleType.party) ((View)dayView.findViewById(R.id.schedule1View)).setBackgroundResource(R.drawable.shape_schedule_seminar);
                    else                                                                ((View)dayView.findViewById(R.id.schedule1View)).setBackgroundResource(R.drawable.shape_schedule_meeting);
                } else {
                    ((LinearLayout)dayView.findViewById(R.id.schedule1Layout)).setVisibility(View.GONE);
                }
                if (scheduleList.size() >= 2) {
                    ((LinearLayout)dayView.findViewById(R.id.schedule2Layout)).setVisibility(View.VISIBLE);
                    ScheduleRequester.ScheduleData scheduleData = scheduleList.get(1);
                    if (scheduleData.type == ScheduleRequester.ScheduleType.party)      ((View)dayView.findViewById(R.id.schedule2View)).setBackgroundResource(R.drawable.shape_schedule_party);
                    else if (scheduleData.type == ScheduleRequester.ScheduleType.party) ((View)dayView.findViewById(R.id.schedule2View)).setBackgroundResource(R.drawable.shape_schedule_seminar);
                    else                                                                ((View)dayView.findViewById(R.id.schedule2View)).setBackgroundResource(R.drawable.shape_schedule_meeting);
                } else {
                    ((LinearLayout)dayView.findViewById(R.id.schedule2Layout)).setVisibility(View.GONE);
                }
                if (scheduleList.size() >= 3) {
                    ((LinearLayout)dayView.findViewById(R.id.schedule3Layout)).setVisibility(View.VISIBLE);
                    ScheduleRequester.ScheduleData scheduleData = scheduleList.get(2);
                    if (scheduleData.type == ScheduleRequester.ScheduleType.party)      ((View)dayView.findViewById(R.id.schedule3View)).setBackgroundResource(R.drawable.shape_schedule_party);
                    else if (scheduleData.type == ScheduleRequester.ScheduleType.party) ((View)dayView.findViewById(R.id.schedule3View)).setBackgroundResource(R.drawable.shape_schedule_seminar);
                    else                                                                ((View)dayView.findViewById(R.id.schedule3View)).setBackgroundResource(R.drawable.shape_schedule_meeting);
                } else {
                    ((LinearLayout)dayView.findViewById(R.id.schedule3Layout)).setVisibility(View.GONE);
                }

                if ((currentDay.get(Calendar.MONTH) == today.get(Calendar.MONTH))
                        && (currentDay.get(Calendar.DATE) == today.get(Calendar.DATE))) {
                    ((View)dayView.findViewById(R.id.todayView)).setVisibility(View.VISIBLE);
                } else {
                    ((View)dayView.findViewById(R.id.todayView)).setVisibility(View.INVISIBLE);
                }

                final int year = currentDay.get(Calendar.YEAR);
                final int month = currentDay.get(Calendar.MONTH);
                final int date = currentDay.get(Calendar.DATE);

                ((Button)dayView.findViewById(R.id.dayButton)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, date);
                        mCallback.didSelectDay(calendar);
                    }
                });

                currentDay.add(Calendar.DATE, 1);
            }
        }
    }

    private Calendar getFirstDay() {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, mMonthOffset);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);

        for (int i = 0; i < 7; i++) {
            if (calendar.get(Calendar.DAY_OF_WEEK) == 2) {
                return calendar;
            }
            calendar.add(Calendar.DATE, -1);
        }
        return null;
    }

    public interface CalendarMonthCallback {
        void didSelectDay(Calendar calendar);
    }
}
