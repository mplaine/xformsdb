package fi.tkk.tml.xformsdb.transformer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xml.sax.InputSource;

import fi.tkk.tml.xformsdb.core.Constants;
import fi.tkk.tml.xformsdb.error.ErrorConstants;
import fi.tkk.tml.xformsdb.error.FileException;
import fi.tkk.tml.xformsdb.error.ManagerException;
import fi.tkk.tml.xformsdb.error.TransformerException;
import fi.tkk.tml.xformsdb.manager.HTTPRequestHeadersGetManager;
import fi.tkk.tml.xformsdb.manager.xformsdb.XFormsDBFileManager;
import fi.tkk.tml.xformsdb.manager.xformsdb.XFormsDBLoginManager;
import fi.tkk.tml.xformsdb.manager.xformsdb.XFormsDBQueryManager;
import fi.tkk.tml.xformsdb.manager.xformsdb.XFormsDBStateManager;
import fi.tkk.tml.xformsdb.manager.xformsdb.XFormsDBUserManager;
import fi.tkk.tml.xformsdb.manager.xformsdb.XFormsDBWidgetQueryManager;
import fi.tkk.tml.xformsdb.manager.xslt.TransformerManager;
import fi.tkk.tml.xformsdb.servlet.XFormsDBServlet;
import fi.tkk.tml.xformsdb.transformer.xforms.XFormsLoadsTransformer;
import fi.tkk.tml.xformsdb.transformer.xforms.XFormsModelsTransformer;
import fi.tkk.tml.xformsdb.transformer.xforms.XFormsSubmissionsTransformer;
import fi.tkk.tml.xformsdb.transformer.xformsdb.XFormsDBInstancesTransformer;
import fi.tkk.tml.xformsdb.transformer.xformsdb.XFormsDBSubmissionsTransformer;
import fi.tkk.tml.xformsdb.transformer.xformsrtc.XFormsRTCConnectionsTransformer;
import fi.tkk.tml.xformsdb.transformer.xformsrtc.XFormsRTCConnectsTransformer;
import fi.tkk.tml.xformsdb.transformer.xformsrtc.XFormsRTCDisconnectsTransformer;
import fi.tkk.tml.xformsdb.transformer.xformsrtc.XFormsRTCSendsTransformer;
import fi.tkk.tml.xformsdb.transformer.xhtml.XHTMLAsTransformer;
import fi.tkk.tml.xformsdb.transformer.xhtml.XHTMLMetasTransformer;
import fi.tkk.tml.xformsdb.util.FileUtils;
import fi.tkk.tml.xformsdb.util.IOUtils;
import fi.tkk.tml.xformsdb.util.StringUtils;
import fi.tkk.tml.xformsdb.util.XMLEscapeUtils;
import fi.tkk.tml.xformsdb.xml.sax.XFormsDBHandler;
import fi.tkk.tml.xformsdb.xml.to.RequestHeaderTO;


/**
 * Transform a file authored in the XFormsDB format by the user.
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on January 7, 2016
 */
