package com.test.bemoapplication.utils;

import android.app.Activity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

/**
 * Created by pardypanda05 on 1/8/17.
 */

public class Utils {


    public static int getRandomPosition(){

        Random r = new Random();
        int Low = 0;
        int High = 3;

        return r.nextInt(High-Low) + Low;
    }

    public static DatabaseReference initiateFireBase(Activity mActivity, String url) {
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        DatabaseReference mFirebaseDatabase;
        FirebaseDatabase mFirebaseInstance;

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference(url);

        FirebaseApp firebaseApp = FirebaseApp.initializeApp(mActivity);
        System.out.println("AppId - " + firebaseApp.getOptions().getApplicationId());
        return mFirebaseDatabase;
    }
}
