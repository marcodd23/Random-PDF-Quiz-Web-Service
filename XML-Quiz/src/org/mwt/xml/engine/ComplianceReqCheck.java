package org.mwt.xml.engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
//import org.mwt.xml.engine.ComplianceReqCheckInterface;
import org.mwt.xml.engine.util.PropertiesFactory;
import org.mwt.xml.engine.util.ValidatorUtil;
import org.mwt.xml.engine.util.XmlSTAXParserUtil;

/**
 *
 * @author marco
 */
public class ComplianceReqCheck {

    private static ComplianceReqCheck instance = null;

    private final XmlSTAXParserUtil xmlParserUtil = XmlSTAXParserUtil.getIstance();

    private final PropertiesFactory propertiesFactory = PropertiesFactory.getInstance();

    private final Properties properties = propertiesFactory.getRepositoryPathProperties();

    private final ValidatorUtil validatorUtil = ValidatorUtil.getInstance();

    private ComplianceReqCheck() {

    }

    public List<String> listAllSectors() {
        File folder = new File(properties.getProperty("questionPath"));
        File[] listOfFiles = folder.listFiles();
        List<String> sectorsList = new ArrayList<>();
        int totalFiles = listOfFiles.length;
        if (listOfFiles.length != 0) {
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    sectorsList.add(listOfFiles[i].getName().split("\\.")[0]);
                }
            }
        }
        return sectorsList;
    }

    /**
     * PARSING FATTO CON STAX per controllare se ci sono almeno N Questions del
     * livello Richiesto, in modo da poter soddisfare la richiesta dell'utente
     *
     * @param didacticSector
     * @param difficultLevel
     * @param questionsNumber
     * @return
     */
    public boolean checkRequestCompliance(String didacticSector,
            Integer difficultLevel, Integer questionsNumber) {

        File xmlQuestionFile = new File(this.properties.getProperty("questionPath") + "/" + didacticSector + ".xml");
        // File xmlQuestionSchemaFile = new File(this.properties.getProperty("questionSchemaPath")); 
        InputStream xmlQuestionSchema = this.getClass().getResourceAsStream(this.properties.getProperty("questionSchemaPath"));

        int complianceCounter = 0;
        XMLInputFactory xif = XMLInputFactory.newInstance();
        XMLStreamReader xsr = null;
        FileReader fr = null;

        if (validatorUtil.validateXML(xmlQuestionFile, xmlQuestionSchema)) {
            try {
                fr = new FileReader(xmlQuestionFile);
                xsr = xif.createXMLStreamReader(fr);

                complianceCounter = xmlParserUtil.reqComplStaxEventHandler(xsr, difficultLevel, questionsNumber);

            } catch (FileNotFoundException ex) {
                Logger.getLogger(ComplianceReqCheck.class.getName()).log(Level.SEVERE, null, ex);
            } catch (XMLStreamException ex) {
                //XMLStreamException è l'unico tipo di eccezione dello stream reader STAX
                // Logger.getLogger(XML_tests.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Errore ["
                        + ex.getLocation().getSystemId() + ":"
                        + ex.getLocation().getLineNumber() + ","
                        + ex.getLocation().getColumnNumber() + "] "
                        + ex.getMessage());

            } finally {
                XmlSTAXParserUtil.closeStaxStreamReader(xsr, fr);
            }

        } else {
            return false;
        }

        if (complianceCounter == questionsNumber) {
            System.out.println("\ncomplianceCounter=" + complianceCounter);
            return true;
        } else {
            return false;
        }
    }

    public static ComplianceReqCheck getInstance() {
        if (instance == null) {
            instance = new ComplianceReqCheck();
        }
        return instance;
    }
}
