/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mwt.xml.engine;

import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
//import org.mwt.xml.engine.QuizGeneratorInterface;
import org.mwt.xml.model.Answer;
import org.mwt.xml.model.DidacticSector;
import org.mwt.xml.model.Question;
import org.mwt.xml.model.Quiz;
import org.mwt.xml.model.Student;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

/**
 *
 * @author marco
 */
public class QuizGenerator {

    private static QuizGenerator instance = null;

    /**
     * Prende in input l'oggetto Quiz e costruisce il documento con i metodi del DOM
     * @param quiz
     * @return Document
     */
    public Document createQuiz(Quiz quiz) {

        Element temp_el;
        Node rootElem;
        Document doc = createEmptyDocument();

        temp_el = doc.createElement("quiz");
        temp_el.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", "http://it.univaq.mwt.xml/quiz");
        temp_el.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        temp_el.setAttribute("xsi:schemaLocation", "http://it.univaq.mwt.xml/quiz ../schema/quizSchema.xsd");
        rootElem = doc.appendChild(temp_el);

        temp_el = doc.createElement("serialNumber");
        temp_el.setTextContent(quiz.getSerialNumber());
        rootElem.appendChild(temp_el);

        temp_el = doc.createElement("timestamp");
        temp_el.setTextContent(quiz.getTimestamp());
        rootElem.appendChild(temp_el);

        temp_el = doc.createElement("didacticSector");
        temp_el = fillDidacticSector(doc, temp_el, quiz.getSector());
        rootElem.appendChild(temp_el);

        temp_el = doc.createElement("level");
        temp_el.setTextContent(quiz.getLevel().toString());
        rootElem.appendChild(temp_el);

        temp_el = doc.createElement("student");
        temp_el = fillStudent(doc, temp_el, quiz.getStudent());
        rootElem.appendChild(temp_el);

        temp_el = doc.createElement("questionsList");
        temp_el = fillQuestionsList(doc, temp_el, quiz.getQuestionsList());
        rootElem.appendChild(temp_el);

        return doc;

    }

