package fi.tkk.tml.xformsdb.transformer.xhtml;

import javax.servlet.http.HttpServletResponse;

import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fi.tkk.tml.xformsdb.error.ErrorConstants;
import fi.tkk.tml.xformsdb.error.TransformerException;
import fi.tkk.tml.xformsdb.util.StringUtils;


/**
 * Transform the <xhtml:a> elements.
 * 
 * The transformed <xhtml:a> elements will be given
 * as an XML formatted parameter to the xformsdb_xforms.xsl stylesheet
 * which replaces all <xhtml:a> elements with
 * an appropriate transformation result.
 *
 * @author Markku Laine
 * @version 1.0	 Created on November 14, 2009
 */
public class XHTMLAsTransformer {

	

	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger	= Logger.getLogger( XHTMLAsTransformer.class );

	
	// PRIVATE CONSTURCTORS
	// Prevent instantiation of this class
	private XHTMLAsTransformer() {
		logger.log( Level.DEBUG, "Constructor has been called." );
	}
	
	
	// PUBLIC STATIC METHODS
	public static void transform( HttpServletResponse response, Document document ) throws TransformerException {
		logger.log( Level.DEBUG, "Method has been called." );

		
		try {
			Element xhtmlAsElement		= document.getRootElement().getFirstChildElement( "as", "http://www.w3.org/1999/xhtml" );
			Elements xhtmlAElements		= xhtmlAsElement.getChildElements( "a", "http://www.w3.org/1999/xhtml" );
			Element xhtmlAElement		= null;
			for ( int i = 0; i < xhtmlAElements.size(); i++ ) {
				xhtmlAElement			= xhtmlAElements.get( i );
				
				// Update resource
				if ( xhtmlAElement.getAttribute( "href" ) != null ) {
					xhtmlAElement.addAttribute( new Attribute( "href", StringUtils.toSafeASCIIString( xhtmlAElement.getAttributeValue( "href" ), false, response ) ) );						
				}
			}
			
						
			logger.log( Level.DEBUG, "The <xhtml:a> elements have been successfully transformed." );			
		} catch ( Exception ex ) {
			throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_30, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_30, ex );
		}
	}
}