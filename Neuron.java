
/**
 * Write a description of class Neuron here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Neuron
{
    private double[] weights;
    private double bias;
    private Activation activation;
    private double previousOutput;

    /**
     * Creates a new neuron initialized with random weigths and bias
     * @param numWeights number of weights to initialize neuron with
     * @param activation activation function to use
     */
    public Neuron(int numWeights, Activation activation)
    {
        weights = new double[numWeights];
        for(int i = 0; i < weights.length; i++){
            weights[i] = Math.random();
        }
        bias = Math.random();
        this.activation = activation;
    }
    
    public double[] getWeights(){
        return weights;
    }
    
    public double getBias(){
        return bias;
    }
    
    /**
     * Multiplies weights to corresponding input, adds bias, and returns activated result
     * @param input the input to process
     * @return output after processing input
     */
    public double process(double[] input){
        double output = 0;
        for(int i = 0; i < input.length; i++){
            output += weights[i] * input[i];
        }
        output += bias;
        output = activation.activate(output);
        previousOutput = output;
        return output;
    }
}
