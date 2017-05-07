
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
        String moveStrings = "1.e4 e6 2.d4 d5 3.Nd2 c5 4.exd5 Qxd5 5.Ngf3 cxd4 6.Bc4 Qd6 7.Qe2 Nf6 8.Nb3 Nc6 9.Bg5 Qb4+ 10.Bd2 Qb6 11.O-O-O Bd7 12.Bg5 Bc5 13.Kb1 O-O-O 14.Ne5 Nxe5 15.Qxe5 Bd6";
        moveStrings = moveStrings.replaceAll("[0-9]+\\.", "");
        String[] moves = moveStrings.split(" ");
        boolean color = true;
        Board board = new Board(true);
        for(String moveString : moves){
            Move move = Move.fromString(moveString, color, board);
            System.out.println(move);
            color = !color;
            move.execute(board);
        }
    }
    
    public static void testCastle(){
        Board board = new Board(true);
        board.getPieces()[7][6] = null;
        board.getPieces()[7][5] = null;
        Move move = new SpecialMove(true, true, true);
        move.execute(board);
        System.out.println(board);
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
