package fi.tkk.tml.xformsdb.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import fi.tkk.tml.xformsdb.core.Constants;
import fi.tkk.tml.xformsdb.error.XMLException;


/**
 * Convenience class for handling XML documents.
 * 
 * Note: The strings must always be in the UTF-16 charset.
 * 
 * 
 * @author Markku Laine
 * @version 1.0	 Created on October 22, 2009
 */
public class XMLUtils {
	
	
	
	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger					= Logger.getLogger( XMLUtils.class );

	
	// PRIVATE CONSTRUCTORS
	/**
	 * Prevent object construction outside of this class.
	 */
	private XMLUtils() {
		logger.log( Level.DEBUG, "Constructor has been called." );
	}
	

	// PUBLIC STATIC METHODS
	/**
	 * Retrieve the <code>Node</code> as a string.
	 * The result string uses the given encoding in the XML declaration.
	 * 
	 * 
	 * @param node				The node.
	 * 							NOTE: The node must always be in the format specified by the XML declaration.
	 * @param encoding			The encoding to be used in the XML declaration.
	 *							If <code>null</code>, then the UTF-8 encoding is used in the XML declaration.
	 * @return xmlString		NOTE: The string is always in the UTF-16 charset.
	 * @throws XMLException		XML exception.
	 */
	public static String getNodeAsString( Node node, String encoding ) throws XMLException {
		logger.log( Level.DEBUG, "Method has been called." );
		String xmlEncoding			= null;
		TransformerFactory tf		= null;
		Transformer t				= null;
		ByteArrayOutputStream baos	= null;
		Result result				= null;
		Source source				= null;
		String xmlString			= null;
		
		
		if ( node != null ) {
			logger.log( Level.DEBUG, "Node is okay." );

			// Use the default encoding if needed
			if ( encoding == null ) {
				encoding			= Constants.XML_DEFAULT_ENCODING;
			}

			try {
				// Set up a transformer
				tf					= TransformerFactory.newInstance();
				t					= tf.newTransformer();
				//t.setOutputProperty( OutputKeys.OMIT_XML_DECLARATION, Constants.OUTPUT_XML_OMIT_XML_DECLARATION_NO );
				t.setOutputProperty( OutputKeys.METHOD, Constants.OUTPUT_XML_METHOD_XML );
				t.setOutputProperty( OutputKeys.VERSION, Constants.OUTPUT_XML_VERSION_1_0 );
				t.setOutputProperty( OutputKeys.ENCODING, xmlEncoding );
				t.setOutputProperty( OutputKeys.INDENT, Constants.OUTPUT_XML_INDENT_YES );
				//t.setOutputProperty( OutputKeys.STANDALONE, "no" );
				//t.setOutputProperty( OutputKeys.MEDIA_TYPE, "text/xml" );
	
				// Prepare source and result
				source				= new DOMSource( node ); // Encoding detected automatically based on the XML declaration, defaults to UTF-8
				baos				= new ByteArrayOutputStream();
				result				= new StreamResult( baos );
	
				// Transform
				t.transform( source, result );

				// Convert the buffer's contents into a string
				xmlString			= new String( baos.toByteArray(), encoding );
				logger.log( Level.DEBUG, "XML string has been successfully created." );
			} catch ( Exception ex ) {
				throw new XMLException( "Failed to get the node as a string. " + ex.getMessage(), ex );
			} finally {
				try {
					if ( baos != null ) {
						baos.close();
					}
				} catch ( IOException ioex ) {
					logger.log( Level.ERROR, "Failed to close the streams.", ioex );					
				}
			}
		}
		else {
			throw new XMLException( "Node is not okay." );
		}
		
		
		return xmlString;
	}


	/**
	 * Retrieve the <code>InputStream</code> as a string.
	 * The result string uses the given encoding in the XML declaration.
	 * 
	 * 
	 * @param inputStream		The input stream.
	 * 							NOTE: The input stream must always be in the format specified by the XML declaration.
	 * @param encoding			The encoding to be used in the XML declaration.
	 * 							If <code>null</code>, then the UTF-8 encoding is used in the XML declaration.
	 * @return xmlString		The input stream as a string.
	 * 							NOTE: The string is always in the UTF-16 charset.
	 * @throws XMLException		XML exception.
	 */
	public static String getInputStreamAsString( InputStream inputStream, String encoding ) throws XMLException {
		logger.log( Level.DEBUG, "Method has been called." );
		return XMLUtils.getInputStreamAsString( inputStream, null, encoding );
	}
	

	/**
	 * Retrieve the <code>InputStream</code> as a string.
	 * The result string uses the given encoding in the XML declaration.
	 * 
	 * 
	 * @param inputStream			The input stream.
	 * @param inputStreamEncoding	The encoding of the input stream.
	 * 								If <code>null</code>, then the encoding is automatically detected based on the XML declaration, defaults to UTF-8.
	 * @param encoding				The encoding to be used in the XML declaration.
	 * 								If <code>null</code>, then the UTF-8 encoding is used in the XML declaration.
	 * @return xmlString			The input stream as a string.
	 * 								NOTE: The string is always in the UTF-16 charset.
	 * @throws XMLException			XML exception.
	 */
	public static String getInputStreamAsString( InputStream inputStream, String inputStreamEncoding, String encoding ) throws XMLException {
		logger.log( Level.DEBUG, "Method has been called." );
		TransformerFactory tf		= null;
		Transformer t				= null;
		InputSource inputSource		= null;
		DocumentBuilderFactory dbf	= null;
		DocumentBuilder db			= null;
		Document d					= null;
		ByteArrayOutputStream baos	= null;
		Result result				= null;
		Source source				= null;
		String xmlString			= null;
		
		
		if ( inputStream != null ) {
			logger.log( Level.DEBUG, "Input stream is okay." );

			// Use the default encoding if needed
			if ( encoding == null ) {
				encoding			= Constants.XML_DEFAULT_ENCODING;
			}

			try {
				// Set up a transformer
				tf					= TransformerFactory.newInstance();
				t					= tf.newTransformer();
				//t.setOutputProperty( OutputKeys.OMIT_XML_DECLARATION, Constants.OUTPUT_XML_OMIT_XML_DECLARATION_NO );
				t.setOutputProperty( OutputKeys.METHOD, Constants.OUTPUT_XML_METHOD_XML );
				t.setOutputProperty( OutputKeys.VERSION, Constants.OUTPUT_XML_VERSION_1_0 );
				t.setOutputProperty( OutputKeys.ENCODING, encoding );
				t.setOutputProperty( OutputKeys.INDENT, Constants.OUTPUT_XML_INDENT_YES );
				//t.setOutputProperty( OutputKeys.STANDALONE, "no" );
				//t.setOutputProperty( OutputKeys.MEDIA_TYPE, "text/xml" );
	
				// Prepare source and result
				if ( inputStreamEncoding == null ) {
					source			= new StreamSource( inputStream ); // Encoding detected automatically based on the XML declaration, defaults to UTF-8					
				}
				else {
					inputSource		= new InputSource();
					inputSource.setByteStream( inputStream );
					inputSource.setEncoding( inputStreamEncoding );
					dbf				= DocumentBuilderFactory.newInstance();
					db				= dbf.newDocumentBuilder();
					d				= db.parse( inputSource );
					source			= new DOMSource( d );					
				}
				baos				= new ByteArrayOutputStream();
				result				= new StreamResult( baos );
	
				// Transform
				t.transform( source, result );
				
				// Convert the buffer's contents into a string
				xmlString			= baos.toString( encoding );
				logger.log( Level.DEBUG, "XML string has been successfully created." );
			} catch ( Exception ex ) {
				throw new XMLException( "Failed to get the input stream as a string. " + ex.getMessage(), ex );
			} finally {
				try {
					if ( inputStream != null ) {
						inputStream.close();
					}
					if ( baos != null ) {
						baos.close();
					}
				} catch ( IOException ioex ) {
					logger.log( Level.ERROR, "Failed to close the streams.", ioex );
				}
			}
		}
		else {
			throw new XMLException( "Input stream is not okay." );
		}
		
		
		return xmlString;
	}
	
	
	/**
	 * Filter the XML declaration.
	 * 
	 * 
	 * @param xmlString				The XML as a string.
	 * @return filteredXMLString	The filtered XML as a string.
	 * @throws XMLException			XML exception.
	 */
	public static String filterXMLDeclaration( String xmlString ) throws XMLException {
		logger.log( Level.DEBUG, "Method has been called." );
		String filteredXMLString		= null;
		String xmlDeclaration			= "<?xml ";
		
		
		if ( xmlString != null ) {
			logger.log( Level.DEBUG, "XML is okay." );
			
			// Start with XML declaration
			if ( xmlString.startsWith( xmlDeclaration, 0 ) ) {
				// Find the start tag of next element
				int indexOfNextElement	= xmlString.indexOf( "<", xmlDeclaration.length() );
				if ( indexOfNextElement != -1 ) {
					filteredXMLString	= xmlString.substring( indexOfNextElement );
				}
				else {
					throw new XMLException( "XML is not okay." );					
				}
			}
			// Original XML string
			else {
				filteredXMLString		= xmlString;
			}
		}
		else {
			throw new XMLException( "XML is not okay." );
		}
		
		
		return filteredXMLString;
	}
	
	
	/**
	 * Indent the XML file given as an <code>InputStream</code>.
	 * The result string uses the given encoding in the XML declaration.
	 * 
	 * 
	 * @param indentTransformer		The transformer containing the indent XSL file.
	 * @param xmlString				The XML file as a string.
	 * 								NOTE: The string must always be in the UTF-16 charset.
	 * @param encoding				The encoding to be used in the XML declaration.
	 * 								If <code>null</code>, then the UTF-8 encoding is used in the XML declaration.
	 * @return xmlString			The indented XML file as a string.
	 * 								NOTE: The string is always in the UTF-16 charset.
	 * @throws XMLException			XML exception.
	 */
	public static String indent( Transformer indentTransformer, String xmlString, String encoding ) throws XMLException {
		logger.log( Level.DEBUG, "Method has been called." );
		Source source				= null;
		ByteArrayOutputStream baos	= null;
		Result result				= null;
		String xmlStringIndented	= null;
		
		
		if ( xmlString != null ) {
			logger.log( Level.DEBUG, "XML string is okay." );

			// Use the default encoding if needed
			if ( encoding == null ) {
				encoding			= Constants.XML_DEFAULT_ENCODING;
			}

			try {
				// Configure the transformer (the XSL file includes own settings, too)
				//indentTransformer.setOutputProperty( OutputKeys.OMIT_XML_DECLARATION, Constants.OUTPUT_XML_OMIT_XML_DECLARATION_NO );
				indentTransformer.setOutputProperty( OutputKeys.METHOD, Constants.OUTPUT_XML_METHOD_XML );
				indentTransformer.setOutputProperty( OutputKeys.VERSION, Constants.OUTPUT_XML_VERSION_1_0 );
				indentTransformer.setOutputProperty( OutputKeys.ENCODING, encoding );
				indentTransformer.setOutputProperty( OutputKeys.INDENT, Constants.OUTPUT_XML_INDENT_YES );
				//indentTransformer.setOutputProperty( OutputKeys.STANDALONE, "no" );
				//indentTransformer.setOutputProperty( OutputKeys.MEDIA_TYPE, "text/xml" );
	
				// Prepare source and result
				source				= new StreamSource( IOUtils.convert( xmlString, encoding ) ); // Encoding detected automatically based on the XML declaration, defaults to UTF-8
				baos				= new ByteArrayOutputStream();
				result				= new StreamResult( baos );
	
				// Transform
				indentTransformer.transform( source, result );
				
				// Convert the buffer's contents into a string
				xmlStringIndented	= baos.toString( encoding );
				logger.log( Level.DEBUG, "XML file has been successfully indented." );
			} catch ( Exception ex ) {
				throw new XMLException( "Failed to indent the XML file. " + ex.getMessage(), ex );
			} finally {
				try {
					if ( baos != null ) {
						baos.close();
					}
				} catch ( IOException ioex ) {
					logger.log( Level.ERROR, "Failed to close the stream.", ioex );
				}
			}
		}
		else {
			throw new XMLException( "XML string is not okay." );
		}
		
		
		return xmlStringIndented;
	}
}