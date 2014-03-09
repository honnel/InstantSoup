package kit.edu.tm.instantsoup.gui.status;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class StatusListAdapter extends BaseAdapter {	
	
	/**
     * Remember our context so we can use it when constructing views.
     */
	private Context context;
	private boolean[] expanded; //TODO Doesn't update when model change. Should be stored and updated by model?!
		
	public StatusListAdapter(Context context, Integer ListSize) {
		this.context = context;
		this.expanded = new boolean[ListSize];
		for (int i = 0; i < this.expanded.length; i++) {
			this.expanded[i] = false;
		}
	}
		
	protected Context getContext() {
		return context;
	}

	/**
     * The number of items in the list is determined by the number of peers
     * in the lobby.
     * 
     * @see android.widget.ListAdapter#getCount()
     */
	public int getCount() {
		return expanded.length;
	}

    /**
     * @see android.widget.ListAdapter#getItem(int)
     */
	public abstract Object getItem(int position);

	/**
     * Use the peerId as a unique id getLeastSignificantBits.
     * @see android.widget.ListAdapter#getItemId(int)
     */
	public abstract long getItemId(int position);

	/**
     * Make a lineaLayout for each element to hold each row.
     * @see android.widget.ListAdapter#getView(int, android.view.View, android.view.ViewGroup)
     */
	public abstract View getView(int position, View convertView, ViewGroup parent);
	
	public void toggle(int position) {
		expanded[position] = !expanded[position];
        notifyDataSetChanged();
	}
	
	protected boolean getExpanded(int position) {
		return expanded[position];
	} 	
}