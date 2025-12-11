
import java.awt.*;
import java.util.ArrayList;
import javax.swing.JPanel;

public class Breadth_First_Search {
    // Implementation of BFS algorithm for maze solving\
    private String[][] maze;
    private boolean solved = false;
    public ArrayList<ArrayList<Integer>> curr_in_check = new ArrayList<>();
    public ArrayList<ArrayList<Integer>> checked = new ArrayList<>();

    public Breadth_First_Search(String[][] maze) {
        // Constructor
        this.maze = maze;
    }
    // A getter method for curr_in_check
    public ArrayList<ArrayList<Integer>> get_Curr_in_check() {
        return curr_in_check; 
    }
    public ArrayList<ArrayList<Integer>> get_Checked() {
        return checked; 
    }

    public void Solve (int x_start, int y_start, int x_finish, int y_finish, JPanel panel) {
        //Does not allow it to run the algorithm multiple times
        if (solved) return;
        solved = true;
        
        //Starting cell
        int cell_row = x_start;
        int cell_col = y_start;

        ArrayList<Integer> starting_pos = new ArrayList<>();
        starting_pos.add(x_start);
        starting_pos.add(y_start);

        // The list of checking cells
        
        curr_in_check.add(starting_pos);

        // The list of checked cells
        
        int iterations = 0;
        boolean exit_found = false;
        ArrayList<ArrayList<Integer>> curr_in_check_temp = new ArrayList<>();
        //Main while loop to get t0 the end
        while (!exit_found/*cell_row != y_finish || cell_col != x_finish*/) {
            //Creating a new temp list to avoid concurrent modification error
            curr_in_check_temp.clear();
            for (ArrayList<Integer> list : curr_in_check) {
                curr_in_check_temp.add(new ArrayList<>(list)); // Copy each inner list
            }

            //Adding the cells that the programm checked into the checked list
            for (ArrayList<Integer> checked_cell : curr_in_check) {
                if (!checked_cell.equals(starting_pos)){
                    checked.add(checked_cell);
                }
            }

            System.out.println("Current in check_temp:" + curr_in_check_temp.size());
            curr_in_check.clear();
            for (ArrayList<Integer> current_cell : curr_in_check_temp) {
                
                cell_row = current_cell.get(0);
                cell_col = current_cell.get(1);
                ArrayList<Integer> cell = new ArrayList<>();
                cell.add(cell_row);
                cell.add(cell_col);
                //Check if the exit is found
                if (cell_row == y_finish && cell_col == x_finish){
                    exit_found = true;
                    break;
                }
                
                // index 0 is checking row, index 1 is checking col, index 2 is checked row, index 3 is checked col
                if (cell_row + 1 < maze.length && (maze[cell_row + 1][cell_col].equals(" ") || maze[cell_row + 1][cell_col].equals("F")) ) {
                    ArrayList<Integer> pos = new ArrayList<>();
                    pos.add(cell_row + 1);
                    pos.add(cell_col);
                    pos.add(cell_row);
                    pos.add(cell_col);
                    curr_in_check.add(pos);
                    System.out.println("Checking the row above");
                }
                if (cell_row - 1 >= 0 && (maze[cell_row - 1][cell_col].equals(" ") || maze[cell_row - 1][cell_col].equals("F")) ) {
                    ArrayList<Integer> pos = new ArrayList<>();
                    pos.add(cell_row - 1);
                    pos.add(cell_col);
                    pos.add(cell_row);
                    pos.add(cell_col);
                    curr_in_check.add(pos);
                    System.out.println("Checking the row below");
                }
                if (cell_col < maze[0].length && (maze[cell_row][cell_col + 1].equals(" ") || maze[cell_row][cell_col + 1].equals("F")) ) {
                    ArrayList<Integer> pos = new ArrayList<>();
                    pos.add(cell_row);
                    pos.add(cell_col + 1);
                    pos.add(cell_row);
                    pos.add(cell_col);
                    curr_in_check.add(pos);
                    System.out.println("Checking the column right");
                }
                if (cell_col - 1 >= 0 && (maze[cell_row][cell_col - 1].equals(" ") || maze[cell_row][cell_col - 1].equals("F")) ) {
                    ArrayList<Integer> pos = new ArrayList<>();
                    pos.add(cell_row);
                    pos.add(cell_col - 1);
                    pos.add(cell_row);
                    pos.add(cell_col);
                    curr_in_check.add(pos);
                    System.out.println("Checking the column Left");
                }

                //Checking that it is not the starting cell
                if (!maze[cell_row][cell_col].equals("S")){maze[cell_row][cell_col] = "V";};
                
                // Repaint after each cell is processed
                panel.repaint();
                try {
                    Thread.sleep(100); // 200ms delay to see animation
                } catch (InterruptedException e) {}
                
                System.out.println("Current in check:" + curr_in_check.size());
                for (ArrayList<Integer> pos : curr_in_check) {
                    System.out.println("Row: " + pos.get(0) + " Col: " + pos.get(1));
                }
                System.out.println("The final position is " + y_finish + ", " + x_finish);
                
            }
            
            iterations ++;
            //break;
        }
    }
}