    private Document createEmptyDocument() {

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

    private Element fillStudent(Document doc, Element root, Student student) {
        Element temp_el;
        temp_el = doc.createElement("name");
        temp_el.setTextContent(student.getName());
        root.appendChild(temp_el);
        temp_el = doc.createElement("surname");
        temp_el.setTextContent(student.getSurname());
        root.appendChild(temp_el);
        temp_el = doc.createElement("idNumber");
        temp_el.setTextContent(student.getIdNumber());
        root.appendChild(temp_el);
        return root;
    }

    private Element fillQuestionsList(Document doc, Element root, List<Question> questionList) {

        Element temp_el;
        for (Question question : questionList) {
            temp_el = doc.createElement("question");
            temp_el = fillQuestion(doc, temp_el, question);
            root.appendChild(temp_el);
        }
        return root;
    }

    private Element fillQuestion(Document doc, Element root, Question question) {

        Element temp_el;

        temp_el = doc.createElement("image");
        temp_el.setAttribute("href", question.getImgUri());
        root.appendChild(temp_el);

        temp_el = doc.createElement("text");
        temp_el.setTextContent(question.getQuestionText());
        root.appendChild(temp_el);

        temp_el = doc.createElement("optionsList");
        temp_el = fillOptionsList(doc, temp_el, question.getAnswersList());
        root.appendChild(temp_el);

        return root;
    }

    private Element fillOptionsList(Document doc, Element root, List<Answer> answersList) {
        Element temp_el;

        for (Answer answer : answersList) {
            if (answer.isCorrect()) {
                temp_el = doc.createElement("option");
                temp_el.setAttribute("trueAnswer", "true");
                temp_el.setTextContent(answer.getAnswerText());
            } else {

                temp_el = doc.createElement("option");
                temp_el.setAttribute("trueAnswer", "false");
                temp_el.setTextContent(answer.getAnswerText());
            }
            root.appendChild(temp_el);
        }

        return root;
    }

    public void saveDocument(Document d, Writer w) {
        DOMImplementation di = d.getImplementation();
        DOMImplementationLS dils = (DOMImplementationLS) di;

        LSSerializer lss = dils.createLSSerializer();
        LSOutput lso = dils.createLSOutput();
        lso.setCharacterStream(w);
        lso.setEncoding("UTF-8");
        lss.getDomConfig().setParameter("format-pretty-print", true);
        lss.write(d, lso);
    }

    public String saveDocumentToString(Document d) {
        DOMImplementation di = d.getImplementation();
        DOMImplementationLS dils = (DOMImplementationLS) di;
        LSSerializer lss = dils.createLSSerializer();
        lss.getDomConfig().setParameter("format-pretty-print", true);
        return lss.writeToString(d);
    }

    ////////////////////////////////////////// con Stax ///////////////////////////////////////////////
    public XMLStreamWriter StAXcreateDocument(Quiz quiz) {
        XMLOutputFactory xof = XMLOutputFactory.newInstance();
        XMLStreamWriter xsw = null;
        try {
            xsw = xof.createXMLStreamWriter(new OutputStreamWriter(System.out, "UTF-8"));
            xsw.writeStartDocument("UTF-8", "1.0");

            xsw.writeStartElement("quiz");
            xsw.writeDefaultNamespace("http://it.univaq.mwt.xml/quiz");
            xsw.writeNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
            xsw.writeNamespace("schemaLocation", "http://it.univaq.mwt.xml/quiz ../schema/quizSchema.xsd");

            StAXfillDocument(xsw, quiz);

            return xsw;
        } catch (XMLStreamException ex) {
            Logger.getLogger(QuizGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(QuizGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (xsw != null) {
            try {
                xsw.close();
            } catch (XMLStreamException ex) {
                //
            }
        }
        return null;
    }

    private void StAXfillDocument(XMLStreamWriter xsw, Quiz quiz) {
        try {

            xsw.writeStartElement("serialNumber");
            xsw.writeCharacters(quiz.getSerialNumber());
            xsw.writeEndElement();

            xsw.writeStartElement("timestamp");
            xsw.writeCharacters(quiz.getTimestamp());
            xsw.writeEndElement();

            xsw.writeStartElement("didacticSector");
            xsw.writeStartElement("sectorId");
            xsw.writeCharacters(quiz.getSector().getSectorID());
            xsw.writeEndElement();
            xsw.writeStartElement(quiz.getSector().getDescription());
            xsw.writeEndElement();
            xsw.writeEndDocument();

            xsw.writeStartElement("level");
            xsw.writeCharacters(quiz.getLevel().toString());
            xsw.writeEndElement();

            xsw.writeStartElement("student");
            xsw.writeStartElement("name");
            xsw.writeCharacters(quiz.getStudent().getName());
            xsw.writeEndElement();
            xsw.writeStartElement("surname");
            xsw.writeCharacters(quiz.getStudent().getSurname());
            xsw.writeEndElement();
            xsw.writeStartElement("idNumber");
            xsw.writeCharacters(quiz.getStudent().getIdNumber());
            xsw.writeEndElement();
            xsw.writeEndElement();

            xsw.writeStartElement("questionsList");
            for (Question question : quiz.getQuestionsList()) {

                xsw.writeStartElement("question");
                xsw.writeStartElement("image");
                xsw.writeAttribute("href", question.getImgUri());
                xsw.writeEndElement();
                xsw.writeStartElement("text");
                xsw.writeCharacters(question.getQuestionText());
                xsw.writeEndElement();
                xsw.writeStartElement("optionsList");
                for (Answer answer : question.getAnswersList()) {

                    xsw.writeStartElement("option");
                    if (answer.isCorrect()) {
                        xsw.writeAttribute("trueAnswer", "true");
                    } else {
                        xsw.writeAttribute("trueAnswer", "false");
                    }
                    xsw.writeCharacters(answer.getAnswerText());
                    xsw.writeEndElement();
                }
                xsw.writeEndElement();
            }
            xsw.writeEndElement();

            xsw.writeEndElement();

            xsw.writeEndDocument();

        } catch (XMLStreamException ex) {
            Logger.getLogger(QuizGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static QuizGenerator getInstance() {

        if (instance == null) {
            instance = new QuizGenerator();
        }
        return instance;
    }

}
