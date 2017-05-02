
/**
 * Write a description of class Tanh here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Tanh implements Activation
{
    public double activate(double output){
        return Math.tanh(output);
    }
    public double derivative(double activatedOutput){
        return 1 - Math.pow(activatedOutput, 2);
    }
}
