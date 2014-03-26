package org.mwt.xml.engine.util;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.mwt.xml.model.DidacticSector;
import org.mwt.xml.model.Question;

/**
 *
 * @author marco
 */
public class XmlSTAXParserUtil {

    private static XmlSTAXParserUtil istance = null;

    public int reqComplStaxEventHandler(XMLStreamReader xsr, Integer difficultLevel,
            Integer questionsNumber) throws XMLStreamException {

        boolean inQuestionsGroup = false;
        boolean inQuestion = false;
        boolean inLevel = false;
        String elementText = null;
        int complianceCounter = 0;

        while (xsr.hasNext() && (complianceCounter < questionsNumber)) {
            //System.out.println("complianceCounter=" + complianceCounter +  ", questionNumber=" + questionNumber );

            int next = xsr.next();
            if (xsr.isWhiteSpace()) {
                continue; //per ignorare gli spazi bianchi
            }
            switch (next) {

                case XMLStreamReader.START_DOCUMENT:
                    inQuestionsGroup = false;
                    inQuestion = false;
                    inLevel = false;
                    break;

                case XMLStreamReader.START_ELEMENT:
                    if (xsr.getLocalName().equals("questionsGroup")) {
                        inQuestionsGroup = true;
                    } else if (xsr.getLocalName().equals("question") && inQuestionsGroup) {
                        inQuestion = true;
                    } else if (xsr.getLocalName().equals("level") && inQuestion) {
                        //ATTENZIONE: leggendo il contenuto con getElementText
                        //non verrà generato l'evento di chiusura END_ELEMENT per questo elemento!
                        elementText = xsr.getElementText();
                        if (elementText.equals(difficultLevel.toString())) {
                            complianceCounter++;
                        }
                    }
                    break;

                case XMLStreamReader.END_ELEMENT:
                    if (xsr.getLocalName().equals("questionsGroup")) {
                        inQuestionsGroup = false;
                    } else if (xsr.getLocalName().equals("question") && inQuestionsGroup) {
                        inQuestion = false;
                    }
                    break;
            }
        }

        return complianceCounter;

    }

    /*public DidacticSector extractDidacticSectorStaxEventHandler(XMLStreamReader xsr) throws XMLStreamException {

        DidacticSector sector = new DidacticSector();
        boolean inDidacticSector = false;
        boolean parseEnd = false;

        while (xsr.hasNext() && !parseEnd) {
            //System.out.println("complianceCounter=" + complianceCounter +  ", questionNumber=" + questionNumber );

            int next = xsr.next();
            if (xsr.isWhiteSpace()) {
                continue; //per ignorare gli spazi bianchi
            }
            switch (next) {

                case XMLStreamReader.START_DOCUMENT:
                    break;

                case XMLStreamReader.START_ELEMENT:
                    if (xsr.getLocalName().equals("didacticSector")) {
                        inDidacticSector = true;
                    } else if (xsr.getLocalName().equals("sectorId") && inDidacticSector) {
                          sector.setSectorID(xsr.getElementText());  
                    } else if (xsr.getLocalName().equals("description") && inDidacticSector) {

                        sector.setDescription(xsr.getElementText());
                        parseEnd = true;
                    }
                    break;

                case XMLStreamReader.END_ELEMENT:
                    break;
            }
        }

        return sector;
    }*/
    

    public static void closeStaxStreamReader(XMLStreamReader xsr, FileReader fr) {
        try {
            if (xsr != null) {
                xsr.close();
            }
            if (fr != null) {
                fr.close();
            }
        } catch (XMLStreamException ex) {
            //
        } catch (IOException ex) {
            //
        }
    }

    public static XmlSTAXParserUtil getIstance() {

        if (istance == null) {
            istance = new XmlSTAXParserUtil();
        }
        return istance;
    }

}
