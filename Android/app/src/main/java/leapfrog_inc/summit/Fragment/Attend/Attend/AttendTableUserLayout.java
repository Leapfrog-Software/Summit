package leapfrog_inc.summit.Fragment.Attend.Attend;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import leapfrog_inc.summit.Function.Constants;
import leapfrog_inc.summit.Function.PicassoUtility;
import leapfrog_inc.summit.Http.Requester.UserRequester;
import leapfrog_inc.summit.R;

/**
 * Created by Leapfrog-Software on 2018/05/15.
 */

public class AttendTableUserLayout extends FrameLayout {

    private Context mContext;

    public AttendTableUserLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.layout_attend_table_user, this, true);

        mContext = context;
    }

    public void set(String userId) {

        UserRequester.UserData userData = UserRequester.getInstance().query(userId);
        if (userData == null) return;

        PicassoUtility.getUserImage(mContext, Constants.UserImageDirectory + userId, (ImageView)findViewById(R.id.faceImageView));

        ((TextView)findViewById(R.id.nameTextView)).setText(userData.nameLast + " " + userData.nameFirst);
        ((TextView)findViewById(R.id.profileTextView)).setText(userData.company + " " + userData.position);
    }
}
