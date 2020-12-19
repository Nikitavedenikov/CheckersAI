import java.util.ArrayList;

public class MiniMaxAgent {

    public static final long TIME_FOR_MOVE = 3000;

    private PlayerInfo playerInfo;

    public MiniMaxAgent(PlayerInfo playerInfo) {
        this.playerInfo = playerInfo;
    }

    public Move MinimaxDecision(Board board, int depth){
        double value = Double.NEGATIVE_INFINITY;

        ArrayList<Move> availableMoves = board.findAllMoves(playerInfo.getColor());
        Move res = availableMoves.get(0);

        if(availableMoves.stream().anyMatch(Move::isEbashylovo)) {
            //for each call min and find max
            for(Move move: availableMoves){
                if(!move.isEbashylovo())
                    continue;

                ArrayList<Move> clone = new ArrayList<>();
                clone.add(move);
                double currentValue = Double.max(value,Max(depth,  board.makeMove(move), Double.NEGATIVE_INFINITY, clone,move.getTo()));

                //System.out.println("sda");
                if(currentValue > value){
                    value = currentValue;
                    res = move;
                }

                System.out.println("Move : " + move.toString() + "    Value : " + currentValue);
            }
        }else{
            for(Move move: availableMoves){
                ArrayList< Move> arr = new ArrayList<>();
                arr.add(move);
                Board copy  = board.makeMove(move);
                //System.out.println("sda");
                double currentValue = Min(depth, copy, value, arr,-1);
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

    public double Max(int depth, Board board, double minValue, ArrayList<Move> movesBefore, int attackPos){

        //check depth
        if(depth==0)
            return getUtility(board);

        double value = Double.NEGATIVE_INFINITY;

        //find available moves
        //TODO: remove .equals
        ArrayList<Move> availableMoves;
        if(attackPos == -1)
            availableMoves = board.findAllMoves(playerInfo.getColor());
        else
            availableMoves = board.findAttackMoves(attackPos);

        if(availableMoves.stream().anyMatch(Move::isEbashylovo) ){
            //for each call min and find max
            for(Move move: availableMoves){
                if(!move.isEbashylovo())
                    continue;

                ArrayList<Move> clone = (ArrayList<Move>) movesBefore.clone();
                clone.add(move);
                Board boardClone = board.makeMove(move);
                value = Double.max(value,Max(depth-1, boardClone , Double.NEGATIVE_INFINITY, clone, move.getTo()));

            }
        }
        else{
            if(attackPos!=-1)
                availableMoves = board.findAllMoves(playerInfo.getColor());
            //for each call min and find max
            for(Move move: availableMoves){
                ArrayList<Move> clone = (ArrayList<Move>) movesBefore.clone();
                clone.add(move);
                value = Double.max(value,Min(depth-1,  board.makeMove(move), value, clone,-1));
                //System.out.println("max : " + value);
                if(value>minValue) break;
            }
        }

        //return max value
        return value;
    }

    public double Min(int depth, Board board, double maxValue, ArrayList<Move> movesBefore, int attackPos){
        //System.out.println("");

        //check depth
        if(depth==0)
            return getUtility(board);

        double value = Double.POSITIVE_INFINITY;

        ArrayList<Move> availableMoves;

        //find available moves
        //TODO: remove .equals
        if(attackPos==-1)
            availableMoves = board.findAllMoves(playerInfo.getColor().equals("RED") ? "BLACK" : "RED");
        else
            availableMoves = board.findAttackMoves(attackPos);

        if(availableMoves.stream().anyMatch(Move::isEbashylovo)){
            //for each call min and find max
            for(Move move: availableMoves){
                if(!move.isEbashylovo())
                    continue;

                ArrayList<Move> clone = (ArrayList<Move>) movesBefore.clone();
                clone.add(move);
                Board boardClone = board.makeMove(move);
                value = Double.min(value,Min(depth - 1, boardClone , Double.POSITIVE_INFINITY, clone, move.getTo()));

            }
        }else {
            if(attackPos!=-1)
                availableMoves = board.findAllMoves(playerInfo.getColor());
            //for each call max and find min
            for (Move move : availableMoves) {
                ArrayList<Move> clone = (ArrayList<Move>) movesBefore.clone();
                clone.add(move);
                value = Double.min(value, Max(depth - 1, board.makeMove(move), value, clone, -1));
                //System.out.println("min : " + value);
                if (value < maxValue) break;
            }
        }

        //return min value
        return value;
    }


    private double getUtility(Board board){
        return playerInfo.getColor().equals("RED") ? board.getHeuristic() : -board.getHeuristic();
    }


}
