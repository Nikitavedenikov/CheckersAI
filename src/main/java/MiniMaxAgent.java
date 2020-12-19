import java.util.ArrayList;

public class MiniMaxAgent {

    public static final long TIME_FOR_MOVE = 3000;

    private final CELL_COLOR playerColor;

    public MiniMaxAgent(PlayerInfo playerInfo) {
        playerColor = playerInfo.getColor();
    }

    public Move MinimaxDecision(Board board, int depth){
        double value = Double.NEGATIVE_INFINITY;

        ArrayList<Move> availableMoves = board.findAllMoves(playerColor);
        Move res = availableMoves.get(0);

        if(availableMoves.stream().anyMatch(Move::isEbashylovo)) {
            //for each call min and find max
            for(Move move: availableMoves){
                if(!move.isEbashylovo())
                    continue;

                double currentValue = Double.max(value,Max(depth,  board.makeMove(move), Double.NEGATIVE_INFINITY,move.getTo()));

                //System.out.println("sda");
                if(currentValue > value){
                    value = currentValue;
                    res = move;
                }

                System.out.println("Move : " + move.toString() + "    Value : " + currentValue);
            }
        }else{
            for(Move move: availableMoves){
                //System.out.println("sda");
                double currentValue = Min(depth, board.makeMove(move), value,-1);
                if(currentValue > value){
                    value = currentValue;
                    res = move;
                }

                System.out.println("Move : " + move.toString() + "    Value : " + currentValue);
            }
        }

        System.out.println("RES: Move " + res.toString() + "    Value " + value);
        return res;
    }

    public double Max(int depth, Board board, double minValue, int attackPos){

        //check depth
        if(depth==0)
            return getUtility(board);

        double value = Double.NEGATIVE_INFINITY;

        //find available moves
        ArrayList<Move> availableMoves;
        if(attackPos == -1)
            availableMoves = board.findAllMoves(playerColor);
        else
            availableMoves = board.findAttackMoves(attackPos);

        if(availableMoves.stream().anyMatch(Move::isEbashylovo) ){
            //for each call min and find max
            for(Move move: availableMoves){
                if(!move.isEbashylovo())
                    continue;

                Board boardClone = board.makeMove(move);
                value = Double.max(value,Max(depth-1, boardClone , Double.NEGATIVE_INFINITY, move.getTo()));

            }
        }
        else{
            if(attackPos!=-1)
                availableMoves = board.findAllMoves(playerColor);
            //for each call min and find max
            for(Move move: availableMoves){
                value = Double.max(value,Min(depth-1,  board.makeMove(move), value,-1));
                //System.out.println("max : " + value);
                if(value>minValue) break;
            }
        }

        //return max value
        return value;
    }

    public double Min(int depth, Board board, double maxValue, int attackPos){
        //System.out.println("");

        //check depth
        if(depth==0)
            return getUtility(board);

        double value = Double.POSITIVE_INFINITY;

        ArrayList<Move> availableMoves;

        //find available moves
        if(attackPos==-1)
            availableMoves = board.findAllMoves(playerColor == CELL_COLOR.RED ? CELL_COLOR.BLACK : CELL_COLOR.RED);
        else
            availableMoves = board.findAttackMoves(attackPos);

        if(availableMoves.stream().anyMatch(Move::isEbashylovo)){
            //for each call min and find max
            for(Move move: availableMoves){
                if(!move.isEbashylovo())
                    continue;

                Board boardClone = board.makeMove(move);
                value = Double.min(value,Min(depth - 1, boardClone , Double.POSITIVE_INFINITY, move.getTo()));

            }
        }else {
            if(attackPos!=-1)
                availableMoves = board.findAllMoves(playerColor);
            //for each call max and find min
            for (Move move : availableMoves) {
                value = Double.min(value, Max(depth - 1, board.makeMove(move), value, -1));
                //System.out.println("min : " + value);
                if (value < maxValue) break;
            }
        }

        //return min value
        return value;
    }


    private double getUtility(Board board){
        return playerColor == CELL_COLOR.RED ? board.getHeuristic() : -board.getHeuristic();
    }


}
