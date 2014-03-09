package kit.edu.tm.instantserver.functionality;

public class ClientArray extends Thread{
	public static void main(String args[]) {
		  ClientArray t = new ClientArray();
		  t.start();
	   }
	
	public void run(){
		Client c = new Client();
		  c.start();
		  try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  Client d = new Client();
		  d.start();
	}
}
