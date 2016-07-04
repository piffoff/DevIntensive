package com.softdesign.devintensive.data.managers;


import android.content.SharedPreferences;
import android.net.Uri;

import com.softdesign.devintensive.utils.ConstantMenedger;
import com.softdesign.devintensive.utils.DevintensiveApplication;

import java.util.ArrayList;
import java.util.List;

public class PreferenceManager {

    private SharedPreferences mSharedPreferences;

    private static final String[] USER_FIELDS = {ConstantMenedger.USER_PHONE_KEY, ConstantMenedger.USER_EMAIL_KEY, ConstantMenedger.USER_VK_KEY, ConstantMenedger.USER_GIT_KEY, ConstantMenedger.USER_ABOUT_KEY};

    public PreferenceManager(){
        this.mSharedPreferences = DevintensiveApplication.getSharedPreferences();

    }

    public void saveUserProfileData(List<String> userFields){
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        for (int i = 0; i < USER_FIELDS.length; i++) {
            editor.putString(USER_FIELDS[i], userFields.get(i));
        }
        editor.apply();
    }

    public List<String> laadUserProfileData(){

        List<String> userFields = new ArrayList<>();
        userFields.add(mSharedPreferences.getString(ConstantMenedger.USER_PHONE_KEY, "null"));
        userFields.add(mSharedPreferences.getString(ConstantMenedger.USER_EMAIL_KEY, "null"));
        userFields.add(mSharedPreferences.getString(ConstantMenedger.USER_VK_KEY, "null"));
        userFields.add(mSharedPreferences.getString(ConstantMenedger.USER_GIT_KEY, "null"));
        userFields.add(mSharedPreferences.getString(ConstantMenedger.USER_ABOUT_KEY, "null"));

        return userFields;
    }

    public void saveUserFhoto(Uri uri){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantMenedger.USER_PHOTO_KEY, uri.toString());
        editor.apply();
    }

    public Uri loadUserPhoto(){
        return Uri.parse(mSharedPreferences.getString(ConstantMenedger.USER_PHOTO_KEY, "android.resourse://com.softdesign.devintensive/drawable/user_photo.jpg"));
    }
}
