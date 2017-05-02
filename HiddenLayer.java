
/**
 * Write a description of class Layer here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class HiddenLayer implements Layer
{
    private Neuron[] neurons;

    /**
     * Creates a new hidden layer with tanh neurons
     * @param numInputs number of inputs to this layer (number of outputs from previous layer)
     * @param numOutputs number of outputs from this layer
     */
    public HiddenLayer(int numInputs, int numOutputs)
    {
        Activation tanh = new Tanh();
        neurons = new Neuron[numOutputs];
        for(int i = 0; i < neurons.length; i++){
            neurons[i] = new Neuron(numInputs, tanh);
        }
    }
    
    public double[][] getWeights(){
        double[][] weights = new double[neurons.length][0];
        for(int i = 0; i < neurons.length; i++){
            weights[i] = neurons[i].getWeights();
        }
        return weights;
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
}
