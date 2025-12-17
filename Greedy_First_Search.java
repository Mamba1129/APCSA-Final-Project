import java.util.ArrayList;

public class Greedy_First_Search extends Breadth_First_Search {

    private boolean solved = false;

    public Greedy_First_Search(String[][] maze) {
        super(maze);
    }

    // A method to determine the distance from current position to finish
    public int detirmine_the_distance(ArrayList<Integer> current_pos, int x_finish, int y_finish) {
        int current_x = current_pos.get(0);
        int current_y = current_pos.get(1);
        int distance = Math.abs(current_x - x_finish) + Math.abs(current_y - y_finish);
        return distance;
    }

    @Override
    public void Solve (int x_start, int y_start, int x_finish, int y_finish, javax.swing.JPanel panel) {
        //Does not allow it to run the algorithm multiple times
        if (solved) return;
        solved = true;

        //Starting cell
        int cell_row = x_start;
        int cell_col = y_start;

        ArrayList<Integer> starting_pos = new ArrayList<>();
        starting_pos.add(x_start);
        starting_pos.add(y_start);

        //Adding the start point to the starting position
        checked.add(starting_pos);

        ArrayList<ArrayList<Integer>> checked_temp = new ArrayList<>();
        while (true){
            curr_in_check.clear();
            //Creating a new temp list of checked cells 
            checked_temp.clear();
            for (ArrayList<Integer> list : checked) {
                checked_temp.add(new ArrayList<>(list)); // Copy each inner list
            }

            System.out.println("Checked size" + checked.size());
            for (ArrayList<Integer> pos : checked_temp){

                cell_row = pos.get(0);
                cell_col = pos.get(1);
                if (cell_row + 1 < maze.length && (maze[cell_row + 1][cell_col].equals(" ") || maze[cell_row + 1][cell_col].equals("F"))){
                    ArrayList<Integer> upper_cell = new ArrayList<>();
                    upper_cell.add(cell_row + 1);
                    upper_cell.add(cell_col);
                    upper_cell.add(cell_row);
                    upper_cell.add(cell_col);
                    
                    boolean already_checked = false;
                    for (ArrayList<Integer> checked_cell : checked_temp) {
                        if (checked_cell.get(0).equals(upper_cell.get(0)) && checked_cell.get(1).equals(upper_cell.get(1))) {
                            already_checked = true;
                            break;
                        }
                    }
                    if (!already_checked) {
                        curr_in_check.add(upper_cell);
                    }
                }
                if (cell_row - 1 >= 0 && (maze[cell_row - 1][cell_col].equals(" ") || maze[cell_row - 1][cell_col].equals("F"))){
                    ArrayList<Integer> lowwer_cell = new ArrayList<>();
                    lowwer_cell.add(cell_row - 1);
                    lowwer_cell.add(cell_col);
                    lowwer_cell.add(cell_row);
                    lowwer_cell.add(cell_col);
                    
                    boolean already_checked = false;
                    for (ArrayList<Integer> checked_cell : checked_temp) {
                        if (checked_cell.get(0).equals(lowwer_cell.get(0)) && checked_cell.get(1).equals(lowwer_cell.get(1))) {
                            already_checked = true;
                            break;
                        }
                    }
                    if (!already_checked) {
                        curr_in_check.add(lowwer_cell);
                    }
                }
                if (cell_col + 1 < maze[0].length && (maze[cell_row][cell_col + 1].equals(" ") || maze[cell_row][cell_col + 1].equals("F"))){
                    ArrayList<Integer> right_cell = new ArrayList<>();
                    right_cell.add(cell_row);
                    right_cell.add(cell_col + 1);
                    right_cell.add(cell_row);
                    right_cell.add(cell_col);
                    
                    boolean already_checked = false;
                    for (ArrayList<Integer> checked_cell : checked_temp) {
                        if (checked_cell.get(0).equals(right_cell.get(0)) && checked_cell.get(1).equals(right_cell.get(1))) {
                            already_checked = true;
                            break;
                        }
                    }
                    if (!already_checked) {
                        curr_in_check.add(right_cell);
                    }
                }
                if (cell_col - 1 >= 0 && (maze[cell_row][cell_col - 1].equals(" ") || maze[cell_row][cell_col - 1].equals("F"))){
                    ArrayList<Integer> left_cell = new ArrayList<>();
                    left_cell.add(cell_row);
                    left_cell.add(cell_col - 1);
                    left_cell.add(cell_row);
                    left_cell.add(cell_col);
                    
                    boolean already_checked = false;
                    for (ArrayList<Integer> checked_cell : checked_temp) {
                        if (checked_cell.get(0).equals(left_cell.get(0)) && checked_cell.get(1).equals(left_cell.get(1))) {
                            already_checked = true;
                            break;
                        }
                    }
                    if (!already_checked) {
                        curr_in_check.add(left_cell);
                    }
                }
            }

            //Figuring out the cell with the minimum distance to the finish
            int min_distance = Integer.MAX_VALUE;
            for (ArrayList<Integer> pos1 : curr_in_check){
                if (detirmine_the_distance(pos1, x_finish, y_finish) < min_distance){
                    min_distance = detirmine_the_distance(pos1, x_finish, y_finish);
                }
            } 

            //Assinging tjhe cell with the minimum distance to be the current cell
            for (ArrayList<Integer> pos1 : curr_in_check){
                if (detirmine_the_distance(pos1, x_finish, y_finish) == min_distance){
                    checked.add(pos1);
                    if (detirmine_the_distance(pos1, x_finish, y_finish) == 0){
                        //Reconstruct the path using findPath from parent class
                        findPath(pos1, checked, path);
                        System.out.println("Path found with " + path.size() + " cells:");
                        for (ArrayList<Integer> cell : path) {
                            System.out.println("  [" + cell.get(0) + ", " + cell.get(1) + "]");
                        }
                        panel.repaint();
                        solved = true;
                        return;
                    }
                    break;
                }
            }

            try {
                Thread.sleep(100); // 100ms delay to see animation
            } catch (InterruptedException e) {}

            panel.repaint();
        }
    }
}
    
