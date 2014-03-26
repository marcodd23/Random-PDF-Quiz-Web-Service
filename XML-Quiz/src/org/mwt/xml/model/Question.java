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
public class Question {
    
    private DidacticSector sector;
    private Integer level;
    private String imgUri;
    private String questionText;
    private List<Answer> answersList = new ArrayList<>();

    public Question() {
    }
    
    public Question(DidacticSector sector, Integer level, String imgUri, String questionText) {
        this.sector = sector;
        this.level = level;
        this.imgUri = imgUri;
        this.questionText = questionText;
    }

    public DidacticSector getSector() {
        return sector;
    }

    public void setSector(DidacticSector sector) {
        this.sector = sector;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public List<Answer> getAnswersList() {
        return answersList;
    }

    public void setAnswersList(List<Answer> answersList) {
        this.answersList = answersList;
    }

    
}
