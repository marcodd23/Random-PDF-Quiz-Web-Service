package org.mwt.xml.exceptions;

import java.util.ArrayList;
import java.util.List;

public class XMLQuizException extends Exception {

    private static final long serialVersionUID = 1L;

    private ExceptionCause exceptionCause;

    private List<ExceptionCause> exceptionCauseList;

    public XMLQuizException(List<ExceptionCause> exceptionCauseList) {
        this.exceptionCauseList = exceptionCauseList;
    }

    public XMLQuizException(ExceptionCause exceptionCause) {
        this.exceptionCause = exceptionCause;
    }

    public XMLQuizException(ExceptionCause exceptionCause, Throwable cause) {
        super(cause);
        this.exceptionCause = exceptionCause;
    }

    public XMLQuizException() {
        super();
    }

    public XMLQuizException(String message, Throwable cause) {
        super(message, cause);
    }

    public XMLQuizException(String message) {
        super(message);
    }

    public XMLQuizException(Throwable cause) {
        super(cause);
    }

    public ExceptionCause getExceptionCause() {
        return exceptionCause;
    }

    public void setExceptionCause(ExceptionCause exceptionCause) {
        this.exceptionCause = exceptionCause;
    }

    public List<ExceptionCause> getExceptionCauseList() {
        return exceptionCauseList;
    }

    public void setExceptionCauseList(List<ExceptionCause> exceptionCauseList) {
        this.exceptionCauseList = exceptionCauseList;
    }

    public enum ExceptionCause {

        NOT_VALID("Il documento caricato non è valido rispetto alla grammatica"),
        ERROR_ADD_QUESTIONS("Impossibile scrivere i quesiti aggiuntivi sul repository !!! "),
        STUDENT_ID_NOT_VALID("La matricola non è valida"),
        SECTORID_NOT_EXIST("Il settore inserito non esiste"),
        NOT_ENOUGH_QUESTIONS("Non ci sono abbastanza quesiti per costruire il Quiz"),
        WRONG_DIFFICULT_LEVEL("Il livello di difficoltà deve essere compreso tra 1 e 5");

        private final String cause;

        private ExceptionCause(final String cause) {
            this.cause = cause;
        }

        @Override
        public String toString() {
            return cause;
        }

    }

}
