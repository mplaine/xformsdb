package fi.tkk.tml.xformsdb.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fi.tkk.tml.xformsdb.core.Constants;


/**
 * Convenience class for handling inputs and outputs.
 * 
 * 
 * @author Markku Laine
 * @version 1.0	 Created on October 21, 2009
 */
public class IOUtils {
	
	
	
	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger					= Logger.getLogger( IOUtils.class );
	private static final String JAVA_DEFAULT_ENCODING	= "UTF-16";
	
	
	// PRIVATE CONSTRUCTORS
	/**
	 * Prevent object construction outside of this class.
	 */
	private IOUtils() {
		logger.log( Level.DEBUG, "Constructor has been called." );
	}
	

	// PUBLIC STATIC METHODS
	/**
	 * Convert the from input stream into the to output stream and retrieve the content length of the stream in bytes.
	 * 
	 * 
	 * @param fromInputStream		The input stream to convert from.
	 * @param toOutputStream		The output stream to convert to.
	 * @param closeFromInputStream	<code>true</code>, if the input stream to convert from needs to be closed,
	 * 								otherwise <code>false</code>.
	 * @param closeToOutputStream	<code>true</code>, if the output stream to convert to needs to be closed,
	 * 								otherwise <code>false</code>.
	 * @return totalBytes			The content length of the stream in bytes.
	 * @throws IOException			I/O exception.
	 */
	public static int convertAndRetrieveContentLength( InputStream fromInputStream, OutputStream toOutputStream, boolean closeFromInputStream, boolean closeToOutputStream ) throws IOException {
		logger.log( Level.DEBUG, "Method has been called." );
		int totalBytes	= 0;
		byte[] buffer	= null;		
		int bytes		= 0;

		
		try {
			buffer		= new byte[ 4 * 1024 ]; // 4K buffer
			
			
			// Read the from input stream and write the to output stream
			while ( ( bytes = fromInputStream.read( buffer ) ) != -1 ) {
				toOutputStream.write( buffer, 0, bytes );
				totalBytes	= totalBytes + bytes;
			}
		} finally {
			try {
				if ( closeFromInputStream == true ) {
					if ( fromInputStream != null ) {
						fromInputStream.close();
					}
				}
				if ( closeToOutputStream == true ) {
					if ( toOutputStream != null ) {
						toOutputStream.close();
					}
				}
			} catch ( IOException ioex ) {
				logger.log( Level.ERROR, "Failed to close the streams.", ioex );
			}
		}
		
		
		return totalBytes;
	}
	
	
	/**
	 * Convert the from input stream into the to output stream.
	 * 
	 * 
	 * @param fromInputStream		The input stream to convert from.
	 * @param toOutputStream		The output stream to convert to.
	 * @param closeFromInputStream	<code>true</code>, if the input stream to convert from needs to be closed,
	 * 								otherwise <code>false</code>.
	 * @param closeToOutputStream	<code>true</code>, if the output stream to convert to needs to be closed,
	 * 								otherwise <code>false</code>.
	 * @throws IOException			I/O exception.
	 */
	public static void convert( InputStream fromInputStream, OutputStream toOutputStream, boolean closeFromInputStream, boolean closeToOutputStream ) throws IOException {
		logger.log( Level.DEBUG, "Method has been called." );

		
		try {
			// Convert the from input stream into the to output stream
			// Convert IN --> OUT
			IOUtils.convertAndRetrieveContentLength( fromInputStream, toOutputStream, closeFromInputStream, closeToOutputStream );
		} finally {
		}
	}

	
	/**
	 * Convert the from input stream into the to string.
	 * 
	 * 
	 * @param fromInputStream		The input stream to convert from.
	 * @param fromCharsetName		The name of a charset from which the conversion is made from.
	 * 								If <code>null</code>, then the UTF-16 charset is used.
	 * @param closeFromInputStream	<code>true</code>, if the input stream to convert from needs to be closed,
	 * 								otherwise <code>false</code>.
	 * @return toString				The string to convert to.
	 * 								NOTE: The string is always in the UTF-16 charset.
	 * @throws IOException			I/O exception.
	 */
	public static String convert( InputStream fromInputStream, String fromCharsetName, boolean closeFromInputStream ) throws IOException {
		logger.log( Level.DEBUG, "Method has been called." );
		byte[] buffer							= null;		
		int bytes								= 0;
		ByteArrayOutputStream toOutputStream	= null;
		String toString							= null;

		
		try {
			toOutputStream						= new ByteArrayOutputStream();
			buffer								= new byte[ 4 * 1024 ]; // 4K buffer
			
			
			// Read the from input stream and write the to output stream
			while ( ( bytes = fromInputStream.read( buffer ) ) != -1 ) {
				toOutputStream.write( buffer, 0, bytes );
			}
			
			// Convert the buffer's contents into a string
			if ( fromCharsetName == null ) {
				toString						= new String( toOutputStream.toByteArray(), IOUtils.JAVA_DEFAULT_ENCODING );
			}
			else {
				toString						= new String( toOutputStream.toByteArray(), fromCharsetName );
			}
		} finally {
			try {
				if ( closeFromInputStream == true ) {
					if ( fromInputStream != null ) {
						fromInputStream.close();
					}
				}
				if ( toOutputStream != null ) {
					toOutputStream.close();
				}
			} catch ( IOException ioex ) {
				logger.log( Level.ERROR, "Failed to close the streams.", ioex );
			}
		}
		
		
		return toString;
	}

	
	/**
	 * Convert the from string into the to input stream.
	 * 
	 * 
	 * @param fromString				The string to convert from.
	 * 									Note: The string must always be in the UTF-16 charset.
	 * @param toCharsetName				The name of a charset from which the conversion is made to.
	 * 									If <code>null</code>, then the UTF-16 charset is used.
	 * @return toInputStream			The input stream to convert to.
	 * @throws IOException				I/O exception.
	 */
	public static InputStream convert( String fromString, String toCharsetName ) throws IOException {
		logger.log( Level.DEBUG, "Method has been called." );
		byte[] toBytes				= null;
		InputStream toInputStream	= null;

		
		// Encode this String into a sequence of bytes
		if ( toCharsetName == null ) {
			toBytes					= fromString.getBytes( IOUtils.JAVA_DEFAULT_ENCODING );					
		}
		else {
			toBytes					= fromString.getBytes( toCharsetName );
		}

		// Create the to input stream
		toInputStream				= new ByteArrayInputStream( toBytes );
		
		
		return toInputStream;
	}	

	
	/**
	 * Convert the from input stream into the to output stream.
	 * 
	 * 
	 * @param fromInputStream		The input stream to convert from.
	 * @param toOutputStream		The output stream to convert to.
	 * @param fromCharsetName		The name of a charset from which the conversion is made from.
	 * 								If <code>null</code>, then the UTF-16 charset is used.
	 * @param toCharsetName			The name of a charset from which the conversion is made to.
	 * 								If <code>null</code>, then the UTF-16 charset is used.
	 * @param closeFromInputStream	<code>true</code>, if the input stream to convert from needs to be closed,
	 * 								otherwise <code>false</code>.
	 * @param closeToOutputStream	<code>true</code>, if the output stream to convert to needs to be closed,
	 * 								otherwise <code>false</code>.
	 * @throws IOException			I/O exception.
	 */
	public static void convert( InputStream fromInputStream, OutputStream toOutputStream, String fromCharsetName, String toCharsetName, boolean closeFromInputStream, boolean closeToOutputStream ) throws IOException {
		logger.log( Level.DEBUG, "Method has been called." );
		Reader r				= null;
		BufferedReader br		= null;
		Writer w				= null;
		BufferedWriter bw		= null;
		PrintWriter pw			= null;
		boolean lineRead		= false;
		String line				= null;

		
		try {
			// Read the from input stream
			if ( fromCharsetName == null ) {
				r				= new InputStreamReader( fromInputStream, IOUtils.JAVA_DEFAULT_ENCODING );					
			}
			else {
				r				= new InputStreamReader( fromInputStream, fromCharsetName );
			}		
			br					= new BufferedReader( r );				
			
			// Write the to output stream
			if ( toCharsetName == null ) {
				w				= new OutputStreamWriter( toOutputStream, IOUtils.JAVA_DEFAULT_ENCODING );
			}
			else {
				w				= new OutputStreamWriter( toOutputStream, toCharsetName );
			}
			bw					= new BufferedWriter( w );
			pw					= new PrintWriter( bw );

			// Read the from input stream and write the to output stream
			while ( ( line = br.readLine() ) != null ) {
				// Check line
				if ( lineRead == false ) {
					// Write the line
					pw.print( line );
					lineRead	= true;
				}
				else {				
					// Write the line
					pw.print( Constants.LINE_SEPARATOR + line );
				}
			}
			pw.flush();
		} finally {
			try {
				if ( closeFromInputStream == true ) {
					if ( fromInputStream != null ) {
						fromInputStream.close();
					}
					if ( r != null ) {
						r.close();
					}
					if ( br != null ) {
						br.close();
					}
				}
				if ( closeToOutputStream == true ) {
					if ( toOutputStream != null ) {
						toOutputStream.close();
					}
					if ( w != null ) {
						w.close();
					}
					if ( bw != null ) {
						bw.close();
					}
					if ( pw != null ) {
						pw.close();
					}
				}
			} catch ( IOException ioex ) {
				logger.log( Level.ERROR, "Failed to close the streams.", ioex );
			}
		}
	}

	
	/**
	 * Convert the from output stream into the to output stream.
	 * 
	 * 
	 * @param fromOutputStream			The output stream to convert from.
	 * @param toOutputStream			The output stream to convert to.
	 * @param fromCharsetName			The name of a charset from which the conversion is made from.
	 * 									If <code>null</code>, then the UTF-16 charset is used.
	 * @param toCharsetName				The name of a charset from which the conversion is made to.
	 * 									If <code>null</code>, then the UTF-16 charset is used.
	 * @param closeFromOutputStream		<code>true</code>, if the output stream to convert from needs to be closed,
	 * 									otherwise <code>false</code>.
	 * @param closeToOutputStream		<code>true</code>, if the output stream to convert to needs to be closed,
	 * 									otherwise <code>false</code>.
	 * @throws IOException				I/O exception.
	 */
	public static void convert( ByteArrayOutputStream fromOutputStream, OutputStream toOutputStream, String fromCharsetName, String toCharsetName, boolean closeFromOutputStream, boolean closeToOutputStream ) throws IOException {
		logger.log( Level.DEBUG, "Method has been called." );
		InputStream fromInputStream	= null;

		
		try {
			// Create the from input stream
			fromInputStream			= new ByteArrayInputStream( fromOutputStream.toByteArray() );
			
			// Convert the from input stream into the to output stream
			// Convert IN --> OUT
			IOUtils.convert( fromInputStream, toOutputStream, fromCharsetName, toCharsetName, true, closeToOutputStream );
		} finally {
			try {
				if ( closeFromOutputStream == true ) {
					if ( fromOutputStream != null ) {
						fromOutputStream.close();
					}
				}
			} catch ( IOException ioex ) {
				logger.log( Level.ERROR, "Failed to close the streams.", ioex );
			}
		}
	}

	
	/**
	 * Convert the from input stream into the to input stream.
	 * 
	 * 
	 * @param fromInputStream		The input stream to convert from.
	 * @param fromCharsetName		The name of a charset from which the conversion is made from.
	 * 								If <code>null</code>, then the UTF-16 charset is used.
	 * @param toCharsetName			The name of a charset from which the conversion is made to.
	 * 								If <code>null</code>, then the UTF-16 charset is used.
	 * @return toInputStream		The input stream to convert to.
	 * @param closeFromInputStream	<code>true</code>, if the input stream to convert from needs to be closed,
	 * 								otherwise <code>false</code>.
	 * @throws IOException			I/O exception.
	 */
	public static InputStream convert( InputStream fromInputStream, String fromCharsetName, String toCharsetName, boolean closeFromInputStream ) throws IOException {
		logger.log( Level.DEBUG, "Method has been called." );
		InputStream toInputStream	= null;
		Reader r					= null;
		BufferedReader br			= null;
		ByteArrayOutputStream baos	= null;
		Writer w					= null;
		BufferedWriter bw			= null;
		PrintWriter pw				= null;
		boolean lineRead			= false;
		String line					= null;
		
		
		try {
			// Read the from input stream
			if ( fromCharsetName == null ) {
				r					= new InputStreamReader( fromInputStream, IOUtils.JAVA_DEFAULT_ENCODING );					
			}
			else {
				r					= new InputStreamReader( fromInputStream, fromCharsetName );
			}		
			br						= new BufferedReader( r );
			
			// Write ByteArrayOutputStream
			baos					= new ByteArrayOutputStream();
			if ( toCharsetName == null ) {
				w					= new OutputStreamWriter( baos, IOUtils.JAVA_DEFAULT_ENCODING );
			}
			else {
				w					= new OutputStreamWriter( baos, toCharsetName );
			}
			bw						= new BufferedWriter( w );
			pw						= new PrintWriter( bw );

			// Read the from input stream and write ByteArrayOutputStream
			while ( ( line = br.readLine() ) != null ) {
				// Check line
				if ( lineRead == false ) {
					// Write the line
					pw.print( line );
					lineRead		= true;
				}
				else {
					// Write the line
					pw.print( Constants.LINE_SEPARATOR + line );					
				}
			}
			pw.flush();

			toInputStream			= new ByteArrayInputStream( baos.toByteArray() );
		} finally {
			try {
				if ( closeFromInputStream == true ) {
					if ( fromInputStream != null ) {
						fromInputStream.close();
					}
					if ( r != null ) {
						r.close();
					}
					if ( br != null ) {
						br.close();
					}
				}
				if ( baos != null ) {
					baos.close();
				}
				if ( w != null ) {
					w.close();
				}
				if ( bw != null ) {
					bw.close();
				}
				if ( pw != null ) {
					pw.close();
				}
			} catch ( IOException ioex ) {
				logger.log( Level.ERROR, "Failed to close the streams.", ioex );
			}
		}
		
		
		return toInputStream;
	}

	
	/**
	 * Convert the from output stream into the to input stream.
	 * 
	 * 
	 * @param fromOutputStream			The output stream to convert from.
	 * @param fromCharsetName			The name of a charset from which the conversion is made from.
	 * 									If <code>null</code>, then the UTF-16 charset is used.
	 * @param toCharsetName				The name of a charset from which the conversion is made to.
	 * 									If <code>null</code>, then the UTF-16 charset is used.
	 * @return toInputStream			The input stream to convert to.
	 * @param closeFromOutputStream		<code>true</code>, if the output stream to convert from needs to be closed,
	 * 									otherwise <code>false</code>.
	 * @throws IOException				I/O exception.
	 */
	public static InputStream convert( ByteArrayOutputStream fromOutputStream, String fromCharsetName, String toCharsetName, boolean closeFromOutputStream ) throws IOException {
		logger.log( Level.DEBUG, "Method has been called." );
		InputStream toInputStream	= null;
		InputStream fromInputStream	= null;

		
		try {
			// Create the from input stream
			fromInputStream			= new ByteArrayInputStream( fromOutputStream.toByteArray() );
			
			// Convert the from input stream into the to input stream
			// Convert IN --> IN
			toInputStream			= IOUtils.convert( fromInputStream, fromCharsetName, toCharsetName, true );
		} finally {
			try {
				if ( closeFromOutputStream == true ) {
					if ( fromOutputStream != null ) {
						fromOutputStream.close();
					}
				}
			} catch ( IOException ioex ) {
				logger.log( Level.ERROR, "Failed to close the streams.", ioex );
			}
		}
		
		
		return toInputStream;
	}	
}