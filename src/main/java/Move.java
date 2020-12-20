public class Move {
    private int from;
    private int to;
    private boolean isAttack;
    private int killPosition;

    public Move(int from, int to) {
        this.from = from;
        this.to = to;
        this.isAttack = false;
        killPosition = -1;
    }

    public Move(int from, int to, boolean isAttack, int killPosition) {
        this.from = from;
        this.to = to;
        this.isAttack = isAttack;
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

    public boolean isAttack() {
        return isAttack;
    }

    public void setAttack(boolean attack) {
        isAttack = attack;
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
        strbd.append("From : ").append(from).append("\tTo : ").append(to).append("\tAttack : ").append(isAttack);
        return strbd.toString();

    }
}
