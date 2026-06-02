package main.com.cineramamaps.Session;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import main.com.cineramamaps.model.UserDetails;


public class SessionManager {
    private final SharedPreferences prf;
    private final SharedPreferences.Editor edit;
    Context context;

    public static SessionManager get(Context context) {
        return new SessionManager(context);
    }
    public SessionManager(Context context) {
        this.context = context;
        prf=context.getSharedPreferences(SessionKey.DB_FindMyBuddy.name(), Context.MODE_PRIVATE);
        edit=prf.edit();
    }

    public void CreateSession(UserDetails details){
        Gson gson = new Gson();
        String result = gson.toJson(details);
        edit.putBoolean(SessionKey.IsUserLogedIn.name(),true);
        edit.putString(SessionKey.UserProfile.name(),result);
        edit.commit();
    }
    public void CreateSession(){
        edit.putBoolean(SessionKey.IsUserLogedIn.name(),true);
        edit.commit();
    }
    public UserDetails getUserDetails(){
        Gson gson = new Gson();
        return gson.fromJson(prf.getString(SessionKey.UserProfile.name(),""),UserDetails.class);
    }
    public String getUserID(){
        if (getUserDetails()==null){
            return "";
        }
       else if (getUserDetails().getId()==null){
            return "";
        }
        else {
            return getUserDetails().getId();
        }

    }

    public boolean isUserLogin(){
        return prf.getBoolean(SessionKey.IsUserLogedIn.name(),false);
    }
    public void Logout(){
        edit.clear();
        edit.commit();
    }

    public void setLanguage(String lng){
        SharedPreferences lng_prf = context.getSharedPreferences(SessionKey.DB_FindMyBuddy_Lng.name(), Context.MODE_PRIVATE);
        SharedPreferences.Editor lng_edit = lng_prf.edit();
        lng_edit.putString(SessionKey.language.name(),lng);
        lng_edit.apply();

        lng_edit.commit();
    }

    public String getSelectedLanguage(){
        SharedPreferences lng_prf = context.getSharedPreferences(SessionKey.DB_FindMyBuddy_Lng.name(), Context.MODE_PRIVATE);
        return lng_prf.getString(SessionKey.language.name(), "en");
    }
    public boolean IsEnglish(){
        SharedPreferences lng_prf = context.getSharedPreferences(SessionKey.DB_FindMyBuddy_Lng.name(), Context.MODE_PRIVATE);
         String lang = lng_prf.getString(SessionKey.language.name(), "en");

        if (lang.equalsIgnoreCase("en")){
            return true;
        }
        else {
            return false;
        }
    }
    public boolean IsEnglish1(){
       /* SharedPreferences lng_prf = context.getSharedPreferences(SessionKey.DB_FindMyBuddy_Lng.name(), Context.MODE_PRIVATE);
      //  String dd = lng_prf.getString(SessionKey.language.name(), "en").equals("en");

        if (SessionKey.language.name().equalsIgnoreCase("en")){
            return true;
        }
        else {
            return false;
        }*/

        SharedPreferences lng_prf = context.getSharedPreferences(SessionKey.DB_FindMyBuddy_Lng.name(), Context.MODE_PRIVATE);
        String lang = lng_prf.getString(SessionKey.language.name(), "en");

        if (lang.equalsIgnoreCase("en")){
            return true;
        }
        else {
            return false;
        }

    }

}

