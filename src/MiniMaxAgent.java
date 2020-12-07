import java.util.ArrayList;

public class MiniMaxAgent {

    public static final long TIME_FOR_MOVE = 3000;

    private GameInfo gameInfo;
    private PlayerInfo playerInfo;

    private ArrayList<Integer> availablePositions;

    private ArrayList<Cell> playerPositions;

    private ArrayList<Cell> enemyPositions;


    public MiniMaxAgent(PlayerInfo playerInfo, GameInfo gameInfo) {
        this.playerInfo = playerInfo;
        this.gameInfo = gameInfo;

    }

    public int[] MinimaxDecision(){
        int[] res = new int[2];


        return res;
    }


    private ArrayList<Move> successor(String playerColor, Board board) {
        ArrayList<Move> result = new ArrayList<>();


        return result;
    }
}
