
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
    
    /**
     * Constuctor for SpecialMove (pawn promotion with known piece being promoted to)
     * @param row1 destination square's row
     * @param col1 destination square's column
     * @param row2 originating square's row
     * @param col2 originating square's column
     * @param pieceNum piece to promote to in order of value (0 - pawn, 1 - knight, ..., 4 - queen)
     */
    public SpecialMove(int row1, int col1, int row2, int col2, int pieceNum){
        super(row1, col1, row2, col2);
        type = false;
        if(piece < 0 || piece > 4){
            pieceNum = 5;
        }
        piece = pieceNum;
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
    
    public int execute(Piece[][] pieces){
        if(type){
            if(!checkCastleValidity(pieces, pColor, color, side)){
                return -1;
            }
            
            int rookDisp, rookCol;
            if(pColor){
                rookDisp = (side)? -1 : 1;
                rookCol = (side)? 7 : 0;
            }
            else{
                rookDisp = (side)? 1 : -1;
                rookCol = (side)? 0 : 7;
            }
            int kingRow = super.getFromRow();
            int kingCol = super.getFromCol();
            int kingToCol = super.getCol();
            pieces[kingRow][kingToCol] = pieces[kingRow][kingCol].move(kingRow, kingToCol);
            pieces[kingRow][kingCol] = null;
            pieces[kingRow][kingToCol + rookDisp] = pieces[kingRow][rookCol].move(kingRow, kingToCol + rookDisp);
            pieces[kingRow][rookCol] = null;
            return 0;
        }
        else{
            if(pieces[super.getFromRow()][super.getFromCol()].getNum() != 0){
                return -1;
            }
            if(super.execute(pieces) > -1){
                Piece pawn = pieces[super.getRow()][super.getCol()];
                pieces[super.getRow()][super.getCol()] = getPiece(pawn.getColor(), pawn.getRow(), pawn.getColumn());
            }
            else{
                return -1;
            }
        }
        return 0;
    }
    
    public static boolean checkCastleValidity(Piece[][] pieces, boolean playerColor, boolean color, boolean side){
        //check if king and rook unmoved, spaces between unoccupied, king not in check,
        //and king does not cross over or end on square in which it would be in check
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
}
