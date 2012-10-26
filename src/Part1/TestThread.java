package Part1;



/**
 * Used for testing the EventBarrier implementation
 * @author tS3m
 *
 */
public class TestThread extends Thread{
	EventBarrier myEventBarrier;
	int myID;
	
	public TestThread(EventBarrier b,int id){
		myEventBarrier=b;
		myID=id;
		
	}
	
	public void run(){
		System.out.println("Thread "+myID+" has started");
		try {
			myEventBarrier.hold();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			myEventBarrier.complete();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
