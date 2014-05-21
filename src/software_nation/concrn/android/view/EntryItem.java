package software_nation.concrn.android.view;

public class EntryItem implements Item{
	public final String title;
	public final boolean hasNext;
	public boolean hasChecked;
	public final String sectionName;
	
	public EntryItem(String title, boolean hasNext, boolean hasChecked, String sectionName) {
		this.title = title;
		this.hasNext = hasNext;
		this.hasChecked = hasChecked;
		this.sectionName = sectionName;
	}


	public boolean isHasChecked() {
		return hasChecked;
	}


	public void setHasChecked(boolean hasChecked) {
		this.hasChecked = hasChecked;
	}


	public boolean isHasNext() {
		return hasNext;
	}


	@Override
	public boolean isSection() {
		return false;
	}
}
