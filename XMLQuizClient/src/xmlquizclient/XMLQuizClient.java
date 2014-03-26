/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlquizclient;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mwt.xmlquiz.soa.artifacts.FaultMessage;
import org.mwt.xmlquiz.soa.artifacts.QuestionXmlTextOrUri;
import org.mwt.xmlquiz.soa.artifacts.QuizBuilderList;

/**
 *
 * @author marco
 */
public class XMLQuizClient {

    private static final UserInputUtil userInputUtil = UserInputUtil.getInstance();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        String choise = userInputUtil.getInitialChoise();

        //Store Question
        if (Integer.parseInt(choise) == 1) {
            boolean success = false;
            QuestionXmlTextOrUri questionXmlTextOrUri = new QuestionXmlTextOrUri();
            String questionToStore = userInputUtil.getStoreQuestionInput();
            Object input = getFromUrl(questionToStore);

            if (input instanceof InputStream) {
                questionXmlTextOrUri.setQuestionXmlUri(questionToStore);
            } else if (input instanceof String) {
                questionXmlTextOrUri.setQuestionXmlText(questionToStore);
            }
            try {
                success = storeQuestion(questionXmlTextOrUri);
            } catch (FaultMessage ex) {
                Iterator<String> iterator = ex.getFaultInfo().getFaultDescription().iterator();
                while (iterator.hasNext()) {
                    System.err.println(iterator.next());
                }
            }
            if (success) {
                System.out.println("\n================= QUESTIONS STORED !!!!! =======================\n");
            }
        }
        //Create Quiz
        if (Integer.parseInt(choise) == 2) {
            QuizBuilderList quizDefinition = new QuizBuilderList();
            quizDefinition.setSector(userInputUtil.getDidacticSectorInput());
            quizDefinition.setLevel(userInputUtil.getDifficultLevelInput().intValue());
            quizDefinition.setQuestionsNumber(userInputUtil.getQuestionsNumbInput().intValue());
            quizDefinition.setStudentName(userInputUtil.getStudentNameInput());
            quizDefinition.setStudentSurname(userInputUtil.getStudentSurnameInput());
            quizDefinition.setStudentIDNumber(userInputUtil.getStudentIDInput());
            try {
                String quiz = createQuiz(quizDefinition);
                System.out.println("==========  STAMPO IL QUIZ ============\n");
                System.out.println(quiz);
            } catch (FaultMessage ex) {
                // Logger.getLogger(XMLQuizClient.class.getName()).log(Level.SEVERE, null, ex);
                Iterator<String> iterator = ex.getFaultInfo().getFaultDescription().iterator();
                while (iterator.hasNext()) {
                    System.err.println(iterator.next());
                }

            }
        }
        //Format Quiz
        if (Integer.parseInt(choise) == 3) {
            try {
                String quizToFormat = userInputUtil.getStoreQuestionInput();
                String outputPdfPath = userInputUtil.getPathToSaveQuiz();
                byte[] PDFbytes = formatQuiz(quizToFormat);
                try (FileOutputStream outStream = new FileOutputStream(outputPdfPath + ".pdf")) {
                    outStream.write(PDFbytes);
                }
                System.out.println("\n=============PDF Stored !!!!============\n");
            } catch (FaultMessage ex) {
                Iterator<String> iterator = ex.getFaultInfo().getFaultDescription().iterator();
                while (iterator.hasNext()) {
                    System.err.println(iterator.next());
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(XMLQuizClient.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(XMLQuizClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    /**
     * Prende in input una stringa e con questa prova a costruire un oggetto
     * URL, se riesce a costruirlo ritorna un Object istanza di un InputStream,
     * altrimenti si avrà una MalformedURLException e il metodo tornera in
     * output un Object che è la stessa stringa che ha avuto in input
     */
    private static Object getFromUrl(String url) {
        try {
            URL xmlUrl = new URL(url);
            InputStream inputStream = xmlUrl.openConnection().getInputStream();
            return inputStream;
        } catch (MalformedURLException ex) {

        } catch (IOException ex) {
            Logger.getLogger(XMLQuizClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return url;
    }

    private static String createQuiz(org.mwt.xmlquiz.soa.artifacts.QuizBuilderList quizDefinition) throws FaultMessage {
        org.mwt.xmlquiz.soa.artifacts.XMLQuizService service = new org.mwt.xmlquiz.soa.artifacts.XMLQuizService();
        org.mwt.xmlquiz.soa.artifacts.XMLQuiz port = service.getXMLQuizSOAPPort();
        return port.createQuiz(quizDefinition);
    }

    private static byte[] formatQuiz(java.lang.String quizInput) throws FaultMessage {
        org.mwt.xmlquiz.soa.artifacts.XMLQuizService service = new org.mwt.xmlquiz.soa.artifacts.XMLQuizService();
        org.mwt.xmlquiz.soa.artifacts.XMLQuiz port = service.getXMLQuizSOAPPort();
        return port.formatQuiz(quizInput);
    }

    private static boolean storeQuestion(org.mwt.xmlquiz.soa.artifacts.QuestionXmlTextOrUri xmlQuestion) throws FaultMessage {
        org.mwt.xmlquiz.soa.artifacts.XMLQuizService service = new org.mwt.xmlquiz.soa.artifacts.XMLQuizService();
        org.mwt.xmlquiz.soa.artifacts.XMLQuiz port = service.getXMLQuizSOAPPort();
        return port.storeQuestion(xmlQuestion);
    }

}
