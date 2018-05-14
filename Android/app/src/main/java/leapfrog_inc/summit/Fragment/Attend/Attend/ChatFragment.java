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
                                view.setTranslationX(motionEvent.getRawX() - (float) mOffset.x);
                                view.setTranslationY(motionEvent.getRawY() - (float) mOffset.y);
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

    public interface ChatFragmentCallback {
        void onTapSend(String chatId, String chat);
    }
}
