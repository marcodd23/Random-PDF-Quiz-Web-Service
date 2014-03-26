/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mwt.xml.services;

import java.io.BufferedReader;
import org.mwt.xml.exceptions.XMLQuizException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mwt.xml.UserInputUtil;
//import org.mwt.xml.engine.ComplianceReqCheckInterface;
//import org.mwt.xml.engine.QuizGeneratorInterface;
//import org.mwt.xml.engine.QuizTransformerInterface;
import org.mwt.xml.engine.ComplianceReqCheck;
import org.mwt.xml.services.impl.XMLQuizServiceBusiness;
import org.mwt.xml.engine.QuestionsExtractor;
import org.mwt.xml.engine.QuizGenerator;
import org.mwt.xml.engine.QuizTransformer;
import org.mwt.xml.engine.util.PropertiesFactory;
import org.mwt.xml.engine.util.ValidatorUtil;
import org.mwt.xml.exceptions.XMLQuizException.ExceptionCause;
import org.mwt.xml.model.Question;
import org.mwt.xml.model.Quiz;
import org.mwt.xml.model.Student;
import org.w3c.dom.Document;

/**
 *
 * @author marco
 */
/**
 * Questa classe è l' interfaccia che contiene i metodi per il WebService
 */
public class XMLQuizService {

    private static XMLQuizService instance = null;

    private final XMLQuizServiceBusiness questionHandler = XMLQuizServiceBusiness.getInstance();
    private final QuestionsExtractor questionExtractor = QuestionsExtractor.getInstance();
    private final QuizTransformer quizTransformer = QuizTransformer.getInstance();
    private final QuizGenerator quizGenerator = QuizGenerator.getInstance();
    private final UserInputUtil userInputUtil = UserInputUtil.getInstance();
    private final ValidatorUtil validatorUtil = ValidatorUtil.getInstance();
    private final PropertiesFactory propertiesFactory = PropertiesFactory.getInstance();
    private final Properties properties = propertiesFactory.getRepositoryPathProperties();
    private final ComplianceReqCheck complianceRequest = ComplianceReqCheck.getInstance();

    /**
     * Questo metodo prende in input o una stringa che rappresenta il file xml
     * delle Questions da aggiungere, oppure una url da cui scaricare il file
     * xml
     *
     * @param questionString
     * @param url
     * @return
     * @throws org.mwt.xml.exceptions.XMLQuizException
     */
    public boolean storeQuestion(String questionString, String url) throws XMLQuizException {
        List<Question> questionsToADD = null;
        boolean added = false;

        if (url != null) {
            try {
                URL xmlUrl = new URL(url);
                //Creo un reader che va a leggere dall'inputstream generato dalla connessione aperta verso la url
                BufferedReader reader = new BufferedReader(new InputStreamReader(xmlUrl.openStream()));
                String input = "";
                //leggo il documento xml e lo metto in una string
                while (reader.ready()) {
                    input += reader.readLine();
                }
                reader.close();
                //Trasformo l'array letto in un array di byte e creo un Inputstream
                InputStream stringXMLInputStream = new ByteArrayInputStream(input.getBytes());
                questionsToADD = questionExtractor.xPathExtractAllQuestions(stringXMLInputStream);
                added = questionHandler.addQuestionsGroupToRepository(questionsToADD);

            } catch (MalformedURLException ex) {
                Logger.getLogger(XMLQuizService.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(XMLQuizService.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else if (questionString != null) {

            //Trasformo l'array letto in un array di byte e creo un Inputstream
            InputStream stringXMLInputStream = new ByteArrayInputStream(questionString.getBytes());
            questionsToADD = questionExtractor.xPathExtractAllQuestions(stringXMLInputStream);
            added = questionHandler.addQuestionsGroupToRepository(questionsToADD);
        }

        return added;
    }

    public String createQuiz(CreateQuizInputWrapper input) throws XMLQuizException {

        String sectorID = input.getSectorID();
        Integer difficultLevel = input.getDifficultLevel();
        Integer questionsNumber = input.getQuestionsNumber();
        String studentName = input.getStudentName();
        String studentSurname = input.getStudentSurname();
        String studentID = input.getStudentID();

        Student student = new Student(studentName, studentSurname, studentID);

        List<String> allSectors = complianceRequest.listAllSectors();

        //controllo se ci sono eccezioni causate dagli input e le aggiungo alla classe XMLQuizException
        List<ExceptionCause> exCauses = new ArrayList<>();
        if (difficultLevel < 1 || difficultLevel > 5) {
            exCauses.add(XMLQuizException.ExceptionCause.SECTORID_NOT_EXIST);
        }
        if (!userInputUtil.checkSectorInput(sectorID, allSectors)) {
            exCauses.add(XMLQuizException.ExceptionCause.SECTORID_NOT_EXIST);

        } else if (!complianceRequest.checkRequestCompliance(sectorID, difficultLevel, questionsNumber)) {
            exCauses.add(XMLQuizException.ExceptionCause.NOT_ENOUGH_QUESTIONS);
        }
        //Se ci sono eccezioni lancio una XMLQuizException ed esco
        if (!exCauses.isEmpty()) {
            throw new XMLQuizException(exCauses);
        }

        List<Question> questionsExtracted = questionExtractor.xPathExtractAllQuestionsByLevel(sectorID, difficultLevel, questionsNumber);
        Quiz quiz = composeQuiz(questionsExtracted, student);
        Document quizDOM = quizGenerator.createQuiz(quiz);
        // Writer wr = new StringWriter();
        return quizGenerator.saveDocumentToString(quizDOM);

    }

    public byte[] formatTest(String xmlInput) throws XMLQuizException {
  
        InputStream xmlQuestionSchema = this.getClass().getResourceAsStream(this.properties.getProperty("quizSchemaPath"));
        InputStream xmlInputStream = new ByteArrayInputStream(xmlInput.getBytes());

        if (validatorUtil.validateXML(xmlInputStream, xmlQuestionSchema)) {

            ByteArrayOutputStream PDF;
            try {
                PDF = quizTransformer.quizToPDF(xmlInputStream);
                return PDF.toByteArray();
            } catch (IOException ex) {
                Logger.getLogger(XMLQuizService.class.getName()).log(Level.SEVERE, null, ex);
                throw new XMLQuizException(XMLQuizException.ExceptionCause.NOT_VALID);
            }

        } else {
            List<XMLQuizException.ExceptionCause> exCauses = new ArrayList<>();
            exCauses.add(XMLQuizException.ExceptionCause.NOT_VALID);
            throw new XMLQuizException(exCauses);
        }

    }

    /* private Object getFromUrl(String url) {
     try {
     URL xmlUrl = new URL(url);
     InputStream inputStream = xmlUrl.openConnection().getInputStream();
     return inputStream;
     } catch (MalformedURLException ex) {
     Logger.getLogger(XMLQuizService.class.getName()).log(Level.SEVERE, null, ex);
     } catch (IOException ex) {
     Logger.getLogger(XMLQuizService.class.getName()).log(Level.SEVERE, null, ex);
     }
     return url;
     } */
    private Quiz composeQuiz(List<Question> questions, Student student) throws XMLQuizException {

        Quiz quiz = new Quiz();

        if (!student.getIdNumber().matches("[\\d]{0,6}")) {

            List<ExceptionCause> causes = new ArrayList<>();
            causes.add(XMLQuizException.ExceptionCause.STUDENT_ID_NOT_VALID);
            throw new XMLQuizException(causes);
        }

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

    public static XMLQuizService getInstance() {

        if (instance == null) {
            instance = new XMLQuizService();
        }
        return instance;
    }

}
