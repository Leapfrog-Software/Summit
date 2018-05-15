package leapfrog_inc.summit.Fragment.Setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import leapfrog_inc.summit.Fragment.BaseFragment;
import leapfrog_inc.summit.Fragment.Common.Dialog;
import leapfrog_inc.summit.R;

/**
 * Created by Leapfrog-Software on 2018/05/12.
 */

public class SettingProfileNameFragment extends BaseFragment {

    private boolean mIsKana = false;
    private String mDefaultLast = "";
    private String mDefaultFirst = "";
    private SettingProfileNameFragmentCallback mCallback;

    public void set(boolean isKana, String defaultLast, String defaultFirst, SettingProfileNameFragmentCallback callback) {
        mIsKana = isKana;
        mDefaultLast = defaultLast;
        mDefaultFirst = defaultFirst;
        mCallback = callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_setting_profile_name, null);

        initContent(view);
        initAction(view);

        return view;
    }

    private void initAction(View view) {

        ((ImageButton) view.findViewById(R.id.backButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String last = ((EditText)getView().findViewById(R.id.lastEditText)).getText().toString();
                String first = ((EditText)getView().findViewById(R.id.firstEditText)).getText().toString();
                if ((mDefaultLast.equals(last)) && (mDefaultFirst.equals(first))) {
                    popFragment(AnimationType.horizontal);
                } else {
                    if ((last + first).length() > 12) {
                        Dialog.show(getActivity(), Dialog.Style.error, "入力エラー", "お名前は12文字以内で入力してください", null);
                    } else {
                        mCallback.didEditName(last, first);
                        popFragment(AnimationType.horizontal);
                    }
                }
            }
        });
    }

    private void initContent(View view) {

        if (mIsKana) {
            ((TextView)view.findViewById(R.id.titleTextView)).setText("氏名(かな)");
            ((EditText)view.findViewById(R.id.lastEditText)).setHint("やまだ");
            ((EditText)view.findViewById(R.id.firstEditText)).setHint("たろう");
        } else {
            ((TextView)view.findViewById(R.id.titleTextView)).setText("氏名");
            ((EditText)view.findViewById(R.id.lastEditText)).setHint("山田");
            ((EditText)view.findViewById(R.id.firstEditText)).setHint("太郎");
        }
        ((EditText)view.findViewById(R.id.lastEditText)).setText(mDefaultLast);
        ((EditText)view.findViewById(R.id.firstEditText)).setText(mDefaultFirst);
    }

    public interface SettingProfileNameFragmentCallback {
        void didEditName(String last, String first);
    }
}
