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
 * Transform the <xforms:submission> elements.
 * 
 * The transformed <xforms:submission> elements will be given
 * as an XML formatted parameter to the xformsdb_xforms.xsl stylesheet
 * which replaces all <xforms:submission> elements with
 * an appropriate transformation result.
 *
 * @author Markku Laine
 * @version 1.0	 Created on November 14, 2009
 */
public class XFormsSubmissionsTransformer {

	

	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger			= Logger.getLogger( XFormsSubmissionsTransformer.class );

	
	// PRIVATE CONSTURCTORS
	// Prevent instantiation of this class
	private XFormsSubmissionsTransformer() {
		logger.log( Level.DEBUG, "Constructor has been called." );
	}
	
	
	// PUBLIC STATIC METHODS
	public static void transform( HttpServletResponse response, Document document ) throws TransformerException {
		logger.log( Level.DEBUG, "Method has been called." );

		
		try {
			Element xformsSubmissionsElement	= document.getRootElement().getFirstChildElement( "submissions", "http://www.w3.org/2002/xforms" );
			Elements xformsSubmissionElements	= xformsSubmissionsElement.getChildElements( "submission", "http://www.w3.org/2002/xforms" );
			Element xformsSubmissionElement		= null;
			for ( int i = 0; i < xformsSubmissionElements.size(); i++ ) {
				xformsSubmissionElement			= xformsSubmissionElements.get( i );

				// Update action
				if ( xformsSubmissionElement.getAttribute( "action" ) != null ) {
					xformsSubmissionElement.addAttribute( new Attribute( "action", StringUtils.toSafeASCIIString( xformsSubmissionElement.getAttributeValue( "action" ), false, response ) ) );
				}
			}
			
			
			logger.log( Level.DEBUG, "The <xforms:submission> elements have been successfully transformed." );			
		} catch ( Exception ex ) {
			throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_24, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_24, ex );
		}
	}
}