import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Board board = new Board();
        board.printCurrentBoard();

        System.out.println("Who will go first?");
        int choice;
        Scanner input = new Scanner(System.in);

        while (true) {
            System.out.println("Choose 0 for COMPUTER & 1 for USER:");
            choice = input.nextInt();

            if (choice == 0 || choice == 1) break;
            else System.out.println("Wrong input player!");
        }

        System.out.println("Values for rows & cols: from 1 to 3!");
        System.out.println();

        if (choice == 0) {
            Random random = new Random();
            int randomX = random.nextInt(3);
            int randomY = random.nextInt(3);

            Point point = new Point(randomX, randomY);
            board.move(point, 1);
            board.printCurrentBoard();
        }

        while (!board.isGameOver()) {
            int x;
            int y;

            System.out.println("Input your move: ");
            do {
                x = input.nextInt() - 1;
                y = input.nextInt() - 1;
            } while (board.isTileEmpty(x, y));

            Point userMove = new Point(x, y);

            board.move(userMove, 2);
            board.printCurrentBoard();  // print for first player
            if (board.isGameOver()) break;

            board.alphaBetaMinimax(Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 1);
            board.move(board.pickBestMove(), 1);
            board.printCurrentBoard();  // print for second player
        }
        input.close();

        board.printWinner();
    }
}