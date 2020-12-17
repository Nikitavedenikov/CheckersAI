public class GameInfo {
    private String status;
    private String whose_turn;
    private String winner;

    private Cell[] board;

    private double available_time;
    private boolean is_started;
    private boolean is_finished;

    public static final int SIZE = 32;

    public GameInfo(String status, String whose_turn, String winner, Cell[] board, double available_time, boolean is_started, boolean is_finished) {
        this.status = status;
        this.whose_turn = whose_turn;
        this.winner = winner;
        this.board = board;
        this.available_time = available_time;
        this.is_started = is_started;
        this.is_finished = is_finished;
    }

    private int[] allCells;

    public String getStatus() {
        return status;
    }

    public String getWhose_turn() {
        return whose_turn;
    }

    public String getWinner() {
        return winner;
    }

    public Cell[] getBoard() {
        return board;
    }

    public double getAvailable_time() {
        return available_time;
    }

    public boolean is_started() {
        return is_started;
    }

    public boolean is_finished() {
        return is_finished;
    }

}

class Cell{
    private String color;
    private int row;
    private int column;
    private boolean king;
    private int position;

    public void setKing(boolean king) {
        this.king = king;
    }
    public Cell(Cell c) {
        this(c.getColor(), c.getRow(), c.getColumn(), c.isKing(), c.getPosition());
    }

    public Cell(String color, int row, int column, boolean king, int position) {
        this.color = color;
        this.row = row;
        this.column = column;
        this.king = king;
        this.position = position;
    }

    public void updatePosition(Cell newCell){
        this.row = newCell.getRow();
        this.column = newCell.getColumn();
        this.position = newCell.getPosition();
    }

    public void clearCell(){
        this.color = "EMPTY";
        this.king = false;
    }

    public String getColor() {
        return color;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public boolean isKing() {
        return king;
    }

    public int getPosition() {
        return position;
    }

    public void updateCell(Cell cell) {
        this.color = cell.getColor();
        this.king = cell.isKing();
    }
}
