package fi.tkk.tml.xformsdb.handler;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fi.tkk.tml.xformsdb.core.Constants;
import fi.tkk.tml.xformsdb.error.ErrorConstants;
import fi.tkk.tml.xformsdb.error.HandlerException;
import fi.tkk.tml.xformsdb.error.ManagerException;
import fi.tkk.tml.xformsdb.manager.xformsdb.XFormsDBUserManager;
import fi.tkk.tml.xformsdb.servlet.XFormsDBServlet;
import fi.tkk.tml.xformsdb.xml.to.xformsdb.XFormsDBConfigTO;


/**
 * Handle user <xformsdb:user> requests.
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on October 21, 2009
 */
public class XFormsDBUserHandler extends ResponseHandler {
	
	
	
	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger		= Logger.getLogger( XFormsDBUserHandler.class );

	
	// PUBLIC CONSTURCTORS
	public XFormsDBUserHandler() {
		logger.log( Level.DEBUG, "Constructor has been called." );
	}
	
	
	// PUBLIC METHODS
	public String handle( XFormsDBServlet xformsdbServlet, HttpSession session ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		XFormsDBConfigTO xformsDBConfigTO	= null;
		String xformsdbUser					= null;
		
		
		try {
			// Retrieve the XFormsDB configuration file
			xformsDBConfigTO				= xformsdbServlet.getXFormsDBConfigTO();
			
			// Retrieve the logged in user from XFormsDB
			xformsdbUser					= XFormsDBUserManager.getLoggedInXFormsDBUser( session );
			
			if ( xformsdbUser == null ) {
				// Write the response XML string i.e. empty XFormsDB user element
				xformsdbUser				=  "<?xml version=\"" + Constants.OUTPUT_XML_VERSION_1_0 + "\" encoding=\"" + xformsDBConfigTO.getEncoding() + "\"?>" + Constants.LINE_SEPARATOR;
				xformsdbUser				+= "<" + Constants.XFORMSDB_USER + " " + Constants.NAMESPACE_XMLNS_EMPTY + " " + Constants.NAMESPACE_XMLNS_XFORMSDB + " />";
//				throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBUSERHANDLER_1, ErrorConstants.ERROR_MESSAGE_XFORMSDBUSERHANDLER_1 );
			}
			logger.log( Level.DEBUG, "The logged in user has been successfully retrieved from XFormsDB." );
		} catch ( ManagerException mex ) {
			throw new HandlerException( mex.getCode(), mex.getMessage(), mex );
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBUSERHANDLER_2, ErrorConstants.ERROR_MESSAGE_XFORMSDBUSERHANDLER_2, ex  );
		}
		
		
		return xformsdbUser;
	}	
}