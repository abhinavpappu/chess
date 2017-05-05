
/**
 * Write a description of class ANN_Tester here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ANN_Tester
{
    public static void main(String[] args){
        ANN ann = new ANN(new int[]{1, 2});
        String str = arrToString(ann.predict(new double[]{1}));
        System.out.println(str);
    }
    
    public static void testMove(){
        Board board = new Board(true);
        Move move = Move.fromString("e4", true, board);
        System.out.println(move);
    }
    
    private static String arrToString(double[] arr){
        String str = "[";
        for(double val : arr){
            str += val + ", ";
        }
        str = str.substring(0, str.length() - 2) + "]";
        return str;
    }
}
