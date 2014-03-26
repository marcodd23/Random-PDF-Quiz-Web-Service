/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mwt.xml.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author marco
 */
public class DidacticSector {
    
    private String sectorID;
    private String description;
    private List<Question> questions = new ArrayList<Question>();

    public DidacticSector() {
    }

    public String getSectorID() {
        return sectorID;
    }

    public void setSectorID(String sectorID) {
        this.sectorID = sectorID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }


}
