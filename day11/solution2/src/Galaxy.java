public class Galaxy {
    private int x;
    private int y;

    public Galaxy(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Calculate the distance between this galaxy and another. The distance is
     * calculated by going from this galaxy to the other galaxy using only
     * horizontal and vertical steps.
     * @param other the other galaxy
     * @return the distance between this galaxy and the other
     */
    public long distanceTo(Galaxy other) {
        return (long) Math.abs(this.x - other.x) + Math.abs(this.y - other.y);
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return this.x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return this.y;
    }

    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }
}
