package leapfrog_inc.summit.Fragment.Card;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import leapfrog_inc.summit.Fragment.BaseFragment;
import leapfrog_inc.summit.Http.Requester.UserRequester;
import leapfrog_inc.summit.R;

/**
 * Created by Leapfrog-Software on 2018/05/08.
 */

public class CardDetailFragment extends BaseFragment {

    public void set(UserRequester.UserData userData) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_card_detail, null);

        return view;
    }
}
