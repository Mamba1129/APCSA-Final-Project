import java.awt.*;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Maze {
    public static boolean solved = false; 
    public static void main(String[] args) {
        // First maze - original
        String[][] maze1 = {
            {"#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#"},
            {"#", "S", " ", " ", " ", "#", " ", " ", " ", " ", " ", " ", " ", " ", "#"},
            {"#", " ", "#", "#", " ", "#", " ", "#", "#", "#", "#", "#", "#", " ", "#"},
            {"#", " ", "#", " ", " ", " ", " ", " ", " ", " ", " ", " ", "#", " ", "#"},
            {"#", " ", "#", " ", "#", "#", "#", "#", "#", "#", "#", " ", "#", " ", "#"},
            {"#", " ", " ", " ", " ", " ", " ", " ", " ", " ", "#", " ", " ", " ", "#"},
            {"#", "#", "#", "#", "#", "#", "#", " ", "#", " ", "#", "#", "#", " ", "#"},
            {"#", " ", " ", " ", " ", " ", " ", " ", "#", " ", " ", " ", " ", " ", "#"},
            {"#", " ", "#", "#", "#", " ", "#", "#", "#", "#", "#", "#", "#", " ", "#"},
            {"#", " ", "#", " ", " ", " ", " ", " ", " ", " ", " ", " ", "#", " ", "#"},
            {"#", " ", "#", " ", "#", "#", "#", "#", "#", "#", "#", " ", "#", " ", "#"},
            {"#", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", "#", " ", "#"},
            {"#", " ", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", " ", "#"},
            {"#", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", "F", "#"},
            {"#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#"}
        };
        
        // Second maze - designed to show difference between BFS and Greedy
        String[][] maze2 = {
            {"#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#"},
            {"#", "S", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", "#"},
            {"#", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", "#"},
            {"#", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", "#"},
            {"#", " ", " ", " ", " ", " ", " ", " ", "#", "#", "#", "#", "#", " ", "#"},
            {"#", " ", " ", " ", " ", " ", " ", " ", "#", " ", " ", " ", " ", " ", "#"},
            {"#", " ", " ", " ", " ", " ", " ", " ", "#", " ", " ", " ", " ", " ", "#"},
            {"#", " ", " ", " ", " ", " ", " ", " ", "#", " ", " ", " ", " ", " ", "#"},
            {"#", " ", " ", " ", " ", " ", " ", " ", "#", " ", " ", " ", " ", " ", "#"},
            {"#", " ", " ", " ", " ", " ", " ", " ", "#", " ", " ", " ", " ", " ", "#"},
            {"#", " ", " ", " ", " ", " ", " ", " ", "#", " ", " ", " ", " ", " ", "#"},
            {"#", " ", " ", " ", " ", " ", " ", " ", "#", " ", " ", " ", " ", " ", "#"},
            {"#", " ", " ", " ", " ", " ", " ", " ", "#", " ", " ", " ", " ", " ", "#"},
            {"#", " ", " ", " ", " ", " ", " ", " ", "#", " ", " ", " ", " ", "F", "#"},
            {"#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#", "#"}
        };
        
        // Choose which maze to use (change between maze1 and maze2)
        String[][] maze = maze1;
        int y = 0;
        int x = 0;
        final int y_start;
        final int x_start;
        final int y_finish;
        final int x_finish;
        
        // Find start and finish positions
        int tempYStart = 0, tempXStart = 0, tempYFinish = 0, tempXFinish = 0;
        for (String[] row : maze) {
            for (String cell : row) {
                if (cell.equals("S")) {
                    tempXStart = x;
                    tempYStart = y;
                } else if (cell.equals("F")) {
                    tempXFinish = x;
                    tempYFinish = y;
                }
                x++;
            }
            x = 0;
            y++;
        }
        x_start = tempXStart;
        y_start = tempYStart;
        x_finish = tempXFinish;
        y_finish = tempYFinish;   

        //The call of the path finding algothirm
        //Breadth_First_Search bfs = new Breadth_First_Search(maze);
        Greedy_First_Search bfs = new Greedy_First_Search(maze);

    class MyCanvas extends JPanel {

        private BufferedImage image;
        
        public MyCanvas() {
            try {
                image = ImageIO.read(new File("C:/Users/soimm/OneDrive/Documents/AP_Compscie_Maze_Solver/cursor_solver.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            // Run BFS in a separate thread so it doesn't block the UI
            new Thread(() -> {
                try {
                    Thread.sleep(500); // Wait for window to appear
                    bfs.Solve(x_start, y_start, x_finish, y_finish, this);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        @Override
        public void paintComponent(Graphics g) {

            super.paintComponent(g);

            // Create BFS instance once in constructor
            
            int cellSize = 50;
            int y = 0;
            int x = 0;
            int y_start = 0;
            int x_start = 0;
            int y_final = 0;
            int x_final = 0;
            for (String[] row : maze) {
                for (String cell : row) {
                    if (cell.equals("#")) {
                        g.setColor(Color.BLACK);
                    } else if (cell.equals("S")) {
                        g.setColor(Color.BLUE);
                        x_start = x;
                        y_start = y;
                    } else if (cell.equals("F")) {
                        g.setColor(Color.RED);
                        x_final = x;
                        y_final = y;
                    } else {
                        g.setColor(Color.WHITE);
                    }
                    g.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                    x++;
                }
                x = 0;
                y++;
            }   
            // Draw the cursor image at the start position
            g.drawImage(image, x_start * cellSize, y_start * cellSize, cellSize, cellSize, this);
            System.out.println("Drawing " + bfs.get_Curr_in_check().size() + " cyan cells");
            for (ArrayList<Integer> pos : bfs.get_Curr_in_check()) {
                int row = pos.get(0);
                int col = pos.get(1);
                g.setColor(Color.CYAN);
                g.fillRect(col * cellSize, row * cellSize, cellSize, cellSize);
            }
            for (ArrayList<Integer> pos : bfs.get_Checked()) {
                if (pos.get(0) != y_start || pos.get(1) != x_start) {
                    int row = pos.get(0);
                    int col = pos.get(1);
                    g.setColor(Color.GRAY);
                    g.fillRect(col * cellSize, row * cellSize, cellSize, cellSize);
                }
            }

            g.setColor(Color.RED);
            g.fillRect(x_final * cellSize, y_final * cellSize, cellSize, cellSize );
            
            // Draw red line through the path
            ArrayList<ArrayList<Integer>> path = bfs.get_Path();
            if (path.size() > 1) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(Color.RED);
                g2d.setStroke(new BasicStroke(3)); // 3 pixel wide line
                
                for (int i = 0; i < path.size() - 1; i++) {
                    int row1 = path.get(i).get(0);
                    int col1 = path.get(i).get(1);
                    int row2 = path.get(i + 1).get(0);
                    int col2 = path.get(i + 1).get(1);
                    
                    // Draw line from center of cell1 to center of cell2
                    int x1 = col1 * cellSize + cellSize / 2;
                    int y1 = row1 * cellSize + cellSize / 2;
                    int x2 = col2 * cellSize + cellSize / 2;
                    int y2 = row2 * cellSize + cellSize / 2;
                    
                    g2d.drawLine(x1, y1, x2, y2);
                }
            }
            
        }
    }

    // In your main method or constructor:
    JFrame frame = new JFrame("Maze");
    frame.setSize(maze[0].length * 50 + 14, maze.length * 50 + 37);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(new MyCanvas());
    frame.setVisible(true);

}
}
