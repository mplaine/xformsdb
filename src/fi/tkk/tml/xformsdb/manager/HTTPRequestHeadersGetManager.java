package fi.tkk.tml.xformsdb.manager;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fi.tkk.tml.xformsdb.core.Constants;
import fi.tkk.tml.xformsdb.error.ErrorConstants;
import fi.tkk.tml.xformsdb.error.ManagerException;
import fi.tkk.tml.xformsdb.xml.to.RequestHeaderTO;


/**
 * Manage the HTTP request headers (GET).
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on October 26, 2009
 */
public class HTTPRequestHeadersGetManager {

	

	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger	= Logger.getLogger( HTTPRequestHeadersGetManager.class );
	
	
	// PRIVATE CONSTURCTORS
	// Prevent instantiation of this class
	private HTTPRequestHeadersGetManager() {
		logger.log( Level.DEBUG, "Constructor has been called." );
	}	

	
	// PUBLIC METHODS
	public static synchronized void setHTTPRequestHeadersGet( HttpSession session, List<RequestHeaderTO> httpRequestHeadersGet ) throws ManagerException {
		logger.log( Level.DEBUG, "Method has been called." );
		
		
		try {
			// Set the HTTP request headers (GET) into the session
			session.setAttribute( Constants.SESSION_ATTRIBUTE_HTTP_REQUEST_HEADERS_GET, httpRequestHeadersGet );
			logger.log( Level.DEBUG, "The HTTP request headers (GET) have been successfully set into the session." );
		} catch ( Exception ex ) {
			throw new ManagerException( ErrorConstants.ERROR_CODE_MANAGER_26, ErrorConstants.ERROR_MESSAGE_MANAGER_26, ex );
		}
	}

	
	public static synchronized void removeHTTPRequestHeadersGet( HttpSession session ) throws ManagerException {
		logger.log( Level.DEBUG, "Method has been called." );		
		
		
		try {
			// Remove the HTTP request headers (GET) from the session
			session.removeAttribute( Constants.SESSION_ATTRIBUTE_HTTP_REQUEST_HEADERS_GET );
			logger.log( Level.DEBUG, "The HTTP request headers (GET) have been successfully removed from the session." );
		} catch ( Exception ex ) {
			throw new ManagerException( ErrorConstants.ERROR_CODE_MANAGER_27, ErrorConstants.ERROR_MESSAGE_MANAGER_27, ex );
		}
	}

	
	public static synchronized List<RequestHeaderTO> getHTTPRequestHeadersGet( HttpSession session ) throws ManagerException {
		logger.log( Level.DEBUG, "Method has been called." );
		List<RequestHeaderTO> httpRequestHeadersGet	= null;
		
		
		try {
			// Retrieve/write the HTTP request headers (GET) from the session
			httpRequestHeadersGet					= ( List<RequestHeaderTO> ) session.getAttribute( Constants.SESSION_ATTRIBUTE_HTTP_REQUEST_HEADERS_GET );
			if ( httpRequestHeadersGet == null ) {
				httpRequestHeadersGet				= new ArrayList<RequestHeaderTO>();
			}
			logger.log( Level.DEBUG, "The HTTP request headers (GET) have been successfully retrieved from the session." );
		} catch ( Exception ex ) {
			throw new ManagerException( ErrorConstants.ERROR_CODE_MANAGER_28, ErrorConstants.ERROR_MESSAGE_MANAGER_28, ex );
		}
		
		
		return httpRequestHeadersGet;
	}		
}