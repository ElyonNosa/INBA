package comp3350.inba.objects;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * This class holds information pertaining to the user
 **/
public class User {

    private final SharedPreferences prefs;

    public User(Context cntx) {
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void setLoginStatus(boolean status)
    {
        prefs.edit().putBoolean("loginstatus", status).apply();
    }

    public boolean getLoginStatus()
    {
        return prefs.getBoolean("loginstatus", false);
    }

    public void setUserid(String userid)
    {
        prefs.edit().putString("userid", userid).apply();
    }

    public String getUserid()
    {
        return prefs.getString("userid", "");
    }

    private void setPassword(String password)
    {
        prefs.edit().putString("password", password).apply();
    }

    public void setPasswd(String pw) {
        prefs.edit().putString("password", pw).apply();
    }

    public String getPasswd() {
        return prefs.getString("password", "");
    }

    public String getUserID() {
        return prefs.getString("userid", "");
    }

    public String getUserName() {
        return prefs.getString("username", "");
    }

    public double getWkdayThresh() {
        return prefs.getFloat("weekdaythreshold", 0);
    }

    public double getWkendThresh() {
        return prefs.getFloat("weekendthreshold", 0);
    }

    public void setUserName(String userName) {
        prefs.edit().putString("username", userName).apply();
    }

    public void setWkdayThresh(double wkdayThresh) {
        prefs.edit().putFloat("weekdaythreshold", 0).apply();
    }

    public void setWkendThresh(double wkendThresh) {
        prefs.edit().putFloat("weekendthreshold", 0).apply();
    }
}
