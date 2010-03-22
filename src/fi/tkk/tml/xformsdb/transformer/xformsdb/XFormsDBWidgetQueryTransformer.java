package fi.tkk.tml.xformsdb.transformer.xformsdb;

import javax.servlet.http.HttpSession;

import nu.xom.Attribute;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fi.tkk.tml.xformsdb.core.Constants;
import fi.tkk.tml.xformsdb.error.ErrorConstants;
import fi.tkk.tml.xformsdb.error.ManagerException;
import fi.tkk.tml.xformsdb.error.TransformerException;
import fi.tkk.tml.xformsdb.manager.xformsdb.XFormsDBUserManager;
import fi.tkk.tml.xformsdb.util.IOUtils;


/**
 * Transform the <xformsdb:widgetquery> element.
 * 
 * 
 * @author Markku Laine
 * @version 1.0	 Created on December 04, 2009
 */
public class XFormsDBWidgetQueryTransformer {

	

	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger				= Logger.getLogger( XFormsDBWidgetQueryTransformer.class );
	
	
	// PRIVATE CONSTURCTORS
	// Prevent instantiation of this class
	private XFormsDBWidgetQueryTransformer() {
		logger.log( Level.DEBUG, "Constructor has been called." );
	}
	
	
	// PRIVATE STATIC METHODS
	private static String getLoggedInXFormsDBUser( HttpSession session, String encoding ) throws TransformerException {
		logger.log( Level.DEBUG, "Method has been called." );
		String xformsdbUser							= null;
		
		
		try {
			// Retrieve the logged in user from XFormsDB
			xformsdbUser							= XFormsDBUserManager.getLoggedInXFormsDBUser( session );
			
			if ( xformsdbUser == null ) {
				// Write the response XML string i.e. empty XFormsDB user element
				xformsdbUser						= "<?xml version=\"" + Constants.OUTPUT_XML_VERSION_1_0 + "\" encoding=\"" + encoding + "\"?>" + Constants.LINE_SEPARATOR;
				xformsdbUser						+= "<" + Constants.XFORMSDB_USER + " " + Constants.NAMESPACE_XMLNS_EMPTY + " " + Constants.NAMESPACE_XMLNS_XFORMSDB + " />";
//				throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_68, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_68 );
			}
			logger.log( Level.DEBUG, "The logged in user has been successfully retrieved from XFormsDB." );
		} catch ( ManagerException mex ) {
			throw new TransformerException( mex.getCode(), mex.getMessage(), mex );
//		} catch ( TransformerException tex ) {
//			throw tex;
		} catch ( Exception ex ) {
			throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_85, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_85, ex  );
		}
		
		
		return xformsdbUser;
	}
	
	
	// PUBLIC STATIC METHODS
	public static void transform( HttpSession session, Document document, Element activeXFormsDBWidgetQueryElement, String expressionType, String encoding ) throws TransformerException {
		logger.log( Level.DEBUG, "Method has been called." );

		
		try {
			// Validate
			if ( expressionType == null || ( Constants.EXPRESSION_TYPE_ALL.equals( expressionType ) == false && Constants.EXPRESSION_TYPE_SELECT.equals( expressionType ) == false && Constants.EXPRESSION_TYPE_SELECT4UPDATE.equals( expressionType ) == false && Constants.EXPRESSION_TYPE_UPDATE.equals( expressionType ) == false ) ) {
				throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_86, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_86 );
			}

			
			// Create a temporary copy of the <xformsdb:widgetquery> element (secured/sent) and its children
			Element sentXFormsDBWidgetQueryElement					= new Element( document.getRootElement() );
			
			// Create a copy of the <xformsdb:widgetquery> element (unsecured/stored)
			Element newXFormsDBWidgetQueryElement					= new Element( activeXFormsDBWidgetQueryElement );

			// Add type
			newXFormsDBWidgetQueryElement.addAttribute( new Attribute( "type", expressionType ) );

			// Copy the XML Namespace declarations for the <xformsdb:widgetquery> element
			String sentXFormsDBWidgetQueryElementNamespacePrefix	= null;
			String sentXFormsDBWidgetQueryElementNamespaceURI		= null;
			for ( int i = 0; i < sentXFormsDBWidgetQueryElement.getNamespaceDeclarationCount(); i++ ) {
				sentXFormsDBWidgetQueryElementNamespacePrefix		= sentXFormsDBWidgetQueryElement.getNamespacePrefix( i );
				sentXFormsDBWidgetQueryElementNamespaceURI			= sentXFormsDBWidgetQueryElement.getNamespaceURI( sentXFormsDBWidgetQueryElementNamespacePrefix );
				newXFormsDBWidgetQueryElement.addNamespaceDeclaration( sentXFormsDBWidgetQueryElementNamespacePrefix, sentXFormsDBWidgetQueryElementNamespaceURI );
			}
			
			// Copy the values (can be XML, too) for the <xformsdb:var> elements
			Elements newXFormsDBWidgetQueryElementXFormsDBVarElements	= newXFormsDBWidgetQueryElement.getChildElements( "var", Constants.NAMESPACE_URI_XFORMSDB );
			Elements sentXFormsDBWidgetQueryElementXFormsDBVarElements	= sentXFormsDBWidgetQueryElement.getChildElements( "var", Constants.NAMESPACE_URI_XFORMSDB );
			Element newXFormsDBWidgetQueryElementXFormsDBVarElement		= null;
			Element sentXFormsDBWidgetQueryElementXFormsDBVarElement	= null;
			for ( int i = 0; i < newXFormsDBWidgetQueryElementXFormsDBVarElements.size(); i++ ) {
				newXFormsDBWidgetQueryElementXFormsDBVarElement			= newXFormsDBWidgetQueryElementXFormsDBVarElements.get( i );
				for ( int j = 0; j < sentXFormsDBWidgetQueryElementXFormsDBVarElements.size(); j++ ) {
					sentXFormsDBWidgetQueryElementXFormsDBVarElement	= sentXFormsDBWidgetQueryElementXFormsDBVarElements.get( j );
					if ( newXFormsDBWidgetQueryElementXFormsDBVarElement.getAttributeValue( "name" ).equals( sentXFormsDBWidgetQueryElementXFormsDBVarElement.getAttributeValue( "name" ) ) == true ) {
						newXFormsDBWidgetQueryElementXFormsDBVarElement.getParent().replaceChild( newXFormsDBWidgetQueryElementXFormsDBVarElement, new Element( sentXFormsDBWidgetQueryElementXFormsDBVarElement ) );
						break;
					}
				}
			}

			// Copy/retrieve the values (text only) for the <xformsdb:secvar> elements
			Elements newXFormsDBWidgetQueryElementXFormsDBSecvarElements	= newXFormsDBWidgetQueryElement.getChildElements( "secvar", Constants.NAMESPACE_URI_XFORMSDB );
			Element newXFormsDBWidgetQueryElementXFormsDBSecvarElement		= null;
			Builder builder													= new Builder();
			String username													= null;
			String roles													= null;
			for ( int i = 0; i < newXFormsDBWidgetQueryElementXFormsDBSecvarElements.size(); i++ ) {
				newXFormsDBWidgetQueryElementXFormsDBSecvarElement			= newXFormsDBWidgetQueryElementXFormsDBSecvarElements.get( i );
				// Handle the attribute name "username" i.e. retrieve the value from the session
				if ( "username".equals( newXFormsDBWidgetQueryElementXFormsDBSecvarElement.getAttributeValue( "name" ) ) == true ) {
					username												= builder.build( IOUtils.convert( XFormsDBWidgetQueryTransformer.getLoggedInXFormsDBUser( session, encoding ), encoding ) ).getRootElement().getAttributeValue( "username" );
					newXFormsDBWidgetQueryElementXFormsDBSecvarElement.removeChildren();
					if ( username != null ) {
						newXFormsDBWidgetQueryElementXFormsDBSecvarElement.appendChild( username.trim() );
					}
					else {
						newXFormsDBWidgetQueryElementXFormsDBSecvarElement.appendChild( "" );
					}
				}				
				// Handle the attribute name "roles" i.e. retrieve the value from the session
				if ( "roles".equals( newXFormsDBWidgetQueryElementXFormsDBSecvarElement.getAttributeValue( "name" ) ) == true ) {
					roles													= builder.build( IOUtils.convert( XFormsDBWidgetQueryTransformer.getLoggedInXFormsDBUser( session, encoding ), encoding ) ).getRootElement().getAttributeValue( "roles" );
					newXFormsDBWidgetQueryElementXFormsDBSecvarElement.removeChildren();
					if ( roles != null ) {
						newXFormsDBWidgetQueryElementXFormsDBSecvarElement.appendChild( roles.trim() );
					}
					else {
						newXFormsDBWidgetQueryElementXFormsDBSecvarElement.appendChild( "" );
					}
				}				
			}

			// Copy the <xformsdb:attachment> element and its children
			Element sentXFormsDBWidgetQueryElementXFormsDBAttachmentElement		= sentXFormsDBWidgetQueryElement.getFirstChildElement( "attachment", Constants.NAMESPACE_URI_XFORMSDB );
			if ( sentXFormsDBWidgetQueryElementXFormsDBAttachmentElement != null ) {
				Element newXFormsDBWidgetQueryElementXFormsDBAttachmentElement	= newXFormsDBWidgetQueryElement.getFirstChildElement( "attachment", Constants.NAMESPACE_URI_XFORMSDB );
				if ( newXFormsDBWidgetQueryElementXFormsDBAttachmentElement == null ) {
					// Does not have the <xformsdb:attachment> element
					newXFormsDBWidgetQueryElement.appendChild( new Element( sentXFormsDBWidgetQueryElementXFormsDBAttachmentElement ) );
				}
				else {
					// Already has the <xformsdb:attachment> element
					newXFormsDBWidgetQueryElementXFormsDBAttachmentElement.getParent().replaceChild( newXFormsDBWidgetQueryElementXFormsDBAttachmentElement, new Element( sentXFormsDBWidgetQueryElementXFormsDBAttachmentElement ) );
				}
			}

			// Update the <xformsdb:widgetquery> element (secured/sent)
			document.setRootElement( new Element( newXFormsDBWidgetQueryElement ) );
			logger.log( Level.DEBUG, "The <xformsdb:widgetquery> element has been successfully transformed." );
		} catch ( TransformerException tex ) {
			throw tex;
		} catch ( Exception ex ) {
			throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_87, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_87, ex );
		}
	}
}