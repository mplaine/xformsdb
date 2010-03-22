package fi.tkk.tml.xformsdb.handler;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fi.tkk.tml.xformsdb.core.Constants;
import fi.tkk.tml.xformsdb.error.ErrorConstants;
import fi.tkk.tml.xformsdb.error.HandlerException;
import fi.tkk.tml.xformsdb.error.ManagerException;
import fi.tkk.tml.xformsdb.error.TransformerException;
import fi.tkk.tml.xformsdb.manager.xformsdb.XFormsDBFileManager;
import fi.tkk.tml.xformsdb.manager.xformsdb.XFormsDBLoginManager;
import fi.tkk.tml.xformsdb.manager.xformsdb.XFormsDBQueryManager;
import fi.tkk.tml.xformsdb.manager.xformsdb.XFormsDBWidgetQueryManager;
import fi.tkk.tml.xformsdb.servlet.XFormsDBServlet;
import fi.tkk.tml.xformsdb.transformer.xformsdb.XFormsDBCookieTransformer;
import fi.tkk.tml.xformsdb.transformer.xformsdb.XFormsDBFileTransformer;
import fi.tkk.tml.xformsdb.transformer.xformsdb.XFormsDBLoginTransformer;
import fi.tkk.tml.xformsdb.transformer.xformsdb.XFormsDBLogoutTransformer;
import fi.tkk.tml.xformsdb.transformer.xformsdb.XFormsDBQueryTransformer;
import fi.tkk.tml.xformsdb.transformer.xformsdb.XFormsDBStateTransformer;
import fi.tkk.tml.xformsdb.transformer.xformsdb.XFormsDBUserTransformer;
import fi.tkk.tml.xformsdb.transformer.xformsdb.XFormsDBWidgetQueryTransformer;
import fi.tkk.tml.xformsdb.util.IOUtils;
import fi.tkk.tml.xformsdb.util.StringUtils;
import fi.tkk.tml.xformsdb.xml.to.xformsdb.MimeMappingTO;
import fi.tkk.tml.xformsdb.xml.to.xformsdb.XFormsDBConfigTO;


/**
 * Handle POST requests having the
 * content type: application/xml.
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on November 14, 2009
 */
