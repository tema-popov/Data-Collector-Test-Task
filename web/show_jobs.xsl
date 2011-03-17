<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="html" indent="yes" encoding="utf-8"/>

    <xsl:template match="/">
        <html>
            <head>
                <title>
                    <xsl:text>Проверка состояния базы</xsl:text>
                </title>
            </head>
            <body>
            <xsl:apply-templates select="page/data/job"/>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="job">
        <h1><xsl:value-of select="title"/></h1>
        <p><xsl:value-of select="description"/></p>
        <p><xsl:value-of select="salary"/></p>
        <a href="{link}"><xsl:value-of select="link"/></a>
        <p><xsl:if test="@full-day = 'true'">Полный День</xsl:if></p>


    </xsl:template>



</xsl:stylesheet>