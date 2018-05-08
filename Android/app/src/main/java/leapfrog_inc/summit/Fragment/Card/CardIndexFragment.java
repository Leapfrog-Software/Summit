package leapfrog_inc.summit.Fragment.Card;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import leapfrog_inc.summit.Fragment.BaseFragment;
import leapfrog_inc.summit.R;

/**
 * Created by Leapfrog-Software on 2018/05/08.
 */

public class CardIndexFragment extends BaseFragment {

    private int mCurrentKanaIndex;

    public void setDefaultIndex(int defaultIndex) {
        mCurrentKanaIndex = defaultIndex;
    }

    public void set(int kanaIndex) {
        mCurrentKanaIndex = kanaIndex;
        resetKanas(getView());
    }

    public int getCurrentKanaIndex() {
        return mCurrentKanaIndex;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_card_index, null);

        resetKanas(view);

        return view;
    }

    private void resetKanas(View view) {

        if (view == null) return;

        int[] kanaTextViewIds = {R.id.kana1TextView, R.id.kana2TextView, R.id.kana3TextView, R.id.kana4TextView, R.id.kana5TextView, R.id.kana6TextView, R.id.kana7TextView, R.id.kana8TextView, R.id.kana9TextView, R.id.kana10TextView, R.id.kana11TextView};
        for (int i = 0; i < kanaTextViewIds.length; i++) {
            TextView kanaTextView = (TextView) view.findViewById(kanaTextViewIds[i]);
            Object tag = kanaTextView.getTag();
            if (tag == null) continue;

            if ((int)tag == mCurrentKanaIndex) {
                kanaTextView.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.basicBlack));
            } else {
                kanaTextView.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.weakBlack));
            }
        }
    }
}

