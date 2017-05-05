import java.util.ArrayList;
/**
 * Represents a Chess rook
 * 
 * @author Abhinav Pappu
 */
public class Rook implements Piece
{
    private boolean color;
    private int row, col;
    
    /**
     * 
     * @param color color of piece (true - white, false - black)
     * 
     */
    public Rook(boolean color, int row, int col)
    {
        this.color = color;
        move(row, col);
    }
    
    public int getValue(){
        return 5;
    }
    
    public int getNum(){
        return color? 3 : 9;
    }
    
    public String getIcon(){
        return "\u265C";
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
    
    public ArrayList<Move> getMoves(Board board){
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
        return moves;
    }
    
    public Piece move(int row, int col){
        this.row = row;
        this.col = col;
        return this;
    }
    
    public String toString(){
        return ((color)? "White" : "Black") + " Rook";
    }
}