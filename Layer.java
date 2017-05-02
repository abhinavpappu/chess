
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
}
