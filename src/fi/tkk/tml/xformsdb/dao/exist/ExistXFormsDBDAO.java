package fi.tkk.tml.xformsdb.dao.exist;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.OutputKeys;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.Serializer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.exist.xmldb.XQueryService;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.CompiledExpression;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

import fi.tkk.tml.xformsdb.core.Constants;
import fi.tkk.tml.xformsdb.dao.DAODataSource;
import fi.tkk.tml.xformsdb.dao.XFormsDBDAO;
import fi.tkk.tml.xformsdb.error.AuthenticationException;
import fi.tkk.tml.xformsdb.error.DAOException;
import fi.tkk.tml.xformsdb.error.ErrorConstants;
import fi.tkk.tml.xformsdb.error.FileException;
import fi.tkk.tml.xformsdb.error.XMLException;
import fi.tkk.tml.xformsdb.util.FileUtils;
import fi.tkk.tml.xformsdb.util.IOUtils;
import fi.tkk.tml.xformsdb.util.StringUtils;
import fi.tkk.tml.xformsdb.util.XMLUtils;


/**
 * ExistXFormsDBDAO implementation of the XFormsDBDAO
 * interface. This class can contain all Exist specific
 * code.
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on February 23, 2010
 */
public final class ExistXFormsDBDAO implements XFormsDBDAO {
	

	
	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger					= Logger.getLogger( ExistXFormsDBDAO.class );

	
	// PRIVATE VARIABLES
	private DAODataSource daoDataSource;
	
	
	// PRIVATE METHODS
	private String[] fixCollectionAndDoc( String collection, String doc ) {
		String[] collectionAndDoc						= new String[ 2 ];
		

		// Fix collection
		if ( collection != null ) {
			// Trim collection
			collection									= collection.trim();
			
			// Remove slashes from the end of collection
			int lastIndexOfSlash						= 0;
			boolean removeSlashesFromEnd				= true;
			while ( removeSlashesFromEnd == true ) {
				lastIndexOfSlash						= collection.lastIndexOf( "/" );
				if ( lastIndexOfSlash != -1 && collection.length() == ( lastIndexOfSlash + 1 ) ) {
					collection							= collection.substring( 0, lastIndexOfSlash );
				}
				else {
					removeSlashesFromEnd				= false;
				}
			}
		}

		// Fix doc
		if ( doc != null ) {
			// Trim doc
			doc											= doc.trim();
	
			// Remove slashes from the beginning of doc
			int firstIndexOfSlash						= 0;
			boolean removeSlashesFromBeginning			= true;
			while ( removeSlashesFromBeginning == true ) {
				firstIndexOfSlash						= doc.indexOf( "/" );
				if ( firstIndexOfSlash == 0 ) {
					doc									= doc.substring( ( firstIndexOfSlash + 1 ) );
				}
				else {
					removeSlashesFromBeginning			= false;
				}
			}
			
			// Final fix for the collection and doc
			int docLastIndexOfSlash						= doc.lastIndexOf( "/" );
			if ( docLastIndexOfSlash != -1 ) {
				collection								= collection + "/" + doc.subSequence( 0, docLastIndexOfSlash );
				doc										= doc.substring( ( docLastIndexOfSlash + 1 ) );
			}
		}
		
		// Set values
		collectionAndDoc[ 0 ]							= collection;
		collectionAndDoc[ 1 ]							= doc;

		
		return collectionAndDoc;
	}
		
	
	// PUBLIC CONSTRUCTORS
	/**
	 * Construct the ExistXFormsDBDAO.
	 * 
	 * 
	 * @param daoDataSource					DAO data source that enables the retrieval of the collection.
	 */
	public ExistXFormsDBDAO( DAODataSource daoDataSource ) {
		logger.log( Level.DEBUG, "Constructor has been called." );
		this.daoDataSource								= daoDataSource;
	}
	
	
	// PUBLIC METHODS	
	/**
	 * Execute the select query expression and get the result in XML format.
	 * 
	 * 
	 * @param document			The XML document (<xformsdb:query> element).
	 * @param encoding			The character encoding to be used.
	 * @return xmlString		The result of the query in XML format.
	 * @throws DAOException		DAO exception.
	 */
	public String select( Document document, String encoding ) throws DAOException {
		logger.log( Level.DEBUG, "Method has been called." );
		String collectionString							= null;
		String docString								= null;
		Collection collection							= null;
		Element xformsdbQueryElement					= null;
		Element xformsdbExpressionElement				= null;
		Elements xformsdbXmlnsElements					= null;
		Elements xformsdbVarElements					= null;
		Elements xformsdbSecvarElements					= null;
		Element xformsdbXmlnsElement					= null;
		Element xformsdbVarElement						= null;
		Element xformsdbSecvarElement					= null;
		String xqe										= null;
		String xmlString								= null;
		String xmlStringFiltered						= null;

		
		try {
			// Retrieve the needed elements
			xformsdbQueryElement						= document.getRootElement();
			xformsdbExpressionElement					= xformsdbQueryElement.getFirstChildElement( "expression", Constants.NAMESPACE_URI_XFORMSDB );
			xformsdbXmlnsElements						= xformsdbQueryElement.getChildElements( "xmlns", Constants.NAMESPACE_URI_XFORMSDB );
			xformsdbVarElements							= xformsdbQueryElement.getChildElements( "var", Constants.NAMESPACE_URI_XFORMSDB );
			xformsdbSecvarElements						= xformsdbQueryElement.getChildElements( "secvar", Constants.NAMESPACE_URI_XFORMSDB );
			xformsdbXmlnsElement						= null;
			xformsdbVarElement							= null;
			xformsdbSecvarElement						= null;
			String xqueryStr							= null;
			String inlineXQueryStr						= null;

			// Fix collection and doc
			docString									= ( ( xformsdbQueryElement.getAttribute( "doc" ) == null ) ? null : xformsdbQueryElement.getAttributeValue( "doc" ) );
			collectionString							= this.daoDataSource.getCollection();
			String collectionAndDoc[]					= this.fixCollectionAndDoc( collectionString, docString );
			collectionString							= collectionAndDoc[ 0 ];
			docString									= collectionAndDoc[ 1 ];

			// Get the collection
			collection									= this.daoDataSource.getCollection( ExistDAOFactory.DRIVER, collectionString );
			
			// Get the XQuery service
			XQueryService service						= ( XQueryService ) collection.getService( "XQueryService", "1.0" );
			service.setProperty( OutputKeys.OMIT_XML_DECLARATION, Constants.OUTPUT_XML_OMIT_XML_DECLARATION_NO );
			service.setProperty( OutputKeys.METHOD, Constants.OUTPUT_XML_METHOD_XML );
			service.setProperty( OutputKeys.VERSION, Constants.OUTPUT_XML_VERSION_1_0 );
			service.setProperty( OutputKeys.ENCODING, encoding );
			service.setProperty( OutputKeys.INDENT, Constants.OUTPUT_XML_INDENT_YES );
			//service.setProperty( OutputKeys.STANDALONE, "no" );
			//service.setProperty( OutputKeys.MEDIA_TYPE, "text/xml" );

			// Get the XPath object for compiling the xpath string
			//XPathFactory xpathFactory					= XPathFactory.newInstance();
			//XPath xpath								= xpathFactory.newXPath();
			String xpathStr								= null;

			
			// Retrieve the XPath & XQuery strings, prefer resource over inline
			// The queryStr variable is just for testing the existence of the query as well as for optimistic printing of the query using XFormsDB encoding as there is no way to read/parse the encoding of the query itself
			if ( xformsdbExpressionElement.getAttribute( "resource" ) != null ) {
				try {
					xqueryStr							= IOUtils.convert( FileUtils.getFileAsInputStream( xformsdbExpressionElement.getAttributeValue( "resource" ) ), encoding, true );
				} catch ( FileException fex ) {
					throw new FileException( ErrorConstants.ERROR_CODE_DAO_25, ErrorConstants.ERROR_MESSAGE_DAO_25_1 + xformsdbQueryElement.getAttributeValue( "id" ) + ErrorConstants.ERROR_MESSAGE_DAO_25_2 );
				}
				
				if ( xqueryStr != null ) {
					xqueryStr							= xqueryStr.trim();
				}
				xpathStr								= xqueryStr;
			}
			else if ( xformsdbExpressionElement.getAttribute( "resource" ) == null && xformsdbExpressionElement.getChildCount() > 0 ) {
				inlineXQueryStr							= "";
				for ( int i = 0; i < xformsdbExpressionElement.getChildCount(); i++ ) {
					inlineXQueryStr						+= xformsdbExpressionElement.getChild( i ).toXML();
				}
				xqueryStr								= inlineXQueryStr.trim();
				xpathStr								= xqueryStr; 
			}
			else {
				// select4update
				if ( Constants.EXPRESSION_TYPE_SELECT4UPDATE.equals( xformsdbQueryElement.getAttributeValue( "type" ) ) == true ) {
					throw new DAOException( ErrorConstants.ERROR_CODE_DAO_4, ErrorConstants.ERROR_MESSAGE_DAO_4 );
				}
				// select
				else {
					throw new DAOException( ErrorConstants.ERROR_CODE_DAO_5, ErrorConstants.ERROR_MESSAGE_DAO_5 );
				}
			}

			// Check the XPath & XQuery expression strings
			if ( xpathStr == null && Constants.EXPRESSION_TYPE_SELECT4UPDATE.equals( xformsdbQueryElement.getAttributeValue( "type" ) ) == true ) {
				throw new DAOException( ErrorConstants.ERROR_CODE_DAO_6, ErrorConstants.ERROR_MESSAGE_DAO_6 );
			}
			else if ( xqueryStr == null && Constants.EXPRESSION_TYPE_SELECT.equals( xformsdbQueryElement.getAttributeValue( "type" ) ) == true ) {
				throw new DAOException( ErrorConstants.ERROR_CODE_DAO_7, ErrorConstants.ERROR_MESSAGE_DAO_7 );
			}
			
			
			// select4update
			if ( Constants.EXPRESSION_TYPE_SELECT4UPDATE.equals( xformsdbQueryElement.getAttributeValue( "type" ) ) == true ) {
				// Check/compile the XPath string, i.e. make sure the update query string uses XPath, not XQuery
				//xpath.compile( xpathStr );
				// Add XQuery version
				xqe										= "xquery version \"" + Constants.OUTPUT_XQUERY_VERSION_1_0 + "\" encoding \"" + encoding + "\";" + Constants.LINE_SEPARATOR + Constants.LINE_SEPARATOR;

				// Declare namespaces
				for ( int i = 0; i < xformsdbXmlnsElements.size(); i++ ) {
					xformsdbXmlnsElement				= xformsdbXmlnsElements.get( i );
					xqe									+= "declare namespace " + xformsdbXmlnsElement.getAttributeValue( "prefix" ) + "=\"" + xformsdbXmlnsElement.getAttributeValue( "uri" ) + "\";" + Constants.LINE_SEPARATOR;
				}

				// Declare external (normal) variables
				for ( int i = 0; i < xformsdbVarElements.size(); i++ ) {
					xformsdbVarElement					= xformsdbVarElements.get( i );
					xqe									+= "declare variable $" + xformsdbVarElement.getAttributeValue( "name" ) + " external;" + Constants.LINE_SEPARATOR;
				}
				// Declare external (security) variables
				for ( int i = 0; i < xformsdbSecvarElements.size(); i++ ) {
					xformsdbSecvarElement				= xformsdbSecvarElements.get( i );
					xqe									+= "declare variable $" + xformsdbSecvarElement.getAttributeValue( "name" ) + " external;" + Constants.LINE_SEPARATOR;
				}
				// Add extra empty lines
				xqe										+= Constants.LINE_SEPARATOR + Constants.LINE_SEPARATOR;
				xqe										+= xpathStr;
			}
			// select
			else {
				xqe										= xqueryStr;
			}
			// The XQuery expression to be executed
			logger.log( Level.DEBUG, "XQuery:" + Constants.LINE_SEPARATOR + xqe );
			
			// Compile the query expression
			CompiledExpression compiledExpression		= service.compile( xqe );


			// Set the external (normal) variables
			Document xformsdbVarElementContentsDocument	= null;
			ByteArrayOutputStream outputStream			= null;
			Serializer serializer						= null;
			String xformsdbVarElementValue				= null;
			for ( int i = 0; i < xformsdbVarElements.size(); i++ ) {
				xformsdbVarElement						= xformsdbVarElements.get( i );
				xformsdbVarElementValue					= "";
				// Check the contents of the <xformsdb:var> element
				if ( xformsdbVarElement.getChildElements().size() == 0 ) {
					xformsdbVarElementValue				= xformsdbVarElement.getValue();
				}
				else {
					// Retrieve the contents of the <xformsdb:var> element
					xformsdbVarElementContentsDocument	= new Document( new Element( xformsdbVarElement.getChildElements().get( 0 ) ) );				

					// Serialize the document
					outputStream						= new ByteArrayOutputStream();
					serializer							= new Serializer( outputStream, encoding );
					serializer.write( xformsdbVarElementContentsDocument );			
					
					// Change the encoding
					xformsdbVarElementValue				= outputStream.toString( encoding );
				}
				service.declareVariable( xformsdbVarElement.getAttributeValue( "name" ), xformsdbVarElementValue );
				logger.log( Level.DEBUG, "External (normal) variable: " + xformsdbVarElement.getAttributeValue( "name" ) + "=" + xformsdbVarElementValue );
			}
			// Set the external (security) variables
			for ( int i = 0; i < xformsdbSecvarElements.size(); i++ ) {
				xformsdbSecvarElement					= xformsdbSecvarElements.get( i );
				service.declareVariable( xformsdbSecvarElement.getAttributeValue( "name" ), xformsdbSecvarElement.getValue() );
				logger.log( Level.DEBUG, "External (security) variable: " + xformsdbSecvarElement.getAttributeValue( "name" ) + "=" + xformsdbSecvarElement.getValue() );
			}
			
			// Execute the compiled XQuery
			ResourceSet resourceSet						= null;
			// Execute the compiled XQuery against collection
			if ( docString == null ) {
				resourceSet								= service.execute( compiledExpression );
			}
			// Execute the compiled XQuery against XML document
			else {
				resourceSet								= service.execute( ( XMLResource ) collection.getResource( docString ), compiledExpression );
			}

			// Iterate over the results
			ResourceIterator resourceIt					= resourceSet.getIterator();
			if ( resourceIt.hasMoreResources() == true ) {
				xmlString								= "";
			}
			while( resourceIt.hasMoreResources() == true ) {
				Resource resource						= resourceIt.nextResource();
				xmlString								+= ( String ) resource.getContent();
				
				if ( resourceIt.hasMoreResources() ) {
					xmlString							+= Constants.LINE_SEPARATOR;					
				}
			}
			
			 // Ensure that the result does not consist only of the XML declaration
			try {
				xmlStringFiltered						= XMLUtils.filterXMLDeclaration( xmlString );
			} catch ( XMLException xmlex ) {
				xmlStringFiltered						= null;
			} catch ( Exception ex ) {
				xmlStringFiltered						= null;
			}
			
			// Query did not result any data 
			if ( xmlString == null || xmlStringFiltered.length() == 0 ) {
				throw new DAOException( ErrorConstants.ERROR_CODE_DAO_52, ErrorConstants.ERROR_MESSAGE_DAO_52 );
			}

			logger.log( Level.DEBUG, "Result of the XQuery:" + Constants.LINE_SEPARATOR + xmlString );
		//} catch ( XPathExpressionException xpatheex ) {
			//throw new DAOException( ErrorConstants.ERROR_CODE_DAO_8, ExceptionUtils.getRootCauseMessage( xpatheex ), xpatheex );
		} catch ( XMLDBException xmldbex ) {
			throw new DAOException( ErrorConstants.ERROR_CODE_DAO_9, ErrorConstants.ERROR_MESSAGE_DAO_9 + xmldbex.errorCode + ").", xmldbex );
		} catch ( FileException fex ) {
			throw new DAOException( ErrorConstants.ERROR_CODE_DAO_28, ErrorConstants.ERROR_MESSAGE_DAO_28_1 + xformsdbQueryElement.getAttributeValue( "id" ) + ErrorConstants.ERROR_MESSAGE_DAO_28_2, fex );
		} catch ( DAOException daoex ) {
			throw daoex;
		} catch ( Exception ex ) {
			throw new DAOException( ErrorConstants.ERROR_CODE_DAO_10, ErrorConstants.ERROR_MESSAGE_DAO_10_1 + xformsdbQueryElement.getAttributeValue( "id" ) + ErrorConstants.ERROR_MESSAGE_DAO_10_2, ex );
		} finally {
			if ( collection != null ) {
				try {
					collection.close();
				} catch ( XMLDBException xmldbex ) {
					logger.log( Level.ERROR, "Failed to close the resources (error code: " + xmldbex.errorCode + ").", xmldbex );
				}
			}
		}
		
		
		return xmlString;
	}
	
	
	/**
	 * Execute the update query expression and get the result in XML format.
	 * 
	 * 
	 * @param document				The XML document (<xformsdb:query> element).
	 * @param tm					The merged XML document; 3DM result.
	 * @param encoding				The character encoding to be used.
	 * @return xmlString			The result of the query in XML format.
	 * @throws DAOException			DAO exception.
	 */
	public String update( Document document, String tm, String encoding ) throws DAOException {
		logger.log( Level.DEBUG, "Method has been called." );
		String collectionString							= null;
		String docString								= null;
		Collection collection							= null;
		Element xformsdbQueryElement					= null;
		Element xformsdbExpressionElement				= null;
		Elements xformsdbXmlnsElements					= null;
		Elements xformsdbVarElements					= null;
		Elements xformsdbSecvarElements					= null;
		Element xformsdbXmlnsElement					= null;
		Element xformsdbVarElement						= null;
		Element xformsdbSecvarElement					= null;
		String xqe										= null;
		String xmlString								= null;
		String xmlStringFiltered						= null;

		
		try {
			// Retrieve the needed elements
			xformsdbQueryElement						= document.getRootElement();
			xformsdbExpressionElement					= xformsdbQueryElement.getFirstChildElement( "expression", Constants.NAMESPACE_URI_XFORMSDB );
			xformsdbXmlnsElements						= xformsdbQueryElement.getChildElements( "xmlns", Constants.NAMESPACE_URI_XFORMSDB );
			xformsdbVarElements							= xformsdbQueryElement.getChildElements( "var", Constants.NAMESPACE_URI_XFORMSDB );
			xformsdbSecvarElements						= xformsdbQueryElement.getChildElements( "secvar", Constants.NAMESPACE_URI_XFORMSDB );
			xformsdbXmlnsElement						= null;
			xformsdbVarElement							= null;
			xformsdbSecvarElement						= null;
			String inlineXQueryStr						= null;

			// Fix collection and doc
			docString									= ( ( xformsdbQueryElement.getAttribute( "doc" ) == null ) ? null : xformsdbQueryElement.getAttributeValue( "doc" ) );
			collectionString							= this.daoDataSource.getCollection();
			String collectionAndDoc[]					= this.fixCollectionAndDoc( collectionString, docString );
			collectionString							= collectionAndDoc[ 0 ];
			docString									= collectionAndDoc[ 1 ];

			// Get the collection
			collection									= this.daoDataSource.getCollection( ExistDAOFactory.DRIVER, collectionString );
			
			// Get the XQuery service
			XQueryService service						= ( XQueryService ) collection.getService( "XQueryService", "1.0" );
			service.setProperty( OutputKeys.OMIT_XML_DECLARATION, Constants.OUTPUT_XML_OMIT_XML_DECLARATION_NO );
			service.setProperty( OutputKeys.METHOD, Constants.OUTPUT_XML_METHOD_XML );
			service.setProperty( OutputKeys.VERSION, Constants.OUTPUT_XML_VERSION_1_0 );
			service.setProperty( OutputKeys.ENCODING, encoding );
			service.setProperty( OutputKeys.INDENT, Constants.OUTPUT_XML_INDENT_YES );
			//service.setProperty( OutputKeys.STANDALONE, "no" );
			//service.setProperty( OutputKeys.MEDIA_TYPE, "text/xml" );

			// Get the XPath object for compiling the xpath string
			//XPathFactory xpathFactory					= XPathFactory.newInstance();
			//XPath xpath								= xpathFactory.newXPath();
			String xpathStr								= null;
			
			
			// Retrieve the XPath string
			// The queryStr variable is just for testing the existence of the query as well as for optimistic printing of the query using XFormsDB encoding as there is no way to read/parse the encoding of the query itself
			if ( xformsdbExpressionElement.getAttribute( "resource" ) != null ) {
				try {
					xpathStr							= IOUtils.convert( FileUtils.getFileAsInputStream( xformsdbExpressionElement.getAttributeValue( "resource" ) ), encoding, true );
				} catch ( FileException fex ) {
					throw new FileException( ErrorConstants.ERROR_CODE_DAO_25, ErrorConstants.ERROR_MESSAGE_DAO_25_1 + xformsdbQueryElement.getAttributeValue( "id" ) + ErrorConstants.ERROR_MESSAGE_DAO_25_2 );
				}
				
				if ( xpathStr != null ) {
					xpathStr							= xpathStr.trim();
				}
			}
			else if ( xformsdbExpressionElement.getAttribute( "resource" ) == null && xformsdbExpressionElement.getChildCount() > 0 ) {
				inlineXQueryStr							= "";
				for ( int i = 0; i < xformsdbExpressionElement.getChildCount(); i++ ) {
					inlineXQueryStr						+= xformsdbExpressionElement.getChild( i ).toXML();
				}
				xpathStr								= inlineXQueryStr.trim();
			}
			else {
				throw new DAOException( ErrorConstants.ERROR_CODE_DAO_11, ErrorConstants.ERROR_MESSAGE_DAO_11 );
			}
			
			// Check the xPath & XQuery expression strings
			if ( xpathStr == null && Constants.EXPRESSION_TYPE_UPDATE.equals( xformsdbQueryElement.getAttributeValue( "type" ) ) == true ) {
				throw new DAOException( ErrorConstants.ERROR_CODE_DAO_12, ErrorConstants.ERROR_MESSAGE_DAO_12 );
			}
			

			// update
			if ( Constants.EXPRESSION_TYPE_UPDATE.equals( xformsdbQueryElement.getAttributeValue( "type" ) ) == true ) {
				// Check/compile the XPath string, i.e. make sure the update query string uses XPath, not XQuery
				//xpath.compile( xpathStr );
				// Add XQuery version
				xqe										= "xquery version \"" + Constants.OUTPUT_XQUERY_VERSION_1_0 + "\" encoding \"" + encoding + "\";" + Constants.LINE_SEPARATOR + Constants.LINE_SEPARATOR;
				
				// Declare namespaces
				for ( int i = 0; i < xformsdbXmlnsElements.size(); i++ ) {
					xformsdbXmlnsElement				= xformsdbXmlnsElements.get( i );
					xqe									+= "declare namespace " + xformsdbXmlnsElement.getAttributeValue( "prefix" ) + "=\"" + xformsdbXmlnsElement.getAttributeValue( "uri" ) + "\";" + Constants.LINE_SEPARATOR;
				}

				// Declare external (normal) variables
				for ( int i = 0; i < xformsdbVarElements.size(); i++ ) {					
					xformsdbVarElement					= xformsdbVarElements.get( i );
					xqe									+= "declare variable $" + xformsdbVarElement.getAttributeValue( "name" ) + " external;" + Constants.LINE_SEPARATOR;
					
				}
				// Declare external (security) variables
				for ( int i = 0; i < xformsdbSecvarElements.size(); i++ ) {					
					xformsdbSecvarElement				= xformsdbSecvarElements.get( i );
					xqe									+= "declare variable $" + xformsdbSecvarElement.getAttributeValue( "name" ) + " external;" + Constants.LINE_SEPARATOR;
					
				}
				// Add extra empty lines
				xqe										+= Constants.LINE_SEPARATOR + Constants.LINE_SEPARATOR;
				xqe										+= "let $updateResult := update replace " + xpathStr + " with " + tm + Constants.LINE_SEPARATOR;
				xqe										+= "return " + xpathStr;
			}			
			logger.log( Level.DEBUG, "XQuery:" + Constants.LINE_SEPARATOR + xqe );
			
			// Compile the query expression
			CompiledExpression compiledExpression		= service.compile( xqe );


			// Set the external (normal) variables
			Document xformsdbVarElementContentsDocument	= null;
			ByteArrayOutputStream outputStream			= null;
			Serializer serializer						= null;
			String xformsdbVarElementValue				= null;
			for ( int i = 0; i < xformsdbVarElements.size(); i++ ) {
				xformsdbVarElement						= xformsdbVarElements.get( i );
				xformsdbVarElementValue					= "";
				// Check the contents of the <xformsdb:var> element
				if ( xformsdbVarElement.getChildElements().size() == 0 ) {
					xformsdbVarElementValue				= xformsdbVarElement.getValue();
				}
				else {
					// Retrieve the contents of the <xformsdb:var> element
					xformsdbVarElementContentsDocument	= new Document( new Element( xformsdbVarElement.getChildElements().get( 0 ) ) );				

					// Serialize the document
					outputStream						= new ByteArrayOutputStream();
					serializer							= new Serializer( outputStream, encoding );
					serializer.write( xformsdbVarElementContentsDocument );			
					
					// Change the encoding
					xformsdbVarElementValue				= outputStream.toString( encoding );
				}
				service.declareVariable( xformsdbVarElement.getAttributeValue( "name" ), xformsdbVarElementValue );
				logger.log( Level.DEBUG, "External (normal) variable: " + xformsdbVarElement.getAttributeValue( "name" ) + "=" + xformsdbVarElementValue );
			}
			// Set the external (security) variables
			for ( int i = 0; i < xformsdbSecvarElements.size(); i++ ) {
				xformsdbSecvarElement					= xformsdbSecvarElements.get( i );
				service.declareVariable( xformsdbSecvarElement.getAttributeValue( "name" ), xformsdbSecvarElement.getValue() );
				logger.log( Level.DEBUG, "External (security) variable: " + xformsdbSecvarElement.getAttributeValue( "name" ) + "=" + xformsdbSecvarElement.getValue() );
			}

			// Execute the compiled XQuery
			ResourceSet resourceSet						= null;
			// Execute the compiled XQuery against collection
			if ( docString == null ) {
				resourceSet								= service.execute( compiledExpression );
			}
			// Execute the compiled XQuery against XML document
			else {
				resourceSet								= service.execute( ( XMLResource ) collection.getResource( docString ), compiledExpression );
			}

			// Iterate over the results
			ResourceIterator resourceIt					= resourceSet.getIterator();
			if ( resourceIt.hasMoreResources() == true ) {
				xmlString								= "";
			}
			while( resourceIt.hasMoreResources() == true ) {
				Resource resource						= resourceIt.nextResource();
				xmlString								+= ( String ) resource.getContent();
				
				if ( resourceIt.hasMoreResources() ) {
					xmlString							+= Constants.LINE_SEPARATOR;					
				}
			}
			
			 // Ensure that the result does not consist only of the XML declaration
			try {
				xmlStringFiltered						= XMLUtils.filterXMLDeclaration( xmlString );
			} catch ( XMLException xmlex ) {
				xmlStringFiltered						= null;
			} catch ( Exception ex ) {
				xmlStringFiltered						= null;
			}
			
			// Query did not result any data 
			if ( xmlString == null || xmlStringFiltered.length() == 0 ) {
				throw new DAOException( ErrorConstants.ERROR_CODE_DAO_51, ErrorConstants.ERROR_MESSAGE_DAO_51 );
			}

			logger.log( Level.DEBUG, "Result of the XQuery:" + Constants.LINE_SEPARATOR + xmlString );
		//} catch ( XPathExpressionException xpatheex ) {
			//throw new DAOException( ErrorConstants.ERROR_CODE_DAO_13, ExceptionUtils.getRootCauseMessage( xpatheex ), xpatheex );
		} catch ( XMLDBException xmldbex ) {
			throw new DAOException( ErrorConstants.ERROR_CODE_DAO_14, ErrorConstants.ERROR_MESSAGE_DAO_14 + xmldbex.errorCode + ").", xmldbex );
		} catch ( FileException fex ) {
			throw new DAOException( ErrorConstants.ERROR_CODE_DAO_30, fex.getMessage(), fex );
		} catch ( DAOException daoex ) {
			throw daoex;
		} catch ( Exception ex ) {
			throw new DAOException( ErrorConstants.ERROR_CODE_DAO_15, ErrorConstants.ERROR_MESSAGE_DAO_15, ex );
		} finally {
			if ( collection != null ) {
				try {
					collection.close();
				} catch ( XMLDBException xmldbex ) {
					logger.log( Level.ERROR, "Failed to close the resources (error code: " + xmldbex.errorCode + ").", xmldbex );
				}
			}
		}
		
		
		return xmlString;
	}
	
	
	/**
	 * Execute all query expression and get the result in XML format.
	 * 
	 * 
	 * @param document			The XML document (<xformsdb:query> element).
	 * @param encoding			The character encoding to be used.
	 * @return xmlString		The result of the query in XML format.
	 * @throws DAOException		DAO exception.
	 */
	public String all( Document document, String encoding ) throws DAOException {
		logger.log( Level.DEBUG, "Method has been called." );
		String collectionString							= null;
		String docString								= null;
		Collection collection							= null;
		Element xformsdbQueryElement					= null;
		Element xformsdbExpressionElement				= null;
		Elements xformsdbVarElements					= null;
		Elements xformsdbSecvarElements					= null;
		Element xformsdbVarElement						= null;
		Element xformsdbSecvarElement					= null;
		String xqe										= null;
		String xmlString								= null;
		String xmlStringFiltered						= null;

		
		try {
			// Retrieve the needed elements
			xformsdbQueryElement						= document.getRootElement();
			xformsdbExpressionElement					= xformsdbQueryElement.getFirstChildElement( "expression", Constants.NAMESPACE_URI_XFORMSDB );
			xformsdbVarElements							= xformsdbQueryElement.getChildElements( "var", Constants.NAMESPACE_URI_XFORMSDB );
			xformsdbSecvarElements						= xformsdbQueryElement.getChildElements( "secvar", Constants.NAMESPACE_URI_XFORMSDB );
			xformsdbVarElement							= null;
			xformsdbSecvarElement						= null;
			String xqueryStr							= null;
			String inlineXQueryStr						= null;

			// Fix collection and doc
			docString									= ( ( xformsdbQueryElement.getAttribute( "doc" ) == null ) ? null : xformsdbQueryElement.getAttributeValue( "doc" ) );
			collectionString							= this.daoDataSource.getCollection();
			String collectionAndDoc[]					= this.fixCollectionAndDoc( collectionString, docString );
			collectionString							= collectionAndDoc[ 0 ];
			docString									= collectionAndDoc[ 1 ];
			
			// Get the collection
			collection									= this.daoDataSource.getCollection( ExistDAOFactory.DRIVER, collectionString );
			
			// Get the XQuery service
			XQueryService service						= ( XQueryService ) collection.getService( "XQueryService", "1.0" );
			service.setProperty( OutputKeys.OMIT_XML_DECLARATION, Constants.OUTPUT_XML_OMIT_XML_DECLARATION_NO );
			service.setProperty( OutputKeys.METHOD, Constants.OUTPUT_XML_METHOD_XML );
			service.setProperty( OutputKeys.VERSION, Constants.OUTPUT_XML_VERSION_1_0 );
			service.setProperty( OutputKeys.ENCODING, encoding );
			service.setProperty( OutputKeys.INDENT, Constants.OUTPUT_XML_INDENT_YES );
			//service.setProperty( OutputKeys.STANDALONE, "no" );
			//service.setProperty( OutputKeys.MEDIA_TYPE, "text/xml" );

			
			// Retrieve the XQuery string, prefer resource over inline
			// The queryStr variable is just for testing the existence of the query as well as for optimistic printing of the query using XFormsDB encoding as there is no way to read/parse the encoding of the query itself
			if ( xformsdbExpressionElement.getAttribute( "resource" ) != null ) {
				try {
					xqueryStr							= IOUtils.convert( FileUtils.getFileAsInputStream( xformsdbExpressionElement.getAttributeValue( "resource" ) ), encoding, true );
				} catch ( FileException fex ) {
					throw new FileException( ErrorConstants.ERROR_CODE_DAO_56, ErrorConstants.ERROR_MESSAGE_DAO_56_1 + xformsdbQueryElement.getAttributeValue( "id" ) + ErrorConstants.ERROR_MESSAGE_DAO_56_2 );
				}
				
				if ( xqueryStr != null ) {
					xqueryStr							= xqueryStr.trim();
				}
			}
			else if ( xformsdbExpressionElement.getAttribute( "resource" ) == null && xformsdbExpressionElement.getChildCount() > 0 ) {
				inlineXQueryStr							= "";
				for ( int i = 0; i < xformsdbExpressionElement.getChildCount(); i++ ) {
					inlineXQueryStr						+= xformsdbExpressionElement.getChild( i ).toXML();
				}
				xqueryStr								= inlineXQueryStr.trim();
			}
			else {
				throw new DAOException( ErrorConstants.ERROR_CODE_DAO_54, ErrorConstants.ERROR_MESSAGE_DAO_54 );
			}

			// Check the XQuery expression string
			if ( xqueryStr == null ) {
				throw new DAOException( ErrorConstants.ERROR_CODE_DAO_55, ErrorConstants.ERROR_MESSAGE_DAO_55 );
			}
			
			
			xqe											= xqueryStr;
			// The XQuery expression to be executed
			logger.log( Level.DEBUG, "XQuery:" + Constants.LINE_SEPARATOR + xqe );
			
			// Compile the query expression
			CompiledExpression compiledExpression		= service.compile( xqe );


			// Set the external (normal) variables
			Document xformsdbVarElementContentsDocument	= null;
			ByteArrayOutputStream outputStream			= null;
			Serializer serializer						= null;
			String xformsdbVarElementValue				= null;
			for ( int i = 0; i < xformsdbVarElements.size(); i++ ) {
				xformsdbVarElement						= xformsdbVarElements.get( i );
				xformsdbVarElementValue					= "";
				// Check the contents of the <xformsdb:var> element
				if ( xformsdbVarElement.getChildElements().size() == 0 ) {
					xformsdbVarElementValue				= xformsdbVarElement.getValue();
				}
				else {
					// Retrieve the contents of the <xformsdb:var> element
					xformsdbVarElementContentsDocument	= new Document( new Element( xformsdbVarElement.getChildElements().get( 0 ) ) );				

					// Serialize the document
					outputStream						= new ByteArrayOutputStream();
					serializer							= new Serializer( outputStream, encoding );
					serializer.write( xformsdbVarElementContentsDocument );			
					
					// Change the encoding
					xformsdbVarElementValue				= outputStream.toString( encoding );
				}
				service.declareVariable( xformsdbVarElement.getAttributeValue( "name" ), xformsdbVarElementValue );
				logger.log( Level.DEBUG, "External (normal) variable: " + xformsdbVarElement.getAttributeValue( "name" ) + "=" + xformsdbVarElementValue );
			}
			// Set the external (security) variables
			for ( int i = 0; i < xformsdbSecvarElements.size(); i++ ) {
				xformsdbSecvarElement					= xformsdbSecvarElements.get( i );
				service.declareVariable( xformsdbSecvarElement.getAttributeValue( "name" ), xformsdbSecvarElement.getValue() );
				logger.log( Level.DEBUG, "External (security) variable: " + xformsdbSecvarElement.getAttributeValue( "name" ) + "=" + xformsdbSecvarElement.getValue() );
			}
			
			// Execute the compiled XQuery
			ResourceSet resourceSet						= null;
			// Execute the compiled XQuery against collection
			if ( docString == null ) {
				resourceSet								= service.execute( compiledExpression );
			}
			// Execute the compiled XQuery against XML document
			else {
				resourceSet								= service.execute( ( XMLResource ) collection.getResource( docString ), compiledExpression );
			}

			// Iterate over the results
			ResourceIterator resourceIt					= resourceSet.getIterator();
			if ( resourceIt.hasMoreResources() == true ) {
				xmlString								= "";
			}
			while( resourceIt.hasMoreResources() == true ) {
				Resource resource						= resourceIt.nextResource();
				xmlString								+= ( String ) resource.getContent();
				
				if ( resourceIt.hasMoreResources() ) {
					xmlString							+= Constants.LINE_SEPARATOR;					
				}
			}
			
			 // Ensure that the result does not consist only of the XML declaration
			try {
				xmlStringFiltered						= XMLUtils.filterXMLDeclaration( xmlString );
			} catch ( XMLException xmlex ) {
				xmlStringFiltered						= null;
			} catch ( Exception ex ) {
				xmlStringFiltered						= null;
			}
			
			// Query did not result any data
			if ( xmlString == null || xmlStringFiltered.length() == 0 ) {
				throw new DAOException( ErrorConstants.ERROR_CODE_DAO_57, ErrorConstants.ERROR_MESSAGE_DAO_57 );
			}

			logger.log( Level.DEBUG, "Result of the XQuery:" + Constants.LINE_SEPARATOR + xmlString );
		} catch ( XMLDBException xmldbex ) {
			throw new DAOException( ErrorConstants.ERROR_CODE_DAO_58, ErrorConstants.ERROR_MESSAGE_DAO_58 + xmldbex.errorCode + ").", xmldbex );
		} catch ( FileException fex ) {
			throw new DAOException( ErrorConstants.ERROR_CODE_DAO_59, ErrorConstants.ERROR_MESSAGE_DAO_59_1 + xformsdbQueryElement.getAttributeValue( "id" ) + ErrorConstants.ERROR_MESSAGE_DAO_59_2, fex );
		} catch ( DAOException daoex ) {
			throw daoex;
		} catch ( Exception ex ) {
			throw new DAOException( ErrorConstants.ERROR_CODE_DAO_60, ErrorConstants.ERROR_MESSAGE_DAO_60_1 + xformsdbQueryElement.getAttributeValue( "id" ) + ErrorConstants.ERROR_MESSAGE_DAO_60_2, ex );
		} finally {
			if ( collection != null ) {
				try {
					collection.close();
				} catch ( XMLDBException xmldbex ) {
					logger.log( Level.ERROR, "Failed to close the resources (error code: " + xmldbex.errorCode + ").", xmldbex );
				}
			}
		}
		
		
		return xmlString;
	}

	
	/**
	 * Authenticate the user with a username and password against the realm.
	 * 
	 * 
	 * @param document			The XML document (<xformsdb:login> element).
	 * @param encoding			The character encoding to be used.
	 * @return xmlString		The information of the authenticated user in XML format.
	 * @throws DAOException		DAO exception.
	 */
	public String authenticateUser( Document document, String encoding ) throws DAOException {
		logger.log( Level.DEBUG, "Method has been called." );
		String collectionString							= null;
		String docString								= null;
		Collection collection							= null;
		Element xformsdbLoginElement					= null;
		Element xformsdbExpressionElement				= null;
		Elements xformsdbVarElements					= null;
		Element xformsdbVarElement						= null;
		String xqe										= null;
		String xmlString								= null;
		String xmlStringFiltered						= null;

		
		try {
			// Retrieve the needed elements
			xformsdbLoginElement						= document.getRootElement();
			xformsdbExpressionElement					= xformsdbLoginElement.getFirstChildElement( "expression", Constants.NAMESPACE_URI_XFORMSDB );
			xformsdbVarElements							= xformsdbLoginElement.getChildElements( "var", Constants.NAMESPACE_URI_XFORMSDB );
			xformsdbVarElement							= null;
			String xqueryStr							= null;

			// Fix collection and doc
			docString									= ( ( xformsdbLoginElement.getAttribute( "doc" ) == null ) ? null : xformsdbLoginElement.getAttributeValue( "doc" ) );
			collectionString							= this.daoDataSource.getCollection();
			String collectionAndDoc[]					= this.fixCollectionAndDoc( collectionString, docString );
			collectionString							= collectionAndDoc[ 0 ];
			docString									= collectionAndDoc[ 1 ];

			// Get the collection
			collection									= this.daoDataSource.getCollection( ExistDAOFactory.DRIVER, collectionString );
			
			// Get the XQuery service
			XQueryService service						= ( XQueryService ) collection.getService( "XQueryService", "1.0" );
			service.setProperty( OutputKeys.OMIT_XML_DECLARATION, Constants.OUTPUT_XML_OMIT_XML_DECLARATION_NO );
			service.setProperty( OutputKeys.METHOD, Constants.OUTPUT_XML_METHOD_XML );
			service.setProperty( OutputKeys.VERSION, Constants.OUTPUT_XML_VERSION_1_0 );
			service.setProperty( OutputKeys.ENCODING, encoding );
			service.setProperty( OutputKeys.INDENT, Constants.OUTPUT_XML_INDENT_YES );
			//service.setProperty( OutputKeys.STANDALONE, "no" );
			//service.setProperty( OutputKeys.MEDIA_TYPE, "text/xml" );

			
			// Retrieve the XQuery string
			// The queryStr variable is just for testing the existence of the query as well as for optimistic printing of the query using XFormsDB encoding as there is no way to read/parse the encoding of the query itself
			// Here, however, we know that the encoding of the query file is UTF-8
 			if ( xformsdbExpressionElement.getAttribute( "resource" ) != null ) {
				try {
					xqueryStr							= IOUtils.convert( Thread.currentThread().getContextClassLoader().getResourceAsStream( xformsdbExpressionElement.getAttributeValue( "resource" ) ), Constants.XML_DEFAULT_ENCODING, true );
				} catch ( Exception fex ) {
					throw new FileException( ErrorConstants.ERROR_CODE_DAO_43, ErrorConstants.ERROR_MESSAGE_DAO_43 );
				}
			}
			else {
				throw new DAOException( ErrorConstants.ERROR_CODE_DAO_44, ErrorConstants.ERROR_MESSAGE_DAO_44 );
			}

			// Check the XQuery expression string
			if ( xqueryStr == null ) {
				throw new DAOException( ErrorConstants.ERROR_CODE_DAO_45, ErrorConstants.ERROR_MESSAGE_DAO_45 );
			}
			
	
			xqe											= xqueryStr;
			// The XQuery expression to be executed
			logger.log( Level.DEBUG, "XQuery:" + Constants.LINE_SEPARATOR + xqe );
			
			// Compile the query expression
			CompiledExpression compiledExpression		= service.compile( xqe );


			// Set the external (normal) variables
			Document xformsdbVarElementContentsDocument	= null;
			ByteArrayOutputStream outputStream			= null;
			Serializer serializer						= null;
			String xformsdbVarElementValue				= null;
			for ( int i = 0; i < xformsdbVarElements.size(); i++ ) {
				xformsdbVarElement						= xformsdbVarElements.get( i );
				xformsdbVarElementValue					= "";
				// Check the contents of the <xformsdb:var> element
				if ( xformsdbVarElement.getChildElements().size() == 0 ) {
					xformsdbVarElementValue				= xformsdbVarElement.getValue();
				}
				else {
					// Retrieve the contents of the <xformsdb:var> element
					xformsdbVarElementContentsDocument	= new Document( new Element( xformsdbVarElement.getChildElements().get( 0 ) ) );				

					// Serialize the document
					outputStream						= new ByteArrayOutputStream();
					serializer							= new Serializer( outputStream, encoding );
					serializer.write( xformsdbVarElementContentsDocument );			
					
					// Change the encoding
					xformsdbVarElementValue				= outputStream.toString( encoding );
				}
				service.declareVariable( xformsdbVarElement.getAttributeValue( "name" ), xformsdbVarElementValue );
				logger.log( Level.DEBUG, "External (normal) variable: " + xformsdbVarElement.getAttributeValue( "name" ) + "=" + xformsdbVarElementValue );
			}
			
			// Execute the compiled XQuery
			ResourceSet resourceSet						= null;
			// Execute the compiled XQuery against collection
			if ( docString == null ) {
				resourceSet								= service.execute( compiledExpression );
			}
			// Execute the compiled XQuery against XML document
			else {
				resourceSet								= service.execute( ( XMLResource ) collection.getResource( docString ), compiledExpression );
			}

			// Iterate over the results
			ResourceIterator resourceIt					= resourceSet.getIterator();
			if ( resourceIt.hasMoreResources() == true ) {
				xmlString								= "";
			}
			while( resourceIt.hasMoreResources() == true ) {
				Resource resource						= resourceIt.nextResource();
				xmlString								+= ( String ) resource.getContent();
				
				if ( resourceIt.hasMoreResources() ) {
					xmlString							+= Constants.LINE_SEPARATOR;
				}
			}
			
			 // Ensure that the result does not consist only of the XML declaration
			try {
				xmlStringFiltered						= XMLUtils.filterXMLDeclaration( xmlString );
			} catch ( XMLException xmlex ) {
				xmlStringFiltered						= null;
			} catch ( Exception ex ) {
				xmlStringFiltered						= null;
			}
			
			// Incorrect username/password combination
			if ( xmlStringFiltered == null || xmlStringFiltered.length() == 0 ) {
				throw new AuthenticationException( ErrorConstants.ERROR_CODE_AUTHENTICATION_2, ErrorConstants.ERROR_MESSAGE_AUTHENTICATION_2 );
			}
			
			logger.log( Level.DEBUG, "Result of the XQuery:" + Constants.LINE_SEPARATOR + xmlString );
		} catch ( XMLDBException xmldbex ) {
			throw new DAOException( ErrorConstants.ERROR_CODE_DAO_48, ErrorConstants.ERROR_MESSAGE_DAO_48 + xmldbex.errorCode + ").", xmldbex );
		} catch ( FileException fex ) {
			throw new DAOException( ErrorConstants.ERROR_CODE_DAO_47, ErrorConstants.ERROR_MESSAGE_DAO_47_1 + xformsdbLoginElement.getAttributeValue( "id" ) + ErrorConstants.ERROR_MESSAGE_DAO_47_2, fex );
		} catch ( DAOException daoex ) {
			throw daoex;
		} catch ( AuthenticationException aex ) {
			throw new DAOException( aex.getCode(), aex.getMessage(), aex );
		} catch ( Exception ex ) {
			throw new DAOException( ErrorConstants.ERROR_CODE_DAO_46, ErrorConstants.ERROR_MESSAGE_DAO_46_1 + xformsdbLoginElement.getAttributeValue( "id" ) + ErrorConstants.ERROR_MESSAGE_DAO_46_2, ex );
		} finally {
			if ( collection != null ) {
				try {
					collection.close();
				} catch ( XMLDBException xmldbex ) {
					logger.log( Level.ERROR, "Failed to close the resources (error code: " + xmldbex.errorCode + ").", xmldbex );
				}
			}
		}
		
		
		return xmlString;
	}
	
	
	/**
	 * Select files from the files metadata data source.
	 * 
	 * 
	 * @param document			The XML document (<xformsdb:file> element).
	 * @param actionURL			The action URL, from which the request has been sent.
	 * @param encoding			The character encoding to be used.
	 * @return xmlString		The files list in XML format.
	 * @throws DAOException		DAO exception.
	 */
	public String selectFiles( Document document, String actionURL, String encoding ) throws DAOException {
		logger.log( Level.DEBUG, "Method has been called." );
		String collectionString							= null;
		String docString								= null;
		Collection collection							= null;
		Element xformsdbFileElement						= null;
		Element xformsdbExpressionElement				= null;
		Elements xformsdbVarElements					= null;
		Elements xformsdbSecvarElements					= null;
		Element xformsdbVarElement						= null;
		Element xformsdbSecvarElement					= null;
		boolean isIDsVariableSet						= false;
		boolean isUsernameVariableSet					= false;
		boolean isRolesVariableSet						= false;
		String xqe										= null;
		String xmlString								= null;
		String xmlStringFiltered						= null;

		
		try {
			// Retrieve the needed elements
			xformsdbFileElement							= document.getRootElement();
			xformsdbExpressionElement					= xformsdbFileElement.getFirstChildElement( "expression", Constants.NAMESPACE_URI_XFORMSDB );
			xformsdbVarElements							= xformsdbFileElement.getChildElements( "var", Constants.NAMESPACE_URI_XFORMSDB );
			xformsdbSecvarElements						= xformsdbFileElement.getChildElements( "secvar", Constants.NAMESPACE_URI_XFORMSDB );
			xformsdbVarElement							= null;
			xformsdbSecvarElement						= null;
			String xqueryStr							= null;

			// Fix collection and doc
			docString									= ( ( xformsdbFileElement.getAttribute( "doc" ) == null ) ? null : xformsdbFileElement.getAttributeValue( "doc" ) );
			collectionString							= this.daoDataSource.getCollection();
			String collectionAndDoc[]					= this.fixCollectionAndDoc( collectionString, docString );
			collectionString							= collectionAndDoc[ 0 ];
			docString									= collectionAndDoc[ 1 ];

			// Get the collection
			collection									= this.daoDataSource.getCollection( ExistDAOFactory.DRIVER, collectionString );
			
			// Get the XQuery service
			XQueryService service						= ( XQueryService ) collection.getService( "XQueryService", "1.0" );
			service.setProperty( OutputKeys.OMIT_XML_DECLARATION, Constants.OUTPUT_XML_OMIT_XML_DECLARATION_NO );
			service.setProperty( OutputKeys.METHOD, Constants.OUTPUT_XML_METHOD_XML );
			service.setProperty( OutputKeys.VERSION, Constants.OUTPUT_XML_VERSION_1_0 );
			service.setProperty( OutputKeys.ENCODING, encoding );
			service.setProperty( OutputKeys.INDENT, Constants.OUTPUT_XML_INDENT_YES );
			//service.setProperty( OutputKeys.STANDALONE, "no" );
			//service.setProperty( OutputKeys.MEDIA_TYPE, "text/xml" );

			
			// Retrieve the XQuery string
			// The queryStr variable is just for testing the existence of the query as well as for optimistic printing of the query using XFormsDB encoding as there is no way to read/parse the encoding of the query itself
			// Here, however, we know that the encoding of the query file is UTF-8
			if ( xformsdbExpressionElement.getAttribute( "resource" ) != null ) {
				try {
					xqueryStr							= IOUtils.convert( Thread.currentThread().getContextClassLoader().getResourceAsStream( xformsdbExpressionElement.getAttributeValue( "resource" ) ), Constants.XML_DEFAULT_ENCODING, true );
				} catch ( Exception fex ) {
					throw new FileException( ErrorConstants.ERROR_CODE_DAO_64, ErrorConstants.ERROR_MESSAGE_DAO_64 );
				}
			}
			else {
				throw new DAOException( ErrorConstants.ERROR_CODE_DAO_65, ErrorConstants.ERROR_MESSAGE_DAO_65 );
			}

			// Check the XQuery expression string
			if ( xqueryStr == null ) {
				throw new DAOException( ErrorConstants.ERROR_CODE_DAO_66, ErrorConstants.ERROR_MESSAGE_DAO_66 );
			}
			
	
			xqe											= xqueryStr;
			// The XQuery expression to be executed
			logger.log( Level.DEBUG, "XQuery:" + Constants.LINE_SEPARATOR + xqe );
			
			// Compile the query expression
			CompiledExpression compiledExpression		= service.compile( xqe );


			// Set the external (normal) variables
			Document xformsdbVarElementContentsDocument	= null;
			ByteArrayOutputStream outputStream			= null;
			Serializer serializer						= null;
			String xformsdbVarElementValue				= null;
			for ( int i = 0; i < xformsdbVarElements.size(); i++ ) {
				xformsdbVarElement						= xformsdbVarElements.get( i );
				xformsdbVarElementValue					= "";
				// Check the contents of the <xformsdb:var> element
				if ( xformsdbVarElement.getChildElements().size() == 0 ) {
					xformsdbVarElementValue				= xformsdbVarElement.getValue();
				}
				else {
					// Retrieve the contents of the <xformsdb:var> element
					xformsdbVarElementContentsDocument	= new Document( new Element( xformsdbVarElement.getChildElements().get( 0 ) ) );				

					// Serialize the document
					outputStream						= new ByteArrayOutputStream();
					serializer							= new Serializer( outputStream, encoding );
					serializer.write( xformsdbVarElementContentsDocument );			
					
					// Change the encoding
					xformsdbVarElementValue				= outputStream.toString( encoding );
				}
				
				service.declareVariable( xformsdbVarElement.getAttributeValue( "name" ), xformsdbVarElementValue );
				logger.log( Level.DEBUG, "External (normal) variable: " + xformsdbVarElement.getAttributeValue( "name" ) + "=" + xformsdbVarElementValue );
				// Check the name of the variable
				if ( "username".equals( xformsdbVarElement.getAttributeValue( "name" ) ) == true ) {
					isUsernameVariableSet				= true;
				}
				else if ( "roles".equals( xformsdbVarElement.getAttributeValue( "name" ) ) == true ) {
					isRolesVariableSet					= true;
				}
				else if ( "ids".equals( xformsdbVarElement.getAttributeValue( "name" ) ) == true ) {
					isIDsVariableSet					= true;
				}
			}
			// Set the external (security) variables
			for ( int i = 0; i < xformsdbSecvarElements.size(); i++ ) {
				xformsdbSecvarElement					= xformsdbSecvarElements.get( i );
				service.declareVariable( xformsdbSecvarElement.getAttributeValue( "name" ), xformsdbSecvarElement.getValue() );
				logger.log( Level.DEBUG, "External (security) variable: " + xformsdbSecvarElement.getAttributeValue( "name" ) + "=" + xformsdbSecvarElement.getValue() );
				// Check the name of the variable
				if ( "username".equals( xformsdbSecvarElement.getAttributeValue( "name" ) ) == true ) {
					isUsernameVariableSet				= true;
				}
				else if ( "roles".equals( xformsdbSecvarElement.getAttributeValue( "name" ) ) == true ) {
					isRolesVariableSet					= true;
				}
			}
			// Set the external (extra) variables
			service.declareVariable( "action", actionURL );
			logger.log( Level.DEBUG, "External (extra) variable: action=" + actionURL );
			
			if ( isUsernameVariableSet == false && isRolesVariableSet == false && isIDsVariableSet == false ) {
				service.declareVariable( "type", "all" );
				logger.log( Level.DEBUG, "External (extra) variable: type=all" );
			}
			else if ( isUsernameVariableSet == true && isRolesVariableSet == false && isIDsVariableSet == false ) {
				service.declareVariable( "type", "username" );
				logger.log( Level.DEBUG, "External (extra) variable: type=username" );
			}
			else if ( isUsernameVariableSet == false && isRolesVariableSet == true && isIDsVariableSet == false ) {
				service.declareVariable( "type", "roles" );
				logger.log( Level.DEBUG, "External (extra) variable: type=roles" );
			}
			else if ( isUsernameVariableSet == false && isRolesVariableSet == false && isIDsVariableSet == true ) {
				service.declareVariable( "type", "ids" );
				logger.log( Level.DEBUG, "External (extra) variable: type=ids" );
			}
			else {
				throw new DAOException( ErrorConstants.ERROR_CODE_DAO_71, ErrorConstants.ERROR_MESSAGE_DAO_71 );
			}
			
			
			// Execute the compiled XQuery
			ResourceSet resourceSet						= null;
			// Execute the compiled XQuery against collection
			if ( docString == null ) {
				resourceSet								= service.execute( compiledExpression );
			}
			// Execute the compiled XQuery against XML document
			else {
				resourceSet								= service.execute( ( XMLResource ) collection.getResource( docString ), compiledExpression );
			}

			// Iterate over the results
			ResourceIterator resourceIt					= resourceSet.getIterator();
			if ( resourceIt.hasMoreResources() == true ) {
				xmlString								= "";
			}
			while( resourceIt.hasMoreResources() == true ) {
				Resource resource						= resourceIt.nextResource();
				xmlString								+= ( String ) resource.getContent();
				
				if ( resourceIt.hasMoreResources() ) {
					xmlString							+= Constants.LINE_SEPARATOR;					
				}
			}
			
			 // Ensure that the result does not consist only of the XML declaration
			try {
				xmlStringFiltered						= XMLUtils.filterXMLDeclaration( xmlString );
			} catch ( XMLException xmlex ) {
				xmlStringFiltered						= null;
			} catch ( Exception ex ) {
				xmlStringFiltered						= null;
			}
			
			// Select files query did not result any data
			if ( xmlString == null || xmlStringFiltered.length() == 0 ) {
				throw new DAOException( ErrorConstants.ERROR_CODE_DAO_70, ErrorConstants.ERROR_MESSAGE_DAO_70 );
			}

			logger.log( Level.DEBUG, "Result of the XQuery:" + Constants.LINE_SEPARATOR + xmlString );
		} catch ( XMLDBException xmldbex ) {
			throw new DAOException( ErrorConstants.ERROR_CODE_DAO_67, ErrorConstants.ERROR_MESSAGE_DAO_67 + xmldbex.errorCode + ").", xmldbex );
		} catch ( FileException fex ) {
			throw new DAOException( ErrorConstants.ERROR_CODE_DAO_68, ErrorConstants.ERROR_MESSAGE_DAO_68_1 + xformsdbFileElement.getAttributeValue( "id" ) + ErrorConstants.ERROR_MESSAGE_DAO_68_2, fex );
		} catch ( DAOException daoex ) {
			throw daoex;
		} catch ( Exception ex ) {
			throw new DAOException( ErrorConstants.ERROR_CODE_DAO_69, ErrorConstants.ERROR_MESSAGE_DAO_69_1 + xformsdbFileElement.getAttributeValue( "id" ) + ErrorConstants.ERROR_MESSAGE_DAO_69_2, ex );
		} finally {
			if ( collection != null ) {
				try {
					collection.close();
				} catch ( XMLDBException xmldbex ) {
					logger.log( Level.ERROR, "Failed to close the resources (error code: " + xmldbex.errorCode + ").", xmldbex );
				}
			}
		}
		
		
		return xmlString;
	}
	
	
	/**
	 * Select file from the files metadata data source.
	 * 
	 * 
	 * @param document			The XML document (<xformsdb:file> element).
	 * @param encoding			The character encoding to be used.
	 * @return xmlString		The files list containing only one file in XML format.
	 * @throws DAOException		DAO exception.
	 */
	public String selectFile( Document document, String encoding ) throws DAOException {
		logger.log( Level.DEBUG, "Method has been called." );
		String collectionString							= null;
		String docString								= null;
		Collection collection							= null;
		Element xformsdbFileElement						= null;
		Element xformsdbExpressionElement				= null;
		Elements xformsdbVarElements					= null;
		Element xformsdbVarElement						= null;
		String xqe										= null;
		String xmlString								= null;
		String xmlStringFiltered						= null;

		
		try {
			// Retrieve the needed elements
			xformsdbFileElement							= document.getRootElement();
			xformsdbExpressionElement					= xformsdbFileElement.getFirstChildElement( "expression", Constants.NAMESPACE_URI_XFORMSDB );
			xformsdbVarElements							= xformsdbFileElement.getChildElements( "var", Constants.NAMESPACE_URI_XFORMSDB );
			xformsdbVarElement							= null;
			String xqueryStr							= null;

			// Fix collection and doc
			docString									= ( ( xformsdbFileElement.getAttribute( "doc" ) == null ) ? null : xformsdbFileElement.getAttributeValue( "doc" ) );
			collectionString							= this.daoDataSource.getCollection();
			String collectionAndDoc[]					= this.fixCollectionAndDoc( collectionString, docString );
			collectionString							= collectionAndDoc[ 0 ];
			docString									= collectionAndDoc[ 1 ];

			// Get the collection
			collection									= this.daoDataSource.getCollection( ExistDAOFactory.DRIVER, collectionString );
			
			// Get the XQuery service
			XQueryService service						= ( XQueryService ) collection.getService( "XQueryService", "1.0" );
			service.setProperty( OutputKeys.OMIT_XML_DECLARATION, Constants.OUTPUT_XML_OMIT_XML_DECLARATION_NO );
			service.setProperty( OutputKeys.METHOD, Constants.OUTPUT_XML_METHOD_XML );
			service.setProperty( OutputKeys.VERSION, Constants.OUTPUT_XML_VERSION_1_0 );
			service.setProperty( OutputKeys.ENCODING, encoding );
			service.setProperty( OutputKeys.INDENT, Constants.OUTPUT_XML_INDENT_YES );
			//service.setProperty( OutputKeys.STANDALONE, "no" );
			//service.setProperty( OutputKeys.MEDIA_TYPE, "text/xml" );

			
			// Retrieve the XQuery string
			// The queryStr variable is just for testing the existence of the query as well as for optimistic printing of the query using XFormsDB encoding as there is no way to read/parse the encoding of the query itself
			// Here, however, we know that the encoding of the query file is UTF-8
			if ( xformsdbExpressionElement.getAttribute( "resource" ) != null ) {
				try {
					xqueryStr							= IOUtils.convert( Thread.currentThread().getContextClassLoader().getResourceAsStream( xformsdbExpressionElement.getAttributeValue( "resource" ) ), Constants.XML_DEFAULT_ENCODING, true );
				} catch ( Exception fex ) {
					throw new FileException( ErrorConstants.ERROR_CODE_DAO_72, ErrorConstants.ERROR_MESSAGE_DAO_72 );
				}
			}
			else {
				throw new DAOException( ErrorConstants.ERROR_CODE_DAO_73, ErrorConstants.ERROR_MESSAGE_DAO_73 );
			}

			// Check the XQuery expression string
			if ( xqueryStr == null ) {
				throw new DAOException( ErrorConstants.ERROR_CODE_DAO_74, ErrorConstants.ERROR_MESSAGE_DAO_74 );
			}
			
	
			xqe											= xqueryStr;
			// The XQuery expression to be executed
			logger.log( Level.DEBUG, "XQuery:" + Constants.LINE_SEPARATOR + xqe );
			
			// Compile the query expression
			CompiledExpression compiledExpression		= service.compile( xqe );


			// Set the external (normal) variables
			Document xformsdbVarElementContentsDocument	= null;
			ByteArrayOutputStream outputStream			= null;
			Serializer serializer						= null;
			String xformsdbVarElementValue				= null;
			for ( int i = 0; i < xformsdbVarElements.size(); i++ ) {
				xformsdbVarElement						= xformsdbVarElements.get( i );
				xformsdbVarElementValue					= "";
				// Check the contents of the <xformsdb:var> element
				if ( xformsdbVarElement.getChildElements().size() == 0 ) {
					xformsdbVarElementValue				= xformsdbVarElement.getValue();
				}
				else {
					// Retrieve the contents of the <xformsdb:var> element
					xformsdbVarElementContentsDocument	= new Document( new Element( xformsdbVarElement.getChildElements().get( 0 ) ) );				

					// Serialize the document
					outputStream						= new ByteArrayOutputStream();
					serializer							= new Serializer( outputStream, encoding );
					serializer.write( xformsdbVarElementContentsDocument );			
					
					// Change the encoding
					xformsdbVarElementValue				= outputStream.toString( encoding );
				}
				
				service.declareVariable( xformsdbVarElement.getAttributeValue( "name" ), xformsdbVarElementValue );
				logger.log( Level.DEBUG, "External (normal) variable: " + xformsdbVarElement.getAttributeValue( "name" ) + "=" + xformsdbVarElementValue );
			}
						
			
			// Execute the compiled XQuery
			ResourceSet resourceSet						= null;
			// Execute the compiled XQuery against collection
			if ( docString == null ) {
				resourceSet								= service.execute( compiledExpression );
			}
			// Execute the compiled XQuery against XML document
			else {
				resourceSet								= service.execute( ( XMLResource ) collection.getResource( docString ), compiledExpression );
			}

			// Iterate over the results
			ResourceIterator resourceIt					= resourceSet.getIterator();
			if ( resourceIt.hasMoreResources() == true ) {
				xmlString								= "";
			}
			while( resourceIt.hasMoreResources() == true ) {
				Resource resource						= resourceIt.nextResource();
				xmlString								+= ( String ) resource.getContent();
				
				if ( resourceIt.hasMoreResources() ) {
					xmlString							+= Constants.LINE_SEPARATOR;					
				}
			}
			
			 // Ensure that the result does not consist only of the XML declaration
			try {
				xmlStringFiltered						= XMLUtils.filterXMLDeclaration( xmlString );
			} catch ( XMLException xmlex ) {
				xmlStringFiltered						= null;
			} catch ( Exception ex ) {
				xmlStringFiltered						= null;
			}
			
			// Select file query did not result any data
			if ( xmlString == null || xmlStringFiltered.length() == 0 ) {
				try {
					// Wait for 2 seconds before returning the response in order to slow down malicious software for polling various download IDs
					Thread.sleep( 2000 );
				} catch ( InterruptedException iex ) {
				}
				throw new DAOException( ErrorConstants.ERROR_CODE_DAO_75, ErrorConstants.ERROR_MESSAGE_DAO_75 );
			}

			logger.log( Level.DEBUG, "Result of the XQuery:" + Constants.LINE_SEPARATOR + xmlString );
		} catch ( XMLDBException xmldbex ) {
			throw new DAOException( ErrorConstants.ERROR_CODE_DAO_76, ErrorConstants.ERROR_MESSAGE_DAO_76 + xmldbex.errorCode + ").", xmldbex );
		} catch ( FileException fex ) {
			throw new DAOException( ErrorConstants.ERROR_CODE_DAO_77, ErrorConstants.ERROR_MESSAGE_DAO_77_1 + xformsdbFileElement.getAttributeValue( "id" ) + ErrorConstants.ERROR_MESSAGE_DAO_77_2, fex );
		} catch ( DAOException daoex ) {
			throw daoex;
		} catch ( Exception ex ) {
			throw new DAOException( ErrorConstants.ERROR_CODE_DAO_78, ErrorConstants.ERROR_MESSAGE_DAO_78_1 + xformsdbFileElement.getAttributeValue( "id" ) + ErrorConstants.ERROR_MESSAGE_DAO_78_2, ex );
		} finally {
			if ( collection != null ) {
				try {
					collection.close();
				} catch ( XMLDBException xmldbex ) {
					logger.log( Level.ERROR, "Failed to close the resources (error code: " + xmldbex.errorCode + ").", xmldbex );
				}
			}
		}
		
		
		return xmlString;
	}
	
	
	/**
	 * Insert files to the files metadata data source.
	 * 
	 * 
	 * @param document			The submitted XML document (<xformsdb:file> element (insert)).
	 * @param actionURL			The action URL, from which the request has been sent.
	 * @param encoding			The character encoding to be used.
	 * @return xmlString		The inserted files list in XML format.
	 * @throws DAOException		DAO exception.
	 */
	public String insertFiles( Document document, String actionURL, String encoding ) throws DAOException {
		logger.log( Level.DEBUG, "Method has been called." );
		String collectionString							= null;
		String docString								= null;
		Collection collection							= null;
		Element xformsdbFileElement						= null;
		Element xformsdbExpressionElement				= null;
		Elements xformsdbVarElements					= null;
		Element xformsdbVarElement						= null;
		String xqe										= null;
		String xmlString								= null;
		String xmlStringFiltered						= null;

		
		try {
			// Retrieve the needed elements
			xformsdbFileElement							= document.getRootElement();
			xformsdbExpressionElement					= xformsdbFileElement.getFirstChildElement( "expression", Constants.NAMESPACE_URI_XFORMSDB );
			xformsdbVarElements							= xformsdbFileElement.getChildElements( "var", Constants.NAMESPACE_URI_XFORMSDB );
			xformsdbVarElement							= null;
			String xqueryStr							= null;

			// Fix collection and doc
			docString									= ( ( xformsdbFileElement.getAttribute( "doc" ) == null ) ? null : xformsdbFileElement.getAttributeValue( "doc" ) );
			collectionString							= this.daoDataSource.getCollection();
			String collectionAndDoc[]					= this.fixCollectionAndDoc( collectionString, docString );
			collectionString							= collectionAndDoc[ 0 ];
			docString									= collectionAndDoc[ 1 ];

			// Get the collection
			collection									= this.daoDataSource.getCollection( ExistDAOFactory.DRIVER, collectionString );
			
			// Get the XQuery service
			XQueryService service						= ( XQueryService ) collection.getService( "XQueryService", "1.0" );
			service.setProperty( OutputKeys.OMIT_XML_DECLARATION, Constants.OUTPUT_XML_OMIT_XML_DECLARATION_NO );
			service.setProperty( OutputKeys.METHOD, Constants.OUTPUT_XML_METHOD_XML );
			service.setProperty( OutputKeys.VERSION, Constants.OUTPUT_XML_VERSION_1_0 );
			service.setProperty( OutputKeys.ENCODING, encoding );
			service.setProperty( OutputKeys.INDENT, Constants.OUTPUT_XML_INDENT_YES );
			//service.setProperty( OutputKeys.STANDALONE, "no" );
			//service.setProperty( OutputKeys.MEDIA_TYPE, "text/xml" );

			
			// Retrieve the XQuery string
			// The queryStr variable is just for testing the existence of the query as well as for optimistic printing of the query using XFormsDB encoding as there is no way to read/parse the encoding of the query itself
			// Here, however, we know that the encoding of the query file is UTF-8
			if ( xformsdbExpressionElement.getAttribute( "resource" ) != null ) {
				try {
					xqueryStr							= IOUtils.convert( Thread.currentThread().getContextClassLoader().getResourceAsStream( xformsdbExpressionElement.getAttributeValue( "resource" ) ), Constants.XML_DEFAULT_ENCODING, true );
				} catch ( Exception fex ) {
					throw new FileException( ErrorConstants.ERROR_CODE_DAO_94, ErrorConstants.ERROR_MESSAGE_DAO_94 );
				}
			}
			else {
				throw new DAOException( ErrorConstants.ERROR_CODE_DAO_95, ErrorConstants.ERROR_MESSAGE_DAO_95 );
			}

			// Check the XQuery expression string
			if ( xqueryStr == null ) {
				throw new DAOException( ErrorConstants.ERROR_CODE_DAO_96, ErrorConstants.ERROR_MESSAGE_DAO_96 );
			}
			
	
			xqe											= xqueryStr;
			// The XQuery expression to be executed
			logger.log( Level.DEBUG, "XQuery:" + Constants.LINE_SEPARATOR + xqe );
			
			// Compile the query expression
			CompiledExpression compiledExpression		= service.compile( xqe );


			// Set the external (normal) variables
			Document xformsdbVarElementContentsDocument	= null;
			ByteArrayOutputStream outputStream			= null;
			Serializer serializer						= null;
			String xformsdbVarElementValue				= null;
			for ( int i = 0; i < xformsdbVarElements.size(); i++ ) {
				xformsdbVarElement						= xformsdbVarElements.get( i );
				xformsdbVarElementValue					= "";
				// Check the contents of the <xformsdb:var> element
				if ( xformsdbVarElement.getChildElements().size() == 0 ) {
					xformsdbVarElementValue				= xformsdbVarElement.getValue();
				}
				else {
					// Retrieve the contents of the <xformsdb:var> element
					xformsdbVarElementContentsDocument	= new Document( new Element( xformsdbVarElement.getChildElements().get( 0 ) ) );				

					// Serialize the document
					outputStream						= new ByteArrayOutputStream();
					serializer							= new Serializer( outputStream, encoding );
					serializer.write( xformsdbVarElementContentsDocument );			
					
					// Change the encoding
					xformsdbVarElementValue				= outputStream.toString( encoding );
				}
				
				service.declareVariable( xformsdbVarElement.getAttributeValue( "name" ), xformsdbVarElementValue );
				logger.log( Level.DEBUG, "External (normal) variable: " + xformsdbVarElement.getAttributeValue( "name" ) + "=" + xformsdbVarElementValue );
			}
			// Set the external (extra) variables
			service.declareVariable( "action", actionURL );
			logger.log( Level.DEBUG, "External (extra) variable: action=" + actionURL );
			
			
			// Execute the compiled XQuery
			ResourceSet resourceSet						= null;
			// Execute the compiled XQuery against collection
			if ( docString == null ) {
				resourceSet								= service.execute( compiledExpression );
			}
			// Execute the compiled XQuery against XML document
			else {
				resourceSet								= service.execute( ( XMLResource ) collection.getResource( docString ), compiledExpression );
			}

			// Iterate over the results
			ResourceIterator resourceIt					= resourceSet.getIterator();
			if ( resourceIt.hasMoreResources() == true ) {
				xmlString								= "";
			}
			while( resourceIt.hasMoreResources() == true ) {
				Resource resource						= resourceIt.nextResource();
				xmlString								+= ( String ) resource.getContent();
				
				if ( resourceIt.hasMoreResources() ) {
					xmlString							+= Constants.LINE_SEPARATOR;					
				}
			}
			
			 // Ensure that the result does not consist only of the XML declaration
			try {
				xmlStringFiltered						= XMLUtils.filterXMLDeclaration( xmlString );
			} catch ( XMLException xmlex ) {
				xmlStringFiltered						= null;
			} catch ( Exception ex ) {
				xmlStringFiltered						= null;
			}
			
			// Select files query did not result any data
			if ( xmlString == null || xmlStringFiltered.length() == 0 ) {
				throw new DAOException( ErrorConstants.ERROR_CODE_DAO_97, ErrorConstants.ERROR_MESSAGE_DAO_97 );
			}

			logger.log( Level.DEBUG, "Result of the XQuery:" + Constants.LINE_SEPARATOR + xmlString );
		} catch ( XMLDBException xmldbex ) {
			throw new DAOException( ErrorConstants.ERROR_CODE_DAO_98, ErrorConstants.ERROR_MESSAGE_DAO_98 + xmldbex.errorCode + ").", xmldbex );
		} catch ( FileException fex ) {
			throw new DAOException( ErrorConstants.ERROR_CODE_DAO_99, ErrorConstants.ERROR_MESSAGE_DAO_99_1 + xformsdbFileElement.getAttributeValue( "id" ) + ErrorConstants.ERROR_MESSAGE_DAO_99_2, fex );
		} catch ( DAOException daoex ) {
			throw daoex;
		} catch ( Exception ex ) {
			throw new DAOException( ErrorConstants.ERROR_CODE_DAO_100, ErrorConstants.ERROR_MESSAGE_DAO_100_1 + xformsdbFileElement.getAttributeValue( "id" ) + ErrorConstants.ERROR_MESSAGE_DAO_100_2, ex );
		} finally {
			if ( collection != null ) {
				try {
					collection.close();
				} catch ( XMLDBException xmldbex ) {
					logger.log( Level.ERROR, "Failed to close the resources (error code: " + xmldbex.errorCode + ").", xmldbex );
				}
			}
		}
		
		
		return xmlString;
	}
	
	
	/**
	 * Delete files from the files metadata data source.
	 * 
	 * 
	 * @param document			The submitted XML document (<xformsdb:file> element (delete)).
	 * @param encoding			The character encoding to be used.
	 * @return xmlString		The deleted files list in XML format.
	 * @throws DAOException		DAO exception.
	 */
	public String deleteFiles( Document document, String encoding ) throws DAOException {
		logger.log( Level.DEBUG, "Method has been called." );
		String collectionString							= null;
		String docString								= null;
		Collection collection							= null;
		Element xformsdbFileElement						= null;
		Element xformsdbExpressionElement				= null;
		Elements xformsdbVarElements					= null;
		Element xformsdbVarElement						= null;
		String xqe										= null;
		String xmlString								= null;
		String xmlStringFiltered						= null;

		
		try {
			// Retrieve the needed elements
			xformsdbFileElement							= document.getRootElement();
			xformsdbExpressionElement					= xformsdbFileElement.getFirstChildElement( "expression", Constants.NAMESPACE_URI_XFORMSDB );
			xformsdbVarElements							= xformsdbFileElement.getChildElements( "var", Constants.NAMESPACE_URI_XFORMSDB );
			xformsdbVarElement							= null;
			String xqueryStr							= null;

			// Fix collection and doc
			docString									= ( ( xformsdbFileElement.getAttribute( "doc" ) == null ) ? null : xformsdbFileElement.getAttributeValue( "doc" ) );
			collectionString							= this.daoDataSource.getCollection();
			String collectionAndDoc[]					= this.fixCollectionAndDoc( collectionString, docString );
			collectionString							= collectionAndDoc[ 0 ];
			docString									= collectionAndDoc[ 1 ];

			// Get the collection
			collection									= this.daoDataSource.getCollection( ExistDAOFactory.DRIVER, collectionString );
			
			// Get the XQuery service
			XQueryService service						= ( XQueryService ) collection.getService( "XQueryService", "1.0" );
			service.setProperty( OutputKeys.OMIT_XML_DECLARATION, Constants.OUTPUT_XML_OMIT_XML_DECLARATION_NO );
			service.setProperty( OutputKeys.METHOD, Constants.OUTPUT_XML_METHOD_XML );
			service.setProperty( OutputKeys.VERSION, Constants.OUTPUT_XML_VERSION_1_0 );
			service.setProperty( OutputKeys.ENCODING, encoding );
			service.setProperty( OutputKeys.INDENT, Constants.OUTPUT_XML_INDENT_YES );
			//service.setProperty( OutputKeys.STANDALONE, "no" );
			//service.setProperty( OutputKeys.MEDIA_TYPE, "text/xml" );

			
			// Retrieve the XQuery string
			// The queryStr variable is just for testing the existence of the query as well as for optimistic printing of the query using XFormsDB encoding as there is no way to read/parse the encoding of the query itself
			// Here, however, we know that the encoding of the query file is UTF-8
			if ( xformsdbExpressionElement.getAttribute( "resource" ) != null ) {
				try {
					xqueryStr							= IOUtils.convert( Thread.currentThread().getContextClassLoader().getResourceAsStream( xformsdbExpressionElement.getAttributeValue( "resource" ) ), Constants.XML_DEFAULT_ENCODING, true );
				} catch ( Exception fex ) {
					throw new FileException( ErrorConstants.ERROR_CODE_DAO_103, ErrorConstants.ERROR_MESSAGE_DAO_103 );
				}
			}
			else {
				throw new DAOException( ErrorConstants.ERROR_CODE_DAO_104, ErrorConstants.ERROR_MESSAGE_DAO_104 );
			}

			// Check the XQuery expression string
			if ( xqueryStr == null ) {
				throw new DAOException( ErrorConstants.ERROR_CODE_DAO_105, ErrorConstants.ERROR_MESSAGE_DAO_105 );
			}
			
	
			xqe											= xqueryStr;
			// The XQuery expression to be executed
			logger.log( Level.DEBUG, "XQuery:" + Constants.LINE_SEPARATOR + xqe );
			
			// Compile the query expression
			CompiledExpression compiledExpression		= service.compile( xqe );


			// Set the external (normal) variables
			Document xformsdbVarElementContentsDocument	= null;
			ByteArrayOutputStream outputStream			= null;
			Serializer serializer						= null;
			String xformsdbVarElementValue				= null;
			for ( int i = 0; i < xformsdbVarElements.size(); i++ ) {
				xformsdbVarElement						= xformsdbVarElements.get( i );
				xformsdbVarElementValue					= "";
				// Check the contents of the <xformsdb:var> element
				if ( xformsdbVarElement.getChildElements().size() == 0 ) {
					xformsdbVarElementValue				= xformsdbVarElement.getValue();
				}
				else {
					// Retrieve the contents of the <xformsdb:var> element
					xformsdbVarElementContentsDocument	= new Document( new Element( xformsdbVarElement.getChildElements().get( 0 ) ) );				

					// Serialize the document
					outputStream						= new ByteArrayOutputStream();
					serializer							= new Serializer( outputStream, encoding );
					serializer.write( xformsdbVarElementContentsDocument );			
					
					// Change the encoding
					xformsdbVarElementValue				= outputStream.toString( encoding );
				}
				
				service.declareVariable( xformsdbVarElement.getAttributeValue( "name" ), xformsdbVarElementValue );
				logger.log( Level.DEBUG, "External (normal) variable: " + xformsdbVarElement.getAttributeValue( "name" ) + "=" + xformsdbVarElementValue );
			}
			
			
			// Execute the compiled XQuery
			ResourceSet resourceSet						= null;
			// Execute the compiled XQuery against collection
			if ( docString == null ) {
				resourceSet								= service.execute( compiledExpression );
			}
			// Execute the compiled XQuery against XML document
			else {
				resourceSet								= service.execute( ( XMLResource ) collection.getResource( docString ), compiledExpression );
			}

			// Iterate over the results
			ResourceIterator resourceIt					= resourceSet.getIterator();
			if ( resourceIt.hasMoreResources() == true ) {
				xmlString								= "";
			}
			while( resourceIt.hasMoreResources() == true ) {
				Resource resource						= resourceIt.nextResource();
				xmlString								+= ( String ) resource.getContent();
				
				if ( resourceIt.hasMoreResources() ) {
					xmlString							+= Constants.LINE_SEPARATOR;					
				}
			}
			
			 // Ensure that the result does not consist only of the XML declaration
			try {
				xmlStringFiltered						= XMLUtils.filterXMLDeclaration( xmlString );
			} catch ( XMLException xmlex ) {
				xmlStringFiltered						= null;
			} catch ( Exception ex ) {
				xmlStringFiltered						= null;
			}
			
			// Select files query did not result any data
			if ( xmlString == null || xmlStringFiltered.length() == 0 ) {
				throw new DAOException( ErrorConstants.ERROR_CODE_DAO_106, ErrorConstants.ERROR_MESSAGE_DAO_106 );
			}

			logger.log( Level.DEBUG, "Result of the XQuery:" + Constants.LINE_SEPARATOR + xmlString );
		} catch ( XMLDBException xmldbex ) {
			throw new DAOException( ErrorConstants.ERROR_CODE_DAO_107, ErrorConstants.ERROR_MESSAGE_DAO_107 + xmldbex.errorCode + ").", xmldbex );
		} catch ( FileException fex ) {
			throw new DAOException( ErrorConstants.ERROR_CODE_DAO_108, ErrorConstants.ERROR_MESSAGE_DAO_108_1 + xformsdbFileElement.getAttributeValue( "id" ) + ErrorConstants.ERROR_MESSAGE_DAO_108_2, fex );
		} catch ( DAOException daoex ) {
			throw daoex;
		} catch ( Exception ex ) {
			throw new DAOException( ErrorConstants.ERROR_CODE_DAO_109, ErrorConstants.ERROR_MESSAGE_DAO_109_1 + xformsdbFileElement.getAttributeValue( "id" ) + ErrorConstants.ERROR_MESSAGE_DAO_109_2, ex );
		} finally {
			if ( collection != null ) {
				try {
					collection.close();
				} catch ( XMLDBException xmldbex ) {
					logger.log( Level.ERROR, "Failed to close the resources (error code: " + xmldbex.errorCode + ").", xmldbex );
				}
			}
		}
		
		
		return xmlString;
	}
	
	
	/**
	 * Update files to the files metadata data source.
	 * 
	 * 
	 * @param document			The submitted XML document (<xformsdb:file> element (update)).
	 * @param encoding			The character encoding to be used.
	 * @return xmlString		The updated files list in XML format.
	 * @throws DAOException		DAO exception.
	 */
	public String updateFiles( Document document, String encoding ) throws DAOException {
		logger.log( Level.DEBUG, "Method has been called." );
		String collectionString							= null;
		String docString								= null;
		Collection collection							= null;
		Element xformsdbFileElement						= null;
		Element xformsdbExpressionElement				= null;
		Elements xformsdbVarElements					= null;
		Element xformsdbVarElement						= null;
		String xqe										= null;
		String xmlString								= null;
		String xmlStringFiltered						= null;

		
		try {
			// Retrieve the needed elements
			xformsdbFileElement							= document.getRootElement();
			xformsdbExpressionElement					= xformsdbFileElement.getFirstChildElement( "expression", Constants.NAMESPACE_URI_XFORMSDB );
			xformsdbVarElements							= xformsdbFileElement.getChildElements( "var", Constants.NAMESPACE_URI_XFORMSDB );
			xformsdbVarElement							= null;
			String xqueryStr							= null;

			// Fix collection and doc
			docString									= ( ( xformsdbFileElement.getAttribute( "doc" ) == null ) ? null : xformsdbFileElement.getAttributeValue( "doc" ) );
			collectionString							= this.daoDataSource.getCollection();
			String collectionAndDoc[]					= this.fixCollectionAndDoc( collectionString, docString );
			collectionString							= collectionAndDoc[ 0 ];
			docString									= collectionAndDoc[ 1 ];

			// Get the collection
			collection									= this.daoDataSource.getCollection( ExistDAOFactory.DRIVER, collectionString );
			
			// Get the XQuery service
			XQueryService service						= ( XQueryService ) collection.getService( "XQueryService", "1.0" );
			service.setProperty( OutputKeys.OMIT_XML_DECLARATION, Constants.OUTPUT_XML_OMIT_XML_DECLARATION_NO );
			service.setProperty( OutputKeys.METHOD, Constants.OUTPUT_XML_METHOD_XML );
			service.setProperty( OutputKeys.VERSION, Constants.OUTPUT_XML_VERSION_1_0 );
			service.setProperty( OutputKeys.ENCODING, encoding );
			service.setProperty( OutputKeys.INDENT, Constants.OUTPUT_XML_INDENT_YES );
			//service.setProperty( OutputKeys.STANDALONE, "no" );
			//service.setProperty( OutputKeys.MEDIA_TYPE, "text/xml" );

			
			// Retrieve the XQuery string
			// The queryStr variable is just for testing the existence of the query as well as for optimistic printing of the query using XFormsDB encoding as there is no way to read/parse the encoding of the query itself
			// Here, however, we know that the encoding of the query file is UTF-8
			if ( xformsdbExpressionElement.getAttribute( "resource" ) != null ) {
				try {
					xqueryStr							= IOUtils.convert( Thread.currentThread().getContextClassLoader().getResourceAsStream( xformsdbExpressionElement.getAttributeValue( "resource" ) ), Constants.XML_DEFAULT_ENCODING, true );
				} catch ( Exception fex ) {
					throw new FileException( ErrorConstants.ERROR_CODE_DAO_112, ErrorConstants.ERROR_MESSAGE_DAO_112 );
				}
			}
			else {
				throw new DAOException( ErrorConstants.ERROR_CODE_DAO_113, ErrorConstants.ERROR_MESSAGE_DAO_113 );
			}

			// Check the XQuery expression string
			if ( xqueryStr == null ) {
				throw new DAOException( ErrorConstants.ERROR_CODE_DAO_114, ErrorConstants.ERROR_MESSAGE_DAO_114 );
			}
			
	
			xqe											= xqueryStr;
			// The XQuery expression to be executed
			logger.log( Level.DEBUG, "XQuery:" + Constants.LINE_SEPARATOR + xqe );
			
			// Compile the query expression
			CompiledExpression compiledExpression		= service.compile( xqe );


			// Set the external (normal) variables
			Document xformsdbVarElementContentsDocument	= null;
			ByteArrayOutputStream outputStream			= null;
			Serializer serializer						= null;
			String xformsdbVarElementValue				= null;
			for ( int i = 0; i < xformsdbVarElements.size(); i++ ) {
				xformsdbVarElement						= xformsdbVarElements.get( i );
				xformsdbVarElementValue					= "";
				// Check the contents of the <xformsdb:var> element
				if ( xformsdbVarElement.getChildElements().size() == 0 ) {
					xformsdbVarElementValue				= xformsdbVarElement.getValue();
				}
				else {
					// Retrieve the contents of the <xformsdb:var> element
					xformsdbVarElementContentsDocument	= new Document( new Element( xformsdbVarElement.getChildElements().get( 0 ) ) );				

					// Serialize the document
					outputStream						= new ByteArrayOutputStream();
					serializer							= new Serializer( outputStream, encoding );
					serializer.write( xformsdbVarElementContentsDocument );			
					
					// Change the encoding
					xformsdbVarElementValue				= outputStream.toString( encoding );
				}
				
				service.declareVariable( xformsdbVarElement.getAttributeValue( "name" ), xformsdbVarElementValue );
				logger.log( Level.DEBUG, "External (normal) variable: " + xformsdbVarElement.getAttributeValue( "name" ) + "=" + xformsdbVarElementValue );
			}
			
			
			// Execute the compiled XQuery
			ResourceSet resourceSet						= null;
			// Execute the compiled XQuery against collection
			if ( docString == null ) {
				resourceSet								= service.execute( compiledExpression );
			}
			// Execute the compiled XQuery against XML document
			else {
				resourceSet								= service.execute( ( XMLResource ) collection.getResource( docString ), compiledExpression );
			}

			// Iterate over the results
			ResourceIterator resourceIt					= resourceSet.getIterator();
			if ( resourceIt.hasMoreResources() == true ) {
				xmlString								= "";
			}
			while( resourceIt.hasMoreResources() == true ) {
				Resource resource						= resourceIt.nextResource();
				xmlString								+= ( String ) resource.getContent();
				
				if ( resourceIt.hasMoreResources() ) {
					xmlString							+= Constants.LINE_SEPARATOR;					
				}
			}
			
			 // Ensure that the result does not consist only of the XML declaration
			try {
				xmlStringFiltered						= XMLUtils.filterXMLDeclaration( xmlString );
			} catch ( XMLException xmlex ) {
				xmlStringFiltered						= null;
			} catch ( Exception ex ) {
				xmlStringFiltered						= null;
			}
			
			// Select files query did not result any data
			if ( xmlString == null || xmlStringFiltered.length() == 0 ) {
				throw new DAOException( ErrorConstants.ERROR_CODE_DAO_115, ErrorConstants.ERROR_MESSAGE_DAO_115 );
			}

			logger.log( Level.DEBUG, "Result of the XQuery:" + Constants.LINE_SEPARATOR + xmlString );
		} catch ( XMLDBException xmldbex ) {
			throw new DAOException( ErrorConstants.ERROR_CODE_DAO_116, ErrorConstants.ERROR_MESSAGE_DAO_116 + xmldbex.errorCode + ").", xmldbex );
		} catch ( FileException fex ) {
			throw new DAOException( ErrorConstants.ERROR_CODE_DAO_117, ErrorConstants.ERROR_MESSAGE_DAO_117_1 + xformsdbFileElement.getAttributeValue( "id" ) + ErrorConstants.ERROR_MESSAGE_DAO_117_2, fex );
		} catch ( DAOException daoex ) {
			throw daoex;
		} catch ( Exception ex ) {
			throw new DAOException( ErrorConstants.ERROR_CODE_DAO_118, ErrorConstants.ERROR_MESSAGE_DAO_118_1 + xformsdbFileElement.getAttributeValue( "id" ) + ErrorConstants.ERROR_MESSAGE_DAO_118_2, ex );
		} finally {
			if ( collection != null ) {
				try {
					collection.close();
				} catch ( XMLDBException xmldbex ) {
					logger.log( Level.ERROR, "Failed to close the resources (error code: " + xmldbex.errorCode + ").", xmldbex );
				}
			}
		}
		
		
		return xmlString;
	}
	
	
	/**
	 * Authenticate the widget user with an idkey and at against the widget realm.
	 * 
	 * 
	 * @param idkey				The idkey for authenticating the widget user.
	 * @param at				The at for authenticating the widget user.
	 * @param uriString			The URI string for connecting to the widget realm.
	 * @param encoding			The character encoding to be used.
	 * @return xmlString		The information of the authenticated widget user in XML format.
	 * @throws DAOException		DAO exception.
	 */
	public String authenticateWidgetUser( String idkey, String at, String uriString, String encoding ) throws DAOException {
		logger.log( Level.DEBUG, "Method has been called." );
		HttpClient httpClient			= null;
		HttpGet httpGet					= null;
		HttpResponse httpResponse		= null;
		HttpEntity httpEntity			= null;
		StatusLine statusLine			= null;
		String xmlString				= null;
		String xmlStringFiltered		= null;

		
		try {
			// Update the URI string i.e. set the idkey and at parameter values
			uriString					= uriString.replace( "{idkey}", idkey );
			uriString					= uriString.replace( "{at}", at );

			// Create an instance of HttpClient
			httpClient					= new DefaultHttpClient();
		    
		    // Create an instance of HttpGet
		    httpGet						= new HttpGet( StringUtils.toSafeASCIIString( uriString ) ); // Important for encoding the URI correctly!
		    
		    // Execute the HTTP request
    		logger.log( Level.DEBUG, "Executing HTTP request: " + httpGet.getURI() );
	        httpResponse				= httpClient.execute( httpGet );

	        // Retrieve the HTTP response entity
    		httpEntity					= httpResponse.getEntity();

    		if ( httpEntity != null ) {
    			// Retrieve the HTTP response status line
    			statusLine				= httpResponse.getStatusLine();

    			if ( statusLine.getStatusCode() == HttpServletResponse.SC_OK ) {
    				// Retrieve the body of the response (content)
    				if ( EntityUtils.getContentCharSet( httpEntity ) != null ) {
        				// Decode using the response character encoding and transform to the given encoding format
    					// Work around for responses with non-matching encoding in the XML declaration and in the content charset
        				xmlString		= XMLUtils.getInputStreamAsString( httpEntity.getContent(), EntityUtils.getContentCharSet( httpEntity ), encoding );
    				}
    				else {
        				// Decode using the encoding in the XML declaration and transform to the given encoding format
        				xmlString		= XMLUtils.getInputStreamAsString( httpEntity.getContent(), encoding );    					
    				}
    			}
    		}
    		
	        // Ensure that the result does not consist only of the XML declaration
			try {
				xmlStringFiltered		= XMLUtils.filterXMLDeclaration( xmlString );
			} catch ( XMLException xmlex ) {
				xmlStringFiltered		= null;
			} catch ( Exception ex ) {
				xmlStringFiltered		= null;
			}

			// Incorrect idkey/at combination
			if ( xmlString == null || xmlStringFiltered.length() == 0 ) {
				throw new AuthenticationException( ErrorConstants.ERROR_CODE_AUTHENTICATION_4, ErrorConstants.ERROR_MESSAGE_AUTHENTICATION_4 );
			}

			logger.log( Level.DEBUG, "Result of the XQuery:" + Constants.LINE_SEPARATOR + xmlString );
		} catch ( ClientProtocolException cpex ) {
			throw new DAOException( ErrorConstants.ERROR_CODE_DAO_124, ErrorConstants.ERROR_MESSAGE_DAO_124, cpex );
		} catch ( IOException ioex ) {
			throw new DAOException( ErrorConstants.ERROR_CODE_DAO_124, ErrorConstants.ERROR_MESSAGE_DAO_124, ioex );			
		} catch ( AuthenticationException aex ) {
			throw new DAOException( aex.getCode(), aex.getMessage(), aex );
		} catch ( Exception ex ) {
			throw new DAOException( ErrorConstants.ERROR_CODE_DAO_121, ErrorConstants.ERROR_MESSAGE_DAO_121, ex );
		} finally {
			if ( httpClient != null ) {
		        // When HttpClient instance is no longer needed, 
		        // shut down the connection manager to ensure
		        // immediate deallocation of all system resources
		        httpClient.getConnectionManager().shutdown();			
			}
		}
		
		
		return xmlString;
	}
	
	
	/**
	 * Authenticate the widget data sources with an idkey and at against the widget data sources.
	 * 
	 * 
	 * @param idkey				The idkey for authenticating the widget data sources.
	 * @param at				The at for authenticating the widget data sources.
	 * @param uriString			The URI string for connecting to the widget data sources.
	 * @param encoding			The character encoding to be used.
	 * @return xmlString		The information of the authenticated widget user's data sources in XML format.
	 * @throws DAOException		DAO exception.
	 */
	public String authenticateWidgetDataSources( String idkey, String at, String uriString, String encoding ) throws DAOException {
		logger.log( Level.DEBUG, "Method has been called." );
		HttpClient httpClient			= null;
		HttpGet httpGet					= null;
		HttpResponse httpResponse		= null;
		HttpEntity httpEntity			= null;
		StatusLine statusLine			= null;
		String xmlString				= null;
		String xmlStringFiltered		= null;

		
		try {
			// Update the URI string i.e. set the idkey and at parameter values
			uriString					= uriString.replace( "{idkey}", idkey );
			uriString					= uriString.replace( "{at}", at );

			// Create an instance of HttpClient
			httpClient					= new DefaultHttpClient();
		    
		    // Create an instance of HttpGet
		    httpGet						= new HttpGet( StringUtils.toSafeASCIIString( uriString ) ); // Important for encoding the URI correctly!
		    
		    // Execute the HTTP request
    		logger.log( Level.DEBUG, "Executing HTTP request: " + httpGet.getURI() );
	        httpResponse				= httpClient.execute( httpGet );

	        // Retrieve the HTTP response entity
    		httpEntity					= httpResponse.getEntity();

    		if ( httpEntity != null ) {
    			// Retrieve the HTTP response status line
    			statusLine				= httpResponse.getStatusLine();

    			if ( statusLine.getStatusCode() == HttpServletResponse.SC_OK ) {
    				// Retrieve the body of the response (content)
    				if ( EntityUtils.getContentCharSet( httpEntity ) != null ) {
        				// Decode using the response character encoding and transform to the given encoding format
    					// Work around for responses with non-matching encoding in the XML declaration and in the content charset
        				xmlString		= XMLUtils.getInputStreamAsString( httpEntity.getContent(), EntityUtils.getContentCharSet( httpEntity ), encoding );
    				}
    				else {
        				// Decode using the encoding in the XML declaration and transform to the given encoding format
        				xmlString		= XMLUtils.getInputStreamAsString( httpEntity.getContent(), encoding );    					
    				}
    			}    			
    		}
    		
	        // Ensure that the result does not consist only of the XML declaration
			try {
				xmlStringFiltered		= XMLUtils.filterXMLDeclaration( xmlString );
			} catch ( XMLException xmlex ) {
				xmlStringFiltered		= null;
			} catch ( Exception ex ) {
				xmlStringFiltered		= null;
			}
			
			// Incorrect idkey/at combination
			if ( xmlString == null || xmlStringFiltered.length() == 0 ) {
				throw new AuthenticationException( ErrorConstants.ERROR_CODE_AUTHENTICATION_6, ErrorConstants.ERROR_MESSAGE_AUTHENTICATION_6 );
			}

			logger.log( Level.DEBUG, "Result of the XQuery:" + Constants.LINE_SEPARATOR + xmlString );
		} catch ( ClientProtocolException cpex ) {
			throw new DAOException( ErrorConstants.ERROR_CODE_DAO_128, ErrorConstants.ERROR_MESSAGE_DAO_128, cpex );
		} catch ( IOException ioex ) {
			throw new DAOException( ErrorConstants.ERROR_CODE_DAO_128, ErrorConstants.ERROR_MESSAGE_DAO_128, ioex );			
		} catch ( AuthenticationException aex ) {
			throw new DAOException( aex.getCode(), aex.getMessage(), aex );
		} catch ( Exception ex ) {
			throw new DAOException( ErrorConstants.ERROR_CODE_DAO_129, ErrorConstants.ERROR_MESSAGE_DAO_129, ex );
		} finally {
			if ( httpClient != null ) {
		        // When HttpClient instance is no longer needed, 
		        // shut down the connection manager to ensure
		        // immediate deallocation of all system resources
		        httpClient.getConnectionManager().shutdown();			
			}
		}
		
		
		return xmlString;
	}
}