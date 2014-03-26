
package org.mwt.xmlquiz.soa.artifacts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per QuestionXmlTextOrUri complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="QuestionXmlTextOrUri">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="QuestionXmlText" type="{http://it.univaq.mwt.soa/XMLQuizService.xsd1}XmlText" minOccurs="0"/>
 *         &lt;element name="QuestionXmlUri" type="{http://it.univaq.mwt.soa/XMLQuizService.xsd1}XmlUri" minOccurs="0"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QuestionXmlTextOrUri", propOrder = {
    "questionXmlText",
    "questionXmlUri"
})
public class QuestionXmlTextOrUri {

    @XmlElement(name = "QuestionXmlText")
    protected String questionXmlText;
    @XmlElement(name = "QuestionXmlUri")
    protected String questionXmlUri;

    /**
     * Recupera il valore della proprietà questionXmlText.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQuestionXmlText() {
        return questionXmlText;
    }

    /**
     * Imposta il valore della proprietà questionXmlText.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQuestionXmlText(String value) {
        this.questionXmlText = value;
    }

    /**
     * Recupera il valore della proprietà questionXmlUri.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQuestionXmlUri() {
        return questionXmlUri;
    }

    /**
     * Imposta il valore della proprietà questionXmlUri.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQuestionXmlUri(String value) {
        this.questionXmlUri = value;
    }

}
