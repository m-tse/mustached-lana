package Part1;

import java.util.Random;




/**
 * Used for testing the EventBarrier Implementation
 * @author tS3m
 *
 */
public class TestMain {
	public static void main(String[] args) throws InterruptedException{
		EventBarrier anEventBarrier = new MyEventBarrier();
		TestGateKeeper gatekeep = new TestGateKeeper(anEventBarrier, 0);
		gatekeep.start();
		int counter = 0;
		while(true){
			TestThread t = new TestThread(anEventBarrier, counter);
			counter++;
			t.start();
			Random rand = new Random();
			int sleeptime = rand.nextInt(2000);
			Thread.currentThread().sleep(sleeptime);
		}
	}
}