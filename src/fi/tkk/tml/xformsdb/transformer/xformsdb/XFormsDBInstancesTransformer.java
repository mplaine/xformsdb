package fi.tkk.tml.xformsdb.transformer.xformsdb;

import java.net.URLDecoder;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fi.tkk.tml.xformsdb.core.Constants;
import fi.tkk.tml.xformsdb.error.ErrorConstants;
import fi.tkk.tml.xformsdb.error.TransformerException;
import fi.tkk.tml.xformsdb.servlet.XFormsDBServlet;


/**
 * Transform the <xformsdb:instance> elements.
 * 
 * The transformed <xformsdb:instance> elements will be given
 * as an XML formatted parameter to the xformsdb_xforms.xsl stylesheet
 * which replaces all <xformsdb:instance> elements with
 * an appropriate transformation result.
 *
 * @author Markku Laine
 * @version 1.0	 Created on January 5, 2016
 */
public class XFormsDBInstancesTransformer {

	

	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger							= Logger.getLogger( XFormsDBInstancesTransformer.class );
	
	
	// PRIVATE CONSTURCTORS
	// Prevent instantiation of this class
	private XFormsDBInstancesTransformer() {
		logger.log( Level.DEBUG, "Constructor has been called." );
	}
	
	
	// PUBLIC STATIC METHODS
	public static void transform( XFormsDBServlet xformsdbServlet, HttpServletRequest request, Document document, Map<String, String> expressionTypeUpdateRequestInstances, Map<String, String> stateTypeSetRequestInstances, Map<String, String> fileTypeInsertRequestInstances, Map<String, String> fileTypeDeleteRequestInstances, Map<String, String> fileTypeUpdateRequestInstances ) throws TransformerException {
		logger.log( Level.DEBUG, "Method has been called." );

		
		try {
			String namespacePrefixXFormsDBWithColon				= Constants.NAMESPACE_PREFIX_XFORMSDB + ( ( "".equals( Constants.NAMESPACE_PREFIX_XFORMSDB ) == true ) ? "" : ":" );

			Element xformsdbInstancesElement					= document.getRootElement().getFirstChildElement( "instances", Constants.NAMESPACE_URI_XFORMSDB );
			Elements xformsdbInstanceElements					= xformsdbInstancesElement.getChildElements( "instance", Constants.NAMESPACE_URI_XFORMSDB );
			Element xformsdbInstanceElement						= null;
			for ( int i = 0; i < xformsdbInstanceElements.size(); i++ ) {
				xformsdbInstanceElement							= xformsdbInstanceElements.get( i );
				
				// Validate
				if (	xformsdbInstanceElement.getAttribute( "id" ) == null ||
						"".equals( xformsdbInstanceElement.getAttributeValue( "id" ) ) == true ) {
					throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_15, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_15 );
				}

				
				// Update properties if needed
				Element xformsdbQueryElement					= xformsdbInstanceElement.getFirstChildElement( "query", Constants.NAMESPACE_URI_XFORMSDB );
				Element xformsdbWidgetQueryElement				= xformsdbInstanceElement.getFirstChildElement( "widgetquery", Constants.NAMESPACE_URI_XFORMSDB );
				Element xformsdbStateElement					= xformsdbInstanceElement.getFirstChildElement( "state", Constants.NAMESPACE_URI_XFORMSDB );
				Element xformsdbLoginElement					= xformsdbInstanceElement.getFirstChildElement( "login", Constants.NAMESPACE_URI_XFORMSDB );
				Element xformsdbLogoutElement					= xformsdbInstanceElement.getFirstChildElement( "logout", Constants.NAMESPACE_URI_XFORMSDB );
				Element xformsdbUserElement						= xformsdbInstanceElement.getFirstChildElement( "user", Constants.NAMESPACE_URI_XFORMSDB );
				Element xformsdbFileElement						= xformsdbInstanceElement.getFirstChildElement( "file", Constants.NAMESPACE_URI_XFORMSDB );
				Element xformsdbCookieElement					= xformsdbInstanceElement.getFirstChildElement( "cookie", Constants.NAMESPACE_URI_XFORMSDB );
				if ( xformsdbQueryElement != null ) {
					Element xformsdbExpression					= null;
					// Add the <xformsdb:expression> element if needed
					if ( xformsdbQueryElement.getFirstChildElement( "expression", Constants.NAMESPACE_URI_XFORMSDB ) == null ) {
						xformsdbExpression						= new Element( namespacePrefixXFormsDBWithColon + "expression", Constants.NAMESPACE_URI_XFORMSDB );
						xformsdbQueryElement.appendChild( xformsdbExpression );
					}
					xformsdbExpression							= xformsdbQueryElement.getFirstChildElement( "expression", Constants.NAMESPACE_URI_XFORMSDB );
					
					// Update resource if needed
					if ( xformsdbExpression.getAttribute( "resource" ) != null ) {
						String requestURI						= URLDecoder.decode( request.getRequestURI(), Constants.CLIENT_URI_ENCODING_DEFAULT ); // Without query string
						String requestContextPath				= URLDecoder.decode( request.getContextPath(), Constants.CLIENT_URI_ENCODING_DEFAULT ); // Without query string
						String requestPath						= requestURI.substring( requestContextPath.length(), requestURI.length() ); // Request URI - context path
						int requestPathLastIndexOfSlash			= requestPath.lastIndexOf( "/" );
						String requestPathSubstring				= null;
						if ( requestPathLastIndexOfSlash != -1 ) {
							requestPathSubstring				= requestPath.substring( 0, requestPathLastIndexOfSlash + 1 );						
						}
						else {
							requestPathSubstring				= requestPath;
						}
						String queryExpressionResource			= xformsdbExpression.getAttributeValue( "resource" );
						String filePath							= requestPathSubstring + queryExpressionResource;
						String realFilePath						= xformsdbServlet.getServletContext().getRealPath( filePath );
						xformsdbExpression.addAttribute( new Attribute( "resource", realFilePath ) );
					}
					// Add inline if needed
					else if ( "".equals( xformsdbExpression.getValue() ) == true ) {
						xformsdbExpression.appendChild( "/" );
					}

					// Add xformsdb:attachment if needed
					if ( expressionTypeUpdateRequestInstances.get( xformsdbInstanceElement.getAttributeValue( "id" ) ) != null ) {
						Element xformsdbAttachment				= new Element( namespacePrefixXFormsDBWithColon + "attachment", Constants.NAMESPACE_URI_XFORMSDB );
						xformsdbQueryElement.appendChild( xformsdbAttachment );
					}
					
					// Add id
					xformsdbQueryElement.addAttribute( new Attribute( "id", UUID.randomUUID().toString() ) );
				}
				if ( xformsdbWidgetQueryElement != null ) {
					Element xformsdbExpression					= null;
					// Add the <xformsdb:expression> element if needed
					if ( xformsdbWidgetQueryElement.getFirstChildElement( "expression", Constants.NAMESPACE_URI_XFORMSDB ) == null ) {
						xformsdbExpression						= new Element( namespacePrefixXFormsDBWithColon + "expression", Constants.NAMESPACE_URI_XFORMSDB );
						xformsdbWidgetQueryElement.appendChild( xformsdbExpression );
					}
					xformsdbExpression							= xformsdbWidgetQueryElement.getFirstChildElement( "expression", Constants.NAMESPACE_URI_XFORMSDB );
					
					// Update resource if needed
					if ( xformsdbExpression.getAttribute( "resource" ) != null ) {
						String requestURI						= URLDecoder.decode( request.getRequestURI(), Constants.CLIENT_URI_ENCODING_DEFAULT ); // Without query string
						String requestContextPath				= URLDecoder.decode( request.getContextPath(), Constants.CLIENT_URI_ENCODING_DEFAULT ); // Without query string
						String requestPath						= requestURI.substring( requestContextPath.length(), requestURI.length() ); // Request URI - context path
						int requestPathLastIndexOfSlash			= requestPath.lastIndexOf( "/" );
						String requestPathSubstring				= null;
						if ( requestPathLastIndexOfSlash != -1 ) {
							requestPathSubstring				= requestPath.substring( 0, requestPathLastIndexOfSlash + 1 );
						}
						else {
							requestPathSubstring				= requestPath;
						}
						String widgetQueryExpressionResource	= xformsdbExpression.getAttributeValue( "resource" );
						String filePath							= requestPathSubstring + widgetQueryExpressionResource;
						String realFilePath						= xformsdbServlet.getServletContext().getRealPath( filePath );
						xformsdbExpression.addAttribute( new Attribute( "resource", realFilePath ) );
					}
					// Add inline if needed
					else if ( "".equals( xformsdbExpression.getValue() ) == true ) {
						xformsdbExpression.appendChild( "/" );
					}

					// Add xformsdb:attachment if needed
					if ( expressionTypeUpdateRequestInstances.get( xformsdbInstanceElement.getAttributeValue( "id" ) ) != null ) {
						Element xformsdbAttachment				= new Element( namespacePrefixXFormsDBWithColon + "attachment", Constants.NAMESPACE_URI_XFORMSDB );
						xformsdbWidgetQueryElement.appendChild( xformsdbAttachment );
					}

					// Add id
					xformsdbWidgetQueryElement.addAttribute( new Attribute( "id", UUID.randomUUID().toString() ) );
				}
				else if ( xformsdbStateElement != null ) {
					// Add attachment if needed
					if ( stateTypeSetRequestInstances.get( xformsdbInstanceElement.getAttributeValue( "id" ) ) != null ) {
						Element xformsdbAttachment				= new Element( namespacePrefixXFormsDBWithColon + "attachment", Constants.NAMESPACE_URI_XFORMSDB );
						xformsdbStateElement.appendChild( xformsdbAttachment );
					}
				}
				else if ( xformsdbLoginElement != null ) {
					// Add resource
					Element xformsdbExpression					= new Element( namespacePrefixXFormsDBWithColon + "expression", Constants.NAMESPACE_URI_XFORMSDB );
					xformsdbLoginElement.appendChild( xformsdbExpression );
					xformsdbExpression.addAttribute( new Attribute( "resource", Constants.XQ_AUTHENTICATE_USER_FILE_PATH ) );

					// Add id
					xformsdbLoginElement.addAttribute( new Attribute( "id", UUID.randomUUID().toString() ) );
				}
				else if ( xformsdbLogoutElement != null ) {
					// Nothing to be updated
				}
				else if ( xformsdbUserElement != null ) {
					// Nothing to be updated
				}
				else if ( xformsdbFileElement != null ) {
					// Add a dummy expression element without the resource attribute which will be replaced later
					Element xformsdbExpression			= new Element( namespacePrefixXFormsDBWithColon + "expression", Constants.NAMESPACE_URI_XFORMSDB );
					xformsdbFileElement.appendChild( xformsdbExpression );

					// A correct expression element and a resource attribute cannot be added here because we do not know the type of the request to be submitted yet
					// Insert
					if ( fileTypeInsertRequestInstances.get( xformsdbInstanceElement.getAttributeValue( "id" ) ) != null ) {
						/*
						// Add resource
						String filePath							= Constants.XQ_SELECT_FILES_FILE_PATH;; //Constants.XQ_INSERT_FILES_FILE_PATH;
						String realFilePath						= xformsdbServlet.getServletContext().getRealPath( filePath );
						Element xformsdbExpression				= new Element( namespacePrefixXFormsDBWithColon + "expression", Constants.NAMESPACE_URI_XFORMSDB );
						xformsdbFileElement.appendChild( xformsdbExpression );
						xformsdbExpression.addAttribute( new Attribute( "resource", realFilePath ) );
						*/
						// Add xformsdb:attachment
						Element xformsdbAttachment				= new Element( namespacePrefixXFormsDBWithColon + "attachment", Constants.NAMESPACE_URI_XFORMSDB );
						xformsdbFileElement.appendChild( xformsdbAttachment );
					}
					// Delete
					else if ( fileTypeDeleteRequestInstances.get( xformsdbInstanceElement.getAttributeValue( "id" ) ) != null ) {
						/*
						// Add resource
						String filePath							= Constants.XQ_SELECT_FILES_FILE_PATH;; //Constants.XQ_DELETE_FILES_FILE_PATH;
						String realFilePath						= xformsdbServlet.getServletContext().getRealPath( filePath );
						Element xformsdbExpression				= new Element( namespacePrefixXFormsDBWithColon + "expression", Constants.NAMESPACE_URI_XFORMSDB );
						xformsdbFileElement.appendChild( xformsdbExpression );
						xformsdbExpression.addAttribute( new Attribute( "resource", realFilePath ) );
						*/						
						// Add xformsdb:attachment
						Element xformsdbAttachment				= new Element( namespacePrefixXFormsDBWithColon + "attachment", Constants.NAMESPACE_URI_XFORMSDB );
						xformsdbFileElement.appendChild( xformsdbAttachment );
					}
					// Update
					else if ( fileTypeUpdateRequestInstances.get( xformsdbInstanceElement.getAttributeValue( "id" ) ) != null ) {
						/*
						// Add resource
						String filePath							= Constants.XQ_SELECT_FILES_FILE_PATH;; //Constants.XQ_UPDATE_FILES_FILE_PATH;
						String realFilePath						= xformsdbServlet.getServletContext().getRealPath( filePath );
						Element xformsdbExpression				= new Element( namespacePrefixXFormsDBWithColon + "expression", Constants.NAMESPACE_URI_XFORMSDB );
						xformsdbFileElement.appendChild( xformsdbExpression );
						xformsdbExpression.addAttribute( new Attribute( "resource", realFilePath ) );
						*/
						// Add xformsdb:attachment
						Element xformsdbAttachment				= new Element( namespacePrefixXFormsDBWithColon + "attachment", Constants.NAMESPACE_URI_XFORMSDB );
						xformsdbFileElement.appendChild( xformsdbAttachment );
					}
					// Select
					else {
						/*
						// Add resource
						String filePath							= Constants.XQ_SELECT_FILES_FILE_PATH;
						String realFilePath						= xformsdbServlet.getServletContext().getRealPath( filePath );
						Element xformsdbExpression				= new Element( namespacePrefixXFormsDBWithColon + "expression", Constants.NAMESPACE_URI_XFORMSDB );
						xformsdbFileElement.appendChild( xformsdbExpression );
						xformsdbExpression.addAttribute( new Attribute( "resource", realFilePath ) );
						*/
					}

					// Add id
					xformsdbFileElement.addAttribute( new Attribute( "id", UUID.randomUUID().toString() ) );
				}
				else if ( xformsdbCookieElement != null ) {
					// Nothing to be updated
				}
			}
			
			
			logger.log( Level.DEBUG, "The <xformsdb:instance> elements have been successfully transformed." );			
		} catch ( TransformerException tex ) {
			throw tex;
		} catch ( Exception ex ) {
			throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_10, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_10, ex );
		}
	}	
}