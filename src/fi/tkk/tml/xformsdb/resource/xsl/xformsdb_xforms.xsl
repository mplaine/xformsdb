<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
				xmlns:ev="http://www.w3.org/2001/xml-events"
				xmlns:saxon="http://saxon.sf.net/"
				xmlns:xforms="http://www.w3.org/2002/xforms"
				xmlns:xformsdb="http://www.tml.tkk.fi/2007/xformsdb"
				xmlns:xxforms="http://orbeon.org/oxf/xml/xforms"
				xmlns:exforms="http://www.exforms.org/exf/1-0"
				xmlns:xs="http://www.w3.org/2001/XMLSchema"
				xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				xmlns:xhtml="http://www.w3.org/1999/xhtml"
				xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
				exclude-result-prefixes="saxon">
	<!-- Import the identity transformation -->
	<xsl:import href="identity.xsl" />
	<!-- Define the output format -->
	<xsl:output method="xml" version="1.0" />
 	<!-- Define the parameters -->
	<!--
	<xsl:param name="paramDocType" select="''" />
	-->
	<xsl:param name="paramXFormsDBRequestBaseURIInstance" select="'xformsdb-request-base-uri-instance'" />
	<xsl:param name="paramXFormsDBRequestHeadersInstance" select="'xformsdb-request-headers-instance'" />
	<xsl:param name="paramXFormsDBRequestParametersInstance" select="'xformsdb-request-parameters-instance'" />
	<xsl:param name="paramXFormsDBStateInstance" select="'xformsdb-state-instance'" />
	<xsl:param name="paramXFormsDBErrorElement" select="'xformsdb:error'" />
	<xsl:param name="paramXFormsDBRequestErrorEvent" select="'xformsdb-request-error'" />
	<xsl:param name="paramXFormsDBRequestBaseURIXMLString" select="/.." />
	<xsl:param name="paramXFormsDBRequestHeadersXMLString" select="/.." />
	<xsl:param name="paramXFormsDBRequestParametersXMLString" select="/.." />
	<xsl:param name="paramXFormsDBStateXMLString" select="/.." />
	<xsl:param name="paramXFormsDBInstancesXMLString" select="/.." />
	<xsl:param name="paramXFormsDBSubmissionsXMLString" select="/.." />
	<xsl:param name="paramXFormsModelsXMLString" select="/.." />
	<xsl:param name="paramXFormsLoadsXMLString" select="/.." />
	<xsl:param name="paramXFormsSubmissionsXMLString" select="/.." />
	<xsl:param name="paramXHTMLMetasXMLString" select="/.." />
	<xsl:param name="paramXHTMLAsXMLString" select="/.." />



	<!-- Add DOCTYPE -->
	<xsl:template match="/">
		<!-- Disable output escaping because the DOCTYPE includes entities -->
		<!--
		<xsl:value-of disable-output-escaping="yes" select="$paramDocType" />
		-->
		<!-- Apply further templates -->
		<xsl:apply-templates />
	</xsl:template>
	

	<xsl:template match="/xhtml:html/xhtml:head/xforms:model">
		<!-- Parse the XML document -->
 		<xsl:variable name="varXFormsModels" select="saxon:parse( $paramXFormsModelsXMLString )/xforms:models" />
		<!-- Calculate the index/position of the matched <xforms:model> element -->
 		<xsl:variable name="varXFormsModelIndex" select="count( preceding::xforms:model ) + 1" />

		<xsl:copy>
			<xsl:copy-of select="@*" />
			<!-- Add the <xforms:instance id="xformsdb-response-proxy-instance-xxxxx"> element, which acts as a proxy inside this model for XFormsDB-related related -->
			<!-- Add the instance element and rename it after the parent (xforms:model) element -->
			<xsl:variable name="xformsdbResponseProxyInstanceElementName">
				<xsl:choose>
					<xsl:when test="string-length( name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] ) ) > 0">
						<xsl:value-of select="name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] )" /><xsl:text>:instance</xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<xsl:text>instance</xsl:text>
					</xsl:otherwise>
				</xsl:choose>				
			</xsl:variable>
			<xsl:variable name="xformsdbResponseProxyInstanceElementNamespaceURI">
				<xsl:value-of select="'http://www.w3.org/2002/xforms'" />
			</xsl:variable>
			<xsl:element name="{ $xformsdbResponseProxyInstanceElementName }" namespace="{ $xformsdbResponseProxyInstanceElementNamespaceURI }">
				<xsl:attribute name="id">
					<xsl:value-of select="$varXFormsModels/xforms:model[ $varXFormsModelIndex ]/@proxyinstance" />
				</xsl:attribute>
				<dummy xmlns="" />
			</xsl:element>
			
			<xsl:if test="$varXFormsModelIndex = 1">
				<!-- Add the <xforms:instance id="xformsdb-request-base-uri"> element, which provides request base URI -->
				<!-- Add the instance element and rename it after the parent (xforms:model) element -->
				<xsl:variable name="xformsdbRequestBaseURIInstanceElementName">
					<xsl:choose>
						<xsl:when test="string-length( name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] ) ) > 0">
							<xsl:value-of select="name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] )" /><xsl:text>:instance</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>instance</xsl:text>
						</xsl:otherwise>
					</xsl:choose>				
				</xsl:variable>
				<xsl:variable name="xformsdbRequestBaseURIInstanceElementNamespaceURI">
					<xsl:value-of select="'http://www.w3.org/2002/xforms'" />
				</xsl:variable>
				<xsl:element name="{ $xformsdbRequestBaseURIInstanceElementName }" namespace="{ $xformsdbRequestBaseURIInstanceElementNamespaceURI }">
					<xsl:attribute name="id">
						<xsl:value-of select="$paramXFormsDBRequestBaseURIInstance" />
					</xsl:attribute>
					<!-- Parse the XML document -->
			 		<xsl:variable name="varXFormsDBRequestBaseURI" select="saxon:parse( $paramXFormsDBRequestBaseURIXMLString )" />
					<xsl:copy-of select="$varXFormsDBRequestBaseURI" />
				</xsl:element>			
				<!-- Add the <xforms:instance id="xformsdb-request-headers"> element, which provides all request headers -->
				<!-- Add the instance element and rename it after the parent (xforms:model) element -->
				<xsl:variable name="xformsdbRequestHeadersInstanceElementName">
					<xsl:choose>
						<xsl:when test="string-length( name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] ) ) > 0">
							<xsl:value-of select="name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] )" /><xsl:text>:instance</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>instance</xsl:text>
						</xsl:otherwise>
					</xsl:choose>				
				</xsl:variable>
				<xsl:variable name="xformsdbRequestHeadersInstanceElementNamespaceURI">
					<xsl:value-of select="'http://www.w3.org/2002/xforms'" />
				</xsl:variable>
				<xsl:element name="{ $xformsdbRequestHeadersInstanceElementName }" namespace="{ $xformsdbRequestHeadersInstanceElementNamespaceURI }">
					<xsl:attribute name="id">
						<xsl:value-of select="$paramXFormsDBRequestHeadersInstance" />
					</xsl:attribute>
					<!-- Parse the XML document -->
			 		<xsl:variable name="varXFormsDBRequestHeaders" select="saxon:parse( $paramXFormsDBRequestHeadersXMLString )" />
					<xsl:copy-of select="$varXFormsDBRequestHeaders" />
				</xsl:element>			
				<!-- Add the <xforms:instance id="xformsdb-request-parameters"> element, which provides all request parameters -->
				<!-- Add the instance element and rename it after the parent (xforms:model) element -->
				<xsl:variable name="xformsdbRequestParametersInstanceElementName">
					<xsl:choose>
						<xsl:when test="string-length( name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] ) ) > 0">
							<xsl:value-of select="name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] )" /><xsl:text>:instance</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>instance</xsl:text>
						</xsl:otherwise>
					</xsl:choose>				
				</xsl:variable>
				<xsl:variable name="xformsdbRequestParametersInstanceElementNamespaceURI">
					<xsl:value-of select="'http://www.w3.org/2002/xforms'" />
				</xsl:variable>
				<xsl:element name="{ $xformsdbRequestParametersInstanceElementName }" namespace="{ $xformsdbRequestParametersInstanceElementNamespaceURI }">
					<xsl:attribute name="id">
						<xsl:value-of select="$paramXFormsDBRequestParametersInstance" />
					</xsl:attribute>
					<!-- Parse the XML document -->
			 		<xsl:variable name="varXFormsDBRequestParameters" select="saxon:parse( $paramXFormsDBRequestParametersXMLString )" />
					<xsl:copy-of select="$varXFormsDBRequestParameters" />
				</xsl:element>
				<!-- Add the <xforms:instance id="xformsdb-state"> element, which provides the application state -->
				<!-- Add the instance element and rename it after the parent (xforms:model) element -->
				<xsl:variable name="xformsdbStateInstanceElementName">
					<xsl:choose>
						<xsl:when test="string-length( name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] ) ) > 0">
							<xsl:value-of select="name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] )" /><xsl:text>:instance</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>instance</xsl:text>
						</xsl:otherwise>
					</xsl:choose>				
				</xsl:variable>
				<xsl:variable name="xformsdbStateInstanceElementNamespaceURI">
					<xsl:value-of select="'http://www.w3.org/2002/xforms'" />
				</xsl:variable>
				<xsl:element name="{ $xformsdbStateInstanceElementName }" namespace="{ $xformsdbStateInstanceElementNamespaceURI }">
					<xsl:attribute name="id">
						<xsl:value-of select="$paramXFormsDBStateInstance" />
					</xsl:attribute>
					<!-- Parse the XML document -->
			 		<xsl:variable name="varXFormsDBState" select="saxon:parse( $paramXFormsDBStateXMLString )" />
					<xsl:copy-of select="$varXFormsDBState" />
				</xsl:element>				
			</xsl:if>
			<!-- Apply further templates -->
			<xsl:apply-templates />
		</xsl:copy>
	</xsl:template>
	
	
	<!-- Transform the <xformsdb:instance> element -->
	<xsl:template match="/xhtml:html/xhtml:head/xforms:model/xformsdb:instance">
		<!-- Parse the XML document -->
 		<xsl:variable name="varXFormsDBInstances" select="saxon:parse( $paramXFormsDBInstancesXMLString )/xformsdb:instances" />
		<!-- Calculate the index/position of the matched <xformsdb:instance> element -->
 		<xsl:variable name="varXFormsDBInstanceIndex" select="count( preceding::xformsdb:instance ) + 1" />
		<!-- Transform the <xformsdb:instance> element --> 
		<!-- Rename the element after the parent element -->
		<xsl:variable name="xformsInstanceElementName">
			<xsl:choose>
				<xsl:when test="string-length( name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] ) ) > 0">
					<xsl:value-of select="name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] )" /><xsl:text>:instance</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>instance</xsl:text>
				</xsl:otherwise>
			</xsl:choose>				
		</xsl:variable>
		<xsl:variable name="xformsInstanceElementNamespaceURI">
			<xsl:value-of select="'http://www.w3.org/2002/xforms'" />
		</xsl:variable>
		<xsl:element name="{ $xformsInstanceElementName }" namespace="{ $xformsInstanceElementNamespaceURI }" >
			<!-- Iterate over all attributes -->
 			<xsl:for-each select="@*">
 				<xsl:choose>
	 				<!-- Remove the untransformed/incorrect id attribute -->
	 				<xsl:when test="name() = 'id'">
	 				</xsl:when>
	 				<!-- Copy other attributes -->
	 				<xsl:otherwise>
						<xsl:attribute name="{ name() }">
							<xsl:value-of select="." />
						</xsl:attribute>
	 				</xsl:otherwise>
	 			</xsl:choose>
 			</xsl:for-each>
			<!-- Add the transformed/correct id attribute -->
			<xsl:if test="$varXFormsDBInstances/xformsdb:instance[ $varXFormsDBInstanceIndex ]/@id">
				<xsl:attribute name="id">
					<xsl:value-of select="$varXFormsDBInstances/xformsdb:instance[ $varXFormsDBInstanceIndex ]/@id" />
				</xsl:attribute>
			</xsl:if>
			<!-- Transform XFormsDB-related requests -->
			<xsl:choose>
				<!-- Transform the <xformsdb:query> element -->
				<xsl:when test="$varXFormsDBInstances/xformsdb:instance[ $varXFormsDBInstanceIndex ]/xformsdb:query">
					<xsl:call-template name="transformXFormsDBQuery">
						<xsl:with-param name="pXFormsDBInstances" select="$varXFormsDBInstances" />
						<xsl:with-param name="pXFormsDBInstanceIndex" select="$varXFormsDBInstanceIndex" />
					</xsl:call-template>
				</xsl:when>
				<!-- Transform the <xformsdb:widgetquery> element -->
				<xsl:when test="$varXFormsDBInstances/xformsdb:instance[ $varXFormsDBInstanceIndex ]/xformsdb:widgetquery">
					<xsl:call-template name="transformXFormsDBWidgetQuery">
						<xsl:with-param name="pXFormsDBInstances" select="$varXFormsDBInstances" />
						<xsl:with-param name="pXFormsDBInstanceIndex" select="$varXFormsDBInstanceIndex" />
					</xsl:call-template>
				</xsl:when>
				<!-- Transform the <xformsdb:state> element -->
				<xsl:when test="$varXFormsDBInstances/xformsdb:instance[ $varXFormsDBInstanceIndex ]/xformsdb:state">
					<xsl:call-template name="transformXFormsDBState">
						<xsl:with-param name="pXFormsDBInstances" select="$varXFormsDBInstances" />
						<xsl:with-param name="pXFormsDBInstanceIndex" select="$varXFormsDBInstanceIndex" />
					</xsl:call-template>
				</xsl:when>
				<!-- Transform the <xformsdb:login> element -->
				<xsl:when test="$varXFormsDBInstances/xformsdb:instance[ $varXFormsDBInstanceIndex ]/xformsdb:login">
					<xsl:call-template name="transformXFormsDBLogin">
						<xsl:with-param name="pXFormsDBInstances" select="$varXFormsDBInstances" />
						<xsl:with-param name="pXFormsDBInstanceIndex" select="$varXFormsDBInstanceIndex" />
					</xsl:call-template>
				</xsl:when>
				<!-- Transform the <xformsdb:logout> element -->
				<xsl:when test="$varXFormsDBInstances/xformsdb:instance[ $varXFormsDBInstanceIndex ]/xformsdb:logout">
					<xsl:call-template name="transformXFormsDBLogout">
						<xsl:with-param name="pXFormsDBInstances" select="$varXFormsDBInstances" />
						<xsl:with-param name="pXFormsDBInstanceIndex" select="$varXFormsDBInstanceIndex" />
					</xsl:call-template>
				</xsl:when>
				<!-- Transform the <xformsdb:user> element -->
				<xsl:when test="$varXFormsDBInstances/xformsdb:instance[ $varXFormsDBInstanceIndex ]/xformsdb:user">
					<xsl:call-template name="transformXFormsDBUser">
						<xsl:with-param name="pXFormsDBInstances" select="$varXFormsDBInstances" />
						<xsl:with-param name="pXFormsDBInstanceIndex" select="$varXFormsDBInstanceIndex" />
					</xsl:call-template>
				</xsl:when>
				<!-- Transform the <xformsdb:file> element -->
				<xsl:when test="$varXFormsDBInstances/xformsdb:instance[ $varXFormsDBInstanceIndex ]/xformsdb:file">
					<xsl:call-template name="transformXFormsDBFile">
						<xsl:with-param name="pXFormsDBInstances" select="$varXFormsDBInstances" />
						<xsl:with-param name="pXFormsDBInstanceIndex" select="$varXFormsDBInstanceIndex" />
					</xsl:call-template>
				</xsl:when>
				<!-- Transform the <xformsdb:cookie> element -->
				<xsl:when test="$varXFormsDBInstances/xformsdb:instance[ $varXFormsDBInstanceIndex ]/xformsdb:cookie">
					<xsl:call-template name="transformXFormsDBCookie">
						<xsl:with-param name="pXFormsDBInstances" select="$varXFormsDBInstances" />
						<xsl:with-param name="pXFormsDBInstanceIndex" select="$varXFormsDBInstanceIndex" />
					</xsl:call-template>
				</xsl:when>
				<!-- Otherwise do nothing -->
				<xsl:otherwise>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:element>
 	</xsl:template>


	<!-- Transform the <xformsdb:query> element -->
	<xsl:template name="transformXFormsDBQuery">
		<!-- Define the parameters -->
		<xsl:param name="pXFormsDBInstances" />
		<xsl:param name="pXFormsDBInstanceIndex" />

		<!-- Add the element -->
		<xsl:variable name="xformsdbQueryElementName">
			<xsl:choose>
				<xsl:when test="string-length( name( namespace::*[ . = 'http://www.tml.tkk.fi/2007/xformsdb' ] ) ) > 0">
					<xsl:value-of select="name( namespace::*[ . = 'http://www.tml.tkk.fi/2007/xformsdb' ] )" /><xsl:text>:query</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>query</xsl:text>
				</xsl:otherwise>
			</xsl:choose>				
		</xsl:variable>
		<xsl:variable name="xformsdbQueryElementNamespaceURI">
			<xsl:value-of select="'http://www.tml.tkk.fi/2007/xformsdb'" />
		</xsl:variable>
		<xsl:element name="{ $xformsdbQueryElementName }" namespace="{ $xformsdbQueryElementNamespaceURI }">
 			<!-- Add the transformed/correct id attribute -->
			<xsl:if test="$pXFormsDBInstances/xformsdb:instance[ $pXFormsDBInstanceIndex ]/xformsdb:query/@id">
	 			<xsl:attribute name="id">
	  				<xsl:value-of select="$pXFormsDBInstances/xformsdb:instance[ $pXFormsDBInstanceIndex ]/xformsdb:query/@id" />
				</xsl:attribute>
			</xsl:if>
 			<!-- Copy the <xformsdb:xmlns> elements -->
			<!--<xsl:apply-templates select="./xformsdb:query/xformsdb:xmlns" />-->
 			<!-- Copy the <xformsdb:var> elements -->
			<xsl:apply-templates select="./xformsdb:query/xformsdb:var" />
 			<!-- Copy the <xformsdb:secvar> elements -->
			<!--<xsl:apply-templates select="./xformsdb:query/xformsdb:secvar[ @name = 'username' ][ 1 ]" />-->
			<!--<xsl:apply-templates select="./xformsdb:query/xformsdb:secvar[ @name = 'roles' ][ 1 ]" />-->
			<!-- Add the <xformsdb:attachment> element for the data to be sent back within the XFormsDB query request. For update query instances only. -->
			<xsl:if test="$pXFormsDBInstances/xformsdb:instance[ $pXFormsDBInstanceIndex ]/xformsdb:query/xformsdb:attachment">
				<xsl:variable name="xformsdbAttachmentElementName">
					<xsl:choose>
						<xsl:when test="string-length( name( namespace::*[ . = 'http://www.tml.tkk.fi/2007/xformsdb' ] ) ) > 0">
							<xsl:value-of select="name( namespace::*[ . = 'http://www.tml.tkk.fi/2007/xformsdb' ] )" /><xsl:text>:attachment</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>attachment</xsl:text>
						</xsl:otherwise>
					</xsl:choose>				
				</xsl:variable>
				<xsl:variable name="xformsdbAttachmentElementNamespaceURI">
					<xsl:value-of select="'http://www.tml.tkk.fi/2007/xformsdb'" />
				</xsl:variable>
				<xsl:element name="{ $xformsdbAttachmentElementName }" namespace="{ $xformsdbAttachmentElementNamespaceURI }" />
		 	</xsl:if>
		</xsl:element>
	</xsl:template>


	<!-- Transform the <xformsdb:widgetquery> element -->
	<xsl:template name="transformXFormsDBWidgetQuery">
		<!-- Define the parameters -->
		<xsl:param name="pXFormsDBInstances" />
		<xsl:param name="pXFormsDBInstanceIndex" />

		<!-- Add the element -->
		<xsl:variable name="xformsdbWidgetQueryElementName">
			<xsl:choose>
				<xsl:when test="string-length( name( namespace::*[ . = 'http://www.tml.tkk.fi/2007/xformsdb' ] ) ) > 0">
					<xsl:value-of select="name( namespace::*[ . = 'http://www.tml.tkk.fi/2007/xformsdb' ] )" /><xsl:text>:widgetquery</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>widgetquery</xsl:text>
				</xsl:otherwise>
			</xsl:choose>				
		</xsl:variable>
		<xsl:variable name="xformsdbWidgetQueryElementNamespaceURI">
			<xsl:value-of select="'http://www.tml.tkk.fi/2007/xformsdb'" />
		</xsl:variable>
		<xsl:element name="{ $xformsdbWidgetQueryElementName }" namespace="{ $xformsdbWidgetQueryElementNamespaceURI }">
 			<!-- Add the transformed/correct id attribute -->
			<xsl:if test="$pXFormsDBInstances/xformsdb:instance[ $pXFormsDBInstanceIndex ]/xformsdb:widgetquery/@id">
	 			<xsl:attribute name="id">
	  				<xsl:value-of select="$pXFormsDBInstances/xformsdb:instance[ $pXFormsDBInstanceIndex ]/xformsdb:widgetquery/@id" />
				</xsl:attribute>
			</xsl:if>
 			<!-- Copy the <xformsdb:xmlns> elements -->
			<!--<xsl:apply-templates select="./xformsdb:widgetquery/xformsdb:xmlns" />-->
 			<!-- Copy the <xformsdb:var> elements -->
			<xsl:apply-templates select="./xformsdb:widgetquery/xformsdb:var" />
 			<!-- Copy the <xformsdb:secvar> elements -->
			<!--<xsl:apply-templates select="./xformsdb:widgetquery/xformsdb:secvar[ @name = 'username' ][ 1 ]" />-->
			<!--<xsl:apply-templates select="./xformsdb:widgetquery/xformsdb:secvar[ @name = 'roles' ][ 1 ]" />-->
			<!-- Add the <xformsdb:attachment> element for the data to be sent back within the XFormsDB widgetquery request. For update widgetquery instances only. -->
			<xsl:if test="$pXFormsDBInstances/xformsdb:instance[ $pXFormsDBInstanceIndex ]/xformsdb:widgetquery/xformsdb:attachment">
				<xsl:variable name="xformsdbAttachmentElementName">
					<xsl:choose>
						<xsl:when test="string-length( name( namespace::*[ . = 'http://www.tml.tkk.fi/2007/xformsdb' ] ) ) > 0">
							<xsl:value-of select="name( namespace::*[ . = 'http://www.tml.tkk.fi/2007/xformsdb' ] )" /><xsl:text>:attachment</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>attachment</xsl:text>
						</xsl:otherwise>
					</xsl:choose>				
				</xsl:variable>
				<xsl:variable name="xformsdbAttachmentElementNamespaceURI">
					<xsl:value-of select="'http://www.tml.tkk.fi/2007/xformsdb'" />
				</xsl:variable>
				<xsl:element name="{ $xformsdbAttachmentElementName }" namespace="{ $xformsdbAttachmentElementNamespaceURI }" />
		 	</xsl:if>
		</xsl:element>
	</xsl:template>


	<!-- Transform the <xformsdb:state> element -->
	<xsl:template name="transformXFormsDBState">
		<!-- Define the parameters -->
		<xsl:param name="pXFormsDBInstances" />
		<xsl:param name="pXFormsDBInstanceIndex" />

		<!-- Add the element -->
		<xsl:variable name="xformsdbStateElementName">
			<xsl:choose>
				<xsl:when test="string-length( name( namespace::*[ . = 'http://www.tml.tkk.fi/2007/xformsdb' ] ) ) > 0">
					<xsl:value-of select="name( namespace::*[ . = 'http://www.tml.tkk.fi/2007/xformsdb' ] )" /><xsl:text>:state</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>state</xsl:text>
				</xsl:otherwise>
			</xsl:choose>				
		</xsl:variable>
		<xsl:variable name="xformsdbStateElementNamespaceURI">
			<xsl:value-of select="'http://www.tml.tkk.fi/2007/xformsdb'" />
		</xsl:variable>
		<xsl:element name="{ $xformsdbStateElementName }" namespace="{ $xformsdbStateElementNamespaceURI }">
			<!-- Add the <xformsdb:attachment> element for the data to be sent back within the XFormsDB state request -->
			<xsl:if test="$pXFormsDBInstances/xformsdb:instance[ $pXFormsDBInstanceIndex ]/xformsdb:state/xformsdb:attachment">
				<xsl:variable name="xformsdbAttachmentElementName">
					<xsl:choose>
						<xsl:when test="string-length( name( namespace::*[ . = 'http://www.tml.tkk.fi/2007/xformsdb' ] ) ) > 0">
							<xsl:value-of select="name( namespace::*[ . = 'http://www.tml.tkk.fi/2007/xformsdb' ] )" /><xsl:text>:attachment</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>attachment</xsl:text>
						</xsl:otherwise>
					</xsl:choose>				
				</xsl:variable>
				<xsl:variable name="xformsdbAttachmentElementNamespaceURI">
					<xsl:value-of select="'http://www.tml.tkk.fi/2007/xformsdb'" />
				</xsl:variable>
				<xsl:element name="{ $xformsdbAttachmentElementName }" namespace="{ $xformsdbAttachmentElementNamespaceURI }" />
		 	</xsl:if>
		</xsl:element>
	</xsl:template>
	
	
	<!-- Transform the <xformsdb:login> element -->
	<xsl:template name="transformXFormsDBLogin">
		<!-- Define the parameters -->
		<xsl:param name="pXFormsDBInstances" />
		<xsl:param name="pXFormsDBInstanceIndex" />

		<!-- Add the element -->
		<xsl:variable name="xformsdbLoginElementName">
			<xsl:choose>
				<xsl:when test="string-length( name( namespace::*[ . = 'http://www.tml.tkk.fi/2007/xformsdb' ] ) ) > 0">
					<xsl:value-of select="name( namespace::*[ . = 'http://www.tml.tkk.fi/2007/xformsdb' ] )" /><xsl:text>:login</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>login</xsl:text>
				</xsl:otherwise>
			</xsl:choose>				
		</xsl:variable>
		<xsl:variable name="xformsdbLoginElementNamespaceURI">
			<xsl:value-of select="'http://www.tml.tkk.fi/2007/xformsdb'" />
		</xsl:variable>
		<xsl:element name="{ $xformsdbLoginElementName }" namespace="{ $xformsdbLoginElementNamespaceURI }">
 			<!-- Add the transformed/correct id attribute -->
			<xsl:if test="$pXFormsDBInstances/xformsdb:instance[ $pXFormsDBInstanceIndex ]/xformsdb:login/@id">
	 			<xsl:attribute name="id">
	  				<xsl:value-of select="$pXFormsDBInstances/xformsdb:instance[ $pXFormsDBInstanceIndex ]/xformsdb:login/@id" />
				</xsl:attribute>
			</xsl:if>
 			<!-- Copy the <xformsdb:var> elements -->
			<xsl:apply-templates select="./xformsdb:login/xformsdb:var[ @name = 'username' ][ 1 ]" />
			<xsl:apply-templates select="./xformsdb:login/xformsdb:var[ @name = 'password' ][ 1 ]" />
		</xsl:element>
	</xsl:template>


	<!-- Transform the <xformsdb:logout> element -->
	<xsl:template name="transformXFormsDBLogout">
		<!-- Define the parameters -->
		<xsl:param name="pXFormsDBInstances" />
		<xsl:param name="pXFormsDBInstanceIndex" />

		<!-- Add the element -->
		<xsl:variable name="xformsdbLogoutElementName">
			<xsl:choose>
				<xsl:when test="string-length( name( namespace::*[ . = 'http://www.tml.tkk.fi/2007/xformsdb' ] ) ) > 0">
					<xsl:value-of select="name( namespace::*[ . = 'http://www.tml.tkk.fi/2007/xformsdb' ] )" /><xsl:text>:logout</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>logout</xsl:text>
				</xsl:otherwise>
			</xsl:choose>				
		</xsl:variable>
		<xsl:variable name="xformsdbLogoutElementNamespaceURI">
			<xsl:value-of select="'http://www.tml.tkk.fi/2007/xformsdb'" />
		</xsl:variable>
		<xsl:element name="{ $xformsdbLogoutElementName }" namespace="{ $xformsdbLogoutElementNamespaceURI }">
		</xsl:element>
	</xsl:template>
		
	
	<!-- Transform the <xformsdb:user> element -->
	<xsl:template name="transformXFormsDBUser">
		<!-- Define the parameters -->
		<xsl:param name="pXFormsDBInstances" />
		<xsl:param name="pXFormsDBInstanceIndex" />

		<!-- Add the element -->
		<xsl:variable name="xformsdbUserElementName">
			<xsl:choose>
				<xsl:when test="string-length( name( namespace::*[ . = 'http://www.tml.tkk.fi/2007/xformsdb' ] ) ) > 0">
					<xsl:value-of select="name( namespace::*[ . = 'http://www.tml.tkk.fi/2007/xformsdb' ] )" /><xsl:text>:user</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>user</xsl:text>
				</xsl:otherwise>
			</xsl:choose>				
		</xsl:variable>
		<xsl:variable name="xformsdbUserElementNamespaceURI">
			<xsl:value-of select="'http://www.tml.tkk.fi/2007/xformsdb'" />
		</xsl:variable>
		<xsl:element name="{ $xformsdbUserElementName }" namespace="{ $xformsdbUserElementNamespaceURI }">
		</xsl:element>
	</xsl:template>
	

	<!-- Transform the <xformsdb:file> element -->
	<xsl:template name="transformXFormsDBFile">
		<!-- Define the parameters -->
		<xsl:param name="pXFormsDBInstances" />
		<xsl:param name="pXFormsDBInstanceIndex" />

		<!-- Add the element -->
		<xsl:variable name="xformsdbFileElementName">
			<xsl:choose>
				<xsl:when test="string-length( name( namespace::*[ . = 'http://www.tml.tkk.fi/2007/xformsdb' ] ) ) > 0">
					<xsl:value-of select="name( namespace::*[ . = 'http://www.tml.tkk.fi/2007/xformsdb' ] )" /><xsl:text>:file</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>file</xsl:text>
				</xsl:otherwise>
			</xsl:choose>				
		</xsl:variable>
		<xsl:variable name="xformsdbFileElementNamespaceURI">
			<xsl:value-of select="'http://www.tml.tkk.fi/2007/xformsdb'" />
		</xsl:variable>
		<xsl:element name="{ $xformsdbFileElementName }" namespace="{ $xformsdbFileElementNamespaceURI }">
 			<!-- Add the transformed/correct id attribute -->
			<xsl:if test="$pXFormsDBInstances/xformsdb:instance[ $pXFormsDBInstanceIndex ]/xformsdb:file/@id">
	 			<xsl:attribute name="id">
	  				<xsl:value-of select="$pXFormsDBInstances/xformsdb:instance[ $pXFormsDBInstanceIndex ]/xformsdb:file/@id" />
				</xsl:attribute>
			</xsl:if>
 			<!-- Copy the <xformsdb:var> elements -->
			<xsl:apply-templates select="./xformsdb:file/xformsdb:var[ @name = 'username' ][ 1 ]" />
			<xsl:apply-templates select="./xformsdb:file/xformsdb:var[ @name = 'ids' ][ 1 ]" />
			<xsl:apply-templates select="./xformsdb:file/xformsdb:var[ @name = 'roles' ][ 1 ]" />
			<!-- Copy the <xformsdb:secvar> elements -->
			<!--<xsl:apply-templates select="./xformsdb:file/xformsdb:secvar[ @name = 'username' ][ 1 ]" />-->
			<!--<xsl:apply-templates select="./xformsdb:file/xformsdb:secvar[ @name = 'roles' ][ 1 ]" />-->
			<!-- Add the <xformsdb:attachment> element for the data to be sent back within the XFormsDB file request. For insert and delete file instances only. -->
			<xsl:if test="$pXFormsDBInstances/xformsdb:instance[ $pXFormsDBInstanceIndex ]/xformsdb:file/xformsdb:attachment">
				<xsl:variable name="xformsdbAttachmentElementName">
					<xsl:choose>
						<xsl:when test="string-length( name( namespace::*[ . = 'http://www.tml.tkk.fi/2007/xformsdb' ] ) ) > 0">
							<xsl:value-of select="name( namespace::*[ . = 'http://www.tml.tkk.fi/2007/xformsdb' ] )" /><xsl:text>:attachment</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>attachment</xsl:text>
						</xsl:otherwise>
					</xsl:choose>				
				</xsl:variable>
				<xsl:variable name="xformsdbAttachmentElementNamespaceURI">
					<xsl:value-of select="'http://www.tml.tkk.fi/2007/xformsdb'" />
				</xsl:variable>
				<xsl:element name="{ $xformsdbAttachmentElementName }" namespace="{ $xformsdbAttachmentElementNamespaceURI }" />
		 	</xsl:if>
		</xsl:element>
	</xsl:template>


	<!-- Transform the <xformsdb:cookie> element -->
	<xsl:template name="transformXFormsDBCookie">
		<!-- Define the parameters -->
		<xsl:param name="pXFormsDBInstances" />
		<xsl:param name="pXFormsDBInstanceIndex" />

		<!-- Add the element -->
		<xsl:variable name="xformsdbCookieElementName">
			<xsl:choose>
				<xsl:when test="string-length( name( namespace::*[ . = 'http://www.tml.tkk.fi/2007/xformsdb' ] ) ) > 0">
					<xsl:value-of select="name( namespace::*[ . = 'http://www.tml.tkk.fi/2007/xformsdb' ] )" /><xsl:text>:cookie</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>cookie</xsl:text>
				</xsl:otherwise>
			</xsl:choose>				
		</xsl:variable>
		<xsl:variable name="xformsdbCookieElementNamespaceURI">
			<xsl:value-of select="'http://www.tml.tkk.fi/2007/xformsdb'" />
		</xsl:variable>
		<xsl:element name="{ $xformsdbCookieElementName }" namespace="{ $xformsdbCookieElementNamespaceURI }">
		</xsl:element>
	</xsl:template>

	
	<!-- Transform the <xformsdb:submission> element -->
	<xsl:template match="/xhtml:html/xhtml:head/xforms:model/xformsdb:submission">
		<!-- Parse the XML document -->
 		<xsl:variable name="varXFormsDBSubmissions" select="saxon:parse( $paramXFormsDBSubmissionsXMLString )/xformsdb:submissions" />
		<!-- Calculate the index/position of the matched <xformsdb:submission> element -->
 		<xsl:variable name="varXFormsDBSubmissionIndex" select="count( preceding::xformsdb:submission ) + 1" />

		<!-- Transform the <xformsdb:submission> element --> 
		<!-- Rename the element after the parent element -->
		<xsl:variable name="xformsSubmissionElementName">
			<xsl:choose>
				<xsl:when test="string-length( name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] ) ) > 0">
					<xsl:value-of select="name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] )" /><xsl:text>:submission</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>submission</xsl:text>
				</xsl:otherwise>
			</xsl:choose>				
		</xsl:variable>
		<xsl:variable name="xformsSubmissionElementNamespaceURI">
			<xsl:value-of select="'http://www.w3.org/2002/xforms'" />
		</xsl:variable>
		
		<!-- BEGIN: Hack: Create a dummy submission in order to receive information about the file to be uploaded from Orbeon Forms -->
		<xsl:if test="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@xformsdbrequesttype = 'file' and ( $varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@filetype = 'insert' or $varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@filetype = 'update' )">
			<!-- Rename the element after the parent element -->
			<xsl:variable name="xformsSubmissionDummyElementName">
				<xsl:choose>
					<xsl:when test="string-length( name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] ) ) > 0">
						<xsl:value-of select="name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] )" /><xsl:text>:submission</xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<xsl:text>submission</xsl:text>
					</xsl:otherwise>
				</xsl:choose>				
			</xsl:variable>
			<xsl:variable name="xformsSubmissionDummyElementNamespaceURI">
				<xsl:value-of select="'http://www.w3.org/2002/xforms'" />
			</xsl:variable>
			<xsl:element name="{ $xformsSubmissionDummyElementName }" namespace="{ $xformsSubmissionDummyElementNamespaceURI }">
				<!-- Iterate over all attributes -->
	 			<xsl:for-each select="@*">
	 				<xsl:choose>
		 				<!-- Remove the untransformed/incorrect id attribute -->
		 				<xsl:when test="name() = 'id'">
		 				</xsl:when>
		 				<!-- Remove the untransformed/incorrect model attribute -->
		 				<xsl:when test="name() = 'model'">
		 				</xsl:when>
		 				<!-- Remove the untransformed/incorrect xformsdbrequesttype attribute -->
		 				<xsl:when test="name() = 'xformsdbrequesttype'">
		 				</xsl:when>
		 				<!-- Remove the untransformed/incorrect replacetype attribute -->
		 				<xsl:when test="name() = 'replacetype'">
		 				</xsl:when>
		 				<!-- Remove the untransformed/incorrect xformsinsertorigin attribute -->
		 				<xsl:when test="name() = 'xformsinsertorigin'">
		 				</xsl:when>
		 				<!-- Remove the untransformed/incorrect xformsinsertcontext attribute -->
		 				<xsl:when test="name() = 'xformsinsertcontext'">
		 				</xsl:when>
		 				<!-- Remove the untransformed/incorrect replace attribute -->
		 				<xsl:when test="name() = 'replace'">
		 				</xsl:when>
		 				<!-- Remove the untransformed/incorrect instance attribute -->
		 				<xsl:when test="name() = 'instance'">
		 				</xsl:when>
		 				<!-- Remove the untransformed/incorrect ref attribute -->
		 				<xsl:when test="name() = 'ref'">
		 				</xsl:when>
		 				<!-- Remove the untransformed/incorrect includenamespaceprefixes attribute -->
		 				<xsl:when test="name() = 'includenamespaceprefixes'">
		 				</xsl:when>
		 				<!-- Remove the untransformed/incorrect action attribute -->
		 				<xsl:when test="name() = 'action'">
		 				</xsl:when>
		 				<!-- Remove the untransformed/incorrect method attribute -->
		 				<xsl:when test="name() = 'method'">
		 				</xsl:when>
		 				<!-- Remove the untransformed/incorrect mediatype attribute -->
		 				<xsl:when test="name() = 'mediatype'">
		 				</xsl:when>
		 				<!-- Remove the untransformed/incorrect encoding attribute -->
		 				<xsl:when test="name() = 'encoding'">
		 				</xsl:when>
		 				<!-- Remove the untransformed/incorrect requestinstance attribute -->
		 				<xsl:when test="name() = 'requestinstance'">
		 				</xsl:when>
		 				<!-- Remove the untransformed/incorrect expressiontype attribute -->
		 				<xsl:when test="name() = 'expressiontype'">
		 				</xsl:when>
		 				<!-- Remove the untransformed/incorrect attachmentinstance attribute -->
		 				<xsl:when test="name() = 'attachmentinstance'">
		 				</xsl:when>
		 				<!-- Remove the untransformed/incorrect statetype attribute -->
		 				<xsl:when test="name() = 'statetype'">
		 				</xsl:when>
		 				<!-- Remove the untransformed/incorrect filetype attribute -->
		 				<xsl:when test="name() = 'filetype'">
		 				</xsl:when>
		 				<!-- Copy other attributes -->
		 				<xsl:otherwise>
							<xsl:attribute name="{ name() }">
								<xsl:value-of select="." />
							</xsl:attribute>
		 				</xsl:otherwise>
		 			</xsl:choose>
	 			</xsl:for-each>
				<!-- Add the transformed/correct id attribute -->
				<xsl:if test="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@id">
					<xsl:attribute name="id">
						<xsl:value-of select="concat( 'xformsdb-', $varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@id )" />
					</xsl:attribute>
				</xsl:if>
				<!-- Add the transformed/correct replace attribute -->
				<xsl:if test="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@replace">
					<xsl:attribute name="replace">
						<xsl:value-of select="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@replace" />
					</xsl:attribute>
				</xsl:if>
				<!-- Add the transformed/correct instance attribute -->
				<xsl:if test="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@instance">
					<xsl:attribute name="instance">
						<xsl:value-of select="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@instance" />
					</xsl:attribute>
				</xsl:if>
				<!-- Add the transformed/correct ref attribute -->
				<xsl:if test="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@uploadref">
					<xsl:attribute name="ref">
						<xsl:value-of select="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@uploadref" />
					</xsl:attribute>
				</xsl:if>
				<!-- Add the transformed/correct includenamespaceprefixes attribute -->
				<xsl:if test="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@includenamespaceprefixes">
					<xsl:attribute name="includenamespaceprefixes">
						<xsl:value-of select="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@includenamespaceprefixes" />
					</xsl:attribute>
				</xsl:if>
				<!-- Add the transformed/correct action attribute -->
				<xsl:if test="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@uploadaction">
					<xsl:attribute name="action">
						<xsl:value-of select="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@uploadaction" />
					</xsl:attribute>
				</xsl:if>
				<!-- Add the transformed/correct method attribute -->
				<xsl:if test="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@method">
					<xsl:attribute name="method">
						<xsl:value-of select="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@method" />
					</xsl:attribute>
				</xsl:if>
				<!-- Add the transformed/correct mediatype attribute -->
				<xsl:if test="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@mediatype">
					<xsl:attribute name="mediatype">
						<xsl:value-of select="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@mediatype" />
					</xsl:attribute>
				</xsl:if>
				<!-- Add the transformed/correct encoding attribute -->
				<xsl:if test="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@encoding">
					<xsl:attribute name="encoding">
						<xsl:value-of select="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@encoding" />
					</xsl:attribute>
				</xsl:if>
				<!-- Add transformed/corrected actions if needed -->
				<xsl:if test="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@replacetype = 'instance' or $varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@replacetype = 'none'">
					<!-- ::::::::::::: ACTION ELEMENT ::::::::::::: -->
					<!-- Submission error: either submission or request execution failed -->
					<!-- Rename the element after the matched element -->
					<xsl:variable name="actionElementName">
						<xsl:choose>
							<xsl:when test="string-length( name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] ) ) > 0">
								<xsl:value-of select="name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] )" /><xsl:text>:action</xsl:text>
							</xsl:when>
							<xsl:otherwise>
								<xsl:text>action</xsl:text>
							</xsl:otherwise>
						</xsl:choose>				
					</xsl:variable>
					<xsl:variable name="actionElementNamespaceURI">
						<xsl:value-of select="'http://www.w3.org/2002/xforms'" />
					</xsl:variable>
					<xsl:element name="{ $actionElementName }" namespace="{ $actionElementNamespaceURI }">
						<!-- Add the event attribute -->
						<xsl:attribute name="event" namespace="http://www.w3.org/2001/xml-events">
							<xsl:value-of select="$paramXFormsDBRequestErrorEvent" />
						</xsl:attribute>
						<!-- ::::::::::::: DELETE ELEMENT ::::::::::::: -->
						<!-- Delete an old xformsdb:error element (if any) from the request instance -->
						<!-- Rename the element after the matched element -->
						<xsl:variable name="deleteElementName">
							<xsl:choose>
								<xsl:when test="string-length( name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] ) ) > 0">
									<xsl:value-of select="name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] )" /><xsl:text>:delete</xsl:text>
								</xsl:when>
								<xsl:otherwise>
									<xsl:text>delete</xsl:text>
								</xsl:otherwise>
							</xsl:choose>				
						</xsl:variable>
						<xsl:variable name="deleteElementNamespaceURI">
							<xsl:value-of select="'http://www.w3.org/2002/xforms'" />
						</xsl:variable>
						<xsl:element name="{ $deleteElementName }" namespace="{ $deleteElementNamespaceURI }">
							<!-- Add the transformed/correct model attribute -->
							<xsl:if test="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@model">
								<xsl:attribute name="model">
									<xsl:value-of select="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@model" />
								</xsl:attribute>
							</xsl:if>
							<!-- Add the transformed/correct context attribute -->
							<xsl:if test="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@ref">
								<xsl:attribute name="context">
									<xsl:value-of select="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@ref" />
								</xsl:attribute>
							</xsl:if>
							<!-- Add the nodeset attribute -->
							<xsl:attribute name="nodeset">
								<xsl:value-of select="$paramXFormsDBErrorElement" />
							</xsl:attribute>
							<!-- Add the at attribute -->
							<xsl:attribute name="at">
								<xsl:text>last()</xsl:text>
							</xsl:attribute>
						</xsl:element>
						<!-- ::::::::::::: INSERT ELEMENT ::::::::::::: -->
						<!-- Append a new xformsdb:error element from the xformsdb-response-proxy-instance instance to the end of the request instance -->
						<!-- Rename the element after the matched element -->
						<xsl:variable name="insertElementName">
							<xsl:choose>
								<xsl:when test="string-length( name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] ) ) > 0">
									<xsl:value-of select="name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] )" /><xsl:text>:insert</xsl:text>
								</xsl:when>
								<xsl:otherwise>
									<xsl:text>insert</xsl:text>
								</xsl:otherwise>
							</xsl:choose>				
						</xsl:variable>
						<xsl:variable name="insertElementNamespaceURI">
							<xsl:value-of select="'http://www.w3.org/2002/xforms'" />
						</xsl:variable>
						<xsl:element name="{ $insertElementName }" namespace="{ $insertElementNamespaceURI }">
							<!-- Add the transformed/correct model attribute -->
							<xsl:if test="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@model">
								<xsl:attribute name="model">
									<xsl:value-of select="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@model" />
								</xsl:attribute>
							</xsl:if>
							<!-- Add the origin attribute -->
							<xsl:attribute name="origin">
								<xsl:text>instance( '</xsl:text>
								<xsl:value-of select="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@proxyinstance" />
								<xsl:text>' )</xsl:text>
							</xsl:attribute>
							<!-- Add the transformed/correct context attribute -->
							<xsl:if test="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@ref">
								<xsl:attribute name="context">
									<xsl:value-of select="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@ref" />
								</xsl:attribute>
							</xsl:if>
							<!-- Add the nodeset attribute -->
							<xsl:attribute name="nodeset">
								<xsl:text>*</xsl:text>
							</xsl:attribute>
							<!-- Add the at attribute -->
							<xsl:attribute name="at">
								<xsl:text>last()</xsl:text>
							</xsl:attribute>
						</xsl:element>
						<!-- Apply further templates i.e. copy user defined actions -->
						<xsl:apply-templates select="./xforms:action[ @ev:event = $paramXFormsDBRequestErrorEvent ]/*" />
					</xsl:element>
					<!-- ::::::::::::: ACTION ELEMENT ::::::::::::: -->
					<!-- Submission (and request execution) failed -->
					<!-- Rename the element after the matched element -->
					<xsl:variable name="actionElementName">
						<xsl:choose>
							<xsl:when test="string-length( name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] ) ) > 0">
								<xsl:value-of select="name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] )" /><xsl:text>:action</xsl:text>
							</xsl:when>
							<xsl:otherwise>
								<xsl:text>action</xsl:text>
							</xsl:otherwise>
						</xsl:choose>				
					</xsl:variable>
					<xsl:variable name="actionElementNamespaceURI">
						<xsl:value-of select="'http://www.w3.org/2002/xforms'" />
					</xsl:variable>
					<xsl:element name="{ $actionElementName }" namespace="{ $actionElementNamespaceURI }">
						<!-- Add the event attribute -->
						<xsl:attribute name="event" namespace="http://www.w3.org/2001/xml-events">
							<xsl:text>xforms-submit-error</xsl:text>
						</xsl:attribute>
						<!-- ::::::::::::: DISPATCH ELEMENT ::::::::::::: -->
						<!-- Dispatch xformsdb-request-error to the submission element -->
						<!-- Rename the element after the matched element -->
						<xsl:variable name="dispatchElementName">
							<xsl:choose>
								<xsl:when test="string-length( name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] ) ) > 0">
									<xsl:value-of select="name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] )" /><xsl:text>:dispatch</xsl:text>
								</xsl:when>
								<xsl:otherwise>
									<xsl:text>dispatch</xsl:text>
								</xsl:otherwise>
							</xsl:choose>				
						</xsl:variable>
						<xsl:variable name="dispatchElementNamespaceURI">
							<xsl:value-of select="'http://www.w3.org/2002/xforms'" />
						</xsl:variable>
						<xsl:element name="{ $dispatchElementName }" namespace="{ $dispatchElementNamespaceURI }">
							<!-- Add the name attribute -->
							<xsl:attribute name="name">
								<xsl:value-of select="$paramXFormsDBRequestErrorEvent" />
							</xsl:attribute>
							<!-- Add the transformed/correct target attribute -->
							<xsl:if test="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@id">
								<xsl:attribute name="target">
									<xsl:value-of select="concat( 'xformsdb-', $varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@id )" />
								</xsl:attribute>
							</xsl:if>
						</xsl:element>
					</xsl:element>
					<!-- ::::::::::::: ACTION ELEMENT ::::::::::::: -->
					<!-- Successful submission but request execution failed -->
					<!-- Rename the element after the matched element -->
					<xsl:variable name="actionElementName">
						<xsl:choose>
							<xsl:when test="string-length( name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] ) ) > 0">
								<xsl:value-of select="name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] )" /><xsl:text>:action</xsl:text>
							</xsl:when>
							<xsl:otherwise>
								<xsl:text>action</xsl:text>
							</xsl:otherwise>
						</xsl:choose>				
					</xsl:variable>
					<xsl:variable name="actionElementNamespaceURI">
						<xsl:value-of select="'http://www.w3.org/2002/xforms'" />
					</xsl:variable>
					<xsl:element name="{ $actionElementName }" namespace="{ $actionElementNamespaceURI }">
						<!-- Add the if attribute -->
						<xsl:attribute name="if">
							<xsl:text>name( instance( '</xsl:text>
							<xsl:value-of select="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@proxyinstance" />
							<xsl:text>' ) ) = '</xsl:text>
							<xsl:value-of select="$paramXFormsDBErrorElement" />
							<xsl:text>'</xsl:text>
						</xsl:attribute>
		 				<!-- Add the event attribute -->
						<xsl:attribute name="event" namespace="http://www.w3.org/2001/xml-events">
							<xsl:text>xforms-submit-done</xsl:text>
						</xsl:attribute>
						<!-- ::::::::::::: DISPATCH ELEMENT ::::::::::::: -->
						<!-- Dispatch xformsdb-request-error to the submission element -->
						<!-- Rename the element after the matched element -->
						<xsl:variable name="dispatchElementName">
							<xsl:choose>
								<xsl:when test="string-length( name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] ) ) > 0">
									<xsl:value-of select="name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] )" /><xsl:text>:dispatch</xsl:text>
								</xsl:when>
								<xsl:otherwise>
									<xsl:text>dispatch</xsl:text>
								</xsl:otherwise>
							</xsl:choose>				
						</xsl:variable>
						<xsl:variable name="dispatchElementNamespaceURI">
							<xsl:value-of select="'http://www.w3.org/2002/xforms'" />
						</xsl:variable>
						<xsl:element name="{ $dispatchElementName }" namespace="{ $dispatchElementNamespaceURI }">
							<!-- Add the name attribute -->
							<xsl:attribute name="name">
								<xsl:value-of select="$paramXFormsDBRequestErrorEvent" />
							</xsl:attribute>
							<!-- Add the transformed/correct target attribute -->
							<xsl:if test="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@id">
								<xsl:attribute name="target">
									<xsl:value-of select="concat( 'xformsdb-', $varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@id )" />
								</xsl:attribute>
							</xsl:if>
						</xsl:element>
					</xsl:element>
					<!-- ::::::::::::: ACTION ELEMENT ::::::::::::: -->
					<!-- Successful submission and request execution -->
					<!-- Rename the element after the matched element -->
					<xsl:variable name="actionElementName">
						<xsl:choose>
							<xsl:when test="string-length( name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] ) ) > 0">
								<xsl:value-of select="name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] )" /><xsl:text>:action</xsl:text>
							</xsl:when>
							<xsl:otherwise>
								<xsl:text>action</xsl:text>
							</xsl:otherwise>
						</xsl:choose>				
					</xsl:variable>
					<xsl:variable name="actionElementNamespaceURI">
						<xsl:value-of select="'http://www.w3.org/2002/xforms'" />
					</xsl:variable>
					<xsl:element name="{ $actionElementName }" namespace="{ $actionElementNamespaceURI }">
						<!-- Add the if attribute -->
						<xsl:attribute name="if">
							<xsl:text>name( instance( '</xsl:text>
							<xsl:value-of select="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@proxyinstance" />
							<xsl:text>' ) ) != '</xsl:text>
							<xsl:value-of select="$paramXFormsDBErrorElement" />
							<xsl:text>'</xsl:text>
						</xsl:attribute>
		 				<!-- Add the event attribute -->
						<xsl:attribute name="event" namespace="http://www.w3.org/2001/xml-events">
							<xsl:text>xforms-submit-done</xsl:text>
						</xsl:attribute>
						<!-- ::::::::::::: DELETE ELEMENT ::::::::::::: -->
						<!-- Delete old xformsdb:error element (if any) from the request instance -->
						<!-- Rename the element after the matched element -->
						<xsl:variable name="deleteElementName">
							<xsl:choose>
								<xsl:when test="string-length( name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] ) ) > 0">
									<xsl:value-of select="name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] )" /><xsl:text>:delete</xsl:text>
								</xsl:when>
								<xsl:otherwise>
									<xsl:text>delete</xsl:text>
								</xsl:otherwise>
							</xsl:choose>				
						</xsl:variable>
						<xsl:variable name="deleteElementNamespaceURI">
							<xsl:value-of select="'http://www.w3.org/2002/xforms'" />
						</xsl:variable>
						<xsl:element name="{ $deleteElementName }" namespace="{ $deleteElementNamespaceURI }">
							<!-- Add the transformed/correct model attribute -->
							<xsl:if test="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@model">
								<xsl:attribute name="model">
									<xsl:value-of select="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@model" />
								</xsl:attribute>
							</xsl:if>
							<!-- Add the transformed/correct context attribute -->
							<xsl:if test="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@ref">
								<xsl:attribute name="context">
									<xsl:value-of select="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@ref" />
								</xsl:attribute>
							</xsl:if>
							<!-- Add the nodeset attribute -->
							<xsl:attribute name="nodeset">
								<xsl:value-of select="$paramXFormsDBErrorElement" />
							</xsl:attribute>
							<!-- Add the at attribute -->
							<xsl:attribute name="at">
								<xsl:text>last()</xsl:text>
							</xsl:attribute>
						</xsl:element>
						<!-- ::::::::::::: DELETE ELEMENT ::::::::::::: -->
						<!-- Add the delete element (to delete the previous child element of the attachment element) -->
						<!-- Rename the element after the matched element -->
						<xsl:variable name="deleteElementName">
							<xsl:choose>
								<xsl:when test="string-length( name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] ) ) > 0">
									<xsl:value-of select="name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] )" /><xsl:text>:delete</xsl:text>
								</xsl:when>
								<xsl:otherwise>
									<xsl:text>delete</xsl:text>
								</xsl:otherwise>
							</xsl:choose>				
						</xsl:variable>
						<xsl:variable name="deleteElementNamespaceURI">
							<xsl:value-of select="'http://www.w3.org/2002/xforms'" />
						</xsl:variable>
						<xsl:element name="{ $deleteElementName }" namespace="{ $deleteElementNamespaceURI }">
							<!-- Add the transformed/correct model attribute -->
							<xsl:if test="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@model">
								<xsl:attribute name="model">
									<xsl:value-of select="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@model" />
								</xsl:attribute>
							</xsl:if>
							<!-- Add the nodeset attribute -->
							<xsl:if test="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@ref">
								<xsl:attribute name="nodeset">
									<xsl:variable name="xformsdbAttachmentElementName">
										<xsl:choose>
											<xsl:when test="string-length( name( namespace::*[ . = 'http://www.tml.tkk.fi/2007/xformsdb' ] ) ) > 0">
												<xsl:value-of select="name( namespace::*[ . = 'http://www.tml.tkk.fi/2007/xformsdb' ] )" /><xsl:text>:attachment</xsl:text>
											</xsl:when>
											<xsl:otherwise>
												<xsl:text>attachment</xsl:text>
											</xsl:otherwise>
										</xsl:choose>				
									</xsl:variable>
									<xsl:value-of select="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@ref" />
									<xsl:text>/</xsl:text>
									<xsl:value-of select="$xformsdbAttachmentElementName" />
									<xsl:text>/*</xsl:text>
								</xsl:attribute>
							</xsl:if>
							<!-- Add the at attribute -->
							<xsl:attribute name="at">
								<xsl:text>1</xsl:text>
							</xsl:attribute>
						</xsl:element>
						<!-- ::::::::::::: INSERT ELEMENT ::::::::::::: -->
						<!-- Add the insert element (to insert the attachment instance data to the attachment element) -->
						<!-- Rename the element after the matched element -->
						<xsl:variable name="insertElementName">
							<xsl:choose>
								<xsl:when test="string-length( name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] ) ) > 0">
									<xsl:value-of select="name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] )" /><xsl:text>:insert</xsl:text>
								</xsl:when>
								<xsl:otherwise>
									<xsl:text>insert</xsl:text>
								</xsl:otherwise>
							</xsl:choose>				
						</xsl:variable>
						<xsl:variable name="insertElementNamespaceURI">
							<xsl:value-of select="'http://www.w3.org/2002/xforms'" />
						</xsl:variable>
						<xsl:element name="{ $insertElementName }" namespace="{ $insertElementNamespaceURI }">
							<!-- Add the transformed/correct model attribute -->
							<xsl:if test="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@model">
								<xsl:attribute name="model">
									<xsl:value-of select="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@model" />
								</xsl:attribute>
							</xsl:if>
							<!-- Add the context and nodeset attributes -->
							<xsl:if test="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@ref">
								<!-- Add the context attribute -->
								<xsl:attribute name="context">
									<xsl:variable name="xformsdbAttachmentElementName">
										<xsl:choose>
											<xsl:when test="string-length( name( namespace::*[ . = 'http://www.tml.tkk.fi/2007/xformsdb' ] ) ) > 0">
												<xsl:value-of select="name( namespace::*[ . = 'http://www.tml.tkk.fi/2007/xformsdb' ] )" /><xsl:text>:attachment</xsl:text>
											</xsl:when>
											<xsl:otherwise>
												<xsl:text>attachment</xsl:text>
											</xsl:otherwise>
										</xsl:choose>				
									</xsl:variable>
									<xsl:value-of select="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@ref" />
									<xsl:text>/</xsl:text>
									<xsl:value-of select="$xformsdbAttachmentElementName" />
								</xsl:attribute>
								<!-- Add the nodeset attribute -->
								<xsl:attribute name="nodeset">
									<xsl:text>*</xsl:text>
								</xsl:attribute>
							</xsl:if>
							<!-- Add the origin attribute -->
							<xsl:if test="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@xformsinsertorigin">
								<xsl:attribute name="origin">
									<xsl:value-of select="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@xformsinsertorigin" />
								</xsl:attribute>
							</xsl:if>
						</xsl:element>
						<!-- ::::::::::::: SEND ELEMENT ::::::::::::: -->
						<!-- Add the send element (to send the actual submission) -->
						<!-- Rename the element after the matched element -->
						<xsl:variable name="sendElementName">
							<xsl:choose>
								<xsl:when test="string-length( name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] ) ) > 0">
									<xsl:value-of select="name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] )" /><xsl:text>:send</xsl:text>
								</xsl:when>
								<xsl:otherwise>
									<xsl:text>send</xsl:text>
								</xsl:otherwise>
							</xsl:choose>				
						</xsl:variable>
						<xsl:variable name="sendElementNamespaceURI">
							<xsl:value-of select="'http://www.w3.org/2002/xforms'" />
						</xsl:variable>
						<xsl:element name="{ $sendElementName }" namespace="{ $sendElementNamespaceURI }">
							<!-- Add the submission attribute -->
							<xsl:if test="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@id">
								<xsl:attribute name="submission">
									<xsl:value-of select="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@id" />
								</xsl:attribute>
							</xsl:if>							
						</xsl:element>
					</xsl:element>
				</xsl:if>
			</xsl:element>
		</xsl:if>
		<!-- END: Hack: Create a dummy submission in order to receive information about the file to be uploaded from Orbeon Forms -->
		
		<!-- The actual <xformsdb:submission> element --> 
		<xsl:element name="{ $xformsSubmissionElementName }" namespace="{ $xformsSubmissionElementNamespaceURI }">
			<!-- Iterate over all attributes -->
 			<xsl:for-each select="@*">
 				<xsl:choose>
	 				<!-- Remove the untransformed/incorrect id attribute -->
	 				<xsl:when test="name() = 'id'">
	 				</xsl:when>
	 				<!-- Remove the untransformed/incorrect model attribute -->
	 				<xsl:when test="name() = 'model'">
	 				</xsl:when>
	 				<!-- Remove the untransformed/incorrect xformsdbrequesttype attribute -->
	 				<xsl:when test="name() = 'xformsdbrequesttype'">
	 				</xsl:when>
	 				<!-- Remove the untransformed/incorrect replacetype attribute -->
	 				<xsl:when test="name() = 'replacetype'">
	 				</xsl:when>
	 				<!-- Remove the untransformed/incorrect xformsinsertorigin attribute -->
	 				<xsl:when test="name() = 'xformsinsertorigin'">
	 				</xsl:when>
	 				<!-- Remove the untransformed/incorrect xformsinsertcontext attribute -->
	 				<xsl:when test="name() = 'xformsinsertcontext'">
	 				</xsl:when>
	 				<!-- Remove the untransformed/incorrect replace attribute -->
	 				<xsl:when test="name() = 'replace'">
	 				</xsl:when>
	 				<!-- Remove the untransformed/incorrect instance attribute -->
	 				<xsl:when test="name() = 'instance'">
	 				</xsl:when>
	 				<!-- Remove the untransformed/incorrect ref attribute -->
	 				<xsl:when test="name() = 'ref'">
	 				</xsl:when>
	 				<!-- Remove the untransformed/incorrect includenamespaceprefixes attribute -->
	 				<xsl:when test="name() = 'includenamespaceprefixes'">
	 				</xsl:when>
	 				<!-- Remove the untransformed/incorrect action attribute -->
	 				<xsl:when test="name() = 'action'">
	 				</xsl:when>
	 				<!-- Remove the untransformed/incorrect method attribute -->
	 				<xsl:when test="name() = 'method'">
	 				</xsl:when>
	 				<!-- Remove the untransformed/incorrect mediatype attribute -->
	 				<xsl:when test="name() = 'mediatype'">
	 				</xsl:when>
	 				<!-- Remove the untransformed/incorrect encoding attribute -->
	 				<xsl:when test="name() = 'encoding'">
	 				</xsl:when>
	 				<!-- Remove the untransformed/incorrect requestinstance attribute -->
	 				<xsl:when test="name() = 'requestinstance'">
	 				</xsl:when>
	 				<!-- Remove the untransformed/incorrect expressiontype attribute -->
	 				<xsl:when test="name() = 'expressiontype'">
	 				</xsl:when>
	 				<!-- Remove the untransformed/incorrect attachmentinstance attribute -->
	 				<xsl:when test="name() = 'attachmentinstance'">
	 				</xsl:when>
	 				<!-- Remove the untransformed/incorrect statetype attribute -->
	 				<xsl:when test="name() = 'statetype'">
	 				</xsl:when>
	 				<!-- Remove the untransformed/incorrect filetype attribute -->
	 				<xsl:when test="name() = 'filetype'">
	 				</xsl:when>
	 				<!-- Copy other attributes -->
	 				<xsl:otherwise>
						<xsl:attribute name="{ name() }">
							<xsl:value-of select="." />
						</xsl:attribute>
	 				</xsl:otherwise>
	 			</xsl:choose>
 			</xsl:for-each>
			<!-- Add the transformed/correct id attribute -->
			<xsl:if test="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@id">
				<xsl:attribute name="id">
					<xsl:value-of select="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@id" />
				</xsl:attribute>
			</xsl:if>
			<!-- Add the transformed/correct replace attribute -->
			<xsl:if test="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@replace">
				<xsl:attribute name="replace">
					<xsl:value-of select="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@replace" />
				</xsl:attribute>
			</xsl:if>
			<!-- Add the transformed/correct instance attribute -->
			<xsl:if test="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@instance">
				<xsl:attribute name="instance">
					<xsl:value-of select="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@instance" />
				</xsl:attribute>
			</xsl:if>
			<!-- Add the transformed/correct ref attribute -->
			<xsl:if test="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@ref">
				<xsl:attribute name="ref">
					<xsl:value-of select="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@ref" />
				</xsl:attribute>
			</xsl:if>
			<!-- Add the transformed/correct includenamespaceprefixes attribute -->
			<xsl:if test="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@includenamespaceprefixes">
				<xsl:attribute name="includenamespaceprefixes">
					<xsl:value-of select="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@includenamespaceprefixes" />
				</xsl:attribute>
			</xsl:if>
			<!-- Add the transformed/correct action attribute -->
			<xsl:if test="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@action">
				<xsl:attribute name="action">
					<xsl:value-of select="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@action" />
				</xsl:attribute>
			</xsl:if>
			<!-- Add the transformed/correct method attribute -->
			<xsl:if test="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@method">
				<xsl:attribute name="method">
					<xsl:value-of select="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@method" />
				</xsl:attribute>
			</xsl:if>
			<!-- Add the transformed/correct mediatype attribute -->
			<xsl:if test="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@mediatype">
				<xsl:attribute name="mediatype">
					<xsl:value-of select="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@mediatype" />
				</xsl:attribute>
			</xsl:if>
			<!-- Add the transformed/correct encoding attribute -->
			<xsl:if test="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@encoding">
				<xsl:attribute name="encoding">
					<xsl:value-of select="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@encoding" />
				</xsl:attribute>
			</xsl:if>
			<!-- Add transformed/corrected actions if needed -->
			<xsl:if test="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@replacetype = 'instance' or $varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@replacetype = 'none'">
				<!-- ::::::::::::: ACTION ELEMENT ::::::::::::: -->
				<!-- Submission error: either submission or request execution failed -->
				<!-- Rename the element after the matched element -->
				<xsl:variable name="actionElementName">
					<xsl:choose>
						<xsl:when test="string-length( name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] ) ) > 0">
							<xsl:value-of select="name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] )" /><xsl:text>:action</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>action</xsl:text>
						</xsl:otherwise>
					</xsl:choose>				
				</xsl:variable>
				<xsl:variable name="actionElementNamespaceURI">
					<xsl:value-of select="'http://www.w3.org/2002/xforms'" />
				</xsl:variable>
				<xsl:element name="{ $actionElementName }" namespace="{ $actionElementNamespaceURI }">
					<!-- Add the event attribute -->
					<xsl:attribute name="event" namespace="http://www.w3.org/2001/xml-events">
						<xsl:value-of select="$paramXFormsDBRequestErrorEvent" />
					</xsl:attribute>
					<!-- ::::::::::::: DELETE ELEMENT ::::::::::::: -->
					<!-- Delete an old xformsdb:error element (if any) from the request instance -->
					<!-- Rename the element after the matched element -->
					<xsl:variable name="deleteElementName">
						<xsl:choose>
							<xsl:when test="string-length( name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] ) ) > 0">
								<xsl:value-of select="name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] )" /><xsl:text>:delete</xsl:text>
							</xsl:when>
							<xsl:otherwise>
								<xsl:text>delete</xsl:text>
							</xsl:otherwise>
						</xsl:choose>				
					</xsl:variable>
					<xsl:variable name="deleteElementNamespaceURI">
						<xsl:value-of select="'http://www.w3.org/2002/xforms'" />
					</xsl:variable>
					<xsl:element name="{ $deleteElementName }" namespace="{ $deleteElementNamespaceURI }">
						<!-- Add the transformed/correct model attribute -->
						<xsl:if test="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@model">
							<xsl:attribute name="model">
								<xsl:value-of select="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@model" />
							</xsl:attribute>
						</xsl:if>
						<!-- Add the transformed/correct context attribute -->
						<xsl:if test="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@ref">
							<xsl:attribute name="context">
								<xsl:value-of select="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@ref" />
							</xsl:attribute>
						</xsl:if>
						<!-- Add the nodeset attribute -->
						<xsl:attribute name="nodeset">
							<xsl:value-of select="$paramXFormsDBErrorElement" />
						</xsl:attribute>
						<!-- Add the at attribute -->
						<xsl:attribute name="at">
							<xsl:text>last()</xsl:text>
						</xsl:attribute>
					</xsl:element>
					<!-- ::::::::::::: INSERT ELEMENT ::::::::::::: -->
					<!-- Append a new xformsdb:error element from the xformsdb-response-proxy-instance instance to the end of the request instance -->
					<!-- Rename the element after the matched element -->
					<xsl:variable name="insertElementName">
						<xsl:choose>
							<xsl:when test="string-length( name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] ) ) > 0">
								<xsl:value-of select="name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] )" /><xsl:text>:insert</xsl:text>
							</xsl:when>
							<xsl:otherwise>
								<xsl:text>insert</xsl:text>
							</xsl:otherwise>
						</xsl:choose>				
					</xsl:variable>
					<xsl:variable name="insertElementNamespaceURI">
						<xsl:value-of select="'http://www.w3.org/2002/xforms'" />
					</xsl:variable>
					<xsl:element name="{ $insertElementName }" namespace="{ $insertElementNamespaceURI }">
						<!-- Add the transformed/correct model attribute -->
						<xsl:if test="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@model">
							<xsl:attribute name="model">
								<xsl:value-of select="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@model" />
							</xsl:attribute>
						</xsl:if>
						<!-- Add the origin attribute -->
						<xsl:attribute name="origin">
							<xsl:text>instance( '</xsl:text>
							<xsl:value-of select="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@proxyinstance" />
							<xsl:text>' )</xsl:text>
						</xsl:attribute>
						<!-- Add the transformed/correct context attribute -->
						<xsl:if test="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@ref">
							<xsl:attribute name="context">
								<xsl:value-of select="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@ref" />
							</xsl:attribute>
						</xsl:if>
						<!-- Add the nodeset attribute -->
						<xsl:attribute name="nodeset">
							<xsl:text>*</xsl:text>
						</xsl:attribute>
						<!-- Add the at attribute -->
						<xsl:attribute name="at">
							<xsl:text>last()</xsl:text>
						</xsl:attribute>
					</xsl:element>
					<!-- Apply further templates i.e. copy user defined actions -->
					<xsl:apply-templates select="./xforms:action[ @ev:event = $paramXFormsDBRequestErrorEvent ]/*" />
				</xsl:element>
				<!-- ::::::::::::: ACTION ELEMENT ::::::::::::: -->
				<!-- Submission (and request execution) failed -->
				<!-- Rename the element after the matched element -->
				<xsl:variable name="actionElementName">
					<xsl:choose>
						<xsl:when test="string-length( name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] ) ) > 0">
							<xsl:value-of select="name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] )" /><xsl:text>:action</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>action</xsl:text>
						</xsl:otherwise>
					</xsl:choose>				
				</xsl:variable>
				<xsl:variable name="actionElementNamespaceURI">
					<xsl:value-of select="'http://www.w3.org/2002/xforms'" />
				</xsl:variable>
				<xsl:element name="{ $actionElementName }" namespace="{ $actionElementNamespaceURI }">
					<!-- Add the event attribute -->
					<xsl:attribute name="event" namespace="http://www.w3.org/2001/xml-events">
						<xsl:text>xforms-submit-error</xsl:text>
					</xsl:attribute>
					<!-- ::::::::::::: DISPATCH ELEMENT ::::::::::::: -->
					<!-- Dispatch xformsdb-request-error to the submission element -->
					<!-- Rename the element after the matched element -->
					<xsl:variable name="dispatchElementName">
						<xsl:choose>
							<xsl:when test="string-length( name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] ) ) > 0">
								<xsl:value-of select="name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] )" /><xsl:text>:dispatch</xsl:text>
							</xsl:when>
							<xsl:otherwise>
								<xsl:text>dispatch</xsl:text>
							</xsl:otherwise>
						</xsl:choose>				
					</xsl:variable>
					<xsl:variable name="dispatchElementNamespaceURI">
						<xsl:value-of select="'http://www.w3.org/2002/xforms'" />
					</xsl:variable>
					<xsl:element name="{ $dispatchElementName }" namespace="{ $dispatchElementNamespaceURI }">
						<!-- Add the name attribute -->
						<xsl:attribute name="name">
							<xsl:value-of select="$paramXFormsDBRequestErrorEvent" />
						</xsl:attribute>
						<!-- Add the transformed/correct target attribute -->
						<xsl:if test="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@id">
							<xsl:attribute name="target">
								<xsl:value-of select="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@id" />
							</xsl:attribute>
						</xsl:if>
					</xsl:element>
				</xsl:element>
				<!-- ::::::::::::: ACTION ELEMENT ::::::::::::: -->
				<!-- Successful submission but request execution failed -->
				<!-- Rename the element after the matched element -->
				<xsl:variable name="actionElementName">
					<xsl:choose>
						<xsl:when test="string-length( name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] ) ) > 0">
							<xsl:value-of select="name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] )" /><xsl:text>:action</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>action</xsl:text>
						</xsl:otherwise>
					</xsl:choose>				
				</xsl:variable>
				<xsl:variable name="actionElementNamespaceURI">
					<xsl:value-of select="'http://www.w3.org/2002/xforms'" />
				</xsl:variable>
				<xsl:element name="{ $actionElementName }" namespace="{ $actionElementNamespaceURI }">
					<!-- Add the if attribute -->
					<xsl:attribute name="if">
						<xsl:text>name( instance( '</xsl:text>
						<xsl:value-of select="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@proxyinstance" />
						<xsl:text>' ) ) = '</xsl:text>
						<xsl:value-of select="$paramXFormsDBErrorElement" />
						<xsl:text>'</xsl:text>
					</xsl:attribute>
	 				<!-- Add the event attribute -->
					<xsl:attribute name="event" namespace="http://www.w3.org/2001/xml-events">
						<xsl:text>xforms-submit-done</xsl:text>
					</xsl:attribute>
					<!-- ::::::::::::: DISPATCH ELEMENT ::::::::::::: -->
					<!-- Dispatch xformsdb-request-error to the submission element -->
					<!-- Rename the element after the matched element -->
					<xsl:variable name="dispatchElementName">
						<xsl:choose>
							<xsl:when test="string-length( name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] ) ) > 0">
								<xsl:value-of select="name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] )" /><xsl:text>:dispatch</xsl:text>
							</xsl:when>
							<xsl:otherwise>
								<xsl:text>dispatch</xsl:text>
							</xsl:otherwise>
						</xsl:choose>				
					</xsl:variable>
					<xsl:variable name="dispatchElementNamespaceURI">
						<xsl:value-of select="'http://www.w3.org/2002/xforms'" />
					</xsl:variable>
					<xsl:element name="{ $dispatchElementName }" namespace="{ $dispatchElementNamespaceURI }">
						<!-- Add the name attribute -->
						<xsl:attribute name="name">
							<xsl:value-of select="$paramXFormsDBRequestErrorEvent" />
						</xsl:attribute>
						<!-- Add the transformed/correct target attribute -->
						<xsl:if test="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@id">
							<xsl:attribute name="target">
								<xsl:value-of select="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@id" />
							</xsl:attribute>
						</xsl:if>
					</xsl:element>
				</xsl:element>
				<!-- ::::::::::::: ACTION ELEMENT ::::::::::::: -->
				<!-- Successful submission and request execution -->
				<!-- Rename the element after the matched element -->
				<xsl:variable name="actionElementName">
					<xsl:choose>
						<xsl:when test="string-length( name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] ) ) > 0">
							<xsl:value-of select="name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] )" /><xsl:text>:action</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>action</xsl:text>
						</xsl:otherwise>
					</xsl:choose>				
				</xsl:variable>
				<xsl:variable name="actionElementNamespaceURI">
					<xsl:value-of select="'http://www.w3.org/2002/xforms'" />
				</xsl:variable>
				<xsl:element name="{ $actionElementName }" namespace="{ $actionElementNamespaceURI }">
					<!-- Add the if attribute -->
					<xsl:attribute name="if">
						<xsl:text>name( instance( '</xsl:text>
						<xsl:value-of select="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@proxyinstance" />
						<xsl:text>' ) ) != '</xsl:text>
						<xsl:value-of select="$paramXFormsDBErrorElement" />
						<xsl:text>'</xsl:text>
					</xsl:attribute>
	 				<!-- Add the event attribute -->
					<xsl:attribute name="event" namespace="http://www.w3.org/2001/xml-events">
						<xsl:text>xforms-submit-done</xsl:text>
					</xsl:attribute>
					<!-- ::::::::::::: DELETE ELEMENT ::::::::::::: -->
					<!-- Delete old xformsdb:error element (if any) from the request instance -->
					<!-- Rename the element after the matched element -->
					<xsl:variable name="deleteElementName">
						<xsl:choose>
							<xsl:when test="string-length( name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] ) ) > 0">
								<xsl:value-of select="name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] )" /><xsl:text>:delete</xsl:text>
							</xsl:when>
							<xsl:otherwise>
								<xsl:text>delete</xsl:text>
							</xsl:otherwise>
						</xsl:choose>				
					</xsl:variable>
					<xsl:variable name="deleteElementNamespaceURI">
						<xsl:value-of select="'http://www.w3.org/2002/xforms'" />
					</xsl:variable>
					<xsl:element name="{ $deleteElementName }" namespace="{ $deleteElementNamespaceURI }">
						<!-- Add the transformed/correct model attribute -->
						<xsl:if test="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@model">
							<xsl:attribute name="model">
								<xsl:value-of select="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@model" />
							</xsl:attribute>
						</xsl:if>
						<!-- Add the transformed/correct context attribute -->
						<xsl:if test="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@ref">
							<xsl:attribute name="context">
								<xsl:value-of select="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@ref" />
							</xsl:attribute>
						</xsl:if>
						<!-- Add the nodeset attribute -->
						<xsl:attribute name="nodeset">
							<xsl:value-of select="$paramXFormsDBErrorElement" />
						</xsl:attribute>
						<!-- Add the at attribute -->
						<xsl:attribute name="at">
							<xsl:text>last()</xsl:text>
						</xsl:attribute>
					</xsl:element>
					<!-- Add insert action if needed -->
					<xsl:if test="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@replacetype = 'instance'">
						<!-- ::::::::::::: INSERT ELEMENT ::::::::::::: -->
						<!-- Replace the contents of the original instance with the contents of the xformsdb-response-proxy-instance instance -->
						<!-- Rename the element after the matched element -->
						<xsl:variable name="insertElementName">
							<xsl:choose>
								<xsl:when test="string-length( name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] ) ) > 0">
									<xsl:value-of select="name( namespace::*[ . = 'http://www.w3.org/2002/xforms' ] )" /><xsl:text>:insert</xsl:text>
								</xsl:when>
								<xsl:otherwise>
									<xsl:text>insert</xsl:text>
								</xsl:otherwise>
							</xsl:choose>				
						</xsl:variable>
						<xsl:variable name="insertElementNamespaceURI">
							<xsl:value-of select="'http://www.w3.org/2002/xforms'" />
						</xsl:variable>
						<xsl:element name="{ $insertElementName }" namespace="{ $insertElementNamespaceURI }">
							<!-- Add the transformed/correct model attribute -->
							<xsl:if test="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@model">
								<xsl:attribute name="model">
									<xsl:value-of select="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@model" />
								</xsl:attribute>
							</xsl:if>
							<!-- Add the origin attribute -->
							<xsl:attribute name="origin">
								<xsl:text>instance( '</xsl:text>
								<xsl:value-of select="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@proxyinstance" />
								<xsl:text>' )</xsl:text>
							</xsl:attribute>
							<!-- Add the transformed/correct context attribute -->
							<xsl:if test="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@xformsinsertcontext">
								<xsl:attribute name="context">
									<xsl:value-of select="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@xformsinsertcontext" />
								</xsl:attribute>
							</xsl:if>
							<!-- Add the nodeset attribute -->
							<xsl:attribute name="nodeset">
								<xsl:text>.</xsl:text>
							</xsl:attribute>
							<!-- Add the at attribute -->
							<xsl:attribute name="at">
								<xsl:text>last()</xsl:text>
							</xsl:attribute>
						</xsl:element>
					</xsl:if>
					<!-- Apply further templates i.e. copy user defined actions -->
					<xsl:apply-templates select="./xforms:action[ @ev:event = 'xforms-submit-done' ]/*" />
				</xsl:element>
			</xsl:if>
			<!-- Apply further templates -->
			<xsl:apply-templates />
		</xsl:element>
	</xsl:template>
			

	<!-- Transform the <xforms:action ev:event="xformsdb-request-error"> element -->
	<xsl:template match="/xhtml:html/xhtml:head/xforms:model/xformsdb:submission/xforms:action[ @ev:event = 'xformsdb-request-error' ]">
		<!-- Parse the XML document -->
 		<xsl:variable name="varXFormsDBSubmissions" select="saxon:parse( $paramXFormsDBSubmissionsXMLString )/xformsdb:submissions" />
		<!-- Calculate the index/position of the matched <xformsdb:submission> element -->
 		<xsl:variable name="varXFormsDBSubmissionIndex" select="count( preceding::xformsdb:submission ) + 1" />
		<!-- Add authored actions if needed. Otherwise, do nothing i.e. do not copy this element (has already been copied in the xformsdb:submission transformation) -->
		<xsl:if test="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@replacetype = 'all'">
			<xsl:copy>
				<xsl:apply-templates select="@*|node()" />
			</xsl:copy>
		</xsl:if>
	</xsl:template>


	<!-- Transform the <xforms:action ev:event="xforms-submit-done"> element -->
	<xsl:template match="/xhtml:html/xhtml:head/xforms:model/xformsdb:submission/xforms:action[ @ev:event = 'xforms-submit-done' ]">
		<!-- Parse the XML document -->
 		<xsl:variable name="varXFormsDBSubmissions" select="saxon:parse( $paramXFormsDBSubmissionsXMLString )/xformsdb:submissions" />
		<!-- Calculate the index/position of the matched <xformsdb:submission> element -->
 		<xsl:variable name="varXFormsDBSubmissionIndex" select="count( preceding::xformsdb:submission ) + 1" />
		<!-- Add authored actions if needed. Otherwise, do nothing i.e. do not copy this element (has already been copied in the xformsdb:submission transformation) -->
		<xsl:if test="$varXFormsDBSubmissions/xformsdb:submission[ $varXFormsDBSubmissionIndex ]/@replacetype = 'all'">
			<xsl:copy>
				<xsl:apply-templates select="@*|node()" />
			</xsl:copy>
		</xsl:if>
	</xsl:template>

			
	<!-- Handle the <xforms:send> element -->
	<xsl:template match="//xforms:send">
		<!-- Store the matched node for later use -->
 		<xsl:variable name="varXFormsSend" select="." />
		<!-- Retrieve the submission attribute -->
 		<xsl:variable name="varSubmission" select="@submission" />
 		<!-- Parse the XML document -->
 		<xsl:variable name="varXFormsDBSubmissions" select="saxon:parse( $paramXFormsDBSubmissionsXMLString )/xformsdb:submissions" />
 		<!-- Retrieve the event attribute -->
 		<xsl:variable name="varEvent" select="@ev:event" />
 		<!-- Iterate over all <xformsdb:submission> elements -->
 		<xsl:for-each select="$varXFormsDBSubmissions/xformsdb:submission">
	 		<xsl:if test="@id = $varSubmission">
				<!-- ::::::::::::: DELETE ELEMENT ::::::::::::: -->
				<!-- Delete an old xformsdb:error element (if any) from the request instance -->
				<!-- Rename the element after the matched element -->
				<xsl:variable name="deleteElementName">
					<xsl:choose>
						<xsl:when test="string-length( substring-before( $varXFormsSend/name(), ':' ) ) > 0">
							<xsl:value-of select="substring-before( $varXFormsSend/name(), ':' )" /><xsl:text>:delete</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>delete</xsl:text>
						</xsl:otherwise>
					</xsl:choose>				
				</xsl:variable>
				<xsl:variable name="deleteElementNamespaceURI">
					<xsl:value-of select="$varXFormsSend/namespace-uri()" />
				</xsl:variable>
				<xsl:element name="{ $deleteElementName }" namespace="{ $deleteElementNamespaceURI }">
					<!-- Add the transformed/correct model attribute -->
					<xsl:if test="@model">
						<xsl:attribute name="model">
							<xsl:value-of select="@model" />
						</xsl:attribute>
					</xsl:if>
					<!-- Add the nodeset attribute -->
					<xsl:attribute name="nodeset">
						<xsl:value-of select="@ref" />
						<xsl:text>/</xsl:text>
						<xsl:value-of select="$paramXFormsDBErrorElement" />
					</xsl:attribute>
					<!-- Add the at attribute -->
					<xsl:attribute name="at">
						<xsl:text>last()</xsl:text>
					</xsl:attribute>
					<!-- Check whether the event attribute needs to be added -->
  					<xsl:if test="boolean( count( $varEvent ) ) = true()">
  						<!-- Add the event attribute -->
						<!-- Name the attribute after the matched element -->
						<xsl:variable name="eventAttributeName">
							<xsl:choose>
								<xsl:when test="string-length( substring-before( $varEvent/name(), ':' ) ) > 0">
									<xsl:value-of select="substring-before( $varEvent/name(), ':' )" /><xsl:text>:event</xsl:text>
								</xsl:when>
								<xsl:otherwise>
									<xsl:text>event</xsl:text>
								</xsl:otherwise>
							</xsl:choose>				
						</xsl:variable>
						<xsl:variable name="eventAttributeNamespaceURI">
							<xsl:value-of select="$varEvent/namespace-uri()" />
						</xsl:variable>
						<xsl:attribute name="{ $eventAttributeName }" namespace="{ $eventAttributeNamespaceURI }">
							<xsl:value-of select="$varEvent" />
						</xsl:attribute>
					</xsl:if>					
				</xsl:element>
			</xsl:if>
 			<!-- Create additional elements if necessary -->
			<xsl:if test="@id = $varSubmission and ( @expressiontype = 'update' or @statetype = 'set' or @filetype = 'delete' )">
				<!-- ::::::::::::: DELETE ELEMENT ::::::::::::: -->
				<!-- Add the delete element (to delete the previous child element of the attachment element) -->
				<!-- Rename the element after the matched element -->
				<xsl:variable name="deleteElementName">
					<xsl:choose>
						<xsl:when test="string-length( substring-before( $varXFormsSend/name(), ':' ) ) > 0">
							<xsl:value-of select="substring-before( $varXFormsSend/name(), ':' )" /><xsl:text>:delete</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>delete</xsl:text>
						</xsl:otherwise>
					</xsl:choose>				
				</xsl:variable>
				<xsl:variable name="deleteElementNamespaceURI">
					<xsl:value-of select="$varXFormsSend/namespace-uri()" />
				</xsl:variable>
				<xsl:element name="{ $deleteElementName }" namespace="{ $deleteElementNamespaceURI }">
					<!-- Add the transformed/correct model attribute -->
					<xsl:if test="@model">
						<xsl:attribute name="model">
							<xsl:value-of select="@model" />
						</xsl:attribute>
					</xsl:if>
					<!-- Add the nodeset attribute -->
					<xsl:if test="@ref">
						<xsl:attribute name="nodeset">
							<xsl:variable name="xformsdbAttachmentElementName">
								<xsl:choose>
									<xsl:when test="string-length( name( namespace::*[ . = 'http://www.tml.tkk.fi/2007/xformsdb' ] ) ) > 0">
										<xsl:value-of select="name( namespace::*[ . = 'http://www.tml.tkk.fi/2007/xformsdb' ] )" /><xsl:text>:attachment</xsl:text>
									</xsl:when>
									<xsl:otherwise>
										<xsl:text>attachment</xsl:text>
									</xsl:otherwise>
								</xsl:choose>				
							</xsl:variable>
							<xsl:value-of select="@ref" />
							<xsl:text>/</xsl:text>
							<xsl:value-of select="$xformsdbAttachmentElementName" />
							<xsl:text>/*</xsl:text>
						</xsl:attribute>
					</xsl:if>
					<!-- Add the at attribute -->
					<xsl:attribute name="at">
						<xsl:text>1</xsl:text>
					</xsl:attribute>
					<!-- Check whether the event attribute needs to be added -->
  					<xsl:if test="boolean( count( $varEvent ) ) = true()">
  						<!-- Add the event attribute -->
						<!-- Name the attribute after the matched element -->
						<xsl:variable name="eventAttributeName">
							<xsl:choose>
								<xsl:when test="string-length( substring-before( $varEvent/name(), ':' ) ) > 0">
									<xsl:value-of select="substring-before( $varEvent/name(), ':' )" /><xsl:text>:event</xsl:text>
								</xsl:when>
								<xsl:otherwise>
									<xsl:text>event</xsl:text>
								</xsl:otherwise>
							</xsl:choose>				
						</xsl:variable>
						<xsl:variable name="eventAttributeNamespaceURI">
							<xsl:value-of select="$varEvent/namespace-uri()" />
						</xsl:variable>
						<xsl:attribute name="{ $eventAttributeName }" namespace="{ $eventAttributeNamespaceURI }">
							<xsl:value-of select="$varEvent" />
						</xsl:attribute>
					</xsl:if>					
				</xsl:element>
				<!-- ::::::::::::: INSERT ELEMENT ::::::::::::: -->
				<!-- Add the insert element (to insert the attachment instance data to the attachment element) -->
				<!-- Rename the element after the matched element -->
				<xsl:variable name="insertElementName">
					<xsl:choose>
						<xsl:when test="string-length( substring-before( $varXFormsSend/name(), ':' ) ) > 0">
							<xsl:value-of select="substring-before( $varXFormsSend/name(), ':' )" /><xsl:text>:insert</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>insert</xsl:text>
						</xsl:otherwise>
					</xsl:choose>				
				</xsl:variable>
				<xsl:variable name="insertElementNamespaceURI">
					<xsl:value-of select="$varXFormsSend/namespace-uri()" />
				</xsl:variable>
				<xsl:element name="{ $insertElementName }" namespace="{ $insertElementNamespaceURI }">
					<!-- Add the transformed/correct model attribute -->
					<xsl:if test="@model">
						<xsl:attribute name="model">
							<xsl:value-of select="@model" />
						</xsl:attribute>
					</xsl:if>
					<!-- Add the context and nodeset attributes -->
					<xsl:if test="@ref">
						<!-- Add the context attribute -->
						<xsl:attribute name="context">
							<xsl:variable name="xformsdbAttachmentElementName">
								<xsl:choose>
									<xsl:when test="string-length( name( namespace::*[ . = 'http://www.tml.tkk.fi/2007/xformsdb' ] ) ) > 0">
										<xsl:value-of select="name( namespace::*[ . = 'http://www.tml.tkk.fi/2007/xformsdb' ] )" /><xsl:text>:attachment</xsl:text>
									</xsl:when>
									<xsl:otherwise>
										<xsl:text>attachment</xsl:text>
									</xsl:otherwise>
								</xsl:choose>				
							</xsl:variable>
							<xsl:value-of select="@ref" />
							<xsl:text>/</xsl:text>
							<xsl:value-of select="$xformsdbAttachmentElementName" />
						</xsl:attribute>
						<!-- Add the nodeset attribute -->
						<xsl:attribute name="nodeset">
							<xsl:text>*</xsl:text>
						</xsl:attribute>
					</xsl:if>
					<!-- Add the origin attribute -->
					<xsl:if test="@xformsinsertorigin">
						<xsl:attribute name="origin">
							<xsl:value-of select="@xformsinsertorigin" />
						</xsl:attribute>
					</xsl:if>
					<!-- Check whether the event attribute needs to be added -->
  					<xsl:if test="boolean( count( $varEvent ) ) = true()">
  						<!-- Add the event attribute -->
						<!-- Name the attribute after the matched element -->
						<xsl:variable name="eventAttributeName">
							<xsl:choose>
								<xsl:when test="string-length( substring-before( $varEvent/name(), ':' ) ) > 0">
									<xsl:value-of select="substring-before( $varEvent/name(), ':' )" /><xsl:text>:event</xsl:text>
								</xsl:when>
								<xsl:otherwise>
									<xsl:text>event</xsl:text>
								</xsl:otherwise>
							</xsl:choose>				
						</xsl:variable>
						<xsl:variable name="eventAttributeNamespaceURI">
							<xsl:value-of select="$varEvent/namespace-uri()" />
						</xsl:variable>
						<xsl:attribute name="{ $eventAttributeName }" namespace="{ $eventAttributeNamespaceURI }">
							<xsl:value-of select="$varEvent" />
						</xsl:attribute>
					</xsl:if>					
				</xsl:element>
			</xsl:if>
 		</xsl:for-each>
		<!-- Figure out whether or not the submission attribute of the <xforms:send> element needs to be changed -->
		<xsl:variable name="varChangeSubmission">
			<!-- Iterate over all <xformsdb:submission> elements -->
 			<xsl:for-each select="$varXFormsDBSubmissions/xformsdb:submission">
				<xsl:if test="@id = $varSubmission and @xformsdbrequesttype = 'file' and ( @filetype = 'insert' or @filetype = 'update' )">
					<xsl:text>true</xsl:text>
				</xsl:if>
			</xsl:for-each>
		</xsl:variable>
		<!-- Change the submission attribute of the <xforms:send> element if needed -->
		<xsl:choose>
			<!-- BEGIN: Hack: Create a dummy send in order to receive information about the file to be uploaded from Orbeon Forms -->
			<xsl:when test="$varChangeSubmission = 'true'">
		 		<!-- Copy the <xforms:send> element and change the submission -->
				<xsl:for-each select="$varXFormsSend">
					<xsl:copy>
						<xsl:apply-templates select="@*" />
						<xsl:attribute name="submission">
							<xsl:value-of select="concat( 'xformsdb-', @submission )" />
			      		</xsl:attribute>
						<xsl:apply-templates select="node()" />
					</xsl:copy>
				</xsl:for-each>
			</xsl:when>
			<!-- END: Hack: Create a dummy send in order to receive information about the file to be uploaded from Orbeon Forms -->
			<xsl:otherwise>
		 		<!-- Copy the <xforms:send> element unchanged -->
				<xsl:copy-of select="$varXFormsSend" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	 
	<!-- Handle the <xforms:submit> element -->
	<xsl:template match="/xhtml:html/xhtml:body//xforms:submit">
		<!-- Store the matched node for later use -->
 		<xsl:variable name="varXFormsSubmit" select="." />
		<!-- Retrieve the submission attribute -->
 		<xsl:variable name="varSubmission" select="@submission" />
 		<!-- Parse the XML document -->
 		<xsl:variable name="varXFormsDBSubmissions" select="saxon:parse( $paramXFormsDBSubmissionsXMLString )/xformsdb:submissions" />
 		<!-- Iterate over all <xformsdb:submission> elements -->
 		<xsl:for-each select="$varXFormsDBSubmissions/xformsdb:submission">
	 		<xsl:if test="@id = $varSubmission">
	 			<!-- Store the matched node for later use -->
	 			<xsl:variable name="varXFormsDBSubmission" select="." />
	 			<!-- Change the context back to the <xforms:submit> element -->
				<xsl:for-each select="$varXFormsSubmit">
					<xsl:variable name="varXFormsSubmitElementName">
						<xsl:choose>
							<xsl:when test="string-length( substring-before( $varXFormsSubmit/name(), ':' ) ) > 0">
								<xsl:value-of select="substring-before( $varXFormsSubmit/name(), ':' )" /><xsl:text>:submit</xsl:text>
							</xsl:when>
							<xsl:otherwise>
								<xsl:text>submit</xsl:text>
							</xsl:otherwise>
						</xsl:choose>				
					</xsl:variable>
					<xsl:variable name="varXFormsSubmitElementNamespaceURI">
						<xsl:value-of select="$varXFormsSubmit/namespace-uri()" />
					</xsl:variable>
					<xsl:variable name="varXFormsActionElementName">
						<xsl:choose>
							<xsl:when test="string-length( substring-before( $varXFormsSubmit/name(), ':' ) ) > 0">
								<xsl:value-of select="substring-before( $varXFormsSubmit/name(), ':' )" /><xsl:text>:action</xsl:text>
							</xsl:when>
							<xsl:otherwise>
								<xsl:text>action</xsl:text>
							</xsl:otherwise>
						</xsl:choose>				
					</xsl:variable>
					<xsl:variable name="varXFormsActionElementNamespaceURI">
						<xsl:value-of select="$varXFormsSubmit/namespace-uri()" />
					</xsl:variable>
					<!-- Figure out whether or not the submission attribute of the <xforms:submit> element needs to be changed -->
					<xsl:variable name="varChangeSubmission">
						<!-- Iterate over all <xformsdb:submission> elements -->
			 			<xsl:for-each select="$varXFormsDBSubmissions/xformsdb:submission">
							<xsl:if test="@id = $varSubmission and @xformsdbrequesttype = 'file' and ( @filetype = 'insert' or @filetype = 'update' )">
								<xsl:text>true</xsl:text>
							</xsl:if>
						</xsl:for-each>
					</xsl:variable>
					<!-- Create almost identical copy of the <xforms:submit> element -->
					<xsl:element name="{ $varXFormsSubmitElementName }" namespace="{ $varXFormsSubmitElementNamespaceURI }">
						<!-- Iterate over all attributes -->
			 			<xsl:for-each select="@*">
							<xsl:attribute name="{ name() }">
								<xsl:value-of select="." />
							</xsl:attribute>
							<!-- Change the submission attribute of the <xforms:submit> element if needed -->
							<xsl:choose>
								<!-- BEGIN: Hack: Create a dummy submit in order to receive information about the file to be uploaded from Orbeon Forms -->
								<xsl:when test="$varChangeSubmission = 'true'">
									<xsl:attribute name="submission">
										<xsl:value-of select="concat( 'xformsdb-', $varXFormsSubmit/@submission )" />
						      		</xsl:attribute>
								</xsl:when>
								<!-- END: Hack: Create a dummy submit in order to receive information about the file to be uploaded from Orbeon Forms -->
								<xsl:otherwise />
							</xsl:choose>								
				 		</xsl:for-each>
				 		<!-- Copy child elements -->
						<xsl:apply-templates select="$varXFormsSubmit/*" />
						<!-- Create an additional <xforms:action> element -->
						<xsl:element name="{ $varXFormsActionElementName }" namespace="{ $varXFormsActionElementNamespaceURI }">
			 				<!-- Add the event attribute -->
							<xsl:attribute name="event" namespace="http://www.w3.org/2001/xml-events">
								<xsl:text>DOMActivate</xsl:text>
							</xsl:attribute>
							<xsl:if test="$varXFormsDBSubmission/@id = $varSubmission">
								<!-- ::::::::::::: DELETE ELEMENT ::::::::::::: -->
								<!-- Delete an old xformsdb:error element (if any) from the request instance -->
								<!-- Rename the element after the matched element -->
								<xsl:variable name="deleteElementName">
									<xsl:choose>
										<xsl:when test="string-length( substring-before( $varXFormsSubmit/name(), ':' ) ) > 0">
											<xsl:value-of select="substring-before( $varXFormsSubmit/name(), ':' )" /><xsl:text>:delete</xsl:text>
										</xsl:when>
										<xsl:otherwise>
											<xsl:text>delete</xsl:text>
										</xsl:otherwise>
									</xsl:choose>				
								</xsl:variable>
								<xsl:variable name="deleteElementNamespaceURI">
									<xsl:value-of select="$varXFormsSubmit/namespace-uri()" />
								</xsl:variable>
								<xsl:element name="{ $deleteElementName }" namespace="{ $deleteElementNamespaceURI }">
									<!-- Add the transformed/correct model attribute -->
									<xsl:if test="$varXFormsDBSubmission/@model">
										<xsl:attribute name="model">
											<xsl:value-of select="$varXFormsDBSubmission/@model" />
										</xsl:attribute>
									</xsl:if>
									<!-- Add the nodeset attribute -->
									<xsl:attribute name="nodeset">
										<xsl:value-of select="$varXFormsDBSubmission/@ref" />
										<xsl:text>/</xsl:text>
										<xsl:value-of select="$paramXFormsDBErrorElement" />
									</xsl:attribute>
									<!-- Add the at attribute -->
									<xsl:attribute name="at">
										<xsl:text>last()</xsl:text>
									</xsl:attribute>
								</xsl:element>
							</xsl:if>
				 			<!-- Create additional elements if necessary -->
							<xsl:if test="$varXFormsDBSubmission/@id = $varSubmission and ( $varXFormsDBSubmission/@expressiontype = 'update' or $varXFormsDBSubmission/@statetype = 'set' or $varXFormsDBSubmission/@filetype = 'delete' )">
								<!-- ::::::::::::: DELETE ELEMENT ::::::::::::: -->
								<!-- Add the delete element (to delete the previous child element of the attachment element) -->
								<!-- Rename the element after the matched element -->
								<xsl:variable name="deleteElementName">
									<xsl:choose>
										<xsl:when test="string-length( substring-before( $varXFormsSubmit/name(), ':' ) ) > 0">
											<xsl:value-of select="substring-before( $varXFormsSubmit/name(), ':' )" /><xsl:text>:delete</xsl:text>
										</xsl:when>
										<xsl:otherwise>
											<xsl:text>delete</xsl:text>
										</xsl:otherwise>
									</xsl:choose>				
								</xsl:variable>
								<xsl:variable name="deleteElementNamespaceURI">
									<xsl:value-of select="$varXFormsSubmit/namespace-uri()" />
								</xsl:variable>
								<xsl:element name="{ $deleteElementName }" namespace="{ $deleteElementNamespaceURI }">
									<!-- Add the transformed/correct model attribute -->
									<xsl:if test="$varXFormsDBSubmission/@model">
										<xsl:attribute name="model">
											<xsl:value-of select="$varXFormsDBSubmission/@model" />
										</xsl:attribute>
									</xsl:if>
									<!-- Add the nodeset attribute -->
									<xsl:if test="$varXFormsDBSubmission/@ref">
										<xsl:attribute name="nodeset">
											<xsl:variable name="xformsdbAttachmentElementName">
												<xsl:choose>
													<xsl:when test="string-length( name( namespace::*[ . = 'http://www.tml.tkk.fi/2007/xformsdb' ] ) ) > 0">
														<xsl:value-of select="name( namespace::*[ . = 'http://www.tml.tkk.fi/2007/xformsdb' ] )" /><xsl:text>:attachment</xsl:text>
													</xsl:when>
													<xsl:otherwise>
														<xsl:text>attachment</xsl:text>
													</xsl:otherwise>
												</xsl:choose>				
											</xsl:variable>
											<xsl:value-of select="$varXFormsDBSubmission/@ref" />
											<xsl:text>/</xsl:text>
											<xsl:value-of select="$xformsdbAttachmentElementName" />
											<xsl:text>/*</xsl:text>
										</xsl:attribute>
									</xsl:if>
									<!-- Add the at attribute -->
									<xsl:attribute name="at">
										<xsl:text>1</xsl:text>
									</xsl:attribute>
								</xsl:element>
								<!-- ::::::::::::: INSERT ELEMENT ::::::::::::: -->
								<!-- Add the insert element (to insert the attachment instance data to the attachment element) -->
								<!-- Rename the element after the matched element -->
								<xsl:variable name="insertElementName">
									<xsl:choose>
										<xsl:when test="string-length( substring-before( $varXFormsSubmit/name(), ':' ) ) > 0">
											<xsl:value-of select="substring-before( $varXFormsSubmit/name(), ':' )" /><xsl:text>:insert</xsl:text>
										</xsl:when>
										<xsl:otherwise>
											<xsl:text>insert</xsl:text>
										</xsl:otherwise>
									</xsl:choose>				
								</xsl:variable>
								<xsl:variable name="insertElementNamespaceURI">
									<xsl:value-of select="$varXFormsSubmit/namespace-uri()" />
								</xsl:variable>
								<xsl:element name="{ $insertElementName }" namespace="{ $insertElementNamespaceURI }">
									<!-- Add the transformed/correct model attribute -->
									<xsl:if test="$varXFormsDBSubmission/@model">
										<xsl:attribute name="model">
											<xsl:value-of select="$varXFormsDBSubmission/@model" />
										</xsl:attribute>
									</xsl:if>
									<!-- Add the context and nodeset attributes -->
									<xsl:if test="$varXFormsDBSubmission/@ref">
										<!-- Add the context attribute -->
										<xsl:attribute name="context">
											<xsl:variable name="xformsdbAttachmentElementName">
												<xsl:choose>
													<xsl:when test="string-length( name( namespace::*[ . = 'http://www.tml.tkk.fi/2007/xformsdb' ] ) ) > 0">
														<xsl:value-of select="name( namespace::*[ . = 'http://www.tml.tkk.fi/2007/xformsdb' ] )" /><xsl:text>:attachment</xsl:text>
													</xsl:when>
													<xsl:otherwise>
														<xsl:text>attachment</xsl:text>
													</xsl:otherwise>
												</xsl:choose>				
											</xsl:variable>
											<xsl:value-of select="$varXFormsDBSubmission/@ref" />
											<xsl:text>/</xsl:text>
											<xsl:value-of select="$xformsdbAttachmentElementName" />
										</xsl:attribute>
										<!-- Add the nodeset attribute -->
										<xsl:attribute name="nodeset">
											<xsl:text>*</xsl:text>
										</xsl:attribute>
									</xsl:if>
									<!-- Add the origin attribute -->
									<xsl:if test="$varXFormsDBSubmission/@xformsinsertorigin">
										<xsl:attribute name="origin">
											<xsl:value-of select="$varXFormsDBSubmission/@xformsinsertorigin" />
										</xsl:attribute>
									</xsl:if>
								</xsl:element>
							</xsl:if>
						</xsl:element>
					</xsl:element>
				</xsl:for-each>	 			
	 		</xsl:if>
	 	</xsl:for-each>
	 </xsl:template>


	<!-- Handle the <xforms:load> element -->
	<xsl:template match="//xforms:load">
		<!-- Store the matched node for later use -->
 		<xsl:variable name="varXFormsLoad" select="." />
		<!-- Parse the XML document -->
 		<xsl:variable name="varXFormsLoads" select="saxon:parse( $paramXFormsLoadsXMLString )/xforms:loads" />
		<!-- Calculate the index/position of the matched <xforms:load> element -->
 		<xsl:variable name="varXFormsLoadIndex" select="count( preceding::xforms:load ) + 1" />

		<!-- Transform the <xforms:load> element -->
		<!-- Rename the element after the matched element -->
		<xsl:variable name="varXFormsLoadElementName">
			<xsl:choose>
				<xsl:when test="string-length( substring-before( $varXFormsLoad/name(), ':' ) ) > 0">
					<xsl:value-of select="substring-before( $varXFormsLoad/name(), ':' )" /><xsl:text>:load</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>load</xsl:text>
				</xsl:otherwise>
			</xsl:choose>				
		</xsl:variable>
		<xsl:variable name="varXFormsLoadElementNamespaceURI">
			<xsl:value-of select="$varXFormsLoad/namespace-uri()" />
		</xsl:variable>
		<xsl:element name="{ $varXFormsLoadElementName }" namespace="{ $varXFormsLoadElementNamespaceURI }">
			<!-- Iterate over all attributes -->
 			<xsl:for-each select="@*">
 				<xsl:choose>
	 				<!-- Remove the untransformed/incorrect resource attribute -->
	 				<xsl:when test="name() = 'resource'">
	 				</xsl:when>
	 				<!-- Copy other attributes -->
	 				<xsl:otherwise>
						<xsl:attribute name="{ name() }">
							<xsl:value-of select="." />
						</xsl:attribute>
	 				</xsl:otherwise>
	 			</xsl:choose>
	 		</xsl:for-each>
			<!-- Add the transformed/correct resource attribute -->
			<xsl:if test="$varXFormsLoads/xforms:load[ $varXFormsLoadIndex ]/@resource">
				<xsl:attribute name="resource">
					<xsl:value-of select="$varXFormsLoads/xforms:load[ $varXFormsLoadIndex ]/@resource" />
				</xsl:attribute>
			</xsl:if>
			<!-- Apply further templates -->
			<xsl:apply-templates />
		</xsl:element>
	</xsl:template>


	<!-- Handle the <xforms:submission> element -->
	<xsl:template match="/xhtml:html/xhtml:head/xforms:model/xforms:submission">
		<!-- Store the matched node for later use -->
 		<xsl:variable name="varXFormsSubmission" select="." />
		<!-- Parse the XML document -->
 		<xsl:variable name="varXFormsSubmissions" select="saxon:parse( $paramXFormsSubmissionsXMLString )/xforms:submissions" />
		<!-- Calculate the index/position of the matched <xforms:submission> element -->
 		<xsl:variable name="varXFormsSubmissionIndex" select="count( preceding::xforms:submission ) + 1" />

		<!-- Transform the <xforms:submission> element -->
		<!-- Rename the element after the matched element -->
		<xsl:variable name="varXFormsSubmissionElementName">
			<xsl:choose>
				<xsl:when test="string-length( substring-before( $varXFormsSubmission/name(), ':' ) ) > 0">
					<xsl:value-of select="substring-before( $varXFormsSubmission/name(), ':' )" /><xsl:text>:submission</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>submission</xsl:text>
				</xsl:otherwise>
			</xsl:choose>				
		</xsl:variable>
		<xsl:variable name="varXFormsSubmissionElementNamespaceURI">
			<xsl:value-of select="$varXFormsSubmission/namespace-uri()" />
		</xsl:variable>
		<xsl:element name="{ $varXFormsSubmissionElementName }" namespace="{ $varXFormsSubmissionElementNamespaceURI }">
			<!-- Iterate over all attributes -->
 			<xsl:for-each select="@*">
 				<xsl:choose>
	 				<!-- Remove the untransformed/incorrect action attribute -->
	 				<xsl:when test="name() = 'action'">
	 				</xsl:when>
	 				<!-- Copy other attributes -->
	 				<xsl:otherwise>
						<xsl:attribute name="{ name() }">
							<xsl:value-of select="." />
						</xsl:attribute>
	 				</xsl:otherwise>
	 			</xsl:choose>
	 		</xsl:for-each>
			<!-- Add the transformed/correct action attribute -->
			<xsl:if test="$varXFormsSubmissions/xforms:submission[ $varXFormsSubmissionIndex ]/@action">
				<xsl:attribute name="action">
					<xsl:value-of select="$varXFormsSubmissions/xforms:submission[ $varXFormsSubmissionIndex ]/@action" />
				</xsl:attribute>
			</xsl:if>
			<!-- Apply further templates -->
			<xsl:apply-templates />
		</xsl:element>
	</xsl:template>


	<!-- Handle the <xhtml:meta> element -->
	<xsl:template match="//xhtml:meta">
		<!-- Store the matched node for later use -->
 		<xsl:variable name="varXHTMLMeta" select="." />
		<!-- Parse the XML document -->
 		<xsl:variable name="varXHTMLMetas" select="saxon:parse( $paramXHTMLMetasXMLString )/xhtml:metas" />
		<!-- Calculate the index/position of the matched <xhtml:meta> element -->
 		<xsl:variable name="varXHTMLMetaIndex" select="count( preceding::xhtml:meta ) + 1" />

		<!-- Transform the <xhtml:meta> element -->
		<!-- Rename the element after the matched element -->
		<xsl:variable name="varXHTMLMetaElementName">
			<xsl:choose>
				<xsl:when test="string-length( substring-before( $varXHTMLMeta/name(), ':' ) ) > 0">
					<xsl:value-of select="substring-before( $varXHTMLMeta/name(), ':' )" /><xsl:text>:meta</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>meta</xsl:text>
				</xsl:otherwise>
			</xsl:choose>				
		</xsl:variable>
		<xsl:variable name="varXHTMLMetaElementNamespaceURI">
			<xsl:value-of select="$varXHTMLMeta/namespace-uri()" />
		</xsl:variable>
		<xsl:element name="{ $varXHTMLMetaElementName }" namespace="{ $varXHTMLMetaElementNamespaceURI }">
			<!-- Iterate over all attributes -->
 			<xsl:for-each select="@*">
 				<xsl:choose>
	 				<!-- Remove the untransformed/incorrect content attribute -->
	 				<xsl:when test="name() = 'content'">
	 				</xsl:when>
	 				<!-- Copy other attributes -->
	 				<xsl:otherwise>
						<xsl:attribute name="{ name() }">
							<xsl:value-of select="." />
						</xsl:attribute>
	 				</xsl:otherwise>
	 			</xsl:choose>
	 		</xsl:for-each>
			<!-- Add the transformed/correct content attribute -->
			<xsl:if test="$varXHTMLMetas/xhtml:meta[ $varXHTMLMetaIndex ]/@content">
				<xsl:attribute name="content">
					<xsl:value-of select="$varXHTMLMetas/xhtml:meta[ $varXHTMLMetaIndex ]/@content" />
				</xsl:attribute>
			</xsl:if>
			<!-- Apply further templates -->
			<xsl:apply-templates />
		</xsl:element>
	</xsl:template>


	<!-- Handle the <xhtml:a> element -->
	<xsl:template match="//xhtml:a">
		<!-- Store the matched node for later use -->
 		<xsl:variable name="varXHTMLA" select="." />
		<!-- Parse the XML document -->
 		<xsl:variable name="varXHTMLAs" select="saxon:parse( $paramXHTMLAsXMLString )/xhtml:as" />
		<!-- Calculate the index/position of the matched <xhtml:a> element -->
 		<xsl:variable name="varXHTMLAIndex" select="count( preceding::xhtml:a ) + 1" />

		<!-- Transform the <xhtml:a> element -->
		<!-- Rename the element after the matched element -->
		<xsl:variable name="varXHTMLAElementName">
			<xsl:choose>
				<xsl:when test="string-length( substring-before( $varXHTMLA/name(), ':' ) ) > 0">
					<xsl:value-of select="substring-before( $varXHTMLA/name(), ':' )" /><xsl:text>:a</xsl:text>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>a</xsl:text>
				</xsl:otherwise>
			</xsl:choose>				
		</xsl:variable>
		<xsl:variable name="varXHTMLAElementNamespaceURI">
			<xsl:value-of select="$varXHTMLA/namespace-uri()" />
		</xsl:variable>
		<xsl:element name="{ $varXHTMLAElementName }" namespace="{ $varXHTMLAElementNamespaceURI }">
			<!-- Iterate over all attributes -->
 			<xsl:for-each select="@*">
 				<xsl:choose>
	 				<!-- Remove the untransformed/incorrect href attribute -->
	 				<xsl:when test="name() = 'href'">
	 				</xsl:when>
	 				<!-- Copy other attributes -->
	 				<xsl:otherwise>
						<xsl:attribute name="{ name() }">
							<xsl:value-of select="." />
						</xsl:attribute>
	 				</xsl:otherwise>
	 			</xsl:choose>
	 		</xsl:for-each>
			<!-- Add the transformed/correct href attribute -->
			<xsl:if test="$varXHTMLAs/xhtml:a[ $varXHTMLAIndex ]/@href">
				<xsl:attribute name="href">
					<xsl:value-of select="$varXHTMLAs/xhtml:a[ $varXHTMLAIndex ]/@href" />
				</xsl:attribute>
			</xsl:if>
			<!-- Apply further templates -->
			<xsl:apply-templates />
		</xsl:element>
	</xsl:template>
	


</xsl:stylesheet>