
/**
 * Write a description of class Linear here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Linear implements Activation
{
    public double activate(double output){
        return output;
    }
    public double derivative(double activatedOutput){
        return 1;
    }
}
