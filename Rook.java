import java.util.ArrayList;
/**
 * Represents a Chess rook
 * 
 * @author Abhinav Pappu
 */
public class Rook implements Piece
{
    private boolean color, isInDanger, touched;
    private int row, col;
    
    /**
     * Constructor for a Rook
     * @param color color of piece (true - white, false - black)
     * @param row the row of this piece
     * @param col the column of this piece
     */
    public Rook(boolean color, int row, int col)
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
        return 5;
    }
    
    /**
     * Gets number of this piece (used for generating input for the neural network)
     * @return piece's number
     */
    public int getNum(){
        return color? 3 : 9;
    }
    
    /**
     * Gets unicode icon of this piece
     * @return piece's unicode icon
     */
    public String getIcon(){
        return "\u265C";
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
        boolean stop[] = {false, false, false, false};
        for(int i = 1; i <= 7; i++){
            for(double j = -1.49; j <= 1.48; j += .99){
                int cR = (int)Math.round(j) * i; //change in row
                int cC = ((cR == 0)? (int)Math.round(j * 2) : 0) * i; //change in column
                int stopInd = (int)Math.ceil(j) + 1;
                if(!stop[stopInd]){
                    if(row + cR > 7 || row + cR < 0 || col + cC > 7 || col + cC < 0){ //outside of board
                        stop[stopInd] = true;
                    }
                    else if(board.getPiece(row + cR, col + cC) == null){ //no piece at spot
                        moves.add(new Move(row + cR, col + cC, row, col));
                    }
                    else if(board.getPiece(row + cR, col + cC).getColor() == color){ //piece of same color at spot
                        stop[stopInd] = true;
                    }
                    else{ //enemy piece at spot
                        moves.add(new Move(row + cR, col + cC, row, col));
                        stop[stopInd] = true;
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
        return ((color)? "White" : "Black") + " Rook";
    }
    
    /**
     * Creates a copy of this piece
     * @return the copy
     */
    public Piece clone(){
        return new Rook(color, row, col);
    }
}