/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mwt.xml.model;

/**
 *
 * @author marco
 */
public class Answer {
    
    private boolean correct;
    private String answerText;

    public Answer() {
    }

    
    
    public Answer(boolean correct, String answerText) {
        this.correct = correct;
        this.answerText = answerText;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }
    
}
