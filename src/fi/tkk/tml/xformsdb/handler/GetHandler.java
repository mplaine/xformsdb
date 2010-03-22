package fi.tkk.tml.xformsdb.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fi.tkk.tml.xformsdb.error.ErrorConstants;
import fi.tkk.tml.xformsdb.error.HandlerException;
import fi.tkk.tml.xformsdb.servlet.XFormsDBServlet;


/**
 * Handle GET requests.
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on September 15, 2007
 */
public class GetHandler extends ResponseHandler {
	
	
	
	// PRIVATE STATIC FINAL VARIABLES
	private static final String APPLICATION_X_WWW_FORM_URLENCODED	= "application/x-www-form-urlencoded";
	private static final Logger logger								= Logger.getLogger( GetHandler.class );

	
	// PRIVATE VARIABLES
	private GetNullHandler getNullHandler;
	private GetApplicationXWWWFormUrlencodedHandler getApplicationXWWWFormUrlencodedHandler;
	private GetUnknownHandler getUnknownHandler;
	
	
	// PUBLIC CONSTURCTORS
	public GetHandler() {
		super();
		logger.log( Level.DEBUG, "Constructor has been called." );
		this.getNullHandler								= new GetNullHandler();
		this.getApplicationXWWWFormUrlencodedHandler	= new GetApplicationXWWWFormUrlencodedHandler();
		this.getUnknownHandler							= new GetUnknownHandler();
	}
	
	
	// PUBLIC METHODS
	public void handle( XFormsDBServlet xformsdbServlet, HttpServletRequest request, HttpServletResponse response ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		String contentType								= null;
		
		
		try {
			// Get the content type
			contentType									= request.getContentType();

			
			if ( contentType == null ) {
				this.getNullHandler.handle( xformsdbServlet, request, response );
			}
			else {
//				if ( GetHandler.APPLICATION_X_WWW_FORM_URLENCODED.equals( contentType ) ) {
//					this.getApplicationXWWWFormUrlencodedHandler.handle( xformsdbServlet, request, response );
//				}
//				else {
					this.getUnknownHandler.handle( xformsdbServlet, request, response );
//				}
			}
			logger.log( Level.DEBUG, "The GET request has been handled." );
		} catch ( HandlerException hex ) {
			throw hex;
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_GETHANDLER_1, ErrorConstants.ERROR_MESSAGE_GETHANDLER_1, ex );
		}		
	}
}