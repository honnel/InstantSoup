package kit.edu.tm.instantserver.functionality;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;

import kit.edu.tm.instantsoup.Constants;

class Client extends Thread {
   

private PrintWriter out;
private Socket skt;
   
   public void run(){
	   String data = "JOIN" + Constants.TERMINATION + "Test12";
	      try {
	    	
			InetAddress groupv4 = InetAddress.getByName(Constants.IP4);
			MulticastSocket socket = new MulticastSocket(Constants.PORT);
			socket.joinGroup(groupv4);
			String message = (int)(Math.random()*1000) + "\0";
			byte[] packet = message.getBytes();
			DatagramPacket discovery = new DatagramPacket(packet, packet.length, groupv4, Constants.PORT);
			socket.send(discovery);
			System.out.println("Sending PDU...");
	  		
	    	  
			skt = new Socket("192.168.1.53", Constants.LISTENING_PORT);

	        out = new PrintWriter(skt.getOutputStream(), true);
	        System.out.println("Sending string: '" + data + "");
	        out.println(data);
	      }
	      catch(Exception e) {
	    	 
	         System.out.println("Lost connection! :( \n");
	      }
	      try {
	    	Thread.sleep((int)Math.random() * 2000);
	    	out = new PrintWriter(skt.getOutputStream(), true);
	    	String msg = "SAY" + Constants.TERMINATION + "Hallo, ein Test!";
	    	out.println(msg);
	    	System.out.println(msg);
	    	BufferedReader in = new BufferedReader(new InputStreamReader(skt.getInputStream()));
	    	while(!in.ready()){
	    	}
	    	String[] s = in.readLine().split("\0");
	    	System.out.println(s[0] + " " +s[1] + " " + s[2]);
			Thread.sleep((int)Math.random() * 9000);
			out = new PrintWriter(skt.getOutputStream(), true);
			out.println("EXIT");
			System.out.println("EXIT");
			out.close();
	        skt.close();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   }
}