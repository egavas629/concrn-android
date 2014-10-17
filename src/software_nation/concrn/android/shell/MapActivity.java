package software_nation.concrn.android.shell;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import software_nation.concrn.android.model.Constants;
import software_nation.concrn.android.view.ShowCallDialogView;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.VisibleRegion;


public class MapActivity extends FragmentActivity 
{
	private GoogleMap mMap;
	TextView address;
	double currentLongitude = 0;
	double currentLatitude = 0;
	LatLng position = null;
	LocationManager locManager;
	String provider;
	Location location;
	VisibleRegion visibleRegion;
	Point centerPoint;
	Boolean hasUpdatedPosition = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_view);
		address = (TextView) findViewById(R.id.text_street_name);

		setUpMapIfNeeded();
	}

	private void setUpMapIfNeeded() {
		//checking if map is loaded
		if (mMap == null) 
		{

			mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
			if (mMap != null) {
				setUpMapSettings(mMap);
				if (Constants.isPositionAllowed) {
					mMap.setMyLocationEnabled(true);
					mMap.setOnMyLocationChangeListener(new OnMyLocationChangeListener() {

						@Override
						public void onMyLocationChange(Location arg0) {

							
							currentLongitude = arg0.getLongitude();
							currentLatitude = arg0.getLatitude();

							
							if(!hasUpdatedPosition) {
								mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLatitude, currentLongitude), 18.0f));
								hasUpdatedPosition = true;
							}
							

						}
					});

				} else

					mMap.setMyLocationEnabled(false);
				    mMap.setOnCameraChangeListener(new OnCameraChangeListener() {
					public void onCameraChange(CameraPosition arg0) {
						position = arg0.target;
						if (position != null&& position.latitude != 0&& position.longitude != 0) 
						{
							address.setText(getResources().getString(R.string.updating_Location));
							getCentrePoint();
						}
					}
				});
			}
		}
	}

	private void setUpMapSettings(GoogleMap mMap2) {
		UiSettings mUiSettings;
		mUiSettings = mMap2.getUiSettings();
		mUiSettings.setZoomControlsEnabled(false);
		mUiSettings.setCompassEnabled(true);
		mUiSettings.setMyLocationButtonEnabled(true);
		mUiSettings.setScrollGesturesEnabled(true);
		mUiSettings.setZoomGesturesEnabled(true);
		mUiSettings.setTiltGesturesEnabled(true);
		mUiSettings.setRotateGesturesEnabled(false);


	}

	//getting address of the marker position 
	private String getAddressByLongitudeAndLatitude(double longitude,double latitude) {
		Geocoder geocoder;
		String result = null;
		List<Address> addresses = null;
		geocoder = new Geocoder(this, Locale.getDefault());
		try {
			
			addresses = geocoder.getFromLocation(latitude, longitude, 1);
			
			if(addresses!=null && addresses.size()>0)
				result = addresses.get(0).getAddressLine(0);

		} catch (IOException e) {
			result = "Address unavailable";
			e.printStackTrace();
		}
		return result;
	}

	//getting values and go to next screen	
	public void ReportCrises(View v) {
		if(position!=null){
			Constants.report.latitude =position.latitude;
			Constants.report.longitude =position.longitude;
			Constants.report.address = getAddressByLongitudeAndLatitude(position.longitude, position.latitude);
		}

		new CreateReportTask().execute("Trying");
	}
	
	class CreateReportTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... arg0) {
			HelperActivity helper = new HelperActivity(MapActivity.this);
			String url = Constants.BASE_URL + "/reports";
			String report = getReport().toString();
			String result = helper.POST(url, report, null);
			return result;
		}
		
		protected void onPostExecute(String result) {
			JSONObject jObj;
			
			try {
				jObj = new JSONObject(result);
				Constants.report.id = 0;
				if(jObj.getString("id") != null && !jObj.getString("id").equalsIgnoreCase("null")) {
					Constants.report.id = Integer.parseInt(jObj.getString("id"));
				} 
				Log.e("Tag", "In it");
				if (Constants.report.id > 0) {
					new HelperActivity(MapActivity.this).startActivity(GetReportAtributesActivity.class);
				} else {
					Toast.makeText(getApplicationContext(), "There was an error uploading your report", Toast.LENGTH_LONG).show();
				}
			}catch (JSONException e) {
				Toast.makeText(getApplicationContext(), "There was an error uploading your report", Toast.LENGTH_LONG).show();
				e.printStackTrace();
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

	public void Call911(View v) {
		showCallDialog();
	}

	private void showCallDialog() {
		FragmentManager fragmentManager = getSupportFragmentManager();
		ShowCallDialogView callDialog = new ShowCallDialogView();
		callDialog.setCancelable(false);
		callDialog.show(fragmentManager, "Activity");
	}

	/*
	 * Function sets the center point of map 
	 */

	private void getCentrePoint(){

		visibleRegion = mMap.getProjection() .getVisibleRegion();

		Point x = mMap.getProjection().toScreenLocation(visibleRegion.farRight);

		Point y = mMap.getProjection().toScreenLocation(visibleRegion.nearLeft);

		centerPoint = new Point(x.x / 2, y.y / 2);

		LatLng centerFromPoint = mMap.getProjection().fromScreenLocation( centerPoint);

		String add = getAddressByLongitudeAndLatitude(centerFromPoint.longitude, centerFromPoint.latitude);
		if (add != null)
			address.setText(add);

	}

}
