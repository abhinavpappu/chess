import java.util.ArrayList;
/**
 * Write a description of interface Piece here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public interface Piece
{
    int getValue();
    int getNum();
    String getIcon();
    boolean getColor();
    int getRow();
    int getColumn();
    ArrayList<Move> getMoves(Board board);
    Piece move(int row, int col);
}
