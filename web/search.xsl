<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="html" indent="yes" encoding="utf-8"/>

    <xsl:template match="/">
        <html>
            <head>
                <title>
                    <xsl:text>Форма запроса</xsl:text>
                </title>
            </head>
            <body>
                <form method="get" action="search.xml">
                    <input type="text" name="queryText" size="20"/>
                    <input type="text" name="loop" size="5" value="5"/>
                    <input type="hidden" name="button" value="query" />
                    <input type="submit" value="Добавить запрос в базу данных"/>
                </form>
                <form method="get" action="search.xml">
                    <input type="hidden" name="button" value="cluster" />
                    <input type="submit" value="Кластеры"/>
                </form>
                <form method="get" action="search.xml">
                    <input type="hidden" name="button" value="base" />
                    <input type="submit"  value="Показать всю базу" />
                </form>
                <form method="get" action="search.xml">
                    <input type="hidden" name="button" value="clean" />
                    <input type="submit"  value="Очистить базу" />
                </form>
                <hr/>
                <xsl:apply-templates select="page/data/string"/>
                <xsl:if test="boolean(page/data/string)"><hr/></xsl:if>
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