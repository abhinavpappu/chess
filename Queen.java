import java.util.ArrayList;
/**
 * Represents a Chess queen
 * 
 * @author Abhinav Pappu
 */
public class Queen implements Piece
{
    private boolean color, isInDanger;
    private int row, col;
    
    /**
     * 
     * @param color color of piece (true - white, false - black)
     * 
     */
    public Queen(boolean color, int row, int col)
    {
        this.color = color;
        move(row, col);
    }
    
    public int getValue(){
        return 9;
    }
    
    public int getNum(){
        return color? 4 : 10;
    }
    
    public String getIcon(){
        return "\u265B";
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
    
    public ArrayList<Move> getMoves(Board board, boolean removeCheck){
        ArrayList<Move> moves = new ArrayList<Move>();
        boolean[] stop = {false, false, false, false, false, false, false, false};
        for(int i = 1; i <= 7; i++){
            for(int j = -1; j <= 1; j++){
                for(int k = -1; k <= 1; k++){
                    if(j == 0 && k == 0)
                        continue;
                    int cR = j * i; //change in row
                    int cC = k * i; //change in column
                    int stopInd = j * 3 + k + ((j * 3 + k > 0)? 3 : 4);
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
        if(removeCheck){
            for(int i = moves.size() - 1; i >= 0; i--){
                if(board.wouldBeCheck(moves.get(i), color)){
                    moves.remove(i);
                }
            }
        }
        return moves;
    }
    
    public ArrayList<Move> getMoves(Board board){
        return getMoves(board, true);
    }
    
    public Piece move(int row, int col){
        this.row = row;
        this.col = col;
        return this;
    }
    
    public String toString(){
        return ((color)? "White" : "Black") + " Queen";
    }
    
    public Piece clone(){
        return new Queen(color, row, col);
    }
}
