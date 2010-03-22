package fi.tkk.tml.xformsdb.transformer.xformsdb;

import nu.xom.Document;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fi.tkk.tml.xformsdb.error.ErrorConstants;
import fi.tkk.tml.xformsdb.error.TransformerException;


/**
 * Transform the <xformsdb:logout> element.
 * 
 * 
 * @author Markku Laine
 * @version 1.0	 Created on October 30, 2008
 */
public class XFormsDBLogoutTransformer {

	

	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger	= Logger.getLogger( XFormsDBLogoutTransformer.class );
	
	
	// PRIVATE CONSTURCTORS
	// Prevent instantiation of this class
	private XFormsDBLogoutTransformer() {
		logger.log( Level.DEBUG, "Constructor has been called." );
	}
	
	
	// PUBLIC STATIC METHODS
	public static void transform( Document document ) throws TransformerException {
		logger.log( Level.DEBUG, "Method has been called." );

		
		try {
			// Do nothing
			logger.log( Level.DEBUG, "The <xformsdb:logout> element has been successfully transformed." );			
		} catch ( Exception ex ) {
			throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_66, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_66, ex );
		}
	}	
}