package software_nation.concrn.android.shell;

import software_nation.concrn.android.model.Constants;
import software_nation.concrn.android.model.User;
import software_nation.concrn.android.view.DialogView;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
	HelperActivity helper;
	MemoryManager memory;
	EditText name, phone;
	User user = new User();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		memory = new MemoryManager(MainActivity.this);
		helper = new HelperActivity(MainActivity.this);

		//getting user from phone memory if exists
		user = (User) memory.LoadPreferences(MainActivity.this, "LOGIN", User.class);
		name = (EditText) findViewById(R.id.edit_name);
		phone = (EditText)findViewById(R.id.edit_phone);
		if(user!=null){
		//	name.setText(user.name);
		//	phone.setText(user.phone);
			Constants.report.name = user.name;
			Constants.report.phone = user.phone;
			helper.startActivity(MapActivity.class);
			finish();
		}
		setUpDialog();
	}

	private void setUpDialog() {
		FragmentManager fragmentManager = getSupportFragmentManager();
		DialogView loginDialog = new DialogView();
		loginDialog.setCancelable(false);
		loginDialog.show(fragmentManager, "Activity");
	}


	public void Continue(View v){
		if (PhoneNumberUtils.isGlobalPhoneNumber(phone.getText().toString())) {
			Constants.report.name = name.getText().toString();
			Constants.report.phone = phone.getText().toString();
			User user = new User(Constants.report.name, Constants.report.phone);
			memory.SaveArrayPreferences(MainActivity.this, "LOGIN", user);
			helper.startActivity(MapActivity.class);
			finish();
		} else  {
			Toast.makeText(getApplicationContext(), "Phone number invalid", Toast.LENGTH_LONG).show();
		}
	}
}
