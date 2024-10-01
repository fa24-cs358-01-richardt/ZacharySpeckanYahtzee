import java.util.Random;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.lang.NegativeArraySizeException;

public class YahtzeeProcesses {

    //List of categories to score in
    String[] categories = new String[]{
        "Aces",
        "Twos",
        "Threes",
        "Fours",
        "Fives",
        "Sixes",
        "Three of a kind",
        "Four of a kind",
        "Full House",
        "Small Straight",
        "Large Straight",
        "Yahtzee",
        "Chance"
    };
    private int players[], score[], playerUpper[][];
    private String playerCategories[][];
    private boolean playerCheck[][], gotYahtzee[];

    // Initialization
    public YahtzeeProcesses(int play[], String playCat[][], boolean playCheck[][], int scr[], int playUp[][], boolean gotYaht[]){
        players = new int[play.length];
        playerCategories = new String[players.length][13];
        playerCheck  = new boolean[players.length][13];
        score = new int[13];
        playerUpper = new int[players.length][6];

        for (int i = 0; i < play.length; i++) {
            players[i] = play[i];
            score[i] = scr[i];
            gotYahtzee[i] = gotYaht[i];
            for (int j = 0; j < 13; j++) {
                playerCategories[i][j] = playCat[i][j];
                playerCheck[i][j] = playCheck[i][j];
                if (j < 6) {
                    playerUpper[i][j] = playUp[i][j];
                }
            }
        }
    }

    // Sets up the game
    public void setupGame(Scanner scan) throws InputMismatchException, NegativeArraySizeException {
        // Gets the number of players
        do {
            try {
                System.out.println("How many players?");
                players = new int[scan.nextInt()];
                if (players.length < 2 || players.length > 6) {
                    throw new InputMismatchException();
                }
            }
            // Exception handling
            catch (InputMismatchException e) {
                System.out.println("That's not a valid number. Try again.");
            }
            catch (NegativeArraySizeException e) {
                System.out.println("That's not a valid number. Try again.");
            }
            scan.nextLine();
        } while (!(players.length > 1) || !(players.length < 7));

        // Sets players numbers and initial score
        score = new int[players.length];
        playerUpper = new int[players.length][6];
        gotYahtzee = new boolean[players.length];
        for (int i = 0; i < players.length; i++) {
            players[i] = i + 1;
            score[i] = 0;
            gotYahtzee[i] = false;
            for (int j = 0; j < 6; j++) {
                playerUpper[i][j] = 0;
            }
        }

        // Contains all the categories the player has not filled
        playerCategories = new String[players.length][13];
        for (int i = 0; i < players.length; i++) {
            for (int j = 0; j < 13; j++) {
                playerCategories[i][j] = categories[j];
            }
        }

        // Keeps track of what categories have already been selected
        playerCheck = new boolean[players.length][13];
        for (int i = 0; i < players.length; i++) {
            for (int j = 0; j < 13; j++) {
                playerCheck[i][j] = true;
            }
        }
    }

