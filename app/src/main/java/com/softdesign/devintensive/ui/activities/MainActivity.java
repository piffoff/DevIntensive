package com.softdesign.devintensive.ui.activities;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.utils.ConstantMenedger;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private int mCurentEditMode = 0;

    private static final String TAG = ConstantMenedger.TAG_PREFIX + "_MainActivity";
    private ImageView mImageView_1;
    private CoordinatorLayout mCoordinatorLayout;
    private Toolbar mToolbar;
    private DrawerLayout mNavigationDrawer;
    private FloatingActionButton mFab;
    private EditText mUserPhone, mUserMail, mUserVk, mUserGit, mUserAbout;

    private List<EditText> mUSerInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate()");

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);

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

        if (savedInstanceState == null) {
            showSnackBar("First run!");
        } else showSnackBar("Not first run.");

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setupToolbar();

        mNavigationDrawer = (DrawerLayout) findViewById(R.id.navigation_drawer);

        setupDrawer();

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurentEditMode == 0) {
                    changeEditMode(1);
                }
                else {
                    changeEditMode(0);
                }

            }
        });

        mUserPhone = (EditText) findViewById(R.id.phone);
        mUserMail = (EditText) findViewById(R.id.mail);
        mUserVk = (EditText) findViewById(R.id.vk);
        mUserGit = (EditText) findViewById(R.id.git);
        mUserAbout = (EditText) findViewById(R.id.about);

        mUSerInfo = new ArrayList<>();
        mUSerInfo.add(mUserPhone);
        mUSerInfo.add(mUserMail);
        mUSerInfo.add(mUserVk);
        mUSerInfo.add(mUserGit);
        mUSerInfo.add(mUserAbout);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart()");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState()");
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

    public void changeEditMode(int mode) {
        if (mode == 1) {
            for (EditText userValue : mUSerInfo) {
                userValue.setEnabled(true);
                userValue.setFocusable(true);
            }
        } else {
            for (EditText userValue : mUSerInfo) {
                userValue.setEnabled(false);
                userValue.setFocusable(false);
            }
        }

    }

    public void loadUserInfo() {

    }

    public void saveUserInfo() {

    }

}

