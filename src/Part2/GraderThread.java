package Part2;

import java.io.BufferedReader;
import java.io.IOException;

public class GraderThread extends Thread {

	private Object lock;
	private int myId;
	private BufferedReader myInput;
	
	public GraderThread(int riderId, BufferedReader input) {
		lock = new Object();
		this.myId = riderId;
		this.myInput = input;
	}
	
	
	
	public void run() {
		String line;
		synchronized(lock) {
			try {
				line = this.myInput.readLine();
				if (line == null) {
					return;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	
}
