package Part2;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimulationGrader {

	
	private static HashMap<String, Integer> rubric;
	static {
		rubric = new HashMap<String, Integer>();
		rubric.put("T\\d: R\\d pushes [U|D]\\d", 0);
		rubric.put("T\\d: R\\d waits [U|D]\\d", 1);
		rubric.put("T\\d: R\\d enters E\\d on F\\d", 2);
		rubric.put("T\\d: R\\d pushes E\\dB\\d", 3);
		rubric.put("T\\d: R\\d exits E\\d on F\\d", 4);
		rubric.put("E\\d on F\\d opens", 5);
		rubric.put("E\\d on F\\d closes", 6);
		rubric.put("E\\d moves up to F\\d", 7);
		rubric.put("E\\d moves down to F\\d", 8);
	}
	
	
	public static void main(String[] args) throws IOException {
		
		if (args.length == 0) {
			System.out.println("ERROR: No input file provided!");
			return;
		} else if (args.length > 1) {
			System.out.println("More than one file provided, only looking at first file.");
		}	
		
		FileInputStream file = new FileInputStream(args[0]);
		DataInputStream data = new DataInputStream(file);
		BufferedReader reader = new BufferedReader(new InputStreamReader(data));
		
		HashMap<String, Integer> scores = new HashMap<String, Integer>();
		HashMap<String, String> lastElevatorAction = new HashMap<String, String>();
		HashMap<String, String> lastElevatorKey = new HashMap<String, String>();
		String line;
		int totalScore = 0;
		
		String riderString = "R\\d";
		Pattern riderRegex = Pattern.compile(riderString); 
		String elevatorString = "E\\d";
		Pattern elevatorRegex = Pattern.compile(elevatorString); 
		
		while ((line = reader.readLine()) != null) {
			for (String key: rubric.keySet()) {
				Pattern regex = Pattern.compile(key); 
				Matcher matcher = regex.matcher(line);
				if (!matcher.find()) {
					continue;
				}
				if (matcher.matches()) {
					Matcher riderMatcher = riderRegex.matcher(matcher.group());
					
					Matcher elevatorMatcher = elevatorRegex.matcher(matcher.group());
					if (!riderMatcher.find()) {
						if (!elevatorMatcher.find()) {
							continue;
						} else {
							lastElevatorKey.put(elevatorMatcher.group(), key);
							lastElevatorAction.put(elevatorMatcher.group(), line);
						}
					} else {
						int value = rubric.get(key);
						if (value == 2 || value == 4) {
							if (!elevatorMatcher.find()) {
								continue;
							} else {
								String elevatorInfo = elevatorMatcher.group();

								String elevatorKey = lastElevatorKey.get(elevatorInfo);
								if (rubric.get(elevatorKey) != 5) {
									++totalScore;
								}
								String lastAction = lastElevatorAction.get(elevatorInfo);
								Matcher actionMatcher = elevatorRegex.matcher(lastAction);
								Matcher lineMatcher = elevatorRegex.matcher(line);
								if (actionMatcher.matches() && lineMatcher.matches()) {
									if (actionMatcher.group() != lineMatcher.group()) {
										++totalScore;
									}
								}
							}
						} 
						String riderInfo = riderMatcher.group();
						if (scores.keySet().contains(riderInfo)) {
							if ((rubric.get(key) - scores.get(riderInfo)) != 1) {
								++totalScore;
							}
						} 
						scores.put(riderInfo, rubric.get(key));
					}
				}
			}
		}
		
		if (totalScore == 0) {
			System.out.printf("PASSED\n");
		} else {
			System.out.printf("FAILED: Score %d\n", totalScore);
		}
		
		reader.close();
		data.close();
		file.close();
		
		
	}
	
	
}
