package kit.edu.tm.instantsoup.gui.status;


import java.util.LinkedList;
import java.util.List;

import kit.edu.tm.instantsoup.model.ChannelData;
import kit.edu.tm.instantsoup.model.ServerData;
import android.content.Context;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ServerListEntryView extends LinearLayout {
	
	private TextView name;
	private TextView info;

	public ServerListEntryView(Context context, Object item, boolean expanded) {
		super(context);
        
        ServerData sd = (ServerData) item;     
        
        this.setOrientation(VERTICAL);
        
        // Here we build the child views in code. They could also have
        // been specified in an XML file.
        
        this.name = new TextView(context);
        this.name.setTextSize(20);
        this.name.setText(sd.getPeerId());
        this.addView(this.name, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        
        this.info = new TextView(context);
        
        this.info.setTextSize(12);
        addView(this.info, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        
        this.info.setVisibility(expanded ? VISIBLE : GONE);
	}

	public void setContent(Object item) {
		ServerData sd = (ServerData) item;
		this.name.setText(sd.getPeerId());
		List<String> channelList = new LinkedList<String>();
        for (ChannelData channel : sd.getAllChannels()) {
			channelList.add(channel.getName());
		}        
        this.info.setText(TextUtils.join(", ", channelList.toArray()));	
	}

	public void setExpanded(boolean expanded) {
		this.info.setVisibility(expanded ? VISIBLE : GONE);
		
	}

}
