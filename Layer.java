
/**
 * Write a description of interface Layer here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public interface Layer
{
    double[][] getWeights();
    double[] getBias();
    double[] propogate(double[] input);
    int getNumNeurons();
    double getOutput(int i);
    double getOutputDeriv(int i);
    double getWeight(int i, int j);
    void updateBias(int i, double change);
    void updateWeight(int i, int j, double change);
}
