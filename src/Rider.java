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
		System.out.printf("new passenger on floor %d with destination %d\n", this.myFloor, this.myDestination);
	}
	public void run() {
		try {
			goToFloor(myDestination);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public void goToFloor(int destinationFloor) throws InterruptedException{
		System.out.println("Going to destination");
		Elevator arrived = null;
		if(destinationFloor==myFloor) return;
		else if(destinationFloor>myFloor){//going up
			while (arrived == null) {
				myBuilding.CallUp(myFloor);
				arrived = myBuilding.AwaitUp(myFloor);
			}
			arrived.Enter();
			arrived.RequestFloor(destinationFloor);
			myBuilding.exitBarriers.get(destinationFloor).hold();
			myBuilding.exitBarriers.get(destinationFloor).complete();
			arrived.Exit();
		}
		else if(destinationFloor<myFloor){//going down
			while (arrived == null) {
				myBuilding.CallDown(myFloor);
				arrived = myBuilding.AwaitDown(myFloor);
			}
			arrived.Enter();
			arrived.RequestFloor(destinationFloor);
			myBuilding.exitBarriers.get(destinationFloor).hold();
			myBuilding.exitBarriers.get(destinationFloor).complete();
			arrived.Exit();
		}
		
	}
}
