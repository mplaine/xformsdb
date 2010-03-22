package fi.tkk.tml.xformsdb.transformer.xforms;

import javax.servlet.http.HttpServletResponse;

import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fi.tkk.tml.xformsdb.core.Constants;
import fi.tkk.tml.xformsdb.error.ErrorConstants;
import fi.tkk.tml.xformsdb.error.TransformerException;


/**
 * Transform the <xforms:model> elements.
 * 
 * The transformed <xforms:model> elements will be given
 * as an XML formatted parameter to the xformsdb_xforms.xsl stylesheet
 * which replaces all <xforms:model> elements with
 * an appropriate transformation result.
 *
 * @author Markku Laine
 * @version 1.0	 Created on November 18, 2009
 */
public class XFormsModelsTransformer {

	

	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger		= Logger.getLogger( XFormsModelsTransformer.class );
	
	
	// PRIVATE CONSTURCTORS
	// Prevent instantiation of this class
	private XFormsModelsTransformer() {
		logger.log( Level.DEBUG, "Constructor has been called." );
	}
	
	
	// PUBLIC STATIC METHODS
	public static void transform( HttpServletResponse response, Document document ) throws TransformerException {
		logger.log( Level.DEBUG, "Method has been called." );

		
		try {
			Element xformsModelsElement		= document.getRootElement().getFirstChildElement( "models", "http://www.w3.org/2002/xforms" );
			Elements xformsModelElements	= xformsModelsElement.getChildElements( "model", "http://www.w3.org/2002/xforms" );
			Element xformsModelElement		= null;
			for ( int i = 0; i < xformsModelElements.size(); i++ ) {
				xformsModelElement			= xformsModelElements.get( i );
				
				// Add proxyinstance
				xformsModelElement.addAttribute( new Attribute( "proxyinstance", Constants.XFORMSDB_RESPONSE_PROXY_INSTANCE + "-" + ( i + 1 ) ) );
			}

			
			logger.log( Level.DEBUG, "The <xforms:model> elements have been successfully transformed." );			
		} catch ( Exception ex ) {
			throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_90, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_90, ex );
		}
	}	
}