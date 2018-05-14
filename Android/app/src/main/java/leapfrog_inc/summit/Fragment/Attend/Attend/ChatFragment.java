package leapfrog_inc.summit.Fragment.Attend.Attend;

import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;

import leapfrog_inc.summit.Fragment.BaseFragment;
import leapfrog_inc.summit.Function.DeviceUtility;
import leapfrog_inc.summit.Http.Requester.AttendRequester;
import leapfrog_inc.summit.R;

/**
 * Created by Leapfrog-Software on 2018/05/14.
 */

public class ChatFragment extends BaseFragment {

    private ChatFragmentCallback mCallback;
    private Point mOffset = null;

    public void set(ChatFragmentCallback callback) {
        mCallback = callback;
    }

    public void set(int tableIndex, ArrayList<AttendRequester.ChatData> chatList, ArrayList<String> userIds) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_chat, null);

        initAction(view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        View view = getView();

        ViewGroup.LayoutParams params = view.getLayoutParams();
        int windowHeight = DeviceUtility.getWindowSize(getActivity()).y - DeviceUtility.getStatusBarHeight(getActivity());
        params.height = windowHeight - getTopLimit();
        view.setLayoutParams(params);

        int height = DeviceUtility.getWindowSize(getActivity()).y;
        int y = height - (int)(100 * DeviceUtility.getDeviceDensity(getActivity()));
        view.setTranslationY(y);
    }

    private void initAction(View view) {

        ((LinearLayout)view.findViewById(R.id.headerLayout)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Point offset = new Point();
                        offset.x = (int)motionEvent.getX();
                        offset.y = (int)motionEvent.getY() + DeviceUtility.getStatusBarHeight(getActivity());
                        mOffset = offset;
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        if (mOffset != null) {
                            View view = getView();
                            if (view != null) {
                                int y = (int)(motionEvent.getRawY() - (float) mOffset.y);
                                if (y < getTopLimit()) y = getTopLimit();
                                if (y > getBottomLimit()) y = getBottomLimit();
                                view.setTranslationY(y);
                            }
                            return true;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        mOffset = null;
                        return true;
                    case MotionEvent.ACTION_CANCEL:
                        mOffset = null;
                        return true;
                }
                return false;
            }
        });

        ((Button)view.findViewById(R.id.sendButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getView();
                if (view == null) return;

                String chat = ((EditText)view.findViewById(R.id.chatEditText)).getText().toString();
                mCallback.onTapSend("", chat);
            }
        });
    }

    private int getTopLimit() {
        return (int)(120 * DeviceUtility.getDeviceDensity(getActivity()));
    }

    private int getBottomLimit() {
        int height = DeviceUtility.getWindowSize(getActivity()).y;
        return height - (int)(80 * DeviceUtility.getDeviceDensity(getActivity()));
    }

    public interface ChatFragmentCallback {
        void onTapSend(String chatId, String chat);
    }
}
