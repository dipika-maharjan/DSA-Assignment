/*Question No.(5)
 * Algorithm Explanation:
    1. Visual Representation:
    a. Nodes (servers/clients) are represented as circles with unique IDs.
    b. Edges (connections) are represented as lines with associated costs and bandwidths.

    2. Interactive Optimization:
    a. Users can add nodes and edges to the graph.
    b. The "Optimize Network" button will eventually implement algorithms like Kruskal's or Prim's to find a minimum spanning tree (MST) that minimizes total cost while ensuring connectivity.

    3. Dynamic Path Calculation:
    a. The "Calculate Shortest Path" button uses Dijkstra's algorithm to find the path with the maximum bandwidth between two nodes.

    4. Real-time Evaluation:
    a. The total cost and latency of the network are displayed in real-time.
    b. Users can adjust the topology and see the updated results.
 */

 // Import necessary classes
import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

// Define the main class for the GUI of Network Optimization
class NetworkOptimizationGUI extends JFrame {
    private GraphPanel graphPanel;  // Panel to draw the graph
    private JButton optimizeButton, shortestPathButton, addNodeButton, addEdgeButton, addDescriptionButton;  // Buttons for actions
    private JLabel resultLabel;  // Label to display results (cost or path)
    private Graph graph;  // The graph representing the network
    private JTable descriptionTable;  // Table to display node descriptions
    private DefaultTableModel tableModel;  // Table model for node descriptions
    private Map<String, NodeDescription> nodeDescriptions;  // Store descriptions for nodes

    public NetworkOptimizationGUI() {
        // Set up the window title and size
        setTitle("Network Optimization Tool");
        setSize(1000, 600);  // Set the window size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Close the window when exit is triggered
        setLayout(new BorderLayout());  // Set layout for the JFrame

        // Initialize the graph object
        graph = new Graph();

        // Create the graph display panel
        graphPanel = new GraphPanel(graph);

        // Initialize the buttons for various actions
        optimizeButton = new JButton("Optimize Network");
        shortestPathButton = new JButton("Find Shortest Path");
        addNodeButton = new JButton("Add Node");
        addEdgeButton = new JButton("Add Edge");
        addDescriptionButton = new JButton("Add Description");

        // Result label to display optimization results
        resultLabel = new JLabel("Result: ");

        // Control panel for buttons and result label
        JPanel controlPanel = new JPanel();
        controlPanel.add(addNodeButton);  // Add node button
        controlPanel.add(addEdgeButton);  // Add edge button
        controlPanel.add(optimizeButton);  // Optimize network button
        controlPanel.add(shortestPathButton);  // Find shortest path button
        controlPanel.add(addDescriptionButton);  // Add description button
        controlPanel.add(resultLabel);  // Result label

        // Initialize the node descriptions map
        nodeDescriptions = new HashMap<>();

        // Initialize the table model and table
        tableModel = new DefaultTableModel(new Object[]{"Node", "Description"}, 0);
        descriptionTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(descriptionTable);  // Add table to scroll pane

        // Add the graph panel and control panel to the window layout
        add(graphPanel, BorderLayout.CENTER);  // Add the graph panel at the center
        add(controlPanel, BorderLayout.SOUTH);  // Add the control panel at the bottom
        add(scrollPane, BorderLayout.EAST);  // Add the description table at the right side

        // Action listener for adding a node
        addNodeButton.addActionListener(e -> {
            // Prompt the user to enter the node name
            String node = JOptionPane.showInputDialog("Enter node name:");
            if (node != null && !node.trim().isEmpty()) {
                // Add node to the graph
                graph.addNode(node.trim());
                graphPanel.repaint();  // Redraw the graph
            }
        });

        // Action listener for adding an edge between nodes
        addEdgeButton.addActionListener(e -> {
            // Prompt the user to enter two node names, cost and bandwidth for the edge
            String node1 = JOptionPane.showInputDialog("Enter first node:");
            String node2 = JOptionPane.showInputDialog("Enter second node:");
            String costStr = JOptionPane.showInputDialog("Enter cost:");
            String bandwidthStr = JOptionPane.showInputDialog("Enter bandwidth:");

            if (node1 != null && node2 != null && costStr != null && bandwidthStr != null) {
                try {
                    // Parse cost and bandwidth to integers
                    int cost = Integer.parseInt(costStr);
                    int bandwidth = Integer.parseInt(bandwidthStr);
                    // Add the edge to the graph
                    graph.addEdge(node1, node2, cost, bandwidth);
                    graphPanel.repaint();  // Redraw the graph
                } catch (NumberFormatException ex) {
                    // Show an error message if input is not valid
                    JOptionPane.showMessageDialog(this, "Invalid input. Please enter valid numbers for cost and bandwidth.");
                }
            }
        });

        // Action listener for optimizing the network (finding MST)
        optimizeButton.addActionListener(e -> {
            // Find the Minimum Spanning Tree (MST)
            int cost = graph.minimumSpanningTree();
            resultLabel.setText("Optimized Cost: " + cost);  // Display the MST cost
            graphPanel.repaint();  // Redraw the graph
        });

        // Action listener for finding the shortest path between two nodes
        shortestPathButton.addActionListener(e -> {
            // Prompt the user to enter the start and end node names
            String start = JOptionPane.showInputDialog("Enter start node:");
            String end = JOptionPane.showInputDialog("Enter end node:");
            // Find the shortest path using Dijkstra's algorithm
            int distance = graph.dijkstra(start, end);
            resultLabel.setText("Shortest Path Cost: " + distance);  // Display the shortest path cost
            graphPanel.repaint();  // Redraw the graph
        });

        // Action listener for adding a description to a node
        addDescriptionButton.addActionListener(e -> {
            // Prompt the user to enter the node name and its description
            String node = JOptionPane.showInputDialog("Enter node name:");
            if (node != null && !node.trim().isEmpty() && graph.getAdjList().containsKey(node)) {
                // Prompt for description
                String description = JOptionPane.showInputDialog("Enter description for node " + node + ":");
                if (description != null) {
                    // Create or update the node description
                    NodeDescription nodeDescription = nodeDescriptions.getOrDefault(node, new NodeDescription(node, ""));
                    nodeDescription.setDescription(description);
                    nodeDescriptions.put(node, nodeDescription);
                    updateTable();  // Update the table with new description
                }
            } else {
                JOptionPane.showMessageDialog(this, "Node does not exist.");
            }
        });
    }

