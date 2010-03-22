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
 * Manage <xformsdb:query> elements.
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on December 19, 2008
 */
public class XFormsDBQueryManager {

	

	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger							= Logger.getLogger( XFormsDBQueryManager.class );
	
	
	// PRIVATE CONSTURCTORS
	// Prevent instantiation of this class
	private XFormsDBQueryManager() {
		logger.log( Level.DEBUG, "Constructor has been called." );
	}
	
	
	// PRIVATE STATIC SYNCHRONIZED METHODS
	private static synchronized Map<String, Element> getActiveXFormsDBQueryElements( HttpSession session ) throws ManagerException {
		logger.log( Level.DEBUG, "Method has been called." );
		Map<String, Element> activeXFormsDBQueryElements		= null;
		
		
		try {
			// Retrieve the active <xformsdb:query> elements from the session
			activeXFormsDBQueryElements							= ( Map<String, Element> ) session.getAttribute( Constants.SESSION_ATTRIBUTE_ACTIVE_XFORMSDB_QUERY_ELEMENTS );
			if ( activeXFormsDBQueryElements == null ) {
				activeXFormsDBQueryElements						= new HashMap<String, Element>( 128 );
			}
			logger.log( Level.DEBUG, "The active <xformsdb:query> elements have been successfully retrieved from the session." );
		} catch ( Exception ex ) {
			throw new ManagerException( ErrorConstants.ERROR_CODE_MANAGER_5, ErrorConstants.ERROR_MESSAGE_MANAGER_5, ex );
		}
		
		
		return activeXFormsDBQueryElements;
	}
	
	
	private static synchronized void setActiveXFormsDBQueryElements( HttpSession session, Map<String, Element> activeXFormsDBQueryElements ) throws ManagerException {
		logger.log( Level.DEBUG, "Method has been called." );
		
		
		try {
			// Set the active <xformsdb:query> elements into the session
			session.setAttribute( Constants.SESSION_ATTRIBUTE_ACTIVE_XFORMSDB_QUERY_ELEMENTS, activeXFormsDBQueryElements );
			logger.log( Level.DEBUG, "The active <xformsdb:query> elements have been successfully set into the session." );
		} catch ( Exception ex ) {
			throw new ManagerException( ErrorConstants.ERROR_CODE_MANAGER_6, ErrorConstants.ERROR_MESSAGE_MANAGER_6, ex );
		}
	}

	
	private static synchronized Map<String, String> getSelect4UpdateXFormsDBQueryResults( HttpSession session ) throws ManagerException {
		logger.log( Level.DEBUG, "Method has been called." );
		Map<String, String> select4UpdateXFormsDBQueryResults	= null;
		

		try {
			// Retrieve the SELECT4UPDATE <xformsdb:query> results from the session
			select4UpdateXFormsDBQueryResults					= ( Map<String, String> ) session.getAttribute( Constants.SESSION_ATTRIBUTE_SELECT4UPDATE_XFORMSDB_QUERY_RESULTS );
			if ( select4UpdateXFormsDBQueryResults == null ) {
				select4UpdateXFormsDBQueryResults				= new HashMap<String, String>( 128 );
			}
			logger.log( Level.DEBUG, "The SELECT4UPDATE <xformsdb:query> results have been successfully retrieved from the session." );
		} catch ( Exception ex ) {
			throw new ManagerException( ErrorConstants.ERROR_CODE_MANAGER_7, ErrorConstants.ERROR_MESSAGE_MANAGER_7, ex );
		}
		
		
		return select4UpdateXFormsDBQueryResults;
	}


	private static synchronized void setSelect4UpdateXFormsDBQueryResults( HttpSession session, Map<String, String> select4UpdateXFormsDBQueryResults ) throws ManagerException {
		logger.log( Level.DEBUG, "Method has been called." );
		
		
		try {
			// Set the SELECT4UPDATE <xformsdb:query> results into the session
			session.setAttribute( Constants.SESSION_ATTRIBUTE_SELECT4UPDATE_XFORMSDB_QUERY_RESULTS, select4UpdateXFormsDBQueryResults );
			logger.log( Level.DEBUG, "The SELECT4UPDATE <xformsdb:query> results have been successfully set into the session." );
		} catch ( Exception ex ) {
			throw new ManagerException( ErrorConstants.ERROR_CODE_MANAGER_8, ErrorConstants.ERROR_MESSAGE_MANAGER_8, ex );
		}	
	}

	
	// PUBLIC STATIC SYNCHRONIZED METHODS
	public static synchronized void addActiveXFormsDBQueryElements( HttpSession session, Document document ) throws ManagerException {
		logger.log( Level.DEBUG, "Method has been called." );
		Map<String, Element> activeXFormsDBQueryElements		= null;
		Element xformsdbQueryElement							= null;
		String id												= null;
		
		
		try {
			// Retrieve the active <xformsdb:query> elements from the session
			activeXFormsDBQueryElements							= XFormsDBQueryManager.getActiveXFormsDBQueryElements( session );
			
	
			Element xformsdbInstancesElement					= document.getRootElement().getFirstChildElement( "instances", Constants.NAMESPACE_URI_XFORMSDB );
			Elements xformsdbInstanceElements					= xformsdbInstancesElement.getChildElements( "instance", Constants.NAMESPACE_URI_XFORMSDB );
			Element xformsdbInstanceElement						= null;
			for ( int i = 0; i < xformsdbInstanceElements.size(); i++ ) {
				xformsdbInstanceElement							= xformsdbInstanceElements.get( i );
				xformsdbQueryElement							= xformsdbInstanceElement.getFirstChildElement( "query", Constants.NAMESPACE_URI_XFORMSDB );
				if ( xformsdbQueryElement != null ) {
					id											= xformsdbQueryElement.getAttributeValue( "id" );
					// Add the active <xformsdb:query> element into the map of active <xformsdb:query> elements
					activeXFormsDBQueryElements.put( id, new Element( xformsdbQueryElement ) );
				}
			}
					
			// Set the active <xformsdb:query> elements into the session
			XFormsDBQueryManager.setActiveXFormsDBQueryElements( session, activeXFormsDBQueryElements );
			logger.log( Level.DEBUG, "The active <xformsdb:query> elements have been successfully added into the session." );
		} catch ( ManagerException mex ) {
			throw mex;
		} catch ( Exception ex ) {
			throw new ManagerException( ErrorConstants.ERROR_CODE_MANAGER_9, ErrorConstants.ERROR_MESSAGE_MANAGER_9, ex );
		}
	}
	
	
	public static synchronized Element getActiveXFormsDBQueryElement( HttpSession session, String id ) throws ManagerException {
		logger.log( Level.DEBUG, "Method has been called." );
		Map<String, Element> activeXFormsDBQueryElements		= null;
		Element activeXFormsDBQueryElement						= null;
		

		try {
			// Retrieve the active <xformsdb:query> elements from the session
			activeXFormsDBQueryElements							= XFormsDBQueryManager.getActiveXFormsDBQueryElements( session );
			activeXFormsDBQueryElement							= activeXFormsDBQueryElements.get( id );
			logger.log( Level.DEBUG, "The active <xformsdb:query> element has been successfully retrieved from the session." );
		} catch ( ManagerException mex ) {
			throw mex;
		} catch ( Exception ex ) {
			throw new ManagerException( ErrorConstants.ERROR_CODE_MANAGER_10, ErrorConstants.ERROR_MESSAGE_MANAGER_10, ex );
		}
		
		
		return activeXFormsDBQueryElement;
	}

		
	public static synchronized void addSelect4UpdateXFormsDBQueryResult( HttpSession session, String queryId, String xformsdbQueryResult ) throws ManagerException {
		logger.log( Level.DEBUG, "Method has been called." );
		Map<String, String> select4UpdateXFormsDBQueryResults	= null;
		

		try {
			// Retrieve the SELECT4UPDATE XFormsDB query results from the session
			select4UpdateXFormsDBQueryResults					= XFormsDBQueryManager.getSelect4UpdateXFormsDBQueryResults( session );
			
			// Add the <xformsdb:query> result into the map of SELECT4UPDATE <xformsdb:query> results
			select4UpdateXFormsDBQueryResults.put( queryId, xformsdbQueryResult );
			
			// Set the SELECT4UPDATE <xformsdb:query> results into the session
			XFormsDBQueryManager.setSelect4UpdateXFormsDBQueryResults( session, select4UpdateXFormsDBQueryResults );
			logger.log( Level.DEBUG, "The SELECT4UPDATE <xformsdb:query> results have been successfully added into the session." );
		} catch ( ManagerException mex ) {
			throw mex;
		} catch ( Exception ex ) {
			throw new ManagerException( ErrorConstants.ERROR_CODE_MANAGER_11, ErrorConstants.ERROR_MESSAGE_MANAGER_11, ex );
		}
	}
	
	
	public static synchronized String getSelect4UpdateXFormsDBQueryResult( HttpSession session, String id ) throws ManagerException {
		logger.log( Level.DEBUG, "Method has been called." );
		Map<String, String> select4UpdateXFormsDBQueryResults	= null;
		String select4UpdateXFormsDBQueryResult					= null;
		
		
		try {
			// Retrieve the SELECT4UPDATE <xformsdb:query> results from the session
			select4UpdateXFormsDBQueryResults					= XFormsDBQueryManager.getSelect4UpdateXFormsDBQueryResults( session );
			select4UpdateXFormsDBQueryResult					= select4UpdateXFormsDBQueryResults.get( id );
			logger.log( Level.DEBUG, "The SELECT4UPDATE <xformsdb:query> result has been successfully retrieved from the session." );
		} catch ( ManagerException mex ) {
			throw mex;
		} catch ( Exception ex ) {
			throw new ManagerException( ErrorConstants.ERROR_CODE_MANAGER_12, ErrorConstants.ERROR_MESSAGE_MANAGER_12, ex );
		}
		
		
		return select4UpdateXFormsDBQueryResult;
	}
}