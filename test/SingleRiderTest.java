import java.io.IOException;

import org.junit.Test;

import Part2.Building;
import Part2.Rider;


public class SingleRiderTest {

	@Test
	public void testSingleRider() throws InterruptedException, IOException {
		
		final int elevators = 2;
		final int floors = 10;
		final int riders = 5;
		
		Building b = new Building(floors, elevators, 2);
		int[] currentFloors = {1, 3, 0, 2, 4};
		int[] destinationFloors = {2, 1, 3, 1, 1};
	//	int[] expectedVisitedFloors = {1, 2, 3, 1, 0, 3, 2, 1, 4, 1};
		int[] expectedVisitedFloors = {1, 3, 0};
		int expectedVisits = expectedVisitedFloors.length;
		
		
		b.closeLog();
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
