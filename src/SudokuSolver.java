import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class SudokuSolver {

    private int[][] puzzle;

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


}