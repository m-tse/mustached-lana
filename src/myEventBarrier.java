/**
 * Our implementation of the EventBarrier Interface.  Believed to be working.
 * @author tS3m
 *
 */
public class myEventBarrier implements EventBarrier{
	private boolean signaled=false;
	private int counter=0;
	private Object lock1 = new Object();
	private boolean debug = true;
	
	/**
	 * A waiter thread calls this method to register to the EventBarrier, 
	 * it waits for the EventBarrier to change to a signalled state.
	 */
	@Override
	public void hold() throws InterruptedException {
		System.out.printf("Current thread: %s\n", Thread.currentThread().getName());
		synchronized(lock1){
			counter++;
			this.myPrint("Thread registered.  %d waiters\n", waiters());
		}
		while(!signaled){
			Thread.currentThread().sleep(1000);
		}
	}

	/**
	 * A gatekeeper thread calls this method to signal the EventBarrier into signaled state.  
	 * All waiters become alerted.  
	 */
	@Override
	public void signal() throws InterruptedException {
		this.myPrint("Signalling all waiters\n");
		signaled=true;
		while(counter>0){
			Thread.currentThread().sleep(100);
		}
		signaled=false;
		this.myPrint("All threads are complete, I am now unsignaled\n");
		return;
	}
	
	/**
	 * A waiter thread calls this method after it has completed, telling the EventBarrier it has finished its stuff.
	 */
	@Override
	public void complete() {
		synchronized(lock1){
			this.myPrint("Thread completed.  %d waiters\n", waiters());
			counter--;
			if (counter == 0) {
				this.signaled = false;
			}
		}	
	}
	
	/**
	 * Returns the number of waiters.
	 */
	@Override
	public int waiters() {
		return counter;
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
