/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mwt.xml.engine;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
//import org.mwt.xml.engine.QuizTransformerInterface;
import org.mwt.xml.engine.util.PropertiesFactory;
import org.mwt.xml.exceptions.MyErrorListener;

/**
 *
 * @author marco
 */
public class QuizTransformer {

    private static QuizTransformer instance = null;

    private final PropertiesFactory propertiesFactory = PropertiesFactory.getInstance();

    private final Properties properties = propertiesFactory.getRepositoryPathProperties();

    private final FopFactory fopFactory = FopFactory.newInstance();

    public QuizTransformer() {
    }

    /**
     * Prende in input l'InputStream del QUIZ XML da trasformare in pdf, esegue
     * la trasformazione XSL tramite il foglio di stile XSL, generando l'XSL-FO
     * che poi viene trasformato in PDF usando Apache FOP
     *
     * @param xmlSource
     * @return
     * @throws java.io.IOException
     */
    public ByteArrayOutputStream quizToPDF(InputStream xmlSource) throws IOException {

        /*resetto il puntatore all'inizio dello streaming 
         *nel caso sia già stato percorso dal metodo chiamante per validare
         */
        xmlSource.reset();
        
        Source xmlQuizSrx = new StreamSource(xmlSource);
        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();

        ByteArrayOutputStream outputStream = null;
        try {
            //OutputStream pdfOut = new BufferedOutputStream(new FileOutputStream(new File(pdfPath)));

            outputStream = new ByteArrayOutputStream();

            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, outputStream);

            TransformerFactory trFactory = TransformerFactory.newInstance();
            //Transformer transformer = trFactory.newTransformer(new StreamSource(new File(properties.getProperty("xslFoPath"))));
            Transformer transformer = trFactory.newTransformer(new StreamSource(getClass().getResourceAsStream(properties.getProperty("xslFoPath"))));
            System.out.println("xsl Path: " + properties.getProperty("xslFoPath"));

            transformer.setErrorListener(new MyErrorListener());

            Result res = new SAXResult(fop.getDefaultHandler());

            transformer.transform(xmlQuizSrx, res);
            return outputStream;

        } catch (FOPException ex) {
            Logger.getLogger(QuizTransformer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(QuizTransformer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(QuizTransformer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return outputStream;

    }

    public static QuizTransformer getInstance() {
        if (instance == null) {
            instance = new QuizTransformer();
        }
        return instance;
    }

}
