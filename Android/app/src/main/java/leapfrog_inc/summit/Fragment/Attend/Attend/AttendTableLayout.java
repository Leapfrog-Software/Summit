package leapfrog_inc.summit.Fragment.Attend.Attend;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import java.util.ArrayList;

import leapfrog_inc.summit.R;

/**
 * Created by Leapfrog-Software on 2018/05/14.
 */

public class AttendTableLayout extends FrameLayout {

    public AttendTableLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.layout_attend_table, this, true);
    }

    public void set(ArrayList<String> userIds) {

    }
}