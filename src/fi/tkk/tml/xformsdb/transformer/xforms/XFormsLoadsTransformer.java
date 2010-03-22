package fi.tkk.tml.xformsdb.transformer.xforms;

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
 * Transform the <xforms:load> elements.
 * 
 * The transformed <xforms:load> elements will be given
 * as an XML formatted parameter to the xformsdb_xforms.xsl stylesheet
 * which replaces all <xforms:load> elements with
 * an appropriate transformation result.
 *
 * @author Markku Laine
 * @version 1.0	 Created on November 14, 2009
 */
public class XFormsLoadsTransformer {

	

	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger		= Logger.getLogger( XFormsLoadsTransformer.class );
	
	
	// PRIVATE CONSTURCTORS
	// Prevent instantiation of this class
	private XFormsLoadsTransformer() {
		logger.log( Level.DEBUG, "Constructor has been called." );
	}
	
	
	// PUBLIC STATIC METHODS
	public static void transform( HttpServletResponse response, Document document ) throws TransformerException {
		logger.log( Level.DEBUG, "Method has been called." );

		
		try {
			Element xformsLoadsElement		= document.getRootElement().getFirstChildElement( "loads", "http://www.w3.org/2002/xforms" );
			Elements xformsLoadElements		= xformsLoadsElement.getChildElements( "load", "http://www.w3.org/2002/xforms" );
			Element xformsLoadElement		= null;
			for ( int i = 0; i < xformsLoadElements.size(); i++ ) {
				xformsLoadElement			= xformsLoadElements.get( i );
				
				// Update resource
				if ( xformsLoadElement.getAttribute( "resource" ) != null ) {
					xformsLoadElement.addAttribute( new Attribute( "resource", StringUtils.toSafeASCIIString( xformsLoadElement.getAttributeValue( "resource" ), false, response ) ) );
				}
			}

			
			logger.log( Level.DEBUG, "The <xforms:load> elements have been successfully transformed." );			
		} catch ( Exception ex ) {
			throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_17, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_17, ex );
		}
	}	
}