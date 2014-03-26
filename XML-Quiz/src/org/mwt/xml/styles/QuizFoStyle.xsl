<?xml version="1.0" encoding="UTF-8" ?>

<!-- New document created with EditiX at Tue Mar 18 17:21:27 CET 2014 -->

<xsl:stylesheet 
    version="1.0" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:fo="http://www.w3.org/1999/XSL/Format"
    xmlns:def="http://it.univaq.mwt.xml/quiz">

    <xsl:output method="xml" indent="yes"/>
	
    <xsl:template match="/">
        <fo:root>
            <fo:layout-master-set>
                <fo:simple-page-master master-name="quiz" page-width="210mm" page-height="297mm">
                    <fo:region-body margin="10mm"/>
                    <fo:region-before extent="10mm"/>
                    <fo:region-after extent="0mm"/>
                </fo:simple-page-master>
            </fo:layout-master-set>
		
            <fo:page-sequence master-reference="quiz">
                			
                <fo:flow flow-name="xsl-region-body">
                    <fo:block font-size="14pt" font-family="NewtonAm">
                        <fo:table>
                            <fo:table-column column-width="100%"/>
                            <fo:table-body>
                                <fo:table-row height="30mm">
                                    <fo:table-cell>
                                        <fo:block>
                                            <xsl:call-template name="top"/>
                                        </fo:block>
                                        <fo:block margin-top="20mm">
                                            <xsl:call-template name="header"/>
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                                <fo:table-row border-top="1mm solid green" border-bottom="2mm solid green" height="{297-30-25-25}mm">
                                    <fo:table-cell>
                                        <fo:block padding-top="5mm">
                                            <xsl:apply-templates select="/def:quiz/def:questionsList"/>
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                            </fo:table-body>
                        </fo:table>
                    </fo:block>
                </fo:flow>              
            </fo:page-sequence>
        </fo:root>		
    </xsl:template>
    
    
    <!--    ==================== LISTA DI QUESTIONS ===========================================================-->  
    <xsl:template match="def:questionsList">
        <xsl:for-each select="def:question">      
            <fo:table  space-before="1cm">
                <fo:table-column column-width="5%"/>
                <fo:table-column column-width="95%"/>
                <fo:table-body>
                    <fo:table-row>
                        <fo:table-cell>
                            <fo:block>
                                <xsl:value-of select="position()"/>)
                            </fo:block>
                        </fo:table-cell> 
                        <fo:table-cell>
                            <fo:block>
                                <xsl:value-of select="def:text/text()"/>
                            </fo:block>
                        </fo:table-cell>  
                    </fo:table-row>
                    <fo:table-row>
                        <fo:table-cell>
                            <fo:block></fo:block>
                        </fo:table-cell>
                        <fo:table-cell>
                            <fo:block>
                                <xsl:apply-templates select="def:optionsList"/>
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </fo:table-body>        
            </fo:table>   
        </xsl:for-each> 
        
    </xsl:template>
    
    
    
    <!--  ====================== LISTA DI OPTION ANSWERS================  -->
    <xsl:template match="/def:quiz/def:questionsList/def:question/def:optionsList">
        
        <xsl:for-each select="*[self::def:option]">
            <fo:table>
                <fo:table-column column-width="5%"/>
                <fo:table-column column-width="95%"/>
                <fo:table-body>
                    <fo:table-row>
                        <fo:table-cell>
                            <fo:block font-style="italic" text-align="left">
                                <xsl:choose>
                                    <xsl:when test="position() = 1">a) </xsl:when>
                                    <xsl:when test="position() = 2">b) </xsl:when>
                                    <xsl:otherwise>c) </xsl:otherwise>
                                </xsl:choose>
                            </fo:block>
                        </fo:table-cell> 
                        <fo:table-cell>
                            <fo:block font-style="italic" text-align="left" border-left="8mm">
                                <xsl:value-of select="text()"/>
                            </fo:block>
                        </fo:table-cell>  
                    </fo:table-row>
                </fo:table-body>        
            </fo:table>
        </xsl:for-each>   
    </xsl:template>
    
    
    
    
    
    <!--  =================== TOP ===================  -->
    <xsl:template name="top">
        <fo:table>
            <fo:table-column column-width="50%"/>
            <fo:table-column column-width="50%"/>
            <fo:table-body>
                <fo:table-row>
                    <fo:table-cell>
                        <fo:block font-style="italic" text-align="left">
                            <xsl:value-of select="/def:quiz/def:didacticSector[1]/def:description[1]/text()"/>
                        </fo:block>    
                    </fo:table-cell>
                    <fo:table-cell text-align="center">
                        <fo:block>
                            QuizID: <xsl:value-of select="/def:quiz/def:serialNumber/text()"/>
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
            </fo:table-body>
        </fo:table>
    </xsl:template>
       
       
       
       
    <!--  ================== Header ====================  -->
    <xsl:template name="header">
        <fo:table>
            <fo:table-column column-width="100%"/>
            <fo:table-body>
                <fo:table-row>
                    <fo:table-cell>
                        <fo:block font-size="18pt" text-align="center">
                            QUIZ
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
            </fo:table-body>
        </fo:table>
        <fo:table>
            <fo:table-column column-width="50%"/>
            <fo:table-column column-width="50%"/>
            <fo:table-body>
                <fo:table-row>
                    <fo:table-cell>
                        <fo:block>                  
                            <fo:block font-style="italic" text-align="left">
                                Name: <xsl:value-of select="/def:quiz/def:student/def:name/text()"/>
                            </fo:block>
                            <fo:block font-style="italic" text-align="left">
                                Surname: <xsl:value-of select="/def:quiz/def:student/def:surname/text()"/>
                            </fo:block>
                            <fo:block font-style="italic" text-align="left">
                                IDNUmber: <xsl:value-of select="/def:quiz/def:student/def:idNumber/text()"/>
                            </fo:block>                         
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell text-align="center">
                        <fo:block>                 
                            Difficult Level: <xsl:value-of select=" /def:quiz/def:level/text()"/>
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
            </fo:table-body>
        </fo:table>
    </xsl:template>


</xsl:stylesheet>
