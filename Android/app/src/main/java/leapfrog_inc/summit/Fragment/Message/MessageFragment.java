package leapfrog_inc.summit.Fragment.Message;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;

import leapfrog_inc.summit.Fragment.BaseFragment;
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
                Date lastDate = lastMessageDatetime(filterredMessages);
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
            if (userData != null) {
                adapter.add(userData);
            }
        }

        ListView listView = (ListView)view.findViewById(R.id.listView);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                UserRequester.UserData userData = (UserRequester.UserData)adapterView.getItemAtPosition(i);
                MessageDetailFragment fragment = new MessageDetailFragment();
                fragment.set(userData.userId);
                stackFragment(fragment, AnimationType.horizontal);
            }
        });
    }

    private Date lastMessageDatetime(ArrayList<MessageRequester.MessageData> messages) {

        Date maxDate = null;

        for (int i = 0; i < messages.size(); i++) {
            MessageRequester.MessageData messageData = messages.get(i);
            if ((maxDate == null) || (messageData.datetime.after(maxDate))) {
                maxDate = messageData.datetime;
            }
        }
        return maxDate;
    }

    private class MessageAdapater extends ArrayAdapter<UserRequester.UserData> {

        LayoutInflater mInflater;
        Context mContext;

        public MessageAdapater(Context context){
            super(context, 0);
            mInflater = LayoutInflater.from(context);
            mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = mInflater.inflate(R.layout.adapter_card_kana, parent, false);

            UserRequester.UserData userData = getItem(position);






            return convertView;
        }
    }
}
