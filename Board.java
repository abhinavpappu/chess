import java.util.ArrayList;
/**
 * Write a description of class Board here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Board
{
    /*
    Board:
    00  01  02  03  04  05  06  07
    10  11  12  13  14  15  16  17
    20  21  22  23  24  25  26  27
    30  31  32  33  34  35  36  37
    40  41  42  43  44  45  46  47
    50  51  52  53  54  55  56  57
    60  61  62  63  64  65  66  67
    70  71  72  73  74  75  76  77
     */
    private Piece[][] pieces;
    private boolean playerColor;
    private int playerScore;
    private int computerScore;

    /**
     * @param playerColor color of player (true - white, false - black)
     */
    public Board(boolean playerColor){
        pieces = new Piece[8][8];
        this.playerColor = playerColor;
        setUp();
    }

    private void setUp(){
        for(int i = 0; i < 8; i++){
            pieces[1][i] = new Pawn(!playerColor, 1, i);
            pieces[6][i] = new Pawn(playerColor, 6, i);
        }

        pieces[7][1] = new Knight(playerColor, 7, 1);
        pieces[7][6] = new Knight(playerColor, 7, 6);
        pieces[0][1] = new Knight(!playerColor, 0, 1);
        pieces[0][6] = new Knight(!playerColor, 0, 6);

        pieces[7][2] = new Bishop(playerColor, 7, 2);
        pieces[7][5] = new Bishop(playerColor, 7, 5);
        pieces[0][2] = new Bishop(!playerColor, 0, 2);
        pieces[0][5] = new Bishop(!playerColor, 0, 5);

        pieces[7][0] = new Rook(playerColor, 7, 0);
        pieces[7][7] = new Rook(playerColor, 7, 7);
        pieces[0][0] = new Rook(!playerColor, 0, 0);
        pieces[0][7] = new Rook(!playerColor, 0, 7);

        int shift = (playerColor)? 1 : 0;

        pieces[7][4 - shift] = new Queen(playerColor, 7, 4 - shift);
        pieces[0][4 - shift] = new Queen(!playerColor, 0, 4 - shift);

        pieces[7][3 + shift] = new King(playerColor, 7, 3 + shift);
        pieces[0][3 + shift] = new King(!playerColor, 0, 3 + shift);
    }
    
    public void reset(boolean playerColor){
        for(int i = 0; i < pieces.length; i++){
            for(int j = 0; j < pieces[i].length; j++){
                pieces[i][j] = null;
            }
        }
        this.playerColor = playerColor;
        setUp();
    }
    
    public void reset(){
        reset(playerColor);
    }

    /**
     * @return 2d array of pieces
     */
    public Piece[][] getPieces(){
        return pieces;
    }

    /**
     * @return player color (true - white, false - black)
     */
    public boolean getPlayerColor(){
        return playerColor;
    }

    /**
     * Gets a piece at a specific location on the board
     * 
     * @param row of desired piece
     * @param column of desired piece
     * @return piece at requested position
     */
    public Piece getPiece(int row, int col){
        return pieces[row][col];
    }

    /**
     * Moves a piece to desired location
     * 
     * @param piece piece to move
     * @param row row to move piece to
     * @param col column to move piece to
     * @return boolean indicating whether the move succeeded
     */
    public boolean movePiece(Piece piece, int row, int col){
        Move requestedMove = new Move(row, col, piece.getRow(), piece.getColumn());
        if(piece.getMoves(this).contains(requestedMove)){
            pieces[piece.getRow()][piece.getColumn()] = piece;
            int result = requestedMove.execute(pieces);
            if(result >= 0){
                if(piece.getColor() == getPlayerColor()){
                    playerScore += result;
                }
                else{
                    computerScore += result;
                }
                return true;
            }
            return false;
        }
        return false;
    }

    public ArrayList<Move> getAllMoves(boolean color){
        ArrayList<Move> moves = new ArrayList<Move>();
        for(Piece[] row : pieces){
            for(Piece piece : row){
                if(piece != null && piece.getColor() == color){
                    moves.addAll(piece.getMoves(this));
                }
            }
        }
        return moves;
    }

    public String toString(){
        String str = "";
        for(Piece[] row : pieces){
            for(Piece piece : row){
                str += piece + ", ";
            }
            str += "\n";
        }
        return str;
    }
}
