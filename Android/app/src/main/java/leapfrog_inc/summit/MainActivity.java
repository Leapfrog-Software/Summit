package leapfrog_inc.summit;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
}
