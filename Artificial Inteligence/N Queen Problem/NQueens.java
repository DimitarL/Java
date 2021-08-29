import java.util.Random;

public class NQueens {
    int numOfQueens;    // number of input queens
    long conflicts;     // total number of conflicts
    // int[col] queens keeps on which row the queen is placed
    int[] queens;       // the size is the number of queens(there will always be one in a column)
    int[] row;          // number of queens on every row
    int[] d1;           // number of queens on diagonal 1
    int[] d2;           // number of queens on diagonal 1

    public NQueens(int numOfQueens) {
        this.numOfQueens = numOfQueens;
    }

    void solve() {
        while (true) {
            getRandomBoard();
            long minValuesKeeperNumOfConflicts = conflicts;
            int numNoProgressSteps = 0;

            while (numNoProgressSteps < 100 * numOfQueens) {
                if (conflicts == 0) {
                    return;
                }

                Random random = new Random();
                int randomQueenCol = random.nextInt(numOfQueens);

                int newQueenRow = rowWithMinConflicts(randomQueenCol);
                moveQueen(randomQueenCol, newQueenRow);

                if (conflicts < minValuesKeeperNumOfConflicts) {
                    minValuesKeeperNumOfConflicts = conflicts;
                    numNoProgressSteps = 0;
                } else {
                    numNoProgressSteps++;
                }
            }
        }
    }

    // Generate random initial bord for N queens and calculate total number of conflicts
    void getRandomBoard() {
        queens = new int[numOfQueens];
        row = new int[numOfQueens];
        d1 = new int[2 * (numOfQueens - 1) + 1];
        d2 = new int[2 * (numOfQueens - 1) + 1];

        Random random = new Random();
        for (int colNum = 0; colNum < numOfQueens; colNum++) {
            int rowNum = random.nextInt(numOfQueens);

            queens[colNum] = rowNum;

            conflicts += row[rowNum] + d1[rowNum + colNum] + d2[rowNum + (numOfQueens - colNum - 1)];
            row[rowNum]++;
            d1[rowNum + colNum]++;
            d2[rowNum + numOfQueens - colNum - 1]++;
        }
    }

    private void moveQueen(int col, int newRow)
    {
        int currentRow = queens[col];
        conflicts -= (row[currentRow] + d1[currentRow + col] + d2[currentRow + numOfQueens - col - 1] - 3);
        row[currentRow]--;
        d1[currentRow + col]--;
        d2[currentRow + numOfQueens - col - 1]--;

        queens[col] = newRow;
        conflicts += row[newRow] + d1[newRow + col] + d2[newRow + numOfQueens - col - 1];;
        row[newRow]++;
        d1[newRow + col]++;
        d2[newRow + numOfQueens - col - 1]++;
    }

    int rowWithMinConflicts(int col) {
        int[] minValuesKeeper = new int[numOfQueens];
        minValuesKeeper[0] = queens[col];
        int numMinValues = 1;
        long maxConflictsToRemove = 0;
        long conflictsToRemove = row[queens[col]] + d1[queens[col] + col] + d2[queens[col] + numOfQueens - col - 1] -3;

        for (int i = 0; i < numOfQueens; i++) {
            long conflictsToAdd = row[i] + d1[i + col] + d2[i + numOfQueens - col - 1];
            long maxConflictsToRemoveCandidate = conflictsToRemove - conflictsToAdd;

            if (maxConflictsToRemoveCandidate > maxConflictsToRemove) {
                maxConflictsToRemove = maxConflictsToRemoveCandidate;
                numMinValues = 0;
            }
            if (maxConflictsToRemoveCandidate == maxConflictsToRemove) {
                minValuesKeeper[numMinValues] = i;
                numMinValues++;
            }
        }

        Random random = new Random();
        return minValuesKeeper[random.nextInt(numMinValues)];
    }

    public void printBoard() {
        for (int row = 0; row < numOfQueens; row++) {
            for (int col = 0; col < numOfQueens; col++) {
                if (queens[col] == row) {
                    System.out.print("* ");
                    continue;
                }
                System.out.print("_ ");
            }
            System.out.println();
        }
    }
}
