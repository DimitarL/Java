import java.util.*;

public class Main {
    public static void main(String[] args) {
        int numOfCities;

        Scanner input = new Scanner(System.in);

        while (true) {
            System.out.println("Enter number of cities: ");
            numOfCities = input.nextInt();
            if (numOfCities < 2 || numOfCities > 100) {
                System.out.println("There is no possible solution!");
            } else break;
        }
        input.close();

        new TravelingSalesman(numOfCities, 0);
    }
}
