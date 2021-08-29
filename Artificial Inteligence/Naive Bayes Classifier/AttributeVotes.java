public class AttributeVotes {
    private Integer y;
    private Integer n;

    public AttributeVotes() {
        this.y = 0;
        this.n = 0;
    }

    public void incrementVotes(String vote) {
        if (vote.equals("y")) y++;
        else if (vote.equals("n")) n++;
    }

    public double getProbability(String vote) {
        if (vote.equals("y")) return getYesProbability();
        else if (vote.equals("n")) return getNoProbability();

        return 1;
    }

    public double getYesProbability() {
        return (double) y / (y + n);
    }

    public double getNoProbability() {
        return (double) n / (y + n);
    }
}
