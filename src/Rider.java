
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
	public void goToFloor(int floor) throws InterruptedException{
		if(floor==myFloor) return;
		else if(floor>myFloor){
			myBuilding.CallUp(floor);
			Elevator arrivedElevator = myBuilding.AwaitUp(floor);
			arrivedElevator.Enter();
			arrivedElevator.RequestFloor(floor);
			myBuilding.exitBarriers.get(floor).hold();
			arrivedElevator.Exit();
		}
		else if(floor<myFloor){
			myBuilding.CallDown(floor);
			Elevator arrivedElevator = myBuilding.AwaitDown(floor);
			arrivedElevator.Enter();
			arrivedElevator.RequestFloor(floor);
			myBuilding.exitBarriers.get(floor).hold();
			arrivedElevator.Exit();
		
		}
		
	}
}
