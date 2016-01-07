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
 * Transform the <xformsrtc:disconnect> elements.
 * 
 * The transformed <xformsrtc:disconnect> elements will be given
 * as an XML formatted parameter to the xformsdb_xforms.xsl stylesheet
 * which replaces all <xformsrtc:disconnect> elements with
 * an appropriate transformation result.
 *
 * @author Markku Laine
 * @version 1.0	 Created on January 7, 2016
 */
public class XFormsRTCDisconnectsTransformer {

	

	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger		= Logger.getLogger( XFormsRTCDisconnectsTransformer.class );
	
	
	// PRIVATE CONSTURCTORS
	// Prevent instantiation of this class
	private XFormsRTCDisconnectsTransformer() {
		logger.log( Level.DEBUG, "Constructor has been called." );
	}
	
	
	// PUBLIC STATIC METHODS
	public static void transform( Document document ) throws TransformerException {
		logger.log( Level.DEBUG, "Method has been called." );

		
		try {
			Element xformsrtcDisconnectsElement		= document.getRootElement().getFirstChildElement( "disconnects", "http://cs.aalto.fi/2016/xformsrtc" );
			Elements xformsrtcDisconnectElements	= xformsrtcDisconnectsElement.getChildElements( "disconnect", "http://cs.aalto.fi/2016/xformsrtc" );
			Element xformsrtcDisconnectElement		= null;
			Element xformsrtcConnectionsElement		= document.getRootElement().getFirstChildElement( "connections", "http://cs.aalto.fi/2016/xformsrtc" );
			Elements xformsrtcConnectionElements	= xformsrtcConnectionsElement.getChildElements( "connection", "http://cs.aalto.fi/2016/xformsrtc" );
			Element xformsrtcConnectionElement		= null;
			for ( int i = 0; i < xformsrtcDisconnectElements.size(); i++ ) {
				xformsrtcDisconnectElement				= xformsrtcDisconnectElements.get( i );
				
				// Validate
				if (	xformsrtcDisconnectElement.getAttribute( "connection" ) == null ||
						"".equals( xformsrtcDisconnectElement.getAttributeValue( "connection" ) ) == true ) {
					throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_106, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_106 );
				}

				// Iterate over <xformsrtc:connection> elements in order to find the matching connection
				boolean matchFound					= false;
				for ( int j = 0; j < xformsrtcConnectionElements.size(); j++ ) {
					xformsrtcConnectionElement		= xformsrtcConnectionElements.get( j );

					// Match found
					if (	( xformsrtcDisconnectElement.getAttributeValue( "connection" ).equals( xformsrtcConnectionElement.getAttributeValue( "id" ) ) == true ) &&
							xformsrtcConnectionElement.getAttributeValue( "xformsrtcconnectionindex" ) != null ) {
						// Add xformsrtcconnectionindex
						xformsrtcDisconnectElement.addAttribute( new Attribute( "xformsrtcconnectionindex", xformsrtcConnectionElement.getAttributeValue( "xformsrtcconnectionindex" ) ) );
						matchFound					= true;
						break;
					}
				}
				
				if ( matchFound == false ) {
					throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_107, "Missing a matching <xformsrtc:connection id=\"" + xformsrtcDisconnectElement.getAttributeValue( "connection" ) + "\"> element for the <xformsrtc:disconnect connection=\"" + xformsrtcDisconnectElement.getAttributeValue( "connection" ) + "\"> element." );
				}				
			}

			
			logger.log( Level.DEBUG, "The <xformsrtc:disconnect> elements have been successfully transformed." );			
		} catch ( TransformerException tex ) {
			throw tex;
		} catch ( Exception ex ) {
			throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_96, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_96, ex );
		}
	}	
}