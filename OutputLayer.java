
/**
 * Write a description of class Layer here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class OutputLayer implements Layer
{
    private Neuron[] neurons;

    /**
     * Creates a new hidden layer with neurons
     * @param numInputs number of inputs to this layer (number of outputs from previous layer)
     * @param numOutputs number of outputs from this layer
     */
    public OutputLayer(int numInputs, int numOutputs)
    {
        Activation linear = new Linear();
        neurons = new Neuron[numOutputs];
        for(int i = 0; i < neurons.length; i++){
            neurons[i] = new Neuron(numInputs, linear);
        }
    }
    
    public double[][] getWeights(){
        double[][] weights = new double[neurons.length][0];
        for(int i = 0; i < neurons.length; i++){
            weights[i] = neurons[i].getWeights();
        }
        return weights;
    }
    
    public void setWeights(double[][] weights){
        for(int i = 0; i < neurons.length; i++){
            neurons[i].setWeights(weights[i]);
        }
    }
    
    public double[] getBias(){
        double[] bias = new double[neurons.length];
        for(int i = 0; i < neurons.length; i++){
            bias[i] = neurons[i].getBias();
        }
        return bias;
    }
    
    public double[] propogate(double[] input){
        double[] output = new double[neurons.length];
        for(int i = 0; i < output.length; i++){
            output[i] = neurons[i].process(input);
        }
        return output;
    }
    
    public int getNumNeurons(){
        return neurons.length;
    }
    
    public double getOutput(int i){
        return neurons[i].getOutput();
    }
    
    public double getOutputDeriv(int i){
        return neurons[i].getOutputDeriv();
    }
    
    public double getWeight(int i, int j){
        return neurons[i].getWeight(j);
    }
    
    public void updateBias(int i, double change){
        neurons[i].updateBias(change);
    }
    
    public void updateWeight(int i, int j, double change){
        neurons[i].updateWeight(j, change);
    }
}
