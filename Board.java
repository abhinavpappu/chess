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
     * Constructor for a board
     * @param playerColor color of player (true - white, false - black)
     */
    public Board(boolean playerColor){
        pieces = new Piece[8][8];
        this.playerColor = playerColor;
        setUp();
    }
    
    /**
     * Constructor for a board from another board
     * @param board a Board object to copy from
     */
    public Board(Board board){
        copyFrom(board);
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
    
    /**
     * Resets the board based on player's color
     * @param playerColor color of pieces on the bottom of the board
     */
    public void reset(boolean playerColor){
        for(int i = 0; i < pieces.length; i++){
            for(int j = 0; j < pieces[i].length; j++){
                pieces[i][j] = null;
            }
        }
        this.playerColor = playerColor;
        setUp();
    }
    
    /**
     * Resets the board to initial state with the same player color
     */
    public void reset(){
        reset(playerColor);
    }

    /**
     * Getter for the 2D array of pieces 
     * @return 2d array of pieces
     */
    public Piece[][] getPieces(){
        return pieces;
    }

    /**
     * Gets player's color 
     * @return player color (true - white, false - black)
     */
    public boolean getPlayerColor(){
        return playerColor;
    }
    
    /**
     * Gets player's score
     * @return score of player
     */
    public int getPlayerScore(){
        return playerScore;
    }
    
    /**
     * Gets computer's score 
     * @return score of computer
     */
    public int getComputerScore(){
        return computerScore;
    }

    /**
     * Gets a piece at a specific location on the board 
     * @param row of desired piece
     * @param column of desired piece
     * @return piece at requested position
     */
    public Piece getPiece(int row, int col){
        return pieces[row][col];
    }
    
    /**
     * Moves a piece to desired location 
     * @param piece piece to move
     * @param row row to move piece to
     * @param col column to move piece to
     * @return boolean indicating whether the move succeeded
     */
    public boolean movePiece(Piece piece, int row, int col){
        Move requestedMove = new Move(row, col, piece.getRow(), piece.getColumn());
        ArrayList<Move> moves = piece.getMoves(this);
        int index = moves.indexOf(requestedMove);
        if(index > -1){
            pieces[piece.getRow()][piece.getColumn()] = piece;
            int result = moves.get(index).execute(this);
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
    
    /**
     * Moves a piece to desired location
     * @param fromIndex index of piece to move
     * @param toIndex index of square to move to
     * @param color the color that the piece should be
     * @return boolean indicating whether the move succeeded
     */
    public boolean movePiece(int fromIndex, int toIndex, boolean color){
        Piece piece = getPiece(fromIndex / 8, fromIndex % 8);
        if(piece != null){
            if(piece.getColor() == color){
                return movePiece(piece, toIndex / 8, toIndex % 8);
            }
            return false;
        }
        return false;
    }
    
    /**
     * Moves a piece to desired location
     * @param move Move object representing desired move
     * @return boolean indicating whether the move succeeded
     */
    public boolean movePiece(Move move){
        Piece piece = getPiece(move.getFromRow(), move.getFromCol());
        if(piece != null){
            int result = move.execute(this);
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
    
    /**
     * Updates which pieces are in danger of being attacked by another piece
     */
    public void updateInDangers(){
        for(Piece[] row : pieces){
            for(Piece piece : row){
                if(piece != null){
                    piece.setInDanger(false);
                }
            }
        }
        ArrayList<Move> moves = getAllMoves(true, false);
        moves.addAll(getAllMoves(false, false));
        for(Move move : moves){
            if(pieces[move.getRow()][move.getCol()] != null){
                if(pieces[move.getRow()][move.getCol()].getColor() != pieces[move.getFromRow()][move.getFromCol()].getColor()){
                    pieces[move.getRow()][move.getCol()].setInDanger(true);
                }
            }
        }
    }
    
    /**
     * Checks if the king is in check 
     * @param color the color of the side to check
     * @return boolean indicating whether the king is in check
     */
    public boolean isCheck(boolean color){
        for(Piece[] row : pieces){
            for(Piece piece : row){
                if(piece != null && piece.getNum() == (color? 5 : 11)){
                    return piece.isInDanger();
                }
            }
        }
        return false;
    }
    
    /**
     * Gets all possible moves by all pieces of a color on the board 
     * @param color color of side to get moves from
     * @param removeCheck whether to remove the moves that cause a check on the color's side
     * @return an ArrayList of possible moves
     */
    public ArrayList<Move> getAllMoves(boolean color, boolean removeCheck){
        ArrayList<Move> moves = new ArrayList<Move>();
        for(Piece[] row : pieces){
            for(Piece piece : row){
                if(piece != null && piece.getColor() == color){
                    moves.addAll(piece.getMoves(this, removeCheck));
                }
            }
        }
        return moves;
    }
    
    /**
     * Gets all legal moves by all pieces of a color on the board
     * @param color color of side to get moves from
     * @return an ArrayList of legal moves
     */
    public ArrayList<Move> getAllMoves(boolean color){
        return getAllMoves(color, true);
    }
    
    /**
     * Checks whether a side would be in check after executing a move
     * @param move Move to execute
     * @param color color of side to check
     * @return whether the side would be in check
     */
    public boolean wouldBeCheck(Move move, boolean color){
        Board board = new Board(this);
        move.execute(board);
        return board.isCheck(color);
    }
    
    /**
     * Safely copies everything from another Board without any of the same objects 
     * @param board Board to copy from
     * @return itself
     */
    public Board copyFrom(Board board){
        pieces = new Piece[8][8];
        for(int i = 0; i < pieces.length; i++){
            for(int j = 0; j < pieces[i].length; j++){
                pieces[i][j] = (board.getPiece(i, j) == null)? null : board.getPiece(i, j).clone();
            }
        }
        playerColor = board.getPlayerColor();
        playerScore = board.getPlayerScore();
        computerScore = board.getComputerScore();
        return this;
    }
    
    /**
     * Creates a String representation of the object
     * return a string representing this object
     */
    public String toString(){
        String[] pieceLetters = {"WPAWN", "WKNIG", "WBISH", "WROOK", "WQUEE", "WKING", "BPAWN", "BKNIG", "BBISH", "BROOK", "BQUEE", "BKING"};
        String[] letters = {"a", "b", "c", "d", "e", "f", "g", "h"};
        String str = "";
        for(int i = 0; i < pieces.length; i++){
            str += 8 - i + "  ";
            for(Piece piece : pieces[i]){
                str += ((piece != null)? pieceLetters[piece.getNum()] : "_____")  + "  ";
            }
            str += "\n";
        }
        str += " ";
        for(int i = 0; i < letters.length; i++){
            str += "    " + letters[i] + "  ";
        }
        str += "\n";
        return str;
    }
}
