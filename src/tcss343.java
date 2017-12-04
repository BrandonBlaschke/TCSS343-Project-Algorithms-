
/** TCSS343: Source code for Homework 4
 * @author Brandon Blascke and Hasnah Said
 */

import java.awt.List;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;

public class tcss343 {
	
	/** Brute force algorithm for solving the post problem 
	 * 
	 * @param theStart Starting post
	 * @param theEnd Ending post
	 * @param theCosts 2D array for the cost of post x -> y
	 * @return Array list of all the steps for the solution
	 */
	public static ArrayList<Integer> bruteForce(final int theStart, final int theEnd, final int theCosts[][]) {
		
		//TODO: Check for bad input 
		
		//Hold all Power sets 
		ArrayList<ArrayList<Integer>> sets = new ArrayList<ArrayList<Integer>>(); 
		
		//Hold the numbers in between theStart and theEnd  
		int[] setNums = new int[theEnd - theStart + 1]; 
		
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
		
		//Adding the total for the least set 
		sets.get(leastSet).add((costs[leastSet]));
		return sets.get(leastSet);
	}
	
	/** Dynamic programming solution to the problem
	 * 
	 * @param theStart Starting post
	 * @param theEnd Ending post
	 * @param theCosts 2D array for the cost of post x -> y
	 * @return Stack that where the head is the total cost for the solution and the rest being the steps in took to 
	 * get the final solution
	 */
	public static Deque<Integer> dynamic(final int theStart, final int theEnd, final int theCosts[][]) {
		
		//TODO: Check for bad input 
		
		//Difference from start and the end to get the total length 
		final int totalLength = theEnd - theStart + 1;
		
		//Array of all the post numbers from start to end 
		int[] posts = new int[totalLength];
		for (int i = 0; i < totalLength; i++) {
			posts[i] = i + theStart; 
		}
		
		//Table to keep track of least total for each step
		int[] leastTotals = new int[totalLength];
		
		//Keep track of the previous step to get that step. 
		int[] prevSteps = new int[totalLength];
		
		//Initial starting values for prevSteps
		prevSteps[0] = 0; 
		prevSteps[1] = theStart; 
		
		//Initial Starting values 
		leastTotals[0] = theCosts[theStart][theStart];
		leastTotals[1] = theCosts[theStart][theStart + 1]; 
				
		//For each step of 1->n check previous answers and get least		
		for (int i = 2; i < theEnd - theStart + 1; i++) {
			
			//This is the leastStep, this will eventually be at the end of the loop the 
			//total that was the smallest for that step. 
			int leastStep = leastTotals[i - 1] + theCosts[i-1][i];
			prevSteps[i] = i;
			
			//Go through all the past steps previous lowest solutions
			for (int j = 0; j < i; j++) {
				
				//Total cost for this one instance of a step
				int total = theCosts[j][i] + leastTotals[j];
				
				if (total <= leastStep) {
					leastStep = total;
					prevSteps[i] = j + 1;
				}
			}
			
			//This step gets the lowest
			leastTotals[i] = leastStep; 
		}
		
		//Debugging, shows the tables for finding the solution 
		/*System.out.println("\n Posts");
		for(int i: posts) {
			System.out.print(" " + i);
		}
		
		System.out.println("\n Least Totals");
		for(int i: leastTotals) {
			System.out.print(" " + i);
		}
		
		System.out.println("\nPrevious Steps");
		for(int i: prevSteps) {
			System.out.print(" " + i);
		}*/
		
		return retrace(prevSteps, posts, leastTotals[leastTotals.length - 1]);
	}
	
	/** Retraces the steps from the dynamic solution
	 * 
	 * @param thePrevSteps Previous Steps is the array of steps, with each step being the previous step it took to get there
	 * @param thePosts Posts from the start to the end numbered
	 * @param theLeastTotal The least total cost for the last post or ending post
	 * @return A Stack starting with the least total for the solution and the rest being the steps to get there
	 */
	public static Deque<Integer> retrace(final int[] thePrevSteps, final int[] thePosts, final int theLeastTotal) {
		
		//Create stack and the current step, which is the last step done 
		Deque<Integer> stack = new ArrayDeque<Integer>();
		int currentStep = thePosts.length;
		
		//Loop until we reach the starting post
		while (currentStep != 0) {
			
			//Push the post to the stack and go down to the previous step for that step
			stack.push(thePosts[currentStep - 1]);
			currentStep = thePrevSteps[currentStep - 1];
		}
		
		//This is the total cost for the solution, it is the head of the stack
		stack.push(theLeastTotal);
		return stack; 
	}
	
	/** Prints the stack from retrace to show the final solution and total cost
	 * @param theStack Stack to be printed with total cost
	 */
	public static void printStack(final Deque<Integer> theStack) {
		
		//get total
		int tempForTotal = theStack.pop();
		
		//Print posts 
		System.out.print("\nFinal solution for dynamic:");
		for (int i = 0; i < theStack.size(); i++) {
			System.out.print(" " + theStack.peek());
			theStack.pop();
			i--;
		}
		
		System.out.print(" Total is " + tempForTotal);
	}

	public static void main(String[] args) {
		
		int[][] costs = new int[][]{{0, 2, 3, 7, 8},
			   						{0, 0, 2, 4, 6},
			   						{0, 0, 0, 2, 5},
			   						{0, 0, 0, 0, 2},
			   						{0, 0, 0, 0, 0}};
			   						
		//Should be 1 3 4 5 6 Total is 9
		int[][] costs2 = new int[][]{{0, 2, 3, 7, 10, 12},
				                     {0, 0, 2, 4, 8, 11},
				                     {0, 0, 0, 2, 5, 9},
				                     {0, 0, 0, 0, 2, 6},
				                     {0, 0, 0, 0, 0, 2},
				                     {0, 0, 0, 0, 0, 0}};

		int i = 1;
		int j = 6;

		ArrayList<Integer> solution = bruteForce(i, j, costs2);
		System.out.print("Soultion is [");
		for(int a = 0; a < solution.size() - 1; a++) {
			System.out.print(" " + solution.get(a));
		}
		
		System.out.print("]" + " Total is " + solution.get(solution.size() - 1));
	
		//Print Dynamic solution
		printStack(dynamic(i,j,costs2));
	}

}
