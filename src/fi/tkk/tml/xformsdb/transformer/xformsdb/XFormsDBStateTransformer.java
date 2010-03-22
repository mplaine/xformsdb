package fi.tkk.tml.xformsdb.transformer.xformsdb;

import nu.xom.Attribute;
import nu.xom.Document;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fi.tkk.tml.xformsdb.core.Constants;
import fi.tkk.tml.xformsdb.error.ErrorConstants;
import fi.tkk.tml.xformsdb.error.TransformerException;


/**
 * Transform the <xformsdb:state> element.
 * 
 * 
 * @author Markku Laine
 * @version 1.0	 Created on October 15, 2009
 */
public class XFormsDBStateTransformer {

	

	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger	= Logger.getLogger( XFormsDBStateTransformer.class );
	
	
	// PRIVATE CONSTURCTORS
	// Prevent instantiation of this class
	private XFormsDBStateTransformer() {
		logger.log( Level.DEBUG, "Constructor has been called." );
	}
	
	
	// PUBLIC STATIC METHODS
	public static void transform( Document document, String stateType ) throws TransformerException {
		logger.log( Level.DEBUG, "Method has been called." );

		
		try {
			// Validate
			if ( stateType == null || ( Constants.STATE_TYPE_GET.equals( stateType ) == false && Constants.STATE_TYPE_SET.equals( stateType ) == false ) ) {
				throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_62, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_62 );
			}

			// Add type
			document.getRootElement().addAttribute( new Attribute( "type", stateType ) );

			
			logger.log( Level.DEBUG, "The <xformsdb:state> element has been successfully transformed." );			
		} catch ( TransformerException tex ) {
			throw tex;
		} catch ( Exception ex ) {
			throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_63, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_63, ex );
		}
	}	
}