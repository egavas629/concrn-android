package software_nation.concrn.android.shell;

import software_nation.concrn.android.model.User;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;

public class MemoryManager {
	public static final String TAG = "MemoryManager";
	private static final String FILE_NAME = "Concern";

	public MemoryManager(Activity activity) {
		activity.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
	}
	
	public Object LoadPreferences(Context context, String key,  Class<? extends Object> classOfObject){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String json = prefs.getString(key, "");
		Gson gson = new Gson();
		return gson.fromJson(json, classOfObject);
	}

	public void SaveArrayPreferences(Context context, String key, User user){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = prefs.edit();
		String json;
		Gson gson = new Gson();
		json = gson.toJson(user);
		editor.putString(key, json);
		editor.commit();
		Log.v(TAG, "Object stored in SharedPreferences");
	}

}
