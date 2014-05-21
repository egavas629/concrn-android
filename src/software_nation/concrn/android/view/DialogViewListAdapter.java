package software_nation.concrn.android.view;

import java.util.ArrayList;

import software_nation.concrn.android.shell.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DialogViewListAdapter extends BaseAdapter {
	ArrayList<Item> items = new ArrayList<Item>();
	Activity activity;

	public DialogViewListAdapter(Activity activity, ArrayList<Item> items) {
		this.items = items;
		this.activity = activity;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		Item item = items.get(position);
		return item;
	}

	@Override
	public long getItemId(int position) {
		return -1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		EntryItem item = (EntryItem) getItem(position);
		if (convertView == null) {
			view = LayoutInflater.from(activity).inflate(
					R.layout.dialog_box_list_item, null);
		} else {
			view = convertView;
		}

		TextView textView = (TextView) view.findViewById(R.id.textView);
		ImageView checkerr = (ImageView) view.findViewById(R.id.checkBox);

		textView.setText(item.title);

		if (item.hasChecked)
			checkerr.setVisibility(View.VISIBLE);
		else
			checkerr.setVisibility(View.GONE);
		return view;
	}

}
