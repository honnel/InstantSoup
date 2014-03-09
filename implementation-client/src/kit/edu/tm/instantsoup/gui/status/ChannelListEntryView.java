package kit.edu.tm.instantsoup.gui.status;

import java.util.LinkedList;
import java.util.List;

import kit.edu.tm.instantsoup.R;
import kit.edu.tm.instantsoup.model.ChannelData;
import kit.edu.tm.instantsoup.model.ClientData;
import kit.edu.tm.instantsoup.model.Model;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChannelListEntryView extends LinearLayout {

	private ImageButton btnJoin;
	private TextView name;
	private TextView info;
	private ChannelData channelData;
	private ImageButton btnLeave;

	public ChannelListEntryView(ViewGroup parent, final Context context, Object item, boolean expanded) {
		super(context);

		this.channelData = (ChannelData) item;

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.channel_list_entry, parent, false);
		view.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				info.setVisibility(info.getVisibility() == VISIBLE ? GONE : VISIBLE);
			}
		});

		this.btnJoin = (ImageButton) view.findViewById(R.id.button_join);
		this.btnLeave = (ImageButton) view.findViewById(R.id.button_leave);
		if (GuiUtils.isChannelJoined(channelData.getName(), channelData.getServerId(), channelData.isPrivate())) {
			btnLeave.setVisibility(VISIBLE);
		}
		this.name = (TextView) view.findViewById(R.id.channelListEntryTitle);
		this.info = (TextView) view.findViewById(R.id.channelListEntryInfo);

		this.name.setTextSize(20);

		this.btnJoin.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Model.getLongtermModel().joinChannel(channelData);
				GuiUtils.joinChannel(context, channelData.getName(), channelData.getServerId(), channelData.isPrivate());				
				btnLeave.setVisibility(VISIBLE);
			}
		});

		this.btnLeave.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				GuiUtils.leaveChannel(context, channelData);
				btnLeave.setVisibility(GONE);
			}
		});

		this.info.setTextSize(12);
		this.info.setVisibility(expanded ? VISIBLE : GONE);

		setContent(item);
		this.addView(view);
	}


	
	public void setContent(Object item) {
		ChannelData cd = (ChannelData) item;
		this.name.setText(cd.getName());	
		String content = ""; 
		if (cd.getMembers().size() > 0) {
			content = String.format("Server: %s\nMembers: %s", cd.getServer().getPeerId(), getChannelMembers(cd));
		} else {
			content = String.format("Server: %s", cd.getServer().getPeerId());
		}
		this.info.setText(content);
	}
	
	private String getChannelMembers(ChannelData cd) {
		List<String> channelMembers = new LinkedList<String>();
		for (ClientData client : cd.getMembers()) {
			channelMembers.add(client.getNick());
		}	
		return TextUtils.join(", ", channelMembers.toArray());
	}
	

	public void setExpanded(boolean expanded) {
		this.info.setVisibility(expanded ? VISIBLE : GONE);
	}

}
