package fi.tkk.tml.xformsdb.manager.xformsdb;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fi.tkk.tml.xformsdb.core.Constants;
import fi.tkk.tml.xformsdb.error.ErrorConstants;
import fi.tkk.tml.xformsdb.error.ManagerException;


/**
 * Manage the logged in <xformsdb:user> element.
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on October 13, 2008
 */
public class XFormsDBUserManager {

	

	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger	= Logger.getLogger( XFormsDBUserManager.class );
	
	
	// PRIVATE CONSTURCTORS
	// Prevent instantiation of this class
	private XFormsDBUserManager() {
		logger.log( Level.DEBUG, "Constructor has been called." );
	}	

	
	// PUBLIC METHODS
	public static synchronized void setLoggedInXFormsDBUser( HttpSession session, String loggedInXFormsDBUser ) throws ManagerException {
		logger.log( Level.DEBUG, "Method has been called." );
		
		
		try {
			// Set the logged in <xformsdb:user> element into the session
			session.setAttribute( Constants.SESSION_ATTRIBUTE_LOGGED_IN_XFORMSDB_USER, loggedInXFormsDBUser );
			logger.log( Level.DEBUG, "The logged in <xformsdb:user> element has been successfully set into the session." );
		} catch ( Exception ex ) {
			throw new ManagerException( ErrorConstants.ERROR_CODE_MANAGER_16, ErrorConstants.ERROR_MESSAGE_MANAGER_16, ex );
		}
	}

	
	public static synchronized void removeLoggedInXFormsDBUser( HttpSession session ) throws ManagerException {
		logger.log( Level.DEBUG, "Method has been called." );		
		
		
		try {
			// Remove the logged in <xformsdb:user> element from the session
			session.removeAttribute( Constants.SESSION_ATTRIBUTE_LOGGED_IN_XFORMSDB_USER );
			logger.log( Level.DEBUG, "The logged in <xformsdb:user> element has been successfully removed from the session." );
		} catch ( Exception ex ) {
			throw new ManagerException( ErrorConstants.ERROR_CODE_MANAGER_17, ErrorConstants.ERROR_MESSAGE_MANAGER_17, ex );
		}
	}

	
	public static synchronized String getLoggedInXFormsDBUser( HttpSession session ) throws ManagerException {
		logger.log( Level.DEBUG, "Method has been called." );
		String loggedInXFormsDBUser		= null;
		
		
		try {
			// Retrieve/write the logged in <xformsdb:user> element from the session
			loggedInXFormsDBUser		= ( String ) session.getAttribute( Constants.SESSION_ATTRIBUTE_LOGGED_IN_XFORMSDB_USER );
			logger.log( Level.DEBUG, "The logged in <xformsdb:user> element has been successfully retrieved from the session." );
		} catch ( Exception ex ) {
			throw new ManagerException( ErrorConstants.ERROR_CODE_MANAGER_18, ErrorConstants.ERROR_MESSAGE_MANAGER_18, ex );
		}
		
		
		return loggedInXFormsDBUser;
	}		
}