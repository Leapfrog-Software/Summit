package leapfrog_inc.summit.Fragment.Splash;

import android.accounts.Account;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import leapfrog_inc.summit.Fragment.BaseFragment;
import leapfrog_inc.summit.Fragment.Common.Dialog;
import leapfrog_inc.summit.Fragment.Tabbar.TabbarFragment;
import leapfrog_inc.summit.Function.SaveData;
import leapfrog_inc.summit.Http.Requester.AccountRequester;
import leapfrog_inc.summit.Http.Requester.MessageRequester;
import leapfrog_inc.summit.Http.Requester.ScheduleRequester;
import leapfrog_inc.summit.Http.Requester.UserRequester;
import leapfrog_inc.summit.R;

/**
 * Created by Leapfrog-Software on 2018/05/08.
 */

public class SplashFragment extends BaseFragment {

    enum FetchResult {
        ok,
        error,
        progress
    }

    private FetchResult mScheduleFetchResult = FetchResult.progress;
    private FetchResult mUserFetchResult = FetchResult.progress;
    private FetchResult mMessageFetchResult = FetchResult.progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_splash, null);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SaveData.getInstance().userId.length() == 0) {
                    createUser();
                } else {
                    fetchMasterData();
                }
            }
        }, 1000);

        return view;
    }

    private void createUser() {

        AccountRequester.createUser(new AccountRequester.CreateUserCallback() {
            @Override
            public void didReceiveData(boolean result, String userId) {
                if (result) {
                    SaveData saveData = SaveData.getInstance();
                    saveData.userId = userId;
                    saveData.save();

                    fetchMasterData();

                } else {
                    Dialog.show(getActivity(), Dialog.Style.error, "エラー", "通信に失敗しました", new Dialog.DialogCallback() {
                        @Override
                        public void didClose() {
                            createUser();
                        }
                    });
                }
            }
        });
    }

    private void fetchMasterData() {

        if (mScheduleFetchResult != FetchResult.ok) {
            ScheduleRequester.getInstance().fetch(new ScheduleRequester.ScheduleRequesterCallback() {
                @Override
                public void didReceiveData(boolean result) {
                    if (result)     mScheduleFetchResult = FetchResult.ok;
                    else            mScheduleFetchResult = FetchResult.error;
                    checkResult();
                }
            });
        }
        if (mUserFetchResult != FetchResult.ok) {
            UserRequester.getInstance().fetch(new UserRequester.UserRequesterCallback() {
                @Override
                public void didReceiveData(boolean result) {
                    if (result)     mUserFetchResult = FetchResult.ok;
                    else            mUserFetchResult = FetchResult.error;
                    checkResult();
                }
            });
        }
        if (mMessageFetchResult != FetchResult.ok) {
            MessageRequester.getInstance().fetch(new MessageRequester.MessageRequesterCallback() {
                @Override
                public void didReceiveData(boolean result) {
                    if (result)     mMessageFetchResult = FetchResult.ok;
                    else            mMessageFetchResult = FetchResult.error;
                    checkResult();
                }
            });
        }
    }

    private void checkResult() {

        if ((mScheduleFetchResult == FetchResult.progress)
                || (mUserFetchResult == FetchResult.progress)
                || (mMessageFetchResult == FetchResult.progress)) {
            return;
        }

        if ((mScheduleFetchResult == FetchResult.error)
                || (mUserFetchResult == FetchResult.error)
                || (mMessageFetchResult == FetchResult.error)) {

            Dialog.show(getActivity(), Dialog.Style.error, "エラー", "通信に失敗しました", new Dialog.DialogCallback() {
                @Override
                public void didClose() {
                    checkResult();
                }
            });

            return;
        }

        stackFragment(new TabbarFragment(), AnimationType.none);
    }
}