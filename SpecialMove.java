
/**
 * Write a description of class SpecialMove here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class SpecialMove extends Move
{
    private boolean type, color, side, pColor;
    private int piece;
    
    /**
     * Constructor for SpecialMove (castling)
     * @param color color of castling king
     * @param kingside whether the castle is kingside or queenside
     */
    public SpecialMove(boolean color, boolean kingside, boolean playerColor){
        super(getKingRow(color, playerColor), getKingToCol(playerColor, kingside), getKingRow(color, playerColor), getKingCol(playerColor));
        type = true;
        this.color = color;
        side = kingside;
        pColor = playerColor;
    }
    
    /**
     * Constuctor for SpecialMove (pawn promotion with unknown piece being promoted to)
     * @param row1 destination square's row
     * @param col1 destination square's column
     * @param row2 originating square's row
     * @param col2 originating square's column
     */
    public SpecialMove(int row1, int col1, int row2, int col2){
        super(row1, col1, row2, col2);
        type = false;
        piece = 5;
    }
    
    public SpecialMove(Move move){
        super(move.getRow(), move.getCol(), move.getFromRow(), move.getFromCol());
        type = false;
        piece = 5;
    }
    
    private static int getKingRow(boolean color, boolean playerColor){
        int kingRow;
        if(playerColor){
            kingRow = (color)? 7 : 0;
        }
        else{
            kingRow = (color)? 0 : 7;
        }
        return kingRow;
    }
    
    private static int getKingCol(boolean playerColor){
        return (playerColor)? 4 : 3;
    }
    
    private static int getKingToCol(boolean playerColor, boolean kingside){
        int disp;
        if(playerColor){
            disp = (kingside)? 2 : -2;
        }
        else{
            disp = (kingside)? -2 : 2;
        }
        return getKingCol(playerColor) + disp;
    }
    
    private static int getRookCol(boolean playerColor, boolean side){
        int rookCol;
        if(playerColor){
            rookCol = (side)? 7 : 0;
        }
        else{
            rookCol = (side)? 0 : 7;
        }
        return rookCol;
    }
    
    /**
     * Sets piece being promoted to for pawn promotion
     * 
     * @param pieceNum piece to promote to in order of value (0 - pawn, 1 - knight, ..., 4 - queen)
     * @return whether the action was successful
     */
    public boolean setPromotingPiece(int pieceNum){
        if(type || piece < 0 || piece > 4){
            return false;
        }
        piece = pieceNum;
        return true;
    }
    
    public int execute(Board board){
        Piece[][] pieces = board.getPieces();
        if(type){
            if(!checkCastleValidity(board, pColor, color, side)){
                return -1;
            }
            int rookDisp;
            if(pColor){
                rookDisp = (side)? -1 : 1;
            }
            else{
                rookDisp = (side)? 1 : -1;
            }
            int kingRow = super.getFromRow();
            int kingCol = super.getFromCol();
            int kingToCol = super.getCol();
            int rookCol = getRookCol(pColor, side);
            pieces[kingRow][kingToCol] = pieces[kingRow][kingCol].move(kingRow, kingToCol);
            pieces[kingRow][kingCol] = null;
            pieces[kingRow][kingToCol + rookDisp] = pieces[kingRow][rookCol].move(kingRow, kingToCol + rookDisp);
            pieces[kingRow][rookCol] = null;
            board.updateInDangers();
            return 0;
        }
        else{
            if(pieces[super.getFromRow()][super.getFromCol()].getNum() != 0){
                return -1;
            }
            if(super.execute(board) > -1){
                Piece pawn = pieces[super.getRow()][super.getCol()];
                pieces[super.getRow()][super.getCol()] = getPiece(pawn.getColor(), pawn.getRow(), pawn.getColumn());
                board.updateInDangers();
                return 0;
            }
            else{
                return -1;
            }
        }
    }
    
    public static boolean checkCastleValidity(Board board, boolean playerColor, boolean color, boolean side){
        Piece[][] pieces = board.getPieces();
        int kingRow = getKingRow(color, playerColor);
        int kingCol = getKingCol(playerColor);
        int rookCol = getRookCol(playerColor, side);
        if(pieces[kingRow][kingCol] == null || pieces[kingRow][kingCol].getNum() != (color? 5 : 11)){
            return false;
        }
        if(pieces[kingRow][rookCol] == null || pieces[kingRow][rookCol].getNum() != (color? 3 : 9)){
            return false;
        }
        if(((King)pieces[kingRow][kingCol]).touched() || ((Rook)pieces[kingRow][rookCol]).touched()){
            return false;
        }
        if(board.isCheck(color)){
            return false;
        }
        if(kingCol < rookCol){
            for(int i = kingCol + 1; i < rookCol; i++){
                if(pieces[kingRow][i] != null){
                    return false;
                }
                if(Math.abs(i - kingCol) <= 2 && board.wouldBeCheck(new Move(kingRow, i, kingRow, kingCol), color)){
                    return false;
                }
            }
        }
        else{
            for(int i = kingCol - 1; i > rookCol; i--){
                if(pieces[kingRow][i] != null){
                    return false;
                }
                if(Math.abs(i - kingCol) <= 2 && board.wouldBeCheck(new Move(kingRow, i, kingRow, kingCol), color)){
                    return false;
                }
            }
        }
        return true;
    }
    
    private Piece getPiece(boolean color, int row, int col){
        switch(piece){
            case 0:
                return new Pawn(color, row, col);
            case 1:
                return new Knight(color, row, col);
            case 2: 
                return new Bishop(color, row, col);
            case 3:
                return new Rook(color, row, col);
            case 4:
                return new Queen(color, row, col);
            default:
                return new Queen(color, row, col);
        }
    }
    
    public String toString(){
        if(type){
            return ((color)? "White" : "Black") + ((side)? " Kingside" : " Queenside") + " Castle";
        }
        return "";
    }
}
