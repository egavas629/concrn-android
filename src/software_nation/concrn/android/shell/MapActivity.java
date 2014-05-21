package software_nation.concrn.android.shell;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import software_nation.concrn.android.model.Constants;
import software_nation.concrn.android.view.ShowCallDialogView;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class MapActivity extends FragmentActivity {
	private GoogleMap mMap;
	TextView address;
	Marker marker;
	double currentLongitude = 0;
	double currentLatitude = 0;
	LatLng position = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_view);
		address = (TextView) findViewById(R.id.text_street_name);
		setUpMapIfNeeded();
	}

	private void setUpMapIfNeeded() {
		//checking if map is loaded
		if (mMap == null) {
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			if (mMap != null) {
				setUpMapSettings(mMap);
				if (Constants.isPositionAllowed) {
					mMap.setMyLocationEnabled(true);
					mMap.setOnMyLocationChangeListener(new OnMyLocationChangeListener() {

						@Override
						public void onMyLocationChange(Location arg0) {
							currentLongitude = arg0.getLongitude();
							currentLatitude = arg0.getLatitude();
						}
					});

				} else
					mMap.setMyLocationEnabled(false);

				mMap.setOnCameraChangeListener(new OnCameraChangeListener() {
					public void onCameraChange(CameraPosition arg0) {
						position = arg0.target;
						if (position != null
								&& position.latitude != 0
								&& position.longitude != 0) {
							String add = getAddressByLongitudeAndLatitude(
									position.longitude,
									position.latitude);
							if (add != null)
								address.setText(add);
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
		mUiSettings.setRotateGesturesEnabled(true);
	}

	//getting address of the marker position 
	private String getAddressByLongitudeAndLatitude(double longitude,
			double latitude) {
		Geocoder geocoder;
		String result = null;
		List<Address> addresses;
		geocoder = new Geocoder(this, Locale.getDefault());
		try {
			addresses = geocoder.getFromLocation(latitude, longitude, 1);
			if(addresses!=null && addresses.size()>0)
				result = addresses.get(0).getAddressLine(0);
		} catch (IOException e) {
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
		new HelperActivity(MapActivity.this).startActivity(GetReportAtributesActivity.class);
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

}
