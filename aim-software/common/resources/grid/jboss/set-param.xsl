<?xml version="1.0"?>
<xsl:stylesheet
   version='1.0'
   xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
>

<xsl:output method="xml" indent="yes" encoding="ISO-8859-1"
            doctype-public="-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN"
            doctype-system="http://java.sun.com/dtd/web-app_2_3.dtd"/>

<xsl:strip-space elements="servlet" />

<xsl:param name="parameterName">NAME</xsl:param>
<xsl:param name="parameterValue">VALUE</xsl:param>

<xsl:variable name="bf" select="'icon servlet-name display-name description jsp-file servlet-class'" />
<xsl:variable name="af" select="'init-param load-on-startup run-as security-role-ref'" />

<xsl:template match="@*|*|comment()">
  <xsl:copy><xsl:apply-templates select="@*|node()"/></xsl:copy>
</xsl:template>

<xsl:template match="servlet">
  <servlet>
  <xsl:apply-templates select="@*|node()[contains($bf, name())]"/>
  <xsl:if test="not(init-param/param-name = $parameterName)">
      <init-param>
            <param-name><xsl:value-of select="$parameterName"/></param-name>
            <param-value><xsl:value-of select="$parameterValue"/></param-value>
      </init-param> 
  </xsl:if>
  <xsl:apply-templates select="@*|node()[contains($af, name())]"/>
  </servlet>
</xsl:template>

<xsl:template match="init-param"> 
    <xsl:choose>
        <xsl:when test="param-name = $parameterName">
          <init-param>
              <xsl:copy-of select="param-name"/> 
              <param-value><xsl:value-of select="$parameterValue"/></param-value>
          </init-param>
        </xsl:when>
        <xsl:otherwise>
          <xsl:copy-of select="."/> 
        </xsl:otherwise>
      </xsl:choose>
</xsl:template>

</xsl:stylesheet>
