package software_nation.concrn.android.view;

import software_nation.concrn.android.shell.HelperActivity;
import software_nation.concrn.android.shell.R;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class ShowCallDialogView extends DialogFragment {
	HelperActivity helper;

	public ShowCallDialogView() {
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Dialog dialogView = new Dialog(getActivity());
		dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogView.setContentView(R.layout.call_dialog_box);

		helper = new HelperActivity(getActivity());

		Button buttonOk = (Button) dialogView.findViewById(R.id.bt_ok);
		buttonOk.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				helper.makeCall("911");
			}
		});

		Button cancel = (Button) dialogView.findViewById(R.id.bt_negative);
		cancel.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialogView.cancel();
			}
		});

		dialogView.show();

		return dialogView;
	}
}
