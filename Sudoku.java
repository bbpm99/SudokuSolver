/**
 * Sudoku.java
 * 
 * Implementation of a class that represents a Sudoku puzzle and solves
 * it using recursive backtracking.

import java.io.*;   // allows us to read from a file
import java.util.*;

public class Sudoku {    
    // The current contents of the cells of the puzzle. 
    private int[][] grid;
    
    /*
     * Indicates whether the value in a given cell is fixed 
     * (i.e., part of the initial configuration).
     * valIsFixed[r][c] is true if the value in the cell 
     * at row r, column c is fixed, and false otherwise.
     */
    private boolean[][] valIsFixed;
    
    /*
     * This 3-D array allows us to determine if a given subgrid 
     * (i.e., a given 3x3 region of the puzzle) already contains a given value.
     * We use 2 indices to identify a given subgrid:
     *
     *    (0,0)   (0,1)   (0,2)
     *
     *    (1,0)   (1,1)   (1,2)
     * 
     *    (2,0)   (2,1)   (2,2)
     * 
     * For example, subgridHasVal[0][2][5] will be true if the subgrid in the 
     * upper right-hand corner already has a 5 in it, and false otherwise.
     */
    private boolean[][][] subgridHasVal;
    
    
    /* 
     * Constructs a new Puzzle object, which initially
     * has all empty cells.
     */
    public Sudoku() {
        this.grid = new int[9][9];
        this.valIsFixed = new boolean[9][9];     
        
        /* 
         * Note that the third dimension of the following array is 10,
         * because we need to be able to use the possible values 
         * (1 through 9) as indices.
         */
        this.subgridHasVal = new boolean[9][9][10];        
        

    }
    
    /*
     * Place the specified value in the cell with the specified coordinates, 
     * and update the state of the puzzle accordingly.
     */
    public void placeVal(int val, int row, int col) {
        this.grid[row][col] = val;
        this.subgridHasVal[row/3][col/3][val] = true;
        

    }
    
    /*
     * remove the specified value from the cell with the specified coordinates, 
     * and update the state of the puzzle accordingly.
     */
    public void removeVal(int val, int row, int col) {
        this.grid[row][col] = 0;
        this.subgridHasVal[row/3][col/3][val] = false;
        

    }  
    
    /*
     * read in the initial configuration of the puzzle from the specified 
     * Scanner, and use that config to initialize the state of the puzzle.  
     * The configuration should consist of one line for each row, with the
     * values in the row specified as integers separated by spaces.
     * A value of 0 should be used to indicate an empty cell.
     * 
     */
    public void readConfig(Scanner input) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                int val = input.nextInt();
                this.placeVal(val, r, c);
                if (val != 0) {
                    this.valIsFixed[r][c] = true;
                }
            }
            input.nextLine();
        }
    }
    
    /*
     * Displays the current state of the puzzle.
     */        
    public void printGrid() {
        for (int r = 0; r < 9; r++) {
            this.printRowSeparator();
            for (int c = 0; c < 9; c++) {
                System.out.print("|");
                if (this.grid[r][c] == 0)
                    System.out.print("   ");
                else
                    System.out.print(" " + this.grid[r][c] + " ");
            }
            System.out.println("|");
        }
        this.printRowSeparator();
    }
    
    // A private helper method used by display() 
    // to print a line separating two rows of the puzzle.
    private static void printRowSeparator() {
        for (int i = 0; i < 9; i++)
            System.out.print("----");
        System.out.println("-");
    }
    
    
    public void testme() {
    }
    
    public boolean next(int row, int col) {
        if (col < 8) {
            return this.solveRB(row, col+1);
        } else {
            return this.solveRB(row +1, 0);
        }
    }
    
    //checks to see if number is safe to place here or not
    public boolean safety(int num, int row, int col){
        //row check
        for(int i = 0; i < 9; i++) {
            if(this.grid[row][i] == num) {
                return false;
            }
        }
        //col check
        for(int i = 0; i < 9; i++) {
            if(this.grid[i][col] == num) {
                return false;
            }
        }
        //box check
        if(this.subgridHasVal[row/3][col/3][num]) {
            return false;
        }
        return true;
    }
    
    /*
     * This is the key recursive-backtracking method.
     * Returns true if a solution has already been found, and false otherwise.
     * 
     * There are different ways to use parameters in this method, 
     * and you will need to decide how many parameters you want to use 
     * and what they should mean.
     */
    private boolean solveRB(int row, int col) {     
        if (row >= 9) {
            return true;
        }
        
        if(this.valIsFixed[row][col]) {
            if (next(row,col)) {
                return true;
            }
        }
        else {
            for (int val = 1; val <= 9; val++){
                if (safety(val, row, col)) {
                    this.placeVal(val, row, col);
                    if (next(row, col)){
                        return true;
                    }
                }
                this.removeVal(this.grid[row][col], row, col);
            }
        }
        
        /* 
         * The following return statement allows the initial code to compile.
         * Replace it with your full implementation of the recursive-backtracking
         * method.
         */
        return false;
    } 

    /*
     * public "wrapper" method for solveRB().
     * Makes the initial call to solveRB, and returns whatever it returns.
     */
    public boolean solve() { 
        boolean foundSol = this.solveRB(0,0);
        return foundSol;
    }
    
    

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        Sudoku puzzle = new Sudoku();
        
        System.out.print("Enter the name of the puzzle file: ");
        String filename = scan.nextLine();
        
        try {
            Scanner input = new Scanner(new File(filename));
            puzzle.readConfig(input);
        } catch (IOException e) {
            System.out.println("error accessing file " + filename);
            System.out.println(e);
            System.exit(1);
        }
        
        puzzle.testme();
        
        System.out.println();
        System.out.println("Here is the initial puzzle: ");
        puzzle.printGrid();
        System.out.println();
     
        if (puzzle.solve()) {
            System.out.println("Here is the solution: ");
        } else {
            System.out.println("No solution could be found.");
            System.out.println("Here is the current state of the puzzle:");
        }
        puzzle.printGrid();  
    }
}
