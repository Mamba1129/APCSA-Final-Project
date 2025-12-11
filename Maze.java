import java.awt.*;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Maze {
    public static void main(String[] args) {
        String[][] maze = {
            {"#", "#", "#", "#", "#"},
            {"#", " ", " ", " ", "#"},
            {"#", " ", "#", " ", "#"},
            {" ", " ", "#", "S", "#"},
            {"#", " ", "#", " ", "#"},
            {"#", " ", "#", " ", "#"},
            {"#", " ", "#", " ", "#"},
            {"#", " ", "#", " ", "#"},
            {"#", " ", "#", " ", "#"},
            {"#", "#", "#", "#", "#"},
        };

    class MyCanvas extends JPanel {

        private BufferedImage image;

        public MyCanvas() {
            try {
                image = ImageIO.read(new File("C:/Users/soimm/OneDrive/Documents/AP_Compscie_Maze_Solver/cursor_solver.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void paintComponent(Graphics g) {

            super.paintComponent(g);

            
            int cellSize = 50;
            int y = 0;
            int x = 0;
            int y_start = 0;
            int x_start = 0;
            for (String[] row : maze) {
                for (String cell : row) {
                    if (cell.equals("#")) {
                        g.setColor(Color.BLACK);
                    } else if (cell.equals("S")) {
                        g.setColor(Color.BLUE);
                        x_start = x;
                        y_start = y;
                    } else {
                        g.setColor(Color.WHITE);
                    }
                    g.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                    x++;
                }
                x = 0;
                y++;
            }   
            g.drawImage(image, x_start * cellSize, y_start * cellSize, cellSize, cellSize, this);
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
