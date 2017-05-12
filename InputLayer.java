
/**
 * Write a description of class InputLayer here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class InputLayer implements Layer
{
    private int numInputs;
    private double[] previousOutput;
    
    /**
     * Creates a new input layer
     * @param numInputs number of inputs to the network
     */
    public InputLayer(int numInputs)
    {
        this.numInputs = numInputs;
    }
    
    public double[][] getWeights(){
        return null;
    }
    
    public void setWeights(double[][] weights){
    }
    
    public double[] getBias(){
        return null;
    }
    
    public double[] propogate(double[] input){
        previousOutput = input;
        return input;
    }
    
    public int getNumNeurons(){
        return numInputs;
    }
    
    public double getOutput(int i){
        return previousOutput[i];
    }
    
    public double getOutputDeriv(int i){
        return 0;
    }
    
    public double getWeight(int i, int j){
        return 0;
    }
    
    public void updateBias(int i, double change){
    }
    
    public void updateWeight(int i, int j, double change){
    }
}
