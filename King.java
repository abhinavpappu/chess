import java.util.ArrayList;
/**
 * Represents a Chess king
 * 
 * @author Abhinav Pappu
 */
public class King implements Piece
{
    private boolean color, isInDanger, touched;
    private int row, col;
    
    /**
     * Constructor for a King
     * @param color color of piece (true - white, false - black)
     * @param row the row of this piece
     * @param col the column of this piece
     */
    public King(boolean color, int row, int col)
    {
        this.color = color;
        move(row, col);
        touched = false;
    }
    
    /**
     * Gets the value of this piece
     * @return piece's value
     */
    public int getValue(){
        return 10;
    }
    
    /**
     * Gets number of this piece (used for generating input for the neural network)
     * @return piece's number
     */
    public int getNum(){
        return color? 5 : 11;
    }
    
    /**
     * Gets unicode icon of this piece
     * @return piece's unicode icon
     */
    public String getIcon(){
        return "\u265A";
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
     * Gets whether the piece was ever moved (used for castling validity)
     * @return boolean indicating whether the piece has ever moved
     */
    public boolean touched(){
        return touched;
    }
    
    /**
     * Gets all possible moves of this piece
     * @param board board that contains this piece
     * @param removeCheck whether to remove the moves that cause a check on the piece's side
     * @return an ArrayList of possible moves
     */
    public ArrayList<Move> getMoves(Board board, boolean removeCheck){
        ArrayList<Move> moves = new ArrayList<Move>();
        for(int j = -1; j <= 1; j++){
            for(int k = -1; k <= 1; k++){
                if(j == 0 && k == 0)
                    continue;
                int cR = j; //change in row
                int cC = k; //change in column
                if(row + cR > 7 || row + cR < 0 || col + cC > 7 || col + cC < 0){ //outside of board
                }
                else if(board.getPiece(row + cR, col + cC) == null){ //no piece at spot
                    moves.add(new Move(row + cR, col + cC, row, col));
                }
                else if(board.getPiece(row + cR, col + cC).getColor() == color){ //piece of same color at spot
                }
                else{ //enemy piece at spot
                    moves.add(new Move(row + cR, col + cC, row, col));
                }
            }
        }
        if(SpecialMove.checkCastleValidity(board, board.getPlayerColor(), color, true)){
            moves.add(new SpecialMove(color, true, board.getPlayerColor()));
        }
        if(SpecialMove.checkCastleValidity(board, board.getPlayerColor(), color, false)){
            moves.add(new SpecialMove(color, false, board.getPlayerColor()));
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
        touched = true;
        this.row = row;
        this.col = col;
        return this;
    }
    
    /**
     * Creates a String representation of the object
     * return a string representing this object
     */
    public String toString(){
        return ((color)? "White" : "Black") + " King";
    }
    
    /**
     * Creates a copy of this piece
     * @return the copy
     */
    public Piece clone(){
        return new King(color, row, col);
    }
}
