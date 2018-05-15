package leapfrog_inc.summit.Fragment.Schedule;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import leapfrog_inc.summit.Fragment.Attend.Prepare.AttendPrepareFragment;
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

public class ScheduleFragment extends BaseFragment {

    private Calendar mSelectedDate = Calendar.getInstance();
    private ArrayList<CalendarMonthFragment> mMonthFragments;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_schedule, null);

        initAction(view);
        initCalendar(view);
        resetListView(view);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                timerProc();
                new Handler().postDelayed(this, 10000);
            }
        };
        new Handler().post(runnable);

        return view;
    }

    private void initAction(View view) {

        ((Button)view.findViewById(R.id.nextPlanButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AttendPrepareFragment fragment = new AttendPrepareFragment();
                fragment.set(searchNextSchedule());
                stackFragment(fragment, AnimationType.horizontal);
            }
        });

        ((Button)view.findViewById(R.id.monthLeftButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewPager viewPager = (ViewPager)getView().findViewById(R.id.viewPager);
                int page = viewPager.getCurrentItem();
                if (page >= 1) {
                    page -= 1;
                    viewPager.setCurrentItem(page);
                }
            }
        });

        ((Button)view.findViewById(R.id.monthRightButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewPager viewPager = (ViewPager)getView().findViewById(R.id.viewPager);
                int page = viewPager.getCurrentItem();
                if (page <= 4) {
                    page += 1;
                    viewPager.setCurrentItem(page);
                }
            }
        });
    }

    private void initCalendar(View view) {

        mMonthFragments = new ArrayList<CalendarMonthFragment>();
        for (int i = 0; i < 6; i++) {
            mMonthFragments.add(new CalendarMonthFragment());
        }

        ViewPager viewPager = (ViewPager)view.findViewById(R.id.viewPager);

        CalendarMonthPagerAdapter adapter = new CalendarMonthPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.destroy(viewPager);
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageSelected(int position) {
                setCurrentMonth(getView(), position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });


        setCurrentMonth(view, 0);
    }

    private void setCurrentMonth(View view, int offset) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, offset);
        Date date = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy年 M月");
        String monthString = format.format(date);
        ((TextView)view.findViewById(R.id.monthTextView)).setText(monthString);
    }

    private void resetListView(View view) {

        View cpView = view;
        if (cpView == null)     cpView = getView();
        if (cpView == null)     return;

        final ScheduleAdapater adapter = new ScheduleAdapater(getActivity());

        ArrayList<ScheduleRequester.ScheduleData> scheduleList = ScheduleRequester.getInstance().query(mSelectedDate);
        for (int i = 0; i < scheduleList.size(); i++) {
            adapter.add(scheduleList.get(i));
        }

        ListView listView = (ListView)cpView.findViewById(R.id.listView);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ScheduleRequester.ScheduleData data = (ScheduleRequester.ScheduleData)adapterView.getItemAtPosition(i);

                ScheduleDetailFragment fragment = new ScheduleDetailFragment();
                fragment.set(data);
                stackFragment(fragment, AnimationType.horizontal);
            }
        });

        if (scheduleList.size() > 0) {
            listView.setVisibility(View.VISIBLE);
            view.findViewById(R.id.noDataTextView).setVisibility(View.GONE);
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = (int) (124 * DeviceUtility.getDeviceDensity(getActivity()) * (float) scheduleList.size());
            listView.setLayoutParams(params);
        } else {
            listView.setVisibility(View.GONE);
            view.findViewById(R.id.noDataTextView).setVisibility(View.VISIBLE);
        }

        String titleText = String.valueOf(mSelectedDate.get(Calendar.MONTH) + 1) + "/" + String.valueOf(mSelectedDate.get(Calendar.DATE) + "のイベント");
        ((TextView)view.findViewById(R.id.scheduleDateTextView)).setText(titleText);
    }

    private void timerProc() {

        View view = getView();
        if (view == null) return;

        ScheduleRequester.ScheduleData nextSchedule = searchNextSchedule();
        if (nextSchedule != null) {
            view.findViewById(R.id.nextPlanLayout).setVisibility(View.VISIBLE);

            PicassoUtility.getScheduleImage(getActivity(), Constants.ScheduleImageDirectory + nextSchedule.id, (ImageView)view.findViewById(R.id.nextPlanImageView));

            ((TextView)view.findViewById(R.id.nextPlanScheduleTextView)).setText(nextSchedule.title);

            Calendar today = Calendar.getInstance();

            if ((nextSchedule.datetime.get(Calendar.YEAR) == today.get(Calendar.YEAR))
                    && (nextSchedule.datetime.get(Calendar.MONTH) == today.get(Calendar.MONTH))
                    && (nextSchedule.datetime.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH))) {
                SimpleDateFormat format = new SimpleDateFormat("h:mm〜");
                ((TextView)view.findViewById(R.id.nextPlanDateTextView)).setText(format.format(nextSchedule.datetime.getTime()));
            } else {
                SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 h:mm〜");
                ((TextView)view.findViewById(R.id.nextPlanDateTextView)).setText(format.format(nextSchedule.datetime.getTime()));
            }

            int timeInterval = (int)(nextSchedule.datetime.getTime().getTime() - today.getTime().getTime());
            if ((timeInterval <= 0) && timeInterval >= -nextSchedule.timeLength) {
                ((TextView)view.findViewById(R.id.nextPlanTitleTextView)).setText("開催中");
                view.findViewById(R.id.nextPlanArrowImageView).setVisibility(View.VISIBLE);
            } else if (timeInterval < 60 * 60) {
                int remainMinutes = (int)(timeInterval / 60 + 1);
                ((TextView)view.findViewById(R.id.nextPlanTitleTextView)).setText(String.format("%d分後に開始", remainMinutes));
                view.findViewById(R.id.nextPlanArrowImageView).setVisibility(View.VISIBLE);
            } else {
                ((TextView)view.findViewById(R.id.nextPlanTitleTextView)).setText("直近の予定");
                view.findViewById(R.id.nextPlanArrowImageView).setVisibility(View.INVISIBLE);
            }
        } else {
            view.findViewById(R.id.nextPlanLayout).setVisibility(View.GONE);
        }
    }

    private ScheduleRequester.ScheduleData searchNextSchedule() {

        UserRequester.UserData myUserData = UserRequester.getInstance().myUserData();
        if (myUserData == null) return null;

        long today = (new Date()).getTime();

        ArrayList<ScheduleRequester.ScheduleData> allSchedules = ScheduleRequester.getInstance().getDataList();
        ScheduleRequester.ScheduleData nextSchedule = null;
        for (int i = 0; i < allSchedules.size(); i++) {
            ScheduleRequester.ScheduleData scheduleData = allSchedules.get(i);
            if (myUserData.reserves.contains(scheduleData.id)) {
                if (scheduleData.datetime.getTime().getTime() + scheduleData.timeLength - today >= 0) {
                    if (nextSchedule == null) {
                        nextSchedule = scheduleData;
                    } else if (nextSchedule.datetime.getTime().getTime() - scheduleData.datetime.getTime().getTime() >= 0) {
                        nextSchedule = scheduleData;
                    }
                }
            }
        }
        return nextSchedule;
    }

    private class CalendarMonthPagerAdapter extends FragmentPagerAdapter {

        public CalendarMonthPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            final CalendarMonthFragment fragment = new CalendarMonthFragment();
            fragment.set(position, mSelectedDate, new CalendarMonthFragment.CalendarMonthCallback() {
                @Override
                public void didSelectDay(Calendar calendar) {
                    mSelectedDate = calendar;
                    resetListView(getView());
                    fragment.reselectDate(calendar);
                }
            });
            return fragment;
        }

        @Override
        public int getCount() {
            return 6;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        public void destroy(ViewPager viewPager) {
            for (int i = 0; i < getCount(); i++) {
                try {
                    Object object = instantiateItem(viewPager, i);
                    if (object != null) {
                        destroyItem(viewPager, i, object);
                    }
                } catch (Exception e) {}
            }
        }
    }

    private class ScheduleAdapater extends ArrayAdapter<ScheduleRequester.ScheduleData> {

        LayoutInflater mInflater;
        Context mContext;

        public ScheduleAdapater(Context context){
            super(context, 0);
            mInflater = LayoutInflater.from(context);
            mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ScheduleRequester.ScheduleData scheduleData = getItem(position);

            convertView = mInflater.inflate(R.layout.adapter_schedule, parent, false);

            ((TextView)convertView.findViewById(R.id.titleTextView)).setText(scheduleData.title);

            SimpleDateFormat format = new SimpleDateFormat("hh:mm");
            String datetime = format.format(scheduleData.datetime.getTime());
            ((TextView)convertView.findViewById(R.id.dateTextView)).setText(datetime);

            ((TextView)convertView.findViewById(R.id.providerTextView)).setText(scheduleData.provider);
            ((TextView)convertView.findViewById(R.id.descriptionTextView)).setText(scheduleData.description);


            return convertView;
        }
    }
}
