package kit.edu.tm.instantsoup.gui.status;

import java.util.LinkedList;
import java.util.List;

import kit.edu.tm.instantsoup.model.ChannelData;
import kit.edu.tm.instantsoup.model.ServerData;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ServerExpandableListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private List<ServerData> serverList;
	private List<List<ChannelData>> channelList;

	public ServerExpandableListAdapter(Context context, List<?> list) {
		this.context = context;
		this.serverList = new LinkedList<ServerData>();
		this.channelList = new LinkedList<List<ChannelData>>();

		fillDataset(list);
	}

	private void fillDataset(List<?> list) {
		for (Object object : list) {
			ServerData sd = (ServerData) object;
			serverList.add(sd);
			List<ChannelData> serverChannels = new LinkedList<ChannelData>();
			for (ChannelData cd : sd.getAllChannels()) {
				serverChannels.add(cd);
			}
			channelList.add(serverChannels);
		}
	}

	public Object getChild(int groupPosition, int childPosition) {
		return channelList.get(groupPosition).get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public int getChildrenCount(int groupPosition) {
		return channelList.get(groupPosition).size();
	}

	public TextView getGenericView() {
		// Layout parameters for the ExpandableListView
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 64);

		TextView textView = new TextView(context);
		textView.setLayoutParams(lp);
		// Center the text vertically
		textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		// Set the text starting position
		textView.setPadding(60, 0, 0, 0);
		return textView;
	}

	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {
		TextView textView = getGenericView();
		textView.setText(((ChannelData) getChild(groupPosition, childPosition)).getName());
		textView.setTextSize(12);
		return textView;
	}

	public Object getGroup(int groupPosition) {
		return serverList.get(groupPosition);
	}

	public int getGroupCount() {
		return channelList.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		TextView textView = getGenericView();
		textView.setTextSize(20);
		textView.setText(serverList.get(groupPosition).getPeerId());
		return textView;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public boolean hasStableIds() {
		return true;
	}
}
