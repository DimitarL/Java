class Point {
    int x;
    int y;
    int score = 0;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(int x, int y, int score) {
        this.x = x;
        this.y = y;
        this.score = score;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}