package fi.tkk.tml.xformsdb.manager.xformsdb;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fi.tkk.tml.xformsdb.core.Constants;
import fi.tkk.tml.xformsdb.error.ErrorConstants;
import fi.tkk.tml.xformsdb.error.ManagerException;


/**
 * Manage <xformsdb:login> elements.
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on December 19, 2008
 */
public class XFormsDBLoginManager {

	

	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger						= Logger.getLogger( XFormsDBLoginManager.class );
	
	
	// PRIVATE CONSTURCTORS
	// Prevent instantiation of this class
	private XFormsDBLoginManager() {
		logger.log( Level.DEBUG, "Constructor has been called." );
	}
	
	
	// PRIVATE STATIC SYNCHRONIZED METHODS
	private static synchronized Map<String, Element> getActiveXFormsDBLoginElements( HttpSession session ) throws ManagerException {
		logger.log( Level.DEBUG, "Method has been called." );
		Map<String, Element> activeXFormsDBLoginElements	= null;
		
		
		try {
			// Retrieve the active <xformsdb:login> elements from the session
			activeXFormsDBLoginElements						= ( Map<String, Element> ) session.getAttribute( Constants.SESSION_ATTRIBUTE_ACTIVE_XFORMSDB_LOGIN_ELEMENTS );
			if ( activeXFormsDBLoginElements == null ) {
				activeXFormsDBLoginElements					= new HashMap<String, Element>( 128 );
			}
			logger.log( Level.DEBUG, "The active <xformsdb:login> elements have been successfully retrieved from the session." );
		} catch ( Exception ex ) {
			throw new ManagerException( ErrorConstants.ERROR_CODE_MANAGER_4, ErrorConstants.ERROR_MESSAGE_MANAGER_4, ex );
		}
		
		
		return activeXFormsDBLoginElements;
	}
	
	
	private static synchronized void setActiveXFormsDBLoginElements( HttpSession session, Map<String, Element> activeXFormsDBLoginElements ) throws ManagerException {
		logger.log( Level.DEBUG, "Method has been called." );
		
		
		try {
			// Set the active <xformsdb:login> elements into the session
			session.setAttribute( Constants.SESSION_ATTRIBUTE_ACTIVE_XFORMSDB_LOGIN_ELEMENTS, activeXFormsDBLoginElements );
			logger.log( Level.DEBUG, "The active <xformsdb:login> elements have been successfully set into the session." );
		} catch ( Exception ex ) {
			throw new ManagerException( ErrorConstants.ERROR_CODE_MANAGER_3, ErrorConstants.ERROR_MESSAGE_MANAGER_3, ex );
		}
	}

	
	// PUBLIC STATIC SYNCHRONIZED METHODS
	public static synchronized void addActiveXFormsDBLoginElements( HttpSession session, Document document ) throws ManagerException {
		logger.log( Level.DEBUG, "Method has been called." );
		Map<String, Element> activeXFormsDBLoginElements	= null;
		Element xformsdbLoginElement						= null;
		String id											= null;
		

		try {
			// Retrieve the active <xformsdb:login> elements from the session
			activeXFormsDBLoginElements						= XFormsDBLoginManager.getActiveXFormsDBLoginElements( session );
			
			
			Element xformsdbInstancesElement				= document.getRootElement().getFirstChildElement( "instances", Constants.NAMESPACE_URI_XFORMSDB );
			Elements xformsdbInstanceElements				= xformsdbInstancesElement.getChildElements( "instance", Constants.NAMESPACE_URI_XFORMSDB );
			Element xformsdbInstanceElement					= null;
			for ( int i = 0; i < xformsdbInstanceElements.size(); i++ ) {
				xformsdbInstanceElement						= xformsdbInstanceElements.get( i );
				xformsdbLoginElement						= xformsdbInstanceElement.getFirstChildElement( "login", Constants.NAMESPACE_URI_XFORMSDB );
				if ( xformsdbLoginElement != null ) {
					id										= xformsdbLoginElement.getAttributeValue( "id" );
					// Add the active <xformsdb:login> element into the map of active <xformsdb:login> elements
					activeXFormsDBLoginElements.put( id, new Element( xformsdbLoginElement ) );
				}
			}
			
			// Set the active <xformsdb:login> elements into the session
			XFormsDBLoginManager.setActiveXFormsDBLoginElements( session, activeXFormsDBLoginElements );
			logger.log( Level.DEBUG, "The active <xformsdb:login> elements have been successfully added into the session." );
		} catch ( ManagerException mex ) {
			throw mex;
		} catch ( Exception ex ) {
			throw new ManagerException( ErrorConstants.ERROR_CODE_MANAGER_2, ErrorConstants.ERROR_MESSAGE_MANAGER_2, ex );
		}
	}
	
	
	public static synchronized Element getActiveXFormsDBLoginElement( HttpSession session, String id ) throws ManagerException {
		logger.log( Level.DEBUG, "Method has been called." );
		Map<String, Element> activeXFormsDBLoginElements	= null;
		Element activeXFormsDBLoginElement					= null;
		
		
		try {
			// Retrieve the active <xformsdb:login> elements from the session
			activeXFormsDBLoginElements						= XFormsDBLoginManager.getActiveXFormsDBLoginElements( session );
			activeXFormsDBLoginElement						= activeXFormsDBLoginElements.get( id );
			logger.log( Level.DEBUG, "The active <xformsdb:login> element has been successfully retrieved from the session." );
		} catch ( ManagerException mex ) {
			throw mex;
		} catch ( Exception ex ) {
			throw new ManagerException( ErrorConstants.ERROR_CODE_MANAGER_1, ErrorConstants.ERROR_MESSAGE_MANAGER_1, ex );
		}

		
		return activeXFormsDBLoginElement;
	}
}