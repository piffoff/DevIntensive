package com.softdesign.devintensive.ui.activities;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManger;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.RoundImage;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends BaseActivity {

    private static final String TAG = ConstantManager.TAG_PREFIX + "_MainActivity";

    private DataManger mDataManger;
    private int mCurrentEditMode = 0;
    private CoordinatorLayout mCoordinatorLayout;
    private Toolbar mToolbar;
    private DrawerLayout mNavigationDrawer;
    private FloatingActionButton mFab;
    private EditText mUserPhone, mUserMail, mUserVk, mUserGit, mUserAbout;
    private ImageView mUserAvatar, mProfileImage, mCallImg, mSendImg, mVkImg, mRepositoryImg;
    private RelativeLayout mProfilePlaceHolder;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private AppBarLayout mAppBarLayout;
    private SharedPreferences mSharedPreferences;

    private List<EditText> mUSerInfoViews;

    private AppBarLayout.LayoutParams mAppBarParams = null;
    private File mPhotoFile = null;
    private Uri mSelectedImage = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate()");

        defineVariables();

        defineClickListeners();

        setupToolbar();

        setupDrawer();

        if (savedInstanceState != null) {
            mCurrentEditMode = savedInstanceState.getInt(ConstantManager.EDIT_MODE_KEY, 0);
            changeEditMode(mCurrentEditMode);
        }

        loadUserInfo();
    }

    private void defineVariables() {

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mNavigationDrawer = (DrawerLayout) findViewById(R.id.navigation_drawer);
        mDataManger = DataManger.getInstance();
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);
        mProfileImage = (ImageView) findViewById(R.id.user_photo_img);
        mProfilePlaceHolder = (RelativeLayout) findViewById(R.id.profile_place_holder);
        mFab = (FloatingActionButton) findViewById(R.id.fab);

        mCallImg = (ImageView) findViewById(R.id.call_img);
        mSendImg = (ImageView) findViewById(R.id.send_img);
        mVkImg = (ImageView) findViewById(R.id.vk_img);
        mRepositoryImg = (ImageView) findViewById(R.id.repository_img);


        mUserPhone = (EditText) findViewById(R.id.phone);
        mUserMail = (EditText) findViewById(R.id.mail);
        mUserVk = (EditText) findViewById(R.id.vk_text);
        mUserGit = (EditText) findViewById(R.id.git);
        mUserAbout = (EditText) findViewById(R.id.about);

        mUSerInfoViews = new ArrayList<>();
        mUSerInfoViews.add(mUserPhone);
        mUSerInfoViews.add(mUserMail);
        mUSerInfoViews.add(mUserVk);
        mUSerInfoViews.add(mUserGit);
        mUSerInfoViews.add(mUserAbout);

    }

    private void defineClickListeners() {

        mProfilePlaceHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(ConstantManager.LOAD_PROFILE_PHOTO);
            }
        });


        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentEditMode == 0) {
                    mCurrentEditMode = 1;
                } else {
                    mCurrentEditMode = 0;
                }
                changeEditMode(mCurrentEditMode);
            }
        });

        mCallImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userPhone = mUserPhone.getText().toString();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + userPhone));
                startActivity(intent);
            }
        });
    }

    private void setupToolbar() {

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();

        mAppBarParams = (AppBarLayout.LayoutParams) mCollapsingToolbarLayout.getLayoutParams();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
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

    public void loadUserInfo() {

        insertProfileImage(mDataManger.getPreferenceManager().loadUserPhoto());

        List<String> userData = mDataManger.getPreferenceManager().loadUserProfileData();
        for (int i = 0; i < userData.size(); i++) {
            mUSerInfoViews.get(i).setText(userData.get(i));
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState()");

        outState.putInt(ConstantManager.EDIT_MODE_KEY, mCurrentEditMode);
    }

    @Override
    public void onBackPressed() {

        if (mNavigationDrawer.isDrawerOpen(GravityCompat.START)) {
            mNavigationDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void showSnackBar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    /*Получение результата из другой активити*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case ConstantManager.REQUEST_CAMERA_PICTURE:
                if (resultCode == RESULT_OK && data != null) {
                    mSelectedImage = Uri.fromFile(mPhotoFile);
                    insertProfileImage(mSelectedImage);

                }
                break;
            case ConstantManager.REQUEST_GALLERY_PICKER:
                if (resultCode == RESULT_OK && data != null) {
                    mSelectedImage = data.getData();
                    insertProfileImage(mSelectedImage);
                }
                break;
        }

    }

    /*Создание и обработка диалога*/
    @Override
    protected Dialog onCreateDialog(int id) {

        switch (id) {
            case ConstantManager.LOAD_PROFILE_PHOTO:
                String[] selectItems = {getString(R.string.dialog_load_from_gallery),
                        getString(R.string.dialog_make_photo),
                        getString(R.string.dialog_cancel)};

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.user_profile_dialog_title));
                builder.setItems(selectItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int chooseItem) {
                        switch (chooseItem) {

                            case 0:
                                loadPhotoFromGallery();
                                break;
                            case 1:
                                loadPhotoFromCamera();
                                break;
                            case 2:
                                dialog.cancel();
                                break;
                        }
                    }
                });
                return builder.create();
            default:
                return null;
        }
    }

    private void loadPhotoFromGallery() {

        Intent takeGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        takeGalleryIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(takeGalleryIntent,
                getString(R.string.user_profile_choose_massage)),
                ConstantManager.REQUEST_GALLERY_PICKER);

    }

    private void loadPhotoFromCamera() {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            Intent takeCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            try {
                mPhotoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
                //TODO: обработать ошибку
            }

            if (mPhotoFile != null) {
                //TODO передать фотографию интенту
                takeCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
                startActivityForResult(takeCaptureIntent, ConstantManager.REQUEST_CAMERA_PICTURE);
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, ConstantManager.CAMERA_REQUEST_REMISSION_CODE);
        }

        Snackbar.make(mCoordinatorLayout, "Проверить настройки", Snackbar.LENGTH_LONG).setAction("Check settings", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openApplicationSettings();
            }
        }).show();

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

                showUserPlaceHolder();
                lockToolbar();
                mCollapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
            }
        } else {
            mFab.setImageResource(R.drawable.ic_create_black_24dp);
            for (EditText userValue : mUSerInfoViews) {
                userValue.setEnabled(false);
                userValue.setFocusable(false);
                userValue.setFocusableInTouchMode(false);

                hideUserPlaceHolder();
                unLockToolbar();

                saveUserInfo();
                mCollapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.color_white));
            }
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ConstantManager.CAMERA_REQUEST_REMISSION_CODE && grantResults.length == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //TODO
            }
            if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                //TODO
            }
        }
    }

    private void hideUserPlaceHolder() {
        mProfilePlaceHolder.setVisibility(View.GONE);
    }

    private void showUserPlaceHolder() {
        mProfilePlaceHolder.setVisibility(View.VISIBLE);
    }

    private void lockToolbar() {
        mAppBarLayout.setExpanded(true, true);
        mAppBarParams.setScrollFlags(0);
        mCollapsingToolbarLayout.setLayoutParams(mAppBarParams);
    }

    private void unLockToolbar() {
        mAppBarParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
        mCollapsingToolbarLayout.setLayoutParams(mAppBarParams);
    }

    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imgFileName = "IMG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(".jpg", imgFileName, storageDir);

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.DATA, image.getAbsolutePath());

        this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        return image;
    }

    private void insertProfileImage(Uri selectedImage) {
        Picasso.with(this).load(selectedImage).into(mProfileImage);

        mDataManger.getPreferenceManager().saveUserPhoto(selectedImage);
    }

    public void openApplicationSettings() {

        Intent appSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
        startActivityForResult(appSettings, ConstantManager.PERMISSION_REQUEST_SETTINGS_CODE);
    }

}