package software_nation.concrn.android.view;

import software_nation.concrn.android.model.Constants;
import software_nation.concrn.android.shell.HelperActivity;
import software_nation.concrn.android.shell.R;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class DialogView extends DialogFragment {
	HelperActivity helper;

	public DialogView() {
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Dialog dialogView = new Dialog(getActivity());
		dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogView.setContentView(R.layout.dialog_box);

		helper = new HelperActivity(getActivity());
		Button buttonOk = (Button) dialogView.findViewById(R.id.bt_ok);
		buttonOk.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				Constants.isPositionAllowed = true;
				dialogView.cancel();
			}
		});

		Button buttonDontAllow = (Button) dialogView
				.findViewById(R.id.bt_negative);
		buttonDontAllow.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				Constants.isPositionAllowed = false;
				dialogView.cancel();
			}
		});

		dialogView.show();

		return dialogView;
	}
}
