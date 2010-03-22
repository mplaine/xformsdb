package fi.tkk.tml.xformsdb.manager.xformsdb;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fi.tkk.tml.xformsdb.core.Constants;
import fi.tkk.tml.xformsdb.error.ErrorConstants;
import fi.tkk.tml.xformsdb.error.ManagerException;


/**
 * Manage the web application's <xformsdb:state> element.
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on October 12, 2008
 */
public class XFormsDBStateManager {

	

	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger	= Logger.getLogger( XFormsDBStateManager.class );
	
	
	// PRIVATE CONSTURCTORS
	// Prevent instantiation of this class
	private XFormsDBStateManager() {
		logger.log( Level.DEBUG, "Constructor has been called." );
	}	

	
	// PUBLIC METHODS
	public static synchronized void setXFormsDBState( HttpSession session, String xformsdbState ) throws ManagerException {
		logger.log( Level.DEBUG, "Method has been called." );
		
		
		try {
			// Set the web application's <xformsdb:state> element into the session
			session.setAttribute( Constants.SESSION_ATTRIBUTE_XFORMSDB_STATE, xformsdbState );
			logger.log( Level.DEBUG, "The web application's <xformsdb:state> element has been successfully set into the session." );
		} catch ( Exception ex ) {
			throw new ManagerException( ErrorConstants.ERROR_CODE_MANAGER_19, ErrorConstants.ERROR_MESSAGE_MANAGER_19, ex );
		}
	}
	
	
	public static synchronized String getXFormsDBState( HttpSession session ) throws ManagerException {
		logger.log( Level.DEBUG, "Method has been called." );
		String xformsdbState	= null;

		
		try {
			// Retrieve the web application's <xformsdb:state> element from the session
			xformsdbState		=  ( String ) session.getAttribute( Constants.SESSION_ATTRIBUTE_XFORMSDB_STATE );
			logger.log( Level.DEBUG, "The web application's <xformsdb:state> element has been successfully retrieved from the session." );
		} catch ( Exception ex ) {
			throw new ManagerException( ErrorConstants.ERROR_CODE_MANAGER_20, ErrorConstants.ERROR_MESSAGE_MANAGER_20, ex );
		}
		
		
		return xformsdbState;
	}		
}