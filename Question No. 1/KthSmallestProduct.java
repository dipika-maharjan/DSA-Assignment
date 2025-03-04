/*Question no. 1(b)
 * Determine the kth lowest combined return that can be achieved.
 * 
 * Algorithm Explanation:
 * 1. Use a min-heap (PriorityQueue) to store and retrieve the smallest combined returns efficiently.
 * 2. Start with the smallest possible pair (returns1[0] * returns2[0]).
 * 3. Use a heap to explore the next smallest pairs, pushing elements progressively to track the smallest values.
 * 4. Extract the k-th smallest value from the heap.

*/

import java.util.PriorityQueue;

public class KthSmallestProduct {
    public static class Pair {
        int i, j, value;                
        Pair(int i, int j, int value) { 
            this.i = i;          //i: index in returns1
            this.j = j;          //j: index in returns2
            this.value = value;  //value: their product (returns1[i] * returns2[j])
        }
    }

    public static int findKthLowestReturn(int[] returns1, int[] returns2, int k) {  //Function to find the k-th smallest combined return.
        PriorityQueue<Pair> minHeap = new PriorityQueue<>((a, b) -> a.value - b.value); //Creates a min-heap (smallest value comes out first).

        // Insert the first element from returns1 combined with all elements in returns2
        for (int j = 0; j < returns2.length; j++) {
            minHeap.offer(new Pair(0, j, returns1[0] * returns2[j]));
        }

        int count = 0, result = 0;    //count: Keeps track of how many numbers we’ve removed from the heap. result: Stores the k-th smallest value.
        
        //Loop until we find the k-th smallest product:
        while (!minHeap.isEmpty()) {
            Pair current = minHeap.poll(); //Removes the smallest element from the heap.
            count++; //Increments count.
            result = current.value; //Stores the value in result.

            if (count == k) break;  //If count == k, we have found the answer, so we exit the loop.

            //Moves to the next row in returns1, keeping the same column j:
            int i = current.i, j = current.j;
            if (i + 1 < returns1.length) {
                minHeap.offer(new Pair(i + 1, j, returns1[i + 1] * returns2[j])); //Adds returns1[i+1] * returns2[j] to the heap if it exists.
            }
        }
        return result;  //Returns the k-th smallest combined return.
    }

    public static void main(String[] args) {
        int[] returns1 = {2, 5};
        int[] returns2 = {3, 4};
        int k = 2;
        System.out.println("Output: " + findKthLowestReturn(returns1, returns2, k)); // Output: 8

        int[] returns1_2 = {-4, -2, 0, 3};
        int[] returns2_2 = {2, 4};
        int k2 = 6;
        System.out.println("Output: " + findKthLowestReturn(returns1_2, returns2_2, k2)); // Output: 0
    }
}

/*Output:
* Example 1: Input: returns1= [2,5], returns2= [3,4], k = 2
 1. Combine each element from returns1 with each element form returns2
    returns1[0] * returns[0] = 2*3 = 6
    returns1[0] * returns[1] = 2*4 = 8
    returns1[1] * returns[0] = 5*3 = 15
    returns1[1] * returns[1] = 5*4 = 20
    Therefore, list of all products = [6,8,15,20]

 2. All the products are sorted in this case.
 3. Since, k=2. The 2nd smallest product is 8.

* Example 2: Input: returns1= [-4,-2,0,3], returns2= [2,4], k = 6
 1. Combine each element from returns1 with each element form returns2
    returns1[0] * returns[0] = -4*2 = -8
    returns1[0] * returns[1] = -4*4 = -16
    returns1[1] * returns[0] = -2*2 = -4
    returns1[1] * returns[1] = -2*4 = -8
    returns1[2] * returns[0] = 0*2 = 0
    returns1[2] * returns[1] = 0*4 = 0
    returns1[3] * returns[0] = 3*2 = 6
    returns1[3] * returns[1] = 3*4 = 12
    Therefore, list of all products = [-14,-8,-8,-4,0,0,6,12]

 2. Since, k=2, The 6th smallest investment is 0.
*/
