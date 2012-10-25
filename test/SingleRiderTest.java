import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Random;

import org.junit.Test;


public class SingleRiderTest {

	@Test
	public void testSingleRider() throws InterruptedException {
		
		final int elevators = 2;
		final int floors = 5;
		final int riders = 5;
		
		Building b = new Building(floors, elevators);
		int[] currentFloors = {1, 3, 0, 2, 4};
		int[] destinationFloors = {2, 1, 3, 1, 1};
	//	int[] expectedVisitedFloors = {1, 2, 3, 1, 0, 3, 2, 1, 4, 1};
		int[] expectedVisitedFloors = {1, 3, 0};
		int expectedVisits = expectedVisitedFloors.length;
		
		Rider r0 = new Rider(b, 1, 2);
		Rider r1 = new Rider(b, 3, 0);
		Rider r2 = new Rider(b, 4, 2);
		Rider r3 = new Rider(b, 2, 0);
//		Rider r4 = new Rider(b, 1, 2);
//		Rider r5 = new Rider(b, 1, 2);
//		Rider r6 = new Rider(b, 1, 2);
		
		r0.start();
		r1.start();
		r2.start();
		r3.start();
		
		while (r0.isAlive() || r1.isAlive() ||
				r2.isAlive() || r3.isAlive()) {
			// Do nothing, wait for them to complete
		}
		
		b.printVisitedFloors();
//			
//		for (int i = 0; i < riders; ++i) {
//			Random rand = new Random();
//			int sleeptime = rand.nextInt(3000);
//			int currentFloor = currentFloors[i];
//			int destinationFloor = destinationFloors[i];
//			Rider a = new Rider(b, currentFloor, destinationFloor);
//			a.start();
//		//	Thread.currentThread().sleep(sleeptime);
//		}
//		
//		int actualVisits = 0;
//		while (actualVisits < expectedVisits) {
//			actualVisits = b.getVisitedFloors().size();
//			System.out.println(b.getVisitedFloors());
//			// Just wait for all threads to finish
//		}
//		System.out.println(b.getVisitedFloors());
//		for (int i = 0; i < expectedVisits; ++i) {
//			if (expectedVisitedFloors[i] != b.getVisitedFloors().get(i)) {
//				fail("Unexpected floor visits");
//			}
//		}
	}

}
