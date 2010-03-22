package fi.tkk.tml.xformsdb.manager.xformsdb;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fi.tkk.tml.xformsdb.core.Constants;
import fi.tkk.tml.xformsdb.error.ErrorConstants;
import fi.tkk.tml.xformsdb.error.ManagerException;


/**
 * Manage the logged in <xformsdb:widget-data-sources> element.
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on October 22, 2009
 */
public class XFormsDBWidgetDataSourcesManager {

	

	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger	= Logger.getLogger( XFormsDBWidgetDataSourcesManager.class );
	
	
	// PRIVATE CONSTURCTORS
	// Prevent instantiation of this class
	private XFormsDBWidgetDataSourcesManager() {
		logger.log( Level.DEBUG, "Constructor has been called." );
	}	

	
	// PUBLIC METHODS
	public static synchronized void setXFormsDBWidgetDataSources( HttpSession session, String xformsdbWidgetDataSources ) throws ManagerException {
		logger.log( Level.DEBUG, "Method has been called." );
		
		
		try {
			// Set the <xformsdb:widget-data-sources> element into the session
			session.setAttribute( Constants.SESSION_ATTRIBUTE_XFORMSDB_WIDGET_DATA_SOURCES, xformsdbWidgetDataSources );
			logger.log( Level.DEBUG, "The <xformsdb:widget-data-sources> element has been successfully set into the session." );
		} catch ( Exception ex ) {
			throw new ManagerException( ErrorConstants.ERROR_CODE_MANAGER_33, ErrorConstants.ERROR_MESSAGE_MANAGER_33, ex );
		}
	}

	
	public static synchronized void removeXFormsDBWidgetDataSources( HttpSession session ) throws ManagerException {
		logger.log( Level.DEBUG, "Method has been called." );		
		
		
		try {
			// Remove the <xformsdb:widget-data-sources> element from the session
			session.removeAttribute( Constants.SESSION_ATTRIBUTE_XFORMSDB_WIDGET_DATA_SOURCES );
			logger.log( Level.DEBUG, "The <xformsdb:widget-data-sources> element has been successfully removed from the session." );
		} catch ( Exception ex ) {
			throw new ManagerException( ErrorConstants.ERROR_CODE_MANAGER_34, ErrorConstants.ERROR_MESSAGE_MANAGER_34, ex );
		}
	}

	
	public static synchronized String getXFormsDBWidgetDataSources( HttpSession session ) throws ManagerException {
		logger.log( Level.DEBUG, "Method has been called." );
		String xformsdbWidgetDataSources	= null;
		
		
		try {
			// Retrieve/write the <xformsdb:widget-data-sources> element from the session
			xformsdbWidgetDataSources		= ( String ) session.getAttribute( Constants.SESSION_ATTRIBUTE_XFORMSDB_WIDGET_DATA_SOURCES );
			logger.log( Level.DEBUG, "The <xformsdb:widget-data-sources> element has been successfully retrieved from the session." );
		} catch ( Exception ex ) {
			throw new ManagerException( ErrorConstants.ERROR_CODE_MANAGER_35, ErrorConstants.ERROR_MESSAGE_MANAGER_35, ex );
		}
		
		
		return xformsdbWidgetDataSources;
	}		
}