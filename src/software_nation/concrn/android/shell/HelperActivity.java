package software_nation.concrn.android.shell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

import com.google.gson.Gson;

public class HelperActivity {
	public static final String TAG = "HelperActivity";

	protected Activity mainActivity;

	public HelperActivity(Activity activity) {
		mainActivity = activity;
	}

	public void startActivity(Class<?> activityClass) {
		Log.d(TAG, "Starting activity: " + activityClass.getName());
		Intent intent = new Intent(mainActivity, activityClass);
		mainActivity.startActivity(intent);
	}

	public void startActivityWithFlag(Class<?> activityClass, int intentFlag) {
		Log.d(TAG, "Starting activity: " + activityClass.getName()
				+ " with flag " + intentFlag);
		Intent intent = new Intent(this.mainActivity, activityClass);
		intent.addFlags(intentFlag);
		this.mainActivity.startActivity(intent);
	}

	public void startActivityWithParam(Class<?> activityClass, String key,
			Serializable value) {
		Log.d(TAG, "Starting activity: " + activityClass.getName()
				+ " with param: " + key + "=" + value);
		Intent intent = new Intent(this.mainActivity, activityClass);
		intent.putExtra(key, value);
		this.mainActivity.startActivity(intent);
	}

	public void makeCall(String phoneNumber) {
		Log.d(TAG, "Call made: " + phoneNumber);
		phoneNumber = "tel:" + phoneNumber.trim();
		Intent callIntent = new Intent(Intent.ACTION_CALL,
				Uri.parse(phoneNumber));
		this.mainActivity.startActivity(callIntent);
	}

	public void turnGPSOn() {
		Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
		intent.putExtra("enabled", true);
		this.mainActivity.sendBroadcast(intent);

		String provider = Settings.Secure.getString(
				mainActivity.getContentResolver(),
				Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		if (!provider.contains("gps")) { // if gps is disabled
			final Intent poke = new Intent();
			poke.setClassName("com.android.settings",
					"com.android.settings.widget.SettingsAppWidgetProvider");
			poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
			poke.setData(Uri.parse("3"));
			this.mainActivity.sendBroadcast(poke);

		}
	}

	// automatic turn off the gps
	public void turnGPSOff() {
		String provider = Settings.Secure.getString(
				mainActivity.getContentResolver(),
				Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		if (provider.contains("gps")) { // if gps is enabled
			final Intent poke = new Intent();
			poke.setClassName("com.android.settings",
					"com.android.settings.widget.SettingsAppWidgetProvider");
			poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
			poke.setData(Uri.parse("3"));
			this.mainActivity.sendBroadcast(poke);
		}
	}

	public String parseToJSONString(Object obj) {
		Gson gson = new Gson();
		return gson.toJson(obj);
	}

	/*public String POST(String url, String jsonObject, File imageFile) {
		InputStream inputStream = null;
		String result = "";
		try {

			HttpClient httpclient = new DefaultHttpClient();

			HttpPost httpPost = new HttpPost(url);
			// if(imageFile!=null){
			// MultipartEntity entity = new MultipartEntity();
			// ContentBody cbFile = new FileBody(imageFile, "image/jpeg");
			// entity.addPart("userfile", cbFile);
			// httpPost.setEntity(entity);
			// }else{
			StringEntity se = new StringEntity(jsonObject);
			httpPost.setEntity(se);
			// }

			httpPost.setHeader("Accept", "text/javascript");
			httpPost.setHeader("Content-type", "application/json");

			HttpResponse httpResponse = httpclient.execute(httpPost);
			inputStream = httpResponse.getEntity().getContent();
			if (inputStream != null) {
				result = convertInputStreamToString(inputStream);
				Log.v("Radi: ", "radi\n" + result);
			} else
				result = "Did not work!";

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}*/

	public String POST(String url, String jsonObject, byte[]  data) {
		InputStream inputStream = null;
		String result = "";
		try {

			HttpClient httpclient = new DefaultHttpClient();

			HttpPost httpPost = new HttpPost(url);
			//	FormBodyPart jsonBodyPart = new FormBodyPart("json", jsonBody);
			if(data!=null){
				MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
				/*ContentBody cbFile = new FileBody(imageFile, "image/jpeg");
				entity.addPart("image", cbFile);

				httpPost.setEntity(entity);*/

				ByteArrayBody imageByteArrayBody = new ByteArrayBody(data, "image/png", "avatar.png");
		        FormBodyPart imageFormBodyPart = new FormBodyPart("image", imageByteArrayBody);
		        imageFormBodyPart.addField("Content-Disposition", "form-data; name=\"aPhoto\"");
		        imageFormBodyPart.addField("name", "aPhoto");
		        imageFormBodyPart.addField("image_file_name", "avatar.png");
		        imageFormBodyPart.addField("image_content_type", "image/jpeg");
		        entity.addPart(imageFormBodyPart);
		        httpPost.setEntity(entity);
			}

			//else{
			/***********************************************************************/


			/*	   FileBody bin = new FileBody(file);  


			   Charset chars = Charset.forName("UTF-8");

			   MultipartEntity reqEntity = new MultipartEntity();  
			   reqEntity.addPart("problem[photos_attributes][0][image]", bin);  
			   reqEntity.addPart("problem[category_id]", new StringBody("17", chars));

        			   post.setEntity(reqEntity); 
			   HttpResponse response = client.execute(post);  

			   HttpEntity resEntity = response.getEntity();  
			   if (resEntity != null) {    
			     resEntity.consumeContent();  
			  }

			   return true;


				MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		    for (NameValuePair nameValuePair : nameValuePairs) {
		        if (nameValuePair.getName().equalsIgnoreCase("picture")) {
		                File imgFile = new File(nameValuePair.getValue());
		                FileBody fileBody = new FileBody(imgFile, "image/jpeg");
		                multipartEntity.addPart("post[picture]", fileBody);
		        } else {
		                multipartEntity.addPart("post[" + nameValuePair.getName() + "]", new StringBody(nameValuePair.getValue()));
		        }                   
		    }
		httpPost.setEntity(multipartEntity);
		HttpResponse response = httpClient.execute(httpPost, httpContext);*/
			/*************************************************************************/

			StringEntity se = new StringEntity(jsonObject);
			httpPost.setEntity(se);
			// }

			httpPost.setHeader("Accept", "text/javascript");
			httpPost.setHeader("Content-type", "application/json");

			HttpResponse httpResponse = httpclient.execute(httpPost);
			inputStream = httpResponse.getEntity().getContent();
			if (inputStream != null) {
				result = convertInputStreamToString(inputStream);
				Log.v("Radi: ", "radi\n" + result);                               
			} else
				result = "Did not work!";                                     

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}





	public static String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;
	}




}
