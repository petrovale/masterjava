<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html" indent="yes"/>

    <xsl:strip-space elements="*"/>
    <xsl:template match="/">
        <html>
            <body>
                <h1>Users</h1>
                <table border="1">
                    <tr>
                        <th>User name</th>
                        <th>Email</th>
                    </tr>
                    <xsl:for-each select="/*[name()='Payload']/*[name()='Users']/*">
                        <tr>
                            <td><xsl:value-of select="."/></td>
                            <td><xsl:value-of select="@email"/></td>
                        </tr>
                    </xsl:for-each>
                </table>
            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>