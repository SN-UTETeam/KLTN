package pjm.tlcn.Common;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import pjm.tlcn.Model.User;

public class SessionUser {
    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;
    private Context mContext;
    private Gson mGson;

    private static final String PREF_NAME = "SessionUser";
    private static final String USER = "SessionUser";

    public SessionUser(Context context) {
        this.mContext = context;
        mPref = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        mEditor = mPref.edit();
        mGson = new Gson();
    }

    public void createSessionUser(User user) {
        String userJSON = mGson.toJson(user);
        mEditor.putString(USER, userJSON);
        mEditor.commit();
    }

    public void clearUserStorage() {
        mEditor.clear();
        mEditor.commit();
    }

    public User getSessionUser() {
        return mGson.fromJson(mPref.getString(USER, null), User.class);
    }
}
