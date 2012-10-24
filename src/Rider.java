/**
 * Thread class representing a Rider.  Riders play the "minstrels" role in the lab description.
 * @author tS3m
 *
 */
public class Rider extends Thread{
	Building myBuilding;
	int myFloor;
	int myDestination;
	public Rider(Building b, int floor, int destination){
		myBuilding=b;
		myFloor=floor;
		myDestination=destination;
	}
	public void run() {
		try {
			goToFloor(myDestination);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		
	}
	public void goToFloor(int destinationFloor) throws InterruptedException{
		if(destinationFloor==myFloor) return;
		else if(destinationFloor>myFloor){//going up
			myBuilding.CallUp(myFloor);
			Elevator arrivedElevator = myBuilding.AwaitUp(myFloor);
			arrivedElevator.Enter();
			arrivedElevator.RequestFloor(destinationFloor);
			myBuilding.exitBarriers.get(destinationFloor).hold();
			arrivedElevator.Exit();
		}
		else if(destinationFloor<myFloor){//going down
			myBuilding.CallDown(myFloor);
			Elevator arrivedElevator = myBuilding.AwaitDown(myFloor);
			arrivedElevator.Enter();
			arrivedElevator.RequestFloor(destinationFloor);
			myBuilding.exitBarriers.get(destinationFloor).hold();
			arrivedElevator.Exit();
		
		}
		
	}
}