public class PostApplicationXMLHandler extends ResponseHandler {
	
	
	
	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger			= Logger.getLogger( PostApplicationXMLHandler.class );

	
	// PRIVATE VARIABLES
	private XFormsDBQuerySelectHandler xformsdbQuerySelectHandler;
	private XFormsDBQuerySelect4UpdateHandler xformsdbQuerySelect4UpdateHandler;
	private XFormsDBQueryUpdateHandler xformsdbQueryUpdateHandler;
	private XFormsDBQueryAllHandler xformsdbQueryAllHandler;
	private XFormsDBWidgetQuerySelectHandler xformsdbWidgetQuerySelectHandler;
	private XFormsDBWidgetQuerySelect4UpdateHandler xformsdbWidgetQuerySelect4UpdateHandler;
	private XFormsDBWidgetQueryUpdateHandler xformsdbWidgetQueryUpdateHandler;
	private XFormsDBWidgetQueryAllHandler xformsdbWidgetQueryAllHandler;
	private XFormsDBStateGetHandler xformsdbStateGetHandler;
	private XFormsDBStateSetHandler xformsdbStateSetHandler;
	private XFormsDBLoginHandler xformsdbLoginHandler;
	private XFormsDBLogoutHandler xformsdbLogoutHandler;
	private XFormsDBUserHandler xformsdbUserHandler;
	private XFormsDBFileSelectHandler xformsdbFileSelectHandler;
	private XFormsDBFileInsertHandler xformsdbFileInsertHandler;
	private XFormsDBFileDeleteHandler xformsdbFileDeleteHandler;
	private XFormsDBFileUpdateHandler xformsdbFileUpdateHandler;
	private XFormsDBCookieHandler xformsdbCookieHandler;
	
	
	// PUBLIC CONSTURCTORS
	public PostApplicationXMLHandler() {
		super();
		logger.log( Level.DEBUG, "Constructor has been called." );
		this.xformsdbQuerySelectHandler					= new XFormsDBQuerySelectHandler();
		this.xformsdbQuerySelect4UpdateHandler			= new XFormsDBQuerySelect4UpdateHandler();
		this.xformsdbQueryUpdateHandler					= new XFormsDBQueryUpdateHandler();
		this.xformsdbQueryAllHandler					= new XFormsDBQueryAllHandler();
		this.xformsdbWidgetQuerySelectHandler			= new XFormsDBWidgetQuerySelectHandler();
		this.xformsdbWidgetQuerySelect4UpdateHandler	= new XFormsDBWidgetQuerySelect4UpdateHandler();
		this.xformsdbWidgetQueryUpdateHandler			= new XFormsDBWidgetQueryUpdateHandler();
		this.xformsdbWidgetQueryAllHandler				= new XFormsDBWidgetQueryAllHandler();
		this.xformsdbStateGetHandler					= new XFormsDBStateGetHandler();
		this.xformsdbStateSetHandler					= new XFormsDBStateSetHandler();
		this.xformsdbLoginHandler						= new XFormsDBLoginHandler();
		this.xformsdbLogoutHandler						= new XFormsDBLogoutHandler();
		this.xformsdbUserHandler						= new XFormsDBUserHandler();
		this.xformsdbFileSelectHandler					= new XFormsDBFileSelectHandler();
		this.xformsdbFileInsertHandler					= new XFormsDBFileInsertHandler();
		this.xformsdbFileDeleteHandler					= new XFormsDBFileDeleteHandler();
		this.xformsdbFileUpdateHandler					= new XFormsDBFileUpdateHandler();
		this.xformsdbCookieHandler						= new XFormsDBCookieHandler();
	}
	
	
	// PRIVATE METHODS
	private String getDecodedQueryString( HttpServletRequest request ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		String decodedQueryString	= "";
		
		
		try {
			// Create decoded query string
			if ( request.getQueryString() != null ) {
				decodedQueryString	= "?" + URLDecoder.decode( request.getQueryString(), Constants.CLIENT_URI_ENCODING_DEFAULT );
			}

			logger.log( Level.DEBUG, "The decoded query string has been successfully retrieved." );
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_GETNULLHANDLER_25, ErrorConstants.ERROR_MESSAGE_GETNULLHANDLER_25, ex  );
		}
		
		
		return decodedQueryString;
	}
	
	
	private String getDecodedRequestParameterValue( HttpServletRequest request, String parameterName, boolean parameterNameInJavaDefaultEncoding, String serverURIEncoding ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		String convertedParameterName					= null;
		String decodedParameterValue					= null;
		
		
		try {
			if ( parameterNameInJavaDefaultEncoding ) {
				// Decode the name of the request parameter
				convertedParameterName					= StringUtils.decode( StringUtils.encode( parameterName, Constants.CLIENT_URI_ENCODING_DEFAULT ), serverURIEncoding );
			}
			else {
				convertedParameterName					= parameterName;				
			}

			// Decode the value of the request parameter
			decodedParameterValue						= StringUtils.decode( StringUtils.encode( request.getParameter( convertedParameterName ), serverURIEncoding ), Constants.CLIENT_URI_ENCODING_DEFAULT );				
			logger.log( Level.DEBUG, "The decoded value of a request parameter has been successfully retrieved." );
		} catch ( NullPointerException npex ) {
			decodedParameterValue						= null;
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_GETNULLHANDLER_10, ErrorConstants.ERROR_MESSAGE_GETNULLHANDLER_10, ex  );
		}
		
		
		return decodedParameterValue;
	}

	
	private String getPostedXmlXom( HttpServletRequest request, String encoding ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		String postedXmlXom			= null;
		
		
		try {
			// Retrieve the posted XML (xom)
			postedXmlXom			= IOUtils.convert( request.getInputStream(), encoding, true );
			logger.log( Level.DEBUG, "Posted XML (xom):" + Constants.LINE_SEPARATOR + postedXmlXom );
			logger.log( Level.DEBUG, "The posted XML (xom) has been successfully retrieved." );
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_POSTAPPLICATIONXMLHANDLER_1, ErrorConstants.ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_1, ex  );
		}
		
		
		return postedXmlXom;
	}
	
	
	private String handleXFormsDBState( XFormsDBServlet xformsdbServlet, HttpSession session, Document document, String stateType ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		String xformsdbState	= null;
		
		
		try {
			// Handle the correct <xformsdb:state> type
			if ( Constants.STATE_TYPE_GET.equals( stateType ) == true ) {
				xformsdbState	= this.xformsdbStateGetHandler.handle( xformsdbServlet, session );
			}
			else if ( Constants.STATE_TYPE_SET.equals( stateType ) == true ) {
				xformsdbState	= this.xformsdbStateSetHandler.handle( xformsdbServlet, session, document );
			}
			logger.log( Level.DEBUG, "The <xformsdb:state> request has been successfully handled." );
		} catch ( HandlerException hex ) {
			throw hex;
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_POSTAPPLICATIONXMLHANDLER_17, ErrorConstants.ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_17, ex  );
		}
		
		
		return xformsdbState;
	}

	
	private String handleXFormsDBQuery( XFormsDBServlet xformsdbServlet, HttpSession session, Document document ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		String xmlString		= null;
		

		try {
			// Handle the correct action
			if ( Constants.EXPRESSION_TYPE_SELECT.equals( document.getRootElement().getAttributeValue( "type" ) ) == true ) {
				xmlString		= this.xformsdbQuerySelectHandler.handle( xformsdbServlet, document );
			}
			else if ( Constants.EXPRESSION_TYPE_SELECT4UPDATE.equals( document.getRootElement().getAttributeValue( "type" ) ) == true ) {
				xmlString		= this.xformsdbQuerySelect4UpdateHandler.handle( xformsdbServlet, session, document );
			}
			else if ( Constants.EXPRESSION_TYPE_UPDATE.equals( document.getRootElement().getAttributeValue( "type" ) ) == true ) {
				xmlString		= this.xformsdbQueryUpdateHandler.handle( xformsdbServlet, session, document );
			}
			else if ( Constants.EXPRESSION_TYPE_ALL.equals( document.getRootElement().getAttributeValue( "type" ) ) == true ) {
				xmlString		= this.xformsdbQueryAllHandler.handle( xformsdbServlet, document );
			}
			else {
				throw new HandlerException( ErrorConstants.ERROR_CODE_POSTAPPLICATIONXMLHANDLER_7, ErrorConstants.ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_7 + document.getRootElement().getAttributeValue( "type" ) + "." );
			}
			logger.log( Level.DEBUG, "The <xformsdb:query> request has been successfully handled." );
		} catch ( HandlerException hex ) {
			throw hex;
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_POSTAPPLICATIONXMLHANDLER_8, ErrorConstants.ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_8, ex  );
		}
		
		
		return xmlString;
	}

	
	private String handleXFormsDBWidgetQuery( XFormsDBServlet xformsdbServlet, HttpSession session, Document document ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		String xmlString		= null;
		

		try {
			// Handle the correct action
			if ( Constants.EXPRESSION_TYPE_SELECT.equals( document.getRootElement().getAttributeValue( "type" ) ) == true ) {
				xmlString		= this.xformsdbWidgetQuerySelectHandler.handle( xformsdbServlet, session, document );
			}
			else if ( Constants.EXPRESSION_TYPE_SELECT4UPDATE.equals( document.getRootElement().getAttributeValue( "type" ) ) == true ) {
				xmlString		= this.xformsdbWidgetQuerySelect4UpdateHandler.handle( xformsdbServlet, session, document );
			}
			else if ( Constants.EXPRESSION_TYPE_UPDATE.equals( document.getRootElement().getAttributeValue( "type" ) ) == true ) {
				xmlString		= this.xformsdbWidgetQueryUpdateHandler.handle( xformsdbServlet, session, document );
			}
			else if ( Constants.EXPRESSION_TYPE_ALL.equals( document.getRootElement().getAttributeValue( "type" ) ) == true ) {
				xmlString		= this.xformsdbWidgetQueryAllHandler.handle( xformsdbServlet, session, document );
			}
			else {
				throw new HandlerException( ErrorConstants.ERROR_CODE_POSTAPPLICATIONXMLHANDLER_39, ErrorConstants.ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_39 + document.getRootElement().getAttributeValue( "type" ) + "." );
			}
			logger.log( Level.DEBUG, "The <xformsdb:widgetquery> request has been successfully handled." );
		} catch ( HandlerException hex ) {
			throw hex;
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_POSTAPPLICATIONXMLHANDLER_40, ErrorConstants.ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_40, ex  );
		}
		
		
		return xmlString;
	}

	
	private String handleXFormsDBUser( XFormsDBServlet xformsdbServlet, HttpSession session ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		String xformsdbUser	= null;
		
		
		try {
			// Handle the <xformsdb:user>
			xformsdbUser	= this.xformsdbUserHandler.handle( xformsdbServlet, session );
			logger.log( Level.DEBUG, "The <xformsdb:user> request has been successfully handled." );
		} catch ( HandlerException hex ) {
			throw hex;
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_POSTAPPLICATIONXMLHANDLER_23, ErrorConstants.ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_23, ex  );
		}
		
		
		return xformsdbUser;
	}

	
	private Element getActiveXFormsDBLoginElement( HttpSession session, Document document ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		Element activeXFormsDBLoginElement	= null;
		
		
		try {
			// Retrieve the active <xformsdb:login> element (stored)
			activeXFormsDBLoginElement		= XFormsDBLoginManager.getActiveXFormsDBLoginElement( session, document.getRootElement().getAttributeValue( "id" ) );
			
			if ( activeXFormsDBLoginElement == null ) {
				throw new HandlerException( ErrorConstants.ERROR_CODE_POSTAPPLICATIONXMLHANDLER_19, ErrorConstants.ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_19_1 + document.getRootElement().getAttributeValue( "id" ) + ErrorConstants.ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_19_2 );	
			}
			logger.log( Level.DEBUG, "The active <xformsdb:login> element (stored) has been successfully retrieved." );
		} catch ( ManagerException mex ) {
			throw new HandlerException( mex.getCode(), mex.getMessage(), mex );
		} catch ( HandlerException hex ) {
			throw hex;
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_POSTAPPLICATIONXMLHANDLER_20, ErrorConstants.ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_20, ex  );
		}
		
		
		return activeXFormsDBLoginElement;
	}

	
	private Element getActiveXFormsDBQueryElement( HttpSession session, Document document ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		Element activeXFormsDBQueryElement	= null;
		
		
		try {
			// Retrieve the active <xformsdb:query> (stored)
			activeXFormsDBQueryElement		= XFormsDBQueryManager.getActiveXFormsDBQueryElement( session, document.getRootElement().getAttributeValue( "id" ) );
			
			if ( activeXFormsDBQueryElement == null ) {
				throw new HandlerException( ErrorConstants.ERROR_CODE_POSTAPPLICATIONXMLHANDLER_11, ErrorConstants.ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_11_1 + document.getRootElement().getAttributeValue( "id" ) + ErrorConstants.ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_11_2 );	
			}
			logger.log( Level.DEBUG, "The active <xformsdb:query> element (stored) has been successfully retrieved." );
		} catch ( ManagerException mex ) {
			throw new HandlerException( mex.getCode(), mex.getMessage(), mex );
		} catch ( HandlerException hex ) {
			throw hex;
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_POSTAPPLICATIONXMLHANDLER_4, ErrorConstants.ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_4, ex  );
		}
		
		
		return activeXFormsDBQueryElement;
	}

	
	private Element getActiveXFormsDBWidgetQueryElement( HttpSession session, Document document ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		Element activeXFormsDBWidgetQueryElement	= null;
		
		
		try {
			// Retrieve the active <xformsdb:widgetquery> (stored)
			activeXFormsDBWidgetQueryElement		= XFormsDBWidgetQueryManager.getActiveXFormsDBWidgetQueryElement( session, document.getRootElement().getAttributeValue( "id" ) );
			
			if ( activeXFormsDBWidgetQueryElement == null ) {
				throw new HandlerException( ErrorConstants.ERROR_CODE_POSTAPPLICATIONXMLHANDLER_37, ErrorConstants.ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_37_1 + document.getRootElement().getAttributeValue( "id" ) + ErrorConstants.ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_37_2 );	
			}
			logger.log( Level.DEBUG, "The active <xformsdb:widgetquery> element (stored) has been successfully retrieved." );
		} catch ( ManagerException mex ) {
			throw new HandlerException( mex.getCode(), mex.getMessage(), mex );
		} catch ( HandlerException hex ) {
			throw hex;
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_POSTAPPLICATIONXMLHANDLER_38, ErrorConstants.ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_38, ex  );
		}
		
		
		return activeXFormsDBWidgetQueryElement;
	}

	
	private Element getActiveXFormsDBFileElement( HttpSession session, Document document ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		Element activeXFormsDBFileElement	= null;
		
		
		try {
			// Retrieve the active <xformsdb:file> element (stored)
			activeXFormsDBFileElement		= XFormsDBFileManager.getActiveXFormsDBFileElement( session, document.getRootElement().getAttributeValue( "id" ) );
			
			if ( activeXFormsDBFileElement == null ) {
				throw new HandlerException( ErrorConstants.ERROR_CODE_POSTAPPLICATIONXMLHANDLER_19, ErrorConstants.ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_32_1 + document.getRootElement().getAttributeValue( "id" ) + ErrorConstants.ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_32_2 );	
			}
			logger.log( Level.DEBUG, "The active <xformsdb:file> element (stored) has been successfully retrieved." );
		} catch ( ManagerException mex ) {
			throw new HandlerException( mex.getCode(), mex.getMessage(), mex );
		} catch ( HandlerException hex ) {
			throw hex;
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_POSTAPPLICATIONXMLHANDLER_33, ErrorConstants.ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_33, ex  );
		}
		
		
		return activeXFormsDBFileElement;
	}


	private String handleXFormsDBLogin( XFormsDBServlet xformsdbServlet, HttpSession session, Document document ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		String xformsdbLogin	= null;
		
		
		try {
			// Handle the <xformsdb:login>
			xformsdbLogin		= this.xformsdbLoginHandler.handle( xformsdbServlet, session, document );
			logger.log( Level.DEBUG, "The <xformsdb:login> request has been successfully handled." );
		} catch ( HandlerException hex ) {
			throw hex;
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_POSTAPPLICATIONXMLHANDLER_18, ErrorConstants.ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_18, ex  );
		}
		
		
		return xformsdbLogin;
	}
	
	
	private String handleXFormsDBLogout( XFormsDBServlet xformsdbServlet, HttpSession session, Document document ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		String xformsdbLogout	= null;
		
		
		try {
			// Handle the <xformsdb:logout>
			xformsdbLogout		= this.xformsdbLogoutHandler.handle( xformsdbServlet, session, document );
			logger.log( Level.DEBUG, "The <xformsdb:logout> request has been successfully handled." );
		} catch ( HandlerException hex ) {
			throw hex;
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_POSTAPPLICATIONXMLHANDLER_25, ErrorConstants.ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_25, ex  );
		}
		
		
		return xformsdbLogout;
	}

	
	private String handleXFormsDBFile( XFormsDBServlet xformsdbServlet, String actionURL, Document document ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		String xformsdbFile		= null;
		
		
		try {
			// Handle the <xformsdb:file>
			if ( Constants.FILE_TYPE_SELECT.equals( document.getRootElement().getAttributeValue( "type" ) ) == true ) {
				xformsdbFile		= this.xformsdbFileSelectHandler.handle( xformsdbServlet, actionURL, document );
			}
			else if ( Constants.FILE_TYPE_INSERT.equals( document.getRootElement().getAttributeValue( "type" ) ) == true ) {
				xformsdbFile		= this.xformsdbFileInsertHandler.handle( xformsdbServlet, actionURL, document );
			}
			else if ( Constants.FILE_TYPE_DELETE.equals( document.getRootElement().getAttributeValue( "type" ) ) == true ) {
				xformsdbFile		= this.xformsdbFileDeleteHandler.handle( xformsdbServlet, actionURL, document );
			}
			else if ( Constants.FILE_TYPE_UPDATE.equals( document.getRootElement().getAttributeValue( "type" ) ) == true ) {
				xformsdbFile		= this.xformsdbFileUpdateHandler.handle( xformsdbServlet, actionURL, document );
			}
			else {
				throw new HandlerException( ErrorConstants.ERROR_CODE_POSTAPPLICATIONXMLHANDLER_35, ErrorConstants.ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_35 + document.getRootElement().getAttributeValue( "type" ) + "." );
			}
			logger.log( Level.DEBUG, "The <xformsdb:file> request has been successfully handled." );
		} catch ( HandlerException hex ) {
			throw hex;
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_POSTAPPLICATIONXMLHANDLER_34, ErrorConstants.ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_34, ex  );
		}
		
		
		return xformsdbFile;
	}

	
	private String handleXFormsDBCookie( XFormsDBServlet xformsdbServlet, Cookie[] cookies ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		String xformsdbCookie	= null;
		
		
		try {
			// Handle the <xformsdb:cookie>
			xformsdbCookie		= this.xformsdbCookieHandler.handle( xformsdbServlet, cookies );
			logger.log( Level.DEBUG, "The <xformsdb:cookie> request has been successfully handled." );
		} catch ( HandlerException hex ) {
			throw hex;
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_POSTAPPLICATIONXMLHANDLER_36, ErrorConstants.ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_36, ex  );
		}
		
		
		return xformsdbCookie;
	}

	
	private void writeResponse( HttpServletResponse response, HttpServletRequest request, String charsetName, String xformsDBEncoding, String xmlString ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		
		
		try {
			// Write the response
			super.handle( response, request, Constants.MIME_TYPE_APPLICATION_XML, charsetName, xformsDBEncoding, IOUtils.convert( xmlString, xformsDBEncoding ), true );
			logger.log( Level.DEBUG, "The response has been successfully written." );
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_POSTAPPLICATIONXMLHANDLER_9, ErrorConstants.ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_9, ex  );
		}
	}
	
	
	
	
	
	
	private Document buildXOM( String postedXmlXom, String encoding ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		Builder builder		= null;
		Document document	= null;
		
		
		try {
			// Parse the posted XML document in order to build the XOM document object			
			builder			= new Builder();
			document		= builder.build( IOUtils.convert( postedXmlXom, encoding ) );
			logger.log( Level.DEBUG, "The XOM document object has been successfully built." );
		} catch ( IOException ioex ) {
			ioex.printStackTrace();
		} catch ( ValidityException vex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_POSTAPPLICATIONXMLHANDLER_28, "Validity exception" + ", line " + vex.getLineNumber() + ", column " + vex.getColumnNumber() + ", uri " + vex.getURI() + ". " + vex.getMessage(), vex );
		} catch ( ParsingException pex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_POSTAPPLICATIONXMLHANDLER_29, "Parsing exception" + ", line " + pex.getLineNumber() + ", column " + pex.getColumnNumber() + ", uri " + pex.getURI() + ". " + pex.getMessage(), pex );
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_POSTAPPLICATIONXMLHANDLER_30, ErrorConstants.ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_30, ex );
		}
		
		
		return document;
	}

	
	// PUBLIC METHODS
	public void handle( XFormsDBServlet xformsdbServlet, HttpServletRequest request, HttpServletResponse response ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		XFormsDBConfigTO xformsdbConfigTO					= null;
		String requestURL									= null;
		String requestURI									= null;
		String contextPath									= null;
		String requestedFilePath							= null;
		String realRequestedFilePath						= null;
		int requestedFileExtensionIndex						= 0;
		String requestedFileExtension						= null;
		String queryString									= null;
		String replaceType									= null;
		MimeMappingTO mimeMappingTO							= null;
		MimeMappingTO mimeMappingTOTemp						= null;
		String expressionType								= null;
		String stateType									= null;
		String fileType										= null;
		String encoding										= null;
		String postedXmlXom									= null;
		Document document									= null;
		String localName									= null;
		String namespaceURI									= null;
		Element activeXFormsDBLoginElement					= null;
		Element activeXFormsDBQueryElement					= null;
		Element activeXFormsDBWidgetQueryElement			= null;
		Element activeXFormsDBFileElement					= null;
		InputStream inputStream								= null;
		String xmlString									= null;
		
		
		
		try {			
			// Retrieve the XFormsDB configuration file
			xformsdbConfigTO								= xformsdbServlet.getXFormsDBConfigTO();

			// Retrieve the XFormsDB encoding
			encoding										= xformsdbConfigTO.getEncoding();

			// Retrieve the real request file path
			requestURL										= URLDecoder.decode( request.getRequestURL().toString(), Constants.CLIENT_URI_ENCODING_DEFAULT ); // Without query string
			requestURI										= URLDecoder.decode( request.getRequestURI(), Constants.CLIENT_URI_ENCODING_DEFAULT ); // Without query string
			contextPath										= URLDecoder.decode( request.getContextPath(), Constants.CLIENT_URI_ENCODING_DEFAULT ); // Without query string
			requestedFilePath								= requestURI.substring( contextPath.length(), requestURI.length() ); // Request URI - context path
			realRequestedFilePath							= xformsdbServlet.getServletContext().getRealPath( requestedFilePath ); // File system path
			requestedFileExtensionIndex						= requestedFilePath.lastIndexOf( "." );
			if ( requestedFileExtensionIndex != -1 ) {
				requestedFileExtension						= requestedFilePath.substring( requestedFileExtensionIndex + 1 );				
			}
			queryString										= this.getDecodedQueryString( request ); // Query string
			logger.log( Level.DEBUG, "The real requested file path: " + realRequestedFilePath );
			logger.log( Level.DEBUG, "The request URL: " + requestURL + queryString );				

			
			// Set MIME mapping
			if ( requestedFileExtension != null ) {
				for ( int i = 0; i < xformsdbConfigTO.getMimeMappingTOs().size(); i++ ) {
					mimeMappingTOTemp						= xformsdbConfigTO.getMimeMappingTOs().get( i );
					// Find a valid MIME mapping --> use it
					if ( ( ( mimeMappingTOTemp.getExtension() ).equals( requestedFileExtension ) == true ) || ( ( mimeMappingTOTemp.getExtension() + "upload" ).equals( requestedFileExtension ) == true ) ) {
						logger.log( Level.DEBUG, "Found a valid MIME mapping (extension: " + mimeMappingTOTemp.getExtension() + ") from XFormsDB MIME mappings." );
						mimeMappingTO						= mimeMappingTOTemp;
						break;
					}
				}
			}			
			// Did not found a valid MIME mapping --> use default
			if ( mimeMappingTO == null ) {
				mimeMappingTO								= xformsdbConfigTO.getMimeMappingTOs().get( 0 );
				logger.log( Level.DEBUG, "Did not found a valid MIME mapping from XFormsDB MIME mappings. Use the default XFormsDB MIME mapping (extension: " + mimeMappingTO.getExtension() + ")." );
			}


			// Retrieve the replacetype request parameter
			replaceType										= this.getDecodedRequestParameterValue( request, Constants.REQUEST_PARAMETER_REPLACE_TYPE, true, xformsdbServlet.getServerURIEncoding() );

			// Set the replacetype into the request
			request.setAttribute( Constants.REQUEST_ATTRIBUTE_REPLACETYPE, replaceType );
			
			
			// Not an XFormsDB upload request
			if ( ( mimeMappingTO.getExtension() + "upload" ).equals( requestedFileExtension ) == false ) {
				logger.log( Level.DEBUG, "The real requested file path (" + realRequestedFilePath + ") is not for uploading a file to XFormsDB." );				
				// Retrieve the expressiontype request parameter
				expressionType								= this.getDecodedRequestParameterValue( request, Constants.REQUEST_PARAMETER_EXPRESSION_TYPE, true, xformsdbServlet.getServerURIEncoding() );

				// Retrieve the statetype request parameter
				stateType									= this.getDecodedRequestParameterValue( request, Constants.REQUEST_PARAMETER_STATE_TYPE, true, xformsdbServlet.getServerURIEncoding() );

				// Retrieve the filetype request parameter
				fileType									= this.getDecodedRequestParameterValue( request, Constants.REQUEST_PARAMETER_FILE_TYPE, true, xformsdbServlet.getServerURIEncoding() );
	
				// Retrieve the posted XML (xom)
				postedXmlXom								= this.getPostedXmlXom( request, encoding );
	
				// Build the XOM document
				document									= this.buildXOM( postedXmlXom, encoding );
				logger.log( Level.DEBUG, "The XOM document:" + Constants.LINE_SEPARATOR + document.toXML() );
	
				// Resolve the XFormsDB request type
				localName									= document.getRootElement().getLocalName();
				namespaceURI								= document.getRootElement().getNamespaceURI();
				if ( Constants.NAMESPACE_URI_XFORMSDB.equals( namespaceURI ) == true && "query".equals( localName ) == true ) {
					// Retrieve the active <xformsdb:query> element (stored)
					activeXFormsDBQueryElement				= this.getActiveXFormsDBQueryElement( request.getSession(), document );
	
					// Update the XOM document (<xformsdb:query> element)
					XFormsDBQueryTransformer.transform( request.getSession(), document, activeXFormsDBQueryElement, expressionType, encoding  );
	
					// Handle the <xformsdb:query> request
					xmlString								= this.handleXFormsDBQuery( xformsdbServlet, request.getSession(), document );
				}
				else if ( Constants.NAMESPACE_URI_XFORMSDB.equals( namespaceURI ) == true && "widgetquery".equals( localName ) == true ) {
					// Retrieve the active <xformsdb:widgetquery> element (stored)
					activeXFormsDBWidgetQueryElement		= this.getActiveXFormsDBWidgetQueryElement( request.getSession(), document );
	
					// Update the XOM document (<xformsdb:widgetquery> element)
					XFormsDBWidgetQueryTransformer.transform( request.getSession(), document, activeXFormsDBWidgetQueryElement, expressionType, encoding  );
	
					// Handle the <xformsdb:widgetquery> request
					xmlString								= this.handleXFormsDBWidgetQuery( xformsdbServlet, request.getSession(), document );
				}
				else if ( Constants.NAMESPACE_URI_XFORMSDB.equals( namespaceURI ) == true && "state".equals( localName ) == true ) {
					// Update the XOM document (<xformsdb:state> element)
					XFormsDBStateTransformer.transform( document, stateType );
					
					// Handle the <xformsdb:state> request
					xmlString								= this.handleXFormsDBState( xformsdbServlet, request.getSession(), document, stateType );
				}
				else if ( Constants.NAMESPACE_URI_XFORMSDB.equals( namespaceURI ) == true && "login".equals( localName ) == true ) {
					// Retrieve the active <xformsdb:login> element (stored)
					activeXFormsDBLoginElement				= this.getActiveXFormsDBLoginElement( request.getSession(), document );
		
					// Update the XOM document (<xformsdb:login> element)
					XFormsDBLoginTransformer.transform( document, activeXFormsDBLoginElement );
		
					// Handle the <xformsdb:login> request
					xmlString								= this.handleXFormsDBLogin( xformsdbServlet, request.getSession(), document );
				}
				else if ( Constants.NAMESPACE_URI_XFORMSDB.equals( namespaceURI ) == true && "logout".equals( localName ) == true ) {
					// Update the XOM document (<xformsdb:logout> element)
					XFormsDBLogoutTransformer.transform( document );
					
					// Handle the <xformsdb:logout> request
					xmlString								= this.handleXFormsDBLogout( xformsdbServlet, request.getSession(), document );
				}
				else if ( Constants.NAMESPACE_URI_XFORMSDB.equals( namespaceURI ) == true && "user".equals( localName ) == true ) {
					// Update the XOM document (<xformsdb:user> element)
					XFormsDBUserTransformer.transform( document );
					
					// Handle the <xformsdb:user> request
					xmlString								= this.handleXFormsDBUser( xformsdbServlet, request.getSession() );
				}
				else if ( Constants.NAMESPACE_URI_XFORMSDB.equals( namespaceURI ) == true && "file".equals( localName ) == true ) {
					// Retrieve the active <xformsdb:file> element (stored)
					activeXFormsDBFileElement				= this.getActiveXFormsDBFileElement( request.getSession(), document );
		
					// Update the XOM document (<xformsdb:file> element)
					XFormsDBFileTransformer.transform( xformsdbServlet, request.getSession(), document, activeXFormsDBFileElement, fileType, encoding );
		
					// Handle the <xformsdb:file> request
					xmlString								= this.handleXFormsDBFile( xformsdbServlet, StringUtils.toSafeASCIIString( requestURL + "download", false, response ), document );
				}
				else if ( Constants.NAMESPACE_URI_XFORMSDB.equals( namespaceURI ) == true && "cookie".equals( localName ) == true ) {
					// Update the XOM document (<xformsdb:cookie> element)
					XFormsDBCookieTransformer.transform( document );
					
					// Handle the <xformsdb:cookie> request
					xmlString								= this.handleXFormsDBCookie( xformsdbServlet, request.getCookies() );
				}
				else {
					throw new HandlerException( ErrorConstants.ERROR_CODE_POSTAPPLICATIONXMLHANDLER_13, ErrorConstants.ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_13  );				
				}
	
	
				// Write the response
				this.writeResponse( response, request, encoding, encoding, xmlString );
			}
			// XFormsDB upload request
			else {
				logger.log( Level.DEBUG, "The real requested file path (" + realRequestedFilePath + ") is for uploading a file to XFormsDB." );
				xmlString									= "<?xml version=\"" + Constants.OUTPUT_XML_VERSION_1_0 + "\" encoding=\"" + encoding + "\"?>" + Constants.LINE_SEPARATOR;
				xmlString									+= "<" + Constants.XFORMSDB_UPLOAD + " " + Constants.NAMESPACE_XMLNS_XFORMSDB + " />";

				
				// Write the response
				this.writeResponse( response, request, encoding, encoding, xmlString );
			}
			
			logger.log( Level.DEBUG, "The POST request having the content type: application/xml has been successfully handled." );
		} catch ( HandlerException hex ) {
			throw hex;
		} catch ( TransformerException tex ) {
			throw new HandlerException( tex.getCode(), tex.getMessage(), tex );
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_POSTAPPLICATIONXMLHANDLER_10, ErrorConstants.ERROR_MESSAGE_POSTAPPLICATIONXMLHANDLER_10, ex );
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
}