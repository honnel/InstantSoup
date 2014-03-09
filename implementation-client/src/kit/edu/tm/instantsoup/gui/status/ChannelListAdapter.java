package kit.edu.tm.instantsoup.gui.status;

import java.util.List;

import kit.edu.tm.instantsoup.model.ChannelData;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public class ChannelListAdapter extends StatusListAdapter {

	private List<ChannelData> mChannelList;
	

	public ChannelListAdapter(Context context, List<ChannelData> list) {
		super(context, list.size());
		this.mChannelList = list;
	}

	/**
	 * Uses parameter postion as ItemId. Channels has no unique identifer.
	 * 
	 * @param position
	 * @return position
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ChannelListEntryView entryView;
		ChannelData cd = mChannelList.get(position);
		if (convertView == null) {
			entryView = new ChannelListEntryView(parent, super.getContext(), cd, super.getExpanded(position));
		} else {
			entryView = (ChannelListEntryView) convertView;
			entryView.setContent(cd);
			entryView.setExpanded(super.getExpanded(position));
		}
		return entryView;
	}

	@Override
	public Object getItem(int position) {
		return mChannelList.get(position);
	}
}
