
/**
 * Write a description of class Classifier here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ANN
{
    private Layer[] layers;

    /**
     * Creates a new classifier network
     */
    public ANN(int[] structure)
    {
        if(structure.length < 2){
            throw new RuntimeException("Need a minimum of 2 layers for a neural network");
        }
        layers = new Layer[structure.length];
        layers[0] = new InputLayer(structure[0]);
        for(int i = 1; i < layers.length - 1; i++){
            if(structure[i] < 1){
                throw new RuntimeException("Need at least 1 neuron in each layer");
            }
            layers[i] = new HiddenLayer(structure[i - 1], structure[i]);
        }
        int lastLayer = layers.length - 1;
        layers[lastLayer] = new OutputLayer(structure[lastLayer - 1], structure[lastLayer]);
    }
    
    public double[] predict(double[] input){
        double[] output = input;
        for(int i = 0; i < layers.length; i++){
            output = layers[i].propogate(output);
        }
        return output;
    }
    
    private void trainOne(double[] input, double[] output, learningRate){
        double error = 
    }
    
    public void train(double[][] inputs, double[][] outputs, int iterations, double learningRate){
        
    }
    
    //public void train(double[][] points, double maxError, double learningRate)
}
