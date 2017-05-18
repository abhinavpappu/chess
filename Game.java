import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
/**
 * Write a description of class Game here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Game
{
    public static void main(String[] args){
        Color boardWhite = new Color(176, 190, 197), boardBlack = new Color(84, 110, 122);
        JFrame frame = new JFrame("Chess");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLayout(new GridLayout(8, 8));
        Board board = new Board(true);
        ComputerPlayer cp = new ComputerPlayer(false);
        JPanel[][] squares = new JPanel[8][8];
        JLabel[][] labels = new JLabel[8][8];
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                squares[i][j] = new JPanel();
                squares[i][j].setBackground((i % 2 == j % 2)? boardWhite : boardBlack);
                squares[i][j].setLayout(new GridBagLayout());
                String icon = "";
                Color color = (i % 2 == j % 2)? boardWhite : boardBlack;
                if(board.getPieces()[i][j] != null){
                    icon = board.getPieces()[i][j].getIcon();
                    color = board.getPieces()[i][j].getColor()? Color.WHITE : Color.BLACK;
                }
                labels[i][j] = new JLabel(icon);
                labels[i][j].setFont(new Font("TimesRoman", Font.PLAIN, (int)(frame.getHeight() * 1.0/12)));
                labels[i][j].setForeground(color);
                squares[i][j].add(labels[i][j], new GridBagConstraints());
                frame.add(squares[i][j]);
            }
        }
        
        frame.addComponentListener(new ComponentAdapter(){
            public void componentResized(ComponentEvent e){
                for(JLabel[] arr : labels){
                    for(JLabel label : arr){
                        label.setFont(new Font("TimesRoman", Font.PLAIN, (int)(frame.getHeight() * 1.0/12)));
                    }
                }
            }
        });
        
        //an array since Java doesn't let you modify local variables in anonymous classes
        int[] index = {-1};
        frame.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                int row = (e.getY() - frame.getInsets().top) / squares[0][0].getHeight();
                int col = (e.getX() - frame.getInsets().left) / squares[0][0].getWidth();
                index[0] = row * 8 + col;
                if(board.getPiece(row, col) != null){
                    highlight(board.getPiece(row, col).getMoves(board), squares);
                }
            }
            
            public void mouseReleased(MouseEvent e){
                unhighlight(squares);
                int row = (e.getY() - frame.getInsets().top) / squares[0][0].getHeight();
                int col = (e.getX() - frame.getInsets().left) / squares[0][0].getWidth();
                int toIndex = row * 8 + col;
                if(index[0] > -1 && index[0] < 64){
                    if(board.movePiece(index[0], toIndex)){
                        updateBoard(labels, board);
                        new Thread(new Runnable(){
                            public void run(){
                                cp.play(board);
                                updateBoard(labels, board);
                            }
                        }).start();
                    }
                }
            }
        });
        
        frame.setVisible(true);
    }
    
    private static void updateBoard(JLabel[][] labels, Board board){
        for(int i = 0; i < labels.length; i++){
            for(int j = 0; j < labels[i].length; j++){
                labels[i][j].setText("");
                Piece piece = board.getPiece(i, j);
                if(piece != null){
                    Color color = piece.getColor()? Color.WHITE : Color.BLACK;
                    labels[i][j].setText(piece.getIcon());
                    labels[i][j].setForeground(color);
                }
            }
        }
    }
    
    private static void highlight(ArrayList<Move> moves, JPanel[][] squares){
        for(Move move : moves){
            squares[move.getRow()][move.getCol()].setBorder(BorderFactory.createLineBorder(Color.YELLOW));
        }
    }
    
    private static void unhighlight(JPanel[][] squares){
        for(JPanel[] arr : squares){
            for(JPanel panel : arr){
                panel.setBorder(null);
            }
        }
    }
}