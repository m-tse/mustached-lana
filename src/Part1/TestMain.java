package Part1;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




/**
 * Used for testing the EventBarrier Implementation
 * @author tS3m
 *
 */
public class TestMain {
	public static void main(String[] args) throws InterruptedException, IOException{
		
		
		String str = "T4: R4 pushes U3";
//		String regex = "R\\d";
		String regex = "T\\d: R\\d pushes [U|D]\\d";
		str.matches(regex);
		Pattern reg = Pattern.compile(regex);
		Matcher regMatcher = reg.matcher(str);
//		regMatcher.matches();
		regMatcher.find();
		System.out.println(regMatcher.group());
		System.out.println(regMatcher.group());
		
		
//		MyEventBarrier anEventBarrier = new MyEventBarrier();
//		TestGateKeeper gatekeep = new TestGateKeeper(anEventBarrier, 0);
//		gatekeep.watch();
//		int counter = 0;
//		while(true){
//			TestThread t = new TestThread(anEventBarrier, counter);
//			counter++;
//			t.start();
//			Random rand = new Random();
//			int sleeptime = rand.nextInt(2000);
//			Thread.currentThread().sleep(sleeptime);
//		}
//		
	}
}
