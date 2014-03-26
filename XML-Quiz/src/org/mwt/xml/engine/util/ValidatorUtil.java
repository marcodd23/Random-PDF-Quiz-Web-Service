/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mwt.xml.engine.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Documented;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.mwt.xml.exceptions.MyErrorHandler;
import org.xml.sax.SAXException;

/**
 *
 * @author marco
 */
public class ValidatorUtil {

    private static ValidatorUtil instance = null;

    
    /**fileToValidate deve essere di tipo File o InputStream*/
    public boolean validateXML(Object fileToValidate, InputStream schema) {
        MyErrorHandler eh = new MyErrorHandler();
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        sf.setErrorHandler(eh);

        StreamSource schemaSource = new StreamSource(schema);
        try {
            Schema s;
            if (schema != null) {
                s = sf.newSchema(schemaSource);
            } else {
                s = sf.newSchema();
            }
            Validator v = s.newValidator();
            v.setErrorHandler(eh);
            
            if (fileToValidate instanceof File) {
                v.validate(new StreamSource((File) fileToValidate));
            } else if (fileToValidate instanceof InputStream) {
                v.validate(new StreamSource((InputStream) fileToValidate));
            }
        } catch (IOException ex) {
            System.err.println("Errore di I/O: " + ex.getMessage());
            return false;
        } catch (SAXException ex) {
            //gestita dall'error handler
        }

        if (eh.hasErrors()) {
            System.out.println("*Il documento non è valido");
            return false;
        } else {
            //System.out.println("*Il documento è valido");
            return true;
        }
    }

//    public boolean validateXML(InputStream fileToValidate, InputStream schema) {
//        MyErrorHandler eh = new MyErrorHandler();
//        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
//        sf.setErrorHandler(eh);
//
//        StreamSource schemaSource = new StreamSource(schema);
//        try {
//            Schema s;
//            if (schema != null) {
//                s = sf.newSchema(schemaSource);
//            } else {
//                s = sf.newSchema();
//            }
//            Validator v = s.newValidator();
//            v.setErrorHandler(eh);
//
//            v.validate(new StreamSource(fileToValidate));
//
//        } catch (IOException ex) {
//            System.err.println("Errore di I/O: " + ex.getMessage());
//            return false;
//        } catch (SAXException ex) {
//            //gestita dall'error handler
//        }
//
//        if (eh.hasErrors()) {
//            System.out.println("*Il documento non è valido");
//            return false;
//        } else {
//            //System.out.println("*Il documento è valido");
//            return true;
//        }
//    }

    public static ValidatorUtil getInstance() {

        if (instance == null) {
            instance = new ValidatorUtil();
        }
        return instance;
    }

}
