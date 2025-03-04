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

import java.io.*;
import java.net.*;
import java.util.*;

// Class A - Downloads content from a URL
class CrawlerA extends Thread {
    private String url; // URL to crawl

    public CrawlerA(String url) {
        this.url = url; // Assign URL
    }

    @Override
    public void run() {
        downloadPage(url); // Start downloading
    }

    private void downloadPage(String urlString) {
        try {
            URL url = new URL(urlString); // Create URL object
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream())); // Open stream
            String line;
            StringBuilder content = new StringBuilder(); // Store content

            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n"); // Read and append content
            }
            reader.close(); // Close reader

            saveToFile(urlString, content.toString()); // Save content to file
            System.out.println("Crawled: " + urlString); // Print success message
        } catch (Exception e) {
            System.out.println("Failed to crawl: " + urlString + " - " + e.getMessage()); // Print error message
        }
    }

    private void saveToFile(String url, String content) {
        try {
            String filename = url.replaceAll("[^a-zA-Z0-9]", "_") + ".txt"; // Create filename
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename)); // Open file writer
            writer.write(content); // Write content to file
            writer.close(); // Close writer
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage()); // Print error message
        }
    }
}

// Class B - Another thread for downloading a different URL
class CrawlerB extends Thread {
    private String url; // URL to crawl

    public CrawlerB(String url) {
        this.url = url; // Assign URL
    }

    @Override
    public void run() {
        downloadPage(url); // Start downloading
    }

    private void downloadPage(String urlString) {
        try {
            URL url = new URL(urlString); // Create URL object
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream())); // Open stream
            String line;
            StringBuilder content = new StringBuilder(); // Store content

            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n"); // Read and append content
            }
            reader.close(); // Close reader

            saveToFile(urlString, content.toString()); // Save content to file
            System.out.println("Crawled: " + urlString); // Print success message
        } catch (Exception e) {
            System.out.println("Failed to crawl: " + urlString + " - " + e.getMessage()); // Print error message
        }
    }

    private void saveToFile(String url, String content) {
        try {
            String filename = url.replaceAll("[^a-zA-Z0-9]", "_") + ".txt"; // Create filename
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename)); // Open file writer
            writer.write(content); // Write content to file
            writer.close(); // Close writer
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage()); // Print error message
        }
    }
}

// Main class to start the crawler
public class WebCrawler {
    public static void main(String[] args) {
        List<String> urls = Arrays.asList(
                "https://www.example.com", // First URL
                "https://www.wikipedia.org" // Second URL
        );

        // Creating threads for each URL
        CrawlerA crawlerA = new CrawlerA(urls.get(0)); // Thread for first URL
        CrawlerB crawlerB = new CrawlerB(urls.get(1)); // Thread for second URL

        // Start threads
        crawlerA.start(); // Start first thread
        crawlerB.start(); // Start second thread

        // Wait for threads to complete
        try {
            crawlerA.join(); // Wait for first thread
            crawlerB.join(); // Wait for second thread
        } catch (InterruptedException e) {
            e.printStackTrace(); // Print error if interrupted
        }

        System.out.println("Crawling finished!"); // Print completion message
    }
}

/*Output
 * Crawled: https://www.example.com
Crawled: https://www.wikipedia.org
Crawling finished!
 */
