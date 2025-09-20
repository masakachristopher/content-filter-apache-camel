<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="text" indent="no"/>

    <xsl:template match="/">
        {
        "users": [
        <xsl:for-each select="LinkedHashMap/users[active='true']">
            {
            "id": "<xsl:value-of select="id"/>",
            "name": "<xsl:value-of select="name"/>",
            "email": "<xsl:value-of select="email"/>",
            "active": "<xsl:value-of select="active"/>",
            "roles": [
            <xsl:for-each select="roles">
                "<xsl:value-of select="."/>"
                <xsl:if test="position() != last()">, </xsl:if>
            </xsl:for-each>
            ],
            "internalNotes": "<xsl:value-of select="internalNotes"/>"
            }
            <xsl:if test="position() != last()">,</xsl:if>
        </xsl:for-each>
        ]
        }
    </xsl:template>
</xsl:stylesheet>
