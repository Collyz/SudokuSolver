import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class SudokuSolver {

    private int[][] puzzle;
    private final int[] AVAILABILITY_SET = {1,2,3,4,5,6,7,9};

    public SudokuSolver(){
        this.puzzle = new int[9][9];
    }

    /**
     * Prompts the user to enter the filename to get the puzzle
     */
    public void getPuzzle(){
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
     * Solves the puzzle
     */
    public void solvePuzzle(){

    }

    /**
     * Prints the puzzle
     */
    public void printPuzzle(){
        for(int i = 0; i < puzzle.length;i++){
            String row = "";
            for(int j = 0; j < puzzle[i].length; j++){
                row+= " " + puzzle[i][j];
            }
            row += "\n";
            System.out.print(row);
        }
    }

    public int[] getConstraints(int var, int rowPos, int colPos){
        int[] constraint = AVAILABILITY_SET;
        int[] horizontal = new int[9];
        int[] vertical = new int[9];
        if(var != 0){
            // Get horizontal constraint
            for(int i = 0; i < 9 ;i++){
                horizontal[i] = puzzle[rowPos][i];
            }
            // Get vertical constraint
            for (int i = 0; i < 9; i++) {
                vertical[i] = puzzle[i][colPos];
            }

            // Combine horizontal and vertical constraints
            List<Integer> constraintList = new ArrayList<>();
            for (int value : horizontal) {
                if (value != 0) {
                    constraintList.add(value);
                }
            }
            for (int value : vertical) {
                if (value != 0) {
                    constraintList.add(value);
                }
            }

            // Convert constraint list to array and return
            int[] constraints = new int[constraintList.size()];
            for (int i = 0; i < constraintList.size(); i++) {
                constraints[i] = constraintList.get(i);
            }
            return constraints;
        }else{
            return null;
        }
    }

    public int[] getBoxConstraint(int var, int rowPos, int colPos){
        int[] constraint = AVAILABILITY_SET;
        if(var != 0){
            int boxRow = rowPos / 3; // integer division to get box row index (0-2)
            int boxCol = colPos / 3; // integer division to get box column index (0-2)
            int startRow = boxRow * 3; // calculate start row index of the box
            int startCol = boxCol * 3; // calculate start column index of the box
            for(int i = startRow; i < startRow + 3; i++){
                for(int j = startCol; j < startCol + 3; j++){
                    if(puzzle[i][j] != 0){
                        constraint = removeValue(constraint, puzzle[i][j]);
                    }
                }
            }
        }else{
            return null;
        }
        return constraint;
    }

    private int[] removeValue(int[] array, int value){
        int j = 0;
        for(int i = 0; i < array.length; i++){
            if(array[i] != value){
                array[j++] = array[i];
            }
        }
        int[] result = new int[j];
        System.arraycopy(array, 0, result, 0, j);
        return result;
    }

    public void getAllConstraints(){
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                System.out.println(Arrays.toString(getConstraints(puzzle[i][j], i, j)));
            }
        }
    }

}