import java.util.ArrayList;
import java.io.PrintWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import org.jblas.*;
/**
 * Write a description of class ComputerPlayer here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ComputerPlayer
{
    private static final int[] structure = {768, 1000, 1000, 1};
    private boolean color;
    private ANN network;

    private static void trainNetwork(boolean color){
        if(DatabaseToPoints.getOutputs().length == 0){
            DatabaseToPoints.generate(color);
        }
        double[][] inputs = DatabaseToPoints.getInputs();
        double[][] outputs = DatabaseToPoints.getOutputs();
        ANN trainingNetwork = new ANN(structure);
        loadWeights(color, trainingNetwork);
        long time = System.currentTimeMillis();
        int randInd = (int)(Math.random() * inputs.length);
        double[] test = inputs[randInd];
        while(test.length == 0){
            randInd = (int)(Math.random() * inputs.length);
            test = inputs[randInd];
        }
        System.out.println("Test input: " + arrToString(test));
        System.out.println("Network prediction before training: " + arrToString(trainingNetwork.predict(test)));
        int iterations = 1000000;
        double lr = .0001;
        trainingNetwork.train(inputs, outputs, iterations, lr);
        System.out.println("Done training " + iterations + " iterations with a learning rate of " + lr + " in " + (System.currentTimeMillis() - time) / 1000.0 + " seconds");
        System.out.println("Network prediction after training: " + arrToString(trainingNetwork.predict(test)));
        System.out.println("Actual Output: " + arrToString(outputs[randInd]));
        time = System.currentTimeMillis();
        saveWeights(color, trainingNetwork);
        System.out.println("Saved weights in " + (System.currentTimeMillis() - time) / 1000.0 + " seconds");
    }

    public static void trainWhiteNetwork(){
        trainNetwork(true);
    }

    public static void trainBlackNetwork(){
        trainNetwork(false);
    }
    
    public ComputerPlayer(boolean color){
        this.color = color;
        network = new ANN(structure);
        loadWeights(color, network);
    }
    
    public String compareWeights(ComputerPlayer cp){ //temp
        double[][][] weights = network.getWeights();
        double[][][] weights2 = cp.network.getWeights();
        double[][] biases = network.getBias();
        double[][] biases2 = cp.network.getBias();
        for(int i = 0; i < weights.length; i++){
            for(int j = 0; j < weights[i].length; j++){
                if(biases[i][j] != biases2[i][j]){
                    return "Bias at " + i + ", " + j + "\t\t" + biases[i][j] + " != " + biases2[i][j];
                }
                for(int k = 0; k < weights[i][j].length; k++){
                    if(weights[i][j][k] != weights2[i][j][k]){
                        return "Weight at " + i + ", " + j + ", " + k;
                    }
                }
            }
        }
        return "Same";
    }

    public void play(Board board){
        ArrayList<Move> moves = board.getAllMoves(color);
        Board board2 = new Board(board);
        double max = score(moves.get(0), board2);
        int maxInd = 0;
        for(int i = 1; i < moves.size(); i++){
            double output = score(moves.get(i), board2.copyFrom(board));
            if(output > max){
                max = output;
                maxInd = i;
            }
        }
        board.movePiece(moves.get(maxInd));
    }

    private double score(Move move, Board board){
        board.movePiece(move);
        return network.predict(toInput(board.getPieces()))[0];
    }
    
    public void play2(Board board){
        ArrayList<Move> moves = board.getAllMoves(color);
        int num = 3; //number of moves to look ahead
        int max = Integer.MIN_VALUE;
        Board[] boards = new Board[num + 1];
        boards[boards.length - 1] = board;
        for(int i = 0; i < boards.length - 1; i++){
            boards[i] = new Board(board);
        }
        int[] scores = new int[moves.size()];
        for(int i = 0; i < moves.size(); i++){
            scores[i] = score2(moves.get(i), boards, num - 1);
            if(scores[i] > max){
                max = scores[i];
            }
        }
        ArrayList<Move> bestMoves = new ArrayList<Move>();
        for(int i = 0; i < scores.length; i++){
            if(scores[i] == max){
                bestMoves.add(moves.get(i));
            }
        }
        board.movePiece(bestMoves.get((int)(Math.random() * bestMoves.size())));
    }
    
    private int score2(Move move, Board[] boards, int depth){
        if(depth <= 0){
            boards[depth].copyFrom(boards[depth + 1]);
            boards[depth].movePiece(move);
            return evaluateBoard(boards[depth]);
        }
        boolean c = (depth % 2 == 0)? !color : color;
        boards[depth].copyFrom(boards[depth + 1]);
        boards[depth].movePiece(move);
        ArrayList<Move> moves = boards[depth].getAllMoves(c);
        int[] scores = new int[moves.size()];
        for(int i = 0; i < moves.size(); i++){
            scores[i] = score2(moves.get(i), boards, depth - 1);
        }
        if(moves.size() > 0){
            return getMinMax(scores, c == color);
        }
        return (c == color)? Integer.MIN_VALUE : Integer.MAX_VALUE;
    }
    
    private int getMinMax(int[] arr, boolean max){
        int value = max? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for(int x : arr){
            value = max? Math.max(value, x) : Math.min(value, x);
        }
        return value;
    }
    
    private int evaluateBoard(Board board){
        int value = (board.getComputerScore() - board.getPlayerScore()) * 3;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                Piece piece = board.getPiece(i, j);
                if(piece != null && piece.isInDanger()){
                    value += ((color == piece.getColor())? -1 : 1) * piece.getValue();
                }
            }
        }
        return value;
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

    private static void saveWeights(boolean color, ANN network){
        String filePath = "TrainedWeights/" + (color? "White.txt" : "Black.txt");
        PrintWriter pw = null;
        try{
            pw = new PrintWriter(filePath);
        }
        catch(Exception e){
            throw new RuntimeException("Error saving weights. Check save location.");
        }
        String str = "", str2 = "";
        double[][][] weights = round(network.getWeights(), 5);
        double[][] bias = round(network.getBias(), 5);
        for(int i = 0; i < weights.length; i++){
            String b = arrToString(bias[i]);
            str2 += b.substring(1, b.length() - 1) + "a,";
            for(int j = 0; j < weights[i].length; j++){
                String a = arrToString(weights[i][j]);
                str += a.substring(1, a.length() - 1) + "b,";
            }
            str = str.substring(0, str.length() - 2) + "a,";
        }
        str = str.substring(0, str.length() - 2);
        str2 = str2.substring(0, str2.length() - 2);
        pw.print(str + "c" + str2);
        pw.close();
    }
    
    private static double round(double num, int numDecimals){
        return Math.round(num * Math.pow(10, numDecimals)) / Math.pow(10, numDecimals);
    }
    
    private static double[] round(double[] nums, int numDecimals){
        for(int i = 0; i < nums.length; i++){
            nums[i] = round(nums[i], numDecimals);
        }
        return nums;
    }
    
    private static double[][] round(double[][] nums, int numDecimals){
        for(double[] arr : nums){
            round(arr, numDecimals);
        }
        return nums;
    }
    
    private static double[][][] round(double[][][] nums, int numDecimals){
        for(double[][] arr : nums){
            round(arr, numDecimals);
        }
        return nums;
    }

    public static void loadWeights(boolean color, ANN network){
        String filePath = "TrainedWeights/" + (color? "White.txt" : "Black.txt");
        try{
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String[] str =  bufferedReader.readLine().split("c");
            String[] arr1 = str[0].split("a,");
            String[] arr21 = str[1].split("a,");
            String[][] arr2 = new String[arr1.length][0];
            String[][] arr22 = new String[arr21.length][0];
            for(int i = 0; i < arr1.length; i++){
                arr2[i] = arr1[i].split("b,");
                arr22[i] = arr21[i].split(",");
            }
            String[][][] arr3 = new String[arr1.length][0][0];
            double[][][] weights = new double[arr1.length][0][0];
            double[][] bias = new double[arr21.length][0];
            for(int i = 0; i < arr1.length; i++){
                arr3[i] = new String[arr2[i].length][0];
                weights[i] = new double[arr2[i].length][0];
                bias[i] = new double[arr22[i].length];
                for(int j = 0; j < arr2[i].length; j++){
                    arr3[i][j] = arr2[i][j].split(",");
                    weights[i][j] = new double[arr3[i][j].length];
                    bias[i][j] = Double.parseDouble(arr22[i][j]);
                    for(int k = 0; k < arr3[i][j].length; k++){
                        weights[i][j][k] = Double.parseDouble(arr3[i][j][k]);
                    }
                }
            }
            network.setWeights(weights);
            network.setBias(bias);
            bufferedReader.close();
        }
        catch(Exception e){
            throw new RuntimeException("Error loading weights. " + e.getMessage());
        }
    }
}
