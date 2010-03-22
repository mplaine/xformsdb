package fi.tkk.media.widgets.weather.handler;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nu.xom.Builder;
import nu.xom.Document;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fi.tkk.media.widgets.weather.servlet.WeatherServlet;
import fi.tkk.tml.xformsdb.core.Constants;
import fi.tkk.tml.xformsdb.error.HandlerException;
import fi.tkk.tml.xformsdb.error.XMLException;
import fi.tkk.tml.xformsdb.handler.ResponseHandler;
import fi.tkk.tml.xformsdb.util.IOUtils;
import fi.tkk.tml.xformsdb.util.StringUtils;
import fi.tkk.tml.xformsdb.util.XMLUtils;


/**
 * Handle weather requests.
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on November 25, 2009
 */
public class WeatherHandler extends ResponseHandler {
	
	
	
	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger			= Logger.getLogger( WeatherHandler.class );

	
	// PUBLIC CONSTURCTORS
	public WeatherHandler() {
		super();
		logger.log( Level.DEBUG, "Constructor has been called." );
	}
	
	
	// PRIVATE METHODS

	
	// PUBLIC METHODS
	public void handle( WeatherServlet weatherServlet, HttpServletRequest request, HttpServletResponse response ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		Builder builder					= null;
		Document document				= null;
		String weather					= null;
		String hl						= null;
		String uriString				= null;
		HttpClient httpClient			= null;
		HttpGet httpGet					= null;
		HttpResponse httpResponse		= null;
		HttpEntity httpEntity			= null;
		StatusLine statusLine			= null;
		String xmlString				= null;
		String xmlStringFiltered		= null;

		
		try {
			// Parse the input stream (XML document)
			builder						= new Builder();
			document					= builder.build( request.getInputStream() );
			
			// Retrieve city and language
			weather						= document.getRootElement().getFirstChildElement( "city" ).getValue(); // Retrieve city
			hl							= document.getRootElement().getFirstChildElement( "language" ).getValue(); // Retrieve language
			
			// Update the URI string i.e. set the weather and hl parameter values
			uriString					= "http://www.google.com/ig/api?weather={weather}&hl={hl}";
			uriString					= uriString.replace( "{weather}", weather );
			uriString					= uriString.replace( "{hl}", hl );
			
			// Create an instance of HttpClient
			httpClient					= new DefaultHttpClient();
		    
		    // Create an instance of HttpGet
		    httpGet						= new HttpGet( StringUtils.toSafeASCIIString( uriString ) ); // Important for encoding the URI correctly!
		    
		    // Execute the HTTP request
			logger.log( Level.DEBUG, "Executing HTTP request: " + httpGet.getURI() );
	        httpResponse				= httpClient.execute( httpGet );
	
	        // Retrieve the HTTP response entity
			httpEntity					= httpResponse.getEntity();

			if ( httpEntity != null ) {
				// Retrieve the HTTP response status line
				statusLine				= httpResponse.getStatusLine();
	
				if ( statusLine.getStatusCode() == HttpServletResponse.SC_OK ) {					
    				xmlString			= EntityUtils.toString( httpEntity );
				}    			
			}
			
	        // Ensure that the result does not consist only of the XML declaration
			try {
				xmlStringFiltered		= XMLUtils.filterXMLDeclaration( xmlString );
			} catch ( XMLException xmlex ) {
				xmlStringFiltered		= null;
			} catch ( Exception ex ) {
				xmlStringFiltered		= null;
			}
			
			// Select files query did not result any data
			if ( xmlString == null || xmlStringFiltered.length() == 0 ) {
				throw new HandlerException( 3, "The weather query did not return any data." );
			}

			
			// Write the response
			super.handle( response, request, Constants.MIME_TYPE_APPLICATION_XML, Constants.XFORMSDB_CONFIG_DEFAULT_ENCODING, Constants.XFORMSDB_CONFIG_DEFAULT_ENCODING, IOUtils.convert( xmlString, Constants.XFORMSDB_CONFIG_DEFAULT_ENCODING ), true );
			logger.log( Level.DEBUG, "Result of the weather query:" + Constants.LINE_SEPARATOR + xmlString );
		} catch ( ClientProtocolException cpex ) {
			throw new HandlerException( 4, "Failed to execute the REST method.", cpex );
		} catch ( IOException ioex ) {
			throw new HandlerException( 5, "Failed to execute the REST method.", ioex );			
		} catch ( HandlerException hex ) {
			throw hex;
		} catch ( Exception ex ) {
			throw new HandlerException( 6, "Failed to handle the weather request.", ex );
		} finally {
			if ( httpClient != null ) {
		        // When HttpClient instance is no longer needed, 
		        // shut down the connection manager to ensure
		        // immediate deallocation of all system resources
		        httpClient.getConnectionManager().shutdown();			
			}
		}
	}	
}