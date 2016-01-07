package fi.tkk.tml.xformsdb.transformer.xformsrtc;

import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fi.tkk.tml.xformsdb.error.ErrorConstants;
import fi.tkk.tml.xformsdb.error.TransformerException;


/**
 * Transform the <xformsrtc:send> elements.
 * 
 * The transformed <xformsrtc:send> elements will be given
 * as an XML formatted parameter to the xformsdb_xforms.xsl stylesheet
 * which replaces all <xformsrtc:send> elements with
 * an appropriate transformation result.
 *
 * @author Markku Laine
 * @version 1.0	 Created on January 7, 2016
 */
public class XFormsRTCSendsTransformer {

	

	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger		= Logger.getLogger( XFormsRTCSendsTransformer.class );
	
	
	// PRIVATE CONSTURCTORS
	// Prevent instantiation of this class
	private XFormsRTCSendsTransformer() {
		logger.log( Level.DEBUG, "Constructor has been called." );
	}
	
	
	// PUBLIC STATIC METHODS
	public static void transform( Document document ) throws TransformerException {
		logger.log( Level.DEBUG, "Method has been called." );

		
		try {
			Element xformsrtcSendsElement			= document.getRootElement().getFirstChildElement( "sends", "http://cs.aalto.fi/2016/xformsrtc" );
			Elements xformsrtcSendElements			= xformsrtcSendsElement.getChildElements( "send", "http://cs.aalto.fi/2016/xformsrtc" );
			Element xformsrtcSendElement			= null;
			Element xformsrtcConnectionsElement		= document.getRootElement().getFirstChildElement( "connections", "http://cs.aalto.fi/2016/xformsrtc" );
			Elements xformsrtcConnectionElements	= xformsrtcConnectionsElement.getChildElements( "connection", "http://cs.aalto.fi/2016/xformsrtc" );
			Element xformsrtcConnectionElement		= null;
			for ( int i = 0; i < xformsrtcSendElements.size(); i++ ) {
				xformsrtcSendElement				= xformsrtcSendElements.get( i );
				
				// Validate
				if (	xformsrtcSendElement.getAttribute( "connection" ) == null ||
						"".equals( xformsrtcSendElement.getAttributeValue( "connection" ) ) == true ) {
					throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_109, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_109 );
				}

				// Iterate over <xformsrtc:connection> elements in order to find the matching connection
				boolean matchFound					= false;
				for ( int j = 0; j < xformsrtcConnectionElements.size(); j++ ) {
					xformsrtcConnectionElement		= xformsrtcConnectionElements.get( j );

					// Match found
					if (	( xformsrtcSendElement.getAttributeValue( "connection" ).equals( xformsrtcConnectionElement.getAttributeValue( "id" ) ) == true ) &&
							xformsrtcConnectionElement.getAttributeValue( "xformsrtcconnectionindex" ) != null ) {
						// Add xformsrtcconnectionindex
						xformsrtcSendElement.addAttribute( new Attribute( "xformsrtcconnectionindex", xformsrtcConnectionElement.getAttributeValue( "xformsrtcconnectionindex" ) ) );
						matchFound					= true;
						break;
					}
				}
				
				if ( matchFound == false ) {
					throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_110, "Missing a matching <xformsrtc:connection id=\"" + xformsrtcSendElement.getAttributeValue( "connection" ) + "\"> element for the <xformsrtc:send connection=\"" + xformsrtcSendElement.getAttributeValue( "connection" ) + "\"> element." );
				}				
			}

			
			logger.log( Level.DEBUG, "The <xformsrtc:send> elements have been successfully transformed." );			
		} catch ( TransformerException tex ) {
			throw tex;
		} catch ( Exception ex ) {
			throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_111, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_111, ex );
		}
	}	
}