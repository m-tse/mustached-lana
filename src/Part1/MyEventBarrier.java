package Part1;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Our implementation of the EventBarrier Interface.  Believed to be working.
 * @author tS3m
 *
 */
public class MyEventBarrier implements EventBarrier{
	private boolean signaled=false;
	private int counter=0;
	private int intendedWaiters = 0;
	private boolean debug = false;
	
	
	private FileWriter logFile;
	private PrintWriter logger; 
	
	public MyEventBarrier() throws IOException {
		this.logFile = new FileWriter("Event.log");
		this.logger = new PrintWriter(logFile);
	}
	
	public void prehold() throws InterruptedException {
		while (this.intendedWaiters > this.waiters()) {
			Thread.sleep(100);
		}
	}
	
	/**
	 * A waiter thread calls this method to register to the EventBarrier, 
	 * it waits for the EventBarrier to change to a signalled state.
	 */
	@Override
	public synchronized void hold() throws InterruptedException {
		counter++;
		this.log("T%d holding W%d\n", Thread.currentThread().getId(), this.waiters());
		this.myPrint("Thread registered.  %d waiters\n", waiters());
		while(!signaled){
			this.wait();
		}
	}

	/**
	 * A gatekeeper thread calls this method to signal the EventBarrier into signaled state.  
	 * All waiters become alerted.  
	 */
	@Override
	public synchronized void signal() throws InterruptedException {
		this.myPrint("Signalling all waiters\n");
		signaled=true;
		this.notifyAll();
		this.log("G%s signal ON W%d\n", Thread.currentThread().getId(), this.waiters());
		while(counter>0) {
			this.wait();
		}
		signaled=false;
		this.log("G%s signal OFF W%d\n", Thread.currentThread().getId(), this.waiters());
		this.myPrint("All threads are complete, I T%d am now unsignaled\n", Thread.currentThread().getId());
		return;
	}
	
	/**
	 * A waiter thread calls this method after it has completed, telling the EventBarrier it has finished its stuff.
	 * @throws InterruptedException 
	 */
	@Override
	public synchronized void complete() throws InterruptedException {
		counter--;
		this.myPrint("Thread completed now.  %d waiters\n", waiters());
		this.notifyAll();
		this.log("T%d completed W%d\n", Thread.currentThread().getId(), this.waiters());
		while (counter > 0) {
			this.wait();
		}
	}
	
	/**
	 * Returns the number of waiters.
	 */
	@Override
	public int waiters() {
		return counter;
	}
	
	
	public synchronized void log(String format, Object... args) {
		this.logger.printf(format, args);
	}
	
	public void closeLog() throws IOException {
		this.logger.close();
		this.logFile.close();
	}	
	
	public void incrementIntended() {
//		if (this.intendedWaiters == -1) {
//			this.intendedWaiters++;
//		}
		this.intendedWaiters++;
	}
	
	public void decrementIntended() {
		this.intendedWaiters--;
	}
	
	public int getIntendedWaiters() {
		return this.intendedWaiters;
	}
	
	
	
	/**
	 * Printf utility method
	 */
	public void myPrint(String format, Object... args) {
		if (this.debug) {
			System.out.printf(format, args);
		}
	}
	
	/**
	 * Turn ON mode
	 */
	public void debugOn() {
		this.debug = true;
	}
	
	/**
	 * Turn OFF debug
	 */
	public void debugOff() {
		this.debug = false;
	}
}
