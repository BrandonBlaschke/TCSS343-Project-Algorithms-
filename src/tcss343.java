import java.awt.List;
import java.util.ArrayList;

public class tcss343 {
	
	public static ArrayList<Integer> bruteForce(final int theStart, final int theEnd, final int theCosts[][]) {
		
		//TODO: Check for bad input 
		
		//Hold all Power sets 
		ArrayList<ArrayList<Integer>> sets = new ArrayList<ArrayList<Integer>>(); 
		
		//Hold the numbers in between theStart and theEnd  
		int[] setNums = new int[5]; 
		
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
			
			//for each element in the set check if it is the ending or starting element 
			for (int j = 0; j < sets.get(i).size(); j++) {
				
				if(sets.get(i).get(j) == theEnd) {
					
					checkEnd = true;
				}
				if(sets.get(i).get(j) == theStart) {
					checkStart = true; 
				}
			}
			
			//If the set has either starting or ending element remove it 
			if(!checkStart || !checkEnd) {
				sets.remove(i); 
				i--;
			}
		}
		
		//Get the costs 
		int[] costs = new int[sets.size()];
		
		//for each set
		for (int i = 0; i < sets.size(); i++) {
			int total = 0;
			
			//get the total cost for each move to a post 
			for (int j = 1; j < sets.get(i).size(); j++) {
				
				//This is getting the set#i and element#j and - 1 because were putting that 
				//value into an array and need to go down one. 
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
		
		// debugging purpose
		/*for (int i = 0; i < sets.size(); i++) {
			System.out.print("Set " + i + ". [");
			for (int j = 0; j < sets.get(i).size(); j++) {
				System.out.print(sets.get(i).get(j) + ",");
			}
			System.out.print("]" + " Cost: " + costs[i] + '\n');
		}*/
		
		return sets.get(leastSet);
	}
	
	public static int dynamic(final int theStart, final int theEnd, final int theCosts[][]) {
		
		//TODO: Check for bad input 
		
		//Table to keep track of total
		int[] steps = new int[theEnd - theStart + 1];
		
		//Initial Starting values 
		steps[0] = theCosts[theStart][theStart];
		steps[1] = theCosts[theStart][theStart + 1]; 
				
		//For each step of 1->n check previous answers and get least		
		for (int i = 2; i < theEnd - theStart + 1; i++) {
			
			//This is the leastStep, this will eventually be at the end of the loop the 
			//total that was the smallest for that step. 
			int leastStep = steps[i - 1] + theCosts[i-1][i];
			
			//Go through all the past steps previous lowest solutions
			for (int j = 0; j < i; j++) {
				
				//Total cost for this one instance of a step
				int total = theCosts[j][i] + steps[j];
				
				if (total <= leastStep) {
					leastStep = total;
				}
			}
			
			//This step gets the lowest
			steps[i] = leastStep; 
		}
		
		/*for(int i: steps) {
			System.out.println(i);
		}*/
		
		/*int count = 0; 
		for (int i = theStart; i < theEnd; i++) {
			
			int leastStep = theCosts[0][count]; 
			System.out.println("LeasStep " + leastStep);
			int countJ = 0;
			
			for (int j = theStart; j < i; j++) {
				int total = theCosts[j][i] + steps[countJ];
				System.out.println("Comparing total: " + total + " to leastStep: " + leastStep);
				if (total <= leastStep) {
					leastStep = total;
				}
				countJ++;
			}
			
			steps[count] = leastStep; 
			count++;
		}*/
		
		return steps[steps.length - 1];
	}
	

	public static void main(String[] args) {
		
		int[][] costs = new int[][]{{ 0,  2,  3, 7,  8},
			   						{-1,  0,  2, 4,  6},
			   						{-1, -1,  0, 2,  5},
			   						{-1, -1, -1, 0,  2},
			   						{-1, -1, -1, -1, 0}};

		int i = 1;
		int j = 5;

		ArrayList<Integer> solution = bruteForce(i, j, costs);
		System.out.print("Soultion is [");
		for(int a = 0; a < solution.size(); a++) {
			System.out.print(" " + solution.get(a));
		}
		System.out.print("]");
		
		//TODO: Function to go back and retrace steps 
		System.out.println("\nDynamic Solution is: " + dynamic(i,j,costs));
	}

}
