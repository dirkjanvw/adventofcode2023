public class Play {
    final private int nrRed;
    final private int nrGreen;
    final private int nrBlue;

    public Play(int nrRed, int nrGreen, int nrBlue) {
        this.nrRed = nrRed;
        this.nrGreen = nrGreen;
        this.nrBlue = nrBlue;
    }

    public static Play parsePlay(String playString) {
        int nrRed = 0, nrGreen = 0, nrBlue = 0;
        String[] playStringArray = playString.split(", ");
        for (String playStringElement : playStringArray) {
            String[] playStringElementArray = playStringElement.split(" ");
            switch (playStringElementArray[1]) {
                case "red":
                    nrRed = Integer.parseInt(playStringElementArray[0]);
                    break;
                case "green":
                    nrGreen = Integer.parseInt(playStringElementArray[0]);
                    break;
                case "blue":
                    nrBlue = Integer.parseInt(playStringElementArray[0]);
                    break;
            }
        }
        return new Play(nrRed, nrGreen, nrBlue);
    }

    public int getPower() {
        return nrRed * nrGreen * nrBlue;
    }

    public Play getSmallestSharedPlay(Play other) {
        return new Play(
            Math.max(this.nrRed, other.nrRed),
            Math.max(this.nrGreen, other.nrGreen),
            Math.max(this.nrBlue, other.nrBlue)
        );
    }

    @Override
    public String toString() {
        return String.format("Play{nrRed=%d, nrGreen=%d, nrBlue=%d}", nrRed, nrGreen, nrBlue);
    }
}
