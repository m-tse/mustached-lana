import java.util.Random;



/**
 * Used for testing the EventBarrier implementation
 * @author tS3m
 *
 */
public class testGateKeeper extends Thread{
	EventBarrier myEventBarrier;
	int myID;
	
	public testGateKeeper(EventBarrier b,int id){
		myEventBarrier=b;
		myID=id;
		
	}

	public void run(){
		System.out.println("Gatekeeper "+myID+" has started");
		while(true){
			try {
				System.out.println("waiting a few seconds");
				Random rand = new Random();
				int sleeptime = rand.nextInt(10000);
				sleep(sleeptime);
				myEventBarrier.signal();
				System.out.println("Gatekeeper is now signalling the eventbarrier.");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
