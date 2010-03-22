package fi.tkk.tml.xformsdb.xml.dom;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import fi.tkk.tml.xformsdb.error.ErrorConstants;
import fi.tkk.tml.xformsdb.error.HandlerException;
import fi.tkk.tml.xformsdb.xml.sax.BaseHandler;
import fi.tkk.tml.xformsdb.xml.sax.SAXParseErrorException;
import fi.tkk.tml.xformsdb.xml.sax.SAXParseFatalErrorException;
import fi.tkk.tml.xformsdb.xml.sax.SAXParseWarningException;


/**
 * Handle the collection XML file.
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on October 15, 2009
 */
public class CollectionHandler {
	
	
	
	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger						= Logger.getLogger( CollectionHandler.class );

	
	// PRIVATE VARIABLES
	private List<String> collection;
	
	
	// CONSTRUCTORS
	public CollectionHandler() {
		logger.log( Level.DEBUG, "Constructor has been called." );
		this.collection										= null;
	}

	
	// PUBLIC METHODS
	public void handle( InputStream inputStream ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );

		
		try {
			// Parse the input stream
			DocumentBuilderFactory dbf						= DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware( true );
			dbf.setValidating( false ); // No DTD/XML Schema has been yet assigned
			DocumentBuilder db								= dbf.newDocumentBuilder();
			db.setErrorHandler( new BaseHandler() );
			InputSource inputSource							= new InputSource();
			//inputSource.setEncoding( encoding ); // Encoding detected automatically based on the XML declaration
			inputSource.setByteStream( inputStream );
			Document d										= db.parse( inputSource );

			
			// Retrieve documents' file names from the collection
			NodeList docNodeList							= d.getDocumentElement().getElementsByTagName( "doc" );
			Node docNode									= null;
			NamedNodeMap docAttributes						= null;
			Node docHrefNode								= null;
			this.collection									= new ArrayList<String>();
			for ( int i = 0; i < docNodeList.getLength(); i++ ) {
				docNode										= docNodeList.item( i );
				docAttributes								= docNode.getAttributes();
				docHrefNode									= docAttributes.getNamedItem( "href" );
				this.collection.add( docHrefNode.getNodeValue().trim() );
			}
			logger.log( Level.DEBUG, "The collection XML file has been successfully handled." );
		} catch ( ParserConfigurationException pcex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_DOMPARSER_1, pcex.getMessage(), pcex );
		} catch ( SAXParseWarningException saxpwex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_SAXPARSER_1, "SAX parse warning exception" + ", line " + saxpwex.getLineNumber() + ", column " + saxpwex.getColumnNumber() + ", uri " + saxpwex.getSystemId() + ". " + saxpwex.getMessage(), saxpwex );
		} catch ( SAXParseErrorException saxpeex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_SAXPARSER_2, "SAX parse error exception" + ", line " + saxpeex.getLineNumber() + ", column " + saxpeex.getColumnNumber() + ", uri " + saxpeex.getSystemId() + ". " + saxpeex.getMessage(), saxpeex );
		} catch ( SAXParseFatalErrorException saxpfeex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_SAXPARSER_3, "SAX parse fatal error exception" + ", line " + saxpfeex.getLineNumber() + ", column " + saxpfeex.getColumnNumber() + ", uri " + saxpfeex.getSystemId() + ". " + saxpfeex.getMessage(), saxpfeex );
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_DOMPARSER_2, ErrorConstants.ERROR_MESSAGE_DOMPARSER_2, ex );
		} finally {
			try {
				if ( inputStream != null ) {
					inputStream.close();
				}
			} catch ( IOException ioex ) {
				logger.log( Level.ERROR, "Failed to close the streams.", ioex );
			}			
		}		
	}

	
	public List<String> getCollection() {
		logger.log( Level.DEBUG, "Method has been called." );
		return this.collection;
	}
}