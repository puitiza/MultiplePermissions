package pe.anthony.multiplepermissions;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ANTHONY on 4/01/2018.
 */

public class FirstRunDetected {
    SharedPreferences preferences;   // Shared Preferences variable
    SharedPreferences.Editor editor;   //editor for shared preference
    private String PREFERENCES ="FB_RunFirst";  //Nombre del archivo del preferences
    private  boolean firstRun;

    public FirstRunDetected(Context context) {
        preferences = context.getSharedPreferences(PREFERENCES,Context.MODE_PRIVATE);
        editor = preferences.edit();
        firstRun = preferences.getBoolean("firstRun", true);//Aca dice por defecto esta true
    }

    public boolean loadFirstRun(){
        return firstRun;
    }

    public void saveFirstRun(){
        editor.putBoolean("firstRun",false);
        editor.commit();
    }
}
