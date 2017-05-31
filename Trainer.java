import java.util.ArrayList;
import java.io.PrintWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import org.jblas.*;
import java.util.Scanner;
/**
 * Write a description of class Trainer here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Trainer
{
    public static void main(String[] args){
        double spi = .02;
        Scanner sc = new Scanner(System.in);
        System.out.print("Black(b) or White(w): ");
        String str = sc.nextLine().toLowerCase();
        boolean color = str.substring(0, 1).equals("w");
        int iterations = 100;
        boolean a = true;
        while(a){
            System.out.print("\nNumber of iterations: ");
            str = sc.nextLine();
            if(str.matches("[0-9]{1,8}")){
                int temp = Integer.parseInt(str);
                iterations = Math.min(10000000, Math.max(1, temp));
            }
            int s = (int)(iterations * spi);
            System.out.print(iterations + " iterations will take approximately " + s + " seconds (" + s/60/60 + 
                " hours, " + s/60 % 60 +" minutes, " + s % 60 + " seconds).\nDo you want to continue? (y/n): ");
            str = sc.nextLine().toLowerCase();
            a = !str.substring(0, 1).equals("y");
        }
        System.out.println();
        if(DatabaseToPoints.getOutputs().length == 0){
            DatabaseToPoints.generate(color);
        }
        System.out.println();
        int[] structure = {768, 1000, 1000, 1};
        ANN network = new ANN(structure);
        loadWeights(color, network);
        
        System.out.println("\nEnter the following commands anytime throughout training:\n" +
                            "1. pause - pauses training, resumable\n" +
                            "2. resume - resumes training if paused\n" +
                            "3. status - gets what percentage of training is complete\n" +
                            "4. stop - stops training, saves the weights, and quits\n" +
                            "5. save - pauses training, saves weights, and resumes training\n");
        
        boolean[] b = {true};
        final int iter = iterations;
        Thread t = new Thread(new Runnable(){
            public void run(){
                trainNetwork(network, DatabaseToPoints.getInputs(), DatabaseToPoints.getOutputs(), iter);
                System.out.println();
                saveWeights(color, network);
                System.out.println("\nPress enter to quit.\n");
                b[0] = false;
            }
        });
        
        System.out.println("Press enter to start\n"); //Make it start automatically later using Thread.sleep before while loop
        
        while(b[0]){
            str = sc.nextLine().toLowerCase();
            if(str.equals("pause") || str.equals("1")){
                try{
                    t.wait();
                }
                catch(Exception e){
                    throw new RuntimeException(e.getMessage());
                }
                System.out.println("Training Paused");
            }
            else if(str.equals("resume") || str.equals("2")){
                t.notify();
                System.out.println("Training Resumed");
            }
            else if(str.equals("status") || str.equals("3")){
                System.out.println(round((double)network.getCurrentIteration() / iter * 100, 3) + "% complete");
                int s = (int)((iter - network.getCurrentIteration()) * spi);
                System.out.println("Approximately " + s + " seconds (" + s/60/60 + " hours, " + s/60 % 60 +" minutes, " + s % 60 + " seconds) left.");
            }
            else if(str.equals("stop") || str.equals("4")){
                network.stopTraining();
                b[0] = false;
            }
            else if(str.equals("save") || str.equals("5")){
                try{
                    t.wait();
                }
                catch(Exception e){
                    throw new RuntimeException(e.getMessage());
                }
                saveWeights(color, network);
                t.notify();
            }
            else if(str.equals("")){
                if(t.getState() == Thread.State.NEW){
                    t.start();
                }
            }
            else{
                System.out.println("Command not recognized");
            }
        }
    }
    
    private static void trainNetwork(ANN network, double[][] inputs, double[][] outputs, int iterations){
        int randInd = (int)(Math.random() * inputs.length);
        double[] test = inputs[randInd];
        while(test.length == 0){ //to ensure that a valid test input is selected
            randInd = (int)(Math.random() * inputs.length);
            test = inputs[randInd];
        }
        System.out.println("Test input: " + arrToString(test));
        System.out.println("Actual Output: " + arrToString(outputs[randInd]));
        System.out.println("Network prediction before training: " + arrToString(network.predict(test)));
        
        double lr = .0001;
        System.out.println("Training network with a learning rate of " + lr + " for " + iterations + " iterations");
        long time = System.currentTimeMillis();
        
        network.train(inputs, outputs, iterations, lr);
        System.out.println("Done training " + network.getCurrentIteration() + " iterations with a learning rate of " + lr + " in " + (System.currentTimeMillis() - time) / 1000.0 + " seconds");
        System.out.println("Network prediction after training: " + arrToString(network.predict(test)));
    }
    
    private static String arrToString(double[] arr){
        String str = "[";
        for(double val : arr){
            str += val + ",";
        }
        str = str.substring(0, str.length() - 1) + "]";
        return str;
    }
    
    private static String arrToString(int[] arr){
        String str = "[";
        for(int val : arr){
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
    
    public static void saveWeights(boolean color, ANN network){
        String filePath = "TrainedWeights/" + (color? "White.txt" : "Black.txt");
        PrintWriter pw = null;
        try{
            pw = new PrintWriter(filePath);
        }
        catch(Exception e){
            throw new RuntimeException("Error saving weights. Check save location.");
        }
        System.out.println("Saving weights");
        long time = System.currentTimeMillis();
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
        System.out.println("Saved weights in " + (System.currentTimeMillis() - time) / 1000.0 + " seconds");
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
    
    /**
     * Loads weights from a file to a network
     * @param color color of weights to load from
     * @param network network to load weight to
     */
    public static void loadWeights(boolean color, ANN network){
        String filePath = "TrainedWeights/" + (color? "White.txt" : "Black.txt");
        System.out.println("Loading weights");
        long time = System.currentTimeMillis();
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
        System.out.println("Loaded weights in " + (System.currentTimeMillis() - time) / 1000.0 + " seconds");
    }
}