    // MAIN GAMEPLAY LOOP
    public void playGame(Scanner scan) throws InputMismatchException {
        Random random = new Random();
        int currentResults[] = new int[5];
        int response, keep;
        boolean yahtzeeBonus;

        // Loops until all categories by all players are filled
        for (int t = 0; t < 13; t++) {
            //Loops until the round is complete
            for (int i = 0; i < players.length; i++) {
                yahtzeeBonus = false;
                System.out.println("Player " + (players[i]) + ", let's roll the dice...");

                // First roll
                boolean keepDie[] = new boolean[5];
                for (int j = 0; j < 5; j++) {
                    keepDie[j] = false;
                }
                for (int j = 0; j < 5; j++) {
                        if (!keepDie[j]) {
                            currentResults[j] = random.nextInt(5) + 1;
                            keepDie[j] = false;
                        }
                    }
                // Prints results
                System.out.println("You rolled:");
                for (int j = 0; j < 5; j++) {
                    System.out.print(currentResults[j] + " "); 
                }
                response = 0;
                // Rerolls dice up to two times
                for (int rolls = 0; rolls < 2; rolls++) {
                    do {
                        try {
                            System.out.println("\nDo you want to reroll any dice? (1 for yes, 2 for no)");
                            response = scan.nextInt();
                    
                            if (response == 1) {
                                // Asks which dice to keep
                                for (int j = 0; j < 5; j++) {
                                    System.out.println("Do you want to keep die #" + (j + 1) + "? (1 for yes, 2 for no)");
                                    keep = scan.nextInt();
                                    if (keep == 1) {
                                        keepDie[j] = true;
                                    }
                                }
                                // Rerolls dice
                                for (int j = 0; j < 5; j++) {
                                    if (!keepDie[j]) {
                                        currentResults[j] = random.nextInt(5) + 1;
                                        keepDie[j] = false;
                                    }
                                }
                                // Prints results
                                System.out.println("You rolled:");
                                for (int j = 0; j < 5; j++) {
                                    System.out.print(currentResults[j] + " "); 
                                }
                            }
                            // Breaks the loop if all dice are to be kept
                            if (response == 2) {
                                rolls = 1;
                            }
                            // Nonvalid inputs
                            if (response < 1 || response > 2) {
                                throw new InputMismatchException();
                            }
                        }
                        // Exception handling
                        catch (InputMismatchException e) {
                            System.out.println("That's not a valid number. Try again.");
                        }
                        scan.nextLine();
                    } while (response != 1 && response != 2);
                }
                // Displays which categories you can score in
                System.out.println("You can score in these categories:");

                //Keeps track of what categories are valid
                boolean[] openCategories = new boolean[13];
                for (int j = 0; j < openCategories.length; j++) {
                    openCategories[j] = false;
                }

                //Checks upper section categories
                //clearedUpper makes sure the same category doesn't get printed multiple times
                boolean[] clearedUpper = new boolean[6];
                for (int j = 0; j < 6; j++) {
                    clearedUpper[j] = true;
                }
                for (int j = 0; j < 6; j++) {
                    for (int k = 0; k < 5; k++) {
                        if (playerCheck[i][j] && currentResults[k] == j + 1 && clearedUpper[j] && playerCheck[i][j]) {
                        System.out.print("(" + (j + 1) + ") " + categories[j] + "  ");
                            clearedUpper[j] = false;
                            openCategories[j] = true;
                        }
                    }
                }

                //Sorts results least to greatest
                for (int j = 0; j < 4; j++) {
                    for (int k = j + 1; k < 5; k++) {
                        if (currentResults[j] > currentResults[k]) {
                            int temp = currentResults[k];
                            currentResults[k] = currentResults[j];
                            currentResults[j] = temp;
                        }
                    }
                }

                //Checks for three of a kind
                if (((currentResults[0] == currentResults[2]) || 
                    (currentResults[1] == currentResults[3]) ||
                    (currentResults[2] == currentResults[4])) && playerCheck[i][6]) {
                        System.out.print("(7) " + categories[6] + "  ");
                        openCategories[6] = true;
                }

                //Checks for four of a kind
                if (((currentResults[0] == currentResults[3]) || 
                    (currentResults[1] == currentResults[4])) && playerCheck[i][7]) {
                        System.out.print("(8) " + categories[7] + "  ");
                        openCategories[7] = true;
                }

                //Checks for Full House
                if ((((currentResults[0] == currentResults[1]) && 
                    (currentResults[0] == currentResults[2]) &&
                    (currentResults[3] == currentResults[4])) ||
                    ((currentResults[0] == currentResults[1]) && 
                    (currentResults[2] == currentResults[3]) &&
                    (currentResults[2] == currentResults[4]))) && playerCheck[i][8]) {
                        System.out.print("(9) " + categories[8] + "  ");
                        openCategories[8] = true;
                }

                //Checks for Small/Large Straight
                int ascend = 0;
                for (int j = 0; j < 4; j++) {
                    if (currentResults[j + 1] == currentResults[j] + 1) {
                        ascend++;
                    }
                }
                if (ascend >= 3 && playerCheck[i][9]) {
                    System.out.print("(10) " + categories[9] + "  ");
                    openCategories[9] = true;
                }
                if (ascend == 4 && playerCheck[i][10]) {
                    System.out.print("(11) " + categories[10] + "  ");
                    openCategories[10] = true;
                }

                //Checks for Yahtzee
                if (currentResults[0] == currentResults[4]) {
                    if (playerCheck[i][11]) {
                        System.out.print("(12) " + categories[11] + "  ");
                        openCategories[11] = true;
                    }
                    // And Yahtzee bonus
                    else if (gotYahtzee[i] == true) {
                        yahtzeeBonus = true;
                        System.out.println("You also got a Yahtzee bonus!");
                    }
                }

                // Checks for chance
                if (playerCheck[i][12]) {
                    System.out.print("(13) " + categories[12]);
                    openCategories[12] = true;
                }

                // Checks if any categories are available
                boolean open = false;
                for (int j = 0; j < openCategories.length; j++) {
                    if (openCategories[j] == true) {
                        open = true;
                    }
                }

                // Selecting a category to score in
                int selection = -1;
                int result = 0;
                do {
                    try {
                        // If categories are available:
                        if (open) {
                            System.out.println("\nWhat category do you want to score in? (Enter the number next the category you want to select, or 0 if you want to sacrifice a category.)");
                            selection = scan.nextInt();

                            //Upper section score
                            if (selection > 0 && selection < 7) {
                                for (int j = 0; j < 5; j++) {
                                    if (currentResults[j] == selection) {
                                        result += selection;
                                        playerUpper[i][selection - 1] += selection;
                                    }
                                }
                            }

                            // Three of a kind, four of a kind, and chance score
                            if (selection == 7 || selection == 8 || selection == 13) {
                                for (int j = 0; j < 5; j++) {
                                    result += currentResults[j];
                                }
                            }

                            // Full House score
                            if (selection == 9 && openCategories[8]) {
                                result = 25;
                            }

                            // Small Straight score
                            if (selection == 10 && openCategories[9]) {
                                result = 30;
                            }

                            // Large Straight score
                            if (selection == 11 && openCategories[10]) {
                                result = 40;
                            }

                            // Yahtzee score
                            if (selection == 12 && openCategories[11]) {
                                result = 50;
                                gotYahtzee[i] = true;
                            }

                            // If no categories are selected:
                            if (selection == 0) {
                                open = false;
                            }

                            // If a category is selected:
                            else if ((selection > 0 && selection < 14) && openCategories[selection - 1]) {
                                playerCheck[i][selection - 1] = false;
                            }

                            // Bad input
                            else {
                                System.out.println("That category isn't available. Try again.");
                            }
                        }
                        // If no categories can be selected:
                        if (!open) {
                            System.out.println("Choose a category to score zero points in. (Enter the number next the category you want to select.)");

                            // Prints all player categories not already selected
                            for (int j = 0; j < 13; j++) {
                                if (playerCheck[i][j]) {
                                    System.out.print("(" + (j + 1) + ") " + playerCategories[i][j] + "  ");
                                }
                            }
                            System.out.println("");
                            // Scores zero in a category
                            selection = scan.nextInt();
                            if (playerCheck[i][selection - 1]) {
                                playerCheck[i][selection - 1] = false;
                            }
                            else {
                                throw new InputMismatchException();
                            }
                            result += 0;
                            selection = 0;
                        }
                        // Yahtzee bonus score
                        if (yahtzeeBonus) {
                            result += 100;
                        }
                    }
                    // Exception handling
                    catch (InputMismatchException e) {
                        System.out.println("That's not a valid number. Try again.");
                    }
                    catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println("That's not a valid number. Try again.");
                    }
                    scan.nextLine();
                } while ((selection < 0 || selection > 13) || (result == 0 && selection != 0));

                // Adds score
                score[i] += result;
                System.out.println("Player " + players[i] + ", you scored " + result + " points this round. Your total score is " + score[i] + ". That's it for your turn...");
            }
        }
    }

    // Gets bonuses, sorts scores, and calculates winner
    public void endGame() {
        System.out.println("GAME OVER!");

        // Checks for upper section bonus
        for (int i = 0; i < players.length; i++) {
            int upperTotal = 0;
            for (int j = 0; j < 6; j++) {
                upperTotal += playerUpper[i][j];
            }
            if (upperTotal >= 63) {
                score[i] += 35;
                System.out.println("Player " + players[i] + " got an upper sections bonus!");
            }
        }

        // Sorts scores least to greatest (along with players)
        for (int i = 0; i < players.length - 1; i++) {
            for (int j = i + 1; j < players.length; j++) {
                if (score[i] > score[j]) {
                    int temp = score[j];
                    score[j] = score[i];
                    score[i] = temp;

                    temp = players[j];
                    players[j] = players[i];
                    players[i] = temp;
                }
            }
        }

        // Displays winners from least to greatest
        for (int i = 0; i < players.length; i++) {
            int place = players.length - i;
            if (place > 3) {
                System.out.println(place + "th place: Player " + players[i] + " - " + score[i] + " points");
            }
            if (place == 3) {
                System.out.println(place + "rd place: Player " + players[i] + " - " + score[i] + " points");
            }
            if (place == 2) {
                System.out.println(place + "nd place: Player " + players[i] + " - " + score[i] + " points");
            }
            if (place == 1) {
                System.out.println("WINNER: Player " + players[i] + " - " + score[i] + " points");
            }
        }
    }
}
