package leapfrog_inc.summit;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import leapfrog_inc.summit.Fragment.Setting.SettingProfileFragment;
import leapfrog_inc.summit.Fragment.Splash.SplashFragment;
import leapfrog_inc.summit.Fragment.Tabbar.TabbarFragment;
import leapfrog_inc.summit.Function.SaveData;

public class MainActivity extends AppCompatActivity {

    public TabbarFragment mTabbarFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SaveData.getInstance().initialize(this);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.tabContainer, new SplashFragment());
        transaction.commitAllowingStateLoss();
    }

    public int getSubContainerId() {
        return R.id.subContainer;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode != SettingProfileFragment.requestCodePermission) {
            return;
        }

        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (int i = 0; i < fragments.size(); i++) {
            if (fragments.get(i) instanceof SettingProfileFragment) {
                ((SettingProfileFragment)fragments.get(i)).didGrantPermission();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != SettingProfileFragment.requestCodeGallery) {
            return;
        }
        if(resultCode != RESULT_OK) {
            return ;
        }

        List<Fragment> fragments = getCurrentFragments();
        for (int i = 0; i < fragments.size(); i++) {
            if (fragments.get(i) instanceof SettingProfileFragment) {
                ((SettingProfileFragment)fragments.get(i)).didSelectImage(data);
            }
        }
    }

    public List<Fragment> getCurrentFragments() {
        return getSupportFragmentManager().getFragments();
    }
}
