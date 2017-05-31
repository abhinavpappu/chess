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
        JFrame frame = new JFrame("Chess");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        askColor(frame);
    }
    
    private static void askColor(JFrame frame){
        frame.setLayout(new GridLayout(2, 1));
        JButton whiteButton = new JButton("White");
        whiteButton.setBackground(Color.WHITE);
        whiteButton.setFont(new Font("Helvetica", Font.BOLD, 75));
        whiteButton.setFocusPainted(false);
        whiteButton.setOpaque(true);
        whiteButton.setBorderPainted(false);
        JButton blackButton = new JButton("Black");
        blackButton.setBackground(Color.BLACK);
        blackButton.setForeground(Color.WHITE);
        blackButton.setFont(new Font("Helvetica", Font.BOLD, 75));
        blackButton.setFocusPainted(false);
        blackButton.setOpaque(true);
        blackButton.setBorderPainted(false);
        frame.add(whiteButton);
        frame.add(blackButton);
        frame.setVisible(true);
        
        whiteButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                removeAll(frame);
                playAgainst(frame, true);
            }
        });
        
        blackButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                removeAll(frame);
                playAgainst(frame, false);
            }
        });
    }
    
    private static void playAgainst(JFrame frame, boolean playerColor){
        frame.setLayout(new GridLayout(2, 1));
        JButton b1 = new JButton("<html><p style='text-align:center'>Classic Algorithm<br>(Recommended for now)</p></html>");
        b1.setBackground(Color.WHITE);
        b1.setFont(new Font("Helvetica", Font.BOLD, 40));
        b1.setFocusPainted(false);
        b1.setOpaque(true);
        b1.setBorderPainted(false);
        JButton b2 = new JButton("<html><p style='text-align:center'>Neural Network<br>(Still needs to be trained a lot more)</p></html>");
        b2.setBackground(Color.BLACK);
        b2.setForeground(Color.WHITE);
        b2.setFont(new Font("Helvetica", Font.BOLD, 40));
        b2.setFocusPainted(false);
        b2.setOpaque(true);
        b2.setBorderPainted(false);
        frame.add(b1);
        frame.add(b2);
        frame.revalidate();
        
        b1.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                removeAll(frame);
                setUpBoard(frame, playerColor, false);
            }
        });
        
        b2.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                removeAll(frame);
                setUpBoard(frame, playerColor, true);
            }
        });
    }
    
    private static void removeAll(JFrame frame){
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
    }
    
    private static void setUpBoard(JFrame frame, boolean playerColor, boolean againstNetwork){
        Color boardWhite = new Color(176, 190, 197), boardBlack = new Color(84, 110, 122);
        frame.setLayout(new GridLayout(8, 8));
        Board board = new Board(playerColor);
        ComputerPlayer cp = new ComputerPlayer(!playerColor, againstNetwork);
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
        
        //arrays since Java doesn't let you modify local variables in anonymous classes
        int[] index = {-1};
        boolean[] isPlayerTurn = {false};
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
                if(index[0] > -1 && index[0] < 64 && isPlayerTurn[0]){
                    if(board.movePiece(index[0], toIndex, playerColor)){
                        isPlayerTurn[0] = false;
                        updateBoard(labels, board);
                        if(board.getAllMoves(!playerColor).size() > 0){
                            new Thread(new Runnable(){
                                public void run(){
                                    cp.play(board);
                                    updateBoard(labels, board);
                                    if(board.getAllMoves(playerColor).size() == 0){
                                        gameOver(frame, !playerColor);
                                    }
                                    else{
                                        isPlayerTurn[0] = true;
                                    }
                                }
                            }).start();
                        }
                        else{
                            gameOver(frame, playerColor);
                        }
                    }
                }
            }
        });
        
        if(!playerColor){
            cp.play(board);
        }
        updateBoard(labels, board);
        isPlayerTurn[0] = true;
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
    
    private static void gameOver(JFrame frame, boolean winnerColor){
        JPanel overlay = new JPanel();
        //overlay.setOpaque(false);
        overlay.setLayout(new GridBagLayout());
        String winner = winnerColor? "White" : "Black";
        JLabel text = new JLabel("<html>Game Over<br>" + winner + " Wins</html>");
        text.setFont(new Font("Helvetic", Font.BOLD, 50));
        overlay.add(text, new GridBagConstraints());
        text.setForeground(Color.WHITE);
        overlay.setBackground(new Color(0,0,0,0));
        frame.setGlassPane(overlay);
        overlay.setVisible(true);
        overlay.setBackground(new Color(0,0,0,175));
        frame.repaint();
    }
}