package fi.tkk.tml.xformsdb.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fi.tkk.tml.xformsdb.error.ErrorConstants;
import fi.tkk.tml.xformsdb.error.HandlerException;
import fi.tkk.tml.xformsdb.servlet.XFormsDBServlet;


/**
 * Handle POST requests.
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on October 15, 2009
 */
public class PostHandler extends ResponseHandler {
	
	
	
	// PRIVATE STATIC FINAL VARIABLES
	private static final String	APPLICATION_XML = "application/xml";
	private static final Logger logger			= Logger.getLogger( PostHandler.class );

	
	// PRIVATE VARIABLES
	private PostNullHandler postNullHandler;
	private PostApplicationXMLHandler postApplicationXMLHandler;
	private PostUnknownHandler postUnknownHandler;
	
	
	// PUBLIC CONSTURCTORS
	public PostHandler() {
		super();
		logger.log( Level.DEBUG, "Constructor has been called." );
		this.postNullHandler					= new PostNullHandler();
		this.postApplicationXMLHandler			= new PostApplicationXMLHandler();
		this.postUnknownHandler					= new PostUnknownHandler();
	}
	
	
	// PUBLIC METHODS
	public void handle( XFormsDBServlet xformsdbServlet, HttpServletRequest request, HttpServletResponse response ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		String contentType						= null;

		
		try {
			// Get the content type
			contentType							= request.getContentType();
			
			
			if ( contentType == null ) {
				this.postNullHandler.handle( xformsdbServlet, request, response );
			}
			else {
				if ( PostHandler.APPLICATION_XML.equals( contentType ) ) {
					this.postApplicationXMLHandler.handle( xformsdbServlet, request, response );
				}
				else {
					this.postUnknownHandler.handle( xformsdbServlet, request, response );
				}
			}
		} catch ( HandlerException hex ) {
			throw hex;
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_POSTHANDLER_1, ErrorConstants.ERROR_MESSAGE_POSTHANDLER_1, ex );
		}		
	}
}