import java.awt.List;
import java.util.ArrayList;

public class tcss343 {
	
	
	
	public static int[] bruteForce(final int theStart, final int theEnd, final int theCosts[][]) {
		
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
			}
		}
		
		//debugging purpose
				for(int i = 0; i < sets.size(); i++) {
					System.out.print("Set " + i + ". [");
					for(int j = 0; j < sets.get(i).size(); j++) {
						System.out.print(sets.get(i).get(j) + ",");
					}
					System.out.print("]" + '\n');
				}
		
		return null;
	}

	public static void main(String[] args) {
		
		int[][] costs = new int[][]{{ 1,  2,  3, 4,  5},
			   {-1,  0,  2, 4,  6},
			   {-1, -1,  0, 2,  5},
			   {-1, -1, -1, 0,  2},
			   {-1, -1, -1, -1, 0}};
			   
			   int i = 1; 
			   int j = 5; 
			   
			   bruteForce(i, j, costs);
	}

}
