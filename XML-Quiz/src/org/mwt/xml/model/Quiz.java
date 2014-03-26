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
public class Quiz {
    private String serialNumber;
    private String timestamp;
    private DidacticSector sector;
    private Integer level;
    private Student student;
    private List<Question> questionsList = new ArrayList<>();

    public Quiz() {
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
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

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public List<Question> getQuestionsList() {
        return questionsList;
    }

    public void setQuestionsList(List<Question> questionsList) {
        this.questionsList = questionsList;
    }

 
    
}
