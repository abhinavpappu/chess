import java.util.ArrayList;
/**
 * Represents a Chess pawn
 * 
 * @author Abhinav Pappu
 */
public class Pawn implements Piece
{
    private boolean color, isInDanger;
    private int row, col;
    
    /**
     * Constructor for a Pawn
     * @param color color of piece (true - white, false - black)
     * @param row the row of this piece
     * @param col the column of this piece
     */
    public Pawn(boolean color, int row, int col) {
        this.color = color;
        move(row, col);
    }
    
    /**
     * Gets the value of this piece
     * @return piece's value
     */
    public int getValue() {
        return 1;
    }
    
    /**
     * Gets number of this piece (used for generating input for the neural network)
     * @return piece's number
     */
    public int getNum(){
        return color? 0 : 6;
    }
    
    /**
     * Gets unicode icon of this piece
     * @return piece's unicode icon
     */
    public String getIcon(){
        return "\u265F";
    }
    
    /**
     * Gets color of this piece
     * @return piece's color
     */
    public boolean getColor(){
        return color;
    }
    
    /**
     * Gets row of this piece
     * @return piece's row
     */
    public int getRow() {
        return row;
    }
    
    /**
     * Gets column of this piece
     * @retun piece's column
     */
    public int getColumn() {
        return col;
    }
    
    /**
     * Gets whether this piece is in danger of being attacked by an opposing piece
     * @return boolean indicating whether this piece is in danger
     */
    public boolean isInDanger(){
        return isInDanger;
    }
    
    /**
     * Sets the danger status of this piece
     * @param inDanger boolean indicating whether this piece is in danger
     */
    public void setInDanger(boolean inDanger){
        isInDanger = inDanger;
    }
    
    /**
     * Gets all possible moves of this piece
     * @param board board that contains this piece
     * @param removeCheck whether to remove the moves that cause a check on the piece's side
     * @return an ArrayList of possible moves
     */
    public ArrayList<Move> getMoves(Board board, boolean removeCheck) {
        //need to add en passant
        ArrayList<Move> moves = new ArrayList<Move>();
        int dir = (color == board.getPlayerColor())? -1 : 1;
        if(row + dir <= 7 && row + dir >= 0 && board.getPiece(row + dir, col) == null) {
            moves.add(new Move(row + dir, col, row, col));
        }
        if(row == 3.5 - dir * 2.5 && board.getPiece((int)(3.5 - dir * 1.5), col) == null && board.getPiece(row + 2 * dir, col) == null) {
            moves.add(new Move(row + 2*dir, col, row, col));
        }
        if(row + dir <= 7 && row + dir >= 0 && col + 1 <= 7 && board.getPiece(row + dir, col + 1) != null && board.getPiece(row + dir, col + 1).getColor() != color) {
            moves.add(new Move(row + dir, col + 1, row, col));
        }
        if(row + dir <= 7 && row + dir >= 0 && col - 1 >= 0 && board.getPiece(row + dir, col - 1) != null && board.getPiece(row + dir, col - 1).getColor() != color) {
            moves.add(new Move(row + dir, col - 1, row, col));
        }
        for(int i = 0; i < moves.size(); i++){
            //pawn promotion
            if(moves.get(i).getRow() == 3.5 + dir * 3.5){
                moves.set(i, new SpecialMove(moves.get(i)));
            }
        }
        if(removeCheck){
            for(int i = moves.size() - 1; i >= 0; i--){
                if(board.wouldBeCheck(moves.get(i), color)){
                    moves.remove(i);
                }
            }
        }
        return moves;
    }
    
    /**
     * Gets all legal moves by this piece the board
     * @param board board that contains this piece
     * @return an ArrayList of legal moves
     */
    public ArrayList<Move> getMoves(Board board){
        return getMoves(board, true);
    }
    
    /**
     * Moves this piece to a new location
     * @param row row to move to
     * @param col column to move to
     * @return itself
     */
    public Piece move(int row, int col){
        this.row = row;
        this.col = col;
        return this;
    }
    
    /**
     * Creates a String representation of the object
     * return a string representing this object
     */
    public String toString(){
        return ((color)? "White" : "Black") + " Pawn";
    }
    
    /**
     * Creates a copy of this piece
     * @return the copy
     */
    public Piece clone(){
        return new Pawn(color, row, col);
    }
}
