package org.mwt.xml.engine;

import org.mwt.xml.exceptions.XMLQuizException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.xpath.XPathConstants;
//import org.mwt.xml.engine.QuestionsExtractorInterface;
import org.mwt.xml.engine.util.PropertiesFactory;
import org.mwt.xml.engine.util.QueryXmlUtil;
import org.mwt.xml.engine.util.ValidatorUtil;
import org.mwt.xml.model.Answer;
import org.mwt.xml.model.DidacticSector;
import org.mwt.xml.model.Question;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sun.swing.SwingUtilities2;

/**
 *
 * @author marco
 */
public class QuestionsExtractor {

    private static QuestionsExtractor instance = null;

    private final PropertiesFactory propertiesFactory = PropertiesFactory.getInstance();

    private final Properties properties = propertiesFactory.getRepositoryPathProperties();

    private final ValidatorUtil validatorUtil = ValidatorUtil.getInstance();

    private QuestionsExtractor() {

    }

    /**
     * Estraggo con XPATH tutte le Questions che hanno un determinato livello di
     * difficoltà
     *
     * @param didacticSector
     * @param difficultLevel
     * @param questionsNumber
     * @return List of Questions
     */
    public List<Question> xPathExtractAllQuestionsByLevel(String didacticSector,
            Integer difficultLevel, Integer questionsNumber) {

        List<Question> questionsList = new ArrayList<>();
        String questNsUri = properties.getProperty("questionsNamespace");
        System.out.println("Il Namespace è : " + questNsUri);
        String[] nsPrefix = {"prefix", questNsUri};
        QueryXmlUtil queryXmlUtil = QueryXmlUtil.getInstance();
        String queryToGetQuestions = "/" + nsPrefix[0] + ":questionsGroup/" + nsPrefix[0] + ":question[" + nsPrefix[0] + ":level=" + difficultLevel + "]";
        String queryToGetDidacticSector = "/" + nsPrefix[0] + ":questionsGroup/" + nsPrefix[0] + ":didacticSector";

        System.out.println("La query XPAth è : " + queryToGetQuestions);

        File xmlQuestionFile = new File(this.properties.getProperty("questionPath") + "/" + didacticSector + ".xml");

        Object didacticSectorResult = queryXmlUtil.queryDocument(xmlQuestionFile, queryToGetDidacticSector, nsPrefix);

        Object questionsResult = queryXmlUtil.queryDocument(xmlQuestionFile, queryToGetQuestions, nsPrefix);

        NodeList nodeListQuestions = (NodeList) questionsResult;
        NodeList nodeDidacticSector = (NodeList) didacticSectorResult;
        questionsList = extractNRandomQuestionsList(nodeListToQuestionList(nodeListQuestions, (Element) nodeDidacticSector.item(0)), questionsNumber);

        //printQuestions(questionsList);
        return questionsList;

    }

    /**
     * Estrae tutte le question dal documento in input, inputFile deve essere di
     * tipo File o InputStream
     *
     * @param inputFile di tipo File o InputStream
     * @return
     * @throws org.mwt.xml.exceptions.XMLQuizException
     */
    public List<Question> xPathExtractAllQuestions(Object inputFile) throws XMLQuizException {

        InputStream xmlQuestionSchema = this.getClass().getResourceAsStream(this.properties.getProperty("questionSchemaPath"));

        if (validatorUtil.validateXML(inputFile, xmlQuestionSchema)) {
            List<Question> questionsList = new ArrayList<>();
            String questNsUri = properties.getProperty("questionsNamespace");
            String[] ns = {"prefix", questNsUri};
            QueryXmlUtil queryXmlUtil = QueryXmlUtil.getInstance();
            String queryToGetQuestions = "/" + ns[0] + ":questionsGroup/" + ns[0] + ":question";
            String queryToGetDidacticSector = "/" + ns[0] + ":questionsGroup/" + ns[0] + ":didacticSector";

            System.out.println("La query XPAth è : " + queryToGetQuestions);
            System.out.println("La query XPAth è : " + queryToGetDidacticSector);

            Object didacticSectorResult = queryXmlUtil.queryDocument(inputFile, queryToGetDidacticSector, ns);

            Object questionsResult = queryXmlUtil.queryDocument(inputFile, queryToGetQuestions, ns);

            NodeList nodeListQuestions = (NodeList) questionsResult;
            NodeList nodeDidacticSector = (NodeList) didacticSectorResult;
            questionsList = nodeListToQuestionList(nodeListQuestions, (Element) nodeDidacticSector.item(0));

            //printQuestions(questionsList);
            return questionsList;

        } else {
            List<XMLQuizException.ExceptionCause> exCauses = new ArrayList<>();
            exCauses.add(XMLQuizException.ExceptionCause.NOT_VALID);
            throw new XMLQuizException(exCauses);
        }
    }

