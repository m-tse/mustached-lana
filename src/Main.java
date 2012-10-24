
public class Main {
	public static final int elevators = 1;
	public static final int floors = 5;
	public static void main(String[] args){
		Building b = new Building(elevators, floors);
		Rider a = new Rider(b, 0);
	}
}
