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
    public static void main(String[] args) throws Exception{
        String filePath = "Dataset/FirstTen.txt";
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
            winners[i] = getWinner(moves[i][moves[i].length - 1]);
            moves[i][moves[i].length - 1] = moves[i][moves[i].length - 1].replaceAll(" [012/]{1,3}-[012/]{1,3}", "");
        }
        
        for(String[] game : moves){
            System.out.println(toString(game));
        }
        
        System.out.println("Done");
    }
    
    private static String readFile(String filePath) throws Exception{
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
    
    private static String toString(String[] arr){
        String str = "[";
        for(String val : arr){
            str += val + ", ";
        }
        str = str.substring(0, str.length() - 2) + "]";
        return str;
    }
}
