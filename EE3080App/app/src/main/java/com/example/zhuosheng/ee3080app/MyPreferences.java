package com.example.zhuosheng.ee3080app;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 4/4/2017.
 */

public class MyPreferences {
    private static final String PICTURE_HISTORY = "ref";
    //Saving SharedPreferences
    public static void saveSharedPreferencesLogList(Context context,List<TakenPicture> takenPicturesLog) {
        SharedPreferences mPrefs = context.getSharedPreferences(PICTURE_HISTORY, 0);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        String json = new Gson().toJson(takenPicturesLog);
        prefsEditor.putString("myJson", json);
        prefsEditor.commit();
    }
    //Load SharedPreferences
    public static List<TakenPicture> loadSharedPreferencesLogList(Context context) {
        List<TakenPicture> callLog;
        SharedPreferences mPrefs = context.getSharedPreferences(PICTURE_HISTORY, 0);
        Gson gson = new Gson();
        String json = mPrefs.getString("myJson", "");
        if (json.isEmpty()) {
            callLog = new ArrayList<TakenPicture>();
        } else {
            Type type = new TypeToken<List<TakenPicture>>() {
            }.getType();
            callLog = gson.fromJson(json, type);
        }
        return callLog;
    }
}
