/* Question no.2 (b):
Determine the lexicographically pair of points with the smallest distance and smallest distance calculated 
using  x_coords [i] - x_coords [j]| + | y_coords [i] - y_coords [j]|

Algorithm Explanation:
    1. Iterate through all possible pairs of points (i, j) where i != j.
    2. For each pair, calculate the Manhattan distance using:
       |x_coords[i] - x_coords[j]| + |y_coords[i] - y_coords[j]|
    3. Track the pair with the smallest distance.
    4. If two pairs have the same distance, choose the lexicographically smaller pair:
       - Compare indices: first by i, then by j if i's are equal.
    5. Finally, output the indices of the closest pair.
     */


    import java.util.*; 

    public class ClosestPair {
        public static int[] findClosestPair(int[] x_coords, int[] y_coords) {
            int n = x_coords.length; // Get the number of points
            int minDistance = Integer.MAX_VALUE; // Initialize the minimum distance with a very large value
            int[] minPair = new int[2]; // Array to store indices of the closest pair
    
            // Iterate over all pairs of points
            for (int i = 0; i < n; i++) { // Outer loop for selecting the first point
                for (int j = 0; j < n; j++) { // Inner loop for selecting the second point
                    if (i != j) { // Ensure we don't compare a point with itself
                        // Calculate the Manhattan distance between two points
                        int distance = Math.abs(x_coords[i] - x_coords[j]) + Math.abs(y_coords[i] - y_coords[j]);
    
                        // If a new minimum distance is found, update minDistance and store the pair
                        if (distance < minDistance) { 
                            minDistance = distance; // Update minimum distance
                            minPair[0] = i; // Store index i
                            minPair[1] = j; // Store index j
                        } else if (distance == minDistance) { // If distances are equal, check lexicographical order
                            // Choose the lexicographically smaller pair
                            if (i < minPair[0] || (i == minPair[0] && j < minPair[1])) {
                                minPair[0] = i; // Update i index
                                minPair[1] = j; // Update j index
                            }
                        }
                    }
                }
            }
            return minPair; // Return the closest pair of points
        }
    
        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in); // Create a Scanner object for user input
    
            // Prompt the user to enter x-coordinates
            System.out.print("Enter the x-coordinates: ");
            String[] xInput = scanner.nextLine().split(","); // Read input, split by commas
            int[] x_coords = new int[xInput.length]; // Create an array to store x-coordinates
            for (int i = 0; i < xInput.length; i++) { 
                x_coords[i] = Integer.parseInt(xInput[i].trim()); // Convert input to integer after trimming spaces
            }
    
            // Prompt the user to enter y-coordinates
            System.out.print("Enter the y-coordinates: ");
            String[] yInput = scanner.nextLine().split(","); // Read input, split by commas
            int[] y_coords = new int[yInput.length]; // Create an array to store y-coordinates
            for (int i = 0; i < yInput.length; i++) {
                y_coords[i] = Integer.parseInt(yInput[i].trim()); // Convert input to integer after trimming spaces
            }
    
            // Find the closest pair of points using the function
            int[] result = findClosestPair(x_coords, y_coords);
    
            // Output the indices of the closest pair of points
            System.out.println("The indices of the closest pair of points are: (" + result[0] + ", " + result[1] + ")");
    
            scanner.close(); // Close the scanner to prevent resource leaks
        }
    }
    
    /*Output:
    Case 1:
    Enter the x-coordinates: 1, 2, 3, 2, 4
    Enter the y-coordinates: 2, 3, 1, 2, 3
    The indices of the closest pair of points are: (0, 3)

    Case 2:
    Enter the x-coordinates: 1, 3, 5
    Enter the y-coordinates: 2, 4, 6
    The indices of the closest pair of points are: (0, 1)
     */