package kit.edu.tm.instantsoup.model;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * This is the model service. It keeps on running in the background and ensures that the longterm model is available all
 * the time.
 * 
 * @author Florian Schreier
 */
public class ModelService extends Service {

	/** The instance of long term model. */
	private Model model;

	@Override
	public void onCreate() {
		this.model = Model.getLongtermModel();
		this.model.setServiceContext(this);
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				Model.getLongtermModel().startDiscoveryManager();
				//Model.getLongtermModel().fillModelWidthDummyData(); // TODO Only for testing
			}
		}).start();

		this.model.startStatusBarNotificationManager();
	}

	/**
	 * This service does not provide binding.
	 */
	public IBinder onBind(Intent intent) {
		return null;
	}

}
