import java.util.Scanner;   // Text input/output library

public class Main {
    public static void main(String[] args) {
        int numOfQueens;

        Scanner input = new Scanner(System.in);

        while (true) {
            System.out.println("Enter number of queens: ");
            numOfQueens = input.nextInt();
            if (numOfQueens < 0 || numOfQueens == 2 || numOfQueens == 3) {
                System.out.println("There is no possible solution!");
            } else break;
        }

        NQueens newNQueensProblem = new NQueens(numOfQueens);
        if (numOfQueens <= 100) {
            System.out.println("Solution for " + numOfQueens + " queens: \n");
        }
        long startTime = System.currentTimeMillis();
        newNQueensProblem.solve();
        long endTime = System.currentTimeMillis();

        if (numOfQueens > 100) {
            System.out.println("Еxecution time in seconds:");
            System.out.println((endTime - startTime) / 1000 + "," + (endTime - startTime) % 1000);
        }

        if (numOfQueens <= 100) {
            newNQueensProblem.printBoard();
        }

        input.close();
    }
}
