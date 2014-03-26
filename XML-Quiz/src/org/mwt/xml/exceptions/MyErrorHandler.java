package org.mwt.xml.exceptions;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author MasterWeb
 */
public class MyErrorHandler implements org.xml.sax.ErrorHandler {

    private int nErrors, nWarnings, nFatals;

    public MyErrorHandler() {
        reset();
    }

    public void reset() {
        nErrors = 0;
        nWarnings = 0;
        nFatals = 0;
    }

    @Override
    public void warning(SAXParseException exception) throws SAXException {
        System.err.println(
                "Warning (" + exception.getSystemId()
                + ": " + exception.getLineNumber()
                + "," + exception.getColumnNumber()
                + ") " + exception.getMessage());
        nWarnings++;
    }

    @Override
    public void error(SAXParseException exception) throws SAXException {
        System.err.println(
                "Errore (" + exception.getSystemId()
                + ": " + exception.getLineNumber()
                + "," + exception.getColumnNumber()
                + ") " + exception.getMessage());
        nErrors++;
    }

    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
        System.err.println(
                "Errore fatale (" + exception.getSystemId()
                + ": " + exception.getLineNumber()
                + "," + exception.getColumnNumber()
                + ") " + exception.getMessage());
        nFatals++;
        throw exception;
    }

    public boolean hasErrors() {
        return ((nFatals+nErrors)>0);
    }


}
