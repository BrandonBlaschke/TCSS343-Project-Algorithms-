

/**
 * Names: Brandon Blaschke
 * 		  Hasnah Said
 * December 1, 2017
 * Programming Assignment
 * TCSS 343 - Paulo Baretto
 * 
 * */


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class tcss343 {	
	
	private static Random random = new Random();
	
	public static int[][] tempCosts;
	public static int [][] inputList;

	
	public static ArrayList<Integer> bruteForce(final int theStart, final int theEnd, final int theCosts[][]) {
		
		//Hold all Power sets 
		ArrayList<ArrayList<Integer>> sets = new ArrayList<ArrayList<Integer>>(); 
		
		//Hold the numbers in between theStart and theEnd  
		int[] setNums = new int[theEnd]; 
		
		//Length of the list
		int length = setNums.length; 
		
		//Make set of all posts 
		for(int i = 0; i < theEnd; i++) {
			
			setNums[i] = i + 1;
		}
		
		//Generate all the Power sets 
		for (int i = 0; i < (1 << length); i++) {
			
			ArrayList<Integer> temp = new ArrayList<>();
			sets.add(temp);
			
			for(int j = 0; j < length; j++) {
				if ((i & (1 << j)) > 0) {
					sets.get(i).add(j + 1);
				}
			}
		}
		
		//Check if sets have the start or end elements
		for (int i = 0; i < sets.size(); i++) {
			
			boolean checkEnd = false; 
			boolean checkStart = false;
			for (int j = 0; j < sets.get(i).size(); j++) {
				
				if(sets.get(i).get(j) == theEnd) {
					
					checkEnd = true;
				}
				if(sets.get(i).get(j) == theStart) {
					checkStart = true; 
				}
			}
			
			if(!checkStart || !checkEnd) {
				sets.remove(i); 
				i--;
			}
		}
		
		//Get the costs 
		int[] costs = new int[sets.size()];
		
		for (int i = 0; i < sets.size(); i++) {
			int total = 0;
			for (int j = 1; j < sets.get(i).size(); j++) {
				total += theCosts[sets.get(i).get(j-1)-1][sets.get(i).get(j)-1];
			}
			costs[i] = total; 
		}
		
		//Find least of the sets 
		int leastSet = 0;
		int leastCost = costs[0]; 
		
		for (int i = 0; i < costs.length; i++) {
			if (costs[i] < leastCost) {
				leastSet = i;
				leastCost = costs[i];
			}
		}
		
//		// debugging purpose
//		for (int i = 0; i < sets.size(); i++) {
//			System.out.print("Set " + i + ". [");
//			for (int j = 0; j < sets.get(i).size(); j++) {
//				System.out.print(sets.get(i).get(j) + ",");
//			}
//			System.out.print("]" + " Cost: " + costs[i] + '\n');
//		}
		
		return sets.get(leastSet);
	}
	
	/**
	 * 
	 * @param inputCosts matrix with rental costs.
	 * @param theLeft left starting point.
	 * @param theRight ending point.
	 * @return the cheapest cost to rent a canoe form 1 to n.
	 */
	public static int divideAndConquer(int[][] inputCosts, int theLeft, int theRight) {
		//TODO: Print cheapest sequence.
		
		ArrayList<Integer> costs = new ArrayList<Integer>();
		ArrayList<Integer> sequences = new ArrayList<Integer>();
		
		//base case
		if (theLeft == theRight){
			return 0;
			} else {
			for (int i = theLeft + 1; i <= theRight; i++) {
				
				int rCost = divideAndConquer(inputCosts, i, theRight);
				
				int cost = inputCosts[theLeft][i] + rCost;
				
//				System.out.println("costs[" +(left + 1)+"]["+ (i+1) +"]  " + inputCosts[left][i]);
//				System.out.print("\t" + cost + "\t" );
				costs.add(cost);	
			}
		}
		//find minimum cost in list
		int cheapest = costs.get(0);
		for (Integer i: costs) {
			if (i < cheapest) cheapest = i;
		}
		return cheapest;
	}
	
	/**
	 * Generate a cost table of size n x n where n: 25, 50, 100, 200, 400, 800
	 * There are two scenarios: * costs are entirely random.
	 * 							* costs are random but increasing along each row.
	 * 
	 * @param theName name of the file.
	 * @param theSize number of posts.
	 * @param theType increasing: for random increasing costs.
	 * 				  random:     for random positive costs.
	 */	
	public static void generateCostTable(String theName, int theSize, String theType) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(theName));
			
			int tableSize = theSize * theSize;
			int incCost = 0;
			
			/* Random increasing rental costs.*/
			if (theType.equals("increasing")) {
				for (int i = 0; i < theSize; i++) {
					for (int j= 0; j < theSize; j++) {
						if (i > j) {
							writer.write("NA");
						} else if (i == j) {
							writer.write("0");
						} else {
							incCost += random.nextInt(theSize);
							writer.write(String.valueOf(incCost));
						}
						writer.write("\t");
					}
					writer.write("\n");
				}
			/*Random positive rental costs.*/	
			} else if (theType.equals("random")) {
				for (int i = 0; i < theSize; i++) {
					for (int j = 0; j < theSize; j++) {
						if (i > j) {
							writer.write("NA");
						} else if (i == j) {
							writer.write("0");
						} else {
							writer.write(String.valueOf(random.nextInt(tableSize) + 1));
						}
						writer.write("\t");
					}
					writer.write("\n");
				}
			} else {
				System.out.println("Please enter a valid table type (increasing or random)."); 
			}
			
			writer.close();
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found.");
		} catch (IOException e) {
			System.out.println("IO Exception");
		}
		
	}
	
	/**
	 * Read in the text file the user and insert the costs into a matrix.
	 * 
	 * @param theName name of the file.
	 * */
	public static void createCostTable (String theName) {
		ArrayList<Integer> input = new ArrayList<Integer>();
		File file = new File(theName);
		Scanner scanner = null;
		try{
			scanner = new Scanner (file);
		} catch (FileNotFoundException e) {
			
		}
		while (scanner.hasNext()) {
			if (scanner.hasNextInt()) {
				input.add(scanner.nextInt());
				
			} else if (scanner.hasNext("NA")) {
				input.add(0);
				scanner.next();
			} else {
				scanner.next();
			}
		}
		
		// Create the costs matrix
		int size = (int) Math.sqrt(input.size());
		int index = 0;
		inputList = new int[size][size];
		for (int i = 0; i < size && index < 17; i++) {
			for (int j = 0; j < size; j++) {
				inputList[i][j] = input.get(index);
				index++;
			}
		}
	}
	

	public static void main(String... theArgs) {
		
		//TODO: Add running time.
		
		/* Use the file that the user specifies in terminal.*/
		if (theArgs.length == 0) {
			// Read input text file
			ArrayList<Integer> input = new ArrayList<Integer>();
			Scanner scanner = new Scanner (System.in);
			while (scanner.hasNext()) {
				if (scanner.hasNextInt()) {
					input.add(scanner.nextInt());
				} else if (scanner.hasNext("NA")) {
					input.add(0);
					scanner.next();
				} else {
					scanner.next();
				}
			}
			scanner.close();
			
			/*Create the cost table. */
			int size = (int) Math.sqrt(input.size());
			int index = 0;
			inputList = new int[size][size];
			for (int i = 0; i < size && index < 17; i++) {
				for (int j = 0; j < size; j++) {
					inputList[i][j] = input.get(index);
					index++;
				}
			}
		}
		
		/* Generate a cost table using the arguments the user passes in.
		 * Terminal Command: java tcss343 filename.txt table_size table_type.		
		 */
		else {
			String filename = theArgs[0];
			int tableSize = Integer.parseInt(theArgs[1]);
			String tableType = theArgs[2];

			generateCostTable(filename, tableSize, tableType);
			createCostTable(filename);
		}

		
		int i = 1;
		int n = inputList.length;
		
		System.out.println("Brute Force");

		ArrayList<Integer> solution = bruteForce(i, n, inputList);
		System.out.print("Soultion is [");
		for(int a = 0; a < solution.size(); a++) {
			System.out.print(" " + solution.get(a));
		}
		System.out.print("]\n");
		
		//Divide and conquer
		
		int result = divideAndConquer(inputList, 0, n - 1);
		
		System.out.println("\n\nDivide and Conquer");
		System.out.println("Cheapest:" + result);

	}

}
