
public class myEventBarrier implements EventBarrier{
	private boolean signaled=false;
	private int counter=0;
	private Object lock1 = new Object();
	
	@Override
	public void hold() throws InterruptedException {
		synchronized(lock1){
		counter++;
		System.out.println("A thread has registered with me, and is waiting for a signal.  I have "+waiters()+" threads registered.");
		}
		while(!signaled){
			Thread.sleep(100);
		}
		
	}

	@Override
	public void signal() throws InterruptedException {
		System.out.println("Someone turned my signal on!  I am broadcasting to everyone waiting on that signal.");
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
		System.out.println("A thread has told me it is complete, reducing counter by 1.  Counter is now "+waiters());
		counter--;
		}	
		
	}

	@Override
	public int waiters() {
		return counter;
	}

}
