package Part1;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
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
		
		System.out.println("");
		System.out.println("Running simulation...");
		
		String line; // First line format: Floors, Elevators, Riders, Threads, Capacity
		int threads;
		MyEventBarrier barrier = new MyEventBarrier();
		TestGateKeeper gatekeeper;
		int index = 0;
		while ((line = reader.readLine()) != null) {
			threads = Integer.parseInt(line);
			for (int i = 0; i < threads; ++i) {
				TestThread t = new TestThread(barrier, i);
				t.start();
			}
			Thread.sleep(100);
			gatekeeper = new TestGateKeeper(barrier, index);
			gatekeeper.watch();
			++index;
		}
		
		reader.close();
		data.close();
		file.close();
		barrier.closeLog();
		
		System.out.println("");
		System.out.println("-------------------");
		System.out.println("Simulation finished");
		System.out.println("Examine Event.log");
		System.out.println("-------------------");
		System.out.println("");
		return;
		
		
	}

	
	
}
