/*Question No.3 (b)
 * Algorithm Explanation:
 * 1. Initialize Game Components
    a. Create an empty game board (2D array).
    b. Create a queue to store the sequence of falling blocks.
    c. Create a stack to store placed blocks.
    d. Generate a random block and enqueue it.

    2. Game Loop
    a. Check if the game is over.
    b. Display the game state.
    c. Handle user input (move left, right, rotate).
    d. Move the block down if possible; otherwise:
    e. Place it on the board.
    f. Check for completed rows and clear them.
    g. Generate a new block.

    3. Game Over
    a. Display a message and final score.
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

class Block {
    int[][] shape; // 2D array to store the block's shape
    int x, y;      // Position of the block on the board

    Block(int[][] shape, int x, int y) {
        this.shape = shape;
        this.x = x;
        this.y = y;
    }
}

class GameBoard {
    int rows, cols;
    int[][] grid; // 2D array to represent the game board

    GameBoard(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new int[rows][cols]; // Initialize the grid
    }

    void initializeBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = 0; // 0 represents an empty cell
            }
        }
    }
}

public class TetrisGame extends JPanel implements ActionListener, KeyListener {
    static final int[][][] BLOCK_SHAPES = {
        {{1, 1, 1, 1}}, // I-block
        {{1, 1}, {1, 1}}, // O-block
        {{1, 1, 1}, {0, 1, 0}}, // T-block
        // Add more shapes as needed
    };

    static final int TILE_SIZE = 30; // Size of each tile in pixels
    static final int BOARD_ROWS = 20; // Number of rows in the game board
    static final int BOARD_COLS = 10; // Number of columns in the game board

    GameBoard board; // Game board
    Block currentBlock; // Current falling block
    Timer timer; // Timer for game loop
    Random random; // Random generator for blocks

    public TetrisGame() {
        board = new GameBoard(BOARD_ROWS, BOARD_COLS); // Initialize game board
        board.initializeBoard(); // Clear the board
        random = new Random(); // Initialize random generator
        currentBlock = generateRandomBlock(); // Generate the first block

        timer = new Timer(500, this); // Timer for game loop (500ms delay)
        timer.start(); // Start the timer

        setPreferredSize(new Dimension(BOARD_COLS * TILE_SIZE, BOARD_ROWS * TILE_SIZE)); // Set panel size
        setBackground(Color.BLACK); // Set background color
        setFocusable(true); // Allow panel to receive key events
        addKeyListener(this); // Add key listener for user input
    }

    Block generateRandomBlock() {
        int[][] shape = BLOCK_SHAPES[random.nextInt(BLOCK_SHAPES.length)]; // Randomly select a shape
        int x = 0, y = BOARD_COLS / 2 - 1; // Start at the top-middle of the board
        return new Block(shape, x, y);
    }

    boolean canMove(Block block, int newX, int newY) {
        for (int i = 0; i < block.shape.length; i++) {
            for (int j = 0; j < block.shape[0].length; j++) {
                if (block.shape[i][j] == 1) { // Check only filled parts of the block
                    int x = newX + i;
                    int y = newY + j;
                    if (x < 0 || x >= BOARD_ROWS || y < 0 || y >= BOARD_COLS || board.grid[x][y] != 0) {
                        return false; // Collision detected
                    }
                }
            }
        }
        return true; // No collision
    }

    void placeBlock(Block block) {
        for (int i = 0; i < block.shape.length; i++) {
            for (int j = 0; j < block.shape[0].length; j++) {
                if (block.shape[i][j] == 1) {
                    board.grid[block.x + i][block.y + j] = 1; // Place the block on the board
                }
            }
        }
        checkForCompletedRows(); // Check for completed rows
        currentBlock = generateRandomBlock(); // Generate a new block
    }

    void checkForCompletedRows() {
        for (int i = 0; i < BOARD_ROWS; i++) {
            boolean isRowComplete = true;
            for (int j = 0; j < BOARD_COLS; j++) {
                if (board.grid[i][j] == 0) {
                    isRowComplete = false; // Row is not complete
                    break;
                }
            }
            if (isRowComplete) {
                removeRow(i); // Remove the completed row
                i--; // Check the same row again after shifting
            }
        }
    }

    void removeRow(int row) {
        for (int i = row; i > 0; i--) {
            for (int j = 0; j < BOARD_COLS; j++) {
                board.grid[i][j] = board.grid[i - 1][j]; // Shift rows down
            }
        }
        for (int j = 0; j < BOARD_COLS; j++) {
            board.grid[0][j] = 0; // Clear the top row
        }
    }

    boolean isGameOver() {
        for (int j = 0; j < BOARD_COLS; j++) {
            if (board.grid[0][j] != 0) {
                return true; // Game over if the top row is filled
            }
        }
        return false;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // Clear the panel
        for (int i = 0; i < BOARD_ROWS; i++) {
            for (int j = 0; j < BOARD_COLS; j++) {
                if (board.grid[i][j] != 0) {
                    g.setColor(Color.CYAN); // Set color for filled cells
                    g.fillRect(j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE); // Draw filled cell
                }
            }
        }
        for (int i = 0; i < currentBlock.shape.length; i++) {
            for (int j = 0; j < currentBlock.shape[0].length; j++) {
                if (currentBlock.shape[i][j] == 1) {
                    g.setColor(Color.ORANGE); // Set color for the current block
                    g.fillRect((currentBlock.y + j) * TILE_SIZE, (currentBlock.x + i) * TILE_SIZE, TILE_SIZE, TILE_SIZE); // Draw block
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isGameOver()) {
            timer.stop(); // Stop the timer if the game is over
            JOptionPane.showMessageDialog(this, "Game Over!"); // Show game over message
        } else {
            if (canMove(currentBlock, currentBlock.x + 1, currentBlock.y)) {
                currentBlock.x++; // Move the block down
            } else {
                placeBlock(currentBlock); // Place the block on the board
            }
            repaint(); // Redraw the panel
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT: // Move left
                if (canMove(currentBlock, currentBlock.x, currentBlock.y - 1)) {
                    currentBlock.y--;
                }
                break;
            case KeyEvent.VK_RIGHT: // Move right
                if (canMove(currentBlock, currentBlock.x, currentBlock.y + 1)) {
                    currentBlock.y++;
                }
                break;
            case KeyEvent.VK_DOWN: // Move down
                if (canMove(currentBlock, currentBlock.x + 1, currentBlock.y)) {
                    currentBlock.x++;
                }
                break;
            case KeyEvent.VK_UP: // Rotate
                int[][] rotatedShape = rotateShape(currentBlock.shape);
                if (canMove(new Block(rotatedShape, currentBlock.x, currentBlock.y), currentBlock.x, currentBlock.y)) {
                    currentBlock.shape = rotatedShape;
                }
                break;
        }
        repaint(); // Redraw the panel
    }

    int[][] rotateShape(int[][] shape) {
        int rows = shape.length, cols = shape[0].length;
        int[][] rotated = new int[cols][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                rotated[j][rows - 1 - i] = shape[i][j];
            }
        }
        return rotated;
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Tetris"); // Create the main window
        TetrisGame tetris = new TetrisGame(); // Create the game panel
        frame.add(tetris); // Add the game panel to the window
        frame.pack(); // Resize the window to fit the panel
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close the program when the window is closed
        frame.setVisible(true); // Make the window visible
    }
}
