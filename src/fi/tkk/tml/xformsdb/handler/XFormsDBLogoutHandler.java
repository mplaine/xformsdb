package fi.tkk.tml.xformsdb.handler;

import javax.servlet.http.HttpSession;

import nu.xom.Document;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fi.tkk.tml.xformsdb.core.Constants;
import fi.tkk.tml.xformsdb.error.ErrorConstants;
import fi.tkk.tml.xformsdb.error.HandlerException;
import fi.tkk.tml.xformsdb.servlet.XFormsDBServlet;
import fi.tkk.tml.xformsdb.xml.to.xformsdb.XFormsDBConfigTO;


/**
 * Handle the <xformsdb:logout> requests.
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on October 21, 2009
 */
public class XFormsDBLogoutHandler extends ResponseHandler {
	
	
	
	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger		= Logger.getLogger( XFormsDBLogoutHandler.class );

	
	// PUBLIC CONSTURCTORS
	public XFormsDBLogoutHandler() {
		logger.log( Level.DEBUG, "Constructor has been called." );
	}
	
	
	// PUBLIC METHODS
	public String handle( XFormsDBServlet xformsdbServlet, HttpSession session, Document document ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		XFormsDBConfigTO xformsDBConfigTO	= null;
		String xformsdbLogout				= null;
		
		
		try {
			// Retrieve the XFormsDB configuration file
			xformsDBConfigTO				= xformsdbServlet.getXFormsDBConfigTO();

			// Remove the logged in XFormsDB user from the session
//			XFormsDBUserManager.removeLoggedInXFormsDBUser( session );
			session.invalidate();

			// Write the response XML string i.e. empty XFormsDB user element
			xformsdbLogout					= "<?xml version=\"" + Constants.OUTPUT_XML_VERSION_1_0 + "\" encoding=\"" + xformsDBConfigTO.getEncoding() + "\"?>" + Constants.LINE_SEPARATOR;
			xformsdbLogout					+= "<" + Constants.XFORMSDB_USER + " " + Constants.NAMESPACE_XMLNS_EMPTY + " " + Constants.NAMESPACE_XMLNS_XFORMSDB + " />";
			logger.log( Level.DEBUG, "The user has been successfully logged out from XFormsDB." );
//		} catch ( ManagerException mex ) {
//			throw new HandlerException( mex.getCode(), mex.getMessage(), mex );
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBLOGOUTHANDLER_1, ErrorConstants.ERROR_MESSAGE_XFORMSDBLOGOUTHANDLER_1, ex  );
		}
		
		
		return xformsdbLogout;
	}	
}