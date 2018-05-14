package leapfrog_inc.summit.Fragment.Attend.Attend;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

import leapfrog_inc.summit.Fragment.Attend.Prepare.AttendPrepareFragment;
import leapfrog_inc.summit.Fragment.BaseFragment;
import leapfrog_inc.summit.Function.DeviceUtility;
import leapfrog_inc.summit.Http.Requester.AttendRequester;
import leapfrog_inc.summit.Http.Requester.ScheduleRequester;
import leapfrog_inc.summit.Http.Requester.UserRequester;
import leapfrog_inc.summit.MainActivity;
import leapfrog_inc.summit.R;

/**
 * Created by Leapfrog-Software on 2018/05/11.
 */

public class AttendFragment extends BaseFragment {

    private ScheduleRequester.ScheduleData mScheduleData;
    private int mTableIndex;
    private ArrayList<UserRequester.UserData> mMembers;
    private ChatFragment mChatFragment;
    private AttendRequester mAttendRequester = new AttendRequester();
    private ArrayList<HorizontalScrollView> mHorizontalScrollViews = new ArrayList<HorizontalScrollView>();

    private static int horizontalScrollViewTagStartNo = 100;
    private static int tableLayoutTagStartNo = 10000;

    public void set(ScheduleRequester.ScheduleData scheduleData, int tableIndex) {
        mScheduleData = scheduleData;
        mTableIndex = tableIndex;

        mMembers = UserRequester.getInstance().queryReservedUser(scheduleData.id);
        mAttendRequester.initialize(scheduleData.id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_attend, null);

        initChatFragment(view);
        initContents(view);
        startTimer();
        initAction(view);

        return view;
    }

