
/**
 * Write a description of class Move here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Move
{
    private int toRow, toCol, fromRow, fromCol;
    private final String[] letters = {"a", "b", "c", "d", "e", "f", "g", "h"};
    
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
    
    //public static Move fromString(String str, Board board){
    //}
    
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
