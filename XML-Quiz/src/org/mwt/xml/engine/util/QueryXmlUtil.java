/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mwt.xml.engine.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 *
 * @author marco
 */
public class QueryXmlUtil {

    private static QueryXmlUtil instance = null;

    public QueryXmlUtil() {

    }

    /**
     * Metodo che esegue effettivamente la query XPATH 
     * *inputXML deve essere di tipo File o InputStream
     * @param inputXML
     * @param xpath
     * @param nsBinding
     * @return 
     */
    public Object queryDocument(Object inputXML, String xpath, String[] nsBinding) {
        Object result = null;
        try {
            MyNamespaceContext mnc = new MyNamespaceContext();
            mnc.addBinding(nsBinding[0], nsBinding[1]);
            XPathFactory xpf = XPathFactory.newInstance();
            XPath x = xpf.newXPath(); //XPath è un po' come lo statement per JDBC
            x.setNamespaceContext(mnc); //serve per risolvere i namespace

            //Object result = x.evaluate(xpath, new InputSource(new FileReader(f)), XPathConstants.NODESET);
            if (inputXML instanceof File) {
                result = x.evaluate(xpath, new InputSource(new FileReader((File) inputXML)), XPathConstants.NODESET);
            } else if (inputXML instanceof InputStream) {
                InputStream inputStream = (InputStream) inputXML;
                inputStream.reset();//Poiche l'inputstream è già stato letto per validare , 
                //viene riposizionato il puntatore all'inizio dell'inputstream
                result = x.evaluate(xpath, new InputSource(inputStream), XPathConstants.NODESET);
            }

            return result;

        } catch (FileNotFoundException ex) {
            Logger.getLogger(QueryXmlUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XPathExpressionException ex) {
            Logger.getLogger(QueryXmlUtil.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("ERRORE XPath: " + ex.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(QueryXmlUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static QueryXmlUtil getInstance() {

        if (instance == null) {
            instance = new QueryXmlUtil();
        }
        return instance;
    }

}
