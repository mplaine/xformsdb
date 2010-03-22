package fi.tkk.tml.xformsdb.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fi.tkk.tml.xformsdb.error.ErrorConstants;
import fi.tkk.tml.xformsdb.error.HandlerException;
import fi.tkk.tml.xformsdb.servlet.XFormsDBServlet;


/**
 * Handle POST requests having the
 * content type: <code>null</code>.
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on September 15, 2008
 */
public class PostNullHandler extends ResponseHandler {
	
	
	
	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger	= Logger.getLogger( PostNullHandler.class );

	
	// PUBLIC CONSTURCTORS
	public PostNullHandler() {
		super();
		logger.log( Level.DEBUG, "Constructor has been called." );
	}
	
	
	// PUBLIC METHODS
	public void handle( XFormsDBServlet xformsdbServlet, HttpServletRequest request, HttpServletResponse response ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		throw new HandlerException( ErrorConstants.ERROR_CODE_POSTNULLHANDLER_1, ErrorConstants.ERROR_MESSAGE_POSTNULLHANDLER_1 );
	}	
}