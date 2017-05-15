import java.util.ArrayList;
/**
 * Write a description of class ComputerPlayer here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ComputerPlayer
{
    private static boolean color = true;
    private static Network network;
    private static void trainNetwork(){
        
        if(DatabaseToPoints.getOutputs().length == 0){
            DatabaseToPoints.generate(color);
        }
        double[][] inputs = DatabaseToPoints.getInputs();
        double[][] outputs = DatabaseToPoints.getOutputs();
        int[] size = new int[]{inputs[0].length, 3, outputs[0].length};
        network = new Network(size,inputs[0].length);
        long time = System.currentTimeMillis();
        int iterations = 10;
        for(int i = 0;i<iterations;i++)
        {
            for(int k = 0;k<inputs.length;k++)
            {
                network.train(inputs[k], outputs[k]);
            }
            System.out.println(i + "times trained");
        }
    }
    
    public static void trainWhiteNetwork(){
        color = true;
        trainNetwork();
    }
    
    public static void trainBlackNetwork(){
        color = false;
        trainNetwork();
    }
    
    private static String arrToString(double[] arr){
        String str = "[";
        for(double val : arr){
            str += val + ",";
        }
        str = str.substring(0, str.length() - 1) + "]";
        return str;
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
        double[] temp =  network.feed(toInput(board2.getPieces()));
        double max = temp[0];
        for(double d : temp)
            if(d>max)
                max = d;
        return max;
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
