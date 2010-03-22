<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
				xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
				xmlns:saxon="http://saxon.sf.net/"
				exclude-result-prefixes="saxon">
	<!-- Define the output format -->
	<xsl:output method="xml" version="1.0" indent="yes" saxon:indent-spaces="4" />
	<xsl:strip-space elements="*" />
	
	
	
	<xsl:template match="/">
		<xsl:copy-of select="." />
	</xsl:template>



</xsl:stylesheet>