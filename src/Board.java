import java.util.ArrayList;

public class Board {

    private Cell[] board;
    public final int R_SIZE = 8;
    public final int C_SIZE = 4;

    public Board(GameInfo gameInfo){

        board = new Cell[R_SIZE*C_SIZE];
        for (Cell cell: gameInfo.getBoard() ) {
            board[cell.getPosition()-1] = cell;

        }
        /*
                              .
        ---\    __            |\
            \__/__\___________| \_
            |   ___    |  ,|   ___`-.
            |  /   \   |___/  /   \  `-.
            |_| (O) |________| (O) |____|
               \___/          \___/
         */
        int pos;
        for (int i = 0; i < R_SIZE; i++) {
            for(int j = 0; j < C_SIZE; j++){
                pos = getPosition(i,j);
                if(board[pos-1] == null)
                    board[pos-1] = new Cell("EMPTY", i, j,false,pos);
            }
        }

        board[17] = board[4];
        board[4] = new Cell("EMPTY", 1, 0,false,5);
        //board[22].setKing(true);

        ArrayList<Move> moves = findMoves("BLACK", board[22]);

        System.out.println("MOVES : \n");
        for (Move move:
             moves) {
            System.out.println("\n" + move);
        }
    }

    private ArrayList<Move> findAllMoves(String playerColor) {
        ArrayList<Move> result = new ArrayList<>();

        for (Cell cell: board) {
            if(cell.getColor().equals(playerColor))
                result.addAll(findMoves(playerColor, cell));
        }

        return result;
    }

