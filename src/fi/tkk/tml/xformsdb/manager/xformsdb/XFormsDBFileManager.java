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
 * Manage <xformsdb:file> elements.
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on December 19, 2008
 */
public class XFormsDBFileManager {

	

	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger						= Logger.getLogger( XFormsDBFileManager.class );
	
	
	// PRIVATE CONSTURCTORS
	// Prevent instantiation of this class
	private XFormsDBFileManager() {
		logger.log( Level.DEBUG, "Constructor has been called." );
	}
	
	
	// PRIVATE STATIC SYNCHRONIZED METHODS
	private static synchronized Map<String, Element> getActiveXFormsDBFileElements( HttpSession session ) throws ManagerException {
		logger.log( Level.DEBUG, "Method has been called." );
		Map<String, Element> activeXFormsDBFileElements	= null;
		
		
		try {
			// Retrieve the active <xformsdb:file> elements from the session
			activeXFormsDBFileElements						= ( Map<String, Element> ) session.getAttribute( Constants.SESSION_ATTRIBUTE_ACTIVE_XFORMSDB_FILE_ELEMENTS );
			if ( activeXFormsDBFileElements == null ) {
				activeXFormsDBFileElements					= new HashMap<String, Element>( 128 );
			}
			logger.log( Level.DEBUG, "The active <xformsdb:file> elements have been successfully retrieved from the session." );
		} catch ( Exception ex ) {
			throw new ManagerException( ErrorConstants.ERROR_CODE_MANAGER_24, ErrorConstants.ERROR_MESSAGE_MANAGER_24, ex );
		}
		
		
		return activeXFormsDBFileElements;
	}
	
	
	private static synchronized void setActiveXFormsDBFileElements( HttpSession session, Map<String, Element> activeXFormsDBFileElements ) throws ManagerException {
		logger.log( Level.DEBUG, "Method has been called." );
		
		
		try {
			// Set the active <xformsdb:file> elements into the session
			session.setAttribute( Constants.SESSION_ATTRIBUTE_ACTIVE_XFORMSDB_FILE_ELEMENTS, activeXFormsDBFileElements );
			logger.log( Level.DEBUG, "The active <xformsdb:file> elements have been successfully set into the session." );
		} catch ( Exception ex ) {
			throw new ManagerException( ErrorConstants.ERROR_CODE_MANAGER_23, ErrorConstants.ERROR_MESSAGE_MANAGER_23, ex );
		}
	}

	
	// PUBLIC STATIC SYNCHRONIZED METHODS
	public static synchronized void addActiveXFormsDBFileElements( HttpSession session, Document document ) throws ManagerException {
		logger.log( Level.DEBUG, "Method has been called." );
		Map<String, Element> activeXFormsDBFileElements		= null;
		Element xformsdbFileElement							= null;
		String id											= null;
		

		try {
			// Retrieve the active <xformsdb:file> elements from the session
			activeXFormsDBFileElements						= XFormsDBFileManager.getActiveXFormsDBFileElements( session );
			
			
			Element xformsdbInstancesElement				= document.getRootElement().getFirstChildElement( "instances", Constants.NAMESPACE_URI_XFORMSDB );
			Elements xformsdbInstanceElements				= xformsdbInstancesElement.getChildElements( "instance", Constants.NAMESPACE_URI_XFORMSDB );
			Element xformsdbInstanceElement					= null;
			for ( int i = 0; i < xformsdbInstanceElements.size(); i++ ) {
				xformsdbInstanceElement						= xformsdbInstanceElements.get( i );
				xformsdbFileElement							= xformsdbInstanceElement.getFirstChildElement( "file", Constants.NAMESPACE_URI_XFORMSDB );
				if ( xformsdbFileElement != null ) {
					id										= xformsdbFileElement.getAttributeValue( "id" );
					// Add the active <xformsdb:file> element into the map of active <xformsdb:file> elements
					activeXFormsDBFileElements.put( id, new Element( xformsdbFileElement ) );
				}
			}
			
			// Set the active <xformsdb:file> elements into the session
			XFormsDBFileManager.setActiveXFormsDBFileElements( session, activeXFormsDBFileElements );
			logger.log( Level.DEBUG, "The active <xformsdb:file> elements have been successfully added into the session." );
		} catch ( ManagerException mex ) {
			throw mex;
		} catch ( Exception ex ) {
			throw new ManagerException( ErrorConstants.ERROR_CODE_MANAGER_22, ErrorConstants.ERROR_MESSAGE_MANAGER_22, ex );
		}
	}
	
	
	public static synchronized Element getActiveXFormsDBFileElement( HttpSession session, String id ) throws ManagerException {
		logger.log( Level.DEBUG, "Method has been called." );
		Map<String, Element> activeXFormsDBFileElements		= null;
		Element activeXFormsDBFileElement					= null;
		
		
		try {
			// Retrieve the active <xformsdb:file> elements from the session
			activeXFormsDBFileElements						= XFormsDBFileManager.getActiveXFormsDBFileElements( session );
			activeXFormsDBFileElement						= activeXFormsDBFileElements.get( id );
			logger.log( Level.DEBUG, "The active <xformsdb:file> element has been successfully retrieved from the session." );
		} catch ( ManagerException mex ) {
			throw mex;
		} catch ( Exception ex ) {
			throw new ManagerException( ErrorConstants.ERROR_CODE_MANAGER_21, ErrorConstants.ERROR_MESSAGE_MANAGER_21, ex );
		}

		
		return activeXFormsDBFileElement;
	}
}