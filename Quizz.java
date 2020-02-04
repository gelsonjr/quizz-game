/* Quizz.java
 * 12/02/2019
 * Gelson Cardoso
 * Description: This program will utilize dialog boxes to interact with the user.
 * This program demonstrates the use of dialog boxes as an alternative for console input and output.
 */

import java.util.Scanner;
import java.io.*;
import javax.swing.*;
import java.awt.*;  // needed to manipulate UIManager colors

public class Quizz {

    public static final int SIZE = 10;
    public static final String GOOD_BYE = "Thank you for playing!\n\nHave a great day!\n\n";

    public static void main(String[] args) throws IOException {

        // Invoke styling() method to customize JOptionPane default UI
        styling();

        JOptionPane.showMessageDialog(null, "WELCOME TO OUR JAVA TRIVIA GAME!\n");

        String userName;
        do {
            userName = JOptionPane.showInputDialog("Please enter your name: ");

            if (userName == null){
                JOptionPane.showMessageDialog(null, GOOD_BYE);
                System.exit(0);
            }
        } while (userName.isEmpty());

        char menuChoice;

        do {
            menuChoice = displayMainMenu();

            if (menuChoice == '1')
                // Invoke displayRules() method to display rules of the game
                displayRules();

            else if (menuChoice == '2') {

                int userScore = 0; // initialize user points and also clear them for a new game

                String[] questionArray = new String[SIZE];
                String[] answerA = new String[SIZE];
                String[] answerB = new String[SIZE];
                String[] answerC = new String[SIZE];
                String[] answerD = new String[SIZE];
                char[] correctAnswer = new char[SIZE];
                int[] pointValue = new int[SIZE];

                String[] highNames = new String[3];
                int[] highScores = new int[3];

                // Declare file object
                File questionsFile = new File("questions.txt");

                // Verify if file exists
                if (!questionsFile.exists()) {
                    JOptionPane.showMessageDialog(null,
                            "The file questions.txt is not found. Please look for the file and " +
                                    "try again.");

                    System.exit(0);
                }

                Scanner qScanner = new Scanner(questionsFile);

                for (int i = 0; i < 10; i++) {
                    questionArray[i] = qScanner.nextLine();
                    answerA[i] = qScanner.nextLine();
                    answerB[i] = qScanner.nextLine();
                    answerC[i] = qScanner.nextLine();
                    answerD[i] = qScanner.nextLine();
                    correctAnswer[i] = qScanner.nextLine().charAt(0);
                    pointValue[i] = Integer.parseInt(qScanner.nextLine());

                    userScore += processQuestion(questionArray[i], answerA[i], answerB[i],
                            answerC[i], answerD[i], correctAnswer[i], pointValue[i]);

                    // Invoke displayScore() method to display user's current score
                    displayScore(userScore);
                }

                qScanner.close();

                JOptionPane.showMessageDialog(null,
                        "End of game. You accumulated a total of " + userScore + " points(s).");

                // Invoke readInHighScore() method to read names and scores from file and store
                // them into highNames[] and highScores[] arrays
                ReadInHighScores(highNames, highScores);

                // Invoke CompareScores() method to compare userScore with the high scores from
                // the file, replace and re-arrange them if higher
                CompareScores(userScore, userName, highNames, highScores);

                // Invoke UpdateHighScores() method to update high score text file with current
                // high scores
                UpdateHighScores(highNames, highScores);

            } else if (menuChoice == '3')
                JOptionPane.showMessageDialog(null, GOOD_BYE);

            else
                JOptionPane.showMessageDialog(null, "Invalid entry, please try again!");

        } while (menuChoice != '3');

        System.exit(0);
    }

    // --- Method definitions ---

    // return type: char
    // parameters: none
    // purpose: This method prompts for and return the main menu choice.
    public static char displayMainMenu() {

        // Invoke styling() method to customize JOptionPane default UI
        styling();

        // Declare and initialize variables
        int menuInput;
        char menuInputChar;
        Character[] menuButtons = {'1', '2', '3'}; // buttons

        // Display main menu and prompt user for the main menu choice
        menuInput = JOptionPane.showOptionDialog(null,
                "Please select an option:\n\n" +
                        "1) See Rules\n\n" +
                        "2) Play Game\n\n" +
                        "3) Exit\n\n",
                "Main Menu", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                menuButtons, menuButtons[0]);

        // if user clicks on "x" button the program will end
        if (menuInput == JOptionPane.CLOSED_OPTION){
            JOptionPane.showMessageDialog(null, GOOD_BYE);
            System.exit(0);
        }

        // using menuInput integer value as the array subscript
        menuInputChar = menuButtons[menuInput];
        //menuInputChar = Integer.toString(menuInput).charAt(0);

        return menuInputChar;
    }

    // return type: void
    // parameters: none
    // purpose: This method displays the rules of the game to the user
    public static void displayRules() {

        // Invoke styling() method to replace JOptionPane default UI
        styling();

        // Display the rules of the game
        JOptionPane.showMessageDialog(null,
                "Rules of our Game:\n\n" +
                        "1) Read chapters 1-7\n\n" +
                        "2) No smartphones or any electronic devices allowed\n\n" +
                        "3) Play fair and have fun!\n\n");
    }

    // return type: int
    // parameters: 5 strings (q1, ansA, ansB, ansC, ansD), 1 char (correctAns), 1 int (pointValue)
    // purpose: This method displays a question and its answers based on data from a file and
    // returns the points that the user received if the answers is correct otherwise it returns 0.
    public static int processQuestion(String q1, String ansA, String ansB, String ansC,
                                      String ansD, char correctAns, int pointValue) {

        // Invoke styling() method to customize JOptionPane default UI
        styling();

        Character[] gameButtons = {'A', 'B', 'C', 'D'}; // buttons

        // Questions (showOptionDialog returns an integer)
        int gameInput = JOptionPane.showOptionDialog(null,
                q1 +
                        "\n\na) " + ansA +
                        "\n\nb) " + ansB +
                        "\n\nc) " + ansC +
                        "\n\nd) " + ansD +
                        "\n\nPlease select your answer:",
                "Questions", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                gameButtons, gameButtons[0]);

        // if user clicks on "x" button the program will end
        if (gameInput == JOptionPane.CLOSED_OPTION){
            JOptionPane.showMessageDialog(null, GOOD_BYE);
            System.exit(0);
        }

        // using menuInput integer value as the array subscript
        char gameInputChar = gameButtons[gameInput];

        // Correct answer validation
        if (gameInputChar == correctAns) {
            JOptionPane.showMessageDialog(null,
                    "CORRECT ANSWER!\n\nYou won " + pointValue + " points.");
            return pointValue;
        }

        else {
            JOptionPane.showMessageDialog(null,
                    "Wrong answer. Correct answer was \"" + correctAns + "\"");
            return 0;
        }
    }

    // return type: void
    // parameters: 1 int (score)
    // purpose: This method displays the user's current total
    public static void displayScore(int score) {

        // Display user score
        JOptionPane.showMessageDialog(null, "Your current score is " + score + " points.");
    }

    // return type: void
    // parameters: 1 String[] (name) and 1 int[] (score)
    // purpose: This method opens “highscore.txt” and reads the current names and scores
    public static void ReadInHighScores(String[] name, int[] score) throws IOException {

        // Invoke styling() method to customize JOptionPane default UI
        styling();

        // Declare File object to read file
        File highscoreFile = new File("highscore.txt");

        // To verify if file exists
        if (!highscoreFile.exists()) {
            JOptionPane.showMessageDialog(null,
                    "The file highscore.txt is not found. Please look for the file and try again.");

            System.exit(0);
        }

        // Declare Scanner object to read file
        Scanner scanner = new Scanner(highscoreFile);

        for (int i = 0; i < 3; i++) {
            name[i] = scanner.next();
            score[i] = Integer.parseInt(scanner.next());
        }

        //int highScore = hScanner.nextInt();
        scanner.close(); // to close highscore.txt Scanner object
    }

    // return type: void
    // parameters: 1 int (userScore), 1 String (userName). 1 String[] (name) and 1 int[] (score)
    // purpose: This method compares userScore and userName with high scores from file, if higher
    // replaces file high scores with userScore
    public static void CompareScores(int userScore, String userName, String[] name, int[] score){

        // To make sure array is in descending order of high scores
        for (int i = 0; i < score.length; i++) {
            for (int j = 0; j < score.length; j++) {
                if (score[i] > score[j]) {
                    int temp = score[i];
                    score[i] = score[j];
                    score[j] = temp;

                    String temp2 = name[i]; // to match parallel array order of Strings
                    name[i] = name[j];
                    name[j] = temp2;
                }
            }
        }

        // To compare userScore to the high score stored into score[] array
        if (userScore > score[0]){
            score[2] = score[1];
            score[1] = score[0];
            score[0] = userScore;
            name[2] = name[1];
            name[1] = name[0];
            name[0] = userName;
        }
        else if (userScore > score[1]){
            score[2] = score[1];
            score[1] = userScore;
            name[2] = name[1];
            name[1] = userName;
        }
        else if (userScore > score[2]){
            score[2] = userScore;
            name[2] = userName;
        }

    }

    // return type: void
    // parameters: 1 String[] (highName) and 1 int[] (highScore)
    // purpose: This method updates the file “highscore.txt” with the scores and names in the arrays
    public static void UpdateHighScores(String[] highName, int[] highScore) throws IOException {

        // Declare a PrintWriter object to print data to file
        PrintWriter fileWriter = new PrintWriter("highscore.txt");

        // loop to write values from the arrays into the file
        for (int i = 0; i < highName.length; i++) {
            fileWriter.write(String.format("%s %s%n", highName[i], highScore[i]));
        }
        fileWriter.close();
    }

    // return type: void
    // parameters: none
    // purpose: This method replaces the default style of JOptionPane UI
    public static void styling(){
        // Replaces default values using UIManager
        UIManager.put("Panel.background", Color.black);
        UIManager.put("OptionPane.background", Color.black);
        UIManager.put("Button.margin", new Insets(2, 2, 2, 2));
        UIManager.put("Button.background", Color.black);
        UIManager.put("OptionPane.buttonPadding", 12);
        UIManager.put("Button.foreground", Color.green);
        UIManager.put("Button.select", Color.green);
        UIManager.put("OptionPane.messageFont", new Font("Consolas", Font.BOLD, 14));
        UIManager.put("OptionPane.buttonFont", new Font("Consolas", Font.BOLD, 14));
        UIManager.put("OptionPane.messageForeground", Color.green);
        UIManager.put("TextField.background", Color.black);
        UIManager.put("TextField.foreground", Color.green);
        UIManager.put("TextField.caretForeground", Color.green);
    }
}
