import java.util.ArrayList;
/**
 * Write a description of class ComputerPlayer here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ComputerPlayer
{
    private static ANN network = new ANN(new int[]{768, 256, 64, 1});
    private static boolean color = true;
    
    private static void trainNetwork(){
        if(DatabaseToPoints.getOutputs().length == 0){
            DatabaseToPoints.generate(color);
        }
        double[][] inputs = DatabaseToPoints.getInputs();
        double[][] outputs = DatabaseToPoints.getOutputs();
        long time = System.currentTimeMillis();
        int iterations = 1000000;
        double lr = .001;
        network.train(inputs, outputs, iterations, lr);
        System.out.println("Done training " + iterations + " iterations with a learning rate of " + lr + " in " + (System.currentTimeMillis() - time) / 1000.0 + " seconds");
        int randInd = (int)(Math.random() * inputs.length);
        double[] test = inputs[randInd];
        System.out.println("Test input: " + arrToString(test));
        System.out.println("Network prediction: " + arrToString(network.predict(test)));
        System.out.println("Actual Output: " + arrToString(outputs[randInd]));
    }
    
    public static void trainWhiteNetwork(){
        color = true;
        trainNetwork();
    }
    
    public static void trainBlackNetwork(){
        color = false;
        trainNetwork();
    }
    
    public static void play(Board board){
        ArrayList<Move> moves = board.getAllMoves(color);
        double max = score(moves.get(0), board);
        int maxInd = 0;
        for(int i = 1; i < moves.size(); i++){
            double output = score(moves.get(i), board);
            if(output > max){
                max = output;
                maxInd = i;
            }
        }
        moves.get(maxInd).execute(board);
    }
    
    private static double score(Move move, Board board){
        Board board2 = new Board(board);
        move.execute(board2);
        return network.predict(toInput(board2.getPieces()))[0];
    }
    
    private static String arrToString(double[] arr){
        String str = "[";
        for(double val : arr){
            str += val + ",";
        }
        str = str.substring(0, str.length() - 1) + "]";
        return str;
    }
    
    private static double[] toInput(Piece[][] pieces){
        double[] arr = new double[64 * 12];
        for(int i = 0; i < pieces.length; i++){
            for(int j = 0; j < pieces[i].length; j++){
                if(pieces[i][j] != null){
                    int index = pieces[i][j].getNum() + (i * 8 + j) * 12;
                    arr[index] = 1;
                }
            }
        }
        return arr;
    }
}
