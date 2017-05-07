import java.util.ArrayList;
/**
 * Write a description of class Move here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Move
{
    private static final String[] letters = {"a", "b", "c", "d", "e", "f", "g", "h"};
    private int toRow, toCol, fromRow, fromCol;
    
    /**
     * Constructor for Move from square to square
     * @param row1 destination square's row
     * @param col1 destination square's column
     * @param row2 originating square's row
     * @param col2 originating square's column
     */
    public Move(int row1, int col1, int row2, int col2)
    {
        toRow = row1;
        toCol = col1;
        fromRow = row2;
        fromCol = col2;
    }
    
    public int getRow(){
        return toRow;
    }
    
    public int getCol(){
        return toCol;
    }
    
    public int getFromRow(){
        return fromRow;
    }
    
    public int getFromCol(){
        return fromCol;
    }
    
    /**
     * @param board board to execute move on
     */
    public int execute(Board board){
        Piece[][] pieces = board.getPieces();
        if(fromRow == toRow && fromCol == toCol){
            return -1;
        }
        Piece piece = pieces[fromRow][fromCol];
        if(piece == null){
            return -1;
        }
        Piece piece2 = pieces[toRow][toCol];
        if(piece2 == null){
            pieces[fromRow][fromCol] = null;
            pieces[toRow][toCol] = piece.move(toRow, toCol);
            board.updateInDangers();
            return 0;
        }
        else if(piece2.getColor() == piece.getColor()){
            return -1;
        }
        pieces[fromRow][fromCol] = null;
        pieces[toRow][toCol] = piece.move(toRow, toCol);
        board.updateInDangers();
        return piece2.getValue();
    }
    
    public static Move fromString(String str, boolean color, Board board){
        if(str.substring(str.length() - 1, str.length()).equals("+") || str.substring(str.length() - 1, str.length()).equals("#")){
            str = str.substring(0, str.length() - 1);
        }
        str = str.replaceAll("([a-h]?)x", "$1");
        if(str.equals("O-O")){
            return new SpecialMove(color, true, board.getPlayerColor());
        }
        if(str.equals("O-O-O")){
            return new SpecialMove(color, false, board.getPlayerColor());
        }
        
        String[] pieceNums = {"P", "N", "B", "R", "Q", "K"};
        
        //check for pawn promotions which have "=" + letter of piece being promoted to
        int pawnPromotion = -1;
        if(str.indexOf("=") == str.length() - 2){
            pawnPromotion = indexOf(str.substring(str.length() - 1), pieceNums);
            str = str.substring(0, str.length() - 2);
        }
        
        String toSquare = str.substring(str.length() - 2);
        int toCol = indexOf(toSquare.substring(0, 1), letters);
        int toRow = 8 - Integer.parseInt(toSquare.substring(1, 2));
        int fromRow = -1;
        int fromCol = -1;
        int pieceNum = color? 0 : 6;
        if(str.length() > 2){
            pieceNum = indexOf(str.substring(0, 1), pieceNums);
            if(pieceNum >= 0){
                str = str.substring(1);
                pieceNum += color? 0 : 6;
            }
            else{
                pieceNum = color? 0 : 6;
            }
            if(str.length() > 2){
                if(str.substring(0, 1).matches("[a-h]")){
                    fromCol = indexOf(str.substring(0, 1), letters);
                }
                else{
                    fromRow = 8 - Integer.parseInt(str.substring(0, 1));
                }
            }
        }
        ArrayList<Move> moves = board.getAllMoves(color);
        for(int i = moves.size() - 1; i >= 0; i--){
            Move move = moves.get(i);
            if(move.getCol() != toCol || move.getRow() != toRow || board.getPiece(move.getFromRow(), move.getFromCol()).getNum() != pieceNum){
                moves.remove(i);
            }
            else if((fromRow > -1 && move.getFromRow() != fromRow) || (fromCol > -1 && move.getFromCol() != fromCol)){
                moves.remove(i);
            }
        }
        if(moves.size() == 1){
            if(pawnPromotion > -1){
                SpecialMove move = (SpecialMove)moves.get(0);
                move.setPromotingPiece(pawnPromotion);
                return move;
            }
            return moves.get(0);
        }
        return null;
    }
    
    private static int indexOf(String str, String[] arr){
        for(int i = 0; i < arr.length; i++){
            if(arr[i].equals(str)){
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public boolean equals(Object obj){
        if(obj instanceof Move){
            Move move = (Move)obj;
            return (toRow == move.getRow() && toCol == move.getCol() && fromRow == move.getFromRow() && fromCol == move.getFromCol());
        }
        return false;
    }
    
    @Override
    public String toString(){
        return letters[fromCol] + (8 - fromRow) + " " + letters[toCol] + (8 - toRow);
    }
}
