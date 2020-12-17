import java.util.ArrayList;

public class MiniMaxAgent {

    public static final long TIME_FOR_MOVE = 3000;

    private PlayerInfo playerInfo;

    public MiniMaxAgent(PlayerInfo playerInfo) {
        this.playerInfo = playerInfo;
    }

    public double Max(int depth, Board board, double minValue){

        //check depth
        if(depth==0)
            return getUtility(board);

        double value = Double.NEGATIVE_INFINITY;

        //find available moves
        //TODO: remove .equals
        ArrayList<Move> availableMoves = board.findAllMoves(playerInfo.getColor().equals("RED") ? "BLACK" : "RED");

        if(availableMoves.size()==0)
            System.out.println("No available moves");

        //for each call min and find max
        for(Move move: availableMoves){
            value = Double.max(value,Min(depth-1, board.makeMove(move), value));
            System.out.println("max : " + value);
            if(value>minValue) break;
        }

        //return max value
        return value;
    }

    public double Min(int depth, Board board, double maxValue){
        System.out.println("");

        //check depth
        if(depth==0)
            return getUtility(board);

        double value = Double.POSITIVE_INFINITY;

        //find available moves
        //TODO: remove .equals
        ArrayList<Move> availableMoves = board.findAllMoves(playerInfo.getColor().equals("RED") ? "BLACK" : "RED");

        if(availableMoves.size()==0)
            System.out.println("No available moves");
        //for each call max and find min
        for(Move move: availableMoves){
            value = Double.min(value,Max(depth-1, board.makeMove(move), value));
            System.out.println("min : " + value);
            if(value<maxValue) break;
        }

        //return min value
        return value;
    }

    public Move MinimaxDecision(Board board){
        double value = Double.NEGATIVE_INFINITY;

        ArrayList<Move> availableMoves = board.findAllMoves(playerInfo.getColor());
        Move res = availableMoves.get(0);

        if(availableMoves.stream().anyMatch(Move::isEbashylovo)) {
            for (Move move : availableMoves) {
                if(!move.isEbashylovo())
                    continue;

                double currentValue = Min(5, board.makeMove(move), value);
                if(currentValue > value){
                    value = currentValue;
                    res = move;
                }
            }
        }else{
            for(Move move: availableMoves){
                double currentValue = Min(5, board.makeMove(move), value);
                if(currentValue > value){
                    value = currentValue;
                    res = move;
                }
            }
        }


        return res;
    }

    private double getUtility(Board board){
        return playerInfo.getColor().equals("RED") ? board.getHeuristic() : -board.getHeuristic();
    }


}
