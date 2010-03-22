package fi.tkk.tml.xformsdb.handler;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fi.tkk.tml.xformsdb.core.Constants;
import fi.tkk.tml.xformsdb.error.ErrorConstants;
import fi.tkk.tml.xformsdb.error.HandlerException;
import fi.tkk.tml.xformsdb.error.ManagerException;
import fi.tkk.tml.xformsdb.manager.xformsdb.XFormsDBStateManager;
import fi.tkk.tml.xformsdb.servlet.XFormsDBServlet;
import fi.tkk.tml.xformsdb.xml.to.xformsdb.XFormsDBConfigTO;


/**
 * Handle the <xformsdb:state> GET requests.
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on October 21, 2009
 */
public class XFormsDBStateGetHandler extends ResponseHandler {
	
	
	
	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger		= Logger.getLogger( XFormsDBStateGetHandler.class );

	
	// PUBLIC CONSTURCTORS
	public XFormsDBStateGetHandler() {
		logger.log( Level.DEBUG, "Constructor has been called." );
	}
	
	
	// PUBLIC METHODS
	public String handle( XFormsDBServlet xformsdbServlet, HttpSession session ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		XFormsDBConfigTO xformsDBConfigTO	= null;
		String xformsdbState				= null;
		
		
		try {
			// Retrieve the XFormsDB configuration file
			xformsDBConfigTO				= xformsdbServlet.getXFormsDBConfigTO();
			
			// Retrieve the web application's state from XFormsDB
			xformsdbState					= XFormsDBStateManager.getXFormsDBState( session );
			
			if ( xformsdbState == null ) {
				// Write the response XML string i.e. empty XFormsDB state element
				xformsdbState				= "<?xml version=\"" + Constants.OUTPUT_XML_VERSION_1_0 + "\" encoding=\"" + xformsDBConfigTO.getEncoding() + "\"?>" + Constants.LINE_SEPARATOR;
				xformsdbState				+= "<" + Constants.XFORMSDB_STATE + " " + Constants.NAMESPACE_XMLNS_EMPTY + " " + Constants.NAMESPACE_XMLNS_XFORMSDB + " />";
			}
			logger.log( Level.DEBUG, "The web application state has been successfully retrieved from XFormsDB." );
		} catch ( ManagerException mex ) {
			throw new HandlerException( mex.getCode(), mex.getMessage(), mex );
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBSTATEGETHANDLER_2, ErrorConstants.ERROR_MESSAGE_XFORMSDBSTATEGETHANDLER_2, ex  );
		}
		
		
		return xformsdbState;
	}	
}