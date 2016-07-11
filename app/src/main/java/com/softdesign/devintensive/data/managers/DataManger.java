package com.softdesign.devintensive.data.managers;


public class DataManger {

    private static DataManger INSTANCE = null;
    private PreferenceManager mPreferenceManager;

    public DataManger(){
        this.mPreferenceManager = new PreferenceManager();
    }

    public static DataManger getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DataManger();
        }
        return INSTANCE;
    }

    public PreferenceManager getPreferenceManager(){
        return mPreferenceManager;
    }
}