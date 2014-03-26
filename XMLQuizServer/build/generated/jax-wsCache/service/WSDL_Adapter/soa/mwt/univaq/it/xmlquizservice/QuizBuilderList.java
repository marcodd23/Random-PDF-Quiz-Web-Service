
package soa.mwt.univaq.it.xmlquizservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per QuizBuilderList complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="QuizBuilderList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="sector" type="{http://it.univaq.mwt.soa/XMLQuizService.xsd1}SectorID"/>
 *         &lt;element name="level" type="{http://it.univaq.mwt.soa/XMLQuizService.xsd1}DifficultLevel"/>
 *         &lt;element name="questionsNumber" type="{http://it.univaq.mwt.soa/XMLQuizService.xsd1}NumberOfQuestions"/>
 *         &lt;element name="studentName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="studentSurname" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="studentIDNumber" type="{http://it.univaq.mwt.soa/XMLQuizService.xsd1}StudentID" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QuizBuilderList", propOrder = {
    "sector",
    "level",
    "questionsNumber",
    "studentName",
    "studentSurname",
    "studentIDNumber"
})
public class QuizBuilderList {

    @XmlElement(required = true)
    protected String sector;
    protected int level;
    protected int questionsNumber;
    protected String studentName;
    protected String studentSurname;
    protected String studentIDNumber;

    /**
     * Recupera il valore della proprietà sector.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSector() {
        return sector;
    }

    /**
     * Imposta il valore della proprietà sector.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSector(String value) {
        this.sector = value;
    }

    /**
     * Recupera il valore della proprietà level.
     * 
     */
    public int getLevel() {
        return level;
    }

    /**
     * Imposta il valore della proprietà level.
     * 
     */
    public void setLevel(int value) {
        this.level = value;
    }

    /**
     * Recupera il valore della proprietà questionsNumber.
     * 
     */
    public int getQuestionsNumber() {
        return questionsNumber;
    }

    /**
     * Imposta il valore della proprietà questionsNumber.
     * 
     */
    public void setQuestionsNumber(int value) {
        this.questionsNumber = value;
    }

    /**
     * Recupera il valore della proprietà studentName.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStudentName() {
        return studentName;
    }

    /**
     * Imposta il valore della proprietà studentName.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStudentName(String value) {
        this.studentName = value;
    }

    /**
     * Recupera il valore della proprietà studentSurname.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStudentSurname() {
        return studentSurname;
    }

    /**
     * Imposta il valore della proprietà studentSurname.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStudentSurname(String value) {
        this.studentSurname = value;
    }

    /**
     * Recupera il valore della proprietà studentIDNumber.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStudentIDNumber() {
        return studentIDNumber;
    }

    /**
     * Imposta il valore della proprietà studentIDNumber.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStudentIDNumber(String value) {
        this.studentIDNumber = value;
    }

}
