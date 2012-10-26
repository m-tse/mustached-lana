package Part2;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ElevatorSimulator {

	public static int[] parseInformation(String[] strInfo) {
		int[] info = new int[strInfo.length];
		for (int i = 0; i < info.length; ++i) {
			// Assuming well formed data
			info[i] = Integer.parseInt(strInfo[i]);
		}
		return info;
	}
	
	
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
		
		String line = reader.readLine(); // First line format: Floors, Elevators, Riders, Threads, Capacity
		int[] simulationInfo = parseInformation(line.split(" "));
		int floors = simulationInfo[0];
		int elevators = simulationInfo[1];
		int riders = simulationInfo[2];
		int threads = simulationInfo[3];
		int capacity = simulationInfo[4];
		
		Building building = new Building(floors, elevators, capacity);
		
		ArrayList<Rider> riderThreads = new ArrayList<Rider>();
		
		for (int i = 0; i < threads; ++i) {
			Rider rider = new Rider(building, reader, i);
			rider.start();
			riderThreads.add(rider);
		}
		
		for (Rider r: riderThreads) {
			r.join();
		}
		building.closeLog();
		building.stopElevators();
		
	}
	
	
	
	
	
	
}
