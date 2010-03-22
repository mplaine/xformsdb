package fi.tkk.tml.xformsdb.xml.sax;

import java.io.CharArrayWriter;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Stack;

import javax.xml.XMLConstants;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.ext.Locator2;

import fi.tkk.tml.xformsdb.core.Constants;
import fi.tkk.tml.xformsdb.xml.ns.NamespaceContextImpl;
import fi.tkk.tml.xformsdb.xml.to.MimeMappingTO;
import fi.tkk.tml.xformsdb.xml.to.WebAppTO;


/**
 * Handle SAX2 events of the Web application XML file (web.xml).
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on October 21, 2009
 */
public class WebAppHandler extends BaseHandler implements LexicalHandler {
	
	
	
	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger								= Logger.getLogger( WebAppHandler.class );

	
	// PUBLIC STATIC FINAL VARIABLES
	// FEATURE IDS
	// Namespaces feature id (http://xml.org/sax/features/namespaces)
	public static final String NAMESPACES_FEATURE_ID				= "http://xml.org/sax/features/namespaces";	
	// Namespace prefixes feature id (http://xml.org/sax/features/namespace-prefixes)
	public static final String NAMESPACE_PREFIXES_FEATURE_ID		= "http://xml.org/sax/features/namespace-prefixes";	
		
	// PROPERTY IDS
	// Lexical handler property id (http://xml.org/sax/properties/lexical-handler)
	public static final String LEXICAL_HANDLER_PROPERTY_ID			= "http://xml.org/sax/properties/lexical-handler";

	// DEFAULT SETTINGS
	public static final String DEFAULT_XML_DECL_VERSION				= Constants.OUTPUT_XML_VERSION_1_0;
	public static final String DEFAULT_XML_DECL_ENCODING			= Constants.XFORMSDB_CONFIG_DEFAULT_ENCODING;


	// PRIVATE VARIABLES
	// A stack to keep track of the element names
	// that are currently open ( have called
	// "startElement", but not "endElement".)
	private Stack<String> elementStack;
	// XML Namespace contexts
	private NamespaceContextImpl nsContexts;
    // Document locator
	private Locator locator;
	// Writer
//	private Writer writer;
	// Writer's buffer
//	private OutputStream writerBuffer;
	// XML Declaration version
	private String xmlDeclVersion;
	// XML Declaration encoding
	private String xmlDeclEncoding;
	// Encoding
//	private String encoding;
	// Buffer for collecting data from
	// the "characters" SAX event.
	private CharArrayWriter	characters;
	// In DTD section
	private boolean inDTD;
	// In processing instruction
	private boolean inProcessingInstruction;
	// In comment section
	private boolean inComment;
    // In CDATA section
	private boolean inCDATA;
	// Contents
//	private String contents;
	// Web application file related variables
	private WebAppTO webAppTO;
	private MimeMappingTO mimeMappingTOTemp;


	// CONSTRUCTORS
	/**
	 * Default constructor.
	 */
	public WebAppHandler( /*String encoding*/ ) throws UnsupportedEncodingException {
		super();
		logger.log( Level.DEBUG, "Constructor has been called." );
		this.elementStack				= new Stack<String>();
		this.nsContexts					= new NamespaceContextImpl();
		this.locator					= null;
//		this.writerBuffer				= new ByteArrayOutputStream();
		this.xmlDeclVersion				= WebAppHandler.DEFAULT_XML_DECL_VERSION;
		this.xmlDeclEncoding			= WebAppHandler.DEFAULT_XML_DECL_ENCODING;
//		this.encoding					= WebAppHandler.DEFAULT_ENCODING;
//		this.setOutput( this.writerBuffer, encoding );
		this.characters					= new CharArrayWriter();
		this.inDTD						= false;
		this.inProcessingInstruction	= false;
		this.inComment					= false;
		this.inCDATA					= false;
//		this.contents					= null;
		this.webAppTO					= null;
		this.mimeMappingTOTemp			= null;
	}
	
	
	// PRIVATE METHODS
	// Build the path string from the current state
	// of the stack... Very inefficient
	private String getElementPath() {
		logger.log( Level.DEBUG, "Method has been called." );
		// Build the path string...
		StringBuffer sb	= new StringBuffer( "" );
		Enumeration e	= this.elementStack.elements();
		
		while ( e.hasMoreElements() ) {
			sb.append( "/" + ( String ) e.nextElement() );
		}
		
		
		return sb.toString();
	}
	
	
	/**
	 * Set the buffer for writing.
	 *
	 *
	 * @param buffer		The buffer for writing.
	 * @param encoding		The encoding.
	 */
//	private void setOutput( OutputStream buffer, String encoding ) throws UnsupportedEncodingException {
//		if ( encoding != null ) {
//			this.encoding	= encoding;
//		}
//		this.writer			= new OutputStreamWriter( buffer, this.encoding );
//	}

	
	private void write( String str ) throws SAXException {
		logger.log( Level.DEBUG, "Method has been called." );
//	    try {
//			this.writer.write( str );
//			this.writer.flush();			
//		} catch ( IOException ioex ) {
//			throw new SAXException( "Failed to write the string.", ioex );
//		}
	}

	
	private void writeEscape( String str ) throws SAXException {
		logger.log( Level.DEBUG, "Method has been called." );
//	    try {
//			// Do not escape characters inside the CDATA section
//	    	if ( this.inCDATA == true ) {
//	    		this.write( str );
//	    	}
//	    	else {
//	    		this.writer.write( StringEscapeUtils.escapeXml( str, this.xmlDeclVersion, false ) );
//				this.writer.flush();
//	    	}
//		} catch ( IOException ioex ) {
//			throw new SAXException( "Failed to write the escape string.", ioex );
//		}
	}

	
	private void writeEscapeAttributeValue( String str ) throws SAXException {
		logger.log( Level.DEBUG, "Method has been called." );
//	    try {
//			this.writer.write( StringEscapeUtils.escapeXml( str, this.xmlDeclVersion, true ) );
//			this.writer.flush();
//		} catch ( IOException ioex ) {
//			throw new SAXException( "Failed to write the escape attribute value string.", ioex );
//		}
	}

	
	private void resetCharacters() {
		logger.log( Level.DEBUG, "Method has been called." );
		// Reset characters
		this.characters.reset();
	}

	
	private String getAndResetCharacters() {
		logger.log( Level.DEBUG, "Method has been called." );
		// Retrieve characters
		String characters	= this.characters.toString();
		// Reset characters
		this.resetCharacters();
		
		
		return characters;
	}


