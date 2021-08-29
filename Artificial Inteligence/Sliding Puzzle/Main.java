import java.util.Scanner;   // Text input/output library
import java.lang.Math;      // Library for simple Math operations

public class Main {
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);

        System.out.print("Enter positive number of blocks with numbers: ");
        int blocksWithNumbers = input.nextInt();
        //System.out.print("Enter the index of the position of zero: ");
        //int emptyTilePos = input.nextInt();

        int numRowsCols = (int) Math.sqrt(blocksWithNumbers + 1);

        int initialState[][] = new int[numRowsCols][numRowsCols];

        System.out.println("Enter the elements of the matrix: ");
        for (int i = 0; i < numRowsCols; i++)
            for (int j = 0; j < numRowsCols; j++)
                initialState[i][j] = input.nextInt();

        int goalState[][] = new int[numRowsCols][numRowsCols];
        int goalStateCounter = 1;
        //System.out.println("Enter the elements of the matrix: ");
        for (int i = 0; i < numRowsCols; i++) {
            for (int j = 0; j < numRowsCols; j++) {
                goalState[i][j] = goalStateCounter;
                goalStateCounter++;
            }
        }
        goalState[numRowsCols-1][numRowsCols-1] = 0;


        // Input values for testing
        /*int[][] initialState = {
                // Solvable example 1
                1,2,3,
                8,5,6,
                4,7,0

                // Solvable example 2
                1,2,3
                4,5,6
                7,0,8

                // Not solvable example
                1,2,3
                4,5,6
                8,7,0

                // Solvable example
                1, 2, 3, 4
                5, 6, 7, 8
                9, 10, 11, 12
                13, 14, 0, 15

                // Solvable example
                1 2 3 4
                5 6 0 8
                9 10 7 11
                13 14 15 12
        };*/

        Board newInitialBoard = new Board(initialState, numRowsCols);
        Board newFinalBoard = new Board(goalState, numRowsCols);

        //System.out.println("Number of rows/cols: " + numRowsCols);
        //System.out.println();

        if(newInitialBoard.isSolvable()) {
            IDASearch newSearch = new IDASearch(newInitialBoard, newFinalBoard);
            // Begin search
            for (Board statesBoards: newSearch.getPath(newSearch.search())){
                // Print states
                System.out.println(statesBoards.toString());
            }
            //System.out.println("Manhattan sum: " + newInitialBoard.manhattanDistance());
        }
        else {
            System.out.println("This board is NOT solvable !!!");
        }

        input.close();
    }
}