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
    private double weight;
    /**
     * 
     * @param color color of piece (true - white, false - black)
     * 
     */
    public Pawn(boolean color, int row, int col) {
        this.color = color;
        weight = 1.0;
        move(row, col);
    }
    
    public int getValue() {
        return 1;
    }
    
    public int getNum(){
        return color? 0 : 6;
    }
    
    public String getIcon(){
        return "\u265F";
    }
    
    public boolean getColor() {
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
    
    public ArrayList<Move> getMoves(Board board) {
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
        return moves;
    }
    
    public Piece move(int row, int col){
        this.row = row;
        this.col = col;
        return this;
    }
    
    public String toString(){
        return ((color)? "White" : "Black") + " Pawn";
    }
    
    public Piece clone(){
        return new Pawn(color, row, col);
    }
    
    public double getWeight()
    {
        return weight;
    }
}
