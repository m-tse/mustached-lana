/**
 * Our implementation of the EventBarrier Interface.  Believed to be working.
 * @author tS3m
 *
 */
public class myEventBarrier implements EventBarrier{
	private boolean signaled=false;
	private int counter=0;
	private Object lock1 = new Object();
	
	/**
	 * A waiter thread calls this method to register to the EventBarrier, it waits for the EventBarrier to change to a signalled state.
	 */
	@Override
	public void hold() throws InterruptedException {
		synchronized(lock1){
		counter++;
		System.out.printf("Thread registered.  %d waiters\n", waiters());
		}
		while(!signaled){
			Thread.sleep(100);
		}
		
	}

	/**
	 * A gatekeeper thread calls this method to signal the EventBarrier into signaled state.  All waiters become alerted.  
	 */
	@Override
	public void signal() throws InterruptedException {
		System.out.println("Signalling all waiters");
		signaled=true;
		while(counter>0){
			Thread.sleep(100);
		}
		signaled=false;
		System.out.println("All threads are complete, I am now unsignaled");
		return;
		// TODO Auto-generated method stub
		
	}
	/**
	 * A waiter thread calls this method after it has completed, telling the EventBarrier it has finished its stuff.
	 */
	@Override
	public void complete() {
		synchronized(lock1){
		System.out.printf("Thread completed.  %d waiters\n", waiters());
		counter--;
		}	
		
	}
	/**
	 * Returns the number of waiters.
	 */
	@Override
	public int waiters() {
		return counter;
	}

}
