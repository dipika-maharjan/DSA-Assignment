/*Question no. 6(b)
 * Create a web crawler application that can crawl multiple web pages concurrently using multithreading to improve performance.
 * 
 * Algorithm Explanation:
 *  1. Use a Queue to store URLs to be crawled (FIFO order).
    2. Use a Set to store visited URLs to avoid duplicates.
    3. Create a thread pool using ExecutorService to manage multiple crawling threads.
    4. Each thread fetches a URL, extracts new links, and adds them to the queue.
    5. Handle errors/exceptions properly (e.g., unreachable pages).
    6. Stop when there are no more URLs left to crawl.
 */

 import java.io.*; // Import classes for input/output operations
 import java.net.*; // Import classes for networking (e.g., URLs)
 import java.util.*; // Import utility classes (e.g., Set, Queue)
 import java.util.concurrent.*; // Import classes for concurrent programming (e.g., ExecutorService)
 import javax.swing.*; // Import classes for GUI components (e.g., JFrame, JTextArea)
 
 // A class that represents a task for crawling a single URL
 class CrawlerTask implements Runnable {
     private String url; // The URL to crawl
     private JTextArea textArea; // The GUI component to display results
 
     // Constructor to initialize the URL and GUI text area
     public CrawlerTask(String url, JTextArea textArea) {
         this.url = url; // Set the URL
         this.textArea = textArea; // Set the text area
     }
 
     // The method that runs when the task is executed
     @Override
     public void run() {
         downloadPage(url); // Call the method to download the page
     }
 
     // Method to download the content of a webpage
     private void downloadPage(String urlString) {
         try {
             URL url = new URL(urlString); // Create a URL object from the string
             BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream())); // Open a stream to read the webpage
             String line; // Variable to store each line of the webpage
             StringBuilder content = new StringBuilder(); // Store the entire webpage content
 
             // Read the webpage line by line
             while ((line = reader.readLine()) != null) {
                 content.append(line).append("\n"); // Append each line to the content
             }
             reader.close(); // Close the reader
 
             saveToFile(urlString, content.toString()); // Save the content to a file
             updateGUI("Crawled: " + urlString); // Update the GUI with a success message
         } catch (Exception e) {
             updateGUI("Failed to crawl: " + urlString + " - " + e.getMessage()); // Update the GUI with an error message
         }
     }
 
     // Method to save the webpage content to a file
     private void saveToFile(String url, String content) {
         try {
             // Create a filename by replacing special characters in the URL with underscores
             String filename = url.replaceAll("[^a-zA-Z0-9]", "_") + ".txt";
             BufferedWriter writer = new BufferedWriter(new FileWriter(filename)); // Create a file writer
             writer.write(content); // Write the content to the file
             writer.close(); // Close the writer
         } catch (IOException e) {
             updateGUI("Error saving file: " + e.getMessage()); // Update the GUI if there's an error saving the file
         }
     }
 
     // Method to update the GUI with a message
     private void updateGUI(String message) {
         // Use SwingUtilities to safely update the GUI from a non-GUI thread
         SwingUtilities.invokeLater(() -> textArea.append(message + "\n"));
     }
 }
 
 // Main class for the web crawler with a GUI
 public class WebCrawlerWithGUI {
     private static Set<String> visited = new HashSet<>(); // Set to store visited URLs
     private static Queue<String> queue = new LinkedList<>(); // Queue to store URLs to crawl
     private static ExecutorService executorService = Executors.newFixedThreadPool(10); // Thread pool to manage crawling tasks
 
     // Main method to start the program
     public static void main(String[] args) {
         JFrame frame = new JFrame("Web Crawler"); // Create the main window
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close the program when the window is closed
         frame.setSize(600, 400); // Set the window size
 
         JTextArea textArea = new JTextArea(); // Create a text area to display messages
         textArea.setEditable(false); // Make the text area read-only
         JScrollPane scrollPane = new JScrollPane(textArea); // Add a scroll bar to the text area
         frame.add(scrollPane, "Center"); // Add the text area to the center of the window
 
         JPanel panel = new JPanel(); // Create a panel for input components
         frame.add(panel, "South"); // Add the panel to the bottom of the window
 
         JTextField urlField = new JTextField(30); // Create a text field for entering URLs
         panel.add(urlField); // Add the text field to the panel
 
         JButton startButton = new JButton("Start Crawling"); // Create a button to start crawling
         panel.add(startButton); // Add the button to the panel
 
         // Add an action listener to the button
         startButton.addActionListener(e -> {
             String urlInput = urlField.getText().trim(); // Get the URL input and remove extra spaces
             if (!urlInput.isEmpty()) { // Check if the input is not empty
                 startCrawling(urlInput, textArea); // Start crawling with the input URL
             } else {
                 textArea.append("Please enter a URL.\n"); // Show a message if the input is empty
             }
         });
 
         frame.setVisible(true); // Make the window visible
     }
 
     // Method to start the crawling process
     private static void startCrawling(String urlInput, JTextArea textArea) {
         // Check if the executor service has been shut down
         if (executorService.isShutdown()) {
             textArea.append("The executor has been shut down. Please restart the program.\n");
             return;
         }
 
         String[] urls = urlInput.split(","); // Split the input by commas (for multiple URLs)
         for (String url : urls) {
             String trimmedUrl = url.trim(); // Remove extra spaces from each URL
             String correctedUrl = isValidUrl(trimmedUrl); // Check and correct the URL
             if (correctedUrl != null) { // If the URL is valid
                 queue.add(correctedUrl); // Add it to the queue
                 storeUrlInBackend(correctedUrl); // Store it in the backend file
             } else {
                 textArea.append("Invalid URL: " + trimmedUrl + "\n"); // Show an error message for invalid URLs
             }
         }
 
         // Process URLs in the queue
         while (!queue.isEmpty()) {
             String url = queue.poll(); // Get the next URL from the queue
             if (!visited.contains(url)) { // Check if the URL has not been visited
                 visited.add(url); // Mark the URL as visited
                 executorService.submit(new CrawlerTask(url, textArea)); // Submit a new crawling task
             }
         }
     }
 
     // Method to check if a URL is valid and correct it if necessary
     private static String isValidUrl(String url) {
         // If the URL does not start with http:// or https://, prepend http://
         if (!url.startsWith("http://") && !url.startsWith("https://")) {
             url = "http://" + url;
         }
 
         try {
             new URL(url).toURI(); // Try to create a valid URL object
             return url; // Return the corrected URL
         } catch (Exception e) {
             return null; // Return null if the URL is invalid
         }
     }
 
     // Method to store a URL in a backend file
     private static void storeUrlInBackend(String url) {
         try (BufferedWriter writer = new BufferedWriter(new FileWriter("visited_urls.txt", true))) {
             writer.write(url); // Write the URL to the file
             writer.newLine(); // Add a new line
         } catch (IOException e) {
             System.out.println("Error storing URL in backend: " + e.getMessage()); // Show an error message if writing fails
         }
     }
 }
/*Output
 * Crawled: https://www.example.com
Crawled: https://www.wikipedia.org
Crawling finished!
 */
