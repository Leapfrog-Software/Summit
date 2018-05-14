package leapfrog_inc.summit.Fragment.Card;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import leapfrog_inc.summit.Fragment.BaseFragment;
import leapfrog_inc.summit.Function.Constants;
import leapfrog_inc.summit.Function.KanaUtils;
import leapfrog_inc.summit.Function.PicassoUtility;
import leapfrog_inc.summit.Http.Requester.UserRequester;
import leapfrog_inc.summit.R;

/**
 * Created by Leapfrog-Software on 2018/05/08.
 */

public class CardFragment extends BaseFragment {

    private ArrayList<CardAdapterData> mAdapterDataList;
    private CardIndexFragment mCardIndexFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_card, null);

        initListView(view);
        initIndexFragment();

        return view;
    }

    private void initListView(View view) {

        UserRequester.UserData myUserData = UserRequester.getInstance().myUserData();
        if (myUserData == null) return;

        mAdapterDataList = new ArrayList<CardAdapterData>();

        final CardAdapater adapter = new CardAdapater(getActivity());

        int currentKanaIndex = -1;

        ArrayList<UserRequester.UserData> userList = UserRequester.getInstance().getUserList();
        for (int i = 0; i < userList.size(); i++) {
            UserRequester.UserData userData = userList.get(i);

            if (!myUserData.cards.contains(userData.userId)) {
                continue;
            }

            if (userData.kanaLast.length() <= 0) continue;
            String userKanaPrefix = String.valueOf(userData.nameLast.charAt(0));
            int userKanaIndex = KanaUtils.columnIndex(userKanaPrefix);
            if (userKanaIndex == -1) continue;
            if (userKanaIndex != currentKanaIndex) {
                String kana = KanaUtils.kanas().get(userKanaIndex).get(0);
                CardAdapterData data = new CardAdapterData();
                data.kana = kana;
                adapter.add(data);
                mAdapterDataList.add(data);
                currentKanaIndex = userKanaIndex;
            }
            CardAdapterData data = new CardAdapterData();
            data.userData = userData;
            adapter.add(data);
            mAdapterDataList.add(data);
        }

        ListView listView = (ListView)view.findViewById(R.id.listView);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CardAdapterData data = (CardAdapterData)adapterView.getItemAtPosition(i);
                UserRequester.UserData userData = data.userData;
                if (userData != null) {
                    CardDetailFragment fragment = new CardDetailFragment();
                    fragment.set(userData, false);
                    stackFragment(fragment, AnimationType.horizontal);
                }
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int state) {
                switch (state) {
                    case SCROLL_STATE_IDLE:
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL:
                        break;
                    case SCROLL_STATE_FLING:
                        break;
                }
            }
            @Override
            public void onScroll(AbsListView absListView, int position, int i1, int i2) {

                if (mAdapterDataList.size() > position) {
                    for (int i = position; i >= 0; i--) {
                        CardAdapterData adapterData = mAdapterDataList.get(i);
                        if (adapterData.kana != null) {
                            int kanaIndex = KanaUtils.columnIndex(adapterData.kana);
                            if ((kanaIndex != -1) && (mCardIndexFragment != null) && (kanaIndex != mCardIndexFragment.getCurrentKanaIndex())) {
                                mCardIndexFragment.set(kanaIndex);
                            }
                            return;
                        }
                    }
                }
            }
        });
    }

    private void initIndexFragment() {

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        CardIndexFragment fragment = new CardIndexFragment();

        int firstKanaIndex = -1;
        for (int i = 0; i < mAdapterDataList.size(); i++) {
            String kana = mAdapterDataList.get(i).kana;
            if (kana != null) {
                int kanaIndex = KanaUtils.columnIndex(kana);
                if (kanaIndex != -1) {
                    firstKanaIndex = kanaIndex;
                    break;
                }
            }
        }
        fragment.set(firstKanaIndex);
        transaction.add(R.id.cardIndexBaseLayout, fragment);
        transaction.commit();

        mCardIndexFragment = fragment;
    }

    private class CardAdapterData {
        String kana;
        UserRequester.UserData userData;
    }

    private class CardAdapater extends ArrayAdapter<CardAdapterData> {

        LayoutInflater mInflater;
        Context mContext;

        public CardAdapater(Context context){
            super(context, 0);
            mInflater = LayoutInflater.from(context);
            mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            CardAdapterData data = getItem(position);
            if (data.kana != null) {
                convertView = mInflater.inflate(R.layout.adapter_card_kana, parent, false);
                ((TextView)convertView.findViewById(R.id.kanaTextView)).setText(data.kana);
                return convertView;
            } else {
                convertView = mInflater.inflate(R.layout.adapter_card_user, parent, false);

                PicassoUtility.getUserImage(mContext, Constants.UserImageDirectory + data.userData.userId, (ImageView)convertView.findViewById(R.id.faceImageView));

                ((TextView)convertView.findViewById(R.id.nameTextView)).setText(data.userData.nameLast + " " + data.userData.nameFirst);
                ((TextView)convertView.findViewById(R.id.companyTextView)).setText(data.userData.company);
                ((TextView)convertView.findViewById(R.id.positionTextView)).setText(data.userData.position);
                ((TextView)convertView.findViewById(R.id.messageTextView)).setText(data.userData.message);
                return convertView;
            }
        }
    }
}
