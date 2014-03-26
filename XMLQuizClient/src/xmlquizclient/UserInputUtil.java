/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlquizclient;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 *
 * @author marco
 */
public class UserInputUtil {

    private static UserInputUtil instance = null;

    private final Scanner input;

    private final String OS = System.getProperty("os.name").toLowerCase();
    private String bufferNumberInput;

    public UserInputUtil() {
        input = new Scanner(System.in);
    }

    public void closeInput() {
        input.close();
    }

    public String getInitialChoise() {
        String choise;
        System.out.println("Choise between theese 3 options\n:> ");
        System.out.println("1: Store questions to the repository\n");
        System.out.println("2: Generate a Quiz\n");
        System.out.println("3: Format an XML Quiz in PDF\n");
        System.out.println(">:");
        choise = input.nextLine();
        while (!isInteger(choise)) {
            System.out.println("\nThe input inserted is not a number , you can choise between 1 , 2 or 3: >");
            choise = input.nextLine();
        }
        return choise;
    }

    public String getDidacticSectorInput() {
        String didacticSector;
        System.out.println("\nInsert a didactic Sector:> ");
        didacticSector = input.nextLine().toUpperCase();
        return didacticSector;
    }

    public Integer getDifficultLevelInput() {
        Integer level;
        System.out.println("\nSelect a difficult level between 1-5: >");
        bufferNumberInput = input.nextLine();
        while (!isInteger(bufferNumberInput)) {
            System.out.println("\nThe level inserted is not a number, insert a correct difficult level: >");
            bufferNumberInput = input.nextLine();
        }
        level = Integer.parseInt(bufferNumberInput);

        while (level < 1 || level > 5) {
            System.out.println("\nThe level inserted is out of range, insert a correct difficult level: >");
            level = Integer.parseInt(input.nextLine());
        }
        return level;
    }

    public Integer getQuestionsNumbInput() {
        Integer questionNumber;
        System.out.println("\nSelect the number of questions: >");
        bufferNumberInput = input.nextLine();
        while (!isInteger(bufferNumberInput)) {
            System.out.println("\nThe level inserted is not a number, insert a number of questions: \n");
            bufferNumberInput = input.nextLine();
        }
        questionNumber = Integer.parseInt(bufferNumberInput);
        return questionNumber;
    }

    public boolean getExitInput() {

        String exitParameter;
        boolean requestFulfilled = false;
        System.out.println("\nIt is not possible fulfill the request");
        System.out.println("\nPress 1 to try again or 0 to quit the application ..... ");
        exitParameter = input.nextLine();
        if (exitParameter.equals("0")) {
            requestFulfilled = true;
        }
        while (!exitParameter.equals("1") && !exitParameter.equals("0")) {
            System.out.println("\nWrong parameter, choose 1 to try again or 0 to quit the application");
            exitParameter = input.nextLine();
            if (exitParameter.equals("0")) {
                requestFulfilled = true;
            }
        }
        return requestFulfilled;
    }

    public String getStudentNameInput() {
        String didacticSector;
        System.out.println("\nInsert a Student Name:> ");
        didacticSector = input.nextLine().toUpperCase();
        return didacticSector;
    }

    public String getStudentSurnameInput() {
        String didacticSector;
        System.out.println("\nInsert a Student Surname:> ");
        didacticSector = input.nextLine().toUpperCase();
        return didacticSector;
    }

    public String getStudentIDInput() {
        String didacticSector;
        System.out.println("\nInsert a Student ID:> ");
        didacticSector = input.nextLine().toUpperCase();
        return didacticSector;
    }

    public String getPathToSaveQuiz() {
        String pathString = null;
        String fileNameString = null;
        boolean pathExist = false;
        boolean writable = false;

        System.out.println("Enter the name of the file to save >");
        do {
            fileNameString = input.nextLine();
        } while (fileNameString != null && fileNameString.isEmpty());

        do {
            System.out.println("Enter the ABSOLUTE EXISTING directory path where to save the generatedXmlFile >");
            pathString = input.nextLine();
            if (isUnix()) {
                while (!pathString.matches("^['\"]?(?:/[^/]+)*['\"]?$")) {
                    System.out.println("The path inserted is not corret, enter the ABSOLUTE EXISTING path");
                    pathString = input.nextLine();
                }

            } else if (isWindows()) {
                System.out.println("This is Windows");
            } else {
                System.out.println("Your OS is not support!!");
            }
            pathExist = Files.exists(Paths.get(pathString));

            if (!pathExist) {
                System.out.println("The directory inserted doesn't exist!!!\n");
            }
            if (pathExist) {
                writable = Files.isWritable(Paths.get(pathString));
                if (!writable) {
                    System.out.println("You don't have write permissions!!!\n");
                }
            }

        } while (!pathExist || !writable);

        return pathString + "/" + fileNameString;

    }
    
    

    public String getStoreQuestionInput() {
        String inputString;
        System.out.println("Enter a Valid QuestionsGroup as an Uri or as a String >");
        inputString = input.nextLine();
        return inputString;
    }

    public String getQuizToFormat() {
        String inputString;
        System.out.println("Enter a Valid Quiz to format as a String >");
        inputString = input.nextLine();
        return inputString;
    }

    /////////////////////////////////////////////// Utility interne /////////////////////////////////////////////////
    public boolean isWindows() {

        return (OS.indexOf("win") >= 0);

    }

    public boolean isUnix() {

        return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 || OS.indexOf("mac") >= 0);

    }

    private static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    public static UserInputUtil getInstance() {

        if (instance == null) {
            instance = new UserInputUtil();
        }
        return instance;
    }

}