    /*
     public List<Question> xPathExtractAllQuestions(InputStream inputFile) throws XMLQuizException {

     InputStream xmlQuestionSchema = this.getClass().getResourceAsStream(this.properties.getProperty("questionSchemaPath"));

     if (validatorUtil.validateXML(inputFile, xmlQuestionSchema)) {
     List<Question> questionsList = new ArrayList<>();
     String questNsUri = properties.getProperty("questionsNamespace");
     String[] ns = {"prefix", questNsUri};
     QueryXmlUtil queryXmlUtil = QueryXmlUtil.getInstance();
     String queryToGetQuestions = "/" + ns[0] + ":questionsGroup/" + ns[0] + ":question";
     String queryToGetDidacticSector = "/" + ns[0] + ":questionsGroup/" + ns[0] + ":didacticSector";

     System.out.println("La query XPAth è : " + queryToGetQuestions);
     System.out.println("La query XPAth è : " + queryToGetDidacticSector);

     Object didacticSectorResult = queryXmlUtil.queryDocument(inputFile, queryToGetDidacticSector, ns);

     Object questionsResult = queryXmlUtil.queryDocument(inputFile, queryToGetQuestions, ns);

     NodeList nodeListQuestions = (NodeList) questionsResult;
     NodeList nodeDidacticSector = (NodeList) didacticSectorResult;
     questionsList = nodeListToQuestionList(nodeListQuestions, (Element) nodeDidacticSector.item(0));

     printQuestions(questionsList);

     try {
     inputFile.close();
     } catch (IOException ex) {
     Logger.getLogger(QuestionsExtractor.class.getName()).log(Level.SEVERE, null, ex);
     }

     return questionsList;

     } else {
     List<XMLQuizException.ExceptionCause> exCauses = new ArrayList<>();
     exCauses.add(XMLQuizException.ExceptionCause.NOT_VALID);
     throw new XMLQuizException(exCauses);
     }
     }
    
     */
    private List<Question> nodeListToQuestionList(NodeList nodeListQuestions, Element nodeDidacticSector) {

        //long time1 = System.currentTimeMillis();
        List<Question> questionsList = new ArrayList<>();
        DidacticSector didacticSector = new DidacticSector();
        didacticSector.setSectorID(nodeDidacticSector.getElementsByTagName("sectorId").item(0).getTextContent());
        didacticSector.setDescription(nodeDidacticSector.getElementsByTagName("description").item(0).getTextContent());

        for (int i = 0; i < nodeListQuestions.getLength(); i++) {

            List<Answer> answers = new ArrayList<>();
            Question question = new Question();

            question.setSector(didacticSector);

            Element e = (Element) nodeListQuestions.item(i);
            question.setLevel(Integer.parseInt(e.getElementsByTagName("level").item(0).getTextContent()));
            question.setImgUri(e.getElementsByTagName("image").item(0).getAttributes().getNamedItem("href").getTextContent());
            question.setQuestionText(e.getElementsByTagName("questionText").item(0).getTextContent());

            Element answersList = (Element) e.getElementsByTagName("AnswersList").item(0);

            Answer bufferedAnswer = new Answer();
            bufferedAnswer.setCorrect(true);
            bufferedAnswer.setAnswerText(answersList.getElementsByTagName("rightAnswer").item(0).getTextContent());
            answers.add(bufferedAnswer);

            NodeList answerListNode = answersList.getElementsByTagName("wrongAnswer");
            for (int j = 0; j < answerListNode.getLength(); j++) {

                bufferedAnswer = new Answer();
                bufferedAnswer.setCorrect(false);
                bufferedAnswer.setAnswerText(answerListNode.item(j).getTextContent());
                answers.add(bufferedAnswer);
            }

            question.setAnswersList(extractRandomAnswersList(answers));
            questionsList.add(question);

        }

        //long time2 = System.currentTimeMillis() - time1;
        //System.out.println("TIME FILL QUESTIONLIST WITH DOM: " + time2);
        return questionsList;
    }

    private List<Question> extractNRandomQuestionsList(List<Question> questionList, Integer questionsNumber) {

        Collections.shuffle(questionList);
        return questionList.subList(0, questionsNumber);

    }

    private List<Answer> extractRandomAnswersList(List<Answer> answers) {
        Answer rightAnswer = answers.get(0);
        List<Answer> tempList = new ArrayList<>();
        for (int i = 1; i < answers.size(); i++) {
            tempList.add(answers.get(i));
        }
        Collections.shuffle(tempList);
        answers = new ArrayList<>();
        tempList = tempList.subList(0, 2);
        answers.add(rightAnswer);
        for (int i = 0; i < tempList.size(); i++) {
            answers.add(tempList.get(i));
        }
        Collections.shuffle(answers);
        return answers;
    }

    //Da eleminare alla fine
 /*   private void printQuestions(List<Question> questions) {
     System.out.println("\n////////////////////////// RANDOM GENERATED ANSWER \\\\\\\\\\\\\\\\\\\\\\\\\\ \n");

     for (int i = 0; i < questions.size(); i++) {
     System.out.println("======================= CICLO(" + i + ")============================");
     System.out.println("level: " + questions.get(i).getLevel());
     System.out.println("href: " + questions.get(i).getImgUri());
     System.out.println("questionText: " + questions.get(i).getQuestionText());

     System.out.println("Options: \n");
     for (int j = 0; j < questions.get(i).getAnswersList().size(); j++) {

     System.out.println(j + ") " + questions.get(i).getAnswersList().get(j).getAnswerText());

     }

     System.out.println("\nNumero Seriale:--> " + UUID.randomUUID().toString().substring(0, 7).toUpperCase());
     }
     } 
    
     */
    public static QuestionsExtractor getInstance() {

        if (instance == null) {
            instance = new QuestionsExtractor();
        }
        return instance;
    }
}
