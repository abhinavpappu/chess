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
    private static void trainNetwork(boolean color){
        DatabaseToPoints.generate(true);
        double[][] inputs = DatabaseToPoints.getInputs();
        double[][] outputs = DatabaseToPoints.getOutputs();
        int[] structure = {768, 64, 1};
        ANN network = new ANN(structure);
        long time = System.currentTimeMillis();
        int iterations = 100000;
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
        trainNetwork(true);
    }
    
    public static void trainBlackNetwork(){
        trainNetwork(false);
    }
    
    private static String arrToString(double[] arr){
        String str = "[";
        for(double val : arr){
            str += val + ",";
        }
        str = str.substring(0, str.length() - 1) + "]";
        return str;
    }
}
