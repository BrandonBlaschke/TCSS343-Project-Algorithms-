/**/

import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class tcss343 {	
	
	public static int[][] tempCosts;
	public static int [][] inputList;
	
	
	/*
	 **************************************
	 * 		      Brute Force             *
	 **************************************
	 */
	
	
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
		System.out.println("Brute Force");
		// debugging purpose
		for (int i = 0; i < sets.size(); i++) {
			System.out.print("Set " + i + ". [");
			for (int j = 0; j < sets.get(i).size(); j++) {
				System.out.print(sets.get(i).get(j) + ",");
			}
			System.out.print("]" + " Cost: " + costs[i] + '\n');
		}
		
		return sets.get(leastSet);
	}
	
	/*
	 **************************************
	 * 	    	Divide and Conquer        *
	 **************************************
	 */
	
	public static int divideAndConquer(int[][] inputCosts, int left, int right) {
		ArrayList<Integer> costs = new ArrayList<Integer>();
		//base case
		if (left == right){
			return 0;
		
		//
		} else {
			for (int i = left + 1; i <= right; i++){	
				int cost = inputCosts[left][i] + divideAndConquer(inputCosts, i, right);
//				System.out.println("costs[" +left+"]["+i+"]  " + inputCosts[left][i]);
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
	
	
	
	

	public static void main(String[] args) {
		// Read input text file
		ArrayList<Integer> input = new ArrayList<Integer>();
		File file = new File("input.txt");
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
		
//		tempCosts = new int[][]{{ 0,  2,  3, 7,  8},
//			   				{-1,  0,  2, 4,  6},
//			   				{-1, -1,  0, 2,  5},
//			   				{-1, -1, -1, 0,  2},
//			   				{-1, -1, -1, -1, 0}};
			   				
	    tempCosts = new int[][]{{ 0,  2,  3, 7},
				   			    {-1,  0,  2, 4},
				   			    {-1, -1,  0, 2},
				   			    {-1, -1, -1, 0}};

		int i = 1;
		int n = tempCosts.length;
		
		//Brute Force
		ArrayList<Integer> solution = bruteForce(i, n, tempCosts);
		System.out.print("Soultion is [");
		for(int a = 0; a < solution.size(); a++) {
			System.out.print(" " + solution.get(a));
		}
		System.out.print("]");
		
		//Divide and conquer
		
		int result = divideAndConquer(inputList, 0, n - 1);
		System.out.println("\n\nDivide and Conquer");
		System.out.println("Cheapest:" + result);
		
	}

}
