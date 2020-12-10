import java.util.ArrayList;

public class MiniMaxAgent {

    public static final long TIME_FOR_MOVE = 3000;

    private PlayerInfo playerInfo;

    private ArrayList<Integer> availablePositions;

    private ArrayList<Cell> playerPositions;

    private ArrayList<Cell> enemyPositions;


    public MiniMaxAgent(PlayerInfo playerInfo) {
        this.playerInfo = playerInfo;
    }

//    public double Max(){
//
//    }
//
//    public double Min(){
//
//    }

    public Move MinimaxDecision(Board board){
        double min = Double.NEGATIVE_INFINITY;
        double max = Double.POSITIVE_INFINITY;

        ArrayList<Move> availableMoves = board.findAllMoves(playerInfo.getColor());
        Move res = availableMoves.get(0);

        for(Move move: availableMoves){
            //double currentValue = Min();

            //double value = currentValue
        }

        return res;
    }

}
