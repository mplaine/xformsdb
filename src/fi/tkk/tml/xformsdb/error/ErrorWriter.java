package fi.tkk.tml.xformsdb.error;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fi.tkk.tml.xformsdb.core.Constants;
import fi.tkk.tml.xformsdb.manager.HTTPRequestHeadersGetManager;
import fi.tkk.tml.xformsdb.manager.xformsdb.XFormsDBUserManager;
import fi.tkk.tml.xformsdb.util.ExceptionUtils;
import fi.tkk.tml.xformsdb.util.IOUtils;
import fi.tkk.tml.xformsdb.util.StringUtils;
import fi.tkk.tml.xformsdb.util.XMLEscapeUtils;
import fi.tkk.tml.xformsdb.util.XMLUtils;
import fi.tkk.tml.xformsdb.xml.to.RequestHeaderTO;


/**
 * Writer errors.
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on November 06, 2009
 */
public class ErrorWriter {

	

	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger	= Logger.getLogger( ErrorWriter.class );

		
	// PUBLIC CONSTURCTORS
	public ErrorWriter() {
		logger.log( Level.DEBUG, "Constructor has been called." );
	}
	
	
	// PRIVATE METHODS
	
	private String getErrorStringInXHTML( String encoding, XFormsDBException xex ) throws ErrorException {
		logger.log( Level.DEBUG, "Method has been called." );
		String errorString	= null;
		
		
		try {
			errorString	= "<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>" + Constants.LINE_SEPARATOR
						+ "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">" + Constants.LINE_SEPARATOR
						+ "\t<head>" + Constants.LINE_SEPARATOR
						+ "\t\t<title>XFormsDB Error</title>" + Constants.LINE_SEPARATOR
						+ "\t</head>" + Constants.LINE_SEPARATOR
						+ "\t<body>" + Constants.LINE_SEPARATOR
						+ "\t\t<h1>XFormsDB Error</h1>" + Constants.LINE_SEPARATOR
						+ "\t\t<p>" + Constants.LINE_SEPARATOR
						+ "\t\t\t<b>Error code:</b>" + Constants.LINE_SEPARATOR
						+ "\t\t\t" + xex.getCode() + Constants.LINE_SEPARATOR
						+ "\t\t</p>" + Constants.LINE_SEPARATOR
						+ "\t\t<p>" + Constants.LINE_SEPARATOR
						+ "\t\t\t<b>Error description:</b>" + Constants.LINE_SEPARATOR
						+ "\t\t\t" + XMLEscapeUtils.escapeXml( ExceptionUtils.getMessage( xex ) ) + Constants.LINE_SEPARATOR
						+ "\t\t</p>" + Constants.LINE_SEPARATOR
						+ "\t</body>" + Constants.LINE_SEPARATOR
						+ "</html>";
			logger.log( Level.DEBUG, "The error in the XHTML format has been successfully retrieved." );
		} catch ( Exception ex ) {
			throw new ErrorException( ErrorConstants.ERROR_CODE_ERRORWRITER_1, ErrorConstants.ERROR_MESSAGE_ERRORWRITER_1, ex );
		}

		
		return errorString;
	}
	
	
	private String getErrorStringInXML( String encoding, XFormsDBException xex ) throws ErrorException {
		logger.log( Level.DEBUG, "Method has been called." );
		String errorString	= null;
		
		
		try {
			errorString	= "<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>" + Constants.LINE_SEPARATOR
						+ "<xformsdb:error xmlns:xformsdb=\"http://www.tml.tkk.fi/2007/xformsdb\">" + Constants.LINE_SEPARATOR
						+ "\t<xformsdb:code>" + xex.getCode() + "</xformsdb:code>" + Constants.LINE_SEPARATOR
						+ "\t<xformsdb:description>" + Constants.LINE_SEPARATOR
						+ "\t\t" + XMLEscapeUtils.escapeXml( ExceptionUtils.getMessage( xex ) ) + Constants.LINE_SEPARATOR
						+ "\t</xformsdb:description>" + Constants.LINE_SEPARATOR
						+ "</xformsdb:error>";
			logger.log( Level.DEBUG, "The error in the XML format has been successfully retrieved." );
		} catch ( Exception ex ) {
			throw new ErrorException( ErrorConstants.ERROR_CODE_ERRORWRITER_2, ErrorConstants.ERROR_MESSAGE_ERRORWRITER_2, ex );
		}

		
		return errorString;
	}

	
	private String getErrorStringInXForms( String encoding, XFormsDBException xex ) throws ErrorException {
		logger.log( Level.DEBUG, "Method has been called." );
		String errorString	= null;
		
		
		try {
			errorString	= "<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>" + Constants.LINE_SEPARATOR
						+ "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\"" + Constants.LINE_SEPARATOR
						+ "\txmlns:ev=\"http://www.w3.org/2001/xml-events\"" + Constants.LINE_SEPARATOR
						+ "\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" + Constants.LINE_SEPARATOR
						+ "\txmlns:xs=\"http://www.w3.org/2001/XMLSchema\"" + Constants.LINE_SEPARATOR
						+ "\txmlns:xforms=\"http://www.w3.org/2002/xforms\"" + Constants.LINE_SEPARATOR
						+ "\txmlns:xformsdb=\"http://www.tml.tkk.fi/2007/xformsdb\">" + Constants.LINE_SEPARATOR
						+ "\t<head>" + Constants.LINE_SEPARATOR
						+ "\t\t<title>XFormsDB Error</title>" + Constants.LINE_SEPARATOR
						+ "\t\t<xforms:model>" + Constants.LINE_SEPARATOR
						+ "\t\t\t<!-- A detailed error message -->" + Constants.LINE_SEPARATOR
						+ "\t\t\t<xforms:instance id=\"error-instance\">" + Constants.LINE_SEPARATOR
						+ "\t\t\t\t<xformsdb:error>" + Constants.LINE_SEPARATOR
						+ "\t\t\t\t\t<xformsdb:code>" + xex.getCode() + "</xformsdb:code>" + Constants.LINE_SEPARATOR
						+ "\t\t\t\t\t<xformsdb:description>" + XMLEscapeUtils.escapeXml( ExceptionUtils.getMessage( xex ) ) + "</xformsdb:description>" + Constants.LINE_SEPARATOR
						+ "\t\t\t\t</xformsdb:error>" + Constants.LINE_SEPARATOR
						+ "\t\t\t</xforms:instance>" + Constants.LINE_SEPARATOR
						+ "\t\t</xforms:model>" + Constants.LINE_SEPARATOR
						+ "\t</head>" + Constants.LINE_SEPARATOR
						+ "\t<body>" + Constants.LINE_SEPARATOR
						+ "\t\t<h1>XFormsDB Error</h1>" + Constants.LINE_SEPARATOR
						+ "\t\t<p>" + Constants.LINE_SEPARATOR
						+ "\t\t\t<b>Error code:</b>" + Constants.LINE_SEPARATOR
						+ "\t\t\t<xforms:output ref=\"xformsdb:code\" />" + Constants.LINE_SEPARATOR
						+ "\t\t</p>" + Constants.LINE_SEPARATOR
						+ "\t\t<p>" + Constants.LINE_SEPARATOR
						+ "\t\t\t<b>Error description:</b>" + Constants.LINE_SEPARATOR
						+ "\t\t\t<xforms:output ref=\"xformsdb:description\" />" + Constants.LINE_SEPARATOR
						+ "\t\t</p>" + Constants.LINE_SEPARATOR
						+ "\t</body>" + Constants.LINE_SEPARATOR
						+ "</html>";
			logger.log( Level.DEBUG, "The error in the XForms format has been successfully retrieved." );
		} catch ( Exception ex ) {
			throw new ErrorException( ErrorConstants.ERROR_CODE_ERRORWRITER_3, ErrorConstants.ERROR_MESSAGE_ERRORWRITER_3, ex );
		}

		
		return errorString;
	}
	
	
	private String printStackTraceToString( Exception ex ) throws ErrorException {
		logger.log( Level.DEBUG, "Method has been called." );
		String printStackTraceString	= null;
		StringWriter sw					= null;
		PrintWriter pw					= null;
		
		
		try {
			sw							= new StringWriter();
			pw							= new PrintWriter( sw );
			ex.printStackTrace( pw );
		    printStackTraceString		= sw.toString();
			logger.log( Level.DEBUG, "The error stack track has been successfully printed to string." );
		} catch ( Exception ex2 ) {
			throw new ErrorException( ErrorConstants.ERROR_CODE_ERRORWRITER_4, ErrorConstants.ERROR_MESSAGE_ERRORWRITER_4, ex2 );
		} finally {
			try {
				if ( sw != null ) {
					sw.close();
				}
				if ( pw != null ) {
					pw.close();
				}
			} catch ( IOException ioex ) {
				logger.log( Level.ERROR, "Failed to close the writers.", ioex );
			}
		}
		
		
		return printStackTraceString;
	}
	
	
	private String getLastModifiedString( long lastModified ) {
		logger.log( Level.DEBUG, "Method has been called." );
		String lastModifiedStr		= null;
		DateFormat dateFormatter	= null;
		Date lastModifiedDate		= null;
		
		dateFormatter				= new SimpleDateFormat( Constants.RESOURCE_LAST_MODIFIED_FORMAT, Locale.ENGLISH );
		dateFormatter.setTimeZone( TimeZone.getTimeZone( Constants.RESOURCE_LAST_MODIFIED_TIME_ZONE ) );
		
		// Last modified set (=static resource)
		if ( lastModified > 0 ) {
			lastModifiedDate	= new Date( lastModified );
		}
		// Last modified not set (=dynamic resource)
		else {
			lastModifiedDate	= new Date();
		}
		
		lastModifiedStr			= dateFormatter.format( lastModifiedDate );
		
		
		return lastModifiedStr;
	}

	
	// PUBLIC METHODS
	/**
	 * Write the error.
	 * 
	 * 
	 * @param format				The format of the error.
	 * @param response				The response.
	 * @param request				The request.
	 * @param encoding				The encoding.
	 * @param mimeType				The mime type.
	 * @param serverURIEncoding		The server URI encoding.
	 * @param xex					The exception in the default encoding format.
	 */
	public void write( int format, HttpServletResponse response, HttpServletRequest request, String encoding, String mimeType, String serverURIEncoding, XFormsDBException xex ) throws ErrorException {
		logger.log( Level.DEBUG, "Method has been called." );
		HttpSession session							= null;
		String replaceType							= null;
		String characterEncoding					= null;
		String contentType							= null;
		String errorString							= null;
		InputStream content							= null;
		String requestURL							= null;
		String xformsdbUser							= null;
		String requestHeadersGetString				= null;
		List<RequestHeaderTO> requestHeaderTOs		= null;
		RequestHeaderTO requestHeaderTO				= null;
		Enumeration requestHeaderNames				= null;
		String requestHeadersString					= null;
		String requestHeaderName					= null;
		Enumeration requestHeaderValues				= null;
		boolean addLineSeparator					= false;
		int contentLengthB							= 0;
		double contentLengthKB						= 0.0;
		NumberFormat numberFormat					= NumberFormat.getNumberInstance( Locale.ENGLISH );
		numberFormat.setMinimumIntegerDigits( 1 );
		numberFormat.setMinimumFractionDigits( 1 );
		numberFormat.setMaximumFractionDigits( 1 );		
				
		
		try {
			// Retrieve the session
			session									= request.getSession();

			
			// Correct format if needed
			replaceType								= ( String ) request.getAttribute( Constants.REQUEST_ATTRIBUTE_REPLACETYPE );
			if ( format == Constants.ERROR_FORMAT_XML && Constants.REPLACE_TYPE_ALL.equals( replaceType  ) == true ) {
				format								= Constants.ERROR_FORMAT_XHTML;
			}


			// Retrieve the HTTP request URL
			if ( request.getQueryString() != null ) {
				requestURL							= URLDecoder.decode( request.getRequestURL().toString() + "?" + request.getQueryString(), Constants.CLIENT_URI_ENCODING_DEFAULT );
			}
			else {
				requestURL							= URLDecoder.decode( request.getRequestURL().toString(), Constants.CLIENT_URI_ENCODING_DEFAULT );
			}

			// Retrieve the logged in user from XFormsDB
			xformsdbUser							= XFormsDBUserManager.getLoggedInXFormsDBUser( session );
			
			// Retrieve/write the HTTP request headers (GET) from the session
			requestHeadersGetString					= "";
			requestHeaderTOs						= HTTPRequestHeadersGetManager.getHTTPRequestHeadersGet( session );
			for ( int i = 0; i < requestHeaderTOs.size(); i++ ) {
				requestHeaderTO						= requestHeaderTOs.get( i );
				if ( addLineSeparator == false ) {
					addLineSeparator				= true;
				}
				else {
					requestHeadersGetString			+= Constants.LINE_SEPARATOR;
				}
				requestHeadersGetString				+= requestHeaderTO.getName() + ": " + requestHeaderTO.getValue();			
			}
			
			
			// Retrieve/write the HTTP request headers
			requestHeadersString					= "";
			requestHeaderNames						= request.getHeaderNames();
			addLineSeparator						= false;
			// The error occurred during the same GET request (request header names have been already iterated)
			if ( requestHeaderNames.hasMoreElements() == false ) {
				requestHeadersString				= requestHeadersGetString;
			}
			// The error occurred during a different request
			else {
				// Iterate over request header names
				while ( requestHeaderNames.hasMoreElements() ) {
					requestHeaderName				= ( String ) requestHeaderNames.nextElement();
					requestHeaderValues				= request.getHeaders( requestHeaderName );
					// Iterate over request header values of that particular request header name
					while ( requestHeaderValues.hasMoreElements() ) {
						if ( addLineSeparator == false ) {
							addLineSeparator		= true;
						}
						else {
							requestHeadersString	+= Constants.LINE_SEPARATOR;
						}
						requestHeadersString		+= StringUtils.decode( StringUtils.encode( requestHeaderName, serverURIEncoding ), Constants.CLIENT_URI_ENCODING_DEFAULT ) + ": " + StringUtils.decode( StringUtils.encode( ( String ) requestHeaderValues.nextElement(), serverURIEncoding ), Constants.CLIENT_URI_ENCODING_DEFAULT );						
					}
				}
				
			}

			
			// Get error string in correct format
			if ( format == Constants.ERROR_FORMAT_XML ) {
				errorString							= this.getErrorStringInXML( encoding, xex );
			}
			else if ( format == Constants.ERROR_FORMAT_XHTML ) {
				errorString							= this.getErrorStringInXHTML( encoding, xex );				
			}
			// Convert to the correct (final) encoding format
			content									= IOUtils.convert( errorString, encoding );
			
			
			// Own approach based on best practices: modify given values
			// Treat the application/xhtml+xml MIME type in a special way
			if ( mimeType.equals( Constants.MIME_TYPE_APPLICATION_XHTML_XML ) == true ) {
				// Test browser
				String headerAccept					= request.getHeader( "accept" );
				
				// XHTML Browser, i.e. Firefox
				if ( headerAccept.indexOf( Constants.MIME_TYPE_APPLICATION_XHTML_XML ) != -1 ) {
					logger.log( Level.DEBUG, "XHTML browser" );
					characterEncoding				= encoding;
					// Serve as XML
					contentType						= Constants.MIME_TYPE_APPLICATION_XHTML_XML + "; charset=" + characterEncoding;							
				}
				// HTML Browser, i.e. Internet Explorer
				else {
					logger.log( Level.DEBUG, "HTML browser" );
					characterEncoding				= encoding;
					// Serve as HTML
					contentType						= Constants.MIME_TYPE_TEXT_HTML + "; charset=" + characterEncoding;
					// Best practices: do not include XML declaration in HTML files --> Remove XML declaration
					String xmlString				= IOUtils.convert( content, encoding, true );
					xmlString						= XMLUtils.filterXMLDeclaration( xmlString );
					content							= IOUtils.convert( xmlString, encoding );
				}
			}
			// Treat other MIME types normally, i.e. use optimistic approach
			else {
				characterEncoding					= encoding;
				contentType							= mimeType + "; charset=" + characterEncoding;
			}


			logger.log( Level.DEBUG, "Character encoding: " + characterEncoding );
			logger.log( Level.DEBUG, "Content type: " + contentType );
						
			// For debugging
			String errorStr							= IOUtils.convert( content, encoding, true );
			logger.log( Level.ERROR, "Error in the XML/XHTML format:" + Constants.LINE_SEPARATOR + errorStr );
			logger.log( Level.ERROR, "Additional error information: Stacktrace:" + Constants.LINE_SEPARATOR + this.printStackTraceToString( xex ) );
			if ( xformsdbUser == null ) {
				logger.log( Level.ERROR, "Additional error information: The logged in user: " + xformsdbUser );
			}
			else {
				logger.log( Level.ERROR, "Additional error information: The logged in user:" + Constants.LINE_SEPARATOR + xformsdbUser );
			}
			logger.log( Level.ERROR, "Additional error information: HTTP request URL: " + requestURL );
			logger.log( Level.ERROR, "Additional error information: HTTP request headers (latest GET/POST):" + Constants.LINE_SEPARATOR + requestHeadersString );
			logger.log( Level.ERROR, "Additional error information: HTTP request headers (latest GET):" + Constants.LINE_SEPARATOR + requestHeadersGetString );
			content									= IOUtils.convert( errorStr, encoding );
			// For debugging

			// Retrieve the content length
			ByteArrayOutputStream baot				= new ByteArrayOutputStream();
			contentLengthB							= IOUtils.convertAndRetrieveContentLength( content, baot, true, true );
			contentLengthKB							= ( double ) contentLengthB / 1024;
			content									= new ByteArrayInputStream( baot.toByteArray() );

			
			// NO CACHING
			// Set to expire far in the past
			response.setHeader( "Expires", "Sat, 6 May 1995 12:00:00 GMT" );
			// Set standard HTTP/1.1 no-cache headers
			response.setHeader( "Cache-Control", "no-store, no-cache, must-revalidate" );
			// Set IE extended HTTP/1.1 no-cache headers (use addHeader)
			response.addHeader( "Cache-Control", "post-check=0, pre-check=0" );
			// Set standard HTTP/1.0 no-cache header
			response.setHeader( "Pragma", "no-cache" );
			// Set character encoding, content type etc.
			response.setCharacterEncoding( characterEncoding );
			response.setContentType( contentType );
			response.setContentLength( contentLengthB );
			response.setHeader( "Last-Modified", this.getLastModifiedString( 0 ) );

			// Write the body of the response
			IOUtils.convert( content, response.getOutputStream(), true, true );
			
			logger.log( Level.INFO, "XFormsDB response size: " + contentLengthB + " bytes (" + numberFormat.format( contentLengthKB ) + " kB)" );
			logger.log( Level.DEBUG, "The response has been successfully handled." );
		} catch ( ErrorException eex ) {
			throw eex;
		} catch ( Exception ex ) {
			throw new ErrorException( ErrorConstants.ERROR_CODE_ERRORWRITER_5, ErrorConstants.ERROR_MESSAGE_ERRORWRITER_5, ex );
		} finally {
			try {
				if ( content != null ) {
					content.close();
				}
			} catch ( IOException ioex ) {
				logger.log( Level.ERROR, "Failed to close the stream.", ioex );
			}
		}
	}
}