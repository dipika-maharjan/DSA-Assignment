/*Question no. 2(a) 
 * Determine the minimum number of rewards you need to distribute to the employees.

 * Algorithm Explanation:
 *  1. We need to distribute rewards to employees such that:
        a. Every employee gets at least one reward.
        b. Employees with higher ratings than their adjacent colleagues get more rewards.
    2. To achieve this, we use a two-pass greedy approach:
        a. First pass (left to right): Ensure that employees with higher ratings than the left neighbor get more rewards.
        b. Second pass (right to left): Ensure that employees with higher ratings than the right neighbor get more rewards.
*/

import java.util.*;

public class MinRewards {
    public static int minRewards(int[] ratings) { //Defines the method minRewards, which takes an array ratings and returns the minimum number of rewards.
        int n = ratings.length; // Initializes n to store the number of employees
        int[] rewards = new int[n]; //Creates an array rewards of size n, where each element will represent the reward assigned to each employee.

        // Step 1: Assign 1 reward to each employee initially
        Arrays.fill(rewards, 1); 

        // Step 2: Left-to-right pass
        for (int i = 1; i < n; i++) {
            if (ratings[i] > ratings[i - 1]) { //Compares the current employee’s rating (ratings[i]) with the previous employee’s rating (ratings[i - 1]).
                rewards[i] = rewards[i - 1] + 1; //If the current employee's rating is higher, increase their reward to be one more than the previous employee's reward.
            }
        }

        // Step 3: Right-to-left pass
        for (int i = n - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) { //Compares the current employee’s rating (ratings[i]) with the next employee’s rating (ratings[i + 1]).
                rewards[i] = Math.max(rewards[i], rewards[i + 1] + 1); // If the current employee's rating is higher, ensures their reward is greater than the next employee’s reward, but takes the maximum of the current reward and the new value (rewards[i + 1] + 1).
            }
        }

        // Step 4: Sum up rewards
        int totalRewards = 0; // Initializes totalRewards to 0, which will store the total number of rewards.
        for (int reward : rewards) { //Loops through the rewards array.
            totalRewards += reward; //Adds each employee’s reward to the totalRewards.
        }

        return totalRewards; //Returns the total number of rewards distributed.
    }

    public static void main(String[] args) {
        int[] ratings1 = {1, 0, 2};
        System.out.println("Output: " + minRewards(ratings1)); // Output: 5

        int[] ratings2 = {1, 2, 2};
        System.out.println("Output: " + minRewards(ratings2)); // Output: 4
    }
}

/*Output:
 * Example 1: Input: ratings = [1, 0, 2]
 * Steps:
 * 1. Initialize rewards; ratings  = [1,  0,  2] and rewards  = [1,  1,  1]
 * 2. Left-to-right pass
    a. Compare ratings[1] = 0 with ratings[0] = 1 → No change (0 is not greater).
    b. Compare ratings[2] = 2 with ratings[1] = 0 → Increase reward:
* 3. Right-to-left pass
    a. Compare ratings[1] = 0 with ratings[2] = 2 → No change (0 is smaller).
    b. Compare ratings[0] = 1 with ratings[1] = 0 → Increase reward:
* 4. Sum of rewards: 2 + 1 + 2 = 5

 * Example 2: Input: ratings = [1, 2, 2]
 * Steps:
 * 1. Initialize rewards; ratings  = [1,  2,  2] and rewards  = [1,  1,  1]
 * 2. Left-to-right pass
    a. Compare ratings[1] = 2 with ratings[0] = 1 → Increase reward:
    b. Compare ratings[2] = 2 with ratings[1] = 2 → No change (not greater).
* 3. Right-to-left pass
    a. Compare ratings[1] = 2 with ratings[2] = 2 → No change.
    b. Compare ratings[0] = 1 with ratings[1] = 2 → No change.
* 4. Sum of rewards: 1 + 2 + 1 = 4
 */