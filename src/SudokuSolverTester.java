public class SudokuSolverTester {
    public static void main(String[] args) {

        //Sudoku Solver
        SudokuSolver solver = new SudokuSolver();
        solver.inputPuzzle();
        solver.solveSudoku();
        solver.printPuzzle();
    }
}