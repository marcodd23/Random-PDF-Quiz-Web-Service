
package org.mwt.xmlquiz.soa.artifacts;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.mwt.xmlquiz.soa.artifacts package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _FaultElement_QNAME = new QName("http://it.univaq.mwt.soa/XMLQuizService.xsd1", "FaultElement");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.mwt.xmlquiz.soa.artifacts
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link FaultType }
     * 
     */
    public FaultType createFaultType() {
        return new FaultType();
    }

    /**
     * Create an instance of {@link QuestionXmlTextOrUri }
     * 
     */
    public QuestionXmlTextOrUri createQuestionXmlTextOrUri() {
        return new QuestionXmlTextOrUri();
    }

    /**
     * Create an instance of {@link QuizBuilderList }
     * 
     */
    public QuizBuilderList createQuizBuilderList() {
        return new QuizBuilderList();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FaultType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://it.univaq.mwt.soa/XMLQuizService.xsd1", name = "FaultElement")
    public JAXBElement<FaultType> createFaultElement(FaultType value) {
        return new JAXBElement<FaultType>(_FaultElement_QNAME, FaultType.class, null, value);
    }

}
