import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class NaiveBayes {
    private int datasetLength = 0;
    private final int lineLength = 17;
    private final int numberOfAttributes = lineLength - 1;
    private int dataPartStart = 0;
    private int dataPartEnd = 0;
    private final List<DatasetInstance> instances;
    private final List<DatasetInstance> trainSet;
    private final List<DatasetInstance> validationSet;
    private List<AttributeVotes> democratVotes;
    private List<AttributeVotes> republicanVotes;

    public NaiveBayes() throws FileNotFoundException {
        instances = new ArrayList<>();
        trainSet = new ArrayList<>();
        validationSet = new ArrayList<>();

        initializeData();
    }

    private void initializeData() throws FileNotFoundException {
        // create file instance to reference text file, which is found by its name and its path is taken
        File filePath = new File(Main.class.getResource("CongressionalVotingData.csv").getPath());

        // creating Scanner instance to read File in Java
        Scanner scanner = new Scanner(filePath);

        // reading each line of file using Scanner class
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

//          System.out.println("line: " + line);
            String[] lineInfo = line.split(",");

            String className = lineInfo[0];
            String[] attributes = new String[numberOfAttributes];

            for (int i = 1; i < lineLength; i++) {
                if (lineInfo[i].equals("y")) {
                    attributes[i - 1] = "y";
                } else if (lineInfo[i].equals("n")) {
                    attributes[i - 1] = "n";
                } else {
                    attributes[i - 1] = "?";
                }
            }

            DatasetInstance instance = new DatasetInstance(className, attributes);
            instances.add(instance);

            scanner.hasNextLine();
        }

        datasetLength = instances.size();
        dataPartEnd = datasetLength;
    }

    // classify the instances from the dataset, using Naive Bayes Classifier
    public void solve() {
        double accuracySum = 0.0;

        Collections.shuffle(instances);

        for (int i = 0; i < 10; i++) {
            // split data size to train set and validation set
            splitData();
            makeModel(validationSet);
            double meanAccuracy = train();

            System.out.printf("Accuracy: %.2f%s %n", 100 * meanAccuracy, "%");

            accuracySum += meanAccuracy;
        }

        System.out.printf("Mean accuracy: %.2f%s %n", 100 * (accuracySum / 10), "%");
    }

    // split data to train set and test set
    private void splitData() {
        int oneTenth = datasetLength / 10;

        // test set
        for (int i = dataPartStart; i < dataPartStart + oneTenth; i++) {
            trainSet.add(instances.get(i));
        }

        // train set
        for (int i = 0; i < dataPartStart; i++) {
            validationSet.add(instances.get(i));
        }

        dataPartStart += oneTenth;

        for (int i = dataPartStart; i < dataPartEnd; i++) {
            validationSet.add(instances.get(i));
        }
    }

    private void makeModel(List<DatasetInstance> validationData) {
        // create lists for keeping votes for democrats and republicans
        democratVotes = new ArrayList<>();
        republicanVotes = new ArrayList<>();

        // for democrats and republicans for each attribute keep number of YES and NO
        for (int i = 0; i < numberOfAttributes; i++) {
            // create new attribute votes keeper (Yes and NO)
            AttributeVotes democratAttributeVotesKeeper = new AttributeVotes();
            AttributeVotes republicanAttributeVotesKeeper = new AttributeVotes();

            democratVotes.add(democratAttributeVotesKeeper);
            republicanVotes.add(republicanAttributeVotesKeeper);
        }

        // calculate votes
        for (DatasetInstance instance : validationData) {
            for (int i = 0; i < numberOfAttributes; i++) {
                if (instance.getClassName().equals("democrat")) {
                    AttributeVotes pickAttributeVotes = democratVotes.get(i);
                    String pickedVoteToAdd = instance.getAttributes()[i];
                    pickAttributeVotes.incrementVotes(pickedVoteToAdd);
                } else if (instance.getClassName().equals("republican")) {
                    AttributeVotes pickAttributeVotes = republicanVotes.get(i);
                    String pickedVoteToAdd = instance.getAttributes()[i];
                    pickAttributeVotes.incrementVotes(pickedVoteToAdd);
                }
            }
        }
    }

    private double train() {
        int correctPredictions = 0;

        for (DatasetInstance instance : trainSet) {
            String predictedClassName = classification(instance);
            if (instance.getClassName().equals(predictedClassName)) {
                correctPredictions++;
            }
        }

        // correct predictions / all predictions
        return (double) correctPredictions / trainSet.size();
    }

    // classify new member as democrat or as republican
    private String classification(DatasetInstance instance) {
        // calculate probabilities for being democrat and being republican
        double democratProbability = calculateProbability(instance, "democrat");
        double republicanProbability = calculateProbability(instance, "republican");

        // judge whether it is more likely to be democrat or republican
        if (democratProbability > republicanProbability) return "democrat";
        else return "republican";
    }

    // calculate probability for Democrat or probability for Republican
    private double calculateProbability(DatasetInstance instance, String className) {
        double probability = 1.0;

        if (className.equals("democrat")) {
            for (int i = 0; i < numberOfAttributes; i++) {
                probability *= democratVotes.get(i).getProbability(instance.getAttributes()[i]);
            }
        } else {
            for (int i = 0; i < numberOfAttributes; i++) {
                probability *= republicanVotes.get(i).getProbability(instance.getAttributes()[i]);
            }
        }

        return probability;
    }
}