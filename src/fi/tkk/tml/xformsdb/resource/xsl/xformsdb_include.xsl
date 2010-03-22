<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
				xmlns:xformsdb="http://www.tml.tkk.fi/2007/xformsdb"
				xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- Import the identity transformation -->
	<xsl:import href="identity.xsl" />
	<!-- Define the output format -->
	<xsl:output method="xml" version="1.0" />



	<xsl:template match="/">
		<!-- Apply further templates -->
		<xsl:apply-templates />
	</xsl:template>
	
	
	<xsl:template match="//xformsdb:include">
		<!-- Apply further templates to the included resource -->
		<xsl:apply-templates select="document( @resource, . )" />
		<!-- Apply further templates -->
		<xsl:apply-templates />
	</xsl:template>
	


</xsl:stylesheet>