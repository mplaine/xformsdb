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
 * Transform the <xhtml:meta> elements.
 * 
 * The transformed <xhtml:meta> elements will be given
 * as an XML formatted parameter to the xformsdb_xforms.xsl stylesheet
 * which replaces all <xhtml:meta> elements with
 * an appropriate transformation result.
 *
 * @author Markku Laine
 * @version 1.0	 Created on November 14, 2009
 */
public class XHTMLMetasTransformer {

	

	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger	= Logger.getLogger( XHTMLMetasTransformer.class );

	
	// PRIVATE CONSTURCTORS
	// Prevent instantiation of this class
	private XHTMLMetasTransformer() {
		logger.log( Level.DEBUG, "Constructor has been called." );
	}
	
	
	// PUBLIC STATIC METHODS
	public static void transform( HttpServletResponse response, Document document ) throws TransformerException {
		logger.log( Level.DEBUG, "Method has been called." );
		String contentStr				= null;
		String contentBeginStr			= null;
		String contentEndStr			= null;
		String searchStr				= "url=";
		int indexOfSearchStr			= 0;

		
		try {
			Element xhtmlMetasElement	= document.getRootElement().getFirstChildElement( "metas", "http://www.w3.org/1999/xhtml" );
			Elements xhtmlMetaElements	= xhtmlMetasElement.getChildElements( "meta", "http://www.w3.org/1999/xhtml" );
			Element xhtmlMetaElement	= null;
			for ( int i = 0; i < xhtmlMetaElements.size(); i++ ) {
				xhtmlMetaElement		= xhtmlMetaElements.get( i );
				contentStr				= xhtmlMetaElement.getAttributeValue( "content" );

				// Update content id needed
				if ( contentStr != null ) {
					//contentStr			= contentStr.replace( " ", "" );
					indexOfSearchStr	= contentStr.indexOf( searchStr );
					
					if ( indexOfSearchStr != -1 ) {
						contentBeginStr	= contentStr.substring( 0, ( indexOfSearchStr + searchStr.length() ) );
						contentEndStr	= contentStr.substring( ( indexOfSearchStr + searchStr.length() ) );
						contentEndStr	= StringUtils.toSafeASCIIString( contentEndStr, false, response );
						contentStr		= contentBeginStr + contentEndStr;
						xhtmlMetaElement.addAttribute( new Attribute( "content", contentStr ) );
					}
				}
			}
			
			
			logger.log( Level.DEBUG, "The <xhtml:meta> elements have been successfully transformed." );			
		} catch ( Exception ex ) {
			throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_27, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_27, ex );
		}
	}
}