package Part1;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class EventSimulator {

	public static void main(String[] args) throws IOException, InterruptedException {
		if (args.length == 0) {
			System.out.println("ERROR: No input file provided!");
			return;
		} else if (args.length > 1) {
			System.out.println("More than one file provided, only looking at first file.");
		}

		FileInputStream file = new FileInputStream(args[0]);
		DataInputStream data = new DataInputStream(file);
		BufferedReader reader = new BufferedReader(new InputStreamReader(data));
		
		String line; // First line format: Floors, Elevators, Riders, Threads, Capacity
		int threads;
		MyEventBarrier barrier = new MyEventBarrier();
		TestThread thread;
		TestGateKeeper gatekeeper;
		int index = 0;
		while ((line = reader.readLine()) != null) {
			threads = Integer.parseInt(line);
			System.out.println(threads);
			for (int i = 0; i < threads; ++i) {
				TestThread t = new TestThread(barrier, i);
				t.start();
			}
			Thread.sleep(100);
			gatekeeper = new TestGateKeeper(barrier, index);
			gatekeeper.watch();
			++index;
			System.out.println("WATCHING");	
		}
			
		
		System.out.println(Thread.activeCount());
		barrier.closeLog();
		return;
		
		
	}

	
	
}
