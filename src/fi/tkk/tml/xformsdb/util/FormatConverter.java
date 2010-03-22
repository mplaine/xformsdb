package fi.tkk.tml.xformsdb.util;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;


/**
 * Helper class for converting from one API format (SAX, a stream, or DOM)
 * to another.
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on October 08, 2009
 */
public class FormatConverter {

	
	
	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger		= Logger.getLogger( FormatConverter.class );

	
	// PRIVATE STATIC VARIABLES
	private static Transformer transformer;
	
	
	// PRIVATE CONSTRUCTORS
	/**
	 * Prevent object construction outside of this class.
	 */
	private FormatConverter() throws TransformerException {
		logger.log( Level.DEBUG, "Constructor has been called." );
	}

	
	// PRIVATE STATIC METHODS
	private static void initialize() throws TransformerException {
		TransformerFactory transformerFactory	= TransformerFactory.newInstance();
		transformer								= transformerFactory.newTransformer();
	}
	
	
	// PUBLIC STATIC METHODS
	public static void convert( Source source, Result result ) throws TransformerException {
		initialize();
		transformer.transform( source, result );
	}
}