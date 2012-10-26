package Part1;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;


/**
 * Used for testing the EventBarrier implementation
 * @author tS3m
 *
 */
public class TestGateKeeper {
	MyEventBarrier myEventBarrier;
	int myID;

	public TestGateKeeper(MyEventBarrier b, int id) throws IOException{
		myEventBarrier=b;
		myID=id;

	}


	public void watch() {
		try {
			myEventBarrier.signal();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return;
	}
}
