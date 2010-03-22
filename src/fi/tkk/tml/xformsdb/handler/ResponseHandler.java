package fi.tkk.tml.xformsdb.handler;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fi.tkk.tml.xformsdb.core.Constants;
import fi.tkk.tml.xformsdb.error.ErrorConstants;
import fi.tkk.tml.xformsdb.error.HandlerException;
import fi.tkk.tml.xformsdb.util.IOUtils;
import fi.tkk.tml.xformsdb.util.XMLUtils;


/**
 * Handle responses of the XFormsDB servlet.
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on November 06, 2009
 */
public class ResponseHandler {

	

	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger		= Logger.getLogger( ResponseHandler.class );

		
	// PUBLIC CONSTURCTORS
	public ResponseHandler() {
		super();
		logger.log( Level.DEBUG, "Constructor has been called." );
	}
	
	
	// PRIVATE METHODS
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
	 * Write the response.
	 * 
	 * 
	 * @param response			The response.
	 * @param request			The request.
	 * @param mimeType			The mime type.
	 * @param charsetName		The charset name.
	 * @param xformsDBEncoding	The encoding.
	 * @param content			The content in the correct encoding format.
	 * @param logResponse		<code>true</code> if the response need to be logged,
	 * 							otherwise <code>false</code>.
	 */
	public void handle( HttpServletResponse response, HttpServletRequest request, String mimeType, String charsetName, String xformsDBEncoding, InputStream content, boolean logResponse ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		this.handle( response, request, mimeType, charsetName, xformsDBEncoding, content, 0, logResponse );
	}
	
	
	/**
	 * Write the response.
	 * 
	 * 
	 * @param response			The response.
	 * @param request			The request.
	 * @param mimeType			The mime type.
	 * @param charsetName		The charset name.
	 * @param xformsDBEncoding	The encoding.
	 * @param content			The content in the correct encoding format.
	 * @param lastModified		The last modified.
	 * @param logResponse		<code>true</code> if the response need to be logged,
	 * 							otherwise <code>false</code>.
	 */
	public void handle( HttpServletResponse response, HttpServletRequest request, String mimeType, String charsetName, String xformsDBEncoding, InputStream content, long lastModified, boolean logResponse ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		String characterEncoding			= null;
		String contentType					= null;
		int contentLengthB					= 0;
		double contentLengthKB				= 0.0;
		NumberFormat numberFormat			= NumberFormat.getNumberInstance( Locale.ENGLISH );
		numberFormat.setMinimumIntegerDigits( 1 );
		numberFormat.setMinimumFractionDigits( 1 );
		numberFormat.setMaximumFractionDigits( 1 );		
		
		
		try {
			logger.log( Level.DEBUG, "Charset name: " + charsetName );
			logger.log( Level.DEBUG, "MIME type: " + mimeType );

			if ( mimeType != null ) {
				// Own approach based on best practices: modify given values
				// Treat the application/xhtml+xml MIME type in a special way
				if ( mimeType.equals( Constants.MIME_TYPE_APPLICATION_XHTML_XML ) == true ) {
					// Test browser
					String headerAccept		= request.getHeader( "accept" );
					
					// XHTML Browser, i.e. Firefox
					if ( headerAccept.indexOf( Constants.MIME_TYPE_APPLICATION_XHTML_XML ) != -1 ) {
						logger.log( Level.DEBUG, "XHTML browser" );
						characterEncoding	= xformsDBEncoding;
						// Serve as XML
						contentType			= Constants.MIME_TYPE_APPLICATION_XHTML_XML + "; charset=" + characterEncoding;							
					}
					// HTML Browser, i.e. Internet Explorer
					else {
						logger.log( Level.DEBUG, "HTML browser" );
						characterEncoding	= xformsDBEncoding;
						// Serve as HTML
						contentType			= Constants.MIME_TYPE_TEXT_HTML + "; charset=" + characterEncoding;
						// Best practices: do not include XML declaration in HTML files --> Remove XML declaration
						String xmlString	= IOUtils.convert( content, xformsDBEncoding, true );
						xmlString			= XMLUtils.filterXMLDeclaration( xmlString );
						content				= IOUtils.convert( xmlString, xformsDBEncoding );
					}
				}
				// Treat other MIME types normally, i.e. use optimistic approach
				else {
					characterEncoding		= charsetName;
					if ( characterEncoding == null ) {
						contentType			= mimeType;
					}
					else {
						contentType			= mimeType + "; charset=" + characterEncoding;
					}
				}
			}
			else {
				// Optimistic approach: use given values
				characterEncoding			= charsetName;
				contentType					= mimeType;
			}

			logger.log( Level.DEBUG, "Character encoding: " + characterEncoding );
			logger.log( Level.DEBUG, "Content type: " + contentType );
			
			// For debugging
			if ( logResponse == true ) {
				String responseStr			= IOUtils.convert( content, xformsDBEncoding, true );
				logger.log( Level.DEBUG, "Response string:" + Constants.LINE_SEPARATOR + responseStr );
				content						= IOUtils.convert( responseStr, xformsDBEncoding );
			}
			// For debugging
			
			// Retrieve the content length
			ByteArrayOutputStream baot		= new ByteArrayOutputStream();
			contentLengthB					= IOUtils.convertAndRetrieveContentLength( content, baot, true, true );
			contentLengthKB					= ( double ) contentLengthB / 1024;
			content							= new ByteArrayInputStream( baot.toByteArray() );
			
			
			// NO CACHING
			// Set to expire far in the past
			response.setHeader( "Expires", "Sat, 6 May 1995 12:00:00 GMT" );
			// Ensure that dynamic resources will not be cached
			if ( lastModified == 0 ) {
				// Set standard HTTP/1.1 no-cache headers
				response.setHeader( "Cache-Control", "no-store, no-cache, must-revalidate" );
				// Set IE extended HTTP/1.1 no-cache headers (use addHeader)
				response.addHeader( "Cache-Control", "post-check=0, pre-check=0" );
				// Set standard HTTP/1.0 no-cache header
				response.setHeader( "Pragma", "no-cache" );
			}
			// Set character encoding, content type etc.
			response.setCharacterEncoding( characterEncoding );
			response.setContentType( contentType );
			response.setContentLength( contentLengthB );
			response.setHeader( "Last-Modified", this.getLastModifiedString( lastModified ) );
			
			// Write the body of the response
			IOUtils.convert( content, response.getOutputStream(), true, true );
			
			logger.log( Level.INFO, "XFormsDB response size: " + contentLengthB + " bytes (" + numberFormat.format( contentLengthKB ) + " kB)" );
			logger.log( Level.DEBUG, "The response has been successfully handled." );
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_RESPONSEHANDLER_1, ErrorConstants.ERROR_MESSAGE_RESPONSEHANDLER_1, ex  );
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
	
	
	/**
	 * Write the response for a file to be downloaded.
	 * 
	 * 
	 * @param response			The response.
	 * @param mimeType			The mime type.
	 * @param file				The file to be downloaded.
	 * @param id				The unique ID of the file to be downloaded.
	 */
	public void handleDownload( HttpServletResponse response, String mimeType, File file, String id ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		FileInputStream input				= null;
		BufferedInputStream buf				= null;
		int readBytes						= 0;
		int contentLengthB					= 0;
		double contentLengthKB				= 0.0;
		NumberFormat numberFormat			= NumberFormat.getNumberInstance( Locale.ENGLISH );
		numberFormat.setMinimumIntegerDigits( 1 );
		numberFormat.setMinimumFractionDigits( 1 );
		numberFormat.setMaximumFractionDigits( 1 );		
		
		
		try {
			logger.log( Level.DEBUG, "MIME type: " + mimeType );

			ServletOutputStream out			= response.getOutputStream();
			// NO CACHING
			// Set to expire far in the past
			response.setHeader( "Expires", "Sat, 6 May 1995 12:00:00 GMT" );
			// Set standard HTTP/1.1 no-cache headers
			//response.setHeader( "Cache-Control", "no-store, no-cache, must-revalidate" );
			response.setHeader( "Cache-control", "must-revalidate" ); // IE fix
			// Set IE extended HTTP/1.1 no-cache headers (use addHeader)
			response.addHeader( "Cache-Control", "post-check=0, pre-check=0" );
			// Set standard HTTP/1.0 no-cache header
			response.setHeader( "Pragma", "no-cache" );
			//response.setHeader( "Pragma", "public" ); // IE fix (not needed after all)
			// Set content type
			response.setContentType( mimeType );
			// SAVE AS DIALOG
			String fileName					= file.getName().substring( id.length() + 1 );
			response.addHeader( "Content-Disposition", "attachment; filename=\"" + fileName + "\"" ); // Important!
			
			// Retrieve content length
			contentLengthB					= ( int ) file.length();
			contentLengthKB					= ( double ) contentLengthB / 1024;

			// Set the last modified
			response.setHeader( "Last-Modified", this.getLastModifiedString( file.lastModified() ) );

			
			// Write the body of the response
			response.setContentLength( contentLengthB );
			input							= new FileInputStream( file );
			buf								= new BufferedInputStream( input );
			readBytes						= 0;
			while ( ( readBytes = buf.read()) != -1 ) {
				out.write( readBytes );
			}
			
			logger.log( Level.INFO, "XFormsDB response size: " + contentLengthB + " bytes (" + numberFormat.format( contentLengthKB ) + " kB)" );
			logger.log( Level.DEBUG, "The response for a file to be downloaded has been successfully handled." );
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_RESPONSEHANDLER_2, ErrorConstants.ERROR_MESSAGE_RESPONSEHANDLER_2, ex  );
		} finally {
			try {
				if ( input != null ) {
					input.close();
				}
				if ( buf != null ) {
					buf.close();
				}
			} catch ( IOException ioex ) {
				logger.log( Level.ERROR, "Failed to close the streams.", ioex );
			}
		}
	}
}