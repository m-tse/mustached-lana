
public class myThread extends Thread {

	
	public void run(){
		for (int i = 0; i < 10; ++i) {
			System.out.printf("Thread %s: %d\n", this.getName(), i);
		}
	}
	
	
}
