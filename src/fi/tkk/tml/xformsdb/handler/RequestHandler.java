package fi.tkk.tml.xformsdb.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fi.tkk.tml.xformsdb.core.Constants;
import fi.tkk.tml.xformsdb.error.ErrorConstants;
import fi.tkk.tml.xformsdb.error.HandlerException;
import fi.tkk.tml.xformsdb.servlet.XFormsDBServlet;


/**
 * Handle requests of the XFormsDB core servlet.
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on October 15, 2009
 */
public class RequestHandler {

	

	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger	= Logger.getLogger( RequestHandler.class );

	
	// PRIVATE VARIABLES
	private GetHandler getHandler;
	private PostHandler postHandler;
	
	
	// PUBLIC CONSTURCTORS
	public RequestHandler() {
		super();
		logger.log( Level.DEBUG, "Constructor has been called." );
		this.getHandler					= new GetHandler();
		this.postHandler				= new PostHandler();
	}
	
		
	// PUBLIC METHODS
	public void handle( XFormsDBServlet xformsdbServlet, HttpServletRequest request, HttpServletResponse response ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );		
		String method					= null;

		
		try {
			// Get the method
			method						= request.getMethod();

			
			if ( Constants.METHOD_GET.equalsIgnoreCase( method ) ) {
				this.getHandler.handle( xformsdbServlet, request, response );
			}
			else if ( Constants.METHOD_POST.equalsIgnoreCase( method ) ) {
				this.postHandler.handle( xformsdbServlet, request, response );
			}
			logger.log( Level.DEBUG, "The request has been handled." );
		} catch ( HandlerException hex ) {
			throw hex;
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_REQUESTHANDLER_1, ErrorConstants.ERROR_MESSAGE_REQUESTHANDLER_1, ex );
		}
	}
}