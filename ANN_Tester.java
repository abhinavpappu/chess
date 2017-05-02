
/**
 * Write a description of class ANN_Tester here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ANN_Tester
{
    public static void main(String[] args){
        ANN ann = new ANN(new int[]{1,1});
        System.out.println(toString(ann.predict(new double[]{1})));
    }
    
    private static String toString(double[] arr){
        String str = "[";
        for(double val : arr){
            str += val + ", ";
        }
        str = str.substring(0, str.length() - 2) + "]";
        return str;
    }
}
