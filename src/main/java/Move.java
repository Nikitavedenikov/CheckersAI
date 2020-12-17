public class Move {
    private int from;
    private int to;
    private boolean isEbashylovo;
    private int killPosition;

    public Move(int from, int to) {
        this.from = from;
        this.to = to;
        this.isEbashylovo = false;
        killPosition = -1;
    }

    public Move(int from, int to, boolean isEbashylovo, int killPosition) {
        this.from = from;
        this.to = to;
        this.isEbashylovo = isEbashylovo;
        this.killPosition = killPosition;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public boolean isEbashylovo() {
        return isEbashylovo;
    }

    public void setEbashylovo(boolean ebashylovo) {
        isEbashylovo = ebashylovo;
    }

    public int getKillPosition() {
        return killPosition;
    }

    public void setKillPosition(int killPosition) {
        this.killPosition = killPosition;
    }

    @Override
    public String toString() {
        StringBuilder strbd = new StringBuilder();
        strbd.append("From : ").append(from).append("\tTo : ").append(to).append("\tEbashylovo : ").append(isEbashylovo);
        return strbd.toString();

    }
}
