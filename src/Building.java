import java.util.ArrayList;


public class Building {
	private int floors;
	ArrayList<Elevator> elevators = new ArrayList<Elevator>();


	public ArrayList<EventBarrier> exitBarriers = new ArrayList<EventBarrier>();
	public ArrayList<EventBarrier> entryUpBarriers = new ArrayList<EventBarrier>();
	public ArrayList<EventBarrier> entryDownBarriers = new ArrayList<EventBarrier>();
	Building(int numFloors, int numElevators){
		floors=numFloors;
		for(int i = 0;i<floors;i++){
			entryUpBarriers.add(new myEventBarrier());
			entryDownBarriers.add(new myEventBarrier());
			exitBarriers.add(new myEventBarrier());
		}
		for(int i = 0;i<numElevators;i++)	elevators.add(new Elevator(this));
		for(Elevator e:elevators) e.start();	

	}
	
//for now, simply send a request to the elevator	
	public void CallUp(int floor){
		System.out.printf("Passenger calls up on floor %d\n", floor);
		elevators.get(0).RequestFloor(floor);
	}
	

	public void CallDown(int floor){
		System.out.printf("Passenger calls down on floor %d\n", floor);
		elevators.get(0).RequestFloor(floor);
	}
	
	
	public Elevator AwaitUp(int floor) throws InterruptedException{
		EventBarrier upBarrier = entryUpBarriers.get(floor);
		upBarrier.hold();
		//for now only 1 elevator
		return elevators.get(0);
		
	}
	public Elevator AwaitDown(int floor) throws InterruptedException{
		EventBarrier downBarrier = entryDownBarriers.get(floor);
		downBarrier.hold();
		return elevators.get(0);
		
	}
}
