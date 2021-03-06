<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        version="1.0">
    <xsl:output method="html" indent="no" encoding="utf-8"/>
    <xsl:template match="/">
        <html>
            <head><title>Тест</title></head>
            <body>
            <h1>
                <xsl:text>Проверка</xsl:text>
                <xsl:value-of select="count(//page)"/>
                <xsl:value-of select="count(//a)"/>
                <xsl:value-of select="count(//data)"/>
            </h1>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>