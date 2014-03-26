/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mwt.xml.exceptions;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;

/**
 *
 * @author MasterWeb
 */
public class MyErrorListener implements ErrorListener {

    private int nErrors, nWarnings, nFatals;

    public MyErrorListener() {
        reset();
    }

    public void reset() {
        nErrors = 0;
        nWarnings = 0;
        nFatals = 0;
    }

    public boolean hasErrors() {
        return ((nFatals + nErrors) > 0);
    }

    public void warning(TransformerException exception) throws TransformerException {
        System.err.println(
                "Warning (" + exception.getLocator().getSystemId()
                + ": " + exception.getLocator().getLineNumber()
                + "," + exception.getLocator().getColumnNumber()
                + ") " + exception.getMessage());
        nWarnings++;
    }

    public void error(TransformerException exception) throws TransformerException {
        System.err.println(
                "Errore (" + exception.getLocator().getSystemId()
                + ": " + exception.getLocator().getLineNumber()
                + "," + exception.getLocator().getColumnNumber()
                + ") " + exception.getMessage());
        nErrors++;
    }

    public void fatalError(TransformerException exception) throws TransformerException {
        System.err.println(
                "Errore fatale (" + exception.getLocator().getSystemId()
                + ": " + exception.getLocator().getLineNumber()
                + "," + exception.getLocator().getColumnNumber()
                + ") " + exception.getMessage());
        nFatals++;
        throw exception;
    }
}
