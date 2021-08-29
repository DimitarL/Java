import java.util.ArrayList;
import java.util.List;


public class IDASearch {
    private Board initialState;     // Begin state
    private Board goalState;        // End state

    public IDASearch(Board initialState, Board goalState) {
        this.initialState = initialState;
        this.goalState = goalState;
    }

    // Start of IDA* search. It will return the last node of the optimal path. If the path does not exist,
    // null will be returned.
    public Board search() {
        // Initial F Bound
        double currentFBound = this.initialState.manhattanDistance();

        // Set Root of Path To Initial Node
        ArrayList<Board> path = new ArrayList<>();
        path.add(0, this.initialState);

        // If currentFBound == Double.MAX_VALUE, the goal node does not exist
        double smallestNewFBound;
        do {
            smallestNewFBound = recursiveSearch(path, 0, currentFBound);

            // Check if goal node is found
            if (smallestNewFBound == 0.0) {
                return path.get(path.size() - 1);
            }

            currentFBound = smallestNewFBound;  // Setting new value
        } while (currentFBound != Double.MAX_VALUE);

        return null;
    }

    // Recursively search the children. If goal node is found, 0 will be returned. Else Double.MAX_VALUE will be
    // returned if the goal node can not be found
    private double recursiveSearch(ArrayList<Board> path, double graphPrice, double currentFBound) {
        Board currentNode = path.get(path.size() - 1);

        currentNode.setG(graphPrice);
        currentNode.setF(graphPrice + currentNode.manhattanDistance());

        if (currentNode.getF() > currentFBound) {
            return currentNode.getF();
        }

        // End recursion if goal state is found
        if (currentNode.equals(this.goalState)) {
            return 0;
        }

        // If this value is not changed then all explored paths are smaller than f bound
        double minFisFound = Double.MAX_VALUE;

        List<Board> children = (List<Board>) currentNode.neighbors();

        for (Board child: children) {
            // Check whether child node is not visited
            if (!path.contains(child)) {
                // Add child to the path and continue searching
                path.add(child);
                double minFOverBound = recursiveSearch(path, currentNode.getG() + child.distanceFromParent(),
                        currentFBound);

                // End recursion if goal state is found
                if (minFOverBound == 0.0) {
                    return 0.0;
                }

                if (minFOverBound < minFisFound) {
                    minFisFound = minFOverBound;
                }

                // Remove child from search path
                path.remove(path.size() - 1);
            }
        }
        return minFisFound;
    }

    // Output optimal path from the initial state to the goal state
    public List<Board> getPath(Board endPathNode) {
        ArrayList<Board> path = new ArrayList<>();
        path.add(endPathNode);

        while (endPathNode.getParent() != null) {
            path.add(0, endPathNode.getParent());
            endPathNode = endPathNode.getParent();
        }

        System.out.println("Number of conditions: " + path.size());
        return path;
    }
}