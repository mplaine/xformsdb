package fi.tkk.media.widgets.weather.servlet;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import fi.tkk.media.widgets.weather.handler.WeatherHandler;
import fi.tkk.tml.xformsdb.core.Constants;
import fi.tkk.tml.xformsdb.error.ErrorWriter;
import fi.tkk.tml.xformsdb.error.HandlerException;
import fi.tkk.tml.xformsdb.error.InitializationException;
import fi.tkk.tml.xformsdb.util.ExceptionUtils;


/**
 * Weather servlet.
 *
 *
 * @author Markku Laine
 * @version 1.0	 Created on November 25, 2009
 */
public class WeatherServlet extends HttpServlet {


	
	// PRIVATE STATIC FINAL VARIABLES
	private static final long serialVersionUID		= 1;
	private static final Logger logger				= Logger.getLogger( WeatherServlet.class );
	

	// PRIVATE VARIABLES
	private WeatherHandler wh						= null;
	private ErrorWriter ew							= null;
	private InitializationException iex				= null;
	private String serverURIEncoding				= null;

	
	// PRIVATE METHODS
	/**
	 * Retrieve and initialize the value of the server URI encoding.
	 * 
	 * 
	 * @return serverURIEncoding			The value of the server URI encoding.
	 * @throws InitializationException		If the application fails to retrieve the value of the server URI encoding.
	 */
	private String getAndInitServerURIEncoding() throws InitializationException {
		logger.log( Level.DEBUG, "Method has been called." );
		String serverURIEncoding	= null;
		
		
		try {
			// Retrieve the value of the server URI encoding initialization parameter defined in the web.xml file
			serverURIEncoding		= this.getServletConfig().getInitParameter( Constants.SERVLET_INIT_PARAM_SERVER_URI_ENCODING );

			// Use the default if needed
			if ( serverURIEncoding == null ) {
				serverURIEncoding	= Constants.SERVER_URI_ENCODING_DEFAULT;
			}
			logger.log( Level.DEBUG, "The server URI encoding has been successfully retrieved and initialized." );
		} catch ( Exception ex ) {
			throw new InitializationException( 1, "Failed to retrieve and initialize the value of the server URI encoding.", ex );
		}

		
		return serverURIEncoding;
	}
		
	
	// PUBLIC METHODS
	public void init() throws ServletException {
		// Load the log4j.properties file manually because the current version of Orbeon Forms prevents the log4j.properties file to be loaded automatically
		PropertyConfigurator.configure( Thread.currentThread().getContextClassLoader().getResource( "log4j.properties" ) );
		logger.log( Level.DEBUG, "Method has been called." );
		logger.log( Level.INFO, this.getServletInfo() );
		String serverURIEncoding	= null;
		
		
		try {
			// Retrieve and initialize the value of the server URI encoding
			serverURIEncoding		= this.getAndInitServerURIEncoding();

			// Set the server URI encoding
			this.serverURIEncoding	= serverURIEncoding;
		} catch ( InitializationException iex ) {
			this.iex				= iex;
			logger.log( Level.ERROR, "Error (" + this.iex.getCode() + "): " + this.iex.getMessage() );
		} catch ( Exception ex ) {
			this.iex				= new InitializationException( 2, "Failed to initialize the Weather servlet.", ex );
			logger.log( Level.FATAL, "Error (" + this.iex.getCode() + "): " + this.iex.getMessage() );
		}
		
		
		this.wh						= new WeatherHandler();
		this.ew						= new ErrorWriter();		
		logger.log( Level.INFO, "Weather servlet launched: " + this.getServletContext().getRealPath( "/" ) );
	}
	
	
	public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {		
		logger.log( Level.DEBUG, "Method has been called." );
	}
	
	
	public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {		
		logger.log( Level.DEBUG, "Method has been called." );
		long beginTime				= 0;
		long endTime				= 0;
		long time					= 0;
		
		
		try {
			beginTime				= System.currentTimeMillis();
			// Retrieve the current session or create one. Hack for enabling URL encoding for the response
			request.getSession();

			if ( this.iex == null ) {
				// Handle the request
				this.wh.handle( this, request, response );
			}
			else {
				throw this.iex;
			}
		} catch ( InitializationException iex ) {
			try {
				String encoding		= Constants.XFORMSDB_CONFIG_DEFAULT_ENCODING;
				String mimeType		= Constants.XFORMSDB_CONFIG_DEFAULT_MIME_MAPPING_MIME_TYPE;

				// Write the error
				this.ew.write( Constants.ERROR_FORMAT_XML, response, request, encoding, mimeType, this.getServerURIEncoding(), iex );
			} catch ( Exception ex ) {
				throw new ServletException( ExceptionUtils.getMessage( ex ), ExceptionUtils.getCause( ex ) );
			}
		} catch ( HandlerException hex ) {
			try {
				String encoding		= Constants.XFORMSDB_CONFIG_DEFAULT_ENCODING;
				String mimeType		= Constants.MEDIA_TYPE_APPLICATION_XML;
				
				// Write the error
				this.ew.write( Constants.ERROR_FORMAT_XML, response, request, encoding, mimeType, this.getServerURIEncoding(), hex );
			} catch ( Exception ex ) {
				throw new ServletException( ExceptionUtils.getMessage( ex ), ExceptionUtils.getCause( ex ) );
			}
		} catch ( Exception ex ) {
			throw new ServletException( ExceptionUtils.getMessage( ex ), ExceptionUtils.getCause( ex ) );
		} finally {
			endTime					= System.currentTimeMillis();
			time					= endTime - beginTime;
			if ( request.getQueryString() != null ) {
				logger.log( Level.INFO, "XFormsDB POST request took " + time + "ms: " + URLDecoder.decode( request.getRequestURL().toString() + "?" + request.getQueryString(), Constants.CLIENT_URI_ENCODING_DEFAULT ) );							
			}
			else {
				logger.log( Level.INFO, "XFormsDB POST request took " + time + "ms: " + URLDecoder.decode( request.getRequestURL().toString(), Constants.CLIENT_URI_ENCODING_DEFAULT ) );							
			}
		}
	}
	
	
	public void doPut( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {		
		logger.log( Level.DEBUG, "Method has been called." );
	}
	
	
	public void doDelete( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {		
		logger.log( Level.DEBUG, "Method has been called." );
	}
	
	
	public void destroy() {
		logger.log( Level.DEBUG, "Method has been called." );
	}
	
	
	public String getServletInfo() {
		logger.log( Level.DEBUG, "Method has been called." );
		return "Weather servlet version 1.0, Copyright (c) 2009 The XFormsDB Project";
	}
	
	
	public String getServerURIEncoding() {
		return this.serverURIEncoding;
	}
}