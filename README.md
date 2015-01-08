Random-PDF-Quiz-Web-Service
===========================

A Java application that randomly generate School Test of N Quiz Questions in XML format, after it relies on XSLT Transformation to generate an XSL-FO source and then thanks to Apache FOP it generate a PDF Test. The test contains N quizzes of the chosen difficulty level and each one has 3 randomly answers (only one correct). We defined an XML Schema to define the quiz structure and Test structure , and the quiz data (questions and multiple answers) was stored in XML documents.The Application was developed both as standalone App and also in the form of WEB Service and Client

##How To:

To try the application, change the absolute path in the file **XML-Quiz/src/xmlQuiz.properties**  
