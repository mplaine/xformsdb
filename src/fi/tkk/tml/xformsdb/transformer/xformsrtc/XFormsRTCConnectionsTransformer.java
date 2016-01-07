package fi.tkk.tml.xformsdb.transformer.xformsrtc;

import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fi.tkk.tml.xformsdb.error.ErrorConstants;
import fi.tkk.tml.xformsdb.error.TransformerException;
import fi.tkk.tml.xformsdb.util.StringUtils;


/**
 * Transform the <xformsrtc:connection> elements.
 * 
 * The transformed <xformsrtc:connection> elements will be given
 * as an XML formatted parameter to the xformsdb_xforms.xsl stylesheet
 * which replaces all <xformsrtc:connection> elements with
 * an appropriate transformation result.
 *
 * @author Markku Laine
 * @version 1.0	 Created on January 7, 2016
 */
public class XFormsRTCConnectionsTransformer {

	

	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger		= Logger.getLogger( XFormsRTCConnectionsTransformer.class );
	
	
	// PRIVATE CONSTURCTORS
	// Prevent instantiation of this class
	private XFormsRTCConnectionsTransformer() {
		logger.log( Level.DEBUG, "Constructor has been called." );
	}
	
	
	// PUBLIC STATIC METHODS
	public static void transform( HttpServletResponse response, Document document ) throws TransformerException {
		logger.log( Level.DEBUG, "Method has been called." );


		try {
			Element xformsrtcConnectionsElement		= document.getRootElement().getFirstChildElement( "connections", "http://cs.aalto.fi/2016/xformsrtc" );
			Elements xformsrtcConnectionElements	= xformsrtcConnectionsElement.getChildElements( "connection", "http://cs.aalto.fi/2016/xformsrtc" );
			Element xformsrtcConnectionElement		= null;
			for ( int i = 0; i < xformsrtcConnectionElements.size(); i++ ) {
				xformsrtcConnectionElement			= xformsrtcConnectionElements.get( i );

				// Update missing properties to defaults
				if (	xformsrtcConnectionElement.getAttribute( "resource" ) == null ) { // Similar to the <xforms:submission> element's default behavior
						xformsrtcConnectionElement.addAttribute( new Attribute( "resource", "" ) );
				}
				if (	xformsrtcConnectionElement.getAttribute( "instance" ) == null ) { // Same as the <xforms:submission> element's default behavior
					if ( xformsrtcConnectionElement.getAttribute( "ref" ) == null ) {
						xformsrtcConnectionElement.addAttribute( new Attribute( "instance", xformsrtcConnectionElement.getAttributeValue( "xformsfirstinstance" ) ) );
					}
					else {
						String refValue	= xformsrtcConnectionElement.getAttributeValue( "ref" ).replace( " ", "" );
						// Take everything between instance(' and ') or instance(" and ")
						if ( refValue.startsWith( "instance('" ) == true || refValue.startsWith( "instance(\"" ) == true ) {
							String instanceValue	= refValue.replace( "instance('", "" ).replace( "instance(\"", "" ).replace( "')", "" ).replace( "\")", "" );
							xformsrtcConnectionElement.addAttribute( new Attribute( "instance", instanceValue ) );
						}
						else {
							xformsrtcConnectionElement.addAttribute( new Attribute( "instance", xformsrtcConnectionElement.getAttributeValue( "xformsfirstinstance" ) ) );
						}
					}
				}
				if (	xformsrtcConnectionElement.getAttribute( "ref" ) == null ) { // Similar to the <xforms:submission> element's default behavior
						xformsrtcConnectionElement.addAttribute( new Attribute( "ref", "instance( '" + xformsrtcConnectionElement.getAttributeValue( "xformsfirstinstance" ) + "' )" ) );
				}
				if (	xformsrtcConnectionElement.getAttribute( "replace" ) == null ) { // Differs from the <xforms:submission> element's default value "all"
						xformsrtcConnectionElement.addAttribute( new Attribute( "replace", "instance" ) );
				}

				// Validate
				if (	xformsrtcConnectionElement.getAttribute( "id" ) == null ||
						"".equals( xformsrtcConnectionElement.getAttributeValue( "id" ) ) == true ) {
						throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_99, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_99 );
				}
				if (	xformsrtcConnectionElement.getAttribute( "resource" ) == null ||
						"".equals( xformsrtcConnectionElement.getAttributeValue( "resource" ) ) == true ) {
						throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_100, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_100 );
				}
				if (	xformsrtcConnectionElement.getAttribute( "ref" ) == null ||
						"".equals( xformsrtcConnectionElement.getAttributeValue( "ref" ) ) == true ) {
						throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_101, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_101 );
				}
				if ( 	"instance".equals( xformsrtcConnectionElement.getAttributeValue( "replace" ) ) == false ) {
						throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_102, "Illegal replace attribute value '" + xformsrtcConnectionElement.getAttributeValue( "replace" ) + "' in the <xformsrtc:connection" + ( ( xformsrtcConnectionElement.getAttribute( "id" ) != null ) ? " id=\"" + xformsrtcConnectionElement.getAttributeValue( "id" ) + "\">" : ">" ) + " element. Valid values are: instance." );
				}
				if (	xformsrtcConnectionElement.getAttribute( "instance" ) == null ||
						"".equals( xformsrtcConnectionElement.getAttributeValue( "instance" ) ) == true ) {
						throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_103, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_103 );
				}
				
				// Update resource
				if ( xformsrtcConnectionElement.getAttribute( "resource" ) != null ) {
					xformsrtcConnectionElement.addAttribute( new Attribute( "resource", StringUtils.toSafeASCIIString( xformsrtcConnectionElement.getAttributeValue( "resource" ), false, response ) ) );						
				}
			}
			
			
			logger.log( Level.DEBUG, "The <xformsrtc:connection> elements have been successfully transformed." );			
		} catch ( TransformerException tex ) {
			throw tex;
		} catch ( Exception ex ) {
			throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_98, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_98, ex );
		}
	}	
}