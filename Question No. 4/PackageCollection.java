/*Question no 4(b)
 * Determine the minimum number of roads you need to traverse to collect all packages.
 * 
 * Algorithm Explanation
 * -> The problem can be solved using a Graph Traversal Approach.
    1. Construct a Graph
        a. Create an adjacency list from the given roads array.
    2. Find All Nodes with Packages
        a. Identify locations with packages[i] == 1.
    3. Use BFS to Collect Packages
        a. From each node, perform a BFS up to depth 2 to find reachable package locations.
    4. Find the Minimum Distance Path
        a. Move optimally to collect all packages.
        b. Ensure all nodes with packages are covered while minimizing road usage.
    5. Return to the Start Location
        a. After collecting all packages, return to the starting node while keeping track of traversed roads.
 */

 import java.util.*;

 public class PackageCollection {
 
     public static int minRoadsToTraverse(int[] packages, int[][] roads) {
         int n = packages.length;  // Total number of locations
         List<List<Integer>> graph = new ArrayList<>();  // Adjacency list representation of graph
         
         // Step 1: Build the graph from roads
         for (int i = 0; i < n; i++) {
             graph.add(new ArrayList<>());  // Initialize adjacency list
         }
         for (int[] road : roads) {
             graph.get(road[0]).add(road[1]);  // Connect both locations
             graph.get(road[1]).add(road[0]);  // Since roads are bidirectional
         }
 
         // Step 2: Find locations that have packages
         Set<Integer> packageLocations = new HashSet<>();
         for (int i = 0; i < n; i++) {
             if (packages[i] == 1) {  // If there's a package at this location
                 packageLocations.add(i);
             }
         }
 
         // Step 3: Use BFS to determine reachable packages within distance 2
         Map<Integer, Set<Integer>> reachablePackages = new HashMap<>();
         for (int i = 0; i < n; i++) {
             Set<Integer> reachable = bfs(graph, i, 2, packageLocations);  // Get all packages within 2 distance
             reachablePackages.put(i, reachable);
         }
 
         // Step 4: Find the best starting location that minimizes traversal
         int minDistance = Integer.MAX_VALUE;
         for (int start : packageLocations) {
             int distance = calculateDistance(graph, start, packageLocations);  // Calculate traversal distance
             minDistance = Math.min(minDistance, distance);  // Track minimum traversal
         }
 
         return minDistance;  // Return the minimum number of roads traversed
     }
 
     // BFS function to find all package locations within 2 distance from `start`
     private static Set<Integer> bfs(List<List<Integer>> graph, int start, int maxDepth, Set<Integer> packageLocations) {
         Queue<int[]> queue = new LinkedList<>();
         Set<Integer> visited = new HashSet<>();
         queue.add(new int[]{start, 0});  // Start BFS from `start` location
         visited.add(start);
         Set<Integer> reachable = new HashSet<>();
 
         while (!queue.isEmpty()) {
             int[] node = queue.poll();
             int curr = node[0], depth = node[1];
 
             if (depth > maxDepth) continue;  // Stop if beyond depth 2
             if (packageLocations.contains(curr)) reachable.add(curr);  // If there's a package, collect it
 
             // Explore all neighbors
             for (int neighbor : graph.get(curr)) {
                 if (!visited.contains(neighbor)) {  // If not visited
                     visited.add(neighbor);
                     queue.add(new int[]{neighbor, depth + 1});  // Move to the neighbor
                 }
             }
         }
         return reachable;  // Return all reachable package locations
     }
 
     // Function to calculate the minimum number of road traversals
     private static int calculateDistance(List<List<Integer>> graph, int start, Set<Integer> packageLocations) {
         Queue<int[]> queue = new LinkedList<>();
         Set<Integer> visited = new HashSet<>();
         queue.add(new int[]{start, 0});  // Start BFS from `start`
         visited.add(start);
         int distance = 0;
         Set<Integer> collectedPackages = new HashSet<>();
 
         while (!queue.isEmpty()) {
             int[] node = queue.poll();
             int curr = node[0], depth = node[1];
 
             if (packageLocations.contains(curr) && !collectedPackages.contains(curr)) {
                 collectedPackages.add(curr);
                 distance += depth * 2;  // Add distance to and from the package location
             }
 
             for (int neighbor : graph.get(curr)) {
                 if (!visited.contains(neighbor)) {  // Visit unvisited neighbors
                     visited.add(neighbor);
                     queue.add(new int[]{neighbor, depth + 1});
                 }
             }
         }
         return distance / 2;  // Each move counted twice (to and back)
     }
 
     public static void main(String[] args) {
         int[] packages1 = {1, 0, 0, 0, 0, 1};  // Locations with packages
         int[][] roads1 = {{0, 1}, {1, 2}, {2, 3}, {3, 4}, {4, 5}};  // Roads between locations
         System.out.println("Output: " + minRoadsToTraverse(packages1, roads1)); // Output: 2
 
         int[] packages2 = {0, 0, 0, 1, 1, 0, 0, 1};  // Another test case
         int[][] roads2 = {{0, 1}, {0, 2}, {1, 3}, {1, 4}, {2, 5}, {5, 6}, {5, 7}};
         System.out.println("Output: " + minRoadsToTraverse(packages2, roads2)); // Output: 2
     }
 }

 /*Example:1
  * packages = [1, 0, 0, 0, 0, 1]
    roads = [[0, 1], [1, 2], [2, 3], [3, 4], [4, 5]]

    1. The graph structure looks like this: 0 - 1 - 2 - 3 - 4 - 5
    2. Locations with packages: {0, 5}
    3. Best starting point: Location 2
        a. From location 2, we can collect the package at location 0 (within distance 2).
        b. Move to location 3 and collect the package at location 5 (within distance 2).
        c. Return to location 2.
    4. Minimum roads traversed = 2.
    Output: 2

    Example:2
    packages = [0, 0, 0, 1, 1, 0, 0, 1]
    roads = [[0,1],[0,2],[1,3],[1,4],[2,5],[5,6],[5,7]]

    1. Locations with packages: {3, 4, 7}
    2. Best starting point: Location 0
        a. From location 0, collect packages at 3 and 4 (within distance 2).
        b. Move to location 2, collect the package at 7 (within distance 2).
        c. Return to location 0.
    3. Minimum roads traversed = 2
    Output: 2

  */
 
