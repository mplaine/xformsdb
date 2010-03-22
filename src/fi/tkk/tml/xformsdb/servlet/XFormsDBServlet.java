package fi.tkk.tml.xformsdb.servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.xml.sax.InputSource;

import fi.tkk.tml.xformsdb.core.Constants;
import fi.tkk.tml.xformsdb.dao.DAOFactory;
import fi.tkk.tml.xformsdb.dao.exist.ExistDAOFactory;
import fi.tkk.tml.xformsdb.dao.xmldocument.XMLDocumentDAOFactory;
import fi.tkk.tml.xformsdb.error.ErrorConstants;
import fi.tkk.tml.xformsdb.error.ErrorWriter;
import fi.tkk.tml.xformsdb.error.HandlerException;
import fi.tkk.tml.xformsdb.error.InitializationException;
import fi.tkk.tml.xformsdb.handler.RequestHandler;
import fi.tkk.tml.xformsdb.manager.HTTPRequestHeadersGetManager;
import fi.tkk.tml.xformsdb.util.ExceptionUtils;
import fi.tkk.tml.xformsdb.util.FileUtils;
import fi.tkk.tml.xformsdb.util.StringUtils;
import fi.tkk.tml.xformsdb.xml.sax.XFormsDBConfigHandler;
import fi.tkk.tml.xformsdb.xml.sax.SAXParseErrorException;
import fi.tkk.tml.xformsdb.xml.sax.SAXParseFatalErrorException;
import fi.tkk.tml.xformsdb.xml.sax.SAXParseWarningException;
import fi.tkk.tml.xformsdb.xml.sax.WebAppHandler;
import fi.tkk.tml.xformsdb.xml.to.RequestHeaderTO;
import fi.tkk.tml.xformsdb.xml.to.WebAppTO;
import fi.tkk.tml.xformsdb.xml.to.xformsdb.FilesMetadataTO;
import fi.tkk.tml.xformsdb.xml.to.xformsdb.MimeMappingTO;
import fi.tkk.tml.xformsdb.xml.to.xformsdb.XFormsDBConfigTO;
import fi.tkk.tml.xformsdb.xml.to.xformsdb.DataSourceTO;


/**
 * XFormsDB core servlet.
 *
 *
 * @author Markku Laine
 * @version 1.0	 Created on November 19, 2009
 */
public class XFormsDBServlet extends HttpServlet {


	
	// PRIVATE STATIC FINAL VARIABLES
	private static final long serialVersionUID		= 1;
	private static final Logger logger				= Logger.getLogger( XFormsDBServlet.class );

	
	// PRIVATE VARIABLES
	private RequestHandler rh						= null;
	private ErrorWriter ew							= null;
	private InitializationException iex				= null;
	private String serverURIEncoding				= null;
	private WebAppTO webAppTO						= null;
	private XFormsDBConfigTO xformsdbConfigTO		= null;
	private Map<String, DAOFactory> daoFactories	= null;
	private String xformsdbIncludeFilePath			= null;
	private String xformsdbSecviewFilePath			= null;
	private String xformsdbExtractFilePath			= null;
	private String xformsdbXFormsFilePath			= null;
	private String indentFilePath					= null;
	
	
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
			throw new InitializationException( ErrorConstants.ERROR_CODE_INITIALIZATION_17, ErrorConstants.ERROR_MESSAGE_INITIALIZATION_17, ex );
		}

		
		return serverURIEncoding;
	}
	
	
	/**
	 * Retrieve and initialize the Web application file (web.xml) information.
	 * 
	 * 
	 * @return webAppTO						The Web application file information.
	 * @throws InitializationException		If the application fails to load the Web application file.
	 */
	private WebAppTO getAndInitWebAppTO() throws InitializationException {
		logger.log( Level.DEBUG, "Method has been called." );
		logger.log( Level.INFO, "Loading web.xml: " + this.getServletContext().getRealPath( Constants.WEB_APPLICATION_FILE_PATH ) );
		WebAppTO webAppTO			= null;
		String webAppFilePath		= null;
		InputStream content			= null;
		
		
		try {
			// Retrieve the Web application file path
			webAppFilePath			= this.getServletContext().getRealPath( Constants.WEB_APPLICATION_FILE_PATH );
			logger.log( Level.DEBUG, "The the Web application file path has been successfully retrieved." );
		} catch ( Exception ex ) {
			throw new InitializationException( ErrorConstants.ERROR_CODE_INITIALIZATION_13, ErrorConstants.ERROR_MESSAGE_INITIALIZATION_13, ex );
		}
		
		try {
			// Retrieve the content of the Web application file as an input stream
			content					= FileUtils.getFileAsInputStream( webAppFilePath );
			logger.log( Level.DEBUG, "The content of the Web application file as an input stream has been successfully retrieved." );
		} catch ( Exception ex ) {
			throw new InitializationException( ErrorConstants.ERROR_CODE_INITIALIZATION_12, ErrorConstants.ERROR_MESSAGE_INITIALIZATION_12, ex );
		}

		try {
			// Parse the Web application file
			WebAppHandler wah		= new WebAppHandler();
			SAXParserFactory spf	= SAXParserFactory.newInstance();
			spf.setNamespaceAware( true );
			spf.setValidating( false ); // XML Schema validation seems to be having some mysterious problems --> false
			spf.setFeature( WebAppHandler.NAMESPACES_FEATURE_ID, true );
			spf.setFeature( WebAppHandler.NAMESPACE_PREFIXES_FEATURE_ID, true );
			SAXParser sp			= spf.newSAXParser();
			sp.setProperty( WebAppHandler.LEXICAL_HANDLER_PROPERTY_ID, wah );
			InputSource inputSource	= new InputSource();
			inputSource.setByteStream( content );
			//inputSource.setEncoding( encoding ); // Encoding detected automatically based on the XML declaration
			sp.parse( inputSource, wah );
			
			webAppTO				= wah.getWebAppTO();
			logger.log( Level.DEBUG, "The Web application file has been successfully parsed." );
		} catch ( SAXParseWarningException saxpwex ) {
			throw new InitializationException( "SAX parse warning exception" + ", line " + saxpwex.getLineNumber() + ", column " + saxpwex.getColumnNumber() + ", uri " + saxpwex.getSystemId() + ". " + saxpwex.getMessage(), saxpwex );
		} catch ( SAXParseErrorException saxpeex ) {
			throw new InitializationException( "SAX parse error exception" + ", line " + saxpeex.getLineNumber() + ", column " + saxpeex.getColumnNumber() + ", uri " + saxpeex.getSystemId() + ". " + saxpeex.getMessage(), saxpeex );
		} catch ( SAXParseFatalErrorException saxpfeex ) {
			throw new InitializationException( "SAX parse fatal error exception" + ", line " + saxpfeex.getLineNumber() + ", column " + saxpfeex.getColumnNumber() + ", uri " + saxpfeex.getSystemId() + ". " + saxpfeex.getMessage(), saxpfeex );
		} catch ( Exception ex ) {
			throw new InitializationException( ErrorConstants.ERROR_CODE_INITIALIZATION_11, ErrorConstants.ERROR_MESSAGE_INITIALIZATION_11, ex );
		}
		logger.log( Level.DEBUG, "The Web application file has been successfully retrieved and initialized." );

		
		return webAppTO;
	}
	
	
	/**
	 * Retrieve and initialize the XFormsDB configuration file (conf.xml) information.
	 * 
	 * 
	 * @return xformsDBConfigTO				The XFormsDB configuration file information.
	 * @throws InitializationException		If the application fails to load the XFormsDB configuration file.
	 */
	private XFormsDBConfigTO getAndInitXFormsDBConfigTO() throws InitializationException {
		logger.log( Level.DEBUG, "Method has been called." );
		logger.log( Level.INFO, "Loading conf.xml: " + this.getServletContext().getRealPath( Constants.XFORMSDB_CONFIG_FILE_PATH ) );
		XFormsDBConfigTO xformsDBConfigTO	= null;
		String xformsDBConfigFilePath		= null;
		InputStream content					= null;
		
		
		try {
			// Retrieve the XFormsDB configuration file path
			xformsDBConfigFilePath			= this.getServletContext().getRealPath( Constants.XFORMSDB_CONFIG_FILE_PATH );
			logger.log( Level.DEBUG, "The XFormsDB configuration file path has been successfully retrieved." );
		} catch ( Exception ex ) {
			throw new InitializationException( ErrorConstants.ERROR_CODE_INITIALIZATION_10, ErrorConstants.ERROR_MESSAGE_INITIALIZATION_10, ex );
		}
		
		try {
			// Retrieve the content of the XFormsDB configuration file as an input stream
			content							= FileUtils.getFileAsInputStream( xformsDBConfigFilePath );
			logger.log( Level.DEBUG, "The content of the XFormsDB configuration file as an input stream has been successfully retrieved." );
		} catch ( Exception ex ) {
			throw new InitializationException( ErrorConstants.ERROR_CODE_INITIALIZATION_9, ErrorConstants.ERROR_MESSAGE_INITIALIZATION_9, ex );
		}

		try {
			// Parse the XFormsDB configuration file
			XFormsDBConfigHandler xfdbch	= new XFormsDBConfigHandler();
			SAXParserFactory spf			= SAXParserFactory.newInstance();
			spf.setNamespaceAware( true );
			spf.setValidating( false ); // No DTD or XML Schema has been yet assigned
			spf.setFeature( XFormsDBConfigHandler.NAMESPACES_FEATURE_ID, true );
			spf.setFeature( XFormsDBConfigHandler.NAMESPACE_PREFIXES_FEATURE_ID, true );
			SAXParser sp					= spf.newSAXParser();
			sp.setProperty( XFormsDBConfigHandler.LEXICAL_HANDLER_PROPERTY_ID, xfdbch );
			InputSource inputSource			= new InputSource();
			inputSource.setByteStream( content );
			//inputSource.setEncoding( encoding ); // Encoding detected automatically based on the XML declaration
			sp.parse( inputSource, xfdbch );
			
			xformsDBConfigTO				= xfdbch.getXFormsDBConfigTO();
			logger.log( Level.DEBUG, "The XFormsDB configuration file has been successfully parsed." );
		} catch ( SAXParseWarningException saxpwex ) {
			throw new InitializationException( "SAX parse warning exception" + ", line " + saxpwex.getLineNumber() + ", column " + saxpwex.getColumnNumber() + ", uri " + saxpwex.getSystemId() + ". " + saxpwex.getMessage(), saxpwex );
		} catch ( SAXParseErrorException saxpeex ) {
			throw new InitializationException( "SAX parse error exception" + ", line " + saxpeex.getLineNumber() + ", column " + saxpeex.getColumnNumber() + ", uri " + saxpeex.getSystemId() + ". " + saxpeex.getMessage(), saxpeex );
		} catch ( SAXParseFatalErrorException saxpfeex ) {
			throw new InitializationException( "SAX parse fatal error exception" + ", line " + saxpfeex.getLineNumber() + ", column " + saxpfeex.getColumnNumber() + ", uri " + saxpfeex.getSystemId() + ". " + saxpfeex.getMessage(), saxpfeex );
		} catch ( Exception ex ) {
			throw new InitializationException( ErrorConstants.ERROR_CODE_INITIALIZATION_8, ErrorConstants.ERROR_MESSAGE_INITIALIZATION_8, ex );
		}
		logger.log( Level.DEBUG, "The XFormsDB configuration file has been successfully retrieved and initialized." );
		
		
		return xformsDBConfigTO;
	}
	
	
	/**
	 * Update the XFormsDB configuration file (conf.xml).
	 * 
	 * 
	 * @param xformsdbConfigTO				The XFormsDB configuration file information.
	 * @return xformsdbConfigTO				The updated XFormsDB configuration file information.
	 * @throws InitializationException		If the application fails to update the XFormsDB configuration file (conf.xml).
	 */
	private XFormsDBConfigTO updateXFormsDBConfigTO( XFormsDBConfigTO xformsdbConfigTO ) throws InitializationException {
		logger.log( Level.DEBUG, "Method has been called." );
		File file				= null;
		File finalFile			= null;
		String finalFilePath	= null;
		
		
		try {
			// Create a file of the given files folder path
			file				= new File( xformsdbConfigTO.getFilesFolder() );
			// Absolute path
			if ( file.isAbsolute() == true || file.getPath().startsWith( File.separator ) == true ) {
				// Okay
				finalFilePath	= file.getAbsolutePath();
			}
			// Relative path
			else {
				// The given files folder path contains illegal characters, such as starts with file:
				if ( file.getPath().contains( ":" ) == true ) {
					throw new InitializationException( ErrorConstants.ERROR_CODE_INITIALIZATION_15, ErrorConstants.ERROR_MESSAGE_INITIALIZATION_15_1 + file.getPath() + ErrorConstants.ERROR_MESSAGE_INITIALIZATION_15_2 );
				}
				
				// Okay
				finalFilePath	= this.getServletContext().getRealPath( file.getPath() );
			}
			
			
			// Create a file of the final files folder path
			finalFile			= new File ( finalFilePath );
			
			// Create the directories if needed
			finalFile.mkdirs();			

			// The final files folder path cannot be accessed
			if ( finalFile.isAbsolute() == false || finalFile.isDirectory() == false || finalFile.canRead() == false || finalFile.canWrite() == false ) {
				throw new InitializationException( ErrorConstants.ERROR_CODE_INITIALIZATION_16, ErrorConstants.ERROR_MESSAGE_INITIALIZATION_16_1 + finalFilePath + ErrorConstants.ERROR_MESSAGE_INITIALIZATION_16_2 );				
			}
			
			// Update the files folder path
			xformsdbConfigTO.setFilesFolder( finalFilePath );
			logger.log( Level.DEBUG, "The XFormsDB configuration file (conf.xml) has been successfully updated." );
		} catch ( InitializationException iex ) {
			throw iex;
		} catch ( Exception ex ) {
			throw new InitializationException( ErrorConstants.ERROR_CODE_INITIALIZATION_14, ErrorConstants.ERROR_MESSAGE_INITIALIZATION_14, ex );
		}

	
		return xformsdbConfigTO;
	}

	
	/**
	 * Update the Web application file (web.xml) information.
	 * 
	 * 
	 * @param webAppTO						The Web application file information.
	 * @param xformsDBConfigTO				The XFormsDB configuration file information.
	 * @return webAppTO						The updated Web application file information.
	 * @throws InitializationException		If the application fails to update the Web application file information.
	 */
	private WebAppTO updateWebAppTO( WebAppTO webAppTO, XFormsDBConfigTO xformsDBConfigTO ) throws InitializationException {
		logger.log( Level.DEBUG, "Method has been called." );
		
		
		try {
			// Remove existing XFormsDB MIME mappings (if any) from the Web application file MIME mappings
			MimeMappingTO mimeMappingTO			= null;
			for ( int i = 0; i < xformsDBConfigTO.getMimeMappingTOs().size(); i++ ) {
				mimeMappingTO					= xformsDBConfigTO.getMimeMappingTOs().get( i );
				webAppTO.getMimeMappingTOs().remove( mimeMappingTO.getExtension() );
			}
			logger.log( Level.DEBUG, "Existing XFormsDB MIME mappings have been removed from the Web application file MIME mappings." );
		} catch ( Exception ex ) {
			throw new InitializationException( ErrorConstants.ERROR_CODE_INITIALIZATION_7, ErrorConstants.ERROR_MESSAGE_INITIALIZATION_7, ex );
		}
		
		try {
			// Add the XFormsDB MIME mappings to the Web application file MIME mappings
			MimeMappingTO mimeMappingTO			= null;
			for ( int i = 0; i < xformsDBConfigTO.getMimeMappingTOs().size(); i++ ) {
				mimeMappingTO					= xformsDBConfigTO.getMimeMappingTOs().get( i );
				webAppTO.getMimeMappingTOs().put( mimeMappingTO.getExtension(), mimeMappingTO );
			}
			logger.log( Level.DEBUG, "The XFormsDB MIME mappings have been added to the Web application file MIME mappings." );
		} catch ( Exception ex ) {
			throw new InitializationException( ErrorConstants.ERROR_CODE_INITIALIZATION_6, ErrorConstants.ERROR_MESSAGE_INITIALIZATION_6, ex );
		}

	
		return webAppTO;
	}
	
	
	/**
	 * Retrieve and initialize the DAO factory implementations.
	 * 
	 * 
	 * @param xformsDBConfigTO				The XFormsDB configuration file.
	 * @return daoFactories					The DAO factory implementations.
	 * @throws InitializationException		If the application fails to initialize a data source.
	 */
	private Map<String, DAOFactory> getAndInitDAOFactories( XFormsDBConfigTO xformsDBConfigTO ) throws InitializationException {
		logger.log( Level.DEBUG, "Method has been called." );
		Map<String, DAOFactory> daoFactories	= new HashMap<String, DAOFactory>( 16 );
		List<DataSourceTO> dataSourceTOs		= xformsDBConfigTO.getDataSourceTOs();
		DataSourceTO dataSourceTOTemp			= null;
		FilesMetadataTO filesMetadataTO			= xformsDBConfigTO.getFilesMetadataTO();
		DAOFactory daoFactoryTemp				= null;
		boolean defaultDataSourceSet			= false;
		boolean defaultFilesMetadataSet			= false;
		
		
		try {
			// Iterate over data sources
			for ( int i = 0; i < dataSourceTOs.size(); i++ ) {
				dataSourceTOTemp				= dataSourceTOs.get( i );
				logger.log( Level.INFO, "Initializing data source: " + dataSourceTOTemp.getId() );
		
				// Set the DAO Factory
				int dataSourceType				= Integer.parseInt( dataSourceTOTemp.getType() );
				switch ( dataSourceType ) {
					case DAOFactory.XML_DOCUMENT:
						daoFactoryTemp			= ( XMLDocumentDAOFactory ) DAOFactory.getDAOFactory( DAOFactory.XML_DOCUMENT );
						break;
					case DAOFactory.EXIST:
						daoFactoryTemp			= ( ExistDAOFactory ) DAOFactory.getDAOFactory( DAOFactory.EXIST );
						break;
					default:
						throw new InitializationException( "Illegal data source type value '" + dataSourceType + "' in the <data-source" + ( ( dataSourceTOTemp.getId() != null ) ? " id=\"" + dataSourceTOTemp.getId() + "\">" : ">" ) + " element." );
				}

				// Set DAO data Source
				daoFactoryTemp.getDAODataSource().setId( dataSourceTOTemp.getId() );
				// Type has already been set
				daoFactoryTemp.getDAODataSource().setUri( ( ( "".equals( dataSourceTOTemp.getUri() ) == false ) ? dataSourceTOTemp.getUri() : this.getServletContext().getRealPath( "/" ) ) );
				daoFactoryTemp.getDAODataSource().setUsername( dataSourceTOTemp.getUsername() );
				daoFactoryTemp.getDAODataSource().setPassword( dataSourceTOTemp.getPassword() );
				daoFactoryTemp.getDAODataSource().setCollection( dataSourceTOTemp.getCollection() );

				daoFactories.put( daoFactoryTemp.getDAODataSource().getId(), daoFactoryTemp );
				logger.log( Level.DEBUG, "The DAO factory implementation has been successfully set." );
				
				if ( defaultDataSourceSet == false ) {
					defaultDataSourceSet		= true;
					// Set the default DAO Factory
					daoFactories.put( Constants.XFORMSDB_CONFIG_DEFAULT_DATA_SOURCE_ID, daoFactoryTemp );					
					logger.log( Level.DEBUG, "The default DAO factory implementation has been successfully set." );
				}
			}
			
			if ( defaultDataSourceSet == false ) {
				defaultDataSourceSet			= true;
				// Set the default DAO factory implementation
				daoFactoryTemp					= ( XMLDocumentDAOFactory ) DAOFactory.getDAOFactory( DAOFactory.XML_DOCUMENT );
				dataSourceTOTemp				= new DataSourceTO();
				daoFactoryTemp.getDAODataSource().setId( dataSourceTOTemp.getId() );
				// Type has already been set
				daoFactoryTemp.getDAODataSource().setUri( ( ( "".equals( dataSourceTOTemp.getUri() ) == false ) ? dataSourceTOTemp.getUri() : this.getServletContext().getRealPath( "/" ) ) );
				daoFactoryTemp.getDAODataSource().setUsername( dataSourceTOTemp.getUsername() );
				daoFactoryTemp.getDAODataSource().setPassword( dataSourceTOTemp.getPassword() );
				daoFactoryTemp.getDAODataSource().setCollection( dataSourceTOTemp.getCollection() );
				daoFactories.put( Constants.XFORMSDB_CONFIG_DEFAULT_DATA_SOURCE_ID, daoFactoryTemp );					
				logger.log( Level.DEBUG, "The default DAO factory implementation has been successfully set." );
			}

			
			// Retrieve data source for files metadata
			if ( filesMetadataTO != null ) {
				logger.log( Level.INFO, "Initializing data source for files metadata." );
		
				// Set the DAO Factory
				int filesMetadataType			= Integer.parseInt( filesMetadataTO.getType() );
				switch ( filesMetadataType ) {
					case DAOFactory.XML_DOCUMENT:
						daoFactoryTemp			= ( XMLDocumentDAOFactory ) DAOFactory.getDAOFactory( DAOFactory.XML_DOCUMENT );
						break;
					case DAOFactory.EXIST:
						daoFactoryTemp			= ( ExistDAOFactory ) DAOFactory.getDAOFactory( DAOFactory.EXIST );
						break;
					default:
						throw new InitializationException( "Illegal files metadata type value '" + filesMetadataType + "' in the <files-metadata> element." );
				}
	
				// Set DAO data Source
				daoFactoryTemp.getDAODataSource().setId( Constants.XFORMSDB_CONFIG_DEFAULT_FILES_METADATA_ID );
				// Type has already been set
				daoFactoryTemp.getDAODataSource().setUri( ( ( "".equals( filesMetadataTO.getUri() ) == false ) ? filesMetadataTO.getUri() : this.getServletContext().getRealPath( "/" ) ) );
				daoFactoryTemp.getDAODataSource().setUsername( filesMetadataTO.getUsername() );
				daoFactoryTemp.getDAODataSource().setPassword( filesMetadataTO.getPassword() );
				daoFactoryTemp.getDAODataSource().setCollection( filesMetadataTO.getCollection() );
					
				// Set the files metadata DAO Factory
				defaultFilesMetadataSet			= true;
				daoFactories.put( Constants.XFORMSDB_CONFIG_DEFAULT_FILES_METADATA_ID, daoFactoryTemp );					
				logger.log( Level.DEBUG, "The files metadata DAO factory implementation has been successfully set." );
			}
			
			if ( defaultFilesMetadataSet == false ) {
				defaultFilesMetadataSet			= true;
				// Set the default files metadata DAO factory implementation
				daoFactoryTemp					= ( XMLDocumentDAOFactory ) DAOFactory.getDAOFactory( DAOFactory.XML_DOCUMENT );
				filesMetadataTO					= new FilesMetadataTO();
				daoFactoryTemp.getDAODataSource().setId( Constants.XFORMSDB_CONFIG_DEFAULT_FILES_METADATA_ID );
				// Type has already been set
				daoFactoryTemp.getDAODataSource().setUri( ( ( "".equals( filesMetadataTO.getUri() ) == false ) ? filesMetadataTO.getUri() : this.getServletContext().getRealPath( "/" ) ) );
				daoFactoryTemp.getDAODataSource().setUsername( filesMetadataTO.getUsername() );
				daoFactoryTemp.getDAODataSource().setPassword( filesMetadataTO.getPassword() );
				daoFactoryTemp.getDAODataSource().setCollection( filesMetadataTO.getCollection() );
				daoFactories.put( Constants.XFORMSDB_CONFIG_DEFAULT_FILES_METADATA_ID, daoFactoryTemp );					
				logger.log( Level.DEBUG, "The default files metadata DAO factory implementation has been successfully set." );
			}

			
			logger.log( Level.DEBUG, "The DAO factory implementations have been successfully set." );
		} catch ( InitializationException iex ) {
			throw iex;
		} catch ( Exception ex ) {
			throw new InitializationException( ErrorConstants.ERROR_CODE_INITIALIZATION_5, ErrorConstants.ERROR_MESSAGE_INITIALIZATION_5, ex );
		}
		
		
		return daoFactories;
	}
	
	
	// PUBLIC METHODS
	public void init() throws ServletException {
		// Load the log4j.properties file manually because the current version of Orbeon Forms prevents the log4j.properties file to be loaded automatically
		PropertyConfigurator.configure( Thread.currentThread().getContextClassLoader().getResource( "log4j.properties" ) );
		logger.log( Level.DEBUG, "Method has been called." );
		logger.log( Level.INFO, this.getServletInfo() );
		String serverURIEncoding				= null;
		XFormsDBConfigTO xformsdbConfigTO		= null;
		WebAppTO webAppTO						= null;
		Map<String, DAOFactory> daoFactories	= null;


		try {
			// Retrieve and initialize the value of the server URI encoding
			serverURIEncoding					= this.getAndInitServerURIEncoding();
			
			// Set the server URI encoding
			this.serverURIEncoding				= serverURIEncoding;

			// Retrieve and initialize the Web application file (web.xml)
			webAppTO							= this.getAndInitWebAppTO();

			// Retrieve and initialize the XFormsDB configuration file (conf.xml)
			xformsdbConfigTO					= this.getAndInitXFormsDBConfigTO();

			// Update the XFormsDB configuration file
			xformsdbConfigTO					= this.updateXFormsDBConfigTO( xformsdbConfigTO );

			// Set the XFormsDB configuration file
			this.xformsdbConfigTO				= xformsdbConfigTO;

			// Update the Web application file
			webAppTO							= this.updateWebAppTO( webAppTO, this.xformsdbConfigTO );

			// Set the Web application file
			this.webAppTO						= webAppTO;

			// Retrieve and initialize the DAO factories
			daoFactories						= this.getAndInitDAOFactories( xformsdbConfigTO );

			// Set the DAO factories into the servlet context
			this.daoFactories					= daoFactories;
			
			// Set the XSLT stylesheets
			this.xformsdbIncludeFilePath		= Constants.XSL_XFORMSDB_INCLUDE_FILE_PATH;
			this.xformsdbSecviewFilePath		= Constants.XSL_XFORMSDB_SECVIEW_FILE_PATH;
			this.xformsdbExtractFilePath		= Constants.XSL_XFORMSDB_EXTRACT_FILE_PATH;
			this.xformsdbXFormsFilePath			= Constants.XSL_XFORMSDB_XFORMS_FILE_PATH;
			this.indentFilePath					= Constants.XSL_INDENT_FILE_PATH;
		} catch ( InitializationException iex ) {
			this.iex							= iex;
			logger.log( Level.ERROR, "Error (" + this.iex.getCode() + "): " + this.iex.getMessage() );
		} catch ( Exception ex ) {
			this.iex							= new InitializationException( ErrorConstants.ERROR_CODE_INITIALIZATION_2, ErrorConstants.ERROR_MESSAGE_INITIALIZATION_2, ex );
			logger.log( Level.FATAL, "Error (" + this.iex.getCode() + "): " + this.iex.getMessage() );
		}
		
		
		this.rh									= new RequestHandler();
		this.ew									= new ErrorWriter();		
		logger.log( Level.INFO, "XFormsDB core servlet launched: " + this.getServletContext().getRealPath( "/" ) );
	}
	
	
	public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {		
		logger.log( Level.DEBUG, "Method has been called." );
		long beginTime								= 0;
		long endTime								= 0;
		long time									= 0;
		Enumeration requestHeaderNames				= null;
		String requestHeaderName					= null;
		Enumeration requestHeaderValues				= null;
		List<RequestHeaderTO> requestHeaderTOs		= null;
		RequestHeaderTO requestHeaderTO				= null;
		HttpSession session							= null;
		
		
		try {
			beginTime								= System.currentTimeMillis();
			
			// Retrieve/write the request headers
			requestHeaderNames						= request.getHeaderNames();

			// Iterate over request header names
			requestHeaderTOs						= new ArrayList<RequestHeaderTO>();
			while ( requestHeaderNames.hasMoreElements() ) {
				requestHeaderName					= ( String ) requestHeaderNames.nextElement();
				requestHeaderValues					= request.getHeaders( requestHeaderName );
				// Iterate over request header values of that particular request header name
				while ( requestHeaderValues.hasMoreElements() ) {
					requestHeaderTO					= new RequestHeaderTO( StringUtils.decode( StringUtils.encode( requestHeaderName, this.getServerURIEncoding() ), Constants.CLIENT_URI_ENCODING_DEFAULT ), StringUtils.decode( StringUtils.encode( ( String ) requestHeaderValues.nextElement(), this.getServerURIEncoding() ), Constants.CLIENT_URI_ENCODING_DEFAULT ) );
					requestHeaderTOs.add( requestHeaderTO );
				}
			}

			// Retrieve the current session or create one. Hack for enabling URL rewriting for the response
			session									= request.getSession();
			// Store HTTP request headers (GET) into the session
			HTTPRequestHeadersGetManager.setHTTPRequestHeadersGet( session, requestHeaderTOs );
			
			if ( this.iex == null ) {
				// Handle the request
				this.rh.handle( this, request, response );					
			}
			else {
				throw this.iex;
			}
		} catch ( InitializationException iex ) {
			try {
				String encoding						= Constants.XFORMSDB_CONFIG_DEFAULT_ENCODING;
				String mimeType						= Constants.XFORMSDB_CONFIG_DEFAULT_MIME_MAPPING_MIME_TYPE;

				// Write the error
				this.ew.write( Constants.ERROR_FORMAT_XHTML, response, request, encoding, mimeType, this.getServerURIEncoding(), iex );
			} catch ( Exception ex ) {
				throw new ServletException( ExceptionUtils.getMessage( ex ), ExceptionUtils.getCause( ex ) );
			}
		} catch ( HandlerException hex ) {
			try {
				String encoding						= null;
				String mimeType						= null;
				
				if ( this.getXFormsDBConfigTO() != null ) {
					encoding						= this.getXFormsDBConfigTO().getEncoding();
					mimeType						= this.getXFormsDBConfigTO().getMimeMappingTOs().get( 0 ).getMimeType();
				}
				else {
					encoding						= Constants.XFORMSDB_CONFIG_DEFAULT_ENCODING;
					mimeType						= Constants.XFORMSDB_CONFIG_DEFAULT_MIME_MAPPING_MIME_TYPE;
				}

				// Write the error
				this.ew.write( Constants.ERROR_FORMAT_XHTML, response, request, encoding, mimeType, this.getServerURIEncoding(), hex );
			} catch ( Exception ex ) {
				throw new ServletException( ExceptionUtils.getMessage( ex ), ExceptionUtils.getCause( ex ) );
			}
		} catch ( Exception ex ) {
			throw new ServletException( ExceptionUtils.getMessage( ex ), ExceptionUtils.getCause( ex ) );
		} finally {
			endTime									= System.currentTimeMillis();
			time									= endTime - beginTime;
			if ( request.getQueryString() != null ) {
				logger.log( Level.INFO, "XFormsDB GET request took " + time + "ms: " + URLDecoder.decode( request.getRequestURL().toString() + "?" + request.getQueryString(), Constants.CLIENT_URI_ENCODING_DEFAULT ) );							
			}
			else {
				logger.log( Level.INFO, "XFormsDB GET request took " + time + "ms: " + URLDecoder.decode( request.getRequestURL().toString(), Constants.CLIENT_URI_ENCODING_DEFAULT ) );							
			}
		}
	}
	
	
	public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {		
		logger.log( Level.DEBUG, "Method has been called." );
		long beginTime								= 0;
		long endTime								= 0;
		long time									= 0;

		
		try {
			beginTime								= System.currentTimeMillis();
			// Retrieve the current session or create one. Hack for enabling URL encoding for the response
			request.getSession();
			
			if ( this.iex == null ) {
				// Handle the request
				this.rh.handle( this, request, response );					
			}
			else {
				throw this.iex;
			}
		} catch ( InitializationException iex ) {
			try {
				String encoding						= Constants.XFORMSDB_CONFIG_DEFAULT_ENCODING;
				String mimeType						= Constants.XFORMSDB_CONFIG_DEFAULT_MIME_MAPPING_MIME_TYPE;

				// Write the error
				this.ew.write( Constants.ERROR_FORMAT_XML, response, request, encoding, mimeType, this.getServerURIEncoding(), iex );
			} catch ( Exception ex ) {
				throw new ServletException( ExceptionUtils.getMessage( ex ), ExceptionUtils.getCause( ex ) );
			}
		} catch ( HandlerException hex ) {
			try {
				String encoding						= null;
				String mimeType						= null;
				
				if ( this.getXFormsDBConfigTO() != null ) {
					encoding						= this.getXFormsDBConfigTO().getEncoding();
					mimeType						= Constants.MEDIA_TYPE_APPLICATION_XML;
				}
				else {
					encoding						= Constants.XFORMSDB_CONFIG_DEFAULT_ENCODING;
					mimeType						= Constants.MEDIA_TYPE_APPLICATION_XML;
				}
				
				// Write the error
				this.ew.write( Constants.ERROR_FORMAT_XML, response, request, encoding, mimeType, this.getServerURIEncoding(), hex );
			} catch ( Exception ex ) {
				throw new ServletException( ExceptionUtils.getMessage( ex ), ExceptionUtils.getCause( ex ) );
			}
		} catch ( Exception ex ) {
			throw new ServletException( ExceptionUtils.getMessage( ex ), ExceptionUtils.getCause( ex ) );
		} finally {
			endTime									= System.currentTimeMillis();
			time									= endTime - beginTime;
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
		// Retrieve the current session or create one. Hack for enabling URL encoding for the response
		request.getSession();
	}
	
	
	public void doDelete( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {		
		logger.log( Level.DEBUG, "Method has been called." );
		// Retrieve the current session or create one. Hack for enabling URL encoding for the response
		request.getSession();
	}
	
	
	public void destroy() {
		logger.log( Level.DEBUG, "Method has been called." );
	}
	public String getServletInfo() {
		logger.log( Level.DEBUG, "Method has been called." );
		return "XFormsDB servlet version 1.0, Copyright (c) 2009 The XFormsDB Project";
	}
	

	public String getServerURIEncoding() {
		return this.serverURIEncoding;
	}

	
	public WebAppTO getWebAppTO() {
		return this.webAppTO;
	}

	
	public XFormsDBConfigTO getXFormsDBConfigTO() {
		return this.xformsdbConfigTO;
	}


	public Map<String, DAOFactory> getDAOFactories() {
		return this.daoFactories;
	}

	
	public String getXFormsDBIncludeFilePath() {
		return this.xformsdbIncludeFilePath;
	}
	
	
	public String getXFormsDBSecviewFilePath() {
		return this.xformsdbSecviewFilePath;
	}

	
	public String getXFormsDBExtractFilePath() {
		return this.xformsdbExtractFilePath;
	}
	
	
	public String getXFormsDBXFormsFilePath() {
		return this.xformsdbXFormsFilePath;
	}
	
	
	public String getIndentFilePath() {
		return this.indentFilePath;
	}
}