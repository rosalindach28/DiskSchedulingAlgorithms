import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class DiskSchedulingAlgorithms {
	static Scanner userInput = new Scanner(System.in);
	static Integer[] cylinderRequests = new Integer[1000]; // stores 1000 generated requests
	//static Integer[] example = { 2069, 1212, 2296, 2800, 544, 1618, 356, 1523, 4965, 3681 }; // array for testing purposes

	public static void main(String[] args) throws IOException {

		// Ask user if cylinder requests should be randomly generated or taken from an input file
		System.out.println("Choose how cylinder requests will be generated.");
		System.out.println("1. Randomly");
		System.out.println("2. From input.txt file");
		int userChoice = userInput.nextInt();
		
		// input validation to ensure user enters valid option to generate cylinders
		while (userChoice != 1 && userChoice != 2) {
			System.out.println("Invalid choice, try again.");
			displayCylinderGen();
			userChoice = userInput.nextInt();
		}
		
		//randomly generated cylinder requests
		if(userChoice == 1) {
			Random randomNum = new Random(); // generate random number 
			for(int i = 0; i < cylinderRequests.length; i++) {
				cylinderRequests[i] = randomNum.nextInt(5000); // generate random number between 0 and 4999 since there are 5000 cylinders 
				//System.out.println((i+1) + "." + " Cylinder request: " + cylinderRequests[i]); // print 1000 random cylinder requests
			}
			System.out.println("Generated a random series of 1000 cylinder requests");
		}
		
		else if(userChoice == 2) {
			System.out.print("Enter file path: ");
			String filePath = userInput.next();
			File myFile = new File(filePath);
			Scanner fileOutput = new Scanner(myFile);
			while (fileOutput.hasNextInt()) {
				for (int index = 0; index < cylinderRequests.length; index++)
				{
					cylinderRequests[index] = fileOutput.nextInt();
				}
			}
			//System.out.println("\nFile read");
		}
		
		// Pass initial disk head position and previous head position to determine
		// initial direction for SCAN and CSCAN
		int[] diskHeadPos = new int[2];
		System.out.print("Enter initial position of disk head: ");
		diskHeadPos[0] = userInput.nextInt(); // initial disk head position
		System.out.print("Enter previous position: ");
		diskHeadPos[1] = userInput.nextInt(); // previous position
		
		// Ask user to choose a disk-scheduling algorithm for the program to use
		System.out.println("\nNow choose a disk-scheduling algorithm or exit by entering a number between 1 and 5.");
		System.out.println("1. FCFS");
		System.out.println("2. SSTF");
		System.out.println("3. SCAN");
		System.out.println("4. C-SCAN");
		System.out.println("5. Exit");
		int userAlgoChoice = userInput.nextInt();

		// input validation to ensure user enters valid input
		while (userAlgoChoice != 1 && userAlgoChoice != 2 && userAlgoChoice != 3 && userAlgoChoice != 4
				&& userAlgoChoice != 5) {
			System.out.println("Invalid choice, try again.");
			userAlgoChoice = userInput.nextInt();
		}

//		
		// program will keep asking user to choose an option until they choose to exit
		while (userAlgoChoice != 5) {
			if (userAlgoChoice == 1) {
				System.out.println("You chose FCFS");
				FCFS(diskHeadPos[0], cylinderRequests);
				displayAlgoMenu();
				userAlgoChoice = userInput.nextInt();
			} else if (userAlgoChoice == 2) {
				System.out.println("You chose SSTF");
				// SSTF(diskHeadPos[0], cylinderRequests);
				System.out.println();
				displayAlgoMenu();
				userAlgoChoice = userInput.nextInt();
			} else if (userAlgoChoice == 3) {
				System.out.println("You chose SCAN");
				SCAN(diskHeadPos[1], diskHeadPos[0], cylinderRequests);
				displayAlgoMenu();
				userAlgoChoice = userInput.nextInt();
			} else if (userAlgoChoice == 4) {
				System.out.println("You chose C-SCAN");
				CSCAN(diskHeadPos[1], diskHeadPos[0], cylinderRequests);
				displayAlgoMenu();
				userAlgoChoice = userInput.nextInt();
			} else if (userAlgoChoice == 5) {
				System.exit(0);
			}
		}
	} // end of main

	public static void displayCylinderGen() {
		System.out.println("Choose how cylinder requests will be generated.");
		System.out.println("1. Randomly");
		System.out.println("2. From input.txt file");
	}

	public static void displayAlgoMenu() {
		System.out.println("Choose a disk-scheduling algorithm or exit by entering a number between 1 and 5.");
		System.out.println("1. FCFS");
		System.out.println("2. SSTF");
		System.out.println("3. SCAN");
		System.out.println("4. C-SCAN");
		System.out.println("5. Exit");
	}

	public static void FCFS(int head, Integer[] requests) {
		// implementation of algorithm
		int headMovement = 0;
		int numOfDirectionChanges = 0;

		for (int n = 0; n < requests.length; n++) {
			int currentReq = requests[n];
			int distance = Math.abs(currentReq - head);
			headMovement += distance;
			head = currentReq; // update head to next request in line
		}

		// compare current request to next request until reaching the end
		for (int k = 0; k < requests.length - 1; k++) {
			if (head < requests[k]) { // for direction if initial position is less than next request go towards the
										// right
				if (requests[k] > requests[k + 1]) // when new head is greater than the next request go in the opposite
													// direction
				{
					numOfDirectionChanges++;
					head = requests[k]; // update head
				}
			} else if (head > requests[k]) { // if initial position is greater than next request go towards the left
				if (requests[k] < requests[k + 1]) // when new head is less than the next request go in the opposite
													// direction
				{
					numOfDirectionChanges++;
					head = requests[k];
				}
			}
		}
		System.out.println("Amount of Head movement: " + headMovement);
		System.out.println("Number of changes in direction: " + numOfDirectionChanges);
	}

	// could not implement correctly
	public static void SSTF(int head, int[] requests) {
		int headMovement = 0;
		int numOfDirectionChanges = 0;

		System.out.println("Amount of Head movement: " + headMovement);
		System.out.println("Number of changes in direction: " + numOfDirectionChanges);

	} // end of method

	public static void SCAN(int prev, int head, Integer[] requests) {
		int headMovement = 0;
		int numOfDirectionChanges = 0;
		ArrayList<Integer> bigger = new ArrayList<Integer>(); // arraylist to store values that are bigger than the
																// initial head position
		ArrayList<Integer> smaller = new ArrayList<Integer>(); // // arraylist to store values that are smaller than the
																// initial head position

		if (prev < head) { // direction to start in is determined by previous value before initial head
			for (int r = 0; r < requests.length; r++) { // if the previous value is less than initial then go to the
														// right, towards bigger values
				if (requests[r] > head) {
					bigger.add(requests[r]);
				}
			}
			bigger.add(4999); // reach the end point on left side
			Collections.sort(bigger); // gives list of requests that are bigger than the initial head in ascending
										// order
			for (int m = 0; m < requests.length; m++) { // now get all the values that are smaller than the initial head
				if (requests[m] < head) {
					smaller.add(requests[m]);
				}
			}
			Collections.sort(smaller, Collections.reverseOrder()); // sort smaller values in descending order
			bigger.addAll(smaller); // add the sorted list of smaller values to the list of bigger values
			//System.out.println(bigger); // now contains the updated complete list of requests in the correct order

			for (int i = 0; i < bigger.size(); i++) { // use the updated list to calculate the distance and head
														// movement
				int currentReq = bigger.get(i);
				int distance = Math.abs(currentReq - head);
				headMovement += distance;
				head = currentReq; // update head to next request in line
			}

			// compare current request to next request until reaching the end
			for (int k = 0; k < bigger.size() - 1; k++) {
				if (head < bigger.get(k)) { // for direction if initial position is less than next request go towards
											// the right
					if (bigger.get(k) > bigger.get(k + 1)) // when new head is greater than the next request go in the
															// opposite direction
					{
						numOfDirectionChanges++;
						head = bigger.get(k);
					}
				} else if (head > bigger.get(k)) { // if initial position is greater than next request go towards the
													// left
					if (bigger.get(k) < bigger.get(k + 1)) // when new head is less than the next request go in the
															// opposite direction
					{
						numOfDirectionChanges++;
						head = bigger.get(k);
					}
				}
			}

		} else if (prev > head) { // going towards left
			for (int m = 0; m < requests.length; m++) {
				if (requests[m] < head) {
					smaller.add(requests[m]);
				}
			}
			smaller.add(0); // 0 is end point on left side
			Collections.sort(smaller, Collections.reverseOrder()); // in descending order

			for (int r = 0; r < requests.length; r++) { // if the previous value is less than initial then go to the
														// right, towards bigger values
				if (requests[r] > head) {
					bigger.add(requests[r]);
				}
			}
			Collections.sort(bigger);
			smaller.addAll(bigger);
			//System.out.println(smaller);

			for (int i = 0; i < smaller.size(); i++) {
				int currentReq = smaller.get(i);
				int distance = Math.abs(currentReq - head);
				headMovement += distance;
				head = currentReq; // update head to next request in line
			}

			for (int k = 0; k < smaller.size() - 1; k++) {
				if (head < smaller.get(k)) {
					if (smaller.get(k) > smaller.get(k + 1)) {
						numOfDirectionChanges++;
						head = smaller.get(k);
					}
				} else if (head > smaller.get(k)) {
					if (smaller.get(k) < smaller.get(k + 1)) {
						numOfDirectionChanges++;
						head = smaller.get(k);
					}
				}
			}

		}

		System.out.println("Amount of Head movement: " + headMovement);
		System.out.println("Number of changes in direction: " + numOfDirectionChanges);
	}

	public static void CSCAN(int prev, int head, Integer[] requests) { // similar implementation to SCAN
		int headMovement = 0;
		int numOfDirectionChanges = 0;
		ArrayList<Integer> bigger = new ArrayList<Integer>();
		ArrayList<Integer> smaller = new ArrayList<Integer>();

		if (prev < head) {
			for (int r = 0; r < requests.length; r++) {
				if (requests[r] > head) {
					bigger.add(requests[r]);
				}
			}
			bigger.add(4999); // reach the end point on right side
			Collections.sort(bigger); 
			for (int m = 0; m < requests.length; m++) { // now get all the values that are smaller than the initial head
				if (requests[m] < head) {
					smaller.add(requests[m]);
				}
			}
			smaller.add(0); // include left end point
			Collections.sort(smaller); // sort smaller values in ascending order
			bigger.addAll(smaller); 
			//System.out.println(bigger); 

			for (int i = 0; i < bigger.size(); i++) { // use the updated list to calculate the distance and head
														// movement
				int currentReq = bigger.get(i);
				int distance = Math.abs(currentReq - head);
				headMovement += distance;
				head = currentReq; 
			}

			for (int k = 0; k < bigger.size() - 1; k++) {
				if (head < bigger.get(k)) { 
					if (bigger.get(k) > bigger.get(k + 1)) 
					{
						numOfDirectionChanges++;
						head = bigger.get(k);
					}
				} else if (head > bigger.get(k)) { 
					if (bigger.get(k) < bigger.get(k + 1)) 
					{
						numOfDirectionChanges++;
						head = bigger.get(k);
					}
				}
			}
		}

		else if (prev > head) { // going towards left
			for (int m = 0; m < requests.length; m++) {
				if (requests[m] < head) {
					smaller.add(requests[m]);
				}
			}
			smaller.add(0); // 0 is end point on left side
			Collections.sort(smaller, Collections.reverseOrder()); // in descending order

			for (int r = 0; r < requests.length; r++) { // if the previous value is less than initial then go to the
														// right,
														// towards bigger values
				if (requests[r] > head) {
					bigger.add(requests[r]);
				}
			}
			bigger.add(4999); // add right end point as well
			Collections.sort(bigger, Collections.reverseOrder());
			smaller.addAll(bigger);
			//System.out.println(smaller);

			for (int i = 0; i < smaller.size(); i++) {
				int currentReq = smaller.get(i);
				int distance = Math.abs(currentReq - head);
				headMovement += distance;
				head = currentReq; // update head to next request in line
			}

			for (int k = 0; k < smaller.size() - 1; k++) {
				if (head < smaller.get(k)) {
					if (smaller.get(k) > smaller.get(k + 1)) {
						numOfDirectionChanges++;
						head = smaller.get(k);
					}
				} else if (head > smaller.get(k)) {
					if (smaller.get(k) < smaller.get(k + 1)) {
						numOfDirectionChanges++;
						head = smaller.get(k);
					}
				}
			}
		}
		System.out.println("Amount of Head movement: " + headMovement);
		System.out.println("Number of changes in direction: " + numOfDirectionChanges);
	}

} // end of class
