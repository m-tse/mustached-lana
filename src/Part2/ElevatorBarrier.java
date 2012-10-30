package Part2;

import java.io.IOException;

import Part1.MyEventBarrier;

public class ElevatorBarrier extends MyEventBarrier {

	private int enterers;
	
	
	public ElevatorBarrier() throws IOException {
		super();
		this.enterers = 0;
	}
	
	
	
	public void waitForEnterers() throws InterruptedException {
		while (enterers == 0) {
			this.wait();
		}
		while (enterers > 0) {
			this.wait();
		}
	}
	
	
	public synchronized void enter() {
		this.enterers += 1;
		this.notify();
	}

}
