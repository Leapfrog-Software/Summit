package leapfrog_inc.summit.Fragment.Attend.Attend;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import leapfrog_inc.summit.Fragment.BaseFragment;
import leapfrog_inc.summit.Fragment.Card.CardDetailFragment;
import leapfrog_inc.summit.Fragment.Message.MessageDetailFragment;
import leapfrog_inc.summit.Fragment.Message.MessageFragment;
import leapfrog_inc.summit.Function.Constants;
import leapfrog_inc.summit.Function.PicassoUtility;
import leapfrog_inc.summit.Function.SaveData;
import leapfrog_inc.summit.Http.Requester.MessageRequester;
import leapfrog_inc.summit.Http.Requester.UserRequester;
import leapfrog_inc.summit.R;

/**
 * Created by Leapfrog-Software on 2018/05/15.
 */

public class AttendMemberListFragment extends BaseFragment {

    private ArrayList<String> mUserIds = new ArrayList<String>();

    public void set(ArrayList<String> userIds) {
        mUserIds = userIds;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_attend_member_list, null);

        initListView(view);
        initAction(view);

        return view;
    }

    private void initListView(View view) {

        AttendMemberListAdapter adapter = new AttendMemberListAdapter(getActivity());

        for (int i = 0; i < mUserIds.size(); i++) {
            UserRequester.UserData userData = UserRequester.getInstance().query(mUserIds.get(i));
            if (userData == null) continue;
            adapter.add(userData);
        }

        ListView listView = (ListView)view.findViewById(R.id.listView);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                UserRequester.UserData userData = (UserRequester.UserData) adapterView.getItemAtPosition(i);
                CardDetailFragment fragment = new CardDetailFragment();
                fragment.set(userData, true);
                stackFragment(fragment, AnimationType.none);
            }
        });
    }

    private void initAction(View view) {

        ((ImageButton)view.findViewById(R.id.backButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popFragment(AnimationType.horizontal);
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

    private class AttendMemberListAdapter extends ArrayAdapter<UserRequester.UserData> {

        LayoutInflater mInflater;
        Context mContext;

        public AttendMemberListAdapter(Context context){
            super(context, 0);
            mInflater = LayoutInflater.from(context);
            mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = mInflater.inflate(R.layout.adapter_attend_member_list, parent, false);

            UserRequester.UserData data = getItem(position);

            PicassoUtility.getUserImage(mContext, Constants.UserImageDirectory + data.userId, (ImageView)convertView.findViewById(R.id.faceImageView));

            ((TextView)convertView.findViewById(R.id.nameTextView)).setText(data.nameLast + " " + data.nameFirst);

            ((TextView) convertView.findViewById(R.id.companyTextView)).setText(data.company);
            ((TextView) convertView.findViewById(R.id.positionTextView)).setText(data.position);
            ((TextView) convertView.findViewById(R.id.messageTextView)).setText(data.message);

            return convertView;
        }
    }
}
