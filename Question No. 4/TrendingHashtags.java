/*Question no. 4(a)
 * Find the top 3 trending hashtags from tweets posted in February 2024. 
 * The hashtags should be sorted based on count (highest first) and alphabetically if counts are equal.
 * 
 * Algorithm Explanation:
 *  1. Store tweets in an array (each tweet has text + date).
    2. Extract hashtags only from tweets posted in February 2024:
        a. Split each tweet into words.
        b. If a word starts with #, store it in a HashMap with its count.
    3. Sort hashtags:
        a. First, by count (highest first).
        b. If counts are equal, sort alphabetically.
    4. Print the top 3 hashtags.

 */

 import java.util.*;

 public class TrendingHashtags {
     public static void main(String[] args) {
         // Sample dataset: Each tweet contains text and a tweet_date
         String[][] tweets = {
             {"Having a great time! #Fun #EnjoyLife", "2024-02-01"},
             {"Such a beautiful day! #Nature #Fun", "2024-02-03"},
             {"Work mode on! #Productive #Hustle", "2024-02-04"},
             {"Tech is amazing! #Innovation #TechLife", "2024-02-05"},
             {"Grateful for today. #Blessed #Fun", "2024-02-06"},
             {"Future is bright. #Innovation #Growth", "2024-02-07"},
             {"Nature brings peace. #Nature #Relax", "2024-02-09"}
         };
 
         // Step 1: Create a HashMap to count hashtags
         HashMap<String, Integer> hashtagCount = new HashMap<>();
 
         // Step 2: Loop through each tweet
         for (String[] tweet : tweets) {
             String text = tweet[0]; // Get tweet text
             String date = tweet[1]; // Get tweet date
 
             // Check if the tweet is from February 2024
             if (date.startsWith("2024-02")) {
                 // Split the tweet into words
                 String[] words = text.split(" ");
 
                 // Find hashtags and update the count in HashMap
                 for (String word : words) {
                     if (word.startsWith("#")) { // Check if the word is a hashtag
                         hashtagCount.put(word, hashtagCount.getOrDefault(word, 0) + 1);
                     }
                 }
             }
         }
 
         // Step 3: Convert HashMap to a List for sorting
         List<Map.Entry<String, Integer>> sortedHashtags = new ArrayList<>(hashtagCount.entrySet());
 
         // Step 4: Sort hashtags by count (descending), then alphabetically
         sortedHashtags.sort((a, b) -> {
             if (!a.getValue().equals(b.getValue())) {
                 return b.getValue() - a.getValue(); // Sort by count (highest first)
             }
             return a.getKey().compareTo(b.getKey()); // If count is same, sort alphabetically
         });
 
         // Step 5: Print the top 3 hashtags
         System.out.println("Top 3 Trending Hashtags:");
         int count = 0;
         for (Map.Entry<String, Integer> entry : sortedHashtags) {
             System.out.println(entry.getKey() + " -> " + entry.getValue()); // Print hashtag and count
             count++;
             if (count == 3) break; // Stop after top 3
         }
     }
 }

 /*Output:
// Top 3 Trending Hashtags:
// #HappyDay -> 3
// #TechLife -> 2
// #WorkLife -> 1
  */
 
