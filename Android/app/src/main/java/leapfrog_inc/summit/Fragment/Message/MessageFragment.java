package leapfrog_inc.summit.Fragment.Message;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import leapfrog_inc.summit.Fragment.BaseFragment;
import leapfrog_inc.summit.Function.Constants;
import leapfrog_inc.summit.Function.PicassoUtility;
import leapfrog_inc.summit.Function.SaveData;
import leapfrog_inc.summit.Http.Requester.MessageRequester;
import leapfrog_inc.summit.Http.Requester.UserRequester;
import leapfrog_inc.summit.R;

/**
 * Created by Leapfrog-Software on 2018/05/08.
 */

public class MessageFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_message, null);

        initListView(view);

        return view;
    }

    private void initListView(View view) {

        ArrayList<String> targetIds = new ArrayList<String>();
        ArrayList<MessageRequester.MessageData> messages = MessageRequester.getInstance().getDataList();
        for (int i = 0; i < messages.size(); i++) {
            MessageRequester.MessageData messageData = messages.get(i);
            String targetId = "";
            if (messageData.senderId.equals(SaveData.getInstance().userId)) {
                targetId = messageData.receiverId;
            } else {
                targetId = messageData.senderId;
            }
            if (!targetIds.contains(targetId)) {
                targetIds.add(targetId);
            }
        }

        ArrayList<String> sortedTargetIds = new ArrayList<String>();
        int targetIdSize = targetIds.size();
        for (int i = 0; i < targetIdSize; i++) {
            int minIndex = -1;
            Date maxDate = null;
            for (int j = 0; j < targetIds.size(); j++) {
                ArrayList<MessageRequester.MessageData> filterredMessages = MessageRequester.getInstance().queryMessages(targetIds.get(j));
                Date lastDate = lastMessage(filterredMessages).datetime.getTime();
                if ((minIndex == -1) || (lastDate.after(maxDate))) {
                    minIndex = j;
                    maxDate = lastDate;
                }
            }
            sortedTargetIds.add(targetIds.get(minIndex));
            targetIds.remove(minIndex);
        }

        MessageAdapater adapter = new MessageAdapater(getActivity());

        for (int i = 0; i < sortedTargetIds.size(); i++) {
            UserRequester.UserData userData = UserRequester.getInstance().query(sortedTargetIds.get(i));
            if (userData == null) {
                continue;
            }
            ArrayList<MessageRequester.MessageData> targetMessages = MessageRequester.getInstance().queryMessages(userData.userId);
            MessageRequester.MessageData lastMessage = lastMessage(targetMessages);

            MessageAdapterData adapterData = new MessageAdapterData();
            adapterData.userData = userData;
            adapterData.lastMessage = lastMessage.message;

            Calendar lastMessageDate = lastMessage.datetime;

            Calendar today = Calendar.getInstance();
            if ((today.get(Calendar.YEAR) == lastMessageDate.get(Calendar.YEAR))
                    && (today.get(Calendar.MONTH) == lastMessageDate.get(Calendar.MONTH))
                    && (today.get(Calendar.DAY_OF_MONTH) == lastMessageDate.get(Calendar.DAY_OF_MONTH))) {
                adapterData.date = null;
                SimpleDateFormat format = new SimpleDateFormat("kk:mm");
                adapterData.time = format.format(lastMessageDate.getTime());
            } else if (today.get(Calendar.YEAR) == lastMessageDate.get(Calendar.YEAR)) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("M月d日");
                adapterData.date = dateFormat.format(lastMessageDate.getTime());
                SimpleDateFormat timeFormat = new SimpleDateFormat("kk:mm");
                adapterData.time = timeFormat.format(lastMessageDate.getTime());
            } else {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年M月d日");
                adapterData.date = dateFormat.format(lastMessageDate.getTime());
                SimpleDateFormat timeFormat = new SimpleDateFormat("kk:mm");
                adapterData.time = timeFormat.format(lastMessageDate.getTime());
            }

            adapterData.exist = (today.getTime().getTime() - lastMessageDate.getTime().getTime()) < 60 * 60 * 24;

            adapter.add(adapterData);
        }

        ListView listView = (ListView)view.findViewById(R.id.listView);
        TextView noDataTextView = (TextView)view.findViewById(R.id.noDataTextView);

        if (sortedTargetIds.size() > 0) {
            listView.setVisibility(View.VISIBLE);
            noDataTextView.setVisibility(View.GONE);
        } else {
            listView.setVisibility(View.GONE);
            noDataTextView.setVisibility(View.VISIBLE);
        }

        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MessageAdapterData adapterData = (MessageAdapterData)adapterView.getItemAtPosition(i);
                MessageDetailFragment fragment = new MessageDetailFragment();
                fragment.set(adapterData.userData.userId);
                stackFragment(fragment, AnimationType.horizontal);
            }
        });
    }

    private MessageRequester.MessageData lastMessage(ArrayList<MessageRequester.MessageData> messages) {

        int maxIndex = -1;
        Date maxDate = null;

        for (int i = 0; i < messages.size(); i++) {
            MessageRequester.MessageData messageData = messages.get(i);
            if ((maxDate == null) || (messageData.datetime.after(maxDate))) {
                maxIndex = i;
                maxDate = messageData.datetime.getTime();
            }
        }
        if (maxIndex >= 0) {
            return messages.get(maxIndex);
        } else {
            return null;
        }
    }

    private class MessageAdapterData {
        UserRequester.UserData userData;
        String date;
        String time;
        String lastMessage;
        boolean exist;
    }

    private class MessageAdapater extends ArrayAdapter<MessageAdapterData> {

        LayoutInflater mInflater;
        Context mContext;

        public MessageAdapater(Context context){
            super(context, 0);
            mInflater = LayoutInflater.from(context);
            mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = mInflater.inflate(R.layout.adapter_message, parent, false);

            MessageAdapterData data = getItem(position);

            PicassoUtility.getUserImage(mContext, Constants.UserImageDirectory + data.userData.userId, (ImageView)convertView.findViewById(R.id.faceImageView));

            if (data.exist) {
                ((View) convertView.findViewById(R.id.existView)).setVisibility(View.VISIBLE);
            } else {
                ((View) convertView.findViewById(R.id.existView)).setVisibility(View.GONE);
            }

            ((TextView)convertView.findViewById(R.id.nameTextView)).setText(data.userData.nameLast + " " + data.userData.nameFirst);

            if (data.date != null) {
                ((TextView) convertView.findViewById(R.id.dateTextView)).setVisibility(View.VISIBLE);
                ((TextView) convertView.findViewById(R.id.dateTextView)).setText(data.date);
            } else {
                ((TextView) convertView.findViewById(R.id.dateTextView)).setVisibility(View.GONE);
            }
            ((TextView) convertView.findViewById(R.id.timeTextView)).setText(data.time);

            ((TextView) convertView.findViewById(R.id.companyTextView)).setText(data.userData.company);
            ((TextView) convertView.findViewById(R.id.positionTextView)).setText(data.userData.position);
            ((TextView) convertView.findViewById(R.id.messageTextView)).setText(data.lastMessage);

            return convertView;
        }
    }
}
