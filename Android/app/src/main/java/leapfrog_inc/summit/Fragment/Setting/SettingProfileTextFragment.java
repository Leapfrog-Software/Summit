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

public class SettingProfileTextFragment extends BaseFragment {

    private String mTitle = "";
    private String mDefaultText = "";
    private SettingProfileTextFragmentCallback mCallback;

    public void set(String title, String defaultText, SettingProfileTextFragmentCallback callback) {
        mTitle = title;
        mDefaultText = defaultText;
        mCallback = callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_setting_profile_text, null);

        initContent(view);
        initAction(view);

        return view;
    }

    private void initAction(View view) {

        ((ImageButton) view.findViewById(R.id.backButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = ((EditText)getView().findViewById(R.id.textEditText)).getText().toString();
                if (mDefaultText.equals(text)) {
                    popFragment(AnimationType.horizontal);
                } else {
                    if (text.length() > 12) {
                        Dialog.show(getActivity(), Dialog.Style.error, "入力エラー", "12文字以内で入力してください", null);
                    } else {
                        mCallback.didEditText(text);
                        popFragment(AnimationType.horizontal);
                    }
                }
            }
        });
    }

    private void initContent(View view) {

        ((TextView)view.findViewById(R.id.titleTextView)).setText(mTitle);
        ((EditText)view.findViewById(R.id.textEditText)).setText(mDefaultText);
    }

    public interface SettingProfileTextFragmentCallback {
        void didEditText(String text);
    }
}
