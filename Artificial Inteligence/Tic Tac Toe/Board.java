import java.util.ArrayList;
import java.util.List;

class Board {
    List<Point> rootsChildrenScore = new ArrayList<>();
    List<Point> availablePoints;
    int[][] board = new int[3][3];
    private final int computer = 1;
    private final int user = 2;

    public int evaluateBoard() {
        int score = 0;

        // check rows
        for (int i = 0; i < 3; i++) {
            int X = 0;
            int O = 0;

            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 1) {
                    X++;
                } else if (board[i][j] == 2) {
                    O++;
                }
            }
            score += changeInScore(X, O);
        }

        // check columns
        for (int j = 0; j < 3; j++) {
            int X = 0;
            int O = 0;

            for (int i = 0; i < 3; i++) {
                if (board[i][j] == 1) X++;
                else if (board[i][j] == 2) O++;
            }

            score += changeInScore(X, O);
        }

        int X = 0;
        int O = 0;

        // check diagonal 1
        for (int i = 0, j = 0; i < 3; i++, j++) {
            if (board[i][j] == 1) X++;
            else if (board[i][j] == 2) O++;
        }

        score += changeInScore(X, O);

        X = 0;
        O = 0;

        // check diagonal 2
        for (int i = 2, j = 0; -1 < i; i--, j++) {
            if (board[i][j] == 1) X++;
            else if (board[i][j] == 2) O++;
        }
        score += changeInScore(X, O);

        return score;
    }

    private int changeInScore(int X, int O) {
        int changeInScore;

        if (X == 3) changeInScore = 100;
        else if (X == 2 && O == 0) changeInScore = 7;
        else if (X == 1 && O == 0) changeInScore = 5;
        else if (O == 3) changeInScore = -100;
        else if (O == 2 && X == 0) changeInScore = -7;
        else if (O == 1 && X == 0) changeInScore = -5;
        else changeInScore = 0;

        return changeInScore;
    }

    public int alphaBetaMinimax(int alpha, int beta, int depth, int turn) {
        if (beta <= alpha) {
            if (turn == 1) return Integer.MAX_VALUE;
            else return Integer.MIN_VALUE;
        }

        // if depth limit is reached
        if (depth < 0 || isGameOver()) return evaluateBoard();

        List<Point> pointsAvailable = getAvailableStates();

        if (pointsAvailable.isEmpty()) return 0;

        if (depth == 0) rootsChildrenScore.clear();

        int maxValue = Integer.MIN_VALUE;
        int minValue = Integer.MAX_VALUE;

        for (Point point : pointsAvailable) {
            int currentScore;

            // Computer
            if (turn == 1) {
                move(point, 1);
                currentScore = alphaBetaMinimax(alpha, beta, depth + 1, 2);
                maxValue = Math.max(maxValue, currentScore);

                alpha = Math.max(currentScore, alpha);

                if (depth == 0) {
                    rootsChildrenScore.add(new Point(point.getX(), point.getY(), currentScore));
                }
            // User
            } else if (turn == 2) {
                move(point, 2);
                currentScore = alphaBetaMinimax(alpha, beta, depth + 1, 1);
                minValue = Math.min(minValue, currentScore);

                beta = Math.min(currentScore, beta);
            }

            // reset board
            board[point.x][point.y] = 0;
        }

        if (turn == 1) return maxValue;
        else return minValue;
    }

    public boolean isGameOver() {
        // end game if there is a winner or there are no more free tiles(draw)
        return (hasHeWon(computer) || hasHeWon(user) || getAvailableStates().isEmpty());
    }

    public boolean hasHeWon(int player) {
        // check for diagonal 1 and diagonal 2
        if ((board[0][0] == board[1][1] && board[0][0] == board[2][2] && board[0][0] == player) ||
                (board[0][2] == board[1][1] && board[0][2] == board[2][0] && board[0][2] == player)) {
            return true;
        }
        for (int i = 0; i < 3; i++) {
            // check for row i
            if (board[i][0] == board[i][1] && board[i][0] == board[i][2] && board[i][0] == player) {
                return true;
            } else
                // check for column i
                if (board[0][i] == board[1][i] && board[0][i] == board[2][i] && board[0][i] == player) {
                    return true;
                }
        }
        return false;
    }

    public List<Point> getAvailableStates() {
        availablePoints = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 0) {
                    availablePoints.add(new Point(i, j));
                }
            }
        }
        return availablePoints;
    }

    public void move(Point point, int player) {
        board[point.x][point.y] = player;
    }

    public void removeMove(Point point) {
        board[point.x][point.y] = 0;
    }

    public Point pickBestMove() {
        int MAX = Integer.MIN_VALUE;
        int best = -1;

        for (int i = 0; i < rootsChildrenScore.size(); i++) {
            int score = rootsChildrenScore.get(i).score;
            // for every score(point), move
            // check if computer has won
            // if not, remove the last placed point
            if (MAX < score) {
                MAX = score;
                best = i;
            } else if (MAX == score) {
                move(rootsChildrenScore.get(i), 1);
                if (hasHeWon(computer)) {
                    best = i;
                }
                removeMove(rootsChildrenScore.get(i));
            }
        }
        return rootsChildrenScore.get(best);
    }

    public boolean isTileEmpty(int x, int y) {
        if (board[x][y] != 0) {
            System.out.println("This position is already taken!");
            System.out.println("Please choose a new position!");
            System.out.println("Position: ");
        }
        return board[x][y] != 0;
    }

    public void printCurrentBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 1) {
                    System.out.print("x ");
                } else if (board[i][j] == 2) {
                    System.out.print("o ");
                }
                else System.out.print("- ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public void printWinner() {
        if (hasHeWon(computer)) System.out.println("You lost!");
        else if (hasHeWon(user)) System.out.println("You win!");
        else System.out.println("Draw!");
    }
}