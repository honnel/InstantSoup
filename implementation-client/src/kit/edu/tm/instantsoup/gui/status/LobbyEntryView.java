package kit.edu.tm.instantsoup.gui.status;
import kit.edu.tm.instantsoup.model.ClientData;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

	/**
     * We will use a StatusEntryView to display each discovered client. 
     * It's just a LinearLayout with two text fields.
     *
     */
    public class LobbyEntryView extends LinearLayout {
    	
        private TextView nick;
        private TextView info;
    	
        public LobbyEntryView(Context context, Object item, boolean expanded) {
            super(context);
            
            ClientData cl = (ClientData) item;
            
            this.setOrientation(VERTICAL);
            
            // Here we build the child views in code. They could also have
            // been specified in an XML file.
            
            this.nick = new TextView(context);
            this.nick.setTextSize(20);
            this.nick.setText(cl.getNick());
            this.addView(this.nick, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            
            this.info = new TextView(context);
            this.info.setText(cl.toString());
            this.info.setTextSize(12);
            addView(this.info, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            
            this.info.setVisibility(expanded ? VISIBLE : GONE);
        }
        
       /**
         * Convenience method to expand or hide the info
         */
        public void setExpanded(boolean expanded) {
            this.info.setVisibility(expanded ? VISIBLE : GONE);
        }

		public void setContent(Object item) {
			ClientData cl = (ClientData) item;
			this.nick.setText(cl.getNick());
			this.info.setText(cl.toString());
		} 
    }