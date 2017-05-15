
import java.util.ArrayList;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/**
 * Class Game - write a description of the class here
 * 
 * @author (your name) 
 * @version (a version number)
 */
public class Game extends JApplet implements MouseListener, MouseMotionListener
{
    private int squareWidth, squareHeight;
    
    private Color boardWhite, boardBlack, pieceWhite, pieceBlack;
    private Font pieceFont;

    // -1 - mouse released, 0 - nothing, 1 - mouse pressed, 2 - dragging
    private int dragState;
    private int mouseX, mouseY;
    
    private Board board;
    
    //Used to temporarily hold and clear highlighted moves
    private ArrayList<Move> moves;
    private Piece selectedPiece;

    /**
     * Called by the browser or applet viewer to inform this JApplet that it
     * has been loaded into the system. It is always called before the first 
     * time that the start method is called.
     */
    public void init()
    {
        ComputerPlayer.trainBlackNetwork();
        squareWidth = getWidth() / 8;
        squareHeight = getHeight() / 8;
        
        boardWhite = new Color(176, 190, 197);
        boardBlack = new Color(84, 110, 122);
        pieceWhite = Color.WHITE;
        pieceBlack = Color.BLACK;
        pieceFont = new Font("SansSerif", Font.PLAIN, 2 * squareHeight / 3);

        dragState = 0;
        mouseX = 0;
        mouseY = 0;
        
        board = new Board(true); //give user choice of being white or black later
        
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    /**
     * Called by the browser or applet viewer to inform this JApplet that it 
     * should start its execution. It is called after the init method and 
     * each time the JApplet is revisited in a Web page. 
     */
    public void start()
    {
        // provide any code requred to run each time 
        // web page is visited
    }

    /** 
     * Called by the browser or applet viewer to inform this JApplet that
     * it should stop its execution. It is called when the Web page that
     * contains this JApplet has been replaced by another page, and also
     * just before the JApplet is to be destroyed. 
     */
    public void stop()
    {
        // provide any code that needs to be run when page
        // is replaced by another page or before JApplet is destroyed 
    }

    public void mousePressed(MouseEvent e) {
        int pieceRow = e.getY() / squareHeight;
        int pieceCol = e.getX() / squareWidth;
        selectedPiece = board.getPiece(pieceRow, pieceCol);
        board.getPieces()[selectedPiece.getRow()][selectedPiece.getColumn()] = null;
        if(selectedPiece != null && selectedPiece.getColor() == board.getPlayerColor()){
            dragState = 1;
            repaint();
        }
    }

    public void mouseReleased(MouseEvent e) {
        int mouseRow = e.getY() / squareHeight;
        int mouseCol = e.getX() / squareWidth;
        if(!board.movePiece(selectedPiece, mouseRow, mouseCol)){
            //if the move fails (e.g. attempted to move piece of opposite color), then piece is put back in correct spot
            board.getPieces()[selectedPiece.getRow()][selectedPiece.getColumn()] = selectedPiece;
        }
        else{
            ComputerPlayer.play(board);
        }
        dragState = -1;
        repaint();
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }
    
    public void mouseDragged(MouseEvent e) {
        if(dragState >= 1){
            mouseX = e.getX();
            mouseY = e.getY();
            dragState = 2;
            repaint();
        }
    }
    
    public void mouseMoved(MouseEvent e) {
    }

    /**
     * Paint method for applet.
     * 
     * @param  g   the Graphics object for this applet
     */
    public void paint(Graphics g)
    {
        //JOptionPane.showMessageDialog(null, "Message");
        switch(dragState){
            case 1:
                highlightMoves(g);
                break;
            case 2:
                drawDragPiece(g);
                break;
            case -1:
                //repaintSurrounding(g);
                //repaintSurrounding(g, mouseY / squareHeight, mouseX / squareWidth);
                drawBoard(g);
                dragState = 0;
                break;
            default:
                squareWidth = getWidth() / 8;
                squareHeight = getHeight() / 8;
                drawBoard(g);
        }
    }

    private void drawBoard(Graphics g){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                drawSquare(g, i, j);
            }
        }
    }
    
    private void drawSquare(Graphics g, int row, int col){
        //Draws square
        Color color = (row % 2 == col % 2)? boardWhite : boardBlack;
        g.setColor(color);
        g.fillRect(col * squareWidth, row * squareHeight, squareWidth, squareHeight);
        
        //Draws piece if it exists
        Piece[][] pieces = board.getPieces();
        if(pieces[row][col] != null){
            g.setFont(pieceFont);
            color = (pieces[row][col].getColor())? pieceWhite : pieceBlack;
            g.setColor(color);
            int x = col * squareWidth + squareWidth / 2 - pieceFont.getSize() / 2;
            int y = row * squareHeight + squareHeight / 2 + pieceFont.getSize() / 2;
            g.drawString(pieces[row][col].getIcon(), x, y);
        }
    }
    
    private void highlightSquare(Graphics g, int row, int col){
        g.setColor(Color.YELLOW);
        g.fillRect(col * squareWidth - 1, row * squareHeight - 1, squareWidth + 2, squareHeight + 2);
        drawSquare(g, row, col);
    }
    
    private void highlightMoves(Graphics g){
        moves = selectedPiece.getMoves(board);
        for(Move move : moves){
            highlightSquare(g, move.getRow(), move.getCol());
        }
    }

    private void repaintSurrounding(Graphics g){
        ArrayList<String> squares = new ArrayList<String>();
        for(Move move : moves){
            for(int i = -1; i <= 1; i++){
                for(int j = -1; j <= 1; j++){
                    int row = move.getRow() + i;
                    int col = move.getCol() + j;
                    String square = row + "," + col;
                    if(row <= 7 && row >= 0 && col <= 7 && col >= 0 && !squares.contains(square)){
                        squares.add(square);
                    }
                }
            }
        }
        for(String square : squares){
            String[] position = square.split(",");
            drawSquare(g, Integer.valueOf(position[0]), Integer.valueOf(position[1]));
        }
    }
    
    private void repaintSurrounding(Graphics g, int row, int col){
        for(int i = -1; i <= 1; i++){
            for(int j = -1; j <= 1; j++){
                if(row + i <= 7 && row + i >= 0 && col + j <= 7 && col + j >= 0){
                    drawSquare(g, row + i, col + j);
                }
            }
        }
    }
    
    private void drawDragPiece(Graphics g){
        int mouseRow = mouseY / squareHeight;
        int mouseCol = mouseX / squareWidth;
        repaintSurrounding(g, mouseRow, mouseCol);
        
        highlightMoves(g);
        
        g.setFont(pieceFont);
        Color color = (selectedPiece.getColor())? pieceWhite : pieceBlack;
        g.setColor(color);
        int pieceX = mouseX - pieceFont.getSize() / 2;
        int pieceY = mouseY + pieceFont.getSize() / 2;
        g.drawString(selectedPiece.getIcon(), pieceX, pieceY);
    }

    /**
     * Called by the browser or applet viewer to inform this JApplet that it
     * is being reclaimed and that it should destroy any resources that it
     * has allocated. The stop method will always be called before destroy. 
     */
    public void destroy()
    {
        // provide code to be run when JApplet is about to be destroyed.
    }

    /**
     * Returns information about this applet. 
     * An applet should override this method to return a String containing 
     * information about the author, version, and copyright of the JApplet.
     *
     * @return a String representation of information about this JApplet
     */
    public String getAppletInfo()
    {
        // provide information about the applet
        return "Title:   \nAuthor:   \nA simple applet example description. ";
    }

    /**
     * Returns parameter information about this JApplet. 
     * Returns information about the parameters than are understood by this JApplet.
     * An applet should override this method to return an array of Strings 
     * describing these parameters. 
     * Each element of the array should be a set of three Strings containing 
     * the name, the type, and a description.
     *
     * @return a String[] representation of parameter information about this JApplet
     */
    public String[][] getParameterInfo()
    {
        // provide parameter information about the applet
        String paramInfo[][] = {
                {"firstParameter",    "1-10",    "description of first parameter"},
                {"status", "boolean", "description of second parameter"},
                {"images",   "url",     "description of third parameter"}
            };
        return paramInfo;
    }
}
