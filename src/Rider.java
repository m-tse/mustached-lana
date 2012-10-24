
public class Rider extends Thread{
	Building myBuilding;
	int myFloor;
	public Rider(Building b, int floor){
		myBuilding=b;
		myFloor=floor;
	}
	public void run() {
		// TODO Auto-generated method stub
		
	}
	public void goToFloor(int floor){
		if(floor==myFloor) return;
		else if(floor>myFloor){
			myBuilding.CallUp(floor);
			Elevator arrivedElevator = myBuilding.AwaitUp(floor);
			arrivedElevator.Enter();
			arrivedElevator.RequestFloor(floor);
		}
		
	}
}
