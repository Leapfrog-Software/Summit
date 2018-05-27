package leapfrog_inc.summit.Fragment.Attend.Attend;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import leapfrog_inc.summit.Fragment.BaseFragment;
import leapfrog_inc.summit.Fragment.Message.MessageDetailFragment;
import leapfrog_inc.summit.Function.Constants;
import leapfrog_inc.summit.Function.DeviceUtility;
import leapfrog_inc.summit.Function.PicassoUtility;
import leapfrog_inc.summit.Function.SaveData;
import leapfrog_inc.summit.Http.Requester.AttendRequester;
import leapfrog_inc.summit.Http.Requester.MessageRequester;
import leapfrog_inc.summit.R;

/**
 * Created by Leapfrog-Software on 2018/05/14.
 */

public class ChatFragment extends BaseFragment {

    private ChatFragmentCallback mCallback;
    private Point mOffset = null;
    private int mTableIndex;
    private ArrayList<AttendRequester.ChatData> mChatList;
    ArrayList<String> mUserIds;

    public void set(ChatFragmentCallback callback) {
        mCallback = callback;
    }

    public void set(int tableIndex, ArrayList<AttendRequester.ChatData> chatList, ArrayList<String> userIds) {
        mTableIndex = tableIndex;
        mChatList = chatList;
        mUserIds = userIds;

        resetContents(null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_chat, null);

        initAction(view);
        resetContents(view);

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

                EditText editText = (EditText)view.findViewById(R.id.chatEditText);
                String chat = editText.getText().toString();
                mCallback.onTapSend("", chat);

                editText.setText("");
            }
        });

        ((Button)view.findViewById(R.id.memberViewButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AttendMemberListFragment fragment = new AttendMemberListFragment();
                fragment.set(mUserIds);
                stackFragment(fragment, AnimationType.horizontal);
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

    private void resetContents(View v) {

        View view = v;
        if (view == null) view = getView();
        if (view == null) return;

        setUserImages(view);
        resetListView(view);
    }

    private void setUserImages(View view) {

        if (mUserIds == null) return;

        ImageView member1ImageView = (ImageView)view.findViewById(R.id.member1ImageView);
        member1ImageView.setImageBitmap(null);
        if (mUserIds.size() >= 1) {
            PicassoUtility.getUserImage(getActivity(), Constants.UserImageDirectory + mUserIds.get(0), member1ImageView);
        }

        ImageView member2ImageView = (ImageView)view.findViewById(R.id.member2ImageView);
        member2ImageView.setImageBitmap(null);
        if (mUserIds.size() >= 2) {
            PicassoUtility.getUserImage(getActivity(), Constants.UserImageDirectory + mUserIds.get(1), member2ImageView);
        }

        ImageView member3ImageView = (ImageView)view.findViewById(R.id.member3ImageView);
        member3ImageView.setImageBitmap(null);
        if (mUserIds.size() >= 3) {
            PicassoUtility.getUserImage(getActivity(), Constants.UserImageDirectory + mUserIds.get(2), member3ImageView);
        }

        ImageView member4ImageView = (ImageView)view.findViewById(R.id.member4ImageView);
        member4ImageView.setImageBitmap(null);
        if (mUserIds.size() >= 4) {
            PicassoUtility.getUserImage(getActivity(), Constants.UserImageDirectory + mUserIds.get(3), member4ImageView);
        }

        ((TextView)view.findViewById(R.id.memberCountTextView)).setText(String.valueOf(mUserIds.size()));
    }

    private void resetListView(View view) {

        if (mChatList == null) return;

        ArrayList<AttendRequester.ChatData> sortedChatList = new ArrayList<AttendRequester.ChatData>();
        int count = mChatList.size();
        for (int i = 0; i < count; i++) {
            int minIndex = -1;
            Date minDate = new Date();
            for (int j = 0; j < mChatList.size(); j++) {
                AttendRequester.ChatData chatData = mChatList.get(j);
                if ((minIndex == -1) || (minDate.after(chatData.datetime.getTime()))) {
                    minIndex = j;
                    minDate = chatData.datetime.getTime();
                }
            }
            sortedChatList.add(mChatList.get(minIndex));
            mChatList.remove(minIndex);
        }
        mChatList = sortedChatList;

        ChatAdapater adapter = new ChatAdapater(getActivity());

        for (int i = 0; i < mChatList.size(); i++) {
            adapter.add(mChatList.get(i));
        }

        ListView listView = (ListView)view.findViewById(R.id.listView);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

        listView.setSelection(mChatList.size());
    }

    public interface ChatFragmentCallback {
        void onTapSend(String chatId, String chat);
    }

    private class ChatAdapater extends ArrayAdapter<AttendRequester.ChatData> {

        LayoutInflater mInflater;
        Context mContext;

        public ChatAdapater(Context context){
            super(context, 0);
            mInflater = LayoutInflater.from(context);
            mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            AttendRequester.ChatData chatData = getItem(position);
            if (chatData.senderId.equals(SaveData.getInstance().userId)) {
                convertView = mInflater.inflate(R.layout.adapter_message_detail_right, parent, false);

                ((TextView)convertView.findViewById(R.id.messageTextView)).setText(chatData.chat);
                ((TextView)convertView.findViewById(R.id.dateTextView)).setText(getDateString(chatData.datetime));

            } else {
                convertView = mInflater.inflate(R.layout.adapter_message_detail_left, parent, false);

                PicassoUtility.getUserImage(mContext, Constants.UserImageDirectory + chatData.senderId, (ImageView)convertView.findViewById(R.id.faceImageView));
                ((TextView)convertView.findViewById(R.id.messageTextView)).setText(chatData.chat);
                ((TextView)convertView.findViewById(R.id.dateTextView)).setText(getDateString(chatData.datetime));
            }

            return convertView;
        }

        private String getDateString(Calendar calendar) {

            Calendar today = Calendar.getInstance();

            if ((today.get(Calendar.YEAR) == calendar.get(Calendar.YEAR))
                    && (today.get(Calendar.MONTH) == calendar.get(Calendar.MONTH))
                    && (today.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH))) {
                SimpleDateFormat format = new SimpleDateFormat("kk:mm");
                return format.format(calendar.getTime());
            } else if (today.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)) {
                SimpleDateFormat format = new SimpleDateFormat("M月d日 kk:mm");
                return format.format(calendar.getTime());
            } else {
                SimpleDateFormat format = new SimpleDateFormat("yyyy年M月d日 kk:mm");
                return format.format(calendar.getTime());
            }
        }
    }
}
