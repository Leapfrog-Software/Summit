package leapfrog_inc.summit.Fragment.Attend.Attend;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.ArrayList;

import leapfrog_inc.summit.Fragment.BaseFragment;
import leapfrog_inc.summit.Function.DeviceUtility;
import leapfrog_inc.summit.Http.Requester.AttendRequester;
import leapfrog_inc.summit.Http.Requester.ScheduleRequester;
import leapfrog_inc.summit.Http.Requester.UserRequester;
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

        return view;
    }

    private void initContents(View view) {

        int cellNum = getCellNumer();
        LinearLayout scrollContentsLayout = (LinearLayout)view.findViewById(R.id.scrollContentsLayout);
        int tableWidth = getTableWidth();

        for (int i = 0; i < cellNum; i++) {
            HorizontalScrollView horizontalScrollView = new HorizontalScrollView(getActivity());
            horizontalScrollView.setTag(horizontalScrollViewTagStartNo + i);
            LinearLayout horizontalLayout = new LinearLayout(getActivity());
            horizontalLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
            horizontalScrollView.addView(horizontalLayout);

            for (int j = 0; j < cellNum; j++) {
                AttendTableLayout attendTableLayout = new AttendTableLayout(getActivity(), null);
                attendTableLayout.setTag(tableLayoutTagStartNo + j);
                attendTableLayout.setLayoutParams(new ViewGroup.LayoutParams(tableWidth, tableWidth));
                horizontalLayout.addView(attendTableLayout);
            }
            scrollContentsLayout.addView(horizontalScrollView);

//            horizontalScrollView.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View view, MotionEvent motionEvent) {
//                    return false;
//                }
//            });
        }

        final ScrollView scrollView = (ScrollView)view.findViewById(R.id.scrollView);

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
        return DeviceUtility.getWindowSize(getActivity()).x - 80;
    }

    private void moveToNearestTable() {

        ScrollView scrollView = (ScrollView)getView().findViewById(R.id.scrollView);

        int cellNum = getCellNumer();
        int tableWidth = getTableWidth();

        int scrollX = scrollView.getScrollX();
        int pageX = scrollX / tableWidth;
        int offsetX = scrollX % tableWidth;
        if (pageX < 0) pageX = 0;
        if (offsetX > tableWidth / 2) pageX += 1;
        if (pageX >= cellNum) pageX = cellNum - 1;

        int scrollY = scrollView.getScrollY();
        int pageY = scrollY / tableWidth;
        int offsetY = scrollY % tableWidth;
        if (pageY < 0) pageY = 0;
        if (offsetY > tableWidth / 2) pageY += 1;
        if (pageY >= cellNum) pageY = cellNum - 1;

        scrollView.smoothScrollTo(pageX * tableWidth, pageY * tableWidth);

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

        View view = getView();
        if (view == null) return;

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
