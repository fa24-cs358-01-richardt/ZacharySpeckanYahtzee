
import java.util.Scanner;
import java.util.InputMismatchException;
import java.lang.NegativeArraySizeException;

public class Yahtzee {
    public static void main(String[] args) throws InputMismatchException, NegativeArraySizeException {

        // Import
        Scanner scan = new Scanner(System.in);

        // Initialization of variables used in game
        int players[] = new int[0];    
        String playerCategories[][] = new String[players.length][13];
        boolean playerCheck[][]  = new boolean[players.length][13];
        int score[] = new int[13];
        int playerUpper[][] = new int[players.length][6];
        boolean gotYahtzee[] = new boolean[players.length];
        YahtzeeProcesses processes = new YahtzeeProcesses(players, playerCategories, playerCheck, score, playerUpper, gotYahtzee);

        // Start, middle, and end of game
        processes.setupGame(scan);
        processes.playGame(scan);
        processes.endGame();

        scan.close();
    }
}
