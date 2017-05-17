import org.jblas.*;
/**
 * Write a description of class ANN_Tester here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ANN_Tester
{
    public static void main(String[] args){
        double[][] inputs = new double[150][1];
        double[][] outputs = new double[150][1];
        for(int i = 0; i < inputs.length; i++){
            inputs[i][0] = i * (2 * Math.PI / inputs.length);
            outputs[i][0] = Math.sin(inputs[i][0]);
        }
        System.out.println("Training Data:");
        System.out.println(arrToString(inputs));
        System.out.println(arrToString(outputs));
        DoubleMatrix test = new DoubleMatrix(1, 1, Math.PI / 3);
        int[] structure = {1, 100, 50, 1};
        ANN network = new ANN(structure);
        System.out.println("sin(π/3) = " + Math.sin(test.get(0, 0)));
        System.out.println("Before Training: sin(π/3) ≈ " + network.predict(test));
        long time = System.currentTimeMillis();
        network.train(inputs, outputs, 10000, .01);
        System.out.println("Training complete in " + (System.currentTimeMillis() - time) / 1000.0 + " seconds");
        System.out.println("After Training: sin(π/3) ≈ " + network.predict(test));
    }
    
    public static void testMove(){
        String moveStrings = "1.e4 e6 2.d4 d5 3.Nd2 c5 4.exd5 Qxd5 5.Ngf3 cxd4 6.Bc4 Qd6 7.Qe2 Nf6 8.Nb3 Nc6 9.Bg5 Qb4+ 10.Bd2 Qb6 11.O-O-O Bd7 12.Bg5 Bc5 13.Kb1 O-O-O 14.Ne5 Nxe5 15.Qxe5 Bd6";
        moveStrings = moveStrings.replaceAll("[0-9]+\\.", "");
        String[] moves = moveStrings.split(" ");
        boolean color = true;
        Board board = new Board(true);
        for(String moveString : moves){
            Move move = Move.fromString(moveString, color, board);
            System.out.println(move);
            color = !color;
            move.execute(board);
        }
    }
    
    public static void testCastle(){
        Board board = new Board(true);
        board.getPieces()[7][6] = null;
        board.getPieces()[7][5] = null;
        Move move = new SpecialMove(true, true, true);
        move.execute(board);
        System.out.println(board);
    }
    
    public static void testLoadWeights(){
        ComputerPlayer cp1 = new ComputerPlayer(false);
        ComputerPlayer cp2 = new ComputerPlayer(false);
        System.out.println(cp1.compareWeights(cp2));
        Board board = new Board(true);
        Move move = board.getAllMoves(false).get(0);
        System.out.println(cp1.score(move, board));
        System.out.println(cp2.score(move, board));
    }
    
    private static String arrToString(double[] arr){
        String str = "[";
        for(double val : arr){
            str += val + ", ";
        }
        str = str.substring(0, str.length() - 2) + "]";
        return str;
    }
    
    private static String arrToString(double[][] arr){
        String str = "[";
        for(double[] val : arr){
            str += val[0] + ", ";
        }
        str = str.substring(0, str.length() - 2) + "]";
        return str;
    }
}
