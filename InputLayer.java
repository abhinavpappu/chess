
/**
 * Write a description of class InputLayer here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class InputLayer implements Layer
{

    /**
     * Creates a new input layer
     * @param numInputs number of inputs to the network
     */
    public InputLayer(int numInputs)
    {
        
    }
    
    public double[][] getWeights(){
        return null;
    }
    
    public double[] getBias(){
        return null;
    }
    
    public double[] propogate(double[] input){
        return input;
    }
}
