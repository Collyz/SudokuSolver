import java.io.*;
import java.util.*;

public class SudokuSolver {

    private final int[] AVAILABILITY_SET = {1,2,3,4,5,6,7,8,9};
    private final int EMPTY_CELL = 0;
    private int[][] puzzle;

    public SudokuSolver(){
        this.puzzle = new int[9][9];
    }


    /**
     * Runs various methods to solve the puzzle if possible
     */
    public void run(){
        inputPuzzle();
        if(solveSudoku()){
            printPuzzle();
        }else{
            System.out.println("Solution not found");
        }
    }

    /**
     * Prompts the user to enter the filename to get the puzzle
     */
    public void inputPuzzle(){
        try {
            //Getting filename
            Scanner userInput = new Scanner(System.in);
            System.out.print("Enter the filename: ");
            String filename = userInput.nextLine();
            //Reading file
            Scanner fileReader = new Scanner(new File(filename));
            while(fileReader.hasNext()){
                //Inserting puzzle into 2d array
                for(int i=0; i < puzzle.length; i++){
                    for(int j=0; j < puzzle[i].length; j++){
                        puzzle[i][j] = fileReader.nextInt();
                    }
                }
            }
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the 2D array holding the puzzle
     * @return The current puzzle
     */
    public int[][] getPuzzle(){
        return this.puzzle;
    }

    /**
     * Prints the puzzle (The 2D array holding the puzzle)
     */
    public void printPuzzle(){
        for (int[] ints : puzzle) {
            String row = "";
            for (int anInt : ints) {
                row += " " + anInt;
            }
            row += "\n";
            System.out.print(row);
        }
    }

    /**
     * Solves the sudoku puzzle
     * @return boolean to self validate and determine if solution is possible
     */
    public boolean solveSudoku() {
        //Go through every index in the 2D array
        for (int rowPos = 0; rowPos < puzzle.length; rowPos++) {
            for (int colPos = 0; colPos < puzzle.length; colPos++) {
                //If the puzzle index holds a zero then
                if (puzzle[rowPos][colPos] == EMPTY_CELL) {
                    //Find the available values for that index, try a value
                    int[] available = getAllAvailable(rowPos, colPos);
                    //Check if any available, if not skips
                    if(available.length != 0){
                        for(int i = 0; i < available.length; i++){
                            //Checking if valid value given
                            if(isValid(rowPos, colPos, available[i])){
                                puzzle[rowPos][colPos] = available[i];
                            }

                            if(solveSudoku()){
                                return true;
                            }else{
                                puzzle[rowPos][colPos] = EMPTY_CELL;
                            }
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Determines if the value assigned at the position is a valid value
     * @param row The row the value is stored at
     * @param col The column the value is stored at
     * @param value The value itself at the row,colm position
     * @return boolean value determining if the value is valid
     */
    private boolean isValid(int row, int col, int value) {
        /*Row and Column Check*/
        for (int i = 0; i < puzzle.length; i++) {
            //Row Check
            if (puzzle[row][i] == value) {
                return false;
            }
            //Column Check
            if (puzzle[i][col] == value) {
                return false;
            }
        }
        /*Box Check*/
        int boxRow = (row / 3) * 3; //Calculate start row index of the box
        int boxCol = (col / 3) * 3; //Calculate start column index of the box
        //Loops to check box
        for (int i = boxRow; i < boxRow + 3; i++) {
            for (int j = boxCol; j < boxCol + 3; j++) {
                if (puzzle[i][j] == value) {
                    return false;
                }
            }
        }
        return true;

    }

    /**
     * Gets the vertical and horizontal constraints and produces an
     * availability set based on the constraints.
     * @param value The value at the index
     * @param rowPos The row position
     * @param colPos The column position
     * @return An array as the availability set only checking the horizontal and vertical existing values
     */
    public int[] getVertAndHorizLineValues(int value, int rowPos, int colPos){
        //Using a set because repetitive values inserted into a set are ignored.
        Set<Integer> set = new HashSet<>();
        if(value == 0){
            // Get horizontal constraint
            for(int i = 0; i < 9; i++){
                //Ignoring zeroes
                if(puzzle[rowPos][i] != 0){
                    set.add(puzzle[rowPos][i]);
                }
            }
            // Get vertical constraint
            for (int i = 0; i < 9; i++) {
                //Ignoring zeroes
                if(puzzle[i][colPos] != 0){
                    set.add(puzzle[i][colPos]);
                }
            }

            //Creating an array from the set
            int[] result = new int[set.size()];
            int i = 0;
            for (int val : set) {
                result[i++] = val;
            }
            return result;
        }else{
            return new int[0];
        }
    }

    /**
     * Gets the 'box' constraints for the value produces an availability
     * set based on the constraints. The 'box' is the 3x3 box that the value belongs to.
     * @param value The value at the index
     * @param rowPos The row position
     * @param colPos The column position
     * @return An array that
     */
    public int[] getBoxValues(int value, int rowPos, int colPos){
        if(value != 0){
            return new int[0];
        }
        int boxRow = (rowPos / 3) * 3;
        int boxCol = (colPos / 3) * 3;

        ArrayList<Integer> subArray = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(value == puzzle[boxRow + i][boxCol + j]){
                    subArray.add(0);
                }else {
                    subArray.add(this.puzzle[boxRow + i][boxCol + j]);
                }
            }
        }
        int[] result = new int[subArray.size()];
        for(int i = 0; i < subArray.size();i++) {
            result[i] = subArray.get(i);
        }

        return result;
    }

    /**
     * Sorts and removes values that a variable cannot be
     * @param array The array that has values that a variable can be
     * @return The sorted availability set of the variable
     */
    public int[] removeExisting(int[] array) {
        if(array.length == 0){
            return array;
        }
        List<Integer> list1 = new ArrayList<>();
        for (int i : AVAILABILITY_SET) {
            list1.add(i);
        }

        List<Integer> list2 = new ArrayList<>();
        for (int i : array) {
            list2.add(i);
        }

        list1.removeAll(list2);
        int[] result = new int[list1.size()];
        for(int i = 0; i < list1.size();i++){
            result[i] = list1.get(i);
        }

        Arrays.sort(result);
        return result;
    }

    /**
     * Used to combine the two availability sets and return a
     * set that accounts for vertical, horizontal, and box constraints
     * @param arr1 The first availability set
     * @param arr2 The second availability set
     * @return The combined availability set
     */
    private int[] combineArrays(int[] arr1, int[] arr2){
        Set<Integer> set1 = new HashSet<>();
        int[] combinedArray = new int[arr1.length + arr2.length];
        int index = 0;
        for (int i = 0; i < arr1.length; i++) {
            combinedArray[index] = arr1[i];
            index++;
        }
        for (int i = 0; i < arr2.length; i++) {
            combinedArray[index] = arr2[i];
            index++;
        }
        for(int i = 0; i < combinedArray.length;i++){
            set1.add(combinedArray[i]);
        }

        int[] result = new int[set1.size()];
        int i = 0;
        for(int var : set1){
            result[i++] = var;
        }
        return result;

    }

    /**
     * Returns the complete availability set for the position
     */
    public int[] getAllAvailable(int rowPos, int colPos){
        //Availability Set from vertical and horizontal lines
        int[] temp1 = getVertAndHorizLineValues(puzzle[rowPos][colPos], rowPos, colPos);
        //Availability Set from the box in which the variable exists
        int[] temp2 = getBoxValues(puzzle[rowPos][colPos], rowPos, colPos);

        int [] combinedAvailable = combineArrays(temp2, temp1);
        combinedAvailable = removeExisting(combinedAvailable);
        return combinedAvailable;
    }


}