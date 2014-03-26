/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mwt.xml.services.impl;

import org.mwt.xml.exceptions.XMLQuizException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.mwt.xml.UserInputUtil;
import org.mwt.xml.engine.ComplianceReqCheck;
import org.mwt.xml.engine.QuestionsExtractor;
import org.mwt.xml.engine.QuizGenerator;
import org.mwt.xml.engine.util.PropertiesFactory;
import org.mwt.xml.engine.util.ValidatorUtil;
import org.mwt.xml.engine.util.XmlSTAXParserUtil;
import org.mwt.xml.exceptions.MyErrorHandler;
import org.mwt.xml.model.Answer;
import org.mwt.xml.model.DidacticSector;
import org.mwt.xml.model.Question;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.SAXException;

/**
 *
 * @author marco
 */
public class XMLQuizServiceBusiness {

    private static XMLQuizServiceBusiness instance = null;

    private final XmlSTAXParserUtil xmlParserUtil = XmlSTAXParserUtil.getIstance();

    private final PropertiesFactory propertiesFactory = PropertiesFactory.getInstance();

    private final Properties properties = propertiesFactory.getRepositoryPathProperties();

    private final QuestionsExtractor questionExtractor = QuestionsExtractor.getInstance();

    private final ComplianceReqCheck complianceRequest = ComplianceReqCheck.getInstance();

    private final UserInputUtil userInputUtil = UserInputUtil.getInstance();

    /*private Document loadQuestionBySector(String sectorID) {

     MyErrorHandler eh = new MyErrorHandler();
     Document d = null;
     try {
     DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
     dbf.setNamespaceAware(false); //dice al pareser se deve riconoscere i namespace 
     dbf.setValidating(false); //dice al parser di fare il controllo grammaticale rispetto all DTD
     dbf.setIgnoringElementContentWhitespace(true);//ignora gli spazi bianchi    
     dbf.setIgnoringComments(true);//ignora i commenti
     DocumentBuilder db = dbf.newDocumentBuilder();
     db.setErrorHandler(eh); //gli errori verranno passati al mio error handler
     d = db.parse(new File(this.properties.getProperty("questionPath") + "/" + sectorID + ".xml"));
     } catch (ParserConfigurationException ex) {
     Logger.getLogger(XMLQuizServiceBusiness.class.getName()).log(Level.SEVERE, null, ex);
     } catch (SAXException ex) {
     //gestita da MyErrorHandler
     } catch (IOException ex) {
     Logger.getLogger(XMLQuizServiceBusiness.class.getName()).log(Level.SEVERE, null, ex);
     }
     if (eh.hasErrors()) {
     System.out.println("NON valido");
     return null;
     } else {
     System.out.println("valido");
     return d;
     }

     }*/
    public boolean addQuestionsGroupToRepository(List<Question> questionsToADD) throws XMLQuizException {

        String sectorID = questionsToADD.get(0).getSector().getSectorID();
        List<String> allSectors = complianceRequest.listAllSectors();

        /**
         * Controlla se le questions da aggiungere fanno parte di un sectorID
         * che già esiste In quel caso estrae una lista di questions dal
         * documento che già esiste e forma una lista unione delle vecchie
         * questions e di quelle nuove da aggiungere
         */
        if (userInputUtil.checkSectorInput(sectorID, allSectors)) {
            File oldXmlFile = new File(properties.getProperty("questionPath") + "/" + sectorID + ".xml");
            List<Question> oldQuestions = questionExtractor.xPathExtractAllQuestions(oldXmlFile);
            questionsToADD.addAll(oldQuestions);
        }

        try {

            Document doc = createDocument();
            Element temp_el;
            Node rootElem;

            temp_el = doc.createElement("questionsGroup");
            temp_el.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", "http://it.univaq.mwt.xml/questionGroup");
            temp_el.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            temp_el.setAttribute("xsi:schemaLocation", "http://it.univaq.mwt.xml/questionGroup ../schema/questionGroupSchema.xsd");
            rootElem = doc.appendChild(temp_el);

            temp_el = doc.createElement("didacticSector");
            temp_el = fillDidacticSector(doc, temp_el, questionsToADD.get(0).getSector());
            rootElem.appendChild(temp_el);

            for (Question question : questionsToADD) {

                temp_el = doc.createElement("question");
                fillQuestion(doc, temp_el, question);
                rootElem.appendChild(temp_el);
            }

            File outputFile = new File(this.properties.getProperty("questionPath") + "/" + sectorID + ".xml");
            // saveDocument(doc, new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"));
            saveQuestionsGroup(doc, outputFile);
            return true;

        } catch (XMLQuizException ex) {
            Logger.getLogger(XMLQuizServiceBusiness.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public Document createDocument() {

        Document d = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true); //dice al pareser se deve riconoscere i namespace 
            dbf.setValidating(false); //dice al parser di fare il controllo grammaticale rispetto all DTD
            DocumentBuilder db = dbf.newDocumentBuilder();
            return db.newDocument();

        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XMLQuizServiceBusiness.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;

    }

    private Element fillQuestion(Document doc, Element root, Question question) {

        Element temp_el;

        temp_el = doc.createElement("level");
        temp_el.setTextContent(question.getLevel().toString());
        root.appendChild(temp_el);

        temp_el = doc.createElement("image");
        temp_el.setAttribute("href", question.getImgUri());
        root.appendChild(temp_el);

        temp_el = doc.createElement("questionText");
        temp_el.setTextContent(question.getQuestionText());
        root.appendChild(temp_el);

        temp_el = doc.createElement("AnswersList");
        temp_el = fillAnswersList(doc, temp_el, question.getAnswersList());
        root.appendChild(temp_el);

        return root;
    }

    private Element fillAnswersList(Document doc, Element root, List<Answer> answersList) {
        Element temp_el;
        int rightAnswerPosition = answersList.indexOf("rightAnswer");
        boolean rightAnswFound = false;
        for (int i = 0; i < answersList.size() && !rightAnswFound; i++) {
            if (answersList.get(i).isCorrect()) {
                rightAnswerPosition = i;
                rightAnswFound = true;
            }
        }
        Collections.swap(answersList, rightAnswerPosition, 0);

        for (Answer answer : answersList) {
            if (answer.isCorrect()) {
                temp_el = doc.createElement("rightAnswer");
                temp_el.setTextContent(answer.getAnswerText());
            } else {
                temp_el = doc.createElement("wrongAnswer");
                temp_el.setTextContent(answer.getAnswerText());
            }
            root.appendChild(temp_el);
        }

        return root;
    }

    private void saveQuestionsGroup(Document d, File xmlFile) throws XMLQuizException {
        FileWriter writer = null;
        try {
            writer = new FileWriter(xmlFile, false);
            DOMImplementation di = d.getImplementation();
            DOMImplementationLS dils = (DOMImplementationLS) di;
            LSSerializer lss = dils.createLSSerializer();
            LSOutput lso = dils.createLSOutput();
            lso.setCharacterStream(writer);
            lso.setEncoding("UTF-8");
            lss.getDomConfig().setParameter("format-pretty-print", true);
            lss.write(d, lso);
        } catch (IOException ex) {
            Logger.getLogger(XMLQuizServiceBusiness.class.getName()).log(Level.SEVERE, null, ex);
            List<XMLQuizException.ExceptionCause> exCauses = new ArrayList<>();
            exCauses.add(XMLQuizException.ExceptionCause.ERROR_ADD_QUESTIONS);
            throw new XMLQuizException(exCauses);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(XMLQuizServiceBusiness.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private Element fillDidacticSector(Document doc, Element root, DidacticSector sector) {
        Element temp_el;
        temp_el = doc.createElement("sectorId");
        temp_el.setTextContent(sector.getSectorID());
        root.appendChild(temp_el);
        temp_el = doc.createElement("description");
        temp_el.setTextContent(sector.getDescription());
        root.appendChild(temp_el);
        return root;
    }


    /*public DidacticSector extractDidacticSector(InputStream xmlQuestionInput) {

     DidacticSector sector = null;
     //File xmlQuestionFile = new File(this.properties.getProperty("questionPath") + "/" + didacticSector + ".xml");
     //File xmlQuestionSchemaFile = new File(this.properties.getProperty("questionSchemaPath"));
     InputStream xmlQuestionSchema = this.getClass().getResourceAsStream(this.properties.getProperty("questionSchemaPath"));
     XMLInputFactory xif = XMLInputFactory.newInstance();
     XMLStreamReader xsr = null;
     //FileReader fr = null;
     ValidatorUtil validator = new ValidatorUtil();
     if (validator.validateXML(xmlQuestionInput, xmlQuestionSchema)) {
     try {
     //fr = new FileReader(xmlQuestionFile);
     //xsr = xif.createXMLStreamReader(fr);
     xsr = xif.createXMLStreamReader(xmlQuestionInput);

     sector = xmlParserUtil.extractDidacticSectorStaxEventHandler(xsr);

     return sector;

     } catch (XMLStreamException ex) {
     //XMLStreamException è l'unico tipo di eccezione dello stream reader STAX
     // Logger.getLogger(XML_tests.class.getName()).log(Level.SEVERE, null, ex);
     System.out.println("Errore ["
     + ex.getLocation().getSystemId() + ":"
     + ex.getLocation().getLineNumber() + ","
     + ex.getLocation().getColumnNumber() + "] "
     + ex.getMessage());

     } finally {
     XmlSTAXParserUtil.closeStaxStreamReader(xsr, null);
     }

     }
     return null;
     }*/

    /*private Document createEmptyDocument() {

     Document d = null;
     try {
     DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
     dbf.setNamespaceAware(false); //dice al pareser se deve riconoscere i namespace 
     dbf.setValidating(false); //dice al parser di fare il controllo grammaticale rispetto all DTD
     DocumentBuilder db = dbf.newDocumentBuilder();

     return db.newDocument();

     } catch (ParserConfigurationException ex) {
     Logger.getLogger(QuizGenerator.class.getName()).log(Level.SEVERE, null, ex);
     }

     return null;
     }*/
    
    
    public static XMLQuizServiceBusiness getInstance() {
        if (instance == null) {
            instance = new XMLQuizServiceBusiness();
        }
        return instance;
    }

}
