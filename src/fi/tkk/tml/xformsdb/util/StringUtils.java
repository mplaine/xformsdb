package fi.tkk.tml.xformsdb.util;

import java.io.UnsupportedEncodingException;
import java.net.URI;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;


/**
 * Convenience class for handling string encodings and decodings.
 * 
 * 
 * @author Markku Laine
 * @version 1.0	 Created on November 14, 2009
 */
public class StringUtils {

	
	
	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger					= Logger.getLogger( StringUtils.class );
	private static final String JAVA_DEFAULT_ENCODING	= "UTF-16";
	
	
	// PRIVATE CONSTRUCTORS
	/**
	 * Prevent object construction outside of this class.
	 */
	private StringUtils() {
		logger.log( Level.DEBUG, "Constructor has been called." );
	}

	
	// PUBLIC STATIC METHODS
	/**
	 * Encode the string into a sequence of bytes using the named charset.
	 * 
	 * 
	 * @param string							The string to be encoded.
	 * 											Note: The string must always be in the UTF-16 charset.
	 * @param toCharsetName						The name of a charset to be used for encoding.
	 * 											If <code>null</code>, then the UTF-16 charset is used.
	 * @return encodedBytes						The encoded sequence of bytes of the string.
	 * @throws UnsupportedEncodingException		Encoding exception.
	 */
	public static byte[] encode( String string, String toCharsetName ) throws UnsupportedEncodingException {
		logger.log( Level.DEBUG, "Method has been called." );
		byte[] encodedBytes	= null;
		
		
		// Use the default encoding if needed
		if ( toCharsetName == null ) {
			toCharsetName	= StringUtils.JAVA_DEFAULT_ENCODING;
		}

		// Encode
		encodedBytes		= string.getBytes( toCharsetName );
		
		
		return encodedBytes;
	}
	
	
	/**
	 * Decode the sequence of bytes into a string using the named charset.
	 * 
	 * 
	 * @param bytes								The sequence of bytes to be decoded.
	 * @param fromCharsetName					The charset used for decoding.
	 * @return decodedString					The decoded string of the bytes.
	 *											NOTE: The string is always in the UTF-16 charset.
	 * @throws UnsupportedEncodingException		Encoding exception.
	 */
	public static String decode( byte[] bytes, String fromCharsetName ) throws UnsupportedEncodingException {
		logger.log( Level.DEBUG, "Method has been called." );
		String decodedString	= null;
		
		
		// Use the default encoding if needed
		if ( fromCharsetName == null ) {
			fromCharsetName		= StringUtils.JAVA_DEFAULT_ENCODING;
		}

		// Decode
		decodedString			= new String( bytes, fromCharsetName );
		
		
		return decodedString;
	}
	
	
	/**
	 * Retrieve URI as a safe ASCII string.
	 * 
	 * 
	 * @param uriString			The URI string.
	 * @return safeASCIIString	The URI string as a safe ASCII string.
	 *							NOTE: The string is always in the UTF-16 charset.
	 * @throws Exception		Encoding exception.
	 */
	public static String toSafeASCIIString( String uriString ) throws Exception {
		logger.log( Level.DEBUG, "Method has been called." );
		return StringUtils.toSafeASCIIString( uriString, false, null );
	}
	
	
	/**
	 * Retrieve URI as a safe ASCII string.
	 * 
	 * 
	 * @param uriString			The URI string.
	 * @param redirectURI		<code>true</code> if redirect URL needs to be encoded, otherwise <code>false</code>.
	 * @param response			The HTTP servlet response for encoding the URI string.
	 * @return safeASCIIString	The URI string as a safe ASCII string.
	 *							NOTE: The string is always in the UTF-16 charset.
	 * @throws Exception		Encoding exception.
	 */
	public static String toSafeASCIIString( String uriString, boolean redirectURI, HttpServletResponse response ) throws Exception {
		logger.log( Level.DEBUG, "Method has been called." );
		String safeASCIIString	= null;
		URI uri					= null;

		
		if ( ( uriString.length() >= 2 ) && ( ( "{".equals( uriString.substring( 0, 1 ) ) == true ) && ( "}".equals( uriString.substring( ( uriString.length() - 1 ), uriString.length() ) ) == true ) ) ) {
			// Contains an AVT only --> do nothing
			safeASCIIString		= uriString;
		}
		else {
			// Escape unsafe characters that are not encoded by the URI.toASCIIString() method
			uriString			= uriString.replace( " ", "%20" );  // Space
			uriString			= uriString.replace( "<", "%3C" );  // Less than symbol
			uriString			= uriString.replace( ">", "%3E" );  // Greater than symbol
			uriString			= uriString.replace( "#", "%23" );  // Pound character
			uriString			= uriString.replace( "%", "%25" );  // Percent character
			uriString			= uriString.replace( "{", "%7B" );  // Left curly brace
			uriString			= uriString.replace( "}", "%7D" );  // Right curly brace
			uriString			= uriString.replace( "|", "%7C" );  // Vertical bar/pipe
			uriString			= uriString.replace( "\\", "%5C" ); // Backslash
			uriString			= uriString.replace( "^", "%5E" );  // Caret
			uriString			= uriString.replace( "~", "%7E" );  // Tilde
			uriString			= uriString.replace( "[", "%5B" );  // Left square bracket
			uriString			= uriString.replace( "]", "%5D" );  // Right square bracket
			uriString			= uriString.replace( "`", "%60" );  // Grave accent
			
			// Retrieve the safe ASCII string
			uri					= new URI( uriString );
			safeASCIIString		= uri.toASCIIString();
			
			// Encode URI
			if ( redirectURI == false && response != null ) {
				safeASCIIString	= response.encodeURL( safeASCIIString );
			}
			// Encode redirect URI
			else if ( redirectURI == true && response != null ) {
				safeASCIIString	= response.encodeRedirectURL( safeASCIIString );
			}
		}
		
		
		return safeASCIIString;
	}
}