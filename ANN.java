
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
    
    private double cost(double output, double desired){
        return .5 * Math.pow(desired - output, 2);
    }
    
    private double costDeriv(double output, double desired){
        return output - desired;
    }
    
    private double[] cloneArray(double[] arr){
        double[] arr2 = new double[arr.length];
        for(int i = 0; i < arr.length; i++){
            arr2[i] = arr[i];
        }
        return arr2;
    }
    
    private void trainOne(double[] x, double[] y, double lr){
        double[] output = predict(x);
        double[] error = {};
        double[] error2 = {};
        for(int l = layers.length - 1; l > 0; l--){
            error2 = cloneArray(error);
            error = new double[layers[l].getNumNeurons()];
            for(int i = 0; i < error.length; i++){
                if(l == layers.length - 1){
                    error[i] = costDeriv(output[i], y[i]) * layers[l].getOutputDeriv(i);
                }
                else{
                    error[i] = 0;
                    for(int j = 0; j < layers[l + 1].getNumNeurons(); j++){
                        error[i] += layers[l + 1].getWeight(j, i) * error2[j] * layers[l].getOutputDeriv(i);
                    }
                }
            }
            for(int i = 0; i < error.length; i++){
                layers[l].updateBias(i, lr * error[i]);
                for(int j = 0; j < layers[l - 1].getNumNeurons(); j++){
                    layers[l].updateWeight(i, j, lr * layers[l - 1].getOutput(j) * error[i]);
                }
            }
        }
    }
    
    public void train(double[][] inputs, double[][] outputs, int iterations, double learningRate){
        learningRate *= -1;
        int numErrors = 0;
        for(int i = 0; i < iterations; i++){
            int index = (int)(Math.random() * inputs.length);
            if(inputs[index].length == layers[0].getNumNeurons() && outputs[index].length == layers[layers.length - 1].getNumNeurons()){
                trainOne(inputs[index], outputs[index], learningRate);
            }
            else{
                if(numErrors++ < iterations){
                    i--;
                }
            }
        }
        //return accumulative error later
    }
    
    //public void train(double[][] points, double maxError, double learningRate)
}
