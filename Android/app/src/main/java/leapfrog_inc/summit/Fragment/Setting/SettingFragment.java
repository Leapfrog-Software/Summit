package leapfrog_inc.summit.Fragment.Setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import leapfrog_inc.summit.Fragment.BaseFragment;
import leapfrog_inc.summit.Fragment.Common.WebViewFragment;
import leapfrog_inc.summit.Function.Constants;
import leapfrog_inc.summit.Function.PicassoUtility;
import leapfrog_inc.summit.Http.Requester.UserRequester;
import leapfrog_inc.summit.R;

/**
 * Created by Leapfrog-Software on 2018/05/08.
 */

public class SettingFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_setting, null);

        initContent(view);
        initAction(view);

        return view;
    }

    private void initAction(View view) {

        ((Button)view.findViewById(R.id.profileButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingProfileFragment fragment = new SettingProfileFragment();
                stackFragment(fragment, AnimationType.horizontal);
            }
        });

        ((Button)view.findViewById(R.id.termButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stackWebView(Constants.WebPageUrl.terms, "利用規約");
            }
        });

        ((Button)view.findViewById(R.id.privacyPolicyButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stackWebView(Constants.WebPageUrl.privacyPolicy, "個人情報保護方針");
            }
        });
    }

    private void stackWebView(String url, String title) {

        WebViewFragment fragment = new WebViewFragment();
        fragment.set(url, title);
        stackFragment(fragment, AnimationType.horizontal);
    }

    private void initContent(View view) {

        UserRequester.UserData myUserData = UserRequester.getInstance().myUserData();
        if (myUserData == null) return;

        PicassoUtility.getUserImage(getActivity(), Constants.UserImageDirectory + myUserData.userId, (ImageView)view.findViewById(R.id.faceImageView));

        ((TextView)view.findViewById(R.id.nameTextView)).setText(myUserData.nameLast + " " + myUserData.nameFirst);
        ((TextView)view.findViewById(R.id.companyTextView)).setText(myUserData.company);
        ((TextView)view.findViewById(R.id.positionTextView)).setText(myUserData.position);
    }
}