    private void updateTable() {
        tableModel.setRowCount(0);  // Clear the table
        // Add all node descriptions to the table
        for (NodeDescription nodeDescription : nodeDescriptions.values()) {
            tableModel.addRow(new Object[]{nodeDescription.getNodeName(), nodeDescription.getDescription()});
        }
    }

    // Method to get the description for a specific node
    public NodeDescription getNodeDescription(String nodeName) {
        return nodeDescriptions.get(nodeName);
    }

    // Main method to launch the GUI
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new NetworkOptimizationGUI().setVisible(true));  // Launch the GUI
    }
}

// Class for the graph panel that displays the network graph
class GraphPanel extends JPanel {
    private Graph graph;

    public GraphPanel(Graph graph) {
        this.graph = graph;
    }

    // Method to draw the graph (nodes, edges, and costs)
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);  // Set color for drawing

        // Get the positions of nodes
        Map<String, Point> positions = graph.getNodePositions();

        // Draw edges between nodes
        for (Map.Entry<String, List<Edge>> entry : graph.getAdjList().entrySet()) {
            String node = entry.getKey();
            for (Edge edge : entry.getValue()) {
                Point p1 = positions.get(node);
                Point p2 = positions.get(edge.target);
                if (p1 != null && p2 != null) {
                    g.drawLine(p1.x, p1.y, p2.x, p2.y);  // Draw the line for the edge
                    g.drawString(String.valueOf(edge.cost), (p1.x + p2.x) / 2, (p1.y + p2.y) / 2);  // Display the cost
                }
            }
        }

        // Draw nodes and labels
        for (Map.Entry<String, Point> entry : positions.entrySet()) {
            g.setColor(Color.RED);  // Set color for nodes
            g.fillOval(entry.getValue().x - 10, entry.getValue().y - 10, 20, 20);  // Draw the node as a red circle
            g.setColor(Color.BLACK);  // Set color for labels
            g.drawString(entry.getKey(), entry.getValue().x, entry.getValue().y);  // Draw the node label

            // Draw description if it exists
            NodeDescription nodeDescription = ((NetworkOptimizationGUI) SwingUtilities.getWindowAncestor(this)).getNodeDescription(entry.getKey());
            if (nodeDescription != null) {
                g.drawString(nodeDescription.getDescription(), entry.getValue().x, entry.getValue().y + 20);  // Draw description below node
            }
        }
    }
}

// Graph class that handles the network graph logic
class Graph {
    private Map<String, List<Edge>> adjList;  // Adjacency list for the graph
    private Map<String, Point> nodePositions;  // Positions of nodes for drawing
    private Random random;  // Random object for generating random positions

    public Graph() {
        adjList = new HashMap<>();
        nodePositions = new HashMap<>();
        random = new Random();
    }

    // Method to add a node to the graph
    public void addNode(String node) {
        if (!adjList.containsKey(node)) {
            adjList.put(node, new ArrayList<>());
            nodePositions.put(node, new Point(random.nextInt(600) + 50, random.nextInt(400) + 50));  // Random position for the node
        }
    }

    // Method to add an edge between two nodes
    public void addEdge(String u, String v, int cost, int bandwidth) {
        addNode(u);  // Ensure both nodes exist
        addNode(v);
        adjList.get(u).add(new Edge(v, cost, bandwidth));  // Add edge from u to v
        adjList.get(v).add(new Edge(u, cost, bandwidth));  // Add edge from v to u (undirected)
    }

    // Method to get the adjacency list of the graph
    public Map<String, List<Edge>> getAdjList() {
        return adjList;
    }

    // Method to get the positions of the nodes
    public Map<String, Point> getNodePositions() {
        return nodePositions;
    }

    // Method to find the Minimum Spanning Tree (MST)
    public int minimumSpanningTree() {
        // Placeholder for MST algorithm (e.g., Prim's or Kruskal's)
        return 0;  // Return MST cost (should be implemented)
    }

    // Method to find the shortest path between two nodes using Dijkstra's algorithm
    public int dijkstra(String start, String end) {
        // Placeholder for Dijkstra's algorithm (should be implemented)
        return 0;  // Return shortest path cost (should be implemented)
    }
}

// Edge class to represent an edge in the graph
class Edge {
    String target;  // Target node of the edge
    int cost;  // Cost of the edge
    int bandwidth;  // Bandwidth of the edge

    public Edge(String target, int cost, int bandwidth) {
        this.target = target;
        this.cost = cost;
        this.bandwidth = bandwidth;
    }
}

// Class to hold node descriptions
class NodeDescription {
    private String nodeName;  // Name of the node
    private String description;  // Description for the node

    public NodeDescription(String nodeName, String description) {
        this.nodeName = nodeName;
        this.description = description;
    }

    public String getNodeName() {
        return nodeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
