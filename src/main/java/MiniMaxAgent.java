import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

public class MiniMaxAgent {

    private final CELL_COLOR playerColor;

    //private Tree<Move> movesTree;

    private Move lastAttackMove = null;

    public MiniMaxAgent(PlayerInfo playerInfo) {
        playerColor = playerInfo.getColor();
    }

    public Move MinimaxDecision(Board board, int depth){
        double value = Double.NEGATIVE_INFINITY;

        ArrayList<Move> availableMoves = board.findAllMoves(playerColor);

        Move res = availableMoves.get(0);

        //movesTree = new Tree<>(res);

        if(availableMoves.stream().anyMatch(Move::isAttack)) {

            //for each call min and find max
            for(Move move: availableMoves){
                if(!move.isAttack())
                    continue;

                if(lastAttackMove!=null && lastAttackMove.getTo() != move.getFrom())
                    continue;

//                movesTree.addLeaf(move);
                double currentValue = Double.max(value,Max(depth,  board.makeMove(move), Double.NEGATIVE_INFINITY,move.getTo()));
//                move.setValue(currentValue);

                if(currentValue > value){
                    value = currentValue;
                    res = move;
                    lastAttackMove = res;
                }

                System.out.println("Move : " + move.toString() + "    Value : " + currentValue);
            }

        }else{
            for(Move move: availableMoves){

//                movesTree.addLeaf(move);
                //System.out.println("sda");
                double currentValue = Min(depth, board.makeMove(move), value,-1);
//                move.setValue(currentValue);

                if(currentValue > value){
                    value = currentValue;
                    res = move;
                }

                System.out.println("Move : " + move.toString() + "    Value : " + currentValue);
            }

            lastAttackMove = null;
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

        if(availableMoves.stream().anyMatch(Move::isAttack) ){
            //for each call min and find max
            for(Move move: availableMoves){
                if(!move.isAttack())
                    continue;
//                movesTree.addLeaf(lastMove,move);

                Board boardClone = board.makeMove(move);
                value = Double.max(value,Max(depth-1, boardClone , Double.NEGATIVE_INFINITY, move.getTo()));
//                move.setValue(value);
            }
        }
        else{
            if(attackPos!=-1) {
                value = Double.max(value,Min(depth-1,  board, value,-1));
            }else{
                //for each call min and find max
                for(Move move: availableMoves){
//                    movesTree.addLeaf(lastMove,move);
                    value = Double.max(value,Min(depth-1,  board.makeMove(move), value,-1));
//                    move.setValue(value);

                    //System.out.println("max : " + value);
                    if(value>=minValue) break;

                }
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

        if(availableMoves.stream().anyMatch(Move::isAttack)){
            //for each call min and find max
            for(Move move: availableMoves){
                if(!move.isAttack())
                    continue;
//                movesTree.addLeaf(lastMove,move);
                Board boardClone = board.makeMove(move);
                value = Double.min(value,Min(depth - 1, boardClone , Double.POSITIVE_INFINITY, move.getTo()));
//                move.setValue(value);
            }
        }else {
            if(attackPos!=-1)
                value = Double.min(value, Max(depth - 1, board, value, -1));
            else {
                //for each call max and find min
                for (Move move : availableMoves) {
//                    movesTree.addLeaf(lastMove, move);
                    value = Double.min(value, Max(depth - 1, board.makeMove(move), value, -1));
//                    move.setValue(value);

                    //System.out.println("min : " + value);
                    if (value <= maxValue) break;
                }
            }
        }

        //return min value
        return value;
    }

    public void resetLastMove(){
        lastAttackMove = null;
    }


    private double getUtility(Board board){
        //return board.getHeuristic(playerColor);
        return playerColor == CELL_COLOR.RED ? board.getHeuristic() : -board.getHeuristic();
    }


}
