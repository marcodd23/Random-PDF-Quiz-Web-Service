/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soa.mwt.univaq.it.xmlquizservice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import org.mwt.xml.exceptions.XMLQuizException;
import org.mwt.xml.services.CreateQuizInputWrapper;
import org.mwt.xml.services.XMLQuizService;

/**
 *
 * @author marco
 */
@WebService(serviceName = "XMLQuizService", portName = "XMLQuizSOAPPort", endpointInterface = "soa.mwt.univaq.it.xmlquizservice.XMLQuiz", targetNamespace = "http://it.univaq.mwt.soa/XMLQuizService", wsdlLocation = "WEB-INF/wsdl/WSDL_Adapter/XMLQuizService.wsdl")
public class WSDL_Adapter {

    private final XMLQuizService service = XMLQuizService.getInstance();

    public boolean storeQuestion(soa.mwt.univaq.it.xmlquizservice.QuestionXmlTextOrUri xmlQuestion) throws FaultMessage {
        boolean success = false;
        try {
            if (xmlQuestion.getQuestionXmlText() != null) {
                success = service.storeQuestion(xmlQuestion.getQuestionXmlText(), null);
            } else if (xmlQuestion.getQuestionXmlUri() != null) {
                success = service.storeQuestion(null, xmlQuestion.getQuestionXmlUri());
            }
            return success;
        } catch (XMLQuizException ex) {
            FaultType ft = new FaultType();
            for (int i = 0; i < ex.getExceptionCauseList().size(); i++) {
                ft.getFaultDescription().add(ex.getExceptionCauseList().get(i).toString());
            }

            throw new FaultMessage("Errore storeQuestion", ft);
        }
    }

    public java.lang.String createQuiz(soa.mwt.univaq.it.xmlquizservice.QuizBuilderList quizDefinition) throws FaultMessage {

        
        CreateQuizInputWrapper input =  new CreateQuizInputWrapper();
        input.setSectorID(quizDefinition.getSector());
        input.setDifficultLevel(quizDefinition.getLevel());
        input.setQuestionsNumber(quizDefinition.getQuestionsNumber());
        input.setStudentName(quizDefinition.getStudentName());
        input.setStudentSurname(quizDefinition.getStudentSurname());
        input.setStudentID(quizDefinition.getStudentIDNumber());

        try {
            return service.createQuiz(input);
        } catch (XMLQuizException ex) {
            FaultType ft = new FaultType();
            for (int i = 0; i < ex.getExceptionCauseList().size(); i++) {
                ft.getFaultDescription().add(ex.getExceptionCauseList().get(i).toString());
            }

            throw new FaultMessage("Errore CreateQuiz", ft);
        }
    }

    public byte[] formatQuiz(java.lang.String quizInput) throws FaultMessage {
        try {
            return service.formatTest(quizInput);
        } catch (XMLQuizException ex) {
            FaultType ft = new FaultType();
            for (int i = 0; i < ex.getExceptionCauseList().size(); i++) {
                ft.getFaultDescription().add(ex.getExceptionCauseList().get(i).toString());
            }

            throw new FaultMessage("Errore formatQuiz", ft);
        }
    }

}
