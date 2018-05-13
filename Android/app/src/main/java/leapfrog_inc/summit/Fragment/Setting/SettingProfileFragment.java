package leapfrog_inc.summit.Fragment.Setting;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import leapfrog_inc.summit.Fragment.BaseFragment;
import leapfrog_inc.summit.Fragment.Common.Dialog;
import leapfrog_inc.summit.Fragment.Common.Loading;
import leapfrog_inc.summit.Fragment.Common.PickerFragment;
import leapfrog_inc.summit.Fragment.Common.WebViewFragment;
import leapfrog_inc.summit.Function.Constants;
import leapfrog_inc.summit.Function.PicassoUtility;
import leapfrog_inc.summit.Function.SaveData;
import leapfrog_inc.summit.Http.ImageUploader;
import leapfrog_inc.summit.Http.Requester.AccountRequester;
import leapfrog_inc.summit.Http.Requester.Enum.AgeType;
import leapfrog_inc.summit.Http.Requester.Enum.GenderType;
import leapfrog_inc.summit.Http.Requester.UserRequester;
import leapfrog_inc.summit.MainActivity;
import leapfrog_inc.summit.R;

/**
 * Created by Leapfrog-Software on 2018/05/11.
 */

public class SettingProfileFragment extends BaseFragment {

    public static int requestCodeGallery = 1000;
    public static int requestCodePermission = 1001;
    private Uri mImageUri;
    private String mEditedMessage = null;
    private String mEditedNameLast = null;
    private String mEditedNameFirst = null;
    private String mEditedKanaLast = null;
    private String mEditedKanaFirst = null;
    private int mEditedAgeIndex = -1;
    private int mEditedGenderIndex = -1;
    private String mEditedCompany = null;
    private String mEditedPosition = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {

        View view = inflater.inflate(R.layout.fragment_setting_profile, null);

        initContent(view);
        initAction(view);

        return view;
    }

    private void initAction(View view) {

        ((ImageButton)view.findViewById(R.id.backButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserIfNeeded();
            }
        });

        ((Button)view.findViewById(R.id.faceEditButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickFace();
            }
        });

        ((Button)view.findViewById(R.id.messageButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingProfileMessageFragment fragment = new SettingProfileMessageFragment();
                UserRequester.UserData myUserData = UserRequester.getInstance().myUserData();
                String defaultMessage = (mEditedMessage == null) ? myUserData.message : mEditedMessage;
                fragment.set(defaultMessage, new SettingProfileMessageFragment.SettingProfileMessageFragmentCallback() {
                    @Override
                    public void didEditMessage(String message) {
                        setMessage((TextView)getView().findViewById(R.id.messageTextView), message);
                        mEditedMessage = message;
                    }
                });
                stackFragment(fragment, AnimationType.horizontal);
            }
        });

        ((Button)view.findViewById(R.id.nameButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingProfileNameFragment fragment = new SettingProfileNameFragment();
                UserRequester.UserData myUserData = UserRequester.getInstance().myUserData();
                String defaultLast = (mEditedNameLast == null) ? myUserData.nameLast : mEditedNameLast;
                String defaultFirst = (mEditedNameFirst == null) ? myUserData.nameFirst : mEditedNameFirst;
                fragment.set(false, defaultLast, defaultFirst, new SettingProfileNameFragment.SettingProfileNameFragmentCallback() {
                    @Override
                    public void didEditName(String last, String first) {
                        setName((TextView)getView().findViewById(R.id.nameTextView), last, first);
                        mEditedNameLast = last;
                        mEditedNameFirst = first;
                    }
                });
                stackFragment(fragment, AnimationType.horizontal);
            }
        });

        ((Button)view.findViewById(R.id.kanaButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingProfileNameFragment fragment = new SettingProfileNameFragment();
                UserRequester.UserData myUserData = UserRequester.getInstance().myUserData();
                String defaultLast = (mEditedKanaLast == null) ? myUserData.kanaLast : mEditedKanaLast;
                String defaultFirst = (mEditedKanaFirst == null) ? myUserData.kanaFirst : mEditedKanaFirst;
                fragment.set(true, defaultLast, defaultFirst, new SettingProfileNameFragment.SettingProfileNameFragmentCallback() {
                    @Override
                    public void didEditName(String last, String first) {
                        setKana((TextView)getView().findViewById(R.id.kanaTextView), last, first);
                        mEditedKanaLast = last;
                        mEditedKanaFirst = first;
                    }
                });
                stackFragment(fragment, AnimationType.horizontal);
            }
        });

        ((Button)view.findViewById(R.id.ageButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickerFragment fragment = new PickerFragment();
                ArrayList<AgeType> ageValues = AgeType.allValue();
                ArrayList<String> dataList = new ArrayList<String>();
                for (int i = 0; i < ageValues.size(); i++) {
                    dataList.add(ageValues.get(i).toText());
                }
                UserRequester.UserData myUserData = UserRequester.getInstance().myUserData();
                int index = (myUserData.age == AgeType.unknown) ? -1 : AgeType.allValue().indexOf(myUserData.age);
                int defaultIndex = (mEditedAgeIndex == -1) ? index : mEditedAgeIndex;
                fragment.set("年代", defaultIndex, dataList, new PickerFragment.PickerFragmentCallback() {
                    @Override
                    public void didSelect(int index) {
                        AgeType selectedAge = AgeType.allValue().get(index);
                        setAge((TextView)getView().findViewById(R.id.ageTextView), selectedAge);
                        mEditedAgeIndex = index;
                    }
                });
                stackFragment(fragment, AnimationType.none);
            }
        });

        ((Button)view.findViewById(R.id.genderButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickerFragment fragment = new PickerFragment();
                ArrayList<GenderType> genderValues = GenderType.allValue();
                ArrayList<String> dataList = new ArrayList<String>();
                for (int i = 0; i < genderValues.size(); i++) {
                    dataList.add(genderValues.get(i).toText());
                }
                UserRequester.UserData myUserData = UserRequester.getInstance().myUserData();
                int index = (myUserData.gender == GenderType.unknown) ? -1 : GenderType.allValue().indexOf(myUserData.gender);
                int defaultIndex = (mEditedGenderIndex == -1) ? index : mEditedGenderIndex;
                fragment.set("性別", defaultIndex, dataList, new PickerFragment.PickerFragmentCallback() {
                    @Override
                    public void didSelect(int index) {
                        GenderType selectedGender = GenderType.allValue().get(index);
                        setGender((TextView)getView().findViewById(R.id.genderTextView), selectedGender);
                        mEditedGenderIndex = index;
                    }
                });
                stackFragment(fragment, AnimationType.none);
            }
        });

        ((Button)view.findViewById(R.id.companyButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingProfileTextFragment fragment = new SettingProfileTextFragment();
                UserRequester.UserData myUserData = UserRequester.getInstance().myUserData();
                String defaultText = (mEditedCompany == null) ? myUserData.company : mEditedCompany;
                fragment.set("会社・組織名", defaultText, new SettingProfileTextFragment.SettingProfileTextFragmentCallback() {
                    @Override
                    public void didEditText(String text) {
                        setCompany((TextView)getView().findViewById(R.id.companyTextView), text);
                        mEditedCompany = text;
                    }
                });
                stackFragment(fragment, AnimationType.horizontal);
            }
        });

        ((Button)view.findViewById(R.id.positionButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingProfileTextFragment fragment = new SettingProfileTextFragment();
                UserRequester.UserData myUserData = UserRequester.getInstance().myUserData();
                String defaultText = (mEditedPosition == null) ? myUserData.position : mEditedPosition;
                fragment.set("役職・職種", defaultText, new SettingProfileTextFragment.SettingProfileTextFragmentCallback() {
                    @Override
                    public void didEditText(String text) {
                        setPosition((TextView)getView().findViewById(R.id.positionTextView), text);
                        mEditedPosition = text;
                    }
                });
                stackFragment(fragment, AnimationType.horizontal);
            }
        });
    }

    private void initContent(View view) {

        UserRequester.UserData myUserData = UserRequester.getInstance().myUserData();
        if (myUserData == null) return;

        PicassoUtility.getUserImage(getActivity(), Constants.UserImageDirectory + myUserData.userId, (ImageView)view.findViewById(R.id.faceImageView));

        setMessage((TextView)view.findViewById(R.id.messageTextView), myUserData.message);
        setName((TextView)view.findViewById(R.id.nameTextView), myUserData.nameLast, myUserData.nameFirst);
        setKana((TextView)view.findViewById(R.id.kanaTextView), myUserData.kanaLast, myUserData.kanaFirst);
        setAge((TextView)view.findViewById(R.id.ageTextView), myUserData.age);
        setGender((TextView)view.findViewById(R.id.genderTextView), myUserData.gender);
        setCompany((TextView)view.findViewById(R.id.companyTextView), myUserData.company);
        setPosition((TextView)view.findViewById(R.id.positionTextView), myUserData.position);
    }

    private void setMessage(TextView textView, String message) {
        if (message.length() == 0) {
            textView.setText("(未設定)");
            textView.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.profileInactive));
        } else {
            textView.setText(message);
            textView.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.profileActive));
        }
    }

    private void setName(TextView textView, String nameLast, String nameFirst) {
        if ((nameLast.length() == 0) && (nameFirst.length() == 0)) {
            textView.setText("(未設定)");
            textView.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.profileInactive));
        } else {
            textView.setText(nameLast + " " + nameFirst);
            textView.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.profileActive));
        }
    }

    private void setKana(TextView textView, String kanaLast, String kanaFirst) {
        if ((kanaLast.length() == 0) && (kanaFirst.length() == 0)) {
            textView.setText("(未設定)");
            textView.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.profileInactive));
        } else {
            textView.setText(kanaLast + " " + kanaFirst);
            textView.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.profileActive));
        }
    }

    private void setAge(TextView textView, AgeType age) {
        if (age == AgeType.unknown) {
            textView.setText("(未設定)");
            textView.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.profileInactive));
        } else {
            textView.setText(age.toText());
            textView.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.profileActive));
        }
    }

    private void setGender(TextView textView, GenderType gender) {
        if (gender == GenderType.unknown) {
            textView.setText("(未設定)");
            textView.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.profileInactive));
        } else {
            textView.setText(gender.toText());
            textView.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.profileActive));
        }
    }

    private void setCompany(TextView textView, String company) {
        if (company.length() == 0) {
            textView.setText("(未設定)");
            textView.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.profileInactive));
        } else {
            textView.setText(company);
            textView.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.profileActive));
        }
    }

    private void setPosition(TextView textView, String position) {
        if (position.length() == 0) {
            textView.setText("(未設定)");
            textView.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.profileInactive));
        } else {
            textView.setText(position);
            textView.setTextColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.profileActive));
        }
    }

    private void onClickFace() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            }
            else{
                ActivityCompat.requestPermissions(getActivity(), new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE }, requestCodePermission);
            }
        } else {
            openGallery();
        }
    }

    private void updateUserIfNeeded() {

        boolean needUpdate = false;

        UserRequester.UserData newUserData = UserRequester.getInstance().myUserData();
        if (newUserData == null) {
            popFragment(AnimationType.horizontal);
            return;
        }

        if (mEditedMessage != null) {
            needUpdate = true;
            newUserData.message = mEditedMessage;
        }
        if ((mEditedNameLast != null) || (mEditedNameFirst != null)) {
            needUpdate = true;
            newUserData.nameLast = mEditedNameLast;
            newUserData.nameFirst = mEditedNameFirst;
        }
        if ((mEditedKanaLast != null) || (mEditedKanaFirst != null)) {
            needUpdate = true;
            newUserData.kanaLast = mEditedKanaLast;
            newUserData.kanaFirst = mEditedKanaFirst;
        }
        if (mEditedAgeIndex != -1) {
            needUpdate = true;
            newUserData.age = AgeType.allValue().get(mEditedAgeIndex);
        }
        if (mEditedGenderIndex != -1) {
            needUpdate = true;
            newUserData.gender = GenderType.allValue().get(mEditedGenderIndex);
        }
        if (mEditedCompany != null) {
            needUpdate = true;
            newUserData.company = mEditedCompany;
        }
        if (mEditedPosition != null) {
            needUpdate = true;
            newUserData.position = mEditedPosition;
        }

        if (needUpdate) {
            Loading.start(getActivity());
            AccountRequester.updateUser(newUserData, new AccountRequester.UpdateUserCallback() {
                @Override
                public void didReceiveData(boolean result) {
                    Loading.stop(getActivity());

                    if (result) {
                        Activity mainActivity = getActivity();
                        if (mainActivity == null) return;
                        List<Fragment> fragments = ((MainActivity)mainActivity).getCurrentFragments();
                        for (int i = 0; i < fragments.size(); i++) {
                            Fragment fragment = fragments.get(i);
                            if (fragment instanceof SettingFragment) {
                                ((SettingFragment)fragment).resetContent(null);
                            }
                        }
                        popFragment(AnimationType.horizontal);

                    } else {
                        Dialog.show(getActivity(), Dialog.Style.error, "エラー", "通信に失敗しました", null);
                    }
                }
            });
        } else {
            popFragment(AnimationType.horizontal);
        }
    }

    private void openGallery() {

        Intent intentGallery = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intentGallery.addCategory(Intent.CATEGORY_OPENABLE);
        intentGallery.setType("image/jpeg");

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "tmp.jpg");
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        mImageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);

        Intent intent = Intent.createChooser(intentCamera, "画像の選択");
        intent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {intentGallery});
        getActivity().startActivityForResult(intent, requestCodeGallery);
    }

    public void didGrantPermission() {
        openGallery();
    }

    public void didSelectImage(Intent data) {

        Uri uri = (data != null ? data.getData() : mImageUri);
        if(uri == null) {
            return;
        }
        MediaScannerConnection.scanFile(getActivity(), new String[]{ uri.getPath() }, new String[]{"image/jpeg"}, null);

        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
        } catch (Exception e) {
            return;
        }
        int bmpWidth = bitmap.getWidth();
        Matrix scale = new Matrix();
        scale.postScale((300.0f / (float)bmpWidth), (300.0f / (float)bmpWidth));
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), scale, false);
        bitmap.recycle();
        bitmap = null;

        View view = getView();
        if (view == null) return;

        ImageUploader.ImageUploaderParameter param = new ImageUploader.ImageUploaderParameter();
        param.userId = SaveData.getInstance().userId;
        param.bitmap = resizeBmp;
        ImageUploader uploader = new ImageUploader(new ImageUploader.ImageUploaderCallback() {
            @Override
            public void didReceive(boolean result) {
                PicassoUtility.getUserImage(getActivity(), Constants.UserImageDirectory + SaveData.getInstance().userId, (ImageView)getView().findViewById(R.id.faceImageView));
            }
        });
        uploader.execute(param);
    }
}
