<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
				xmlns:xforms="http://www.w3.org/2002/xforms"
				xmlns:xformsdb="http://www.tml.tkk.fi/2007/xformsdb"
				xmlns:xformsrtc="http://cs.aalto.fi/2016/xformsrtc"
				xmlns:xhtml="http://www.w3.org/1999/xhtml"
				xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
				xmlns:xxforms="http://orbeon.org/oxf/xml/xforms">
	<!-- Define the output format -->
	<xsl:output method="xml" version="1.0" />
 	<!-- Define the parameters -->
	<xsl:param name="paramXFormsDBResponseProxyInstance" select="'xformsdb-response-proxy-instance'" />


	
	<xsl:template match="/">
		<xsl:element name="xhtml:html" namespace="http://www.w3.org/1999/xhtml">
			<xsl:element name="xforms:models" namespace="http://www.w3.org/2002/xforms">
				<xsl:if test="/xhtml:html/xhtml:head/xforms:model">
					<xsl:for-each select="/xhtml:html/xhtml:head/xforms:model">
						<xsl:call-template name="xformsModel">
							<xsl:with-param name="pContext" select="." />
						</xsl:call-template>
					</xsl:for-each>
				</xsl:if>
			</xsl:element>
			<xsl:element name="xformsdb:instances" namespace="http://www.tml.tkk.fi/2007/xformsdb">
				<xsl:if test="/xhtml:html/xhtml:head/xforms:model/xformsdb:instance">
					<xsl:for-each select="/xhtml:html/xhtml:head/xforms:model/xformsdb:instance">
						<xsl:call-template name="xformsdbInstance">
							<xsl:with-param name="pContext" select="." />
						</xsl:call-template>
					</xsl:for-each>
				</xsl:if>
			</xsl:element>
			<xsl:element name="xformsdb:submissions" namespace="http://www.tml.tkk.fi/2007/xformsdb">
				<xsl:if test="/xhtml:html/xhtml:head/xforms:model/xformsdb:submission">
					<xsl:for-each select="/xhtml:html/xhtml:head/xforms:model/xformsdb:submission">
						<xsl:call-template name="xformsdbSubmission">
							<xsl:with-param name="pContext" select="." />
						</xsl:call-template>
					</xsl:for-each>
				</xsl:if>
			</xsl:element>
			<xsl:element name="xformsrtc:connections" namespace="http://cs.aalto.fi/2016/xformsrtc">
				<xsl:if test="/xhtml:html/xhtml:head/xforms:model/xformsrtc:connection">
					<xsl:for-each select="/xhtml:html/xhtml:head/xforms:model/xformsrtc:connection">
						<xsl:call-template name="xformsrtcConnection">
							<xsl:with-param name="pContext" select="." />
						</xsl:call-template>
					</xsl:for-each>
				</xsl:if>
			</xsl:element>
			<xsl:element name="xformsrtc:connects" namespace="http://cs.aalto.fi/2016/xformsrtc">
				<xsl:if test="//xformsrtc:connect">
					<xsl:for-each select="//xformsrtc:connect">
						<xsl:call-template name="xformsrtcConnect">
							<xsl:with-param name="pContext" select="." />
						</xsl:call-template>
					</xsl:for-each>
				</xsl:if>
			</xsl:element>
			<xsl:element name="xformsrtc:disconnects" namespace="http://cs.aalto.fi/2016/xformsrtc">
				<xsl:if test="//xformsrtc:disconnect">
					<xsl:for-each select="//xformsrtc:disconnect">
						<xsl:call-template name="xformsrtcDisconnect">
							<xsl:with-param name="pContext" select="." />
						</xsl:call-template>
					</xsl:for-each>
				</xsl:if>
			</xsl:element>
			<xsl:element name="xformsrtc:sends" namespace="http://cs.aalto.fi/2016/xformsrtc">
				<xsl:if test="//xformsrtc:send">
					<xsl:for-each select="//xformsrtc:send">
						<xsl:call-template name="xformsrtcSend">
							<xsl:with-param name="pContext" select="." />
						</xsl:call-template>
					</xsl:for-each>
				</xsl:if>
			</xsl:element>
			<xsl:element name="xforms:loads" namespace="http://www.w3.org/2002/xforms">
				<xsl:if test="//xforms:load">
					<xsl:for-each select="//xforms:load">
						<xsl:call-template name="xformsLoad">
							<xsl:with-param name="pContext" select="." />
						</xsl:call-template>
					</xsl:for-each>
				</xsl:if>
			</xsl:element>
			<xsl:element name="xforms:submissions" namespace="http://www.w3.org/2002/xforms">
				<xsl:if test="/xhtml:html/xhtml:head/xforms:model/xforms:submission">
					<xsl:for-each select="/xhtml:html/xhtml:head/xforms:model/xforms:submission">
						<xsl:call-template name="xformsSubmission">
							<xsl:with-param name="pContext" select="." />
						</xsl:call-template>
					</xsl:for-each>
				</xsl:if>
			</xsl:element>
			<xsl:element name="xhtml:metas" namespace="http://www.w3.org/1999/xhtml">
				<xsl:if test="//xhtml:meta">
					<xsl:for-each select="//xhtml:meta">
						<xsl:call-template name="xhtmlMeta">
							<xsl:with-param name="pContext" select="." />
						</xsl:call-template>
					</xsl:for-each>
				</xsl:if>
			</xsl:element>
			<xsl:element name="xhtml:as" namespace="http://www.w3.org/1999/xhtml">
				<xsl:if test="//xhtml:a">
					<xsl:for-each select="//xhtml:a">
						<xsl:call-template name="xhtmlA">
							<xsl:with-param name="pContext" select="." />
						</xsl:call-template>
					</xsl:for-each>
				</xsl:if>
			</xsl:element>
		</xsl:element>
	</xsl:template>


	<xsl:template name="xformsModel">
		<xsl:param name="pContext" />
		<xsl:element name="{ $pContext/name() }" namespace="http://www.w3.org/2002/xforms">
			<xsl:if test="$pContext/@id">
				<xsl:attribute name="id">
					<xsl:value-of select="$pContext/@id" />
				</xsl:attribute>
			</xsl:if>
			<xsl:if test="$pContext/@xxforms:external-events">
				<xsl:attribute name="external-events" namespace="http://orbeon.org/oxf/xml/xforms">
					<xsl:value-of select="$pContext/@xxforms:external-events" />
				</xsl:attribute>
			</xsl:if>
		</xsl:element>
	</xsl:template>


	<xsl:template name="xformsdbInstance">
		<xsl:param name="pContext" />
		<xsl:element name="{ $pContext/name() }" namespace="http://www.tml.tkk.fi/2007/xformsdb">
			<xsl:if test="$pContext/@id">
				<xsl:attribute name="id">
					<xsl:value-of select="$pContext/@id" />
				</xsl:attribute>
			</xsl:if>
			<xsl:if test="$pContext/xformsdb:query[ 1 ]">
				<xsl:call-template name="xformsdbInstanceXFormsDBQuery">
					<xsl:with-param name="pContext" select="$pContext/xformsdb:query[ 1 ]" />
				</xsl:call-template>
			</xsl:if>
			<xsl:if test="$pContext/xformsdb:widgetquery[ 1 ]">
				<xsl:call-template name="xformsdbInstanceXFormsDBWidgetQuery">
					<xsl:with-param name="pContext" select="$pContext/xformsdb:widgetquery[ 1 ]" />
				</xsl:call-template>
			</xsl:if>
			<xsl:if test="$pContext/xformsdb:state[ 1 ]">
				<xsl:call-template name="xformsdbInstanceXFormsDBState">
					<xsl:with-param name="pContext" select="$pContext/xformsdb:state[ 1 ]" />
				</xsl:call-template>
			</xsl:if>
			<xsl:if test="$pContext/xformsdb:login[ 1 ]">
				<xsl:call-template name="xformsdbInstanceXFormsDBLogin">
					<xsl:with-param name="pContext" select="$pContext/xformsdb:login[ 1 ]" />
				</xsl:call-template>
			</xsl:if>
			<xsl:if test="$pContext/xformsdb:logout[ 1 ]">
				<xsl:call-template name="xformsdbInstanceXFormsDBLogout">
					<xsl:with-param name="pContext" select="$pContext/xformsdb:logout[ 1 ]" />
				</xsl:call-template>
			</xsl:if>
			<xsl:if test="$pContext/xformsdb:user[ 1 ]">
				<xsl:call-template name="xformsdbInstanceXFormsDBUser">
					<xsl:with-param name="pContext" select="$pContext/xformsdb:user[ 1 ]" />
				</xsl:call-template>
			</xsl:if>
			<xsl:if test="$pContext/xformsdb:file[ 1 ]">
				<xsl:call-template name="xformsdbInstanceXFormsDBFile">
					<xsl:with-param name="pContext" select="$pContext/xformsdb:file[ 1 ]" />
				</xsl:call-template>
			</xsl:if>
			<xsl:if test="$pContext/xformsdb:cookie[ 1 ]">
				<xsl:call-template name="xformsdbInstanceXFormsDBCookie">
					<xsl:with-param name="pContext" select="$pContext/xformsdb:cookie[ 1 ]" />
				</xsl:call-template>
			</xsl:if>
		</xsl:element>
	</xsl:template>


	<xsl:template name="xformsdbInstanceXFormsDBQuery">
		<xsl:param name="pContext" />
		<xsl:element name="{ $pContext/name() }" namespace="http://www.tml.tkk.fi/2007/xformsdb">
			<xsl:if test="$pContext/@datasrc">
				<xsl:attribute name="datasrc">
					<xsl:value-of select="$pContext/@datasrc" />
				</xsl:attribute>
			</xsl:if>
			<xsl:if test="$pContext/@doc">
				<xsl:attribute name="doc">
					<xsl:value-of select="$pContext/@doc" />
				</xsl:attribute>
			</xsl:if>
			<xsl:if test="$pContext/xformsdb:expression[ 1 ]">
				<xsl:call-template name="xformsdbInstanceXFormsDBQueryXFormsDBExpression">
					<xsl:with-param name="pContext" select="$pContext/xformsdb:expression[ 1 ]" />
				</xsl:call-template>
			</xsl:if>
			<xsl:if test="$pContext/xformsdb:xmlns">
				<xsl:for-each select="$pContext/xformsdb:xmlns">
					<xsl:call-template name="xformsdbInstanceXFormsDBQueryXFormsDBXmlns">
						<xsl:with-param name="pContext" select="." />
					</xsl:call-template>
				</xsl:for-each>
			</xsl:if>
			<xsl:if test="$pContext/xformsdb:var">
				<xsl:for-each select="$pContext/xformsdb:var">
					<xsl:call-template name="xformsdbInstanceXFormsDBQueryXFormsDBVar">
						<xsl:with-param name="pContext" select="." />
					</xsl:call-template>
				</xsl:for-each>
			</xsl:if>
			<xsl:if test="$pContext/xformsdb:secvar[ @name = 'username' ][ 1 ]">
				<xsl:call-template name="xformsdbInstanceXFormsDBQueryXFormsDBSecVar">
					<xsl:with-param name="pContext" select="$pContext/xformsdb:secvar[ @name = 'username' ][ 1 ]" />
				</xsl:call-template>
			</xsl:if>
			<xsl:if test="$pContext/xformsdb:secvar[ @name = 'roles' ][ 1 ]">
				<xsl:call-template name="xformsdbInstanceXFormsDBQueryXFormsDBSecVar">
					<xsl:with-param name="pContext" select="$pContext/xformsdb:secvar[ @name = 'roles' ][ 1 ]" />
				</xsl:call-template>
			</xsl:if>
		</xsl:element>
	</xsl:template>


	<xsl:template name="xformsdbInstanceXFormsDBQueryXFormsDBExpression">
		<xsl:param name="pContext" />
		<xsl:element name="{ $pContext/name() }" namespace="http://www.tml.tkk.fi/2007/xformsdb">
			<!-- BEGIN: For backward compability -->
			<xsl:if test="$pContext/@src">
				<xsl:attribute name="resource">
					<xsl:value-of select="$pContext/@src" />
				</xsl:attribute>
			</xsl:if>
			<!-- END: For backward compability -->
			<xsl:if test="$pContext/@resource">
				<xsl:attribute name="resource">
					<xsl:value-of select="$pContext/@resource" />
				</xsl:attribute>
			</xsl:if>
			<xsl:if test="$pContext/@*|$pContext/node()">
				<xsl:copy-of select="$pContext/@*|$pContext/node()" />
			</xsl:if>
		</xsl:element>
	</xsl:template>


	<xsl:template name="xformsdbInstanceXFormsDBQueryXFormsDBXmlns">
		<xsl:param name="pContext" />
		<xsl:element name="{ $pContext/name() }" namespace="http://www.tml.tkk.fi/2007/xformsdb">
			<xsl:if test="$pContext/@prefix">
				<xsl:attribute name="prefix">
					<xsl:value-of select="$pContext/@prefix" />
				</xsl:attribute>
			</xsl:if>
			<xsl:if test="$pContext/@uri">
				<xsl:attribute name="uri">
					<xsl:value-of select="$pContext/@uri" />
				</xsl:attribute>
			</xsl:if>
		</xsl:element>
	</xsl:template>


	<xsl:template name="xformsdbInstanceXFormsDBQueryXFormsDBVar">
		<xsl:param name="pContext" />
		<xsl:element name="{ $pContext/name() }" namespace="http://www.tml.tkk.fi/2007/xformsdb">
			<xsl:if test="$pContext/@name">
				<xsl:attribute name="name">
					<xsl:value-of select="$pContext/@name" />
				</xsl:attribute>
			</xsl:if>
		</xsl:element>
	</xsl:template>


	<xsl:template name="xformsdbInstanceXFormsDBQueryXFormsDBSecVar">
		<xsl:param name="pContext" />
		<xsl:element name="{ $pContext/name() }" namespace="http://www.tml.tkk.fi/2007/xformsdb">
			<xsl:attribute name="name">
				<xsl:value-of select="$pContext/@name" />
			</xsl:attribute>
		</xsl:element>
	</xsl:template>


	<xsl:template name="xformsdbInstanceXFormsDBWidgetQuery">
		<xsl:param name="pContext" />
		<xsl:element name="{ $pContext/name() }" namespace="http://www.tml.tkk.fi/2007/xformsdb">
			<xsl:if test="$pContext/@datasrc">
				<xsl:attribute name="datasrc">
					<xsl:value-of select="$pContext/@datasrc" />
				</xsl:attribute>
			</xsl:if>
			<xsl:if test="$pContext/@doc">
				<xsl:attribute name="doc">
					<xsl:value-of select="$pContext/@doc" />
				</xsl:attribute>
			</xsl:if>
			<xsl:if test="$pContext/xformsdb:expression[ 1 ]">
				<xsl:call-template name="xformsdbInstanceXFormsDBWidgetQueryXFormsDBExpression">
					<xsl:with-param name="pContext" select="$pContext/xformsdb:expression[ 1 ]" />
				</xsl:call-template>
			</xsl:if>
			<xsl:if test="$pContext/xformsdb:xmlns">
				<xsl:for-each select="$pContext/xformsdb:xmlns">
					<xsl:call-template name="xformsdbInstanceXFormsDBWidgetQueryXFormsDBXmlns">
						<xsl:with-param name="pContext" select="." />
					</xsl:call-template>
				</xsl:for-each>
			</xsl:if>
			<xsl:if test="$pContext/xformsdb:var">
				<xsl:for-each select="$pContext/xformsdb:var">
					<xsl:call-template name="xformsdbInstanceXFormsDBWidgetQueryXFormsDBVar">
						<xsl:with-param name="pContext" select="." />
					</xsl:call-template>
				</xsl:for-each>
			</xsl:if>
			<xsl:if test="$pContext/xformsdb:secvar[ @name = 'username' ][ 1 ]">
				<xsl:call-template name="xformsdbInstanceXFormsDBWidgetQueryXFormsDBSecVar">
					<xsl:with-param name="pContext" select="$pContext/xformsdb:secvar[ @name = 'username' ][ 1 ]" />
				</xsl:call-template>
			</xsl:if>
			<xsl:if test="$pContext/xformsdb:secvar[ @name = 'roles' ][ 1 ]">
				<xsl:call-template name="xformsdbInstanceXFormsDBWidgetQueryXFormsDBSecVar">
					<xsl:with-param name="pContext" select="$pContext/xformsdb:secvar[ @name = 'roles' ][ 1 ]" />
				</xsl:call-template>
			</xsl:if>
		</xsl:element>
	</xsl:template>


	<xsl:template name="xformsdbInstanceXFormsDBWidgetQueryXFormsDBExpression">
		<xsl:param name="pContext" />
		<xsl:element name="{ $pContext/name() }" namespace="http://www.tml.tkk.fi/2007/xformsdb">
			<!-- BEGIN: For backward compability -->
			<xsl:if test="$pContext/@src">
				<xsl:attribute name="resource">
					<xsl:value-of select="$pContext/@src" />
				</xsl:attribute>
			</xsl:if>
			<!-- END: For backward compability -->
			<xsl:if test="$pContext/@resource">
				<xsl:attribute name="resource">
					<xsl:value-of select="$pContext/@resource" />
				</xsl:attribute>
			</xsl:if>
			<xsl:if test="$pContext/@*|$pContext/node()">
				<xsl:copy-of select="$pContext/@*|$pContext/node()" />
			</xsl:if>
		</xsl:element>
	</xsl:template>


	<xsl:template name="xformsdbInstanceXFormsDBWidgetQueryXFormsDBXmlns">
		<xsl:param name="pContext" />
		<xsl:element name="{ $pContext/name() }" namespace="http://www.tml.tkk.fi/2007/xformsdb">
			<xsl:if test="$pContext/@prefix">
				<xsl:attribute name="prefix">
					<xsl:value-of select="$pContext/@prefix" />
				</xsl:attribute>
			</xsl:if>
			<xsl:if test="$pContext/@uri">
				<xsl:attribute name="uri">
					<xsl:value-of select="$pContext/@uri" />
				</xsl:attribute>
			</xsl:if>
		</xsl:element>
	</xsl:template>


	<xsl:template name="xformsdbInstanceXFormsDBWidgetQueryXFormsDBVar">
		<xsl:param name="pContext" />
		<xsl:element name="{ $pContext/name() }" namespace="http://www.tml.tkk.fi/2007/xformsdb">
			<xsl:if test="$pContext/@name">
				<xsl:attribute name="name">
					<xsl:value-of select="$pContext/@name" />
				</xsl:attribute>
			</xsl:if>
		</xsl:element>
	</xsl:template>


	<xsl:template name="xformsdbInstanceXFormsDBWidgetQueryXFormsDBSecVar">
		<xsl:param name="pContext" />
		<xsl:element name="{ $pContext/name() }" namespace="http://www.tml.tkk.fi/2007/xformsdb">
			<xsl:attribute name="name">
				<xsl:value-of select="$pContext/@name" />
			</xsl:attribute>
		</xsl:element>
	</xsl:template>


	<xsl:template name="xformsdbInstanceXFormsDBState">
		<xsl:param name="pContext" />
		<xsl:element name="{ $pContext/name() }" namespace="http://www.tml.tkk.fi/2007/xformsdb" />
	</xsl:template>


	<xsl:template name="xformsdbInstanceXFormsDBLogin">
		<xsl:param name="pContext" />
		<xsl:element name="{ $pContext/name() }" namespace="http://www.tml.tkk.fi/2007/xformsdb">
			<xsl:if test="$pContext/@datasrc">
				<xsl:attribute name="datasrc">
					<xsl:value-of select="$pContext/@datasrc" />
				</xsl:attribute>
			</xsl:if>
			<xsl:if test="$pContext/@doc">
				<xsl:attribute name="doc">
					<xsl:value-of select="$pContext/@doc" />
				</xsl:attribute>
			</xsl:if>
			<xsl:if test="$pContext/xformsdb:var[ @name = 'username' ][ 1 ]">
				<xsl:call-template name="xformsdbInstanceXFormsDBLoginXFormsDBVar">
					<xsl:with-param name="pContext" select="$pContext/xformsdb:var[ @name = 'username' ][ 1 ]" />
				</xsl:call-template>
			</xsl:if>
			<xsl:if test="$pContext/xformsdb:var[ @name = 'password' ][ 1 ]">
				<xsl:call-template name="xformsdbInstanceXFormsDBLoginXFormsDBVar">
					<xsl:with-param name="pContext" select="$pContext/xformsdb:var[ @name = 'password' ][ 1 ]" />
				</xsl:call-template>
			</xsl:if>
		</xsl:element>
	</xsl:template>


	<xsl:template name="xformsdbInstanceXFormsDBLoginXFormsDBVar">
		<xsl:param name="pContext" />
		<xsl:element name="{ $pContext/name() }" namespace="http://www.tml.tkk.fi/2007/xformsdb">
			<xsl:attribute name="name">
				<xsl:value-of select="$pContext/@name" />
			</xsl:attribute>
		</xsl:element>
	</xsl:template>


	<xsl:template name="xformsdbInstanceXFormsDBLogout">
		<xsl:param name="pContext" />
		<xsl:element name="{ $pContext/name() }" namespace="http://www.tml.tkk.fi/2007/xformsdb" />
	</xsl:template>


	<xsl:template name="xformsdbInstanceXFormsDBUser">
		<xsl:param name="pContext" />
		<xsl:element name="{ $pContext/name() }" namespace="http://www.tml.tkk.fi/2007/xformsdb" />
	</xsl:template>


	<xsl:template name="xformsdbInstanceXFormsDBFile">
		<xsl:param name="pContext" />
		<xsl:element name="{ $pContext/name() }" namespace="http://www.tml.tkk.fi/2007/xformsdb">
			<xsl:if test="$pContext/xformsdb:var[ @name = 'username' ][ 1 ]">
				<xsl:call-template name="xformsdbInstanceXFormsDBFileXFormsDBVar">
					<xsl:with-param name="pContext" select="$pContext/xformsdb:var[ @name = 'username' ][ 1 ]" />
				</xsl:call-template>
			</xsl:if>
			<xsl:if test="$pContext/xformsdb:var[ @name = 'ids' ][ 1 ]">
				<xsl:call-template name="xformsdbInstanceXFormsDBFileXFormsDBVar">
					<xsl:with-param name="pContext" select="$pContext/xformsdb:var[ @name = 'ids' ][ 1 ]" />
				</xsl:call-template>
			</xsl:if>
			<xsl:if test="$pContext/xformsdb:var[ @name = 'roles' ][ 1 ]">
				<xsl:call-template name="xformsdbInstanceXFormsDBFileXFormsDBVar">
					<xsl:with-param name="pContext" select="$pContext/xformsdb:var[ @name = 'roles' ][ 1 ]" />
				</xsl:call-template>
			</xsl:if>
			<xsl:if test="$pContext/xformsdb:secvar[ @name = 'username' ][ 1 ]">
				<xsl:call-template name="xformsdbInstanceXFormsDBFileXFormsDBSecVar">
					<xsl:with-param name="pContext" select="$pContext/xformsdb:secvar[ @name = 'username' ][ 1 ]" />
				</xsl:call-template>
			</xsl:if>
			<xsl:if test="$pContext/xformsdb:secvar[ @name = 'roles' ][ 1 ]">
				<xsl:call-template name="xformsdbInstanceXFormsDBFileXFormsDBSecVar">
					<xsl:with-param name="pContext" select="$pContext/xformsdb:secvar[ @name = 'roles' ][ 1 ]" />
				</xsl:call-template>
			</xsl:if>
		</xsl:element>
	</xsl:template>


	<xsl:template name="xformsdbInstanceXFormsDBFileXFormsDBVar">
		<xsl:param name="pContext" />
		<xsl:element name="{ $pContext/name() }" namespace="http://www.tml.tkk.fi/2007/xformsdb">
			<xsl:attribute name="name">
				<xsl:value-of select="$pContext/@name" />
			</xsl:attribute>
		</xsl:element>
	</xsl:template>


	<xsl:template name="xformsdbInstanceXFormsDBFileXFormsDBSecVar">
		<xsl:param name="pContext" />
		<xsl:element name="{ $pContext/name() }" namespace="http://www.tml.tkk.fi/2007/xformsdb">
			<xsl:attribute name="name">
				<xsl:value-of select="$pContext/@name" />
			</xsl:attribute>
		</xsl:element>
	</xsl:template>


	<xsl:template name="xformsdbInstanceXFormsDBCookie">
		<xsl:param name="pContext" />
		<xsl:element name="{ $pContext/name() }" namespace="http://www.tml.tkk.fi/2007/xformsdb" />
	</xsl:template>


	<xsl:template name="xformsdbSubmission">
		<xsl:param name="pContext" />
		<xsl:element name="{ $pContext/name() }" namespace="http://www.tml.tkk.fi/2007/xformsdb">
 			<xsl:for-each select="@*">
 				<xsl:choose>
	 				<xsl:when test="name() = 'xformsdbrequesttype'" />
	 				<xsl:otherwise>
						<xsl:attribute name="{ name() }">
							<xsl:value-of select="." />
						</xsl:attribute>
	 				</xsl:otherwise>
	 			</xsl:choose>
 			</xsl:for-each>
			<xsl:if test="/xhtml:html/xhtml:head/xforms:model/xformsdb:instance[ @id = $pContext/@requestinstance ]">
				<xsl:attribute name="xformsdbrequesttype">
					<xsl:choose>
						<xsl:when test="/xhtml:html/xhtml:head/xforms:model/xformsdb:instance[ @id = $pContext/@requestinstance ]/xformsdb:query">
							<xsl:text>query</xsl:text>
						</xsl:when>
						<xsl:when test="/xhtml:html/xhtml:head/xforms:model/xformsdb:instance[ @id = $pContext/@requestinstance ]/xformsdb:widgetquery">
							<xsl:text>widgetquery</xsl:text>
						</xsl:when>
						<xsl:when test="/xhtml:html/xhtml:head/xforms:model/xformsdb:instance[ @id = $pContext/@requestinstance ]/xformsdb:state">
							<xsl:text>state</xsl:text>
						</xsl:when>
						<xsl:when test="/xhtml:html/xhtml:head/xforms:model/xformsdb:instance[ @id = $pContext/@requestinstance ]/xformsdb:login">
							<xsl:text>login</xsl:text>
						</xsl:when>
						<xsl:when test="/xhtml:html/xhtml:head/xforms:model/xformsdb:instance[ @id = $pContext/@requestinstance ]/xformsdb:logout">
							<xsl:text>logout</xsl:text>
						</xsl:when>
						<xsl:when test="/xhtml:html/xhtml:head/xforms:model/xformsdb:instance[ @id = $pContext/@requestinstance ]/xformsdb:user">
							<xsl:text>user</xsl:text>
						</xsl:when>
						<xsl:when test="/xhtml:html/xhtml:head/xforms:model/xformsdb:instance[ @id = $pContext/@requestinstance ]/xformsdb:file">
							<xsl:text>file</xsl:text>
						</xsl:when>
						<xsl:when test="/xhtml:html/xhtml:head/xforms:model/xformsdb:instance[ @id = $pContext/@requestinstance ]/xformsdb:cookie">
							<xsl:text>cookie</xsl:text>
						</xsl:when>
						<xsl:otherwise />
					</xsl:choose>
				</xsl:attribute>
			</xsl:if>
			<xsl:if test="$pContext/@id">
				<xsl:attribute name="id">
					<xsl:value-of select="$pContext/@id" />
				</xsl:attribute>
			</xsl:if>
			<xsl:if test="$pContext/@replace">
				<xsl:attribute name="replace">
					<xsl:value-of select="$pContext/@replace" />
				</xsl:attribute>
			</xsl:if>
			<xsl:if test="$pContext/@replace">
				<xsl:attribute name="replacetype">
					<xsl:value-of select="$pContext/@replace" />
				</xsl:attribute>
			</xsl:if>
			<xsl:if test="$pContext/@instance">
				<xsl:attribute name="instance">
					<xsl:value-of select="$pContext/@instance" />
				</xsl:attribute>
			</xsl:if>
			<xsl:if test="$pContext/@requestinstance">
				<xsl:attribute name="requestinstance">
					<xsl:value-of select="$pContext/@requestinstance" />
				</xsl:attribute>
			</xsl:if>
			<xsl:if test="$pContext/@expressiontype">
				<xsl:attribute name="expressiontype">
					<xsl:value-of select="$pContext/@expressiontype" />
				</xsl:attribute>
			</xsl:if>
			<xsl:if test="$pContext/@attachmentinstance">
				<xsl:attribute name="attachmentinstance">
					<xsl:value-of select="$pContext/@attachmentinstance" />
				</xsl:attribute>
			</xsl:if>
			<xsl:if test="$pContext/@statetype">
				<xsl:attribute name="statetype">
					<xsl:value-of select="$pContext/@statetype" />
				</xsl:attribute>
			</xsl:if>
			<xsl:if test="$pContext/@filetype">
				<xsl:attribute name="filetype">
					<xsl:value-of select="$pContext/@filetype" />
				</xsl:attribute>
			</xsl:if>
			<xsl:if test="$pContext/parent::xforms:model/@id">
				<xsl:attribute name="model">
					<xsl:value-of select="$pContext/parent::xforms:model/@id" />
				</xsl:attribute>
			</xsl:if>
			<xsl:if test="$pContext/parent::xforms:model">
				<xsl:attribute name="proxyinstance">
					<xsl:value-of select="concat( $paramXFormsDBResponseProxyInstance, '-', count( $pContext/parent::xforms:model/preceding-sibling::xforms:model ) + 1 )" />
				</xsl:attribute>
			</xsl:if>
		</xsl:element>
	</xsl:template>


	<xsl:template name="xformsrtcConnection">
		<xsl:param name="pContext" />
		<xsl:element name="{ $pContext/name() }" namespace="http://cs.aalto.fi/2016/xformsrtc">
 			<xsl:for-each select="@*">
				<xsl:attribute name="{ name() }">
					<xsl:value-of select="." />
				</xsl:attribute>
 			</xsl:for-each>
			<xsl:if test="$pContext/@id">
				<xsl:attribute name="id">
					<xsl:value-of select="$pContext/@id" />
				</xsl:attribute>
			</xsl:if>
			<xsl:if test="$pContext/@resource">
				<xsl:attribute name="resource">
					<xsl:value-of select="$pContext/@resource" />
				</xsl:attribute>
			</xsl:if>
			<xsl:if test="$pContext/xforms:resource/@value">
				<xsl:attribute name="resourcechild">
					<xsl:value-of select="$pContext/xforms:resource/@value" />
				</xsl:attribute>
			</xsl:if>
			<xsl:if test="$pContext/@ref">
				<xsl:attribute name="ref">
					<xsl:value-of select="$pContext/@ref" />
				</xsl:attribute>
			</xsl:if>
			<xsl:if test="$pContext/@replace">
				<xsl:attribute name="replace">
					<xsl:value-of select="$pContext/@replace" />
				</xsl:attribute>
			</xsl:if>
			<xsl:if test="$pContext/@instance">
				<xsl:attribute name="instance">
					<xsl:value-of select="$pContext/@instance" />
				</xsl:attribute>
			</xsl:if>
			<xsl:if test="$pContext/parent::xforms:model/@id">
				<xsl:attribute name="model">
					<xsl:value-of select="$pContext/parent::xforms:model/@id" />
				</xsl:attribute>
			</xsl:if>
			<xsl:if test="$pContext/parent::xforms:model">
				<xsl:attribute name="xformsrtcconnectionindex">
					<xsl:value-of select="( count( $pContext/preceding::xformsrtc:connection ) + 1 )" />
					<!-- <xsl:value-of select="( count( $pContext/preceding-sibling::xformsrtc:connection ) + count( $pContext/parent::xforms:model/preceding-sibling::xforms:model/xformsrtc:connection ) + 1 )" /> -->
				</xsl:attribute>
			</xsl:if>
			<xsl:if test="$pContext/parent::xforms:model/*[ local-name() = 'instance' and ( namespace-uri() = 'http://www.w3.org/2002/xforms' or namespace-uri() = 'http://www.tml.tkk.fi/2007/xformsdb' ) ]">
				<xsl:attribute name="xformsfirstinstance">
					<xsl:value-of select="$pContext/parent::xforms:model/*[ local-name() = 'instance' and ( namespace-uri() = 'http://www.w3.org/2002/xforms' or namespace-uri() = 'http://www.tml.tkk.fi/2007/xformsdb' ) ][ 1 ]/@id" />
				</xsl:attribute>
			</xsl:if>
		</xsl:element>	
	</xsl:template>


	<xsl:template name="xformsrtcConnect">
		<xsl:param name="pContext" />
		<xsl:element name="{ $pContext/name() }" namespace="http://cs.aalto.fi/2016/xformsrtc">
			<xsl:if test="$pContext/@connection">
				<xsl:attribute name="connection">
					<xsl:value-of select="$pContext/@connection" />
				</xsl:attribute>
			</xsl:if>
		</xsl:element>
	</xsl:template>


	<xsl:template name="xformsrtcDisconnect">
		<xsl:param name="pContext" />
		<xsl:element name="{ $pContext/name() }" namespace="http://cs.aalto.fi/2016/xformsrtc">
			<xsl:if test="$pContext/@connection">
				<xsl:attribute name="connection">
					<xsl:value-of select="$pContext/@connection" />
				</xsl:attribute>
			</xsl:if>
		</xsl:element>
	</xsl:template>


	<xsl:template name="xformsrtcSend">
		<xsl:param name="pContext" />
		<xsl:element name="{ $pContext/name() }" namespace="http://cs.aalto.fi/2016/xformsrtc">
			<xsl:if test="$pContext/@connection">
				<xsl:attribute name="connection">
					<xsl:value-of select="$pContext/@connection" />
				</xsl:attribute>
			</xsl:if>
		</xsl:element>
	</xsl:template>


	<xsl:template name="xformsLoad">
		<xsl:param name="pContext" />
		<xsl:element name="{ $pContext/name() }" namespace="http://www.w3.org/2002/xforms">
			<xsl:if test="$pContext/@resource">
				<xsl:attribute name="resource">
					<xsl:value-of select="$pContext/@resource" />
				</xsl:attribute>
			</xsl:if>
		</xsl:element>
	</xsl:template>


	<xsl:template name="xformsSubmission">
		<xsl:param name="pContext" />
		<xsl:element name="{ $pContext/name() }" namespace="http://www.w3.org/2002/xforms">
			<xsl:if test="$pContext/@action">
				<xsl:attribute name="action">
					<xsl:value-of select="$pContext/@action" />
				</xsl:attribute>
			</xsl:if>
		</xsl:element>
	</xsl:template>


	<xsl:template name="xhtmlMeta">
		<xsl:param name="pContext" />
		<xsl:element name="{ $pContext/name() }" namespace="http://www.w3.org/1999/xhtml">
			<xsl:if test="$pContext/@content">
				<xsl:attribute name="content">
					<xsl:value-of select="$pContext/@content" />
				</xsl:attribute>
			</xsl:if>
		</xsl:element>
	</xsl:template>


	<xsl:template name="xhtmlA">
		<xsl:param name="pContext" />
		<xsl:element name="{ $pContext/name() }" namespace="http://www.w3.org/1999/xhtml">
			<xsl:if test="$pContext/@href">
				<xsl:attribute name="href">
					<xsl:value-of select="$pContext/@href" />
				</xsl:attribute>
			</xsl:if>
		</xsl:element>
	</xsl:template>



</xsl:stylesheet>