import java.io.FileReader;
import java.io.BufferedReader;
/**
 * Write a description of class DatabaseToPoints here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class DatabaseToPoints
{
    private static double[][] inputs = {};
    private static double[][] outputs = {};
    
    public static void generate(boolean color){
        long time = System.currentTimeMillis();
        String filePath = "Dataset/First150KB.txt";
        String data = readFile(filePath);
        String[] games = data.split("\\n*\\[Event \"(.|\\n)*?\\n\\n\\n*");
        //games[0] is ""
        String[][] moves = new String[games.length - 1][0];
        for(int i = 1; i < games.length; i++){
            games[i] = games[i].replaceAll("\n", " ").replaceAll("  ", " ");
            moves[i - 1] = games[i].split(" *\\d\\d?\\d?\\. *");
            //moves[0] is ""
        }
        
        int[] winners = new int[moves.length];
        for(int i = 0; i < winners.length; i++){
            winners[i] = (color? 1 : -1) * getWinner(moves[i][moves[i].length - 1]);
            moves[i][moves[i].length - 1] = moves[i][moves[i].length - 1].replaceAll(" [012/]{1,3}-[012/]{1,3}", "");
        }
        
        int numMoves = 0;
        String[][][] splitMoves = new String[moves.length][0][0];
        for(int i = 0; i < moves.length; i++){
            splitMoves[i] = new String[moves[i].length - 1][2];
            for(int j = 1; j < moves[i].length; j++){
                splitMoves[i][j - 1] = moves[i][j].split(" ");
                numMoves++;
            }
        }
        
        Board board = new Board(true);
        int numErrors = 0, moveInd = 0;
        inputs = new double[numMoves][0];
        outputs = new double[numMoves][0];
        for(int i = 0; i < splitMoves.length; i++){
            for(int j = 0; j < splitMoves[i].length; j++){
                try{
                    Move.fromString(splitMoves[i][j][0], true, board).execute(board);
                    if(color){
                        inputs[moveInd] = toInput(board.getPieces());
                        outputs[moveInd] = new double[]{winners[i]};
                    }
                    if(splitMoves[i][j].length > 1){
                        Move.fromString(splitMoves[i][j][1], false, board).execute(board);
                        if(!color){
                            inputs[moveInd] = toInput(board.getPieces());
                            outputs[moveInd] = new double[]{winners[i]};
                        }
                    }
                    moveInd++;
                }
                catch(Exception e){
                    numErrors++;
                    break;
                }
            }
            board.reset();
        }
        System.out.println("Done generating points for " + numMoves + " moves in " + splitMoves.length + " games in " + (System.currentTimeMillis() - time) / 1000.0 + " seconds with " + numErrors + " errors");
    }
    
    private static String readFile(String filePath){
        try{
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String text = "";
            String line = "";
            while((line = bufferedReader.readLine()) != null){
                text += line + "\n";
            }
            bufferedReader.close();
            return text;
        }
        catch(Exception e){
            throw new RuntimeException("Error reading file");
        }
    }
    
    //-1 - black, 0 - tie, 1 - white
    private static int getWinner(String game){
        if(game.indexOf("1-0") > -1){
            return 1;
        }
        else if(game.indexOf("0-1") > -1){
            return -1;
        }
        return 0;
    }
    
    private static String arrToString(String[] arr){
        String str = "[";
        for(String val : arr){
            str += val + ", ";
        }
        str = str.substring(0, str.length() - 2) + "]";
        return str;
    }
    
    private static String arrToString(double[] arr){
        String str = "[";
        for(double val : arr){
            str += val + ",";
        }
        str = str.substring(0, str.length() - 1) + "]";
        return str;
    }
    
    private static double[] toInput(Piece[][] pieces){
        double[] arr = new double[64 * 12];
        for(int i = 0; i < pieces.length; i++){
            for(int j = 0; j < pieces[i].length; j++){
                if(pieces[i][j] != null){
                    int index = pieces[i][j].getNum() + (i * 8 + j) * 12;
                    arr[index] = 1;
                }
            }
        }
        return arr;
    }
    
    public static double[][] getInputs(){
        return inputs;
    }
    
    public static double[][] getOutputs(){
        return outputs;
    }
}