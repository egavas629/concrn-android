package software_nation.concrn.android.shell;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import software_nation.concrn.android.model.Constants;
import software_nation.concrn.android.model.Report;
import software_nation.concrn.android.view.EntryAdapter;
import software_nation.concrn.android.view.EntryItem;
import software_nation.concrn.android.view.Item;
import software_nation.concrn.android.view.SectionItem;
import software_nation.concrn.android.view.ShowCallDialogView;
import software_nation.concrn.android.view.ShowSublistItemsDialogView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class GetReportAtributesActivity extends FragmentActivity {
	private static final int CAMERA_REQUEST = 1888; 

	ListView list;
	EntryAdapter adapter;
	ArrayList<Item> items = new ArrayList<Item>();
	ArrayList<Item> genderItems = new ArrayList<Item>();
	ArrayList<Item> ageItems = new ArrayList<Item>();
	ArrayList<Item> raceItems = new ArrayList<Item>();
	ArrayList<Item> settingsItems = new ArrayList<Item>();
	Bitmap photo = null;
	File image = null;
	int index,top;
	Bitmap bitmap;
	String base69Data;
	byte[] data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_view);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
		.detectAll().penaltyLog().penaltyDeath().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll()
				.penaltyLog().penaltyDeath().build());
		/*custom list implemented so the GUI of list looks just like on iOS; 
		 * list uses custom classes EntryItem (for the items) and Section Item (for the headings)
		 */
		setUpSections();
		setUpGenderItems();
		setUpAgeItems();
		setUpRaceItems();
		setUpSettingsItems();


		list = (ListView) findViewById(R.id.listView_main);
		adapter = new EntryAdapter(GetReportAtributesActivity.this, items);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				// TODO Auto-generated method stub

				EntryItem item = (EntryItem) items.get(arg2);

				if (item.title.equalsIgnoreCase("At risk of harm")
						|| item.title.equalsIgnoreCase("Under the influence")
						|| item.title.equalsIgnoreCase("Anxious")
						|| item.title.equalsIgnoreCase("Depressed")
						|| item.title.equalsIgnoreCase("Aggravated")
						|| item.title.equalsIgnoreCase("Threatening"))

					//Constants.report.observations = Constants.report.observations + " "+item.title;
					//indexesList.add(arg2);


					if(item.title.equalsIgnoreCase("Gender") && item.hasNext){

						showListSubItemsDialog(GetReportAtributesActivity.this, genderItems, "Gender");
						//Constants.report.gender = Constants.report.observations + " "+item.title;
					}

				if(item.title.equalsIgnoreCase("Age Group") && item.hasNext){

					showListSubItemsDialog(GetReportAtributesActivity.this, ageItems, "Age");
					//Constants.report.observations = Constants.report.observations + " "+item.title;
				}
				if(item.title.equalsIgnoreCase("Race/Ethniticity") && item.hasNext){

					showListSubItemsDialog(GetReportAtributesActivity.this, raceItems, "Race");
					//Constants.report.observations = Constants.report.observations + " "+item.title;
				}
				if(item.title.equalsIgnoreCase("Crisis Setting") && item.hasNext){

					showListSubItemsDialog(GetReportAtributesActivity.this, settingsItems, "Settings");
					//Constants.report.observations = Constants.report.observations + " "+item.title;
				}
				if(item.title.equalsIgnoreCase("Incident Picture")){

					startCameraActivity();
				}

				storePosition();
				updateItem(item);
			}

		});
	}

	private void setUpSections() {
		items.add(new SectionItem("Patient Description"));
		items.add(new EntryItem("Gender", true, false, "Patient Description"));
		items.add(new EntryItem("Age Group", true, false, "Patient Description"));
		items.add(new EntryItem("Race/Ethniticity", true, false, "Patient Description"));
		items.add(new EntryItem("Crisis Setting", true, false, "Patient Description"));

		items.add(new SectionItem("Crises Observations: Is the patient..."));
		items.add(new EntryItem("At risk of harm", false, false,"Crisses Observation"));
		items.add(new EntryItem("Under the influence", false, false, "Crisses Observation"));
		items.add(new EntryItem("Anxious", false, false, "Crisses Observation"));
		items.add(new EntryItem("Depressed", false, false, "Crisses Observation"));
		items.add(new EntryItem("Aggravated", false, false, "Crisses Observation"));
		items.add(new EntryItem("Threatening", false, false, "Crisses Observation"));

		items.add(new SectionItem("Additional Description..."));
		items.add(new EntryItem("Incident Picture", true, false, "Additionals"));		
	}

	/*method for getting image using Camera*/
	protected void startCameraActivity() {
		Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
		startActivityForResult(cameraIntent, CAMERA_REQUEST); 
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
		if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {  
			photo = (Bitmap) data.getExtras().get("data"); 
			Uri selectedImageUri1 = data.getData();
			String path = getRealPathFromURI(GetReportAtributesActivity.this, selectedImageUri1);
			System.out.println("selected image path is : "+path);

			image = new File(path);
			/****************************/
			System.out.println("path of file is : "+image.getPath()+"  file size is : "+image.length());
	
		}  
	} 

	/*method for geting an image path stored on device*/
	public String getRealPathFromURI(Context context, Uri contentUri) {
		Cursor cursor = null;
		try { 
			String[] proj = { MediaStore.Images.Media.DATA };
			cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		}finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}	


	public void setUpSettingsItems() {
		settingsItems.add(new EntryItem("Public Space", false, false, "Settings"));
		settingsItems.add(new EntryItem("Workplace", false, false, "Settings"));
		settingsItems.add(new EntryItem("School", false, false, "Settings"));
		settingsItems.add(new EntryItem("Home", false, false, "Settings"));
		settingsItems.add(new EntryItem("Other", false, false, "Settings"));		
	}

	public void setUpRaceItems() {
		raceItems.add(new EntryItem("Hispanic or Latino", false, false, "Race"));
		raceItems.add(new EntryItem("American Indian or Alaska Native", false, false, "Race"));
		raceItems.add(new EntryItem("Black or African American", false, false, "Race"));
		raceItems.add(new EntryItem("Native Hawaiian or Pacific Islander", false, false, "Race"));
		raceItems.add(new EntryItem("White", false, false, "Race"));
		raceItems.add(new EntryItem("Other/Unknown", false, false, "Race"));		
	}

	public void setUpAgeItems() {
		ageItems.add(new EntryItem("Youth (0-17)", false, false, "Age"));
		ageItems.add(new EntryItem("Young Adult (18-34)", false, false, "Age"));
		ageItems.add(new EntryItem("Adult (35-64)", false, false, "Age"));
		ageItems.add(new EntryItem("Senior (65+)", false, false, "Age"));		
	}

	public void setUpGenderItems() {
		genderItems.add(new EntryItem("Male", false, false, "Gender"));
		genderItems.add(new EntryItem("Female", false, false, "Gender"));
		genderItems.add(new EntryItem("Other", false, false, "Gender"));		
	}

	private void showListSubItemsDialog(Activity activity, ArrayList<Item> array, String group) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		ShowSublistItemsDialogView subItems = new ShowSublistItemsDialogView(activity, array, group);
		subItems.show(fragmentManager, "Activity");
	}

	protected void updateItem(EntryItem item) {
		if (item.hasChecked)
			item.hasChecked = false;
		else
			item.hasChecked = true;
		list.setAdapter(adapter);
		/*restores the  index of ListView before updating the adapter*/
		list.setSelectionFromTop(index, top);
	}

	public void notifyDataChanged(){
		list.setAdapter(adapter);
		list.setSelectionFromTop(index, top);
	}

	public void Call911(View v) {
		showCallDialog();
	}

	public void UploadReport(View v) {

		HelperActivity helper = new HelperActivity(GetReportAtributesActivity.this);

		/*alternative upload address*/
		//		String url = "http://rangers.concrn.com/reports";

		/*stores the lat long phone address name */
		String url = "http://www.concrn.com/reports";
		

		int reportID = 0;

		String result = helper.POST(url, getReport().toString(), null);
		JSONObject jObj;
		try {
			jObj = new JSONObject(result);
			reportID = Integer.parseInt(jObj.getString("id"));
		}catch (JSONException e) {
			e.printStackTrace();
		}


		if(reportID>0){
			String result2 = helper.POST(url+"/"+reportID+"/upload", getExtendedReport().toString(), data);
			Log.v("Result of basic info is :", result2);
			System.out.println("the result of basic info is : "+result2); 
			System.out.println("the response is : "+result2);
			if(result2!=null){
				String name = Constants.report.name;
				String phone = Constants.report.phone;
				Constants.report = new Report();
				Constants.report.name = name;
				Constants.report.phone = phone;
				finish();				
			}
		}
	}

	private JSONObject getReport() {
		JSONObject request = new JSONObject();
		JSONObject attr = new JSONObject();
		try {
			attr.put("address", Constants.report.address);
			attr.put("lat", Constants.report.latitude);
			attr.put("long", Constants.report.longitude);
			attr.put("name", Constants.report.name);
			attr.put("phone", Constants.report.phone);

			request.put("report", attr);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		Log.v("Result using JSON:", request.toString());

		return request;
	}

	private JSONObject getExtendedReport() {
		JSONObject request = new JSONObject();
		JSONObject attr = new JSONObject();
		try {
			/*if(base69Data.length()>0){
				attr.put("image", base69Data);
			}*/
			attr.put("age", Constants.report.age);
			attr.put("gender", Constants.report.gender);
			attr.put("nature", Constants.report.nature);						

			for (int i = 6; i < 12; i++) {
				EntryItem item = (EntryItem) items.get(i);
				if(item.hasChecked)
					Constants.report.observations = Constants.report.observations + " "+item.title;
			}			

			attr.put("observations",Constants.report.observations);		
			attr.put("race", Constants.report.race);
			attr.put("setting", Constants.report.setting);
		
		

			request.put("report", attr);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		Log.v("Result using JSON:", request.toString());
		System.out.println("the response is : "+request.toString());
		return request;
	}
	private void showCallDialog() {
		FragmentManager fragmentManager = getSupportFragmentManager();
		ShowCallDialogView callDialog = new ShowCallDialogView();
		callDialog.setCancelable(false);
		callDialog.show(fragmentManager, "Activity");
	}
	/*
	 * method stores the index of ListView first visible position    
	 * 
	 * */

	private void storePosition() {
		// TODO Auto-generated method stub
		index = list.getFirstVisiblePosition();
		View v = list.getChildAt(0);
		top = (v == null) ? 0 : v.getTop();
	}


	public void decodeFile(String filePath) {
		// Decode image size
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, o);

		// The new size we want to scale to
		final int REQUIRED_SIZE = 1024;

		// Find the correct scale value. It should be the power of 2.
		int width_tmp = o.outWidth, height_tmp = o.outHeight;
		int scale = 1;
		while (true) {
			
			if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
				break;
			width_tmp /= 2;
			height_tmp /= 2;
			scale *= 2;
		}

		// Decode with inSampleSize
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		bitmap = BitmapFactory.decodeFile(filePath, o2);
		//base69Data=  getBase64StringData();

	}

	/*
	 * 
	 * Starting Camera 
	 * 
	 * */
	/*	public void startCamera() 
	{        
	Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE );
	    intent.putExtra( MediaStore.EXTRA_OUTPUT, outputFileUri );
	    startActivityForResult( intent, 0 );
	}*/
	/*method to convert bitmap into a Base64 String*/
	public void  getBase64StringData(){
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, 100, bos);
		data = bos.toByteArray();

		String file = Base64.encodeToString(data,Base64.DEFAULT);
		System.out.println("the base 64  file is : "+file);
		//return data.toString();
	
	}


}

