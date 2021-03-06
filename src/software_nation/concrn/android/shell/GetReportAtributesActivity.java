package software_nation.concrn.android.shell;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import org.json.JSONArray;
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class GetReportAtributesActivity extends FragmentActivity {
	private static final int CAMERA_REQUEST = 1888; 

	ListView list;
	EntryAdapter adapter;
	ArrayList<Item> items = new ArrayList<Item>();
	ArrayList<Item> genderItems = new ArrayList<Item>();
	ArrayList<Item> ageItems = new ArrayList<Item>();
	ArrayList<Item> raceItems = new ArrayList<Item>();
	ArrayList<Item> settingsItems = new ArrayList<Item>();
	ArrayList<Item> urgencyItems = new ArrayList<Item>();
	EditText natueEditText;
	Bitmap photo = null;
	File image = null;
	int index,top;
	Bitmap bitmap;
	String base69Data;
	Button updateButton;
	byte[] data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_view);
		/*custom list implemented so the GUI of list looks just like on iOS; 
		 * list uses custom classes EntryItem (for the items) and Section Item (for the headings)
		 */
		Constants.report.urgency = "1 - Not urgent";
		setUpSections();
		setUpGenderItems();
		setUpAgeItems();
		setUpRaceItems();
		setUpSettingsItems();
		setUpUrgencyItems();
		updateButton = (Button) findViewById(R.id.update_report_button);
		updateButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.e("Tagging", "Received click");
				new UploadReportTask().execute("");
			}
		});
		list = (ListView) findViewById(R.id.listView_main);
		View footerView=  ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.reporting_footer, null, false);
		list.addFooterView(footerView);
		natueEditText = (EditText) footerView.findViewById(R.id.report_footer_edit_text);
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
						|| item.title.equalsIgnoreCase("Threatening")) {
					
				}

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
				if(item.title.equalsIgnoreCase("Setting") && item.hasNext){

					showListSubItemsDialog(GetReportAtributesActivity.this, settingsItems, "Settings");
					//Constants.report.observations = Constants.report.observations + " "+item.title;
				}
				if(item.title.equalsIgnoreCase("Urgency") && item.hasNext){
					showListSubItemsDialog(GetReportAtributesActivity.this, urgencyItems, "Urgency");
				}
//				if(item.title.equalsIgnoreCase("Incident Picture")){
//
//					startCameraActivity();
//				}

				storePosition();
				updateItem(item);
			}

		});
	}
	
	private class UploadReportTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			return UploadReport();
		}
		
		@Override
		protected void onPostExecute (String result){
			
			if(result!=null){
				
				if (Constants.report.agency.equalsIgnoreCase("Concrn Team")) {
					Toast.makeText(getApplicationContext(), "Thank you for reporting your concrn. Each report pulls Concrn closer to active service in your community. Tell your friends!", Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(getApplicationContext(), "Report successfully updated. "+ Constants.report.agency + " will respond as soon as possible.", Toast.LENGTH_LONG).show();
				}
				
				String name = Constants.report.name;
				String phone = Constants.report.phone;
				Constants.report = new Report();
				Constants.report.name = name;
				Constants.report.phone = phone;
				finish();				
			} else {
				Toast.makeText(getApplicationContext(), "There was an issue updating your report", Toast.LENGTH_LONG).show();
			}
		}
		
	}

	private void setUpSections() {
		items.add(new SectionItem("Report Information"));
		items.add(new EntryItem("Urgency", true, false, "Report Information"));
		items.add(new SectionItem("Description"));
		items.add(new EntryItem("Gender", true, false, "Patient Description"));
		items.add(new EntryItem("Age Group", true, false, "Patient Description"));
		items.add(new EntryItem("Race/Ethniticity", true, false, "Patient Description"));
		
		items.add(new EntryItem("Setting", true, false, "Patient Description"));

		items.add(new SectionItem("The person is..."));
		items.add(new EntryItem("At risk of harm", false, false,"Crisses Observation"));
		items.add(new EntryItem("Under the influence", false, false, "Crisses Observation"));
		items.add(new EntryItem("Anxious", false, false, "Crisses Observation"));
		items.add(new EntryItem("Depressed", false, false, "Crisses Observation"));
		items.add(new EntryItem("Aggravated", false, false, "Crisses Observation"));
		items.add(new EntryItem("Threatening", false, false, "Crisses Observation"));

		items.add(new SectionItem(""));
		//items.add(new EntryItem("Incident Picture", true, false, "Additionals"));		
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
	
	public void setUpUrgencyItems() {
		urgencyItems.add(new EntryItem("1 - Not urgent", false, false, "Urgency"));
		urgencyItems.add(new EntryItem("2 - This week", false, false, "Urgency"));
		urgencyItems.add(new EntryItem("3 - Today", false, false, "Urgency"));
		urgencyItems.add(new EntryItem("4 - Within an hour", false, false, "Urgency"));
		urgencyItems.add(new EntryItem("5 - Need help now", false, false, "Urgency"));
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

	public String UploadReport() {

		HelperActivity helper = new HelperActivity(GetReportAtributesActivity.this);

		/*alternative upload address*/
		//		String url = "http://rangers.concrn.com/reports";

		/*stores the lat long phone address name */
		String url = Constants.BASE_URL + "/reports";
		int reportID = Constants.report.id;
		
		
		return helper.POST(url+"/"+reportID+"/upload", getExtendedReport(), image);
		
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

			for (int i = 8; i < 14; i++) {
				EntryItem item = (EntryItem) items.get(i);
				if(item.hasChecked) {
					if (Constants.report.observations.isEmpty()) {
						Constants.report.observations = item.title;
					} else {
						Constants.report.observations = Constants.report.observations + ", "+item.title;
					}
				}
			}			

			JSONArray jsonArray = new JSONArray();
			jsonArray.put(Constants.report.observations);
			attr.put("observations", jsonArray);		
			attr.put("race", Constants.report.race);
			attr.put("setting", Constants.report.setting);
			if (Constants.report.urgency != null && Constants.report.urgency.length()>0) {
				String urgency = Constants.report.urgency.substring(0, 1);
				attr.put("urgency", urgency);
			}
			if (natueEditText.getText().toString() != null) {
				attr.put("nature", natueEditText.getText().toString());
			}
		

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

