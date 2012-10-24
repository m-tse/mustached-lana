
public class myEventBarrier implements EventBarrier{
	private boolean signaled=false;
	private int counter=0;
	private Object lock1 = new Object();
	
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

	@Override
	public void complete() {
		synchronized(lock1){
		System.out.printf("Thread completed.  %d waiters\n", waiters());
		counter--;
		}	
		
	}

	@Override
	public int waiters() {
		return counter;
	}

}
