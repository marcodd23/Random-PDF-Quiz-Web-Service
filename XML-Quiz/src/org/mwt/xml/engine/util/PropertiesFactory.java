/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mwt.xml.engine.util;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mwt.xml.XmlQuiz;

/**
 *
 * @author marco
 */
public class PropertiesFactory {

    private static PropertiesFactory propertiesFactory = null;

    private final Properties properties = new Properties();

    private PropertiesFactory() {
        try {
            this.properties.load(this.getClass().getResourceAsStream("/xmlQuiz.properties"));
        } catch (IOException ex) {
            Logger.getLogger(XmlQuiz.class.getName()).log(Level.SEVERE, "Failed to load repositoryQuizPath.properties", ex);
            ex.printStackTrace();
        }
        System.out.println("Got: " + "repositoryQuizPath.properties");
    }

    public Properties getRepositoryPathProperties() {
        return properties;
    }

    public static PropertiesFactory getInstance() {
        if (propertiesFactory == null) {
            propertiesFactory = new PropertiesFactory();
        }
        return propertiesFactory;
    }

}
