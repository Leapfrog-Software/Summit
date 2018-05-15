package leapfrog_inc.summit.Fragment.Message;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import leapfrog_inc.summit.Fragment.BaseFragment;
import leapfrog_inc.summit.Function.Constants;
import leapfrog_inc.summit.Function.PicassoUtility;
import leapfrog_inc.summit.Function.SaveData;
import leapfrog_inc.summit.Http.Requester.MessageRequester;
import leapfrog_inc.summit.R;

/**
 * Created by Leapfrog-Software on 2018/05/09.
 */

public class MessageDetailFragment extends BaseFragment {

    private String mTargetUserId;

    public void set(String targetUserId) {
        mTargetUserId = targetUserId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_message_detail, null);

        initAction(view);
        resetListView(view);

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

    private void resetListView(View v) {

        View view = v;
        if (view == null) view = getView();
        if (view == null) return;

        ArrayList<MessageRequester.MessageData> messageList = MessageRequester.getInstance().queryMessages(mTargetUserId);
        ArrayList<MessageRequester.MessageData> sortedMessageList = new ArrayList<MessageRequester.MessageData>();
        int messageSize = messageList.size();
        for (int i = 0; i < messageSize; i++) {
            int minIndex = -1;
            Date minDate = new Date();
            for (int j = 0; j < messageList.size(); j++) {
                MessageRequester.MessageData messageData = messageList.get(j);
                if ((minIndex == -1) || (minDate.after(messageData.datetime.getTime()))) {
                    minIndex = j;
                    minDate = messageData.datetime.getTime();
                }
            }
            sortedMessageList.add(messageList.get(minIndex));
            messageList.remove(minIndex);
        }

        MessageDetailAdapater adapter = new MessageDetailAdapater(getActivity());

        for (int i = 0; i < sortedMessageList.size(); i++) {
            adapter.add(sortedMessageList.get(i));
        }

        ListView listView = (ListView)view.findViewById(R.id.listView);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

        listView.setSelection(sortedMessageList.size());
    }

    private class MessageDetailAdapater extends ArrayAdapter<MessageRequester.MessageData> {

        LayoutInflater mInflater;
        Context mContext;

        public MessageDetailAdapater(Context context){
            super(context, 0);
            mInflater = LayoutInflater.from(context);
            mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            MessageRequester.MessageData messageData = getItem(position);
            if (messageData.senderId.equals(SaveData.getInstance().userId)) {
                convertView = mInflater.inflate(R.layout.adapter_message_detail_right, parent, false);

                ((TextView)convertView.findViewById(R.id.messageTextView)).setText(messageData.message);
                ((TextView)convertView.findViewById(R.id.dateTextView)).setText(getDateString(messageData.datetime));

            } else {
                convertView = mInflater.inflate(R.layout.adapter_message_detail_left, parent, false);

                PicassoUtility.getUserImage(mContext, Constants.UserImageDirectory + messageData.senderId, (ImageView)convertView.findViewById(R.id.faceImageView));
                ((TextView)convertView.findViewById(R.id.messageTextView)).setText(messageData.message);
                ((TextView)convertView.findViewById(R.id.dateTextView)).setText(getDateString(messageData.datetime));
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
