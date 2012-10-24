import java.util.Random;

/**
 * Used for testing the elevator
 * @author tS3m
 *
 */
public class Main {
	public static final int elevators = 1;
	public static final int floors = 5;
	public static void main(String[] args) throws InterruptedException{
		Building b = new Building(floors, elevators);
		while(true){
			Random rand = new Random();
			int sleeptime = rand.nextInt(3000);
			int currentFloor=rand.nextInt(floors);
			int destinationFloor=rand.nextInt(floors);
			System.out.printf("new passenger on floor %d with destination %d\n", currentFloor, destinationFloor);
			Rider a = new Rider(b, currentFloor, destinationFloor);
			a.start();
			Thread.currentThread().sleep(sleeptime);

		}
		
	}
}
