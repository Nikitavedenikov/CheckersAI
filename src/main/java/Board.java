import java.util.ArrayList;

public class Board {
    private Cell[] board;
    public final int R_SIZE = 8;
    public final int C_SIZE = 4;


    private final double[] coef = { 5, 7.75, 4, 2.5, 0.5, -3, 3 };

    public Board (Board b){
        Cell[] arr = new Cell[b.board.length];
        for (int i = 0; i < b.board.length; i++) {
            arr[i] = new Cell(b.board[i]);
        }
        this.board = arr;
    }

    public Board(Cell[] cells){

        board = new Cell[R_SIZE*C_SIZE];
        for (Cell cell: cells ) {
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
    }

    public Board makeMove(Move move){
        Board res = new Board(this);

        if(move.isEbashylovo()){
            res.board[move.getKillPosition()-1].clearCell();
        }

        res.board[move.getTo()-1].updateCell(res.board[move.getFrom()-1]);
        res.board[move.getFrom()-1].clearCell();

        if (move.getTo()== 7 || move.getTo()==0)
            res.board[move.getTo()-1].setKing(true);

        return res;
    }

    public ArrayList<Move> findAllMoves(String playerColor) {
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
    public ArrayList<Move> findMoves(String playerColor, Cell cell) {
        ArrayList<Move> result = new ArrayList<>();
        //find valid not ling moves
        int rowChange = playerColor.equals("RED") ? 1 : -1;
        String enemyColor = rowChange == 1 ? "BLACK" : "RED";

        if(!cell.isKing()){

            int row = cell.getRow() + rowChange;
            if(row >= 0 && row < R_SIZE){

                //↙↘           daun
                int col = getDown(cell.getRow(), cell.getColumn());
                int pos = getPosition(row,col);

                if(col < C_SIZE ){
                    Cell n_cell = board[pos-1];

                    if(n_cell.getColor().equals(enemyColor)){
                        //↙↘           daun one more
                        int killPos = pos;

                        col = getDown(row,col);
                        row += rowChange;
                        pos = getPosition(row,col);

                        if(row < R_SIZE && row>=0 && col >= 0 && col < C_SIZE){
                            n_cell = board[pos-1];

                            //if empty make move
                            if(n_cell.getColor().equals("EMPTY")){
                                result.add(new Move(cell.getPosition(), pos, true,killPos));
                            }
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

                    if(n_cell.getColor().equals(enemyColor)){
                        //↖↗           BBePx one more
                        int killPos = pos;

                        col = getUp(row,col);
                        row += rowChange;
                        pos = getPosition(row,col);
                        if(row < R_SIZE && row>=0 && col >= 0 && col < C_SIZE) {
                            n_cell = board[pos - 1];

                            //if empty make move
                            if (n_cell.getColor().equals("EMPTY")) {
                                result.add(new Move(cell.getPosition(), pos, true, killPos));
                            }
                        }
                    }else if(n_cell.getColor().equals("EMPTY")){
                        result.add(new Move(cell.getPosition(), pos));
                    }
                }
            }

        }else{
//            find valid king moves
            //↗           BBePx
            result.addAll(findKingMoves(cell.getPosition(), cell.getRow(), cell.getColumn(), enemyColor, Direction.UpRight));
            //↘           daun
            result.addAll(findKingMoves(cell.getPosition(), cell.getRow(), cell.getColumn(), enemyColor, Direction.DownRight));
            //↖           BBePx
            result.addAll(findKingMoves(cell.getPosition(), cell.getRow(), cell.getColumn(), enemyColor, Direction.UpLeft));
            //↙           daun
            result.addAll(findKingMoves(cell.getPosition(), cell.getRow(), cell.getColumn(), enemyColor, Direction.DownLeft));

        }


        return result;
    }

    private enum Direction{
        UpRight,
        UpLeft,
        DownRight,
        DownLeft
    }


    /*
                                          /;    ;\
                                      __  \\____//
                                     /{_\_/   `'\____
                                     \___   (o)  (o  }
          _____________________________/          :--'
      ,-,'`@@@@@@@@       @@@@@@         \_    `__\
     ;:(  @@@@@@@@@        @@@             \___(o'o)
     :: )  @@@@          @@@@@@        ,'@@(  `===='
     :: : @@@@@:          @@@@         `@@@:
     :: \  @@@@@:       @@@@@@@)    (  '@@@'
     ;; /\      /`,    @@@@@@@@@\   :@@@@@)
     ::/  )    {_----------------:  :~`,~~;
    ;;'`; :   )                  :  / `; ;
    ;;;; : :   ;                  :  ;  ; :
    `'`' / :  :                   :  :  : :
       )_ \__;      ";"          :_ ;  \_\       `,','
       :__\  \    * `,'*         \  \  :  \   *  8`;'*
           `^'     \ :/           `^'  `-^-'   \v/ :
    */
    private ArrayList<Move> findKingMoves(int startPos, int row, int col, String enemyColor, Direction dir){
        ArrayList<Move> result = new ArrayList<>();

        int pos;
        int killPos = -1;
        boolean isAttack = false;

        int rowChange = (dir == Direction.DownLeft) || (dir == Direction.UpLeft) ? -1 : 1;

        Func func =(dir == Direction.UpRight) || (dir == Direction.UpLeft) ? this::getUp : this::getDown;

        for (int i = row + rowChange, j = func.method(row,col); i < R_SIZE && i >=0 && j>=0 && j < C_SIZE; j = func.method(i, j), i+=rowChange) {
            pos = getPosition(i,j);
            if(board[pos-1].getColor().equals("EMPTY")){
                result.add(new Move(startPos, pos, isAttack,killPos));
            }else if(board[pos-1].getColor().equals(enemyColor)){
                //Move one more

                if(isAttack)
                    break;

                j = func.method(i, j);
                i+= rowChange;

                if(i >= R_SIZE || i<0 || j < 0 || j >= C_SIZE)
                    break;

                killPos = pos;
                pos = getPosition(i,j);

                //if empty make move
                if(board[pos-1].getColor().equals("EMPTY")){
                    result.add(new Move(startPos, pos, true,killPos));
                    //mark all next moves as attack
                    isAttack = true;
                }else{
                    break;
                }
            }else{
                break;
            }
        }

        return result;
    }

    /*
    _$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
    _$$$$$$$$$$$$$$$$$__$$$$$$$$$$$$$$$$
    _$$$$$$$$$$$$$$$$____$$$$$$$$$$$$$$$
    _$$$$___$$$$$$$$$____$$$$$$$$$___$$$
    _$$$$_____$$$$$$$____$$$$$$$_____$$$
    _$$$$___$__$$$$$$____$$$$$$______$$$
    _$$$$___$$__$$$$$____$$$$$__$$___$$$
    _$$$$___$$___$$$$$__$$$$$___$$___$$$
    _$$$$___$$___$$$$$__$$$$$__$$$___$$$
    _$$$$___$$$__$$$$$__$$$$$__$$$___$$$
    _$$$$___$$$__$$$$____$$$$__$$$___$$$
    _$$$$___$$$___$$$____$$$___$$$___$$$
    _$$$$___$$____$$______$$____$$___$$$
    _$$$$______$$$$$__$$__$$$$$______$$$
    _$$$$___$___$$$__$$$$__$$$___$___$$$
    _$$$$___$$_______$$$$_______$$___$$$
    _$$$$___$$$$$__$______$__$$$$$___$$$
    _$$$$___$$$$$__$$$__$$$__$$$$$___$$$
    _$$$$____________________________$$$
    _$$$$$$$$$$$$___$$__$$___$$$$$$$$$$$
    _$$$$$$$$$$$$$___$__$___$$$$$$$$$$$$
    __$$$$$$$$$$$$$________$$$$$$$$$$$$$
    ______$$$$$$$$$$$____$$$$$$$$$$$____
    __________$$$$$$$$$$$$$$$$$$________
     */
    public double getHeuristic(){
        // 0: pawns --- 5
        // 1: kings --- 7.75
        // 2: back row --- 4
        // 3: middle box --- 2.5
        // 4: middle 2 rows, not box --- 0.5
        // 5: can be taken this turn --- -3
        // 6: protected --- 3
        int[] res = new int[7];

        for (Cell cell : board){
            if(!cell.getColor().equals("EMPTY")){
                if(cell.getColor().equals("RED")){
                    if(cell.isKing())
                        res[1]++;
                    else
                        res[0]++;

                    int row = cell.getRow();
                    int col = cell.getColumn();

                    if(row==0){
                        res[2]++;
                        res[6]++;
                    }else{
                        // Check for middle rows
                        if (row == 3 || row == 4) {
                            //middle box
                            if (col == 1 || col == 2) {
                                res[3]++;
                            }
                            //middle non-box
                            else {
                                res[4]++;
                            }
                        }

                        int upLeft = getPosition(row-1, getUp(row,col))-1;
                        int upRight = getPosition(row+1, getUp(row,col))-1;
                        int downLeft = getPosition(row-1, getDown(row,col))-1;
                        int downRight = getPosition(row+1, getDown(row,col))-1;

                        // Check if can be taken this turn
                        if (row < 7) {
                            if (col > 0 && col < 3) {
                                if (board[upRight].getColor().equals("BLACK")
                                        && board[downLeft].getColor().equals("EMPTY")) {
                                    res[5]++;
                                }
                                if (board[downRight].getColor().equals("BLACK")
                                        && board[upLeft].getColor().equals("EMPTY")) {
                                    res[5]++;
                                }
                            }
                        }

                        // Check for protected checkers
                        if (row > 0) {
                            if (col == 0 || col == 3) {
                                res[6]++;
                            }
                            else {
                                if ((board[upLeft].getColor().equals("RED") || !board[upLeft].isKing()) &&
                                    (board[downLeft].getColor().equals("RED") || !board[downLeft].isKing())) {
                                    res[6]++;
                                }
                            }
                        }
                    }
                }else{
                    if(cell.isKing())
                        res[1]--;
                    else
                        res[0]--;

                    int row = cell.getRow();
                    int col = cell.getColumn();

                    if(row==7){
                        res[2]--;
                        res[6]--;
                    }else{
                        // Check for middle rows
                        if (row == 3 || row == 4) {
                            //middle box
                            if (col == 1 || col == 2) {
                                res[3]--;
                            }
                            //middle non-box
                            else {
                                res[4]--;
                            }
                        }

                        int upLeft = getPosition(row-1, getUp(row,col))-1;
                        int upRight = getPosition(row+1, getUp(row,col))-1;
                        int downLeft = getPosition(row-1, getDown(row,col))-1;
                        int downRight = getPosition(row+1, getDown(row,col))-1;

                        // Check if can be taken this turn
                        if (row > 0) {
                            if (col > 0 && col < 3) {
                                if (board[upLeft].getColor().equals("RED")
                                        && board[downRight].getColor().equals("EMPTY")) {
                                    res[5]--;
                                }
                                if (board[downLeft].getColor().equals("RED")
                                        && board[upRight].getColor().equals("EMPTY")) {
                                    res[5]--;
                                }
                            }
                        }

                        // Check for protected checkers
                        if (row < 7) {
                            if (col == 0 || col == 3) {
                                res[6]--;
                            }
                            else {
                                if ((board[upRight].getColor().equals("BLACK") || !board[upRight].isKing()) &&
                                        (board[downRight].getColor().equals("BLACK") || !board[downRight].isKing())) {
                                    res[6]--;
                                }
                            }
                        }
                    }
                }
            }
        }

        double sum = 0.0;
        for (int i = 0; i < 7; i++) {
            sum += res[i]*this.coef[i];
        }

        return sum;
    }


    @FunctionalInterface
    public interface Func {
        int method(int i, int j);
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
