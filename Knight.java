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
     * 
     * @param color color of piece (true - white, false - black)
     * 
     */
    public Knight(boolean color, int row, int col)
    {
        this.color = color;
        move(row, col);
    }
    
    public int getValue(){
        return 3;
    }
    
    public int getNum(){
        return color? 1 : 7;
    }
    
    public String getIcon(){
        return "\u265E";
    }
    
    public boolean getColor(){
        return color;
    }
    
    public int getRow() {
        return row;
    }
    
    public int getColumn() {
        return col;
    }
    
    public boolean isInDanger(){
        return isInDanger;
    }
    
    public void setInDanger(boolean inDanger){
        isInDanger = inDanger;
    }
    
    public ArrayList<Move> getMoves(Board board){
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
        return moves;
    }
    
    public Piece move(int row, int col){
        this.row = row;
        this.col = col;
        return this;
    }
    
    public String toString(){
        return ((color)? "White" : "Black") + " Knight";
    }
    
    public Piece clone(){
        return new Knight(color, row, col);
    }
}
