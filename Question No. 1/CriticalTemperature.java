/*Question no. 1(a)
 * Determine the minimum number of measurements required to find the critical temperature
 * 
 * Algorithm Explanation:
 * 1. Use a 2D array (dp[k+1][n+1]) to store the minimum measurements needed for k samples and n temperature levels.
 * 2. Base Cases:
    a. If there are zero temperature levels (n=0), no measurements are needed (dp[i][0] = 0).
    b. If there is only one sample (k=1), we must check each level from 1 to n linearly (dp[1][j] = j).
* 3. Filling the DP Table
    a. Loop through all samples (k) and temperature levels (n).
    b. Start with dp[i][j] = ∞ (worst case).
    c. Test at each temperature level x from 1 to j.
    d. Compute the worst-case scenario using: worstCase=max(dp[i−1][x−1],dp[i][j−x])
    e. Take the minimum number of moves by updating dp[i][j] with: dp[i][j]=min(dp[i][j],1+worstCase)
* 4. Return dp[k][n] as the final answer.
*/


public class CriticalTemperature {
    public static int minMeasurement(int k, int n){ //k=material sample and n=temperature level
            
        //int [][] as 2D array which stores elements in integer and k+1 for rows and n+1 for columns to handle base case
        int [][]dp = new int[k+1][n+1]; 

        //base cases
        for(int i=0; i<k; i++){
            dp[i][0]=0; //0 temperature level requires 0 measurements
        }
        for(int j=0; j<n; j++){
            dp[1][j]=j; //with 1 sample we need to test all temperature levels from 1 to n linearly
        }
        for(int i=2; i<=k; i++){ //loop iterates over no. of samples. Here, i=1 has already been done in base case so we started from i=2
            for(int j=1; j<=n; j++){ //loop iterates over no. of temp levels. Here, j=0 has already been done in base case so we started from j=1
                dp[i][j] = Integer.MAX_VALUE; //no measurements have been done yet, so it starts with highest possible value(infinity) and it gets updated as we find min. no. of measurement
                for(int x=1; x<=j; x++){ //loop iterates over each possible level to test each level one by one
                    int worstCase = Math.max(dp[i-1][x-1],dp[i][j-x]); //calculate worst case
                    dp[i][j]=Math.min(dp[i][j], 1+worstCase); //update the value in d[i][j] with minimum of previous result
                }
            }
        }
        return dp[k][n]; //return result for sample k and temperature n
    }

    public static void main(String[] args) {
        System.out.println("Minimum measurement (k=1, n=2): " + minMeasurement(1, 2)); //Output: 0
        System.out.println("Minimum measurement (k=2, n=6): " + minMeasurement(2, 6)); //Output: 3
        System.out.println("Minimum measurement (k=3, n=14): " + minMeasurement(3, 14)); //Output: 4
    }
    
}

/*Output:
* Example 1: Input: k = 1, n = 2
    Steps:
    1. Check at 1 → If it changes, f = 0.
    2. If it doesn't change, check at 2 → If it changes, f = 1. Otherwise, f = 2.
    Therefore, total measurements needed: 2

* Example 2: Input: k = 2, n = 6
    Steps:
    1. Check at level 2.
    2. If the sample breaks, f = 0 or 1, and we check sequentially below 2.
    3. If it doesn’t break, check at 4.
    4. If it breaks, we check sequentially from 3.
    5. If it doesn’t break, check at 6.
    6. This ensures that we minimize worst-case checks.
    Therefore, total measurements needed: 3

* Example 3: Input: k = 3, n = 14
    Steps:
    1. First test at level 4.
    2. If the sample breaks, check sequentially from 1 to 3.
    3. If it doesn’t break, check at level 7.
    4. If it breaks, check from 5 to 6.
    5. If it doesn’t break, check at level 10.
    6. Continue refining the search using a balanced strategy to minimize worst-case scenarios.
    Therefore, total measurements needed: 4
*/



