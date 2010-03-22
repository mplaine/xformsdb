<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
				xmlns:saxon="http://saxon.sf.net/"
				xmlns:xformsdb="http://www.tml.tkk.fi/2007/xformsdb"
				xmlns:func="http://www.tml.tkk.fi/2007/xformsdb/xsl/functions" 
				xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
				exclude-result-prefixes="saxon func">
	<!-- Import the identity transformation -->
	<xsl:import href="identity.xsl" />
	<!-- Define the output format -->
	<xsl:output method="xml" version="1.0" />
 	<!-- Define the parameters -->
	<xsl:param name="paramXFormsDBUserXMLString" select="/.." />



	<!--
		XFormsDB XSL Function:
		Tokenize string and normalize spaces from the tokenized strings.
	-->
	<xsl:function name="func:tokenize-and-normalize-space">
		<xsl:param name="pString" />
		<xsl:param name="pPattern" />
		<!-- Tokenize string -->
 		<xsl:variable name="varTokenStrings" select="tokenize( $pString, $pPattern )" />
		<!-- Normalized string -->
		<xsl:variable name="varNormalizedString">
			<!-- Iterate over token strings -->
	 		<xsl:for-each select="$varTokenStrings">
				<!-- Normalize token string -->
	 			<xsl:variable name="varNormalizedTokenString" select="normalize-space( . )" />
	 			<xsl:value-of select="$varNormalizedTokenString" />
	 			<!-- Add token string separator if needed -->
	 			<xsl:if test="position() != last()">
		 			<xsl:value-of select="$pPattern" />
	 			</xsl:if>
	 		</xsl:for-each>
 		</xsl:variable>
		<!-- Tokenize normalized string -->
 		<xsl:sequence select="tokenize( $varNormalizedString, $pPattern )" />
	</xsl:function>


	<!--
		XFormsDB XSL Function:
		Retrieve the role match sequence, which has been obtained by analyzing the attribute value of the <xformsdb:secview> element
	-->
	<xsl:function name="func:get-role-match-sequence">
		<xsl:param name="pSequence" />
		<xsl:param name="pPattern" />
		<!-- Role match string containing a sequence of values. Possible values are 1 (match) and 0 (no match) -->
		<xsl:variable name="roleMatchString">
			<xsl:for-each select="$pSequence">
				<xsl:choose>
					<!-- Add 1 (match) to the role match string if the user has the role -->
					<xsl:when test="func:tokenize-and-normalize-space( saxon:parse( $paramXFormsDBUserXMLString )/xformsdb:user/@roles, ' ' ) = .">
						<xsl:value-of select="'1'" />
						<xsl:if test="position() != last()">
				 			<xsl:value-of select="$pPattern" />
			 			</xsl:if>
					</xsl:when>
					<!-- Add 0 (no match) to the role match string if the user does not have the role -->
					<xsl:otherwise>
						<xsl:value-of select="'0'" />
						<xsl:if test="position() != last()">
				 			<xsl:value-of select="$pPattern" />
			 			</xsl:if>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:for-each>
		</xsl:variable>
		<!-- Tokenize the role match string -->
		<xsl:sequence select="tokenize( $roleMatchString, $pPattern )" />
	</xsl:function> 


	<!-- Add DOCTYPE -->
	<xsl:template match="/">
		<!-- Apply further templates -->
		<xsl:apply-templates />
	</xsl:template>
	

	<!-- Handle the <xformsdb:secview> element -->
	<xsl:template match="//xformsdb:secview">
		<xsl:choose>
			<!-- The element does not have valid attribute(s) and the user is not logged in -->
			<xsl:when test="not( exists( @noallroles ) ) and not( exists( @noroles ) ) and not( exists( @allroles ) ) and not( exists( @roles ) ) and not( exists( saxon:parse( $paramXFormsDBUserXMLString )/xformsdb:user/@username ) )">
				<!-- Render the XML fragment -->
				<xsl:apply-templates />
			</xsl:when>
			<!-- The element does not have valid attribute(s) and the user is logged in -->
			<xsl:when test="not( exists( @noallroles ) ) and not( exists( @noroles ) ) and not( exists( @allroles ) ) and not( exists( @roles ) ) and exists( saxon:parse( $paramXFormsDBUserXMLString )/xformsdb:user/@username )">
				<!-- Do not render the XML fragment -->
			</xsl:when>
			<!-- The element has valid attribute(s) and the user is not logged in -->
			<xsl:when test="( exists( @noallroles ) or exists( @noroles ) or exists( @allroles ) or exists( @roles ) ) and not( exists( saxon:parse( $paramXFormsDBUserXMLString )/xformsdb:user/@username ) )">
				<!-- Do not render the XML fragment -->
			</xsl:when>
			<!-- The element has valid attribute(s) and the user is logged in -->
			<xsl:otherwise>
				<!-- Handle the noroles attribute of the element -->
				<xsl:variable name="varXFormsDBSecViewNoRoles" select="func:tokenize-and-normalize-space( @noroles, ' ' )" />
				<xsl:variable name="varRoleMatchSequence">
					<xsl:choose>
						<!-- The element does not have the noroles attribute or the attribute value is an empty string -->
						<xsl:when test="not( exists( @noroles ) ) or string-length( normalize-space( @noroles ) ) = 0">
							<!-- Continue -->
							<xsl:sequence select="tokenize( '2', ' ' )" />
						</xsl:when>
						<xsl:otherwise>
							<!-- Get the role match sequence -->
							<xsl:sequence select="func:get-role-match-sequence( $varXFormsDBSecViewNoRoles, ' ' )" />
						</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<!-- Analyze the role match sequence -->
				<xsl:choose>
					<!-- The user has one of the roles defined in the attribute -->
					<xsl:when test="contains( $varRoleMatchSequence, '1' ) and not( contains( $varRoleMatchSequence, '2' ) )">
						<!-- Do not render the XML fragment -->
					</xsl:when>
					<!-- The user does not have any of the roles defined in the attribute -->
					<xsl:otherwise>
						<!-- Handle the noallroles attribute of the element -->
						<xsl:variable name="varXFormsDBSecViewNoAllRoles" select="func:tokenize-and-normalize-space( @noallroles, ' ' )" />
						<xsl:variable name="varRoleMatchSequence">
							<xsl:choose>
								<!-- The element does not have the noallroles attribute or the attribute value is an empty string -->
								<xsl:when test="not( exists( @noallroles ) ) or string-length( normalize-space( @noallroles ) ) = 0">
									<!-- Continue -->
									<xsl:sequence select="tokenize( '2' ,' ' )" />
								</xsl:when>
								<xsl:otherwise>
									<!-- Get the role match sequence -->
									<xsl:sequence select="func:get-role-match-sequence( $varXFormsDBSecViewNoAllRoles, ' ' )" />
								</xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
						<!-- Analyze the role match sequence -->
						<xsl:choose>
							<!-- The user has all the roles defined in the attribute -->
							<xsl:when test="not( contains( $varRoleMatchSequence, '0' ) ) and not( contains( $varRoleMatchSequence, '2' ) )">
								<!-- Do not render the XML fragment -->
							</xsl:when>
							<!-- The user does not have all the roles defined in the attribute -->
							<xsl:otherwise>
								<!-- Handle the allroles attribute of the element -->
								<xsl:variable name="varXFormsDBSecViewAllRoles" select="func:tokenize-and-normalize-space( @allroles, ' ' )" />
								<xsl:variable name="varRoleMatchSequence">
									<xsl:choose>
										<!-- The element does not have the allroles attribute or the attribute value is an empty string -->
										<xsl:when test="not( exists( @allroles ) ) or string-length( normalize-space( @allroles ) ) = 0">
											<!-- Continue -->
											<xsl:sequence select="tokenize( '2' ,' ' )" />
										</xsl:when>
										<xsl:otherwise>
											<!-- Get the role match sequence -->
											<xsl:sequence select="func:get-role-match-sequence( $varXFormsDBSecViewAllRoles, ' ' )" />
										</xsl:otherwise>
									</xsl:choose>
								</xsl:variable>
								<!-- Analyze the role match sequence -->
								<xsl:choose>
									<!-- The user has all the roles defined in the attribute -->
									<xsl:when test="not( contains( $varRoleMatchSequence, '0' ) ) and not( contains( $varRoleMatchSequence, '2' ) )">
										<!-- Render the XML fragment -->
										<xsl:apply-templates />
									</xsl:when>
									<!-- The user does not have all the roles defined in the attribute -->
									<xsl:otherwise>
										<!-- Handle the roles attribute of the element -->
										<xsl:variable name="varXFormsDBSecViewRoles" select="func:tokenize-and-normalize-space( @roles, ' ' )" />
										<xsl:variable name="varRoleMatchSequence">
											<xsl:choose>
												<!-- The element does not have the roles attribute or the attribute value is an empty string -->
												<xsl:when test="not( exists( @roles ) ) or string-length( normalize-space( @roles ) ) = 0">
													<!-- Continue -->
													<xsl:sequence select="tokenize( '2' ,' ' )" />
												</xsl:when>
												<xsl:otherwise>
													<!-- Get the role match sequence -->
													<xsl:sequence select="func:get-role-match-sequence( $varXFormsDBSecViewRoles, ' ' )" />
												</xsl:otherwise>
											</xsl:choose>
										</xsl:variable>
										<!-- Analyze the role match sequence -->
										<xsl:choose>
											<!-- The user has one of the roles defined in the attribute -->
											<xsl:when test="contains( $varRoleMatchSequence, '1' ) and not( contains( $varRoleMatchSequence, '2' ) )">
												<!-- Render the XML fragment -->
												<xsl:apply-templates />
											</xsl:when>
											<!-- The user does not have any of the roles defined in the attribute -->
											<xsl:otherwise>
												<!-- Finalize the analysis -->
												<xsl:choose>
													<!-- The user did not meet the conditions defined in the negation attributes -->
													<xsl:when test="( ( exists( @noroles ) and ( string-length( normalize-space( @noroles ) ) &gt; 0 ) ) or ( exists( @noallroles ) and ( string-length( normalize-space( @noallroles ) ) &gt; 0 ) ) ) and not( exists( @roles ) )">
														<!-- Render the XML fragment -->
														<xsl:apply-templates />
													</xsl:when>
													<!-- The user did not meet the conditions defined in the attributes -->
													<xsl:otherwise>
														<!-- Do not render the XML fragment -->														
													</xsl:otherwise>
												</xsl:choose>
											</xsl:otherwise>
										</xsl:choose>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	


</xsl:stylesheet>