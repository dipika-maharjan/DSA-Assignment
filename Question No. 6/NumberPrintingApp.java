/*Question no. 6(a)
 * Create a ThreadController class that coordinates three threads.
 * 
 * Algorithm Explanation:
 *  1. Threads Creation: We create three threads: ZeroThread, EvenThread, and OddThread.
    2. Synchronization: We use a lock and condition variables to ensure only one thread prints at a time.
    3. Printing Order:
        a. ZeroThread prints 0 first.
        b. OddThread prints the next odd number.
        c. ZeroThread prints 0 again.
        d. EvenThread prints the next even number.
        e. This continues until n is reached.
 */

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// Class responsible for printing numbers
class NumberPrinter {
    public void printZero() { System.out.print(0); }  // Prints 0
    public void printEven(int num) { System.out.print(num); }  // Prints even numbers
    public void printOdd(int num) { System.out.print(num); }  // Prints odd numbers
}

// ThreadController manages synchronization between three threads
class ThreadController {
    private int n; // Maximum number to print
    private int currentStep = 0; // Keeps track of sequence order
    private final Lock lock = new ReentrantLock(); // Lock to manage synchronization
    private final Condition zeroCondition = lock.newCondition(); // Condition for ZeroThread
    private final Condition oddCondition = lock.newCondition(); // Condition for OddThread
    private final Condition evenCondition = lock.newCondition(); // Condition for EvenThread
    private NumberPrinter printer; // Reference to NumberPrinter class

    // Constructor initializes 'n' and the printer instance
    public ThreadController(int n, NumberPrinter printer) {
        this.n = n;
        this.printer = printer;
    }

    // Zero thread prints '0' before each number and signals the next thread
    public void printZero() {
        for (int i = 1; i <= n; i++) { // Runs 'n' times
            lock.lock(); // Acquire lock for synchronization
            try {
                while (currentStep % 2 != 0) { // Wait if it's not ZeroThread's turn
                    zeroCondition.await();
                }
                printer.printZero(); // Print 0
                currentStep++; // Move to the next step
                if (i % 2 == 1) {
                    oddCondition.signal(); // Wake up OddThread for odd numbers
                } else {
                    evenCondition.signal(); // Wake up EvenThread for even numbers
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock(); // Release lock
            }
        }
    }

    // Odd number thread prints odd numbers and signals ZeroThread
    public void printOdd() {
        for (int i = 1; i <= n; i += 2) { // Iterates over odd numbers
            lock.lock(); // Acquire lock
            try {
                while (currentStep % 2 == 0) { // Wait until ZeroThread has printed 0
                    oddCondition.await();
                }
                printer.printOdd(i); // Print odd number
                currentStep++; // Move to the next step
                zeroCondition.signal(); // Wake up ZeroThread to print 0 again
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock(); // Release lock
            }
        }
    }

    // Even number thread prints even numbers and signals ZeroThread
    public void printEven() {
        for (int i = 2; i <= n; i += 2) { // Iterates over even numbers
            lock.lock(); // Acquire lock
            try {
                while (currentStep % 2 == 0) { // Wait until ZeroThread has printed 0
                    evenCondition.await();
                }
                printer.printEven(i); // Print even number
                currentStep++; // Move to the next step
                zeroCondition.signal(); // Wake up ZeroThread to print 0 again
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock(); // Release lock
            }
        }
    }
}

// Main class to execute the program
public class NumberPrintingApp {
    public static void main(String[] args) {
        int n = 5; // Set the maximum number
        NumberPrinter printer = new NumberPrinter(); // Create a printer instance
        ThreadController controller = new ThreadController(n, printer); // Create a thread controller instance

        // Create and start three threads
        Thread zeroThread = new Thread(controller::printZero);
        Thread oddThread = new Thread(controller::printOdd);
        Thread evenThread = new Thread(controller::printEven);

        zeroThread.start(); // Start ZeroThread
        oddThread.start(); // Start OddThread
        evenThread.start(); // Start EvenThread
    }
}


 /*Output:
 Example: If n = 5, the output should be "0102030405"
 1. We have three threads:
        ZeroThread → Prints 0 before each number
        OddThread → Prints odd numbers (1, 3, 5...)
        EvenThread → Prints even numbers (2, 4...)
2.  Breaking:
        First, print 0 by 'ZeroThread'
        Print 1 by 'OddThread'
        Print 0 by 'ZeroThread'
        Print 2 by 'EvenThread'
        Repeat until '5'.
Output: 0102030405
 */
 