    /*
    __________________$$$
___________________$$$$
____________________$$$$$
_____________________$$$$$
___________________$$$$$$$
__________________$$$$$$$$
__________________$$$$$$$
_________$$$$___$$$$$$$$$
__________$$$$__$$$$$$$$
___________$$$$$$$$$$$$$
___________$$$$$$$$$$$$
________$$$$$$$$$$$$$$$
__$$$$$$$$$$$$$$$$$$$$
$$$$$$$$$$$$$$$$$$$$$$
$$$$$$$$$$$$$$$$$$$$$
$$$$$$$$$___$$$$$$$$
$$$$$$$_____$$$$$$$$
_$$$$$$$____$$$$$$$$
_$$$$$$$$__$$$$$$$$$
_$$$$$$$$$_$$$$$$$$$
__$$$$$$$$$$$$$$$$$
__$$$$$$$$$$$$$$$$
__$$$$$$$$$$$$$$$$
__$$$$$$$$$$$$$$$
__$$$$$$$$$$$$$$$
___$$$$$$$$$$$$$$
____$$$$$$$$$$$$$$$
____$$$$$$$$$$$$$$$$$
____$$$$$$$$$$$$_$$$$$
____$$$$$$$$$$$$__$$$$$
___$$$$$$$$$$$$$$__$$$$$
___$$$$$$$$$$$$$$___$$$$$$
____$$$$$$$$$$$$$$$$$$$$$$
_____$$$$$$$$$$$$$$$$$$$$
______$$$$$$$$$$$$$$$$$
______$$$$$$$$$$$$$$$$
_______$$$$$$$$$$$$$$$
_______$$$$$$$$$$$$$$
_________$$$$$$$$$$$$$$
_________$$$$$$$$$$$$$$$
_________$$$$$$$$$$$$$$$$
_________$$$$$$$$$$$$$$$$
_________$$$$$___$$$$$$$
_________$$$$$
_________$$$$$
_________$$$$$
_________$$$$$
_________$$$$
__________$$$
__________$$$$$$$
____________$$$$$$$$
     */
    private ArrayList<Move> findMoves(String playerColor, Cell cell) {
        ArrayList<Move> result = new ArrayList<>();
        //find valid not ling moves
        int rowChange = playerColor.equals("RED") ? 1 : -1;

        //check valid hit moves, if not empty return

        //else
        if(!cell.isKing()){

            //TODO: if attacked try to go further on direction and find another attack
            int row = cell.getRow() + rowChange;
            if(row >= 0 || row < R_SIZE){

                //↙↘           daun
                int col = getDown(cell.getRow(), cell.getColumn());
                int pos = getPosition(row,col);
                if(col < C_SIZE ){
                    Cell n_cell = board[pos-1];

                    if(n_cell.getColor().equals(rowChange == 1 ? "BLACK" : "RED")){
                        //↙↘           daun one more
                        col = getDown(row,cell.getColumn());
                        row += rowChange;
                        pos = getPosition(row,col);
                        n_cell = board[pos-1];

                        //if empty make move
                        if(n_cell.getColor().equals("EMPTY")){
                            result.add(new Move(cell.getPosition(), pos, true));
                        }
                    }else if(n_cell.getColor().equals("EMPTY")){
                        result.add(new Move(cell.getPosition(), pos));
                    }
                }

                //↖↗           BBePx
                row = cell.getRow() + rowChange;
                col = getUp(cell.getRow(),cell.getColumn());
                pos = getPosition(row,col);
                if(col >=0){
                    Cell n_cell = board[pos-1];

                    if(n_cell.getColor().equals(rowChange == 1 ? "BLACK" : "RED")){
                        //↖↗           BBePx one more
                        col = getUp(row,col);
                        row += rowChange;
                        pos = getPosition(row,col);
                        n_cell = board[pos-1];

                        //if empty make move
                        if(n_cell.getColor().equals("EMPTY")){
                            result.add(new Move(cell.getPosition(), pos, true));
                        }
                    }else if(n_cell.getColor().equals("EMPTY")){
                        result.add(new Move(cell.getPosition(), pos));
                    }
                }
            }

        }else{
            //find valid king moves
            int row = cell.getRow();
            int col = cell.getColumn();
            int pos;
            boolean isAttack = false;
            //↗           BBePx
            for (int i = row + 1, j = getUp(row,col); i < R_SIZE && j>=0 ; j = getUp(i, j), i++) {
                pos = getPosition(i,j);
                if(board[pos-1].getColor().equals("EMPTY")){
                    result.add(new Move(cell.getPosition(), pos, isAttack));
                }else if(board[pos-1].getColor().equals(rowChange == 1 ? "BLACK" : "RED")){
                    //↗           BBePx one more
                    j = getUp(i, j);
                    i ++;

                    if(i >= R_SIZE || j < 0)
                        break;

                    pos = getPosition(i,j);

                    //if empty make move
                    if(board[pos-1].getColor().equals("EMPTY")){
                        result.add(new Move(cell.getPosition(), pos, true));
                        //mark all next moves as attack
                        isAttack = true;
                    }else{
                        break;
                    }
                }else{
                    break;
                }
            }

            //↘           daun
            isAttack = false;
            for (int i = row + 1, j = getDown(row,col); i < R_SIZE && j < C_SIZE ; j = getDown(i,j), i++ ) {

                pos = getPosition(i,j);
                if(board[pos-1].getColor().equals("EMPTY")){
                    result.add(new Move(cell.getPosition(), pos, isAttack));
                }else if(board[pos-1].getColor().equals(rowChange == 1 ? "BLACK" : "RED")){
                    //↘           daun one more
                    j = getDown(i,j);
                    i ++;

                    if(i >= R_SIZE || j >= C_SIZE)
                        break;

                    pos = getPosition(i,j);

                    //if empty make move
                    if(board[pos-1].getColor().equals("EMPTY")){
                        result.add(new Move(cell.getPosition(), pos, true));
                        //mark all next moves as attack
                        isAttack = true;
                    }else{
                        break;
                    }
                }else{
                    break;
                }
            }

            isAttack = false;
            //↖          BBePx
            for (int i = row - 1, j = getUp(row, col); i >= 0 && j >= 0 ; j = getUp(i, j), i--) {
                pos = getPosition(i,j);
                if(board[pos-1].getColor().equals("EMPTY")){
                    result.add(new Move(cell.getPosition(), pos, isAttack));
                }else if(board[pos-1].getColor().equals(rowChange == 1 ? "BLACK" : "RED")){
                    //↖           BBePx one more
                    j = getUp(i, j);
                    i --;

                    if(i < 0 || j < 0)
                        break;

                    pos = getPosition(i,j);

                    //if empty make move
                    if(board[pos-1].getColor().equals("EMPTY")){
                        result.add(new Move(cell.getPosition(), pos, true));
                        //mark all next moves as attack
                        isAttack = true;
                    }else{
                        break;
                    }
                }else{
                    break;
                }
            }

            isAttack = false;
            //↙           daun
            for (int i = row - 1, j = getDown(row,col); i >=0 && j < C_SIZE ; j = getDown(i,j), i--) {

                pos = getPosition(i,j);
                if(board[pos-1].getColor().equals("EMPTY")){
                    result.add(new Move(cell.getPosition(), pos, isAttack));
                }else if(board[pos-1].getColor().equals(rowChange == 1 ? "BLACK" : "RED")){
                    //↙           daun one more
                    j = getDown(i,j);
                    i--;

                    if(i < 0 || j >= C_SIZE)
                        break;

                    pos = getPosition(i,j);

                    //if empty make move
                    if(board[pos-1].getColor().equals("EMPTY")){
                        result.add(new Move(cell.getPosition(), pos, true));
                        //mark all next moves as attack
                        isAttack = true;
                    }else{
                        break;
                    }
                }else{
                    break;
                }
            }

        }

        return result;
    }

    private int getPosition(int row, int col){
        return (row*C_SIZE + col)+1;
    }

    private int getUp(int i, int j){
        return (i%2 ==0) ? j : j-1;
    }

    private int getDown(int i, int j){
        return (i%2 == 0) ? j + 1 : j;
    }
}