    private void initAction(View view) {

        ((ImageButton)view.findViewById(R.id.backButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = (MainActivity)getActivity();
                List<Fragment> fragments = mainActivity.getCurrentFragments();
                for (int i = 0; i < fragments.size(); i++) {
                    Fragment fragment = fragments.get(i);
                    if (fragment instanceof AttendPrepareFragment) {
                        ((AttendPrepareFragment)fragment).popFragment(AnimationType.horizontal);
                    }
                }
                popFragment(AnimationType.horizontal);
            }
        });
    }

    private void initContents(View view) {

        int cellNum = getCellNumer();
        LinearLayout scrollContentsLayout = (LinearLayout)view.findViewById(R.id.scrollContentsLayout);
        int tableWidth = getTableWidth();

        for (int i = 0; i < cellNum; i++) {
            HorizontalScrollView horizontalScrollView = new HorizontalScrollView(getActivity());
            horizontalScrollView.setTag(horizontalScrollViewTagStartNo + i);
            horizontalScrollView.setHorizontalScrollBarEnabled(false);
            horizontalScrollView.setOverScrollMode(View.OVER_SCROLL_NEVER);
            horizontalScrollView.setClipChildren(false);
            horizontalScrollView.setClipToPadding(false);
            LinearLayout horizontalLayout = new LinearLayout(getActivity());
            horizontalLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
            horizontalScrollView.addView(horizontalLayout);
            horizontalLayout.setClipChildren(false);
            horizontalLayout.setClipToPadding(false);

            for (int j = 0; j < cellNum; j++) {
                AttendTableLayout attendTableLayout = new AttendTableLayout(getActivity(), null);
                attendTableLayout.setTag(tableLayoutTagStartNo + j);
                attendTableLayout.setLayoutParams(new ViewGroup.LayoutParams(tableWidth, tableWidth));
                horizontalLayout.addView(attendTableLayout);
            }
            scrollContentsLayout.addView(horizontalScrollView);

            horizontalScrollView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    int action = motionEvent.getAction();
                    if (action == MotionEvent.ACTION_MOVE) {
                        HorizontalScrollView scrollView = (HorizontalScrollView)view;
                        moveHorizontalScrollView((int)scrollView.getTag(), scrollView.getScrollX());
                        return false;
                    } else if (action == MotionEvent.ACTION_UP) {
                        moveToNearestTable();
                        return true;
                    }
                    return false;
                }
            });
            mHorizontalScrollViews.add(horizontalScrollView);
        }

        final ScrollView scrollView = (ScrollView)view.findViewById(R.id.scrollView);
        ViewGroup.LayoutParams scrollViewParams = scrollView.getLayoutParams();
        scrollViewParams.height = tableWidth;
        scrollView.setLayoutParams(scrollViewParams);

        scrollView.post(new Runnable() {
            @Override
            public void run() {
                int cellNum = getCellNumer();
                int tableWidth = getTableWidth();
                int x = (mTableIndex % cellNum) * tableWidth;
                int y = (mTableIndex / cellNum) * tableWidth;
                scrollView.scrollTo(x, y);
            }
        });

        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    moveToNearestTable();
                    return true;
                }
                return false;
            }
        });
    }

    private int getTableCount() {

        if (mMembers.size() % 2 == 0) {
            return mMembers.size() / 2;
        } else if (mMembers.size() >= 3) {
            return mMembers.size() / 2 + 1;
        }
        return 1;
    }

    private int getCellNumer() {
        int tableCount = getTableCount();
        return (int)(Math.sqrt(tableCount)) + 1;
    }

    private int getTableWidth() {
        return DeviceUtility.getWindowSize(getActivity()).x - (int)(64 * DeviceUtility.getDeviceDensity(getActivity()));
    }

    private void moveHorizontalScrollView(int scrollViewTag, int scrollX) {

        for (int i = 0; i < mHorizontalScrollViews.size(); i++) {
            HorizontalScrollView horizontalScrollView = mHorizontalScrollViews.get(i);
            if ((int)horizontalScrollView.getTag() != scrollViewTag) {
                horizontalScrollView.scrollTo(scrollX, 0);
            }
        }
    }

    private void moveToNearestTable() {

        int cellNum = getCellNumer();
        int tableWidth = getTableWidth();

        HorizontalScrollView horizontalScrollView = (HorizontalScrollView)getView().findViewWithTag(horizontalScrollViewTagStartNo);
        int scrollX = horizontalScrollView.getScrollX();
        int pageX = scrollX / tableWidth;
        int offsetX = scrollX % tableWidth;
        if (pageX < 0) pageX = 0;
        if (offsetX > tableWidth / 2) pageX += 1;
        if (pageX >= cellNum) pageX = cellNum - 1;
        for (int i = 0; i < mHorizontalScrollViews.size(); i++) {
            HorizontalScrollView hsv = mHorizontalScrollViews.get(i);
            hsv.smoothScrollTo(pageX * tableWidth, 0);
        }

        ScrollView scrollView = (ScrollView)getView().findViewById(R.id.scrollView);
        int scrollY = scrollView.getScrollY();
        int pageY = scrollY / tableWidth;
        int offsetY = scrollY % tableWidth;
        if (pageY < 0) pageY = 0;
        if (offsetY > tableWidth / 2) pageY += 1;
        if (pageY >= cellNum) pageY = cellNum - 1;
        scrollView.smoothScrollTo(0, pageY * tableWidth);

        mTableIndex = pageX + pageY * cellNum;
        ArrayList<AttendRequester.ChatData> chatList = mAttendRequester.queryChatList(mTableIndex);
        ArrayList<String> userIds = mAttendRequester.queryAttendUserIds(mTableIndex);
        mChatFragment.set(mTableIndex, chatList, userIds);
    }

    private void initChatFragment(View view) {

        FragmentActivity activity = getActivity();
        if (activity == null) return;

        mChatFragment = new ChatFragment();
        mChatFragment.set(new ChatFragment.ChatFragmentCallback() {
            public void onTapSend(String chatId, String chat) {
                postChat(chatId, chat);
            }
        });

        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.chatLayout, mChatFragment);
        transaction.commitAllowingStateLoss();
    }

    private void postChat(String chatId, String chat) {

        mAttendRequester.postChat(chatId, String.valueOf(mTableIndex), chat, new AttendRequester.PostChatCallback() {
            @Override
            public void didReceiveData(boolean result) {

            }
        });
    }

    private void startTimer() {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                timerProc();
                new Handler().postDelayed(this, 5000);
            }
        };
        new Handler().post(runnable);
    }

    private void timerProc() {

        mAttendRequester.attend(String.valueOf(mTableIndex), new AttendRequester.AttendRequesterCallback() {
            @Override
            public void didReceiveData(boolean result) {
                if (result) {
                    ScrollView scrollView = (ScrollView)getView().findViewById(R.id.scrollView);
                    ArrayList<AttendRequester.AttendData> attendList = mAttendRequester.getAttendList();
                    for (int i = 0; i < attendList.size(); i++) {
                        AttendRequester.AttendData attendData = attendList.get(i);
                        int tableId = Integer.parseInt(attendData.tableId);
                        View attendTableLayout = scrollView.findViewWithTag(tableLayoutTagStartNo + tableId);
                        if ((attendTableLayout != null) && (attendTableLayout instanceof AttendTableLayout)) {
                            ((AttendTableLayout)attendTableLayout).set(attendData.userIds);
                        }
                    }

                    ArrayList<AttendRequester.ChatData> chatList = mAttendRequester.queryChatList(mTableIndex);
                    ArrayList<String> userIds = mAttendRequester.queryAttendUserIds(mTableIndex);
                    mChatFragment.set(mTableIndex, chatList, userIds);
                }
            }
        });
    }
}
