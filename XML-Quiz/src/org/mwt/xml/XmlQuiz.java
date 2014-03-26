package org.mwt.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
//import org.mwt.xml.engine.ComplianceReqCheckInterface;
//import org.mwt.xml.engine.QuizGeneratorInterface;
//import org.mwt.xml.engine.QuizTransformerInterface;
import org.mwt.xml.engine.ComplianceReqCheck;
import org.mwt.xml.engine.QuestionsExtractor;
import org.mwt.xml.engine.QuizGenerator;
import org.mwt.xml.engine.QuizTransformer;
import org.mwt.xml.model.Question;
import org.mwt.xml.model.Quiz;
import org.w3c.dom.Document;

/**
 *
 * @author marco
 */
public class XmlQuiz {
    
    

    public XmlQuiz() {

    }

    public static void main(String[] args) {

        boolean requestFulfilled = false;
        String bufferNumberInput;
        String exitParameter;
        String didacticSector;
        Integer level;
        Integer questionNumber;
        XmlQuiz xmlQuizPlus = new XmlQuiz();
        final ComplianceReqCheck complianceRequest = ComplianceReqCheck.getInstance();
        final QuestionsExtractor questionExtractor = QuestionsExtractor.getInstance();
        final UserInputUtil userInputUtil = UserInputUtil.getInstance();
        final QuizTransformer quizTransform = QuizTransformer.getInstance();
        final QuizGenerator quizGen = QuizGenerator.getInstance();

        System.out.println("============================= XML QUIZ ================================= \n");
        while (!requestFulfilled) {

            ////////////  INPUT SETTORE DIDATTICO
            didacticSector = userInputUtil.getDidacticSectorInput();

            //////////// SECONDO INPUT DIFFICULT LEVEL
            level = userInputUtil.getDifficultLevelInput();

            ///////////// TERZO INPUT NUMBER OF QUESTION IN A QUIZ
            questionNumber = userInputUtil.getQuestionsNumbInput();

            if (complianceRequest.checkRequestCompliance(didacticSector, level, questionNumber)) {
                System.out.println("\nOK!! It's possible to fulfill the request !!!\n");
                
                List<Question> questions = questionExtractor.xPathExtractAllQuestionsByLevel(didacticSector, level, questionNumber);

                Quiz quiz = userInputUtil.composeQuizFromInput(questions);
                
                String outputFileName = userInputUtil.getPathToSaveQuiz();

                Document doc = quizGen.createQuiz(quiz);
                
               
                System.out.println("//////////////////////////// DOC GENERATO ////////////////////////////////////\n");
                
                
                //Genero il PDF
                try {
                    
                    //File fileQuizXml = new File(outputFileName + ".xml");
                    File fileQuizPDF = new File(outputFileName + ".pdf");
                  
                    //L'outputstream per salvare i byte del file pdf
                    ByteArrayOutputStream outQuizXml = new ByteArrayOutputStream();
                    
                    //Scrivo in memoria il Quiz generato sotto forma di ByteArrayOutputStream
                    quizGen.saveDocument(doc,  new OutputStreamWriter(outQuizXml, "UTF-8"));     
                    //quizGen.saveDocument(doc, new OutputStreamWriter(new FileOutputStream(fileQuizXml), "UTF-8"));
                     
                    //Converto in pdf e salvo in un OutputStram di Byte in memoria
                    ByteArrayOutputStream pdfByteStream = quizTransform.quizToPDF(new ByteArrayInputStream(outQuizXml.toByteArray()));
                
                    
                    //Salvo il pdf sul disco 
                    pdfByteStream.writeTo(new FileOutputStream(fileQuizPDF));
                    

                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(XmlQuiz.class.getName()).log(Level.SEVERE, null, ex);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(XmlQuiz.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(XmlQuiz.class.getName()).log(Level.SEVERE, null, ex);
                }

                requestFulfilled = true;
            } else {

                requestFulfilled = userInputUtil.getExitInput();
            }
            

        }
        userInputUtil.closeInput();

    }

    
    
}
