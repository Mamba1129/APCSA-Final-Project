

import java.util.ArrayList;
import javax.swing.JPanel;

public class Breadth_First_Search {
    // Implementation of BFS algorithm for maze solving\
    public String[][] maze;
    //private boolean solved = false;
    public ArrayList<ArrayList<Integer>> curr_in_check = new ArrayList<>();
    public ArrayList<ArrayList<Integer>> checked = new ArrayList<>();
    public ArrayList<ArrayList<Integer>> path = new ArrayList<>();

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
    //Return the path finding algorithm
    public ArrayList<ArrayList<Integer>> get_Path() {
        return path;
    }
    // Recursion method to find the Path
    public void findPath(ArrayList<Integer> current_pos, ArrayList<ArrayList<Integer>> checked, ArrayList<ArrayList<Integer>> path) {
        // Add current position to path first
        ArrayList<Integer> currentCell = new ArrayList<>();
        currentCell.add(current_pos.get(0));
        currentCell.add(current_pos.get(1));
        path.add(0, currentCell); // Add to beginning for correct order
        
        if (current_pos.size() < 4) {
            return; // Base case: reached start (no parent info)
        }
        
        int parent_row = current_pos.get(2);
        int parent_col = current_pos.get(3);

        // Find parent and recurse
        for (ArrayList<Integer> cell : checked) {
            if (cell.get(0) == parent_row && cell.get(1) == parent_col) {
                findPath(cell, checked, path); // Recursive call
                break;
            }
        }
    }

    public void Solve (int x_start, int y_start, int x_finish, int y_finish, JPanel panel) {

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
        ArrayList<Integer> end_pos = new ArrayList<>();
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
                    if (cell_row + 1 == y_finish && cell_col == x_finish){
                        exit_found = true;
                        end_pos.add(cell_row+1);
                        end_pos.add(cell_col);
                        end_pos.add(cell_row);
                        end_pos.add(cell_col);
                        checked.add(end_pos);
                        break;
                    }
                    ArrayList<Integer> pos = new ArrayList<>();
                    pos.add(cell_row + 1);
                    pos.add(cell_col);
                    pos.add(cell_row);
                    pos.add(cell_col);
                    curr_in_check.add(pos);
                    System.out.println("Checking the row above");
                }
                if (cell_row - 1 >= 0 && (maze[cell_row - 1][cell_col].equals(" ") || maze[cell_row - 1][cell_col].equals("F")) ) {
                    if (cell_row - 1 == y_finish && cell_col == x_finish){
                        exit_found = true;
                        end_pos.add(cell_row - 1);
                        end_pos.add(cell_col);
                        end_pos.add(cell_row);
                        end_pos.add(cell_col);
                        checked.add(end_pos);
                        break;
                    }
                    ArrayList<Integer> pos = new ArrayList<>();
                    pos.add(cell_row - 1);
                    pos.add(cell_col);
                    pos.add(cell_row);
                    pos.add(cell_col);
                    curr_in_check.add(pos);
                    System.out.println("Checking the row below");
                }
                if (cell_col < maze[0].length && (maze[cell_row][cell_col + 1].equals(" ") || maze[cell_row][cell_col + 1].equals("F")) ) {
                    if (cell_row == y_finish && cell_col + 1 == x_finish){
                        exit_found = true;
                        end_pos.add(cell_row);
                        end_pos.add(cell_col + 1);
                        end_pos.add(cell_row);
                        end_pos.add(cell_col);
                        checked.add(end_pos);
                        break;
                    }
                    ArrayList<Integer> pos = new ArrayList<>();
                    pos.add(cell_row);
                    pos.add(cell_col + 1);
                    pos.add(cell_row);
                    pos.add(cell_col);
                    curr_in_check.add(pos);
                    System.out.println("Checking the column right");
                }
                if (cell_col - 1 >= 0 && (maze[cell_row][cell_col - 1].equals(" ") || maze[cell_row][cell_col - 1].equals("F")) ) {
                    if (cell_row == y_finish && cell_col - 1 == x_finish){
                        exit_found = true;
                        end_pos.add(cell_row);
                        end_pos.add(cell_col - 1);
                        end_pos.add(cell_row);
                        end_pos.add(cell_col);
                        checked.add(end_pos);
                        break;
                    }
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
                    Thread.sleep(50); // 50ms delay to see animation
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
        System.out.println("The final position is" + end_pos);
        System.out.println("The final pos from the checked list is" + checked.get(checked.size()-1));

        //Figuring out the path
        findPath(end_pos, checked, path);

        System.out.println("Path found with " + path.size() + " cells:");
        for (ArrayList<Integer> cell : path) {
            System.out.println("  [" + cell.get(0) + ", " + cell.get(1) + "]");
        }
        
        // Final repaint to show the path
        panel.repaint();
    }
}
