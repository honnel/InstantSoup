package kit.edu.tm.instantsoup.gui.status;

import java.util.List;

import kit.edu.tm.instantsoup.model.ClientData;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public class LobbyListAdapter extends StatusListAdapter {
	
	private List<ClientData> mClientList;

	public LobbyListAdapter(Context context, List<ClientData> list) {
		super(context, list.size());
		this.mClientList = list;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LobbyEntryView entryView;
		if (convertView == null) {
			entryView = new LobbyEntryView(super.getContext(), mClientList.get(position), super.getExpanded(position));
		} else {
			entryView = (LobbyEntryView) convertView;
			entryView.setContent(mClientList.get(position));
			entryView.setExpanded(super.getExpanded(position));
		}
		return entryView;
	}

	@Override
	public Object getItem(int position) {
		return mClientList.get(position);
	}


}