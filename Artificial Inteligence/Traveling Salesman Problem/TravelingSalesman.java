import java.util.*;

public class TravelingSalesman {
    private final int numOfCities;
    private final int startingCity;
    private final int generationSize = 5000;
    private final int maxIterations = 3000;
    private final int genomeLength;
    private int numTournamentIndividuals;
    // Terminate after maxIterations if genome's length is not lower than the target path
    // length(targetFitness)
    private final int targetFitness = 0;
    private int tournamentLength;
    int[] x;    // int[numOfCities];
    int[] y;    // int[numOfCities];
    int[][] travelPrices;   // int[numOfCities][numOfCities];

    // For each city an object of type CityGenomes is created, and for each object fitness is calculated.
    // I calculate the fitness. Then there is tournament in a cycle followed by crossover and a mutation.
    // The cycle is running until current fitness becomes smaller than the target fitness. If this does
    // not happen, the program concludes according to the maximum number of iterations set.
    public TravelingSalesman(int numOfCities, int startingCity) {
        this.numOfCities = numOfCities;
        genomeLength = numOfCities-1;
        this.startingCity = startingCity;
        numTournamentIndividuals = (int)this.numOfCities * 7 / 10; // Choose number of tournament individuals
        tournamentLength = this.numOfCities; // Choose tournament length
        x = new int[numOfCities];
        y = new int[numOfCities];
        travelPrices = new int[numOfCities][numOfCities];

        List<CityGenomes> population = generateInput(this.numOfCities);

        solve(population);
    }

    public List<CityGenomes> generateInput(int numOfCities) {
        Random random = new Random();
        int min = 0;
        int max = 5000;

        for (int i = 0; i < numOfCities; i++)
        {
            x[i] = random.nextInt(max + 1 - min) + min;
            y[i] = random.nextInt(max + 1 - min) + min;
        }

        for (int i = 0; i < numOfCities; i++) {
            for (int j = 0; j < numOfCities; j++) {
                int abscissa = Math.abs(x[i] - x[j]);
                int ordinate = Math.abs(y[i] - y[j]);
                travelPrices[i][j] = (int) Math.sqrt(Math.pow(abscissa, 2) + Math.pow(ordinate, 2));
                travelPrices[i][j] = (int) Math.sqrt(Math.pow(abscissa, 2) + Math.pow(ordinate, 2));
            }
            travelPrices[i][i] = 0;
        }

        // Make not needed arrays target for garbage collection
        this.x = null;
        this.y = null;

        List<CityGenomes> population = new ArrayList<>();

        for(int i = 0; i < generationSize; i++) {
            population.add(new CityGenomes(numOfCities, travelPrices, startingCity));
        }

        return population;
    }

    public List<CityGenomes> createGeneration(List<CityGenomes> population) {
        List<CityGenomes> generation = new ArrayList<>();
        int currentGenerationSize = 0;
        while(currentGenerationSize < generationSize) {
            List<CityGenomes> parents = pickRandomElements(population,2);
            List<CityGenomes> children = crossover(parents);
            children.set(0, mutate(children.get(0)));
            children.set(1, mutate(children.get(1)));
            generation.addAll(children);
            currentGenerationSize += 2;
        }

        return generation;
    }

    // Shuffle all towns, pick some of them and select the best genom.
    public List<CityGenomes> selection(List<CityGenomes> population){
        List<CityGenomes> selectMinGenom = new ArrayList<>();

        for (int i = 0; i < numTournamentIndividuals; i++){
            selectMinGenom.add(Collections.min(pickRandomElements(population,tournamentLength)));
        }

        return selectMinGenom;
    }

    // Choose random elements
    public static <E> List<E> pickRandomElements(List<E> population, int tournamentLength) {
        Random random = new Random();
        int length = population.size();

        for (int i = 1; i <= tournamentLength; i++) {
            Collections.swap(population, random.nextInt(length) , random.nextInt(length));
        }

        return population.subList(length - tournamentLength, length);
    }

    public List<CityGenomes> crossover(List<CityGenomes> parents) {
        Random random = new Random();
        //Select a random crossover point
        int crossOverPoint  = random.nextInt(genomeLength);

        // Create new list for children
        List<CityGenomes> children = new ArrayList<>();

        // Copy parental genomes
        List<Integer> parent1Copy = new ArrayList<>(parents.get(0).getGenome());
        List<Integer> parent2Copy = new ArrayList<>(parents.get(1).getGenome());

        // One point crossover
        // Swap values among parents in order to create 2 childs
        for(int i = 0; i < crossOverPoint ; i++) {
            // We take the number on position(index) i of parent 1.
            // Then we put the value of the number on position i of parent 2 on position i of parent 1.
            // Finally we put the taken at the beginning value on position i of parent 2.
            int swapPos = parent1Copy.get(i);
            parent1Copy.set(i, parent2Copy.get(i));
            parent2Copy.set(i, swapPos);
        }
        children.add(new CityGenomes(parent1Copy, numOfCities, travelPrices, startingCity));
        children.add(new CityGenomes(parent2Copy, numOfCities, travelPrices, startingCity));

        return children;
    }

    // Mutation swaps two cities in the genome
    public CityGenomes mutate(CityGenomes salesman) {
        Random random = new Random();
        int mutationPoint1  = random.nextInt(genomeLength); // Choose first city
        int mutationPoint2  = random.nextInt(genomeLength); // Choose second city

        List<Integer> genome = salesman.getGenome();
        Collections.swap(genome, mutationPoint1, mutationPoint2); // Swap

        return new CityGenomes(genome, numOfCities, travelPrices, startingCity);
    }

    public CityGenomes solve(List<CityGenomes> population) {
        CityGenomes bestGenome = population.get(0);

        for(int i = 0; i < maxIterations; i++) {
            List<CityGenomes> selectMinGenom = selection(population); // selection(tournament)
            population = createGeneration(selectMinGenom); // crossover + mutation
            bestGenome = Collections.min(population);

            if(bestGenome.getFitness() < targetFitness) {
                // break if genome's length is lower than the target genome length
                break;
            }
            if(i == 10 || i == 20 || i == 50) {
                System.out.printf("\nLength at interation %d is: %d\n", i, bestGenome.getFitness());
            }
        }
        System.out.printf("\nLength at end: " + bestGenome.getFitness() + "\n");

        return bestGenome;
    }
}