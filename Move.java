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
     * Constructor for objects of class Move
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
    public int execute(Piece[][] pieces){
        Piece piece = pieces[fromRow][fromCol];
        Piece piece2 = pieces[toRow][toCol];
        if(piece2 == null){
            pieces[fromRow][fromCol] = null;
            pieces[toRow][toCol] = piece.move(toRow, toCol);
            return 0;
        }
        else if(piece2.getColor() == piece.getColor()){
            return -1;
        }
        pieces[fromRow][fromCol] = null;
        pieces[toRow][toCol] = piece.move(toRow, toCol);
        return piece2.getValue();
    }
    
    public static Move fromString(String str, boolean color, Board board){
        //check for castling first
        //also check for pawn promotions which have "=" + letter of piece being promoted to
        
        String toSquare = str.substring(str.length() - 2);
        int toCol = indexOf(toSquare.substring(0, 1));
        int toRow = 8 - Integer.parseInt(toSquare.substring(1, 2));
        ArrayList<Move> moves = board.getAllMoves(color);
        for(int i = moves.size() - 1; i >= 0; i--){
            if(moves.get(i).getCol() != toCol || moves.get(i).getRow() != toRow){
                moves.remove(i);
            }
        }
        if(moves.size() == 1){
            return moves.get(0);
        }
        
        
        
        return null;
    }
    
    private static int indexOf(String letter)
    {
        for(int i = 0; i < letters.length; i++){
            if(letters[i].equals(letter)){
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
