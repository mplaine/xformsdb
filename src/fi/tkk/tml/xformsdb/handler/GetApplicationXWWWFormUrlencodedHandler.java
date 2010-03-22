package fi.tkk.tml.xformsdb.handler;

import java.io.File;
import java.io.FileInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fi.tkk.tml.xformsdb.error.ErrorConstants;
import fi.tkk.tml.xformsdb.error.FileException;
import fi.tkk.tml.xformsdb.error.HandlerException;
import fi.tkk.tml.xformsdb.servlet.XFormsDBServlet;
import fi.tkk.tml.xformsdb.util.FileUtils;
import fi.tkk.tml.xformsdb.xml.to.WebAppTO;
import fi.tkk.tml.xformsdb.xml.to.xformsdb.XFormsDBConfigTO;


/**
 * Handle GET requests having the
 * content type: application/x-www-form-urlencoded.
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on October 15, 2009
 */
public class GetApplicationXWWWFormUrlencodedHandler extends ResponseHandler {
	
	
	
	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger	= Logger.getLogger( GetApplicationXWWWFormUrlencodedHandler.class );

		
	// PUBLIC CONSTURCTORS
	public GetApplicationXWWWFormUrlencodedHandler() {
		super();
		logger.log( Level.DEBUG, "Constructor has been called." );
	}
	
	
	// PRIVATE METHODS
	private File getRequestedFile( XFormsDBServlet xformsdbServlet, HttpServletRequest request ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		File requestedFile				= null;
		String requestedFileName		= null;
		String requestedFilePath		= null;

		
		try {
			// Get the requested file name
			requestedFileName			= this.getRequestedFileName( xformsdbServlet, request );
			
			// Retrieve the requested file by using the requested file path
			if ( "".equals( requestedFileName ) == false ) {
				logger.log( Level.DEBUG, "Retrieve the requested file by using the requested file path." );

				try {
					requestedFilePath	= xformsdbServlet.getServletContext().getRealPath( request.getRequestURL().toString().substring( request.getRequestURL().toString().indexOf( request.getContextPath().substring( 1 ) ) + request.getContextPath().substring( 1 ).length() ) );
					requestedFile		= FileUtils.getFile( requestedFilePath );
					logger.log( Level.DEBUG, "The requested file has been retrieved by using the requested file path (" + requestedFilePath + ")." );
				} catch ( FileException fex ) {
					logger.log( Level.DEBUG, "Failed to retrieve the requested file by using the requested file path (" + requestedFilePath + ").", fex );
					// Do nothing, because the file does not exist or cannot be read
				}
			}
		} catch ( HandlerException hex ) {
			throw hex;
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_GETAPPLICATIONXWWWFORMURLENCODEDHANDLER_1, ErrorConstants.ERROR_MESSAGE_GETAPPLICATIONXWWWFORMURLENCODEDHANDLER_1, ex  );
		}
		
		
		return requestedFile;
	}
	
	
	private String getRequestedFileName( XFormsDBServlet xformsdbServlet, HttpServletRequest request ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		String requestedFileName	= null;
		String requestedFilePath	= null;
		
		
		try {
			// Retrieve the requested file name
			requestedFilePath		= xformsdbServlet.getServletContext().getRealPath( request.getRequestURL().toString().substring( request.getRequestURL().toString().indexOf( request.getContextPath().substring( 1 ) ) + request.getContextPath().substring( 1 ).length() ) );
			requestedFileName		= requestedFilePath.substring( ( requestedFilePath.lastIndexOf( File.separator ) + 1 ) );
			logger.log( Level.DEBUG, "The requested file name has been retrieved." );
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_GETAPPLICATIONXWWWFORMURLENCODEDHANDLER_2, ErrorConstants.ERROR_MESSAGE_GETAPPLICATIONXWWWFORMURLENCODEDHANDLER_2, ex  );
		}
		
		
		return requestedFileName;
	}
	
	
	private void handleRequestedFile( XFormsDBServlet xformsdbServlet, HttpServletRequest request, HttpServletResponse response, File requestedFile, WebAppTO webAppTO, XFormsDBConfigTO xformsDBConfigTO ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		String requestedFilePath		= null;
		String requestedFileExtension	= null;
		String mimeType					= null;
		
		
		try {
			// Get the requested file path
			requestedFilePath			= xformsdbServlet.getServletContext().getRealPath( request.getRequestURL().toString().substring( request.getRequestURL().toString().indexOf( request.getContextPath().substring( 1 ) ) + request.getContextPath().substring( 1 ).length() ) );
			requestedFilePath			= request.getRequestURL().toString();

			if ( requestedFile != null ) {
				// Update the requested file path
				requestedFilePath		= requestedFile.getPath();
				logger.log( Level.DEBUG, "The requested resource (" + requestedFilePath + ") has been found." );
	
				// Get the requested file extension
				requestedFileExtension	= this.getRequestedFileExtension( requestedFilePath );
	
				if ( webAppTO.getMimeMappingTOs().get( requestedFileExtension ) != null ) {
					mimeType			= webAppTO.getMimeMappingTOs().get( requestedFileExtension ).getMimeType();
				}
			}
			
			super.handle( response, request, mimeType, null, xformsDBConfigTO.getEncoding(), new FileInputStream( requestedFile ), false );
			logger.log( Level.DEBUG, "The response has been created without the XFormsDB transformation." );
		} catch ( HandlerException hex ) {
			throw hex;
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_GETAPPLICATIONXWWWFORMURLENCODEDHANDLER_3, ErrorConstants.ERROR_MESSAGE_GETAPPLICATIONXWWWFORMURLENCODEDHANDLER_3, ex  );			
		}
	}

	
	private String getRequestedFileExtension( String requestedFilePath ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		String requestedFileExtension	= null;
		
		
		try {
			// Retrieve the requested file extension
			requestedFileExtension	= requestedFilePath.substring( ( requestedFilePath.lastIndexOf( "." ) + 1 ) );
			logger.log( Level.DEBUG, "The requested file extension has been retrieved." );
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_GETAPPLICATIONXWWWFORMURLENCODEDHANDLER_4, ErrorConstants.ERROR_MESSAGE_GETAPPLICATIONXWWWFORMURLENCODEDHANDLER_4, ex  );
		}
		
		
		return requestedFileExtension;
	}

	
	// PUBLIC METHODS
	public void handle( XFormsDBServlet xformsdbServlet, HttpServletRequest request, HttpServletResponse response ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		WebAppTO webAppTO					= null;
		XFormsDBConfigTO xformsDBConfigTO	= null;
		File requestedFile					= null;

		
		try {
			// Retrieve the Web application file
			webAppTO						= xformsdbServlet.getWebAppTO();
			
			// Retrieve the XFormsDB configuration file
			xformsDBConfigTO				= xformsdbServlet.getXFormsDBConfigTO();
			
			// Get the requested file
			requestedFile					= this.getRequestedFile( xformsdbServlet, request );

			// Handle the requested file
			this.handleRequestedFile( xformsdbServlet, request, response, requestedFile, webAppTO, xformsDBConfigTO );
			logger.log( Level.DEBUG, "The GET request having the content type: application/x-www-form-urlencoded has been handled." );
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_GETAPPLICATIONXWWWFORMURLENCODEDHANDLER_5, ErrorConstants.ERROR_MESSAGE_GETAPPLICATIONXWWWFORMURLENCODEDHANDLER_5, ex );
		}
	}	
}