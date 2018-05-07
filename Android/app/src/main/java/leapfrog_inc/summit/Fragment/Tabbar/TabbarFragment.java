package leapfrog_inc.summit.Fragment.Tabbar;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import leapfrog_inc.summit.Fragment.BaseFragment;
import leapfrog_inc.summit.Fragment.Card.CardFragment;
import leapfrog_inc.summit.Fragment.Message.MessageFragment;
import leapfrog_inc.summit.Fragment.Schedule.ScheduleFragment;
import leapfrog_inc.summit.Fragment.Setting.SettingFragment;
import leapfrog_inc.summit.MainActivity;
import leapfrog_inc.summit.R;

/**
 * Created by Leapfrog-Software on 2018/05/08.
 */

public class TabbarFragment extends BaseFragment {

    private ScheduleFragment mScheduleFragment;
    private CardFragment mCardFragment;
    private MessageFragment mMessageFragment;
    private SettingFragment mSettingFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_tabbar, null);

        initFragmentController();
        changeTab(0);
        initAction(view);

        ((MainActivity)getActivity()).mTabbarFragment = this;

        return view;
    }

    private void initFragmentController() {

        android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        int contentsLayoutId = R.id.contentsLayout;

        mScheduleFragment = new ScheduleFragment();
        transaction.add(contentsLayoutId, mScheduleFragment);

        mCardFragment = new CardFragment();
        transaction.add(contentsLayoutId, mCardFragment);

        mMessageFragment = new MessageFragment();
        transaction.add(contentsLayoutId, mMessageFragment);

        mSettingFragment = new SettingFragment();
        transaction.add(contentsLayoutId, mSettingFragment);

        transaction.commitAllowingStateLoss();
    }

    private void initAction(View view) {

        ((Button)view.findViewById(R.id.tab1Button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeTab(0);
            }
        });

        ((Button)view.findViewById(R.id.tab2Button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeTab(1);
            }
        });

        ((Button)view.findViewById(R.id.tab3Button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeTab(2);
            }
        });

        ((Button)view.findViewById(R.id.tab4Button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeTab(3);
            }
        });
    }

    public void changeTab(int index) {

        View view = getView();
        if (view == null) return;

        if (index == 0) {
            mScheduleFragment.getView().setVisibility(View.VISIBLE);
            ((ImageView)view.findViewById(R.id.tab1OnImageView)).setVisibility(View.VISIBLE);
            ((ImageView)view.findViewById(R.id.tab1OffImageView)).setVisibility(View.GONE);
            // TODO
//            ((TextView)view.findViewById(R.id.tab1TextView)).setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.tabSelected));
        } else {
            mScheduleFragment.getView().setVisibility(View.GONE);
            ((ImageView)view.findViewById(R.id.tab1OnImageView)).setVisibility(View.GONE);
            ((ImageView)view.findViewById(R.id.tab1OffImageView)).setVisibility(View.VISIBLE);
//            ((TextView)view.findViewById(R.id.tab1TextView)).setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.tabUnselected));
        }

        if (index == 1) {
            mCardFragment.getView().setVisibility(View.VISIBLE);
            ((ImageView)view.findViewById(R.id.tab2OnImageView)).setVisibility(View.VISIBLE);
            ((ImageView)view.findViewById(R.id.tab2OffImageView)).setVisibility(View.GONE);
            // TODO
//            ((TextView)view.findViewById(R.id.tab1TextView)).setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.tabSelected));
        } else {
            mCardFragment.getView().setVisibility(View.GONE);
            ((ImageView)view.findViewById(R.id.tab2OnImageView)).setVisibility(View.GONE);
            ((ImageView)view.findViewById(R.id.tab2OffImageView)).setVisibility(View.VISIBLE);
//            ((TextView)view.findViewById(R.id.tab1TextView)).setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.tabUnselected));
        }

        if (index == 2) {
            mMessageFragment.getView().setVisibility(View.VISIBLE);
            ((ImageView)view.findViewById(R.id.tab3OnImageView)).setVisibility(View.VISIBLE);
            ((ImageView)view.findViewById(R.id.tab3OffImageView)).setVisibility(View.GONE);
            // TODO
//            ((TextView)view.findViewById(R.id.tab1TextView)).setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.tabSelected));
        } else {
            mMessageFragment.getView().setVisibility(View.GONE);
            ((ImageView)view.findViewById(R.id.tab3OnImageView)).setVisibility(View.GONE);
            ((ImageView)view.findViewById(R.id.tab3OffImageView)).setVisibility(View.VISIBLE);
//            ((TextView)view.findViewById(R.id.tab1TextView)).setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.tabUnselected));
        }

        if (index == 3) {
            mSettingFragment.getView().setVisibility(View.VISIBLE);
            ((ImageView)view.findViewById(R.id.tab4OnImageView)).setVisibility(View.VISIBLE);
            ((ImageView)view.findViewById(R.id.tab4OffImageView)).setVisibility(View.GONE);
            // TODO
//            ((TextView)view.findViewById(R.id.tab1TextView)).setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.tabSelected));
        } else {
            mSettingFragment.getView().setVisibility(View.GONE);
            ((ImageView)view.findViewById(R.id.tab4OnImageView)).setVisibility(View.GONE);
            ((ImageView)view.findViewById(R.id.tab4OffImageView)).setVisibility(View.VISIBLE);
//            ((TextView)view.findViewById(R.id.tab1TextView)).setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.tabUnselected));
        }
    }
}
