import java.util.*;

public class CityGenomes implements Comparable {
    int numOfCities = 0;
    int[][] travelPrices;
    int startingCity;
    List<Integer> genome;
    int fitness;

    public CityGenomes(int numOfCities, int[][] travelPrices, int startingCity) {
        this.numOfCities = numOfCities;
        this.travelPrices = travelPrices;
        this.startingCity = startingCity;
        genome = randomSalesman();
        fitness = this.calculateFitness();
    }

    public CityGenomes(List<Integer> permutationOfCities, int numOfCities, int[][] travelPrices, int startingCity) {
        this.numOfCities = numOfCities;
        this.travelPrices = travelPrices;
        this.startingCity = startingCity;
        genome = permutationOfCities;
        fitness = this.calculateFitness();
    }

    public int calculateFitness() {
        int fitness = 0;
        int currentCity = startingCity;

        // Calculate path cost for every gene in the genome
        for (int gene : genome) {
            fitness += travelPrices[currentCity][gene];
            currentCity = gene;
        }
        fitness += travelPrices[genome.get(numOfCities-2)][startingCity];

        return fitness;
    }

    private List<Integer> randomSalesman() {
        List<Integer> result = new ArrayList<Integer>();
        Random random = new Random();

        for(int i = 1; i < numOfCities; i++) {
            result.add(i);
        }

        int endOfShuffle = (int)result.size() / 2;
        int resultLength = result.size();

        for (int i = 1; i <= endOfShuffle; i++) {
            Collections.swap(result, random.nextInt(resultLength), random.nextInt(resultLength));
        }

        return result;
    }

    public List<Integer> getGenome() {
        return genome;
    }

    public int getFitness() {
        return fitness;
    }

    @Override
    public int compareTo(Object obj) {
        CityGenomes genome = (CityGenomes) obj;
        if(this.fitness > genome.getFitness()) {
            return 1;
        }
        else if(this.fitness < genome.getFitness()) {
            return -1;
        }
        else {
            return 0;
        }
    }
}