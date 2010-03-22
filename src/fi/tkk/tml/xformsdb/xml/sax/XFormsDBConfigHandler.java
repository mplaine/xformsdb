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
import fi.tkk.tml.xformsdb.xml.to.xformsdb.FilesMetadataTO;
import fi.tkk.tml.xformsdb.xml.to.xformsdb.MimeMappingTO;
import fi.tkk.tml.xformsdb.xml.to.xformsdb.SecurityFileTO;
import fi.tkk.tml.xformsdb.xml.to.xformsdb.XFormsDBConfigTO;
import fi.tkk.tml.xformsdb.xml.to.xformsdb.DataSourceTO;


/**
 * Handle SAX2 events of the XFormsDB configuration file (conf.xml).
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on October 26, 2009
 */
public class XFormsDBConfigHandler extends BaseHandler implements LexicalHandler {
	
	
	
	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger								= Logger.getLogger( XFormsDBConfigHandler.class );

	
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
	// XFormsDB configuration file related variables
	private XFormsDBConfigTO xformsDBConfigTO;
	private MimeMappingTO mimeMappingTOTemp;
	private DataSourceTO dataSourceTOTemp;
	private FilesMetadataTO filesMetadataTOTemp;
	private SecurityFileTO securityFileTOTemp;


	// CONSTRUCTORS
	/**
	 * Default constructor.
	 */
	public XFormsDBConfigHandler( /*String encoding*/ ) throws UnsupportedEncodingException {
		super();
		logger.log( Level.DEBUG, "Constructor has been called." );
		this.elementStack				= new Stack<String>();
		this.nsContexts					= new NamespaceContextImpl();
		this.nsContexts.bound( Constants.XFORMSDB_CONFIG_NAMESPACE_PREFIX, Constants.XFORMSDB_CONFIG_NAMESPACE_URI );
		this.locator					= null;
//		this.writerBuffer				= new ByteArrayOutputStream();
		this.xmlDeclVersion				= XFormsDBConfigHandler.DEFAULT_XML_DECL_VERSION;
		this.xmlDeclEncoding			= XFormsDBConfigHandler.DEFAULT_XML_DECL_ENCODING;
//		this.encoding					= ConfigurationHandler.DEFAULT_ENCODING;
//		this.setOutput( this.writerBuffer, encoding );
		this.characters					= new CharArrayWriter();
		this.inDTD						= false;
		this.inProcessingInstruction	= false;
		this.inComment					= false;
		this.inCDATA					= false;
//		this.contents					= null;
		this.xformsDBConfigTO			= null; 
		this.mimeMappingTOTemp			= null; 
		this.dataSourceTOTemp			= null;
		this.filesMetadataTOTemp		= null;
		this.securityFileTOTemp			= null;
	}
	
	
	// PRIVATE METHODS
	// Build the path string from the current state
	// of the stack... Very inefficient
	private String getElementPath() {
		logger.log( Level.DEBUG, "Method has been called." );
		// Build the path string...
		StringBuffer sb			= new StringBuffer( "" );
		Enumeration<String> e	= this.elementStack.elements();
		
		while ( e.hasMoreElements() ) {
			sb.append( "/" + e.nextElement() );
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
		// Add the default XFormsDB MIME Mapping if needed
		if ( this.xformsDBConfigTO.getMimeMappingTOs().size() == 0 ) {
			this.mimeMappingTOTemp	= new MimeMappingTO();
			this.xformsDBConfigTO.getMimeMappingTOs().add( this.mimeMappingTOTemp );
		}
		// Add the default data source if needed
		if ( this.xformsDBConfigTO.getDataSourceTOs().size() == 0 ) {
			this.dataSourceTOTemp	= new DataSourceTO();
			this.xformsDBConfigTO.getDataSourceTOs().add( this.dataSourceTOTemp );
		}
		// Add the default security file if needed
		if ( this.xformsDBConfigTO.getSecurityFileTOs().size() == 0 ) {
			this.securityFileTOTemp	= new SecurityFileTO();
			this.xformsDBConfigTO.getSecurityFileTOs().put( Constants.XFORMSDB_CONFIG_DEFAULT_SECURITY_FILE_EXTENSION_XQ, this.securityFileTOTemp );
		}
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
		if ( this.getElementPath().equals( "/" + XFormsDBConfigTO.LOCAL_NAME + "/" + MimeMappingTO.LOCAL_NAME ) == true ) {
			this.xformsDBConfigTO.getMimeMappingTOs().add( this.mimeMappingTOTemp );
		}
		else if ( this.getElementPath().equals( "/" + XFormsDBConfigTO.LOCAL_NAME + "/" + MimeMappingTO.LOCAL_NAME + "/extension" ) == true ) {
			String elementExtension	= characters.trim();
			if ( "".equals( elementExtension ) == false ) {
				// Do not escape
				this.mimeMappingTOTemp.setExtension( elementExtension );
			}
		}
		else if ( this.getElementPath().equals( "/" + XFormsDBConfigTO.LOCAL_NAME + "/" + MimeMappingTO.LOCAL_NAME + "/mime-type" ) == true ) {
			String elementMimeType	= characters.trim();
			if ( "".equals( elementMimeType ) == false ) {
				// Do not escape
				this.mimeMappingTOTemp.setMimeType( elementMimeType );
			}
		}
		else if ( this.getElementPath().equals( "/" + XFormsDBConfigTO.LOCAL_NAME + "/encoding" ) == true ) {
			String elementEncoding	= characters.trim();
			if ( "".equals( elementEncoding ) == false ) {
				// Do not escape
				this.xformsDBConfigTO.setEncoding( elementEncoding );
			}
		}
		else if ( this.getElementPath().equals( "/" + XFormsDBConfigTO.LOCAL_NAME + "/" + DataSourceTO.LOCAL_NAME ) == true ) {
			this.xformsDBConfigTO.getDataSourceTOs().add( this.dataSourceTOTemp );
		}
		else if ( this.getElementPath().equals( "/" + XFormsDBConfigTO.LOCAL_NAME + "/" + DataSourceTO.LOCAL_NAME + "/type" ) == true ) {
			String elementType	= characters.trim();
			if ( "".equals( elementType ) == false ) {
				// Do not escape
				this.dataSourceTOTemp.setType( elementType );				
			}
		}
		else if ( this.getElementPath().equals( "/" + XFormsDBConfigTO.LOCAL_NAME + "/" + DataSourceTO.LOCAL_NAME + "/uri" ) == true ) {
			String elementUri	= characters.trim();
			if ( "".equals( elementUri ) == false ) {
				// Do not escape
				this.dataSourceTOTemp.setUri( elementUri );
			}
		}
		else if ( this.getElementPath().equals( "/" + XFormsDBConfigTO.LOCAL_NAME + "/" + DataSourceTO.LOCAL_NAME + "/username" ) == true ) {
			String elementUsername	= characters.trim();
			if ( "".equals( elementUsername ) == false ) {
				// Do not escape
				this.dataSourceTOTemp.setUsername( elementUsername );
			}
		}
		else if ( this.getElementPath().equals( "/" + XFormsDBConfigTO.LOCAL_NAME + "/" + DataSourceTO.LOCAL_NAME + "/password" ) == true ) {
			String elementPassword	= characters.trim();
			if ( "".equals( elementPassword ) == false ) {
				// Do not escape
				this.dataSourceTOTemp.setPassword( elementPassword );
			}
		}
		else if ( this.getElementPath().equals( "/" + XFormsDBConfigTO.LOCAL_NAME + "/" + DataSourceTO.LOCAL_NAME + "/collection" ) == true ) {
			String elementCollection	= characters.trim();
			if ( "".equals( elementCollection ) == false ) {
				// Do not escape
				this.dataSourceTOTemp.setCollection( elementCollection );
			}
		}
		else if ( this.getElementPath().equals( "/" + XFormsDBConfigTO.LOCAL_NAME + "/" + FilesMetadataTO.LOCAL_NAME ) == true ) {
			this.xformsDBConfigTO.setFilesMetadataTO( this.filesMetadataTOTemp );
		}
		else if ( this.getElementPath().equals( "/" + XFormsDBConfigTO.LOCAL_NAME + "/" + FilesMetadataTO.LOCAL_NAME + "/type" ) == true ) {
			String elementType	= characters.trim();
			if ( "".equals( elementType ) == false ) {
				// Do not escape
				this.filesMetadataTOTemp.setType( elementType );
			}
		}
		else if ( this.getElementPath().equals( "/" + XFormsDBConfigTO.LOCAL_NAME + "/" + FilesMetadataTO.LOCAL_NAME + "/uri" ) == true ) {
			String elementUri	= characters.trim();
			if ( "".equals( elementUri ) == false ) {
				// Do not escape
				this.filesMetadataTOTemp.setUri( elementUri );
			}
		}
		else if ( this.getElementPath().equals( "/" + XFormsDBConfigTO.LOCAL_NAME + "/" + FilesMetadataTO.LOCAL_NAME + "/username" ) == true ) {
			String elementUsername	= characters.trim();
			if ( "".equals( elementUsername ) == false ) {
				// Do not escape
				this.filesMetadataTOTemp.setUsername( elementUsername );
			}
		}
		else if ( this.getElementPath().equals( "/" + XFormsDBConfigTO.LOCAL_NAME + "/" + FilesMetadataTO.LOCAL_NAME + "/password" ) == true ) {
			String elementPassword	= characters.trim();
			if ( "".equals( elementPassword ) == false ) {
				// Do not escape
				this.filesMetadataTOTemp.setPassword( elementPassword );
			}
		}
		else if ( this.getElementPath().equals( "/" + XFormsDBConfigTO.LOCAL_NAME + "/" + FilesMetadataTO.LOCAL_NAME + "/collection" ) == true ) {
			String elementCollection	= characters.trim();
			if ( "".equals( elementCollection ) == false ) {
				// Do not escape
				this.filesMetadataTOTemp.setCollection( elementCollection );
			}
		}
		else if ( this.getElementPath().equals( "/" + XFormsDBConfigTO.LOCAL_NAME + "/files-folder" ) == true ) {
			String elementFilesFolder	= characters.trim();
			if ( "".equals( elementFilesFolder ) == false ) {
				// Do not escape
				this.xformsDBConfigTO.setFilesFolder( elementFilesFolder );
			}
		}
		else if ( this.getElementPath().equals( "/" + XFormsDBConfigTO.LOCAL_NAME + "/tdm-conflict-level" ) == true ) {
			String elementTDMConflictLevel	= characters.trim();
			if ( "".equals( elementTDMConflictLevel ) == false ) {
				// Do not escape
				this.xformsDBConfigTO.setTDMConflictLevel( elementTDMConflictLevel );
			}
		}
		else if ( this.getElementPath().equals( "/" + XFormsDBConfigTO.LOCAL_NAME + "/widget-authentication-uri" ) == true ) {
			String elementWidgetAuthenticationUri	= characters.trim();
			if ( "".equals( elementWidgetAuthenticationUri ) == false ) {
				// Do not escape
				this.xformsDBConfigTO.setWidgetAuthenticationURI( elementWidgetAuthenticationUri );
			}
		}
		else if ( this.getElementPath().equals( "/" + XFormsDBConfigTO.LOCAL_NAME + "/widget-data-sources-uri" ) == true ) {
			String elementWidgetDataSourcesUri	= characters.trim();
			if ( "".equals( elementWidgetDataSourcesUri ) == false ) {
				// Do not escape
				this.xformsDBConfigTO.setWidgetDataSourcesURI( elementWidgetDataSourcesUri );
			}
		}
		else if ( this.getElementPath().equals( "/" + XFormsDBConfigTO.LOCAL_NAME + "/" + SecurityFileTO.LOCAL_NAME ) == true ) {
			this.xformsDBConfigTO.getSecurityFileTOs().put( this.securityFileTOTemp.getExtension(), this.securityFileTOTemp );
		}
		else if ( this.getElementPath().equals( "/" + XFormsDBConfigTO.LOCAL_NAME + "/" + SecurityFileTO.LOCAL_NAME + "/extension" ) == true ) {
			String elementExtension	= characters.trim();
			if ( "".equals( elementExtension ) == false ) {
				// Do not escape
				this.securityFileTOTemp.setExtension( elementExtension );
			}
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
		if ( this.getElementPath().equals( "/" + XFormsDBConfigTO.LOCAL_NAME ) == true ) {
			this.xformsDBConfigTO			= new XFormsDBConfigTO();
		}
		else if ( this.getElementPath().equals( "/" + XFormsDBConfigTO.LOCAL_NAME + "/" + MimeMappingTO.LOCAL_NAME ) == true ) {
			this.mimeMappingTOTemp			= new MimeMappingTO();
		}
		else if ( this.getElementPath().equals( "/" + XFormsDBConfigTO.LOCAL_NAME + "/" + DataSourceTO.LOCAL_NAME ) == true ) {
			this.dataSourceTOTemp			= new DataSourceTO();
		}
		else if ( this.getElementPath().equals( "/" + XFormsDBConfigTO.LOCAL_NAME + "/" + FilesMetadataTO.LOCAL_NAME ) == true ) {
			this.filesMetadataTOTemp		= new FilesMetadataTO();
		}
		else if ( this.getElementPath().equals( "/" + XFormsDBConfigTO.LOCAL_NAME + "/" + SecurityFileTO.LOCAL_NAME ) == true ) {
			this.securityFileTOTemp			= new SecurityFileTO();
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
				if ( ( this.getElementPath().equals( "/" + XFormsDBConfigTO.LOCAL_NAME + "/" + DataSourceTO.LOCAL_NAME ) == true ) && ( DataSourceTO.ATTRIBUTE_NAME_ID.equals( aName ) == true ) ) {
					// Do not escape
					this.dataSourceTOTemp.setId( atts.getValue( i ).trim() );
				}

				
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
	
	
	public XFormsDBConfigTO getXFormsDBConfigTO() {
		logger.log( Level.DEBUG, "Method has been called." );
		return this.xformsDBConfigTO;
	}
}