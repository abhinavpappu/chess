import org.jblas.*;
/**
 * Write a description of class Classifier here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ANN
{
    private DoubleMatrix[] layers;
    private DoubleMatrix[] biases;
    private DoubleMatrix[] outputs;
    private volatile boolean stopTraining;
    private int currentIter;

    /**
     * Constructor for ANN (Artificial Neural Network)
     * @param structure structure of network
     */
    public ANN(int[] structure)
    {
        layers = new DoubleMatrix[structure.length - 1];
        biases = new DoubleMatrix[structure.length - 1];
        for(int i = 0; i < layers.length; i++){
            layers[i] = DoubleMatrix.randn(structure[i], structure[i + 1]);
            biases[i] = DoubleMatrix.randn(structure[i + 1]);
        }
    }
    
    /**
     * Returns an output given an input
     * @param input input to calculate output from
     * @return output
     */
    public DoubleMatrix predict(DoubleMatrix input){
        outputs = new DoubleMatrix[layers.length + 1];
        outputs[0] = input.reshape(1, layers[0].rows);
        for(int i = 0; i < layers.length; i++){
            outputs[i + 1] = outputs[i].mmul(layers[i]).addRowVector(biases[i]);
            if(i < layers.length - 1){
                MatrixFunctions.tanhi(outputs[i + 1]);
            }
        }
        return outputs[layers.length];
    }
    
    /**
     * Returns an output given an input
     * @param input input to calculate output from
     * @return output
     */
    public double[] predict(double[] input){
        DoubleMatrix output = predict(new DoubleMatrix(input));
        return output.toArray();
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

    private void trainOne(DoubleMatrix x, DoubleMatrix y, double lr){
        DoubleMatrix output = predict(x);
        DoubleMatrix error;
        DoubleMatrix error2 = null;
        for(int l = layers.length - 1; l > 0; l--){
            if(l == layers.length - 1){
                error = outputs[l + 1].sub(y);
            }
            else{
                MatrixFunctions.powi(outputs[l + 1], 2);
                error = error2.mmul(layers[l + 1].transpose()).mul(outputs[l + 1].mul(-1).add(1));
            }
            biases[l].addi(error.mul(lr));
            layers[l].addi(outputs[l].transpose().mmul(error).mul(lr));
            error2 = error.dup();
        }
    }
    
    private void trainOne(double[] x, double[] y, double lr){
        trainOne(new DoubleMatrix(x).reshape(1, x.length), new DoubleMatrix(y).reshape(1, y.length), lr);
    }

    /**
     * Trains network on a set of training points
     * @param inputs inputs of training datatset
     * @param outputs outputs of trainging dataset
     * @param iterations number of times to train
     * @param learningRate how fast the network should learn
     */
    public void train(double[][] inputs, double[][] outputs, int iterations, double learningRate){
        learningRate *= -1;
        int numErrors = 0;
        for(int i = 0; i < iterations; i++){
            currentIter = i;
            int index = (int)(Math.random() * inputs.length);
            if(inputs[index].length == layers[0].rows && outputs[index].length == layers[layers.length - 1].columns){
                trainOne(inputs[index], outputs[index], learningRate);
            }
            else{
                if(numErrors++ < iterations){
                    i--;
                }
            }
            if(stopTraining){
                break;
            }
        }
        //return accumulated error later
    }
    
    public void stopTraining(){
        stopTraining = true;
    }
    
    public int getCurrentIteration(){
        return currentIter;
    }

    //public void train(double[][] points, double maxError, double learningRate)
    
    /**
     * Gets weights in network
     * @return weights
     */
    public double[][][] getWeights(){
        double[][][] weights = new double[layers.length][0][0];
        for(int i = 0; i < layers.length; i++){
            weights[i] = layers[i].transpose().toArray2();
        }
        return weights;
    }
    
    /**
     * Gets biases in network
     * @return biases
     */
    public double[][] getBias(){
        double[][] bias = new double[layers.length][0];
        for(int i = 0; i < layers.length; i++){
            bias[i] = biases[i].toArray();
        }
        return bias;
    }
    
    /**
     * Sets the weights of the network
     * @param weights weights to set network with
     */
    public void setWeights(double[][][] weights){
        for(int i = 0; i < layers.length; i++){
            layers[i] = new DoubleMatrix(weights[i]).transpose();
        }
    }
    
    /**
     * Sets the biases of the network
     * @param biases biases to set network with
     */
    public void setBias(double[][] bias){
        for(int i = 0; i < layers.length; i++){
            biases[i] = new DoubleMatrix(bias[i]).reshape(1, bias[i].length);
        }
    }
}
