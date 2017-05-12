import java.util.ArrayList;
import java.io.PrintWriter;
import java.io.FileReader;
import java.io.BufferedReader;
/**
 * Write a description of class ComputerPlayer here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ComputerPlayer
{
    private static ANN network = new ANN(new int[]{768, 768, 768, 96, 64, 12, 1});
    private static boolean color = true;

    private static void trainNetwork(){
        if(DatabaseToPoints.getOutputs().length == 0){
            DatabaseToPoints.generate(color);
        }
        double[][] inputs = DatabaseToPoints.getInputs();
        double[][] outputs = DatabaseToPoints.getOutputs();
        //loadWeights(color);
        long time = System.currentTimeMillis();
        int randInd = (int)(Math.random() * inputs.length);
        double[] test = inputs[randInd];
        System.out.println("Test input: " + arrToString(test));
        System.out.println("Network prediction before training: " + arrToString(network.predict(test)));
        int iterations = 100;
        double lr = .01;
        network.train(inputs, outputs, iterations, lr);
        System.out.println("Done training " + iterations + " iterations with a learning rate of " + lr + " in " + (System.currentTimeMillis() - time) / 1000.0 + " seconds");
        System.out.println("Network prediction after training: " + arrToString(network.predict(test)));
        System.out.println("Actual Output: " + arrToString(outputs[randInd]));
        time = System.currentTimeMillis();
        saveWeights();
        System.out.println("Saved weights in " + (System.currentTimeMillis() - time) / 1000.0 + " seconds");
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

    private static void saveWeights(){
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
            for(double[] neuron : weights[i]){
                String a = arrToString(neuron);
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

    public static void loadWeights(boolean computerColor){
        color = computerColor;
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
                arr22[i] = arr21[i].split("b, ");
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
            bufferedReader.close();
        }
        catch(Exception e){
            throw new RuntimeException("Error loading weights. " + e.getMessage());
        }
    }
}
