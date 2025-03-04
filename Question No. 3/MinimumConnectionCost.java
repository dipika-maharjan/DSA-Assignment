/* Question No.3 (a):
Determine the minimum total cost to connect all devices in the network.

Algorithm Explanation:
1. Treat each device as a node in a graph. Devices can either be connected via direct connections or by installing communication modules.
2. Introduce a virtual node representing the central communication hub. Connect this virtual node to all devices with edges equal to the module installation cost.
3. Combine all the given direct connections and the virtual edges into a single list.
4. Sort all edges based on their cost in ascending order.
5. Apply Kruskal's algorithm to find the Minimum Spanning Tree (MST), ensuring all devices are connected with the minimum total cost.
6. Use the Union-Find data structure to efficiently detect cycles during MST formation.
7. Stop adding edges when all devices are connected, i.e., when we have n edges in total (including virtual edges).
*/

import java.util.*; // Import necessary utilities

public class MinimumConnectionCost {
    // Class to represent an edge (connection between two devices)
    static class Edge {
        int device1, device2, cost;

        Edge(int d1, int d2, int c) {
            this.device1 = d1;
            this.device2 = d2;
            this.cost = c;
        }
    }

    // Union-Find (Disjoint Set) for cycle detection in Kruskal's algorithm
    static class UnionFind {
        int[] parent, rank;

        UnionFind(int size) {
            parent = new int[size]; // Parent array to track root of each set
            rank = new int[size]; // Rank array for union by rank
            for (int i = 0; i < size; i++) {
                parent[i] = i; // Initialize each node as its own parent
            }
        }

        int find(int x) { // Find operation with path compression
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // Recursively find root and compress path
            }
            return parent[x];
        }

        boolean union(int x, int y) { // Union operation to merge sets
            int rootX = find(x);
            int rootY = find(y);
            if (rootX == rootY) return false; // If already in the same set, no need to merge

            if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX; // Attach smaller tree to larger tree
            } else if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++; // Increase rank if merging two equal-rank trees
            }
            return true;
        }
    }

    public static int minNetworkCost(int n, int[] modules, int[][] connections) {
        if (n == 1) return modules[0]; // If only one device, return its module cost

        List<Edge> edges = new ArrayList<>(); // List to store all edges
        int virtualNode = n; // Virtual node representing module installation

        // Add direct connections to edge list (convert to 0-based index)
        for (int[] conn : connections) {
            edges.add(new Edge(conn[0] - 1, conn[1] - 1, conn[2]));
        }

        // Add virtual edges connecting each device to the virtual node (module cost)
        for (int i = 0; i < n; i++) {
            edges.add(new Edge(virtualNode, i, modules[i]));
        }

        // Sort edges by cost (Kruskal’s algorithm requirement)
        edges.sort(Comparator.comparingInt(e -> e.cost));

        UnionFind uf = new UnionFind(n + 1); // Initialize Union-Find for (n+1) nodes
        int totalCost = 0, edgesUsed = 0; // Variables to track MST cost and edge count

        for (Edge edge : edges) {
            if (uf.union(edge.device1, edge.device2)) { // If adding edge doesn't form cycle
                totalCost += edge.cost; // Add cost to total
                edgesUsed++; // Increase edge count
                if (edgesUsed == n) break; // Stop when MST is formed (n edges needed)
            }
        }

        return totalCost; // Return minimum total cost of connecting all devices
    }

    public static void main(String[] args) {
        int n = 3; // Number of devices
        int[] modules = {1, 2, 2}; // Module installation costs
        int[][] connections = {{1, 2, 1}, {2, 3, 1}}; // Direct connections with costs
        
        int result = minNetworkCost(n, modules, connections); // Compute min cost
        System.out.println("Minimum total cost: " + result); // Output result
    }
}


/*
Output:
Case 1:
Enter number of devices: 3
Enter module installation costs (comma separated): 1, 2, 2
Enter number of connections: 2
Enter connections in format 'device1 device2 cost' per line:
1 2 1
2 0 1
Minimum total cost to connect all devices: 3

Case 2:
Enter number of devices: 4
Enter module installation costs (comma separated): 4, 2, 3, 5
Enter number of connections: 4
Enter connections in format 'device1 device2 cost' per line:
0 1 1
1 2 2
2 3 3
0 3 4
Minimum total cost to connect all devices: 8
*/
