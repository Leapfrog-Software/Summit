package leapfrog_inc.summit.Fragment.Setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import leapfrog_inc.summit.Fragment.BaseFragment;
import leapfrog_inc.summit.Fragment.Common.Dialog;
import leapfrog_inc.summit.R;

/**
 * Created by Leapfrog-Software on 2018/05/12.
 */

public class SettingProfileMessageFragment extends BaseFragment {

    private String mDefaultMessage = "";
    private SettingProfileMessageFragmentCallback mCallback;

    public void set(String defaultMessage, SettingProfileMessageFragmentCallback callback) {
        mDefaultMessage = defaultMessage;
        mCallback = callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_setting_profile_message, null);

        initContent(view);
        initAction(view);

        return view;
    }

    private void initAction(View view) {

        ((ImageButton) view.findViewById(R.id.backButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = ((EditText)getView().findViewById(R.id.messageEditText)).getText().toString();
                if (mDefaultMessage.equals(message)) {
                    popFragment(AnimationType.horizontal);
                } else {
                    if (message.length() > 128) {
                        Dialog.show(getActivity(), Dialog.Style.error, "入力エラー", "128文字以内で入力してください", null);
                    } else {
                        mCallback.didEditMessage(message);
                        popFragment(AnimationType.horizontal);
                    }
                }
            }
        });
    }

    private void initContent(View view) {

        ((EditText)view.findViewById(R.id.messageEditText)).setText(mDefaultMessage);
    }

    public interface SettingProfileMessageFragmentCallback {
        void didEditMessage(String message);
    }
}
