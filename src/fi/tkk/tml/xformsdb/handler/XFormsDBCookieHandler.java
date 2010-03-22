package fi.tkk.tml.xformsdb.handler;

import javax.servlet.http.Cookie;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fi.tkk.tml.xformsdb.core.Constants;
import fi.tkk.tml.xformsdb.error.ErrorConstants;
import fi.tkk.tml.xformsdb.error.HandlerException;
import fi.tkk.tml.xformsdb.servlet.XFormsDBServlet;
import fi.tkk.tml.xformsdb.xml.to.xformsdb.XFormsDBConfigTO;


/**
 * Handle the <xformsdb:cookie> requests.
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on October 21, 2009
 */
public class XFormsDBCookieHandler extends ResponseHandler {
	
	
	
	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger		= Logger.getLogger( XFormsDBCookieHandler.class );

	
	// PUBLIC CONSTURCTORS
	public XFormsDBCookieHandler() {
		logger.log( Level.DEBUG, "Constructor has been called." );
	}
	
	
	// PUBLIC METHODS
	public String handle( XFormsDBServlet xformsdbServlet, Cookie[] cookies ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		XFormsDBConfigTO xformsDBConfigTO	= null;
		String xformsdbCookie				= null;
		boolean cookiesEnabled				= false;
		Cookie cookie						= null;
		
		
		try {
			// Retrieve the XFormsDB configuration file
			xformsDBConfigTO				= xformsdbServlet.getXFormsDBConfigTO();

			// Check browser support for cookies
			if ( cookies != null ) {
				for ( int i = 0; i < cookies.length; i++ ) {
					cookie					= cookies[ i ];
					if ( Constants.JSESSIONID_COOKIE.equals( cookie.getName() ) == true ) {
						cookiesEnabled		= true;
						break;
					}
				}
			}
			else {
				cookiesEnabled				= false;
			}

			// Write the response XML string i.e. empty XFormsDB user element
			xformsdbCookie					= "<?xml version=\"" + Constants.OUTPUT_XML_VERSION_1_0 + "\" encoding=\"" + xformsDBConfigTO.getEncoding() + "\"?>" + Constants.LINE_SEPARATOR;
			if ( cookiesEnabled == true ) {
				xformsdbCookie				+= "<" + Constants.XFORMSDB_COOKIE + " " + Constants.NAMESPACE_XMLNS_XFORMSDB + ">";
				xformsdbCookie				+= Constants.JSESSIONID_COOKIE;
				xformsdbCookie				+= "</" + Constants.XFORMSDB_COOKIE + ">";				
			}
			else {
				xformsdbCookie				+= "<" + Constants.XFORMSDB_COOKIE + " " + Constants.NAMESPACE_XMLNS_XFORMSDB + "/>";				
			}

			
			logger.log( Level.DEBUG, "The browser support for cookies has been successfully checked." );
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBCOOKIEHANDLER_1, ErrorConstants.ERROR_MESSAGE_XFORMSDBCOOKIEHANDLER_1, ex  );
		}
		
		
		return xformsdbCookie;
	}	
}