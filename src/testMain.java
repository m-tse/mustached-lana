import java.util.Random;



/**
 * Used for testing the EventBarrier Implementation
 * @author tS3m
 *
 */
public class testMain {
	public static void main(String[] args) throws InterruptedException{
		EventBarrier anEventBarrier = new myEventBarrier();
		testGateKeeper gatekeep = new testGateKeeper(anEventBarrier, 0);
		gatekeep.start();
		int counter = 0;
		while(true){
			testThread t = new testThread(anEventBarrier, counter);
			counter++;
			t.start();
			Random rand = new Random();
			int sleeptime = rand.nextInt(2000);
			Thread.currentThread().sleep(sleeptime);
		}
	}
}
