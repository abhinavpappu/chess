import java.util.ArrayList;
/**
 * Represents a Chess bishop
 * 
 * @author Abhinav Pappu
 */
public class Bishop implements Piece
{
    private boolean color, isInDanger;
    private int row, col;
    private double weight;
    /**
     * 
     * @param color color of piece (true - white, false - black)
     * 
     */
    public Bishop(boolean color, int row, int col)
    {
        this.color = color;
        weight = 3.0;
        move(row, col);
    }
    
    public int getValue(){
        return 3;
    }
    
    public int getNum(){
        return color? 2 : 8;
    }
    
    public String getIcon(){
        return "\u265D";
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
        boolean[] stop = {false, false, false, false};
        for(int i = 1; i <= 7; i++){
            for(int j = -1; j <= 1; j+=2){
                for(int k = -1; k <= 1; k+=2){
                    int cR = j * i; //change in row
                    int cC = k * i; //change in column
                    int stopInd = (int)(j + k * .5 + 1.5);
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
        }
        return moves;
    }
    
    public Piece move(int row, int col){
        this.row = row;
        this.col = col;
        return this;
    }
    
    public String toString(){
        return ((color)? "White" : "Black") + " Bishop";
    }
    
    public Piece clone(){
        return new Bishop(color, row, col);
    }
    
        public double getWeight()
    {
        return weight;
    }
}
