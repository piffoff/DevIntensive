package com.softdesign.devintensive.ui.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManger;
import com.softdesign.devintensive.utils.ConstantMenedger;
import com.softdesign.devintensive.utils.RoundImage;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private int mCurentEditMode = 0;
    private DataManger mDataManger;
    private static final String TAG = ConstantMenedger.TAG_PREFIX + "_MainActivity";
    private ImageView mImageView_1;
    private CoordinatorLayout mCoordinatorLayout;
    private Toolbar mToolbar;
    private DrawerLayout mNavigationDrawer;
    private FloatingActionButton mFab;
    private EditText mUserPhone, mUserMail, mUserVk, mUserGit, mUserAbout;
    private ImageView mUserAvatar;

    private List<EditText> mUSerInfoViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate()");

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mNavigationDrawer = (DrawerLayout) findViewById(R.id.navigation_drawer);
        mDataManger = DataManger.getInstance();

        mUserPhone = (EditText) findViewById(R.id.phone);
        mUserMail = (EditText) findViewById(R.id.mail);
        mUserVk = (EditText) findViewById(R.id.vk);
        mUserGit = (EditText) findViewById(R.id.git);
        mUserAbout = (EditText) findViewById(R.id.about);

        mUSerInfoViews = new ArrayList<>();
        mUSerInfoViews.add(mUserPhone);
        mUSerInfoViews.add(mUserMail);
        mUSerInfoViews.add(mUserVk);
        mUSerInfoViews.add(mUserGit);
        mUSerInfoViews.add(mUserAbout);

        mImageView_1 = (ImageView) findViewById(R.id.call_img);
        mImageView_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.call_img:
                        Log.d(TAG, "OnClickImageView()");
                        showProgress();
                        runWithDelay();
                        break;
                }
            }
        });

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurentEditMode == 0) {
                    mCurentEditMode = 1;
                } else {
                    mCurentEditMode = 0;
                }
                changeEditMode(mCurentEditMode);
            }
        });

        setupToolbar();
        setupDrawer();

        if (savedInstanceState == null) {
            showSnackBar("First run!");
        } else {
            mCurentEditMode = savedInstanceState.getInt(ConstantMenedger.EDIT_MODE_KEY, 0);
            changeEditMode(mCurentEditMode);
        };

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState()");

        outState.putInt(ConstantMenedger.EDIT_MODE_KEY, mCurentEditMode);
    }

    public void runWithDelay() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideProgress();
            }
        }, 3000);
    }

    public void showSnackBar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    public void setupDrawer() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        /*скругление аватарки*/
        mUserAvatar = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.user_avatar);
        mUserAvatar.setImageBitmap(getRoundBitmap(R.drawable.smile));
        /*нажатие меню*/
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                showSnackBar(item.getTitle().toString());
                item.setChecked(true);
                mNavigationDrawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }
    /*методы скругления аватарки*/
    private Bitmap getRoundBitmap(int drawableRes) {

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawableRes);
        bitmap = RoundImage.getRoundedBitmap(bitmap);
        return bitmap;
    }

    public void changeEditMode(int mode) {
        if (mode == 1) {
            mFab.setImageResource(R.drawable.ic_done_black_24dp);
            for (EditText userValue : mUSerInfoViews) {
                userValue.setEnabled(true);
                userValue.setFocusable(true);
                userValue.setFocusableInTouchMode(true);
            }
        } else {
            mFab.setImageResource(R.drawable.ic_create_black_24dp);
            for (EditText userValue : mUSerInfoViews) {
                userValue.setEnabled(false);
                userValue.setFocusable(false);
                userValue.setFocusableInTouchMode(false);
            }
        }
    }

    public void loadUserInfo() {
        List<String> userData = mDataManger.getPreferenceManager().laadUserProfileData();
        for (int i = 0; i < userData.size(); i++) {
            mUSerInfoViews.get(i).setText(userData.get(i));
        }
    }

    public void saveUserInfo() {
        List<String> userData = new ArrayList<>();
        for (EditText userFieldView : mUSerInfoViews) {
            userData.add(userFieldView.getText().toString());
        }
        mDataManger.getPreferenceManager().saveUserProfileData(userData);
    }

    @Override
    public void onBackPressed() {

        if (mNavigationDrawer.isDrawerOpen(GravityCompat.START)) {
         mNavigationDrawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

}