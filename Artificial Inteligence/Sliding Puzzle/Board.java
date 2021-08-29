import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;


public class Board {
    double f = 0.0;     // cost of the cheapest path
    double g = 0.0;     // the cost to reach current node

    private final int numRowsCols;
    private final int[][] board;
    private final Board parent;

    public double getG() {
        return g;
    }

    public void setG(double g) {
        this.g = g;
    }

    public double getF() {
        return f;
    }

    public void setF(double f) {
        this.f = f;
    }

    public double distanceFromParent() {
        return 1;
    }

    public Board getParent() {
        return parent;
    }

    public Board(int[][] board, int numRowsCols) {
        this.board = board;
        this.numRowsCols = numRowsCols;
        this.parent = null;
    }

    // create a board from an n-by-n array of tiles
    public Board(int[][] board, int numRowsCols, Board parent) {
        this.board = board;
        this.numRowsCols = numRowsCols;
        this.parent = parent;
    }

    // Calculate Manhattan Distance
    public int manhattanDistance() {
        int sum = 0;
        for (int i = 0; i < numRowsCols; i++) {
            for (int j = 0; j < numRowsCols; j++) {
                int number = board[i][j];
                int defaultRow = number / numRowsCols;
                int defaultColumn = number % numRowsCols;
                if (defaultColumn == 0) {
                    if (defaultRow == 0) {
                        defaultRow = numRowsCols - 1;
                    } else {
                        defaultRow -= 1;
                    }
                    defaultColumn = numRowsCols - 1;
                } else {
                    defaultColumn -= 1;
                }
                sum += (Math.abs(defaultRow - i) + Math.abs(defaultColumn - j));
            }
        }
        return sum;
    }

    // Representation of the board
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("\n");
        //sb.append("Board: ").append("\n");
        for (int i = 0; i < numRowsCols; i++)
            for (int j = 0; j < numRowsCols; j++) {
                sb.append(board[i][j]).append("  ");
                if (j == numRowsCols - 1) {
                    sb.append("\n");
                }
            }
        return sb.toString();
    }

    // Check whether this board solvable
    public boolean isSolvable() {
        //  An inversion is any pair of tiles i and j where i < j but i appears
        //  after j when considering the board in row-major order
        int inversions = 0;
        int k=0;
        int[] array = new int[numRowsCols * numRowsCols];

        for (int i = 0; i < numRowsCols; i++) {
            for (int j = 0; j < numRowsCols; j++) {
                array[k] = board[i][j];
                k++;
            }
        }

        int zero = 0;

        for (int i = 0; i < numRowsCols * numRowsCols - 1; i++) {
            for (int j = i + 1; j < numRowsCols * numRowsCols; j++) {
                if (array[i] == 0 || array[j] == 0) {
                    if (array[i] == 0) {
                        zero = i;   // numRowsCols;
                        continue;
                    }
                    zero = j;
                    continue;
                }
                if (array[i] > array[j]) {
                    inversions++;
                }
            }
        }
        // If a board has an odd number of inversions, it is unsolvable because
        // the goal board has an even number of inversions

        // An n-by-n board is NOT solvable if its number of inversions is not even
        if(numRowsCols % 2 != 0) {
            return inversions % 2 == 0;
        }

        return (inversions + zero % 2) != 0;
    }

    private int emptyTilePos() {
        for (int i = 0; i < numRowsCols; i++) {
            for (int j = 0; j < numRowsCols; j++) {
                if (board[i][j] == 0) {
                    return numRowsCols * i + j + 1;
                }
            }
        }
        return -1;
    }

    public Iterable<Board> neighbors() {
        List<Board> neighbors = new ArrayList<>();
        int[][] neighbor;
        int i = (emptyTilePos() - 1) / numRowsCols;
        int j = (emptyTilePos() - 1) % numRowsCols;
        int tmp;
        if (i - 1 >= 0 && i - 1 < numRowsCols - 1) {
            neighbor = cloneTiles();
            tmp = neighbor[i][j];
            neighbor[i][j] = neighbor[i - 1][j];
            neighbor[i - 1][j] = tmp;
            Board board = new Board(neighbor, numRowsCols, this);
            neighbors.add(board);
        }
        if (j + 1 > 0 && j + 1 < numRowsCols) {
            neighbor = cloneTiles();
            tmp = neighbor[i][j];
            neighbor[i][j] = neighbor[i][j + 1];
            neighbor[i][j + 1] = tmp;
            Board board = new Board(neighbor, numRowsCols, this);
            neighbors.add(board);
        }
        if (i + 1 > 0 && i + 1 < numRowsCols) {
            neighbor = cloneTiles();
            tmp = neighbor[i][j];
            neighbor[i][j] = neighbor[i + 1][j];
            neighbor[i + 1][j] = tmp;
            Board board = new Board(neighbor, numRowsCols, this);
            neighbors.add(board);
        }

        if (j - 1 >= 0 && j - 1 < numRowsCols - 1) {
            neighbor = cloneTiles();
            tmp = neighbor[i][j];
            neighbor[i][j] = neighbor[i][j - 1];
            neighbor[i][j - 1] = tmp;
            Board board = new Board(neighbor, numRowsCols, this);
            neighbors.add(board);
        }
        return neighbors;
    }

    private int[][] cloneTiles() {
        int[][] s = new int[numRowsCols][numRowsCols];
        for (int i = 0; i < numRowsCols; i++)
            System.arraycopy(board[i], 0, s[i], 0, numRowsCols);
        return s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Board)) return false;
        Board board1 = (Board) o;
        return Arrays.deepEquals(board, board1.board);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(board);
    }
}