public class XFormsDBTransformer {

	

	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger	= Logger.getLogger( XFormsDBTransformer.class );

	
	// PRIVATE CONSTURCTORS
	// Prevent instantiation of this class
	private XFormsDBTransformer() {
		logger.log( Level.DEBUG, "Constructor has been called." );
	}
	
	
	// PRIVATE STATIC METHODS
	private static String getRealRequestedFilePath( HttpServlet xformsdbServlet, HttpServletRequest request ) throws TransformerException {
		logger.log( Level.DEBUG, "Method has been called." );
		String requestURI				= null;
		String contextPath				= null;
		String requestedFilePath		= null;
		String realRequestedFilePath	= null;

		
		try {
			// Retrieve the real request file path
			requestURI					= URLDecoder.decode( request.getRequestURI(), Constants.CLIENT_URI_ENCODING_DEFAULT ); // Without query string
			contextPath					= URLDecoder.decode( request.getContextPath(), Constants.CLIENT_URI_ENCODING_DEFAULT ); // Without query string
			requestedFilePath			= requestURI.substring( contextPath.length(), requestURI.length() ); // Request URI - context path
			realRequestedFilePath		= xformsdbServlet.getServletContext().getRealPath( requestedFilePath ); // File system path
			logger.log( Level.DEBUG, "The real requested file path (" + realRequestedFilePath + ") has been successfully retrieved." );
		} catch ( Exception ex ) {
			throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_39, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_39, ex );
		}
		
		
		return realRequestedFilePath;
	}

	
	private static XFormsDBHandler getXFormsDBHandler( HttpServlet xformsdbServlet ) throws TransformerException {
		logger.log( Level.DEBUG, "Method has been called." );
		XFormsDBHandler xformsdbHandler	= null;

		
		try {
			// Retrieve the XFormsDB handler
			xformsdbHandler				= new XFormsDBHandler();
			logger.log( Level.DEBUG, "The XFormsDB handler has been successfully retrieved." );
		} catch ( Exception ex ) {
			throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_40, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_40, ex );
		}
		
		
		return xformsdbHandler;
	}

	
	private static SAXParser getXFormsDBSAXParser( XFormsDBHandler xformsdbHandler ) throws TransformerException {
		logger.log( Level.DEBUG, "Method has been called." );
		SAXParserFactory xformsdbSAXParserFactory	= null;
		SAXParser xformsdbSAXParser					= null;

		
		try {
			// Retrieve the SAX parser
			xformsdbSAXParserFactory				= SAXParserFactory.newInstance();
			xformsdbSAXParserFactory.setNamespaceAware( true );
			xformsdbSAXParserFactory.setValidating( false ); // XHTML entities are handled with the entity resolver
			xformsdbSAXParserFactory.setFeature( XFormsDBHandler.NAMESPACES_FEATURE_ID, true );
			xformsdbSAXParserFactory.setFeature( XFormsDBHandler.NAMESPACE_PREFIXES_FEATURE_ID, true );
			xformsdbSAXParser						= xformsdbSAXParserFactory.newSAXParser();
			xformsdbSAXParser.setProperty( XFormsDBHandler.LEXICAL_HANDLER_PROPERTY_ID, xformsdbHandler );
			logger.log( Level.DEBUG, "The XFormsDB SAX parser has been successfully retrieved." );
		} catch ( Exception ex ) {
			throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_41, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_41, ex );
		}
		
		
		return xformsdbSAXParser;
	}

	
	private static void parseXFormsDBInclude( SAXParser xformsdbSAXParser, XFormsDBHandler xformsdbHandler, String realRequestedFilePath ) throws TransformerException {
		logger.log( Level.DEBUG, "Method has been called." );
		InputSource inputSourceParseXFormsDBInclude	= null;
		
		
		try {
			// Parse the original XFormsDB document (include) in order to retrieve the DOCTYPE declaration			
			inputSourceParseXFormsDBInclude			= new InputSource();
			//inputSourceParseXFormsDBInclude.setEncoding( encoding ); // Encoding detected automatically based on the XML declaration, defaults to UTF-8
			inputSourceParseXFormsDBInclude.setByteStream( FileUtils.getFileAsInputStream( realRequestedFilePath ) );
			xformsdbSAXParser.parse( inputSourceParseXFormsDBInclude, xformsdbHandler );
			logger.log( Level.DEBUG, "The original XFormsDB document (include) has been successfully parsed." );
		} catch ( FileException fex ) {
			throw new TransformerException( fex.getCode(), fex.getMessage(), fex );
		} catch ( Exception ex ) {
			throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_42, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_42, ex );
		}
	}
	
	
	private static Transformer getTransformer( String xslFilePath ) throws TransformerException {
		logger.log( Level.DEBUG, "Method has been called." );
		Transformer transformer	= null;

		
		try {
			// Retrieve the transformer
			transformer			= TransformerManager.getTransformer( xslFilePath );
			logger.log( Level.DEBUG, "The transformer has been successfully retrieved." );
		} catch ( ManagerException mex ) {
			throw new TransformerException( mex.getCode(), mex.getMessage(), mex );
		} catch ( Exception ex ) {
			throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_43, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_43, ex );
		}
		
		
		return transformer;
	}

	
	private static String transformXFormsDBInclude( Transformer xformsdbTransformerInclude, SAXParser xformsdbSAXParserInclude, String realRequestedFilePath, String encoding ) throws TransformerException {
		logger.log( Level.DEBUG, "Method has been called." );
		InputSource inputSourceTransformXFormsDBInclude		= null;
		Source sourceTransformXFormsDBInclude				= null;
		ByteArrayOutputStream outputStreamXFormsDBInclude	= null;
		Result resultTransformXFormsDBInclude				= null;
		String xformsdbInclude								= null;
		
		
		try {
			// Set output properties
			xformsdbTransformerInclude.setOutputProperty( OutputKeys.OMIT_XML_DECLARATION, Constants.OUTPUT_XML_OMIT_XML_DECLARATION_NO );
			xformsdbTransformerInclude.setOutputProperty( OutputKeys.METHOD, Constants.OUTPUT_XML_METHOD_XML );
			xformsdbTransformerInclude.setOutputProperty( OutputKeys.VERSION, Constants.OUTPUT_XML_VERSION_1_0 );
			xformsdbTransformerInclude.setOutputProperty( OutputKeys.ENCODING, encoding );
			//xformsdbTransformerInclude.setOutputProperty( OutputKeys.INDENT, Constants.OUTPUT_XML_INDENT_NO );
			//if ( publicId != null ) {
			//	xformsdbTransformerInclude.setOutputProperty( OutputKeys.DOCTYPE_PUBLIC, publicId );
			//}
			//if ( systemId != null ) {
			//	xformsdbTransformerInclude.setOutputProperty( OutputKeys.DOCTYPE_SYSTEM, systemId );
			//}
			//xformsdbTransformerInclude.setOutputProperty( OutputKeys.STANDALONE, "no" );
			//xformsdbTransformerInclude.setOutputProperty( OutputKeys.MEDIA_TYPE, "text/xml" );

			// Set parameters (none)

			// Transform the original XFormsDB document (include) in order to include external resources into the XFormsDB document
			inputSourceTransformXFormsDBInclude				= new InputSource();
			//inputSourceTransformXFormsDBInclude.setEncoding( encoding ); // Encoding detected automatically based on the XML declaration, defaults to UTF-8
			inputSourceTransformXFormsDBInclude.setByteStream( FileUtils.getFileAsInputStream( realRequestedFilePath ) );
			inputSourceTransformXFormsDBInclude.setSystemId( realRequestedFilePath ); // Important for resolving relative URIs!
			sourceTransformXFormsDBInclude					= new SAXSource( xformsdbSAXParserInclude.getXMLReader(), inputSourceTransformXFormsDBInclude );
			outputStreamXFormsDBInclude						= new ByteArrayOutputStream();
			resultTransformXFormsDBInclude					= new StreamResult( outputStreamXFormsDBInclude );	
			xformsdbTransformerInclude.transform( sourceTransformXFormsDBInclude, resultTransformXFormsDBInclude );
			
			// Change the encoding of the XFormsDB document
			xformsdbInclude									= outputStreamXFormsDBInclude.toString( encoding );
			logger.log( Level.DEBUG, "The original XFormsDB document (include) has been successfully transformed." );
		} catch ( FileException fex ) {
			throw new TransformerException( fex.getCode(), fex.getMessage(), fex );
		} catch ( Exception ex ) {
			throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_44, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_44, ex );
		}
		
		
		return xformsdbInclude;
	}

	
	private static String transformXFormsDBSecview( Transformer xformsdbTransformerSecview, String xformsdbInclude, String encoding, String xformsdbUserXMLString ) throws TransformerException  {
		logger.log( Level.DEBUG, "Method has been called." );
		InputSource inputSourceTransformXFormsDBSecview		= null;
		Source sourceTransformXFormsDBSecview				= null;
		ByteArrayOutputStream outputStreamXFormsDBSecview	= null;
		Result resultTransformXFormsDBSecview				= null;
		String xformsdbSecview								= null;
		
		
		try {
			// Set output properties
			xformsdbTransformerSecview.setOutputProperty( OutputKeys.OMIT_XML_DECLARATION, Constants.OUTPUT_XML_OMIT_XML_DECLARATION_NO );
			xformsdbTransformerSecview.setOutputProperty( OutputKeys.METHOD, Constants.OUTPUT_XML_METHOD_XML );
			xformsdbTransformerSecview.setOutputProperty( OutputKeys.VERSION, Constants.OUTPUT_XML_VERSION_1_0 );
			xformsdbTransformerSecview.setOutputProperty( OutputKeys.ENCODING, encoding );
			//xformsdbTransformerSecview.setOutputProperty( OutputKeys.INDENT, Constants.OUTPUT_XML_INDENT_NO );
			//if ( publicId != null ) {
			//	xformsdbTransformerSecview.setOutputProperty( OutputKeys.DOCTYPE_PUBLIC, publicId );
			//}
			//if ( systemId != null ) {
			//	xformsdbTransformerSecview.setOutputProperty( OutputKeys.DOCTYPE_SYSTEM, systemId );
			//}
			//xformsdbTransformerSecview.setOutputProperty( OutputKeys.STANDALONE, "no" );
			//xformsdbTransformerSecview.setOutputProperty( OutputKeys.MEDIA_TYPE, "text/xml" );

			// Set parameters
			xformsdbTransformerSecview.setParameter( "paramXFormsDBUserXMLString", xformsdbUserXMLString );

			// Transform the included XFormsDB document (secview) in order to create user role-based output of it
			inputSourceTransformXFormsDBSecview				= new InputSource();
			//inputSourceTransformXFormsDBSecview.setEncoding( encoding ); // Encoding detected automatically based on the XML declaration, defaults to UTF-8
			inputSourceTransformXFormsDBSecview.setByteStream( IOUtils.convert( xformsdbInclude, encoding ) );
			sourceTransformXFormsDBSecview					= new SAXSource( inputSourceTransformXFormsDBSecview );
			outputStreamXFormsDBSecview						= new ByteArrayOutputStream();
			resultTransformXFormsDBSecview					= new StreamResult( outputStreamXFormsDBSecview );
			xformsdbTransformerSecview.transform( sourceTransformXFormsDBSecview, resultTransformXFormsDBSecview );
			
			// Change the encoding of the XML
			xformsdbSecview									= outputStreamXFormsDBSecview.toString( encoding );
			logger.log( Level.DEBUG, "The included XFormsDB document (secview) has been successfully transformed." );
		} catch ( Exception ex ) {
			throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_91, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_91, ex );
		}
		
		
		return xformsdbSecview;
	}
	
	
	private static String transformXFormsDBExtract( Transformer xformsdbTransformerExtract, String xformsdbSecview, String encoding ) throws TransformerException  {
		logger.log( Level.DEBUG, "Method has been called." );
		InputSource inputSourceTransformXFormsDBExtract		= null;
		Source sourceTransformXFormsDBExtract				= null;
		ByteArrayOutputStream outputStreamXFormsDBExtract	= null;
		Result resultTransformXFormsDBExtract				= null;
		String xformsdbExtract								= null;
		
		
		try {
			// Set output properties
			xformsdbTransformerExtract.setOutputProperty( OutputKeys.OMIT_XML_DECLARATION, Constants.OUTPUT_XML_OMIT_XML_DECLARATION_NO );
			xformsdbTransformerExtract.setOutputProperty( OutputKeys.METHOD, Constants.OUTPUT_XML_METHOD_XML );
			xformsdbTransformerExtract.setOutputProperty( OutputKeys.VERSION, Constants.OUTPUT_XML_VERSION_1_0 );
			xformsdbTransformerExtract.setOutputProperty( OutputKeys.ENCODING, encoding );
			xformsdbTransformerExtract.setOutputProperty( OutputKeys.INDENT, Constants.OUTPUT_XML_INDENT_YES );
			//if ( publicId != null ) {
			//	xformsdbTransformerExtract.setOutputProperty( OutputKeys.DOCTYPE_PUBLIC, publicId );
			//}
			//if ( systemId != null ) {
			//	xformsdbTransformerExtract.setOutputProperty( OutputKeys.DOCTYPE_SYSTEM, systemId );
			//}
			//xformsdbTransformerExtract.setOutputProperty( OutputKeys.STANDALONE, "no" );
			//xformsdbTransformerExtract.setOutputProperty( OutputKeys.MEDIA_TYPE, "text/xml" );

			// Set parameters
			xformsdbTransformerExtract.setParameter( "paramXFormsDBResponseProxyInstance", Constants.XFORMSDB_RESPONSE_PROXY_INSTANCE );

			// Transform the secviewed XFormsDB document (extract) in order to extract information from it
			inputSourceTransformXFormsDBExtract				= new InputSource();
			//inputSourceTransformXFormsDBExtract.setEncoding( encoding ); // Encoding detected automatically based on the XML declaration, defaults to UTF-8
			inputSourceTransformXFormsDBExtract.setByteStream( IOUtils.convert( xformsdbSecview, encoding ) );
			sourceTransformXFormsDBExtract					= new SAXSource( inputSourceTransformXFormsDBExtract );
			outputStreamXFormsDBExtract						= new ByteArrayOutputStream();
			resultTransformXFormsDBExtract					= new StreamResult( outputStreamXFormsDBExtract );
			xformsdbTransformerExtract.transform( sourceTransformXFormsDBExtract, resultTransformXFormsDBExtract );
			
			// Change the encoding of the XML
			xformsdbExtract									= outputStreamXFormsDBExtract.toString( encoding );
			logger.log( Level.DEBUG, "The secviewed XFormsDB document (extract) has been successfully transformed." );
		} catch ( Exception ex ) {
			throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_45, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_45, ex );
		}
		
		
		return xformsdbExtract;
	}
	
	
	private static Document buildXOM( String xformsdbExtract, String encoding ) throws TransformerException {
		logger.log( Level.DEBUG, "Method has been called." );
		Builder builder		= null;
		Document document	= null;
		
		
		try {
			// Parse the extracted XML document in order to build the XOM document object			
			builder			= new Builder();
			document		= builder.build( IOUtils.convert( xformsdbExtract, encoding ) );
			logger.log( Level.DEBUG, "The XOM document object has been successfully built." );
		} catch ( IOException ioex ) {
			ioex.printStackTrace();
		} catch ( ValidityException vex ) {
			throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_46, "Validity exception" + ", line " + vex.getLineNumber() + ", column " + vex.getColumnNumber() + ", uri " + vex.getURI() + ". " + vex.getMessage(), vex );
		} catch ( ParsingException pex ) {
			throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_47, "Parsing exception" + ", line " + pex.getLineNumber() + ", column " + pex.getColumnNumber() + ", uri " + pex.getURI() + ". " + pex.getMessage(), pex );
		} catch ( Exception ex ) {
			throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_48, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_48, ex );
		}
		
		
		return document;
	}

	
	private static void updateXOMXFormsDBSubmissions( HttpServletResponse response, Document document, Map<String, String> expressionTypeUpdateRequestInstances, Map<String, String> stateTypeSetRequestInstances, Map<String, String> fileTypeInsertRequestInstances, Map<String, String> fileTypeDeleteRequestInstances, Map<String, String> fileTypeUpdateRequestInstances, String actionURL, String encoding ) throws TransformerException {
		logger.log( Level.DEBUG, "Method has been called." );
		
		
		try {
			// Update the XOM document in order to update the <xformsdb:submission> elements
			XFormsDBSubmissionsTransformer.transform( response, document, expressionTypeUpdateRequestInstances, stateTypeSetRequestInstances, fileTypeInsertRequestInstances, fileTypeDeleteRequestInstances, fileTypeUpdateRequestInstances, actionURL, encoding );
			logger.log( Level.DEBUG, "The XOM document object (<xformsdb:submissions> element) has been successfully updated." );
		} catch ( TransformerException tex ) {
			throw tex;
		} catch ( Exception ex ) {
			throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_49, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_49, ex );
		}
	}
	
	
	private static void updateXOMXFormsDBInstances( XFormsDBServlet xformsdbServlet, HttpServletRequest request, Document document, Map<String, String> expressionTypeUpdateRequestInstances, Map<String, String> stateTypeSetRequestInstances, Map<String, String> fileTypeInsertRequestInstances, Map<String, String> fileTypeDeleteRequestInstances, Map<String, String> fileTypeUpdateRequestInstances ) throws TransformerException {
		logger.log( Level.DEBUG, "Method has been called." );
		
		
		try {
			// Update the XOM document in order to update the <xformsdb:instance> elements
			XFormsDBInstancesTransformer.transform( xformsdbServlet, request, document, expressionTypeUpdateRequestInstances, stateTypeSetRequestInstances, fileTypeInsertRequestInstances, fileTypeDeleteRequestInstances, fileTypeUpdateRequestInstances );
			logger.log( Level.DEBUG, "The XOM document object (<xformsdb:instances> element) has been successfully updated." );
		} catch ( TransformerException tex ) {
			throw tex;
		} catch ( Exception ex ) {
			throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_50, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_50, ex );
		}
	}

	
	private static void updateXOMXFormsModels( HttpServletResponse response, Document document ) throws TransformerException {
		logger.log( Level.DEBUG, "Method has been called." );
		
		
		try {
			// Update the XOM document in order to update the <xforms:model> elements
			XFormsModelsTransformer.transform( response, document );
			logger.log( Level.DEBUG, "The XOM document object (<xforms:models> element) has been successfully updated." );
		} catch ( TransformerException tex ) {
			throw tex;
		} catch ( Exception ex ) {
			throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_89, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_89, ex );
		}
	}


	private static void updateXOMXFormsRTCConnections( HttpServletResponse response, Document document ) throws TransformerException {
		logger.log( Level.DEBUG, "Method has been called." );
		
		
		try {
			// Update the XOM document in order to update the <xformsrtc:connection> elements
			XFormsRTCConnectionsTransformer.transform( response, document );
			logger.log( Level.DEBUG, "The XOM document object (<xformsrtc:connections> element) has been successfully updated." );
		} catch ( TransformerException tex ) {
			throw tex;
		} catch ( Exception ex ) {
			throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_97, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_97, ex );
		}
	}

	
	private static void updateXOMXFormsRTCConnects( Document document ) throws TransformerException {
		logger.log( Level.DEBUG, "Method has been called." );
		
		
		try {
			// Update the XOM document in order to update the <xformsrtc:connect> elements
			XFormsRTCConnectsTransformer.transform( document );
			logger.log( Level.DEBUG, "The XOM document object (<xformsrtc:connects> element) has been successfully updated." );
		} catch ( TransformerException tex ) {
			throw tex;
		} catch ( Exception ex ) {
			throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_93, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_93, ex );
		}
	}

	
	private static void updateXOMXFormsRTCDisconnects( Document document ) throws TransformerException {
		logger.log( Level.DEBUG, "Method has been called." );
		
		
		try {
			// Update the XOM document in order to update the <xformsrtc:disconnect> elements
			XFormsRTCDisconnectsTransformer.transform( document );
			logger.log( Level.DEBUG, "The XOM document object (<xformsrtc:disconnects> element) has been successfully updated." );
		} catch ( TransformerException tex ) {
			throw tex;
		} catch ( Exception ex ) {
			throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_95, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_95, ex );
		}
	}

	
	private static void updateXOMXFormsRTCSends( Document document ) throws TransformerException {
		logger.log( Level.DEBUG, "Method has been called." );
		
		
		try {
			// Update the XOM document in order to update the <xformsrtc:send> elements
			XFormsRTCSendsTransformer.transform( document );
			logger.log( Level.DEBUG, "The XOM document object (<xformsrtc:sends> element) has been successfully updated." );
		} catch ( TransformerException tex ) {
			throw tex;
		} catch ( Exception ex ) {
			throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_108, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_108, ex );
		}
	}


	private static void updateXOMXFormsLoads( HttpServletResponse response, Document document ) throws TransformerException {
		logger.log( Level.DEBUG, "Method has been called." );
		
		
		try {
			// Update the XOM document in order to update the <xforms:load> elements
			XFormsLoadsTransformer.transform( response, document );
			logger.log( Level.DEBUG, "The XOM document object (<xforms:loads> element) has been successfully updated." );
		} catch ( TransformerException tex ) {
			throw tex;
		} catch ( Exception ex ) {
			throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_51, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_51, ex );
		}
	}

	
	private static void updateXOMXFormsSubmissions( HttpServletResponse response, Document document ) throws TransformerException {
		logger.log( Level.DEBUG, "Method has been called." );
		
		
		try {
			// Update the XOM document in order to update the <xforms:submission> elements
			XFormsSubmissionsTransformer.transform( response, document );
			logger.log( Level.DEBUG, "The XOM document object (<xforms:submissions> element) has been successfully updated." );
		} catch ( TransformerException tex ) {
			throw tex;
		} catch ( Exception ex ) {
			throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_52, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_52, ex );
		}
	}

	
	private static void updateXOMXHTMLMetas( HttpServletResponse response, Document document ) throws TransformerException {
		logger.log( Level.DEBUG, "Method has been called." );
		
		
		try {
			// Update the XOM document in order to update the <xhtml:meta> elements
			XHTMLMetasTransformer.transform( response, document );
			logger.log( Level.DEBUG, "The XOM document object (<xhtml:metas> element) has been successfully updated." );
		} catch ( TransformerException tex ) {
			throw tex;
		} catch ( Exception ex ) {
			throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_53, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_53, ex );
		}
	}

	
	private static void updateXOMXHTMLAs( HttpServletResponse response, Document document ) throws TransformerException {
		logger.log( Level.DEBUG, "Method has been called." );
		
		
		try {
			// Update the XOM document in order to update the <xhtml:a> elements
			XHTMLAsTransformer.transform( response, document );
			logger.log( Level.DEBUG, "The XOM document object (<xhtml:as> element) has been successfully updated." );
		} catch ( TransformerException tex ) {
			throw tex;
		} catch ( Exception ex ) {
			throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_54, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_54, ex );
		}
	}

	
	private static void addElementsToXFormsDBQueryManager( HttpSession session, Document document ) throws TransformerException {
		logger.log( Level.DEBUG, "Method has been called." );
		
		
		try {
			// Add the <xformsdb:query> elements to the XFormsDB query manager
			XFormsDBQueryManager.addActiveXFormsDBQueryElements( session, document );
			logger.log( Level.DEBUG, "The <xformsdb:query> elements have been successfully added to the XFormsDB query manager." );
		} catch ( ManagerException mex ) {
			throw new TransformerException( mex.getCode(), mex.getMessage(), mex );
		} catch ( Exception ex ) {
			throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_55, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_55, ex );
		}
	}

	
	private static void addElementsToXFormsDBWidgetQueryManager( HttpSession session, Document document ) throws TransformerException {
		logger.log( Level.DEBUG, "Method has been called." );
		
	
		try {
			// Add the <xformsdb:widgetquery> elements to the XFormsDB widget query manager
			XFormsDBWidgetQueryManager.addActiveXFormsDBWidgetQueryElements( session, document );
			logger.log( Level.DEBUG, "The <xformsdb:widgetquery> elements have been successfully added to the XFormsDB widget query manager." );
		} catch ( ManagerException mex ) {
			throw new TransformerException( mex.getCode(), mex.getMessage(), mex );
		} catch ( Exception ex ) {
			throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_84, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_84, ex );
		}
	}

	
	private static void addElementsToXFormsDBLoginManager( HttpSession session, Document document ) throws TransformerException {
		logger.log( Level.DEBUG, "Method has been called." );
		
		
		try {
			// Add the <xformsdb:login> elements to the XFormsDB login manager
			XFormsDBLoginManager.addActiveXFormsDBLoginElements( session, document );
			logger.log( Level.DEBUG, "The <xformsdb:login> elements have been successfully added to the XFormsDB login manager." );
		} catch ( ManagerException mex ) {
			throw new TransformerException( mex.getCode(), mex.getMessage(), mex );
		} catch ( Exception ex ) {
			throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_56, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_56, ex );
		}
	}

	
	private static void addElementsToXFormsDBFileManager( HttpSession session, Document document ) throws TransformerException {
		logger.log( Level.DEBUG, "Method has been called." );
		
		
		try {
			// Add the <xformsdb:file> elements to the XFormsDB file manager
			XFormsDBFileManager.addActiveXFormsDBFileElements( session, document );
			logger.log( Level.DEBUG, "The <xformsdb:file> elements have been successfully added to the XFormsDB file manager." );
		} catch ( ManagerException mex ) {
			throw new TransformerException( mex.getCode(), mex.getMessage(), mex );
		} catch ( Exception ex ) {
			throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_73, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_73, ex );
		}
	}

	
	private static String transformXFormsDBXForms( Transformer xformsdbTransformerXForms, String xformsdbSecview, String encoding, String requestBaseURIXMLString, String requestHeadersXMLString, String requestParametersXMLString, String xformsdbStateXMLString, Document document ) throws TransformerException  {
		logger.log( Level.DEBUG, "Method has been called." );
		InputSource inputSourceTransformXFormsDBXForms		= null;
		Source sourceTransformXFormsDBXForms				= null;
		ByteArrayOutputStream outputStreamXFormsDBXForms	= null;
		Result resultTransformXFormsDBXForms				= null;
		String xformsdbXForms								= null;
		
		
		try {
			// Set output properties
			xformsdbTransformerXForms.setOutputProperty( OutputKeys.OMIT_XML_DECLARATION, Constants.OUTPUT_XML_OMIT_XML_DECLARATION_NO );
			xformsdbTransformerXForms.setOutputProperty( OutputKeys.METHOD, Constants.OUTPUT_XML_METHOD_XML );
			xformsdbTransformerXForms.setOutputProperty( OutputKeys.VERSION, Constants.OUTPUT_XML_VERSION_1_0 );
			xformsdbTransformerXForms.setOutputProperty( OutputKeys.ENCODING, encoding );
			//xformsdbTransformerXForms.setOutputProperty( OutputKeys.INDENT, Constants.OUTPUT_XML_INDENT_NO );
			//if ( publicId != null ) {
			//	xformsdbTransformerXForms.setOutputProperty( OutputKeys.DOCTYPE_PUBLIC, publicId );
			//}
			//if ( systemId != null ) {
			//	xformsdbTransformerXForms.setOutputProperty( OutputKeys.DOCTYPE_SYSTEM, systemId );
			//}
			//xformsdbTransformerXForms.setOutputProperty( OutputKeys.STANDALONE, "no" );
			//xformsdbTransformerXForms.setOutputProperty( OutputKeys.MEDIA_TYPE, "text/xml" );

			// Set parameters
			//xformsdbTransformerXForms.setParameter( "paramDocType", docType );
			xformsdbTransformerXForms.setParameter( "paramXFormsDBRequestBaseURIInstance", Constants.XFORMSDB_REQUEST_BASE_URI_INSTANCE );
			xformsdbTransformerXForms.setParameter( "paramXFormsDBRequestHeadersInstance", Constants.XFORMSDB_REQUEST_HEADERS_INSTANCE );
			xformsdbTransformerXForms.setParameter( "paramXFormsDBRequestParametersInstance", Constants.XFORMSDB_REQUEST_PARAMETERS_INSTANCE );
			xformsdbTransformerXForms.setParameter( "paramXFormsDBStateInstance", Constants.XFORMSDB_STATE_INSTANCE );
			xformsdbTransformerXForms.setParameter( "paramXFormsDBErrorElement", Constants.XFORMSDB_ERROR_ELEMENT );
			xformsdbTransformerXForms.setParameter( "paramXFormsDBRequestErrorEvent", Constants.XFORMSDB_REQUEST_ERROR_EVENT );
			xformsdbTransformerXForms.setParameter( "paramXFormsDBRequestBaseURIXMLString", requestBaseURIXMLString );
			xformsdbTransformerXForms.setParameter( "paramXFormsDBRequestHeadersXMLString", requestHeadersXMLString );
			xformsdbTransformerXForms.setParameter( "paramXFormsDBRequestParametersXMLString", requestParametersXMLString );
			xformsdbTransformerXForms.setParameter( "paramXFormsDBStateXMLString", xformsdbStateXMLString );
			xformsdbTransformerXForms.setParameter( "paramXFormsDBInstancesXMLString", new Element( document.getRootElement().getFirstChildElement( "instances", Constants.NAMESPACE_URI_XFORMSDB ) ).toXML() );
			xformsdbTransformerXForms.setParameter( "paramXFormsDBSubmissionsXMLString", new Element( document.getRootElement().getFirstChildElement( "submissions", Constants.NAMESPACE_URI_XFORMSDB ) ).toXML() );
			xformsdbTransformerXForms.setParameter( "paramXFormsRTCConnectionsXMLString", new Element( document.getRootElement().getFirstChildElement( "connections", Constants.NAMESPACE_URI_XFORMSRTC ) ).toXML() );
			xformsdbTransformerXForms.setParameter( "paramXFormsRTCConnectsXMLString", new Element( document.getRootElement().getFirstChildElement( "connects", Constants.NAMESPACE_URI_XFORMSRTC ) ).toXML() );
			xformsdbTransformerXForms.setParameter( "paramXFormsRTCDisconnectsXMLString", new Element( document.getRootElement().getFirstChildElement( "disconnects", Constants.NAMESPACE_URI_XFORMSRTC ) ).toXML() );
			xformsdbTransformerXForms.setParameter( "paramXFormsRTCSendsXMLString", new Element( document.getRootElement().getFirstChildElement( "sends", Constants.NAMESPACE_URI_XFORMSRTC ) ).toXML() );
			xformsdbTransformerXForms.setParameter( "paramXFormsRTCEventProxyInstance", Constants.XFORMSRTC_EVENT_PROXY_INSTANCE );
			xformsdbTransformerXForms.setParameter( "paramXFormsRTCVariable", Constants.XFORMSRTC_VARIABLE );
			xformsdbTransformerXForms.setParameter( "paramXFormsModelsXMLString", new Element( document.getRootElement().getFirstChildElement( "models", Constants.NAMESPACE_URI_XFORMS ) ).toXML() );
			xformsdbTransformerXForms.setParameter( "paramXFormsLoadsXMLString", new Element( document.getRootElement().getFirstChildElement( "loads", Constants.NAMESPACE_URI_XFORMS ) ).toXML() );
			xformsdbTransformerXForms.setParameter( "paramXFormsSubmissionsXMLString", new Element( document.getRootElement().getFirstChildElement( "submissions", Constants.NAMESPACE_URI_XFORMS ) ).toXML() );
			xformsdbTransformerXForms.setParameter( "paramXHTMLMetasXMLString", new Element( document.getRootElement().getFirstChildElement( "metas", Constants.NAMESPACE_URI_XHTML ) ).toXML() );
			xformsdbTransformerXForms.setParameter( "paramXHTMLAsXMLString", new Element( document.getRootElement().getFirstChildElement( "as", Constants.NAMESPACE_URI_XHTML ) ).toXML() );

			// Transform the secviewed XFormsDB document (xforms) in order to retrieve XForms document 
			inputSourceTransformXFormsDBXForms				= new InputSource();
			//inputSourceTransformXFormsDBXForms.setEncoding( encoding ); // Encoding detected automatically based on the XML declaration
			inputSourceTransformXFormsDBXForms.setByteStream( IOUtils.convert( xformsdbSecview, encoding ) );
			sourceTransformXFormsDBXForms					= new SAXSource( inputSourceTransformXFormsDBXForms );
			outputStreamXFormsDBXForms						= new ByteArrayOutputStream();
			resultTransformXFormsDBXForms					= new StreamResult( outputStreamXFormsDBXForms );
			xformsdbTransformerXForms.transform( sourceTransformXFormsDBXForms, resultTransformXFormsDBXForms );
			
			// Change the encoding of the XML
			xformsdbXForms									= outputStreamXFormsDBXForms.toString( encoding );
			logger.log( Level.DEBUG, "The included XFormsDB document (xforms) has been successfully transformed." );
		} catch ( Exception ex ) {
			throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_57, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_57, ex );
		}
		
		
		return xformsdbXForms;
	}

	
	private static String getRequestBaseURIXMLString( HttpServletRequest request, String encoding ) throws TransformerException {
		logger.log( Level.DEBUG, "Method has been called." );
		String requestBaseURIXMLString			= null;
		String requestURL						= null;
		String requestURI						= null;
		String contextPath						= null;
		String requestBaseURI					= null;
		

		try {
			// Retrieve/write the request base URI
			requestURL							= URLDecoder.decode( request.getRequestURL().toString(), Constants.CLIENT_URI_ENCODING_DEFAULT ); // Without query string
			requestURI							= URLDecoder.decode( request.getRequestURI(), Constants.CLIENT_URI_ENCODING_DEFAULT ); // Without query string
			contextPath							= URLDecoder.decode( request.getContextPath(), Constants.CLIENT_URI_ENCODING_DEFAULT ); // Without query string
			requestBaseURI						= requestURL.substring( 0, ( requestURL.length() - requestURI.substring( contextPath.length() ).length() ) );
			requestBaseURIXMLString				= "<?xml version=\"" + Constants.OUTPUT_XML_VERSION_1_0 + "\" encoding=\"" + encoding + "\"?>" + Constants.LINE_SEPARATOR;
			requestBaseURIXMLString				+= "<" + Constants.XFORMSDB_REQUEST_BASE_URI + " " + Constants.NAMESPACE_XMLNS_EMPTY + " " + Constants.NAMESPACE_XMLNS_XFORMSDB + ">";
			requestBaseURIXMLString				+= StringUtils.toSafeASCIIString( requestBaseURI );
			requestBaseURIXMLString				+= "</" + Constants.XFORMSDB_REQUEST_BASE_URI + ">";
			logger.log( Level.DEBUG, "RequestBaseURIXMLString:" + Constants.LINE_SEPARATOR + requestBaseURIXMLString );
		} catch ( Exception ex ) {
			throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_79, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_79, ex  );
		}
		
		
		return requestBaseURIXMLString;
	}

	
	private static String getRequestHeadersXMLString( HttpSession session, String encoding ) throws TransformerException {
		logger.log( Level.DEBUG, "Method has been called." );
		List<RequestHeaderTO> requestHeaderTOs	= null;
		int requestHeaderTOsSize				= 0;
		RequestHeaderTO requestHeaderTO			= null;
		String requestHeadersXMLString			= null;
		

		try {
			// Retrieve/write the HTTP request headers (GET) from the session
			requestHeaderTOs					= HTTPRequestHeadersGetManager.getHTTPRequestHeadersGet( session );
			requestHeaderTOsSize				= requestHeaderTOs.size();
			requestHeadersXMLString				= "<?xml version=\"" + Constants.OUTPUT_XML_VERSION_1_0 + "\" encoding=\"" + encoding + "\"?>" + Constants.LINE_SEPARATOR;
			if ( requestHeaderTOsSize > 0 ) {
				requestHeadersXMLString			+= "<" + Constants.XFORMSDB_REQUEST_HEADERS + " " + Constants.NAMESPACE_XMLNS_EMPTY + " " + Constants.NAMESPACE_XMLNS_XFORMSDB + ">" + Constants.LINE_SEPARATOR;
				for ( int i = 0; i < requestHeaderTOsSize; i++ ) {
					requestHeaderTO				= requestHeaderTOs.get( i );
					requestHeadersXMLString		+= "\t<" + Constants.XFORMSDB_REQUEST_HEADER + " name=\"" + XMLEscapeUtils.escapeXml( requestHeaderTO.getName(), "1.0", true ) + "\">" + XMLEscapeUtils.escapeXml( requestHeaderTO.getValue(), "1.0", false ) + "</" + Constants.XFORMSDB_REQUEST_HEADER + ">" + Constants.LINE_SEPARATOR;		
				}
				requestHeadersXMLString			+= "</" + Constants.XFORMSDB_REQUEST_HEADERS + ">";
			}
			else {
				requestHeadersXMLString			+= "<" + Constants.XFORMSDB_REQUEST_HEADERS + " " + Constants.NAMESPACE_XMLNS_EMPTY + " " + Constants.NAMESPACE_XMLNS_XFORMSDB + " />";
			}
			logger.log( Level.DEBUG, "RequestHeadersXMLString:" + Constants.LINE_SEPARATOR + requestHeadersXMLString );
		} catch ( Exception ex ) {
			throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_78, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_78, ex  );
		}
		
		
		return requestHeadersXMLString;
	}

	
	private static String getRequestParametersXMLString( HttpServletRequest request, String encoding, String serverURIEncoding ) throws TransformerException {
		logger.log( Level.DEBUG, "Method has been called." );
		String requestParametersXMLString		= null;
		Enumeration requestParameterNames		= null;
		String requestParameterName				= null;
		String[] requestParameterValues			= null;
		boolean hasRequestParameters			= false;
		

		try {
			// Retrieve/write the request parameters
			requestParameterNames				= request.getParameterNames();
			requestParametersXMLString			= "<?xml version=\"" + Constants.OUTPUT_XML_VERSION_1_0 + "\" encoding=\"" + encoding + "\"?>" + Constants.LINE_SEPARATOR;

			// Iterate over request parameter names
			while ( requestParameterNames.hasMoreElements() ) {
				if ( hasRequestParameters == false ) {
					requestParametersXMLString	+= "<" + Constants.XFORMSDB_REQUEST_PARAMETERS + " " + Constants.NAMESPACE_XMLNS_EMPTY + " " + Constants.NAMESPACE_XMLNS_XFORMSDB + ">" + Constants.LINE_SEPARATOR;
					hasRequestParameters		= true;
				}
				requestParameterName			= ( String ) requestParameterNames.nextElement();
				requestParameterValues			= request.getParameterValues( requestParameterName );
				// Iterate over request parameter values of that particular request parameter name
				for ( int i = 0; i < requestParameterValues.length; i++ ) {
					requestParametersXMLString	+= "\t<" + Constants.XFORMSDB_REQUEST_PARAMETER + " name=\"" + XMLEscapeUtils.escapeXml( StringUtils.decode( StringUtils.encode( requestParameterName, serverURIEncoding ), Constants.CLIENT_URI_ENCODING_DEFAULT ), "1.0", true ) + "\">" + XMLEscapeUtils.escapeXml( StringUtils.decode( StringUtils.encode( requestParameterValues[ i ], serverURIEncoding ), Constants.CLIENT_URI_ENCODING_DEFAULT ), "1.0", false ) + "</" + Constants.XFORMSDB_REQUEST_PARAMETER + ">" + Constants.LINE_SEPARATOR;
				}
			}
			if ( hasRequestParameters == false ) {
				requestParametersXMLString		+= "<" + Constants.XFORMSDB_REQUEST_PARAMETERS + " " + Constants.NAMESPACE_XMLNS_EMPTY + " " + Constants.NAMESPACE_XMLNS_XFORMSDB + " />";
			}
			else {
				requestParametersXMLString		+= "</" + Constants.XFORMSDB_REQUEST_PARAMETERS + ">";
			}
			logger.log( Level.DEBUG, "RequestParametersXMLString:" + Constants.LINE_SEPARATOR + requestParametersXMLString );
		} catch ( Exception ex ) {
			throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_59, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_59, ex  );
		}
		
		
		return requestParametersXMLString;
	}

	
	private static String getXFormsDBUserXMLString( HttpSession session, String encoding ) throws TransformerException {
		logger.log( Level.DEBUG, "Method has been called." );
		String xformsdbUserXMLString	= null;

		
		try {
			// Retrieve the logged in <xformsdb:user> element from the XFormsDB user manager
			xformsdbUserXMLString		= XFormsDBUserManager.getLoggedInXFormsDBUser( session );
			if ( xformsdbUserXMLString == null ) {
				xformsdbUserXMLString	= "<?xml version=\"" + Constants.OUTPUT_XML_VERSION_1_0 + "\" encoding=\"" + encoding + "\"?>" + Constants.LINE_SEPARATOR;
				xformsdbUserXMLString	+= "<" + Constants.XFORMSDB_USER + " " + Constants.NAMESPACE_XMLNS_EMPTY + " " + Constants.NAMESPACE_XMLNS_XFORMSDB + " />";
			}
			logger.log( Level.DEBUG, "XFormsDBUserXMLString:" + Constants.LINE_SEPARATOR + xformsdbUserXMLString );
		} catch ( ManagerException mex ) {
			throw new TransformerException( mex.getCode(), mex.getMessage(), mex );
		} catch ( Exception ex ) {
			throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_60, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_60, ex );
		}

		
		return xformsdbUserXMLString;
	}

	
	private static String getXFormsDBStateXMLString( HttpSession session, String encoding ) throws TransformerException {
		logger.log( Level.DEBUG, "Method has been called." );
		String xformsdbStateXMLString	= null;

		
		try {
			// Retrieve the web application's <xformsdb:state> element from the XFormsDB state manager
			xformsdbStateXMLString		= XFormsDBStateManager.getXFormsDBState( session );
			if ( xformsdbStateXMLString == null ) {
				xformsdbStateXMLString	= "<?xml version=\"" + Constants.OUTPUT_XML_VERSION_1_0 + "\" encoding=\"" + encoding + "\"?>" + Constants.LINE_SEPARATOR;
				xformsdbStateXMLString	+= "<" + Constants.XFORMSDB_STATE + " " + Constants.NAMESPACE_XMLNS_EMPTY + " " + Constants.NAMESPACE_XMLNS_XFORMSDB + " />";
			}
			logger.log( Level.DEBUG, "XFormsDBStateXMLString:" + Constants.LINE_SEPARATOR + xformsdbStateXMLString );
		} catch ( ManagerException mex ) {
			throw new TransformerException( mex.getCode(), mex.getMessage(), mex );
		} catch ( Exception ex ) {
			throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_61, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_61, ex );
		}

		
		return xformsdbStateXMLString;
	}

	
	// PUBLIC STATIC METHODS
	public static String transform( XFormsDBServlet xformsdbServlet, HttpServletRequest request, HttpServletResponse response, String encoding, String serverURIEncoding ) throws TransformerException {
		logger.log( Level.DEBUG, "Method has been called." );
		String realRequestedFilePath								= null;
		XFormsDBHandler xformsdbHandlerInclude						= null;
		SAXParser xformsdbSAXParserInclude							= null;
		Transformer xformsdbTransformerInclude						= null;
		String xformsdbInclude										= null;
		Transformer xformsdbTransformerSecview						= null;
		String xformsdbSecview										= null;
		Transformer xformsdbTransformerExtract						= null;
		String xformsdbExtract										= null;
		Document document											= null;
		Map<String, String> expressionTypeUpdateRequestInstances	= new HashMap<String, String>( 8 );
		Map<String, String> stateTypeSetRequestInstances			= new HashMap<String, String>( 8 );
		Map<String, String> fileTypeInsertRequestInstances			= new HashMap<String, String>( 8 );
		Map<String, String> fileTypeDeleteRequestInstances			= new HashMap<String, String>( 8 );
		Map<String, String> fileTypeUpdateRequestInstances			= new HashMap<String, String>( 8 );
		Transformer xformsdbTransformerXForms						= null;
		String requestBaseURIXMLString								= null;
		String requestHeadersXMLString								= null;
		String requestParametersXMLString							= null;
		String xformsdbUserXMLString								= null;
		String xformsdbStateXMLString								= null;
		String xformsdbXForms										= null;
		
		
		try {
			// Retrieve the real requested file path
			realRequestedFilePath				= XFormsDBTransformer.getRealRequestedFilePath( xformsdbServlet, request );

			// Retrieve the XFormsDB handler (include)
			xformsdbHandlerInclude				= XFormsDBTransformer.getXFormsDBHandler( xformsdbServlet );

			// Retrieve the XFormsDB SAX parser (include)
			xformsdbSAXParserInclude			= XFormsDBTransformer.getXFormsDBSAXParser( xformsdbHandlerInclude );

			// Parse the original XFormsDB document (include)
			XFormsDBTransformer.parseXFormsDBInclude( xformsdbSAXParserInclude, xformsdbHandlerInclude, realRequestedFilePath );

			// Retrieve the XFormsDB (include) transformer
			xformsdbTransformerInclude			= XFormsDBTransformer.getTransformer( xformsdbServlet.getXFormsDBIncludeFilePath() );

			// Transform the original XFormsDB document (include)
			xformsdbInclude						= XFormsDBTransformer.transformXFormsDBInclude( xformsdbTransformerInclude, xformsdbSAXParserInclude, realRequestedFilePath, encoding );

			// Retrieve the XFormsDB (secview) transformer
			xformsdbTransformerSecview			= XFormsDBTransformer.getTransformer( xformsdbServlet.getXFormsDBSecviewFilePath() );

			// Retrieve XFormsDB user XML string
			xformsdbUserXMLString				= XFormsDBTransformer.getXFormsDBUserXMLString( request.getSession(), encoding );

			// Transform the included XFormsDB document (secview)
			xformsdbSecview						= XFormsDBTransformer.transformXFormsDBSecview( xformsdbTransformerSecview, xformsdbInclude, encoding, xformsdbUserXMLString );

			// Retrieve the XFormsDB (extract) transformer
			xformsdbTransformerExtract			= XFormsDBTransformer.getTransformer( xformsdbServlet.getXFormsDBExtractFilePath() );

			// Transform the secviewed XFormsDB document (extract)
			xformsdbExtract						= XFormsDBTransformer.transformXFormsDBExtract( xformsdbTransformerExtract, xformsdbSecview, encoding );

			// Build the XOM document
			document							= XFormsDBTransformer.buildXOM( xformsdbExtract, encoding );
			logger.log( Level.DEBUG, "Extracted XML (before):\n" + document.toXML() );

			// Update the XOM document (<xforms:models> element)
			XFormsDBTransformer.updateXOMXFormsModels( response, document );
			
			// Update the XOM document (<xformsdb:submissions> element)
			XFormsDBTransformer.updateXOMXFormsDBSubmissions( response, document, expressionTypeUpdateRequestInstances, stateTypeSetRequestInstances, fileTypeInsertRequestInstances, fileTypeDeleteRequestInstances, fileTypeUpdateRequestInstances, URLDecoder.decode( request.getRequestURL().toString(), Constants.CLIENT_URI_ENCODING_DEFAULT ), encoding );

			// Update the XOM document (<xformsdb:instances> element)
			XFormsDBTransformer.updateXOMXFormsDBInstances( xformsdbServlet, request, document, expressionTypeUpdateRequestInstances, stateTypeSetRequestInstances, fileTypeInsertRequestInstances, fileTypeDeleteRequestInstances, fileTypeUpdateRequestInstances );

			// Update the XOM document (<xformsrtc:connection> element)
			XFormsDBTransformer.updateXOMXFormsRTCConnections( response, document );

			// Update the XOM document (<xformsrtc:connect> element)
			XFormsDBTransformer.updateXOMXFormsRTCConnects( document );

			// Update the XOM document (<xformsrtc:disconnect> element)
			XFormsDBTransformer.updateXOMXFormsRTCDisconnects( document );

			// Update the XOM document (<xformsrtc:send> element)
			XFormsDBTransformer.updateXOMXFormsRTCSends( document );

			// Update the XOM document (<xforms:loads> element)
			XFormsDBTransformer.updateXOMXFormsLoads( response, document );

			// Update the XOM document (<xforms:submissions> element)
			XFormsDBTransformer.updateXOMXFormsSubmissions( response, document );

			// Update the XOM document (<xhtml:metas> element)
			XFormsDBTransformer.updateXOMXHTMLMetas( response, document );

			// Update the XOM document (<xhtml:as> element)
			XFormsDBTransformer.updateXOMXHTMLAs( response, document );
			logger.log( Level.DEBUG, "Extracted XML (after):\n" + document.toXML() );

			// Add the <xformsdb:query> elements to XFormsDB query manager
			XFormsDBTransformer.addElementsToXFormsDBQueryManager( request.getSession(), document );

			// Add the <xformsdb:widgetquery> elements to XFormsDB widget query manager
			XFormsDBTransformer.addElementsToXFormsDBWidgetQueryManager( request.getSession(), document );

			// Add the <xformsdb:login> elements to XFormsDB login manager
			XFormsDBTransformer.addElementsToXFormsDBLoginManager( request.getSession(), document );

			// Add the <xformsdb:file> elements to XFormsDB file manager
			XFormsDBTransformer.addElementsToXFormsDBFileManager( request.getSession(), document );

			// Retrieve the XFormsDB (xforms) transformer
			xformsdbTransformerXForms			= XFormsDBTransformer.getTransformer( xformsdbServlet.getXFormsDBXFormsFilePath() );

			// Retrieve request base URI XML string
			requestBaseURIXMLString				= XFormsDBTransformer.getRequestBaseURIXMLString( request, encoding );

			// Retrieve request headers XML string
			requestHeadersXMLString				= XFormsDBTransformer.getRequestHeadersXMLString( request.getSession(), encoding );

			// Retrieve request parameters XML string
			requestParametersXMLString			= XFormsDBTransformer.getRequestParametersXMLString( request, encoding, serverURIEncoding );

			// Retrieve XFormsDB state XML string
			xformsdbStateXMLString				= XFormsDBTransformer.getXFormsDBStateXMLString( request.getSession(), encoding );

			// Transform the secviewed XFormsDB document (xforms)
			xformsdbXForms						= XFormsDBTransformer.transformXFormsDBXForms( xformsdbTransformerXForms, xformsdbSecview, encoding, requestBaseURIXMLString, requestHeadersXMLString, requestParametersXMLString, xformsdbStateXMLString, document );
			logger.log( Level.DEBUG, "The XFormsDB document has been successfully transformed." );
		} catch ( TransformerException tex ) {
			throw tex;
		} catch ( Exception ex ) {
			throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_58, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_58, ex );
		}
		
		
		return xformsdbXForms;
	}	
}