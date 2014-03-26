/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mwt.xml;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
//import org.mwt.xml.engine.ComplianceReqCheckInterface;
import org.mwt.xml.engine.ComplianceReqCheck;
import org.mwt.xml.model.Question;
import org.mwt.xml.model.Quiz;
import org.mwt.xml.model.Student;

/**
 *
 * @author marco
 */
public class UserInputUtil {

    private static UserInputUtil instance = null;

    private final Scanner input;
    private final ComplianceReqCheck complianceRequest;
    private final String OS = System.getProperty("os.name").toLowerCase();
    private String bufferNumberInput;

    public UserInputUtil() {
        input = new Scanner(System.in);
        complianceRequest = ComplianceReqCheck.getInstance();
    }

    public void closeInput() {
        input.close();
    }

    public String getDidacticSectorInput() {
        String didacticSector;
        List<String> sectorsList = new ArrayList<>();
        System.out.println("\nSelect a Didactic Sector among these: ");
        sectorsList = complianceRequest.listAllSectors();
        printAllXmlFiles(sectorsList);
        System.out.println("> ");
        didacticSector = input.nextLine().toUpperCase();
        while (!checkSectorInput(didacticSector, sectorsList)) {
            System.out.println("\nThe sector doesn't exist, insert a correct sector: >");
            didacticSector = input.nextLine().toUpperCase();
        }

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

    /**
     * Prende in input la lista delle Questions, chiede all' utente di inserire
     * i dati dello studente , popola l' oggetto Quiz con le Questions e i dati
     * dello studente e lo restituisce
     *
     * @param questions
     * @return Quiz
     */
    public Quiz composeQuizFromInput(List<Question> questions) {

        Student student = new Student();
        Quiz quiz = new Quiz();
        String idNumber;
        String studentName;
        String studentSurname;
        do {
            System.out.println("Enter the name of the student>");
            studentName = input.nextLine();
        } while (studentName == null || studentName.isEmpty());

        do {
            System.out.println("Enter the surname of the student>");
            studentSurname = input.nextLine();
        } while (studentSurname == null || studentSurname.isEmpty());

        System.out.println("Enter the idNumber of the Student>");
        idNumber = input.nextLine();
        while (idNumber == null || !idNumber.matches("[\\d]{6}")) {
            System.out.println("The idNumber is not compliant,enter idNumber of the Student >");
            idNumber = input.nextLine();
        }
        student.setIdNumber(idNumber);
        student.setName(studentName);
        student.setSurname(studentSurname);

        quiz.setSerialNumber("T-" + UUID.randomUUID().toString().substring(0, 7).toUpperCase());

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String now = df.format(Calendar.getInstance().getTime());

        quiz.setTimestamp(now);
        quiz.setLevel(questions.get(0).getLevel());
        quiz.setSector(questions.get(0).getSector());
        quiz.setStudent(student);

        for (Question question : questions) {
            quiz.getQuestionsList().add(question);
        }

        return quiz;
    }

    /**
     * Chiede all'utente di immettere il path ASSOLUTO dove salvare il file,
     * controlla se è un path valido tramite un' Espressione regolare, e
     * controlla se la directory esiste
     * @return 
     */
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
            System.out.println("Enter the ABSOLUTE EXISTING directory path where to save the File >");
            pathString = input.nextLine();
            if (isUnix()) {
                while (!pathString.matches("^['\"]?(?:/[^/]+)*['\"]?$")) {
                    System.out.println("The path inserted is not corret, enter the ABSOLUTE EXISTING path");
                    pathString = input.nextLine();
                }

            } else if (isWindows()) {
                //Not implemented yet
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

    /////////////////////////////////////////////// Utility interne /////////////////////////////////////////////////
    public boolean isWindows() {

        return (OS.indexOf("win") >= 0);

    }

    public boolean isUnix() {

        return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 || OS.indexOf("mac") >= 0);

    }

    private void printAllXmlFiles(Collection<String> sectorsList) {
        int i = 0;
        int totalFiles = sectorsList.size();
        if (!sectorsList.isEmpty()) {
            for (String sectorName : sectorsList) {
                System.out.println((i + 1) + ") " + sectorName);
                i++;
            }
            System.out.println("");
        } else {
            System.out.println("Sorry, there aren't question stored in repository !!!");
        }
    }

    /** Torna true se il SectorId dato in input esiste nella Collection
     * @param input
     * @param sectorsList
     * @return  */
    public boolean checkSectorInput(String input, Collection<String> sectorsList) {
        boolean correct = false;
        for (String sectorName : sectorsList) {
            if (input.equals(sectorName)) {
                correct = true;
            }
        }
        return correct;
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
