package leapfrog_inc.summit.Fragment.Attend.Attend;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;

import leapfrog_inc.summit.Function.DeviceUtility;
import leapfrog_inc.summit.MainActivity;
import leapfrog_inc.summit.R;

/**
 * Created by Leapfrog-Software on 2018/05/14.
 */

public class AttendTableLayout extends FrameLayout {

    private Context mContext;
    static private int userLayoutTagStartNo = 1000;

    public AttendTableLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.layout_attend_table, this, true);

        mContext = context;

        initUserLayout();
    }

    private void initUserLayout() {

        int width = getWidth();

        for (int i = 0; i < 8; i++) {
            AttendTableUserLayout userLayout = new AttendTableUserLayout(mContext, null);
            userLayout.setTag(userLayoutTagStartNo + i);
            int w = width * 3 / 10;
            int h = width / 4 + (int)(26 * DeviceUtility.getDeviceDensity((MainActivity)mContext));

            double[] degrees = {-90, 90, 0, 180, -45, 135, 225, 45};
            double degree = degrees[i];
            double radian = Math.toRadians(degree);
            double r = width / 3;
            double x = width / 2 + r * Math.cos(radian) - w / 2;
            double y = width / 2 + r * Math.sin(radian) - w / 2;

            MarginLayoutParams params = new MarginLayoutParams(w, h);
            params.topMargin = (int)y;
            params.leftMargin = (int)x;
            userLayout.setLayoutParams(params);
            addView(userLayout);
        }
    }

    public void set(ArrayList<String> userIds) {

        for (int i = 0; i < 8; i++) {
            AttendTableUserLayout userLayout = (AttendTableUserLayout) findViewWithTag(userLayoutTagStartNo + i);
            if (userIds.size() > i) {
                userLayout.setVisibility(View.VISIBLE);
                userLayout.set(userIds.get(i));
            } else {
                userLayout.setVisibility(View.INVISIBLE);
            }
        }
    }
}