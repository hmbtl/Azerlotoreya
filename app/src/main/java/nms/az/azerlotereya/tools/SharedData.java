package nms.az.azerlotereya.tools;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import nms.az.azerlotereya.models.User;

public class SharedData {

    private static SharedPreferences prefs;

    public static int timestamp;

    private static final String PREFS_FILENAME = "az.nms.azerlotereya";
    private static final String PREFS_FIRST_RUN = "firstrun";
    private static final String PREFS_USERNAME = "username";
    private static final String PREFS_PASSWORD = "password";
    private static final String PREFS_PINCODE = "pincode";
    private static final String PREFS_LOGGED_IN = "loggedin";
    private static final String PREFS_SET_LOGGED_USEDID = "setloggeduserid";
    private static final String PREFS_AUTH_KEY = "authenticationkey";
    private static final String PREFS_USER = "saveuser";
    private static final String PREFS_GAME = "savegame";
    private static final String PREFS_LANGUAGE = "language";
    private static final String PREFS_SHOW_INFO = "showinfo";


    private static Context context;


    public static void init(Context con) {

        if (prefs == null) {
            prefs = con.getSharedPreferences(PREFS_FILENAME, con.MODE_PRIVATE);
        }
    }

    public static boolean isFirstRun() {
        return prefs.getBoolean(SharedData.PREFS_FIRST_RUN, true);
    }


    public static void setFirstRun(boolean isFirstRun) {
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean(SharedData.PREFS_FIRST_RUN, isFirstRun);
        edit.commit();
    }

    public static void setLang(String lang){
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(SharedData.PREFS_LANGUAGE, lang);
        edit.commit();
    }

    public static String getLang(){
        return prefs.getString(PREFS_LANGUAGE, "en");
    }


    public static String getUsername() {
        return prefs.getString(PREFS_USERNAME, "");
    }

    public static void setUserName(String username) {
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(SharedData.PREFS_USERNAME, username);
        edit.apply();
    }


    public static String getPassword() {
        return prefs.getString(PREFS_PASSWORD, "");
    }

    public static void setPassword(String password) {
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(SharedData.PREFS_PASSWORD, password);
        edit.apply();
    }

    public static void setAuthKey(String authKey) {
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(SharedData.PREFS_AUTH_KEY, authKey);
        edit.apply();
    }

    public static String getAuthKey() {
        return prefs.getString(PREFS_AUTH_KEY, null);
    }


    public static String getPinCode() {
        return prefs.getString(PREFS_PINCODE, "");
    }

    public static boolean isLoggedIn() {
        return prefs.getBoolean(PREFS_LOGGED_IN, false);
    }

    public static void setLoggedIn(boolean isLoggedIn) {
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean(SharedData.PREFS_LOGGED_IN, isLoggedIn);
        edit.apply();
    }

    public static void setPinCode(String pincode) {
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(SharedData.PREFS_PINCODE, pincode);
        edit.apply();
    }

    public static void setUser(User user) {
        SharedPreferences.Editor edit = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        edit.putString(PREFS_USER, json);
        edit.apply();
    }

    public static void setBalanceWithdraw(double amount) {
        User user = SharedData.getUser();

        user.setBalanceWithdraw(amount);
        SharedData.setUser(user);

    }

    public static double getBalanceWithdraw() {
        User user = SharedData.getUser();

        return user.getBalanceWithdraw();
    }

    public static void setBalance(double amount) {
        User user = SharedData.getUser();

        user.setBalance(amount);
        SharedData.setUser(user);

    }

    public static double getBalance() {
        User user = SharedData.getUser();

        return user.getBalance();
    }

    public static String getFullName() {
        User user = SharedData.getUser();

        return user.getFullName();
    }


    public static User getUser() {
        Gson gson = new Gson();
        String json = prefs.getString(PREFS_USER, "");
        return gson.fromJson(json, User.class);
    }


    public static boolean isDontShow(){
        boolean isShowInfo = prefs.getBoolean(PREFS_SHOW_INFO, false);
        return isShowInfo;
    }

    public static void setDontShow(boolean isShowInfo){
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean(SharedData.PREFS_SHOW_INFO, isShowInfo);
        edit.apply();
    }


}
