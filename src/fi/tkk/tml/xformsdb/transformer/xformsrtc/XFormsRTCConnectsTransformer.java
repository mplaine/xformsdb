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
 * Transform the <xformsrtc:connect> elements.
 * 
 * The transformed <xformsrtc:connect> elements will be given
 * as an XML formatted parameter to the xformsdb_xforms.xsl stylesheet
 * which replaces all <xformsrtc:connect> elements with
 * an appropriate transformation result.
 *
 * @author Markku Laine
 * @version 1.0	 Created on January 5, 2016
 */
public class XFormsRTCConnectsTransformer {

	

	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger		= Logger.getLogger( XFormsRTCConnectsTransformer.class );
	
	
	// PRIVATE CONSTURCTORS
	// Prevent instantiation of this class
	private XFormsRTCConnectsTransformer() {
		logger.log( Level.DEBUG, "Constructor has been called." );
	}
	
	
	// PUBLIC STATIC METHODS
	public static void transform( Document document ) throws TransformerException {
		logger.log( Level.DEBUG, "Method has been called." );

		
		try {
			Element xformsrtcConnectsElement		= document.getRootElement().getFirstChildElement( "connects", "http://cs.aalto.fi/2016/xformsrtc" );
			Elements xformsrtcConnectElements		= xformsrtcConnectsElement.getChildElements( "connect", "http://cs.aalto.fi/2016/xformsrtc" );
			Element xformsrtcConnectElement			= null;
			Element xformsrtcConnectionsElement		= document.getRootElement().getFirstChildElement( "connections", "http://cs.aalto.fi/2016/xformsrtc" );
			Elements xformsrtcConnectionElements	= xformsrtcConnectionsElement.getChildElements( "connection", "http://cs.aalto.fi/2016/xformsrtc" );
			Element xformsrtcConnectionElement		= null;
			for ( int i = 0; i < xformsrtcConnectElements.size(); i++ ) {
				xformsrtcConnectElement				= xformsrtcConnectElements.get( i );
				
				// Validate
				if (	xformsrtcConnectElement.getAttribute( "connection" ) == null ||
						"".equals( xformsrtcConnectElement.getAttributeValue( "connection" ) ) == true ) {
					throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_104, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_104 );
				}

				// Iterate over <xformsrtc:connection> elements in order to find the matching connection
				boolean matchFound					= false;
				for ( int j = 0; j < xformsrtcConnectionElements.size(); j++ ) {
					xformsrtcConnectionElement		= xformsrtcConnectionElements.get( j );

					// Match found
					if (	( xformsrtcConnectElement.getAttributeValue( "connection" ).equals( xformsrtcConnectionElement.getAttributeValue( "id" ) ) == true ) &&
							xformsrtcConnectionElement.getAttributeValue( "xformsrtcconnectionindex" ) != null ) {
						// Add xformsrtcconnectionindex
						xformsrtcConnectElement.addAttribute( new Attribute( "xformsrtcconnectionindex", xformsrtcConnectionElement.getAttributeValue( "xformsrtcconnectionindex" ) ) );
						matchFound					= true;
						break;
					}
				}
				
				if ( matchFound == false ) {
					throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_105, "Missing a matching <xformsrtc:connection id=\"" + xformsrtcConnectElement.getAttributeValue( "connection" ) + "\"> element for the <xformsrtc:connect connection=\"" + xformsrtcConnectElement.getAttributeValue( "connection" ) + "\"> element." );
				}				
			}

			
			logger.log( Level.DEBUG, "The <xformsrtc:connect> elements have been successfully transformed." );
		} catch ( TransformerException tex ) {
			throw tex;
		} catch ( Exception ex ) {
			throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_94, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_94, ex );
		}
	}	
}