import java.util.ArrayList;
/**
 * Represents a Chess knight
 * 
 * @author Abhinav Pappu
 */
public class Knight implements Piece
{
    private boolean color, isInDanger;
    private int row, col;
    
    /**
     * Constructor for a Knight
     * @param color color of piece (true - white, false - black)
     * @param row the row of this piece
     * @param col the column of this piece
     */
    public Knight(boolean color, int row, int col)
    {
        this.color = color;
        move(row, col);
    }
    
    /**
     * Gets the value of this piece
     * @return piece's value
     */
    public int getValue(){
        return 3;
    }
    
    /**
     * Gets number of this piece (used for generating input for the neural network)
     * @return piece's number
     */
    public int getNum(){
        return color? 1 : 7;
    }
    
    /**
     * Gets unicode icon of this piece
     * @return piece's unicode icon
     */
    public String getIcon(){
        return "\u265E";
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
    public ArrayList<Move> getMoves(Board board, boolean removeCheck){
        ArrayList<Move> moves = new ArrayList<Move>();
        for(int i = -1; i <= 1 ; i+=2){ // sign for one direction ( + or - )
            for(int j = -1; j <= 1; j+=2){ //sign for other direction
                for(int k = 1; k <= 2; k++){ //movement in one direction
                    int l = 3 - k; //movement in other direction
                    if(row + i * k <= 7 && row + i * k >= 0 && col + j * l <= 7 && col + j * l >= 0 && (board.getPiece(row + i * k, col + j * l) == null || board.getPiece(row + i * k, col + j * l).getColor() != color)){
                        moves.add(new Move(row + i * k, col + j * l, row, col));
                    }
                }
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
        return ((color)? "White" : "Black") + " Knight";
    }
    
    /**
     * Creates a copy of this piece
     * @return the copy
     */
    public Piece clone(){
        return new Knight(color, row, col);
    }
}
