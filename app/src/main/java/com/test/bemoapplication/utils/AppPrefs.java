package com.test.bemoapplication.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by pardypanda05 on 12/10/16.
 */
public class AppPrefs {

    private static final String USER_PREFS = "PERSANG_PREFS";
    private SharedPreferences appSharedPrefs;
    private SharedPreferences.Editor prefsEditor;

    private String isMobileNumberVerified = "isMobileNumberVerified";
    private String userID = "userID";
    private String userName = "userName";

    private String navigate_selected_product_id = "navigate_selected_product_id";

    public AppPrefs(Context context) {
        // TODO Auto-generated constructor stub
        this.appSharedPrefs = context.getSharedPreferences(USER_PREFS, Activity.MODE_PRIVATE);
        this.prefsEditor = appSharedPrefs.edit();
    }

    public void setUserID(String id) {
        // TODO Auto-generated method stub
        prefsEditor.putString(userID, id).commit();
    }

    public String getUserID() {
        // TODO Auto-generated method stub
        return appSharedPrefs.getString(userID, "");
    }

    public void setUserName(String avatar) {
        // TODO Auto-generated method stub
        prefsEditor.putString(userName, avatar).commit();
    }

    public String getUserName() {
        // TODO Auto-generated method stub
        return appSharedPrefs.getString(userName, "");
    }
}
