package software_nation.concrn.android.view;

import java.util.ArrayList;

import software_nation.concrn.android.model.Constants;
import software_nation.concrn.android.shell.GetReportAtributesActivity;
import software_nation.concrn.android.shell.HelperActivity;
import software_nation.concrn.android.shell.R;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ShowSublistItemsDialogView extends DialogFragment {
	HelperActivity helper;
	ArrayList<Item> items = new ArrayList<Item>();
	Activity activity;
	String group;

	public ShowSublistItemsDialogView() {
		// TODO Auto-generated constructor stub
	}

	public ShowSublistItemsDialogView(Activity activity, ArrayList<Item> array,
			String group) {
		this.activity = activity;
		this.items = array;
		this.group = group;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Dialog dialogView = new Dialog(getActivity());
		dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogView.setContentView(R.layout.dialog_box_list_checkerr);

		final ListView list = (ListView) dialogView
				.findViewById(R.id.list_dialog);
		final DialogViewListAdapter adapter = new DialogViewListAdapter(
				activity, items);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				EntryItem item = (EntryItem) items.get(arg2);
				if (group.equals("Gender"))
					Constants.report.gender = item.title;
				if (group.equals("Age"))
					Constants.report.age = item.title;
				if (group.equals("Race"))
					Constants.report.race = item.title;
				if (group.equals("Settings"))
					Constants.report.setting = item.title;
				if (group.equals("Urgency")) {
					Constants.report.urgency = item.title;
				}

				item.hasChecked = !item.hasChecked;
				for (int i = 0; i < items.size(); i++) {
					EntryItem it = (EntryItem) items.get(i);
					if (it != item)
						it.hasChecked = false;
				}
				((GetReportAtributesActivity) activity).notifyDataChanged();
				dialogView.cancel();
			}
		});

		dialogView.show();

		return dialogView;
	}

}