	// PUBLIC METHODS
	// SAX2 ContentHandler method
	public void characters( char[] ch, int start, int length ) throws SAXException {
		logger.log( Level.DEBUG, "Method has been called." );
		this.characters.write( ch, start, length );
	}
	// SAX2 ContentHandler method
	public void endDocument() throws SAXException {
		logger.log( Level.DEBUG, "Method has been called." );
		// Write characters accumulated after last event
		this.writeEscape( this.getAndResetCharacters() );
	}
	// SAX2 ContentHandler method
	public void endElement( String uri, String localName, String qName ) throws SAXException {
		logger.log( Level.DEBUG, "Method has been called." );
		// Element name
		String eName		= null;
		// namespaceAware = false, i.e. does not use Namespaces
		if ( "".equals( localName ) ) {
			eName			= qName;
		}
		// namespaceAware = true, i.e. use Namespaces
		else {
			String ePrefix	= this.nsContexts.getPrefix( uri );
			// Namespace URI was not found from XML Namespace contexts
			if ( ePrefix == null ) {
				eName		= qName;
			}
			// Namespace URI was found from XML Namespace contexts
			else {
				if ( XMLConstants.DEFAULT_NS_PREFIX.equals( ePrefix ) == true ) {
					eName	= localName;
				}
				else {
					eName	= ePrefix + ":" + localName;
				}
			}
		}

		
		String characters	= this.getAndResetCharacters();
		// Write characters accumulated after last event
		this.writeEscape( characters );
		
		// Set values
		if ( this.getElementPath().equals( "/" + WebAppTO.LOCAL_NAME + "/welcome-file-list/welcome-file" ) == true ) {
			// Do not escape
			this.webAppTO.getWelcomeFiles().add( characters.trim() );
		}
		else if ( this.getElementPath().equals( "/web-app/mime-mapping" ) == true ) {
			this.webAppTO.getMimeMappingTOs().put( this.mimeMappingTOTemp.getExtension(), this.mimeMappingTOTemp );
		}
		else if ( this.getElementPath().equals( "/" + WebAppTO.LOCAL_NAME + "/" + MimeMappingTO.LOCAL_NAME + "/extension" ) == true ) {
			// Do not escape
			this.mimeMappingTOTemp.setExtension( characters.trim() );
		}
		else if ( this.getElementPath().equals( "/" + WebAppTO.LOCAL_NAME + "/" + MimeMappingTO.LOCAL_NAME + "/mime-type" ) == true ) {
			// Do not escape
			this.mimeMappingTOTemp.setMimeType( characters.trim() );
		}


		this.write( "</" );
		this.write( eName );
		this.write( ">" );
		

		// Clean up the stack...
		this.elementStack.pop();
	}
	// SAX2 ContentHandler method
	public void endPrefixMapping( String prefix ) throws SAXException {
		logger.log( Level.DEBUG, "Method has been called." );
	}
	// SAX2 ContentHandler method
	public void ignorableWhitespace( char[] ch, int start, int length ) throws SAXException {
		logger.log( Level.DEBUG, "Method has been called." );
		this.characters( ch, start, length );
	}
	// SAX2 ContentHandler method
	public void processingInstruction( String target, String data ) throws SAXException {
		logger.log( Level.DEBUG, "Method has been called." );
		// Write characters accumulated after last event
		this.writeEscape( this.getAndResetCharacters() );
		this.inProcessingInstruction	= true;
		this.write( "<?" );
		this.write( target );
			
		if ( data != null && data.length() > 0 ) {
			this.write( " " );
			this.write( data );
		}
		this.write( "?>" );
		this.writeEscape( Constants.LINE_SEPARATOR );
		this.inProcessingInstruction	= false;
	}
	// SAX2 ContentHandler method
	public void setDocumentLocator( Locator locator ) {
		logger.log( Level.DEBUG, "Method has been called." );
		this.locator	= locator;
	}
	// SAX2 ContentHandler method
	public void skippedEntity( String name ) throws SAXException {
		logger.log( Level.DEBUG, "Method has been called." );
	}
	// SAX2 ContentHandler method
	public void startDocument() throws SAXException {
		logger.log( Level.DEBUG, "Method has been called." );
	}
	// SAX2 ContentHandler method
	public void startElement( String uri, String localName, String qName, Attributes atts ) throws SAXException {
		logger.log( Level.DEBUG, "Method has been called." );
		// XML Declaration values given in the startDocument() method cannot be trusted.
		// Simulate the start of document.
		if ( "".equals( this.getElementPath() ) == true ) {
			if ( this.locator != null ) {
				if ( this.locator instanceof Locator2 ) {
					this.xmlDeclVersion		= ( ( Locator2 ) this.locator ).getXMLVersion();
					this.xmlDeclEncoding	= ( ( Locator2 ) this.locator ).getEncoding();
				}
				this.locator				= null;
			}
			this.write( "<?xml version=\"" + this.xmlDeclVersion + "\" encoding=\"" + this.xmlDeclEncoding + "\"?>" );
			this.writeEscape( Constants.LINE_SEPARATOR );
		}
		
		
		// Element name
		String eName						= null;
		// namespaceAware = false, i.e. does not use Namespaces
		if ( "".equals( localName ) ) {
			eName							= qName;
		}
		// namespaceAware = true, i.e. use Namespaces
		else {
			String ePrefix					= this.nsContexts.getPrefix( uri );
			// Namespace URI was not found from XML Namespace contexts
			if ( ePrefix == null ) {
				eName						= qName;
			}
			// Namespace URI was found from XML Namespace contexts
			else {
				if ( XMLConstants.DEFAULT_NS_PREFIX.equals( ePrefix ) == true ) {
					eName					= localName;
				}
				else {
					eName					= ePrefix + ":" + localName;
				}
			}
		}
		// Push the element name onto the tag stack...
		this.elementStack.push( eName );

		
		// Write characters accumulated after last event
		this.writeEscape( this.getAndResetCharacters() );
		
		// Initialize variables
		if ( this.getElementPath().equals( "/" + WebAppTO.LOCAL_NAME ) == true ) {
			this.webAppTO					= new WebAppTO();
		}
		else if ( this.getElementPath().equals( "/" + WebAppTO.LOCAL_NAME + "/" + MimeMappingTO.LOCAL_NAME ) == true ) {
			this.mimeMappingTOTemp			= new MimeMappingTO();
		}

		
		this.write( "<" );
		this.write( eName );

		// Iterate over attributes
		if ( atts != null ) {
			for ( int i = 0; i < atts.getLength(); i++ ) {
				// Attribute name
				String aName				= null;
				// namespaceAware = false, i.e. does not use Namespaces
				if ( "".equals( atts.getLocalName( i ) ) ) {
					aName					= atts.getQName( i );
				}
				// namespaceAware = true, i.e. use Namespaces
				else {
					String aPrefix			= this.nsContexts.getPrefix( atts.getURI( i ) );
					// Namespace URI was not found from XML Namespace contexts
					if ( aPrefix == null ) {
						aName				= atts.getQName( i );
					}
					// Namespace URI was found from XML Namespace contexts
					else {
						if ( XMLConstants.DEFAULT_NS_PREFIX.equals( aPrefix ) == true ) {
							aName			= atts.getLocalName( i );
						}
						else {
							aName			= aPrefix + ":" + atts.getLocalName( i );
						}
					}
				}

				// Set attribute values

				
				this.write( " " );
				this.write( aName );
				this.write( "=\"" );
				this.writeEscapeAttributeValue( atts.getValue( i ) );
				this.write( "\"" );
			}
		}
		this.write( ">" );

		
		// Display the current path that has been found...
		logger.log( Level.DEBUG, "Path found: [" + this.getElementPath() + "]" );
	}
	// SAX2 ContentHandler method
	public void startPrefixMapping( String prefix, String uri ) throws SAXException {
		logger.log( Level.DEBUG, "Method has been called." );
		this.nsContexts.bound( prefix, uri );
	}
	
	
	// SAX2 LexicalHandler method
	public void comment( char[] ch, int start, int length ) throws SAXException {
		logger.log( Level.DEBUG, "Method has been called." );
		// Write characters accumulated after last event
		this.writeEscape( this.getAndResetCharacters() );
		this.inComment	= true;

		// Do not write comments inside (external) DTD
		if ( this.inDTD == false ) {
			this.write( "<!--" );
			this.write( new String( ch, start, length ) );
			this.write( "-->" );
		}
		this.inComment	= false;
	}
	// SAX2 LexicalHandler method
	public void endCDATA() throws SAXException {
		logger.log( Level.DEBUG, "Method has been called." );
		// Write characters accumulated after last event
		this.writeEscape( this.getAndResetCharacters() );
		this.write( "]]>" );
		this.inCDATA	= false;
	}
	// SAX2 LexicalHandler method
	public void endDTD() throws SAXException {
		logger.log( Level.DEBUG, "Method has been called." );
		// Write characters accumulated after last event
		this.writeEscape( this.getAndResetCharacters() );
		this.inDTD	= false;
		this.resetCharacters();
	}
	// SAX2 LexicalHandler method
	public void endEntity( String name ) throws SAXException {
		logger.log( Level.DEBUG, "Method has been called." );
	}
	// SAX2 LexicalHandler method
	public void startCDATA() throws SAXException {
		logger.log( Level.DEBUG, "Method has been called." );
		// Write characters accumulated after last event
		this.writeEscape( this.getAndResetCharacters() );
		this.inCDATA	= true;
		this.write( "<![CDATA[" );
	}
	// SAX2 LexicalHandler method
	public void startDTD( String name, String publicId, String systemId ) throws SAXException {
		logger.log( Level.DEBUG, "Method has been called." );
		// Write characters accumulated after last event
		this.writeEscape( this.getAndResetCharacters() );
		this.inDTD	= true;
		this.write( "<!DOCTYPE " );
		this.write( name );
		this.write( " PUBLIC \"" );
		this.write( publicId );
		this.write( "\" \"" );
		this.write( systemId );
		this.write( "\">" );
		this.writeEscape( Constants.LINE_SEPARATOR );
		this.resetCharacters();
	}
	// SAX2 LexicalHandler method
	public void startEntity( String name ) throws SAXException {
		logger.log( Level.DEBUG, "Method has been called." );
	}
		
	
//	/**
//	 * Retrieve XML Namespace contexts.
//	 * 
//	 * 
//	 * @return contexts		XML Namespace contexts.
//	 */
//	public Map<String, String> getNamespaceContexts() {
//		logger.log( Level.DEBUG, "Method has been called." );
//		return this.nsContexts.getContexts();
//	}

	
//	public String getContents() {
//		logger.log( Level.DEBUG, "Method has been called." );
//		return this.writerBuffer.toString();
//	}
	
	
	public WebAppTO getWebAppTO() {
		logger.log( Level.DEBUG, "Method has been called." );
		return this.webAppTO;
	}
}