package fi.tkk.tml.xformsdb.xml.sax;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.DefaultHandler2;


/**
 * Provide common functionalities for other SAX2 event handlers.
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on March 23, 2007
 */
public class BaseHandler extends DefaultHandler2 {
	
	
	
	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger	= Logger.getLogger( BaseHandler.class );

	
	// CONSTRUCTORS
	/**
	 * Default constructor.
	 */
	public BaseHandler() {
		super();
		logger.log( Level.DEBUG, "Constructor has been called." );
	}
	
	
	// PUBLIC METHODS
	// SAX2 ErrorHandler method
	public void error( SAXParseException spex ) throws SAXParseException {
		logger.log( Level.DEBUG, "Method has been called." );
		// Recoverable (validation) errors
		// Treat validation errors as fatal errors
		throw new SAXParseErrorException( spex.getMessage(), spex.getPublicId(), spex.getSystemId(), spex.getLineNumber(), spex.getColumnNumber(), spex );
	}
	// SAX2 ErrorHandler method
	public void fatalError( SAXParseException spex ) throws SAXParseException {
		logger.log( Level.DEBUG, "Method has been called." );
		// Non-recoverable (well-formedness) errors
		throw new SAXParseFatalErrorException( spex.getMessage(), spex.getPublicId(), spex.getSystemId(), spex.getLineNumber(), spex.getColumnNumber(), spex );
	}
	// SAX2 ErrorHandler method
	public void warning( SAXParseException spex ) throws SAXParseWarningException {
		logger.log( Level.DEBUG, "Method has been called." );
		// Recoverable warnings
		// Either ignore the warning (print it) and continue parsing the XML file or throw an exception
		logger.log( Level.ERROR, "SAX Parse Warning exception" + ", line " + spex.getLineNumber() + ", column " + spex.getColumnNumber() + ", uri " + spex.getSystemId() + ". " + spex.getMessage(), spex );
		//throw new SAXParseWarningException( spex.getMessage(), spex.getPublicId(), spex.getSystemId(), spex.getLineNumber(), spex.getColumnNumber(), spex );
	}
}