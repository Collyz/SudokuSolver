import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class SudokuSolver {

    private final int[][] puzzle;
    private final int[] AVAILABILITY_SET = {1,2,3,4,5,6,7,8,9};

    public SudokuSolver(){
        this.puzzle = new int[9][9];
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
     * Solves the puzzle
     */
    public void solvePuzzle(){

    }

    /**
     * Prints the puzzle
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

    public int[][] getPuzzle(){
        return this.puzzle;
    }

    public int[] getConstraints(int var, int rowPos, int colPos){
        Set<Integer> set = new HashSet<>();
        if(var == 0){
            // Get horizontal constraint
            for(int i = 0; i < 9; i++){
                if(puzzle[rowPos][i] != 0){
                    set.add(puzzle[rowPos][i]);
                }
            }
            // Get vertical constraint
            for (int i = 0; i < 9; i++) {
                if(puzzle[i][colPos] != 0){
                    set.add(puzzle[i][colPos]);
                }
            }

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

    public int[] getBoxConstraint(int var, int rowPos, int colPos){
        if(var != 0){
            return new int[0];
        }
        int boxRow = rowPos / 3; // integer division to get box row index (0-2)
        int boxCol = colPos / 3; // integer division to get box column index (0-2)
        int startRow = boxRow * 3; // calculate start row index of the box
        int startCol = boxCol * 3; // calculate start column index of the box
        ArrayList<Integer> subArray = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(var == puzzle[startRow + i][startCol + j]){
                    subArray.add(0);
                }else {
                    subArray.add(this.puzzle[startRow + i][startCol + j]);
                }
            }
        }
        int[] result = new int[subArray.size()];
        for(int i = 0; i < subArray.size();i++) {
            result[i] = subArray.get(i);
        }

        return result;
    }

    public void getAllConstraints(){
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                Set<Integer> combined = new HashSet<>();
                int[] temp1 = getBoxConstraint(puzzle[i][j], i, j);
                int[] temp2 = getConstraints(puzzle[i][j], i, j);
                int [] result = combineArrays(temp1, temp2);
                result = removeAvailable(result);
                System.out.println("Puzzle pos: " + i + "," + j + ":"
                        + Arrays.toString(result));
            }
        }
    }



    /**
     * Sorts and removes repetitive values in the constraints
     * @param array The array to sort and remove repeating values
     * @return The sorted and unique array
     */
    public int[] removeAvailable(int[] array) {
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

    public int[] combineArrays(int[] arr1, int[] arr2){
        Set<Integer> set1 = new HashSet<>();
        if(arr1 != null){
            set1 = Arrays.stream(arr1).boxed().collect(Collectors.toSet());
        }
        Set<Integer> set2 = new HashSet<>();
        if(arr2 != null){
            set2 = Arrays.stream(arr2).boxed().collect(Collectors.toSet());
        }

        set1.addAll(set2);
        int[] result = new int[set1.size()];
        int i = 0;
        for(int var : set1){
            result[i++] = var;
        }
        return result;

    }



}