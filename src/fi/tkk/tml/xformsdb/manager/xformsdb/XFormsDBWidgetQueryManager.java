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
 * Manage <xformsdb:widgetquery> elements.
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on October 23, 2009
 */
public class XFormsDBWidgetQueryManager {

	

	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger							= Logger.getLogger( XFormsDBWidgetQueryManager.class );
	
	
	// PRIVATE CONSTURCTORS
	// Prevent instantiation of this class
	private XFormsDBWidgetQueryManager() {
		logger.log( Level.DEBUG, "Constructor has been called." );
	}
	
	
	// PRIVATE STATIC SYNCHRONIZED METHODS
	private static synchronized Map<String, Element> getActiveXFormsDBWidgetQueryElements( HttpSession session ) throws ManagerException {
		logger.log( Level.DEBUG, "Method has been called." );
		Map<String, Element> activeXFormsDBWidgetQueryElements	= null;
		
		
		try {
			// Retrieve the active <xformsdb:widgetquery> elements from the session
			activeXFormsDBWidgetQueryElements					= ( Map<String, Element> ) session.getAttribute( Constants.SESSION_ATTRIBUTE_ACTIVE_XFORMSDB_WIDGET_QUERY_ELEMENTS );
			if ( activeXFormsDBWidgetQueryElements == null ) {
				activeXFormsDBWidgetQueryElements				= new HashMap<String, Element>( 128 );
			}
			logger.log( Level.DEBUG, "The active <xformsdb:widgetquery> elements have been successfully retrieved from the session." );
		} catch ( Exception ex ) {
			throw new ManagerException( ErrorConstants.ERROR_CODE_MANAGER_29, ErrorConstants.ERROR_MESSAGE_MANAGER_29, ex );
		}
		
		
		return activeXFormsDBWidgetQueryElements;
	}
	
	
	private static synchronized void setActiveXFormsDBWidgetQueryElements( HttpSession session, Map<String, Element> activeXFormsDBWidgetQueryElements ) throws ManagerException {
		logger.log( Level.DEBUG, "Method has been called." );
		
		
		try {
			// Set the active <xformsdb:widgetquery> elements into the session
			session.setAttribute( Constants.SESSION_ATTRIBUTE_ACTIVE_XFORMSDB_WIDGET_QUERY_ELEMENTS, activeXFormsDBWidgetQueryElements );
			logger.log( Level.DEBUG, "The active <xformsdb:widgetquery> elements have been successfully set into the session." );
		} catch ( Exception ex ) {
			throw new ManagerException( ErrorConstants.ERROR_CODE_MANAGER_30, ErrorConstants.ERROR_MESSAGE_MANAGER_30, ex );
		}
	}

	
	private static synchronized Map<String, String> getSelect4UpdateXFormsDBWidgetQueryResults( HttpSession session ) throws ManagerException {
		logger.log( Level.DEBUG, "Method has been called." );
		Map<String, String> select4UpdateXFormsDBWidgetQueryResults	= null;
		

		try {
			// Retrieve the SELECT4UPDATE <xformsdb:widgetquery> results from the session
			select4UpdateXFormsDBWidgetQueryResults					= ( Map<String, String> ) session.getAttribute( Constants.SESSION_ATTRIBUTE_SELECT4UPDATE_XFORMSDB_WIDGET_QUERY_RESULTS );
			if ( select4UpdateXFormsDBWidgetQueryResults == null ) {
				select4UpdateXFormsDBWidgetQueryResults				= new HashMap<String, String>( 128 );
			}
			logger.log( Level.DEBUG, "The SELECT4UPDATE <xformsdb:widgetquery> results have been successfully retrieved from the session." );
		} catch ( Exception ex ) {
			throw new ManagerException( ErrorConstants.ERROR_CODE_MANAGER_36, ErrorConstants.ERROR_MESSAGE_MANAGER_36, ex );
		}
		
		
		return select4UpdateXFormsDBWidgetQueryResults;
	}
	
	
	private static synchronized void setSelect4UpdateXFormsDBWidgetQueryResults( HttpSession session, Map<String, String> select4UpdateXFormsDBWidgetQueryResults ) throws ManagerException {
		logger.log( Level.DEBUG, "Method has been called." );
		
		
		try {
			// Set the SELECT4UPDATE <xformsdb:widgetquery> results into the session
			session.setAttribute( Constants.SESSION_ATTRIBUTE_SELECT4UPDATE_XFORMSDB_WIDGET_QUERY_RESULTS, select4UpdateXFormsDBWidgetQueryResults );
			logger.log( Level.DEBUG, "The SELECT4UPDATE <xformsdb:widgetquery> results have been successfully set into the session." );
		} catch ( Exception ex ) {
			throw new ManagerException( ErrorConstants.ERROR_CODE_MANAGER_37, ErrorConstants.ERROR_MESSAGE_MANAGER_37, ex );
		}	
	}

	
	// PUBLIC STATIC SYNCHRONIZED METHODS
	public static synchronized void addActiveXFormsDBWidgetQueryElements( HttpSession session, Document document ) throws ManagerException {
		logger.log( Level.DEBUG, "Method has been called." );
		Map<String, Element> activeXFormsDBWidgetQueryElements	= null;
		Element xformsdbWidgetQueryElement						= null;
		String id												= null;
		
		
		try {
			// Retrieve the active <xformsdb:widgetquery> elements from the session
			activeXFormsDBWidgetQueryElements					= XFormsDBWidgetQueryManager.getActiveXFormsDBWidgetQueryElements( session );
			
	
			Element xformsdbInstancesElement					= document.getRootElement().getFirstChildElement( "instances", Constants.NAMESPACE_URI_XFORMSDB );
			Elements xformsdbInstanceElements					= xformsdbInstancesElement.getChildElements( "instance", Constants.NAMESPACE_URI_XFORMSDB );
			Element xformsdbInstanceElement						= null;
			for ( int i = 0; i < xformsdbInstanceElements.size(); i++ ) {
				xformsdbInstanceElement							= xformsdbInstanceElements.get( i );
				xformsdbWidgetQueryElement						= xformsdbInstanceElement.getFirstChildElement( "widgetquery", Constants.NAMESPACE_URI_XFORMSDB );
				if ( xformsdbWidgetQueryElement != null ) {
					id											= xformsdbWidgetQueryElement.getAttributeValue( "id" );
					// Add the active <xformsdb:widgetquery> element into the map of active <xformsdb:widgetquery> elements
					activeXFormsDBWidgetQueryElements.put( id, new Element( xformsdbWidgetQueryElement ) );
				}
			}
					
			// Set the active <xformsdb:widgetquery> elements into the session
			XFormsDBWidgetQueryManager.setActiveXFormsDBWidgetQueryElements( session, activeXFormsDBWidgetQueryElements );
			logger.log( Level.DEBUG, "The active <xformsdb:widgetquery> elements have been successfully added into the session." );
		} catch ( ManagerException mex ) {
			throw mex;
		} catch ( Exception ex ) {
			throw new ManagerException( ErrorConstants.ERROR_CODE_MANAGER_31, ErrorConstants.ERROR_MESSAGE_MANAGER_31, ex );
		}
	}
	
	
	public static synchronized Element getActiveXFormsDBWidgetQueryElement( HttpSession session, String id ) throws ManagerException {
		logger.log( Level.DEBUG, "Method has been called." );
		Map<String, Element> activeXFormsDBWidgetQueryElements	= null;
		Element activeXFormsDBWidgetQueryElement				= null;
		

		try {
			// Retrieve the active <xformsdb:widgetquery> elements from the session
			activeXFormsDBWidgetQueryElements					= XFormsDBWidgetQueryManager.getActiveXFormsDBWidgetQueryElements( session );
			activeXFormsDBWidgetQueryElement					= activeXFormsDBWidgetQueryElements.get( id );
			logger.log( Level.DEBUG, "The active <xformsdb:widgetquery> element has been successfully retrieved from the session." );
		} catch ( ManagerException mex ) {
			throw mex;
		} catch ( Exception ex ) {
			throw new ManagerException( ErrorConstants.ERROR_CODE_MANAGER_32, ErrorConstants.ERROR_MESSAGE_MANAGER_32, ex );
		}
		
		
		return activeXFormsDBWidgetQueryElement;
	}	
	
	
	public static synchronized void addSelect4UpdateXFormsDBWidgetQueryResult( HttpSession session, String widgetQueryId, String xformsdbWidgetQueryResult ) throws ManagerException {
		logger.log( Level.DEBUG, "Method has been called." );
		Map<String, String> select4UpdateXFormsDBWidgetQueryResults	= null;
		

		try {
			// Retrieve the SELECT4UPDATE XFormsDB widget query results from the session
			select4UpdateXFormsDBWidgetQueryResults					= XFormsDBWidgetQueryManager.getSelect4UpdateXFormsDBWidgetQueryResults( session );
			
			// Add the <xformsdb:widgetquery> result into the map of SELECT4UPDATE <xformsdb:widgetquery> results
			select4UpdateXFormsDBWidgetQueryResults.put( widgetQueryId, xformsdbWidgetQueryResult );
			
			// Set the SELECT4UPDATE <xformsdb:widgetquery> results into the session
			XFormsDBWidgetQueryManager.setSelect4UpdateXFormsDBWidgetQueryResults( session, select4UpdateXFormsDBWidgetQueryResults );
			logger.log( Level.DEBUG, "The SELECT4UPDATE <xformsdb:widgetquery> results have been successfully added into the session." );
		} catch ( ManagerException mex ) {
			throw mex;
		} catch ( Exception ex ) {
			throw new ManagerException( ErrorConstants.ERROR_CODE_MANAGER_38, ErrorConstants.ERROR_MESSAGE_MANAGER_38, ex );
		}
	}

	
	public static synchronized String getSelect4UpdateXFormsDBWidgetQueryResult( HttpSession session, String id ) throws ManagerException {
		logger.log( Level.DEBUG, "Method has been called." );
		Map<String, String> select4UpdateXFormsDBWidgetQueryResults	= null;
		String select4UpdateXFormsDBWidgetQueryResult				= null;
		
		
		try {
			// Retrieve the SELECT4UPDATE <xformsdb:widgetquery> results from the session
			select4UpdateXFormsDBWidgetQueryResults					= XFormsDBWidgetQueryManager.getSelect4UpdateXFormsDBWidgetQueryResults( session );
			select4UpdateXFormsDBWidgetQueryResult					= select4UpdateXFormsDBWidgetQueryResults.get( id );
			logger.log( Level.DEBUG, "The SELECT4UPDATE <xformsdb:widgetquery> result has been successfully retrieved from the session." );
		} catch ( ManagerException mex ) {
			throw mex;
		} catch ( Exception ex ) {
			throw new ManagerException( ErrorConstants.ERROR_CODE_MANAGER_39, ErrorConstants.ERROR_MESSAGE_MANAGER_39, ex );
		}
		
		
		return select4UpdateXFormsDBWidgetQueryResult;
	}
}