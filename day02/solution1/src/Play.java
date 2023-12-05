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

    public boolean isPossible(Play maxPlay) {
        if (nrRed > maxPlay.getNrRed()) {
            return false;
        }
        if (nrGreen > maxPlay.getNrGreen()) {
            return false;
        }
        return nrBlue <= maxPlay.getNrBlue();
    }

    public int getNrRed() {
        return nrRed;
    }

    public int getNrGreen() {
        return nrGreen;
    }

    public int getNrBlue() {
        return nrBlue;
    }

    @Override
    public String toString() {
        return String.format("Play{nrRed=%d, nrGreen=%d, nrBlue=%d}", nrRed, nrGreen, nrBlue);
    }
}
