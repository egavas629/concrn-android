package software_nation.concrn.android.view;

import java.util.ArrayList;

import software_nation.concrn.android.model.Constants;
import software_nation.concrn.android.shell.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class EntryAdapter extends ArrayAdapter<Item> {
	private ArrayList<Item> items;
	private LayoutInflater vi;
	ViewHolder holder = new ViewHolder();

	public EntryAdapter(Context context, ArrayList<Item> items) {
		super(context, 0, items);
		this.items = items;
		vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		final Item i = items.get(position);
		if (i != null) {
			if (i.isSection()) {
				SectionItem si = (SectionItem) i;
				v = vi.inflate(R.layout.list_item_section, null);

				v.setOnClickListener(null);
				v.setOnLongClickListener(null);
				v.setLongClickable(false);

				final TextView sectionView = (TextView) v
						.findViewById(R.id.list_item_section_text);
				sectionView.setText(si.getTitle());

			} else {
				EntryItem ei = (EntryItem) i;
				v = vi.inflate(R.layout.list_item, null);
				holder.checkerr = (ImageView) v.findViewById(R.id.checkBox);
				holder.title = (TextView) v.findViewById(R.id.textView);
				holder.next = (ImageView) v.findViewById(R.id.image_next);
				holder.choosed = (TextView)v.findViewById(R.id.textChoosed);
				if (holder.title != null)
					holder.title.setText(ei.title);
				
				if(ei.title.equals("Gender") && Constants.report.gender!=null)
					holder.choosed.setText(Constants.report.gender);
				if(ei.title.equals("Age Group") && Constants.report.age!=null)
					holder.choosed.setText(Constants.report.age);
				if(ei.title.equals("Race/Ethniticity") && Constants.report.race!=null)
					holder.choosed.setText(Constants.report.race);
				if(ei.title.equals("Crisis Setting") && Constants.report.setting!=null)
					holder.choosed.setText(Constants.report.setting);
				if(ei.title.equals("Urgency") && Constants.report.urgency!=null)
					holder.choosed.setText(Constants.report.urgency);
				if(ei.hasNext){
					holder.next.setVisibility(View.VISIBLE);
					setCheckerr(holder.checkerr, ei);
				}else{ 
					holder.next.setVisibility(View.GONE);
					setCheckerr(holder.checkerr, ei);
				}
			}
		}
		return v;
	}

	private void setCheckerr(ImageView checkerr, EntryItem ei) {
		if(ei.sectionName.equalsIgnoreCase("Crisses Observation")){
			if(ei.hasChecked)
				checkerr.setVisibility(View.VISIBLE);
			else
				checkerr.setVisibility(View.GONE);
		}

	}

	private class ViewHolder {
		TextView choosed;
		TextView title;
		ImageView next;
		ImageView checkerr;
	}
}
