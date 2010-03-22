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
 * Transform the <xformsdb:query> element.
 * 
 * 
 * @author Markku Laine
 * @version 1.0	 Created on December 04, 2009
 */
public class XFormsDBQueryTransformer {

	

	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger				= Logger.getLogger( XFormsDBQueryTransformer.class );
	
	
	// PRIVATE CONSTURCTORS
	// Prevent instantiation of this class
	private XFormsDBQueryTransformer() {
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
			throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_69, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_69, ex  );
		}
		
		
		return xformsdbUser;
	}
	
	
	// PUBLIC STATIC METHODS
	public static void transform( HttpSession session, Document document, Element activeXFormsDBQueryElement, String expressionType, String encoding ) throws TransformerException {
		logger.log( Level.DEBUG, "Method has been called." );

		
		try {
			// Validate
			if ( expressionType == null || ( Constants.EXPRESSION_TYPE_ALL.equals( expressionType ) == false && Constants.EXPRESSION_TYPE_SELECT.equals( expressionType ) == false && Constants.EXPRESSION_TYPE_SELECT4UPDATE.equals( expressionType ) == false && Constants.EXPRESSION_TYPE_UPDATE.equals( expressionType ) == false ) ) {
				throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_71, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_71 );
			}

			
			// Create a temporary copy of the <xformsdb:query> element (secured/sent) and its children
			Element sentXFormsDBQueryElement								= new Element( document.getRootElement() );
			
			// Create a copy of the <xformsdb:query> element (unsecured/stored)
			Element newXFormsDBQueryElement									= new Element( activeXFormsDBQueryElement );

			// Add type
			newXFormsDBQueryElement.addAttribute( new Attribute( "type", expressionType ) );

			// Copy the XML Namespace declarations for the <xformsdb:query> element
			String sentXFormsDBQueryElementNamespacePrefix					= null;
			String sentXFormsDBQueryElementNamespaceURI						= null;
			for ( int i = 0; i < sentXFormsDBQueryElement.getNamespaceDeclarationCount(); i++ ) {
				sentXFormsDBQueryElementNamespacePrefix						= sentXFormsDBQueryElement.getNamespacePrefix( i );
				sentXFormsDBQueryElementNamespaceURI						= sentXFormsDBQueryElement.getNamespaceURI( sentXFormsDBQueryElementNamespacePrefix );
				newXFormsDBQueryElement.addNamespaceDeclaration( sentXFormsDBQueryElementNamespacePrefix, sentXFormsDBQueryElementNamespaceURI );
			}
			
			// Copy the values (can be XML, too) for the <xformsdb:var> elements
			Elements newXFormsDBQueryElementXFormsDBVarElements				= newXFormsDBQueryElement.getChildElements( "var", Constants.NAMESPACE_URI_XFORMSDB );
			Elements sentXFormsDBQueryElementXFormsDBVarElements			= sentXFormsDBQueryElement.getChildElements( "var", Constants.NAMESPACE_URI_XFORMSDB );
			Element newXFormsDBQueryElementXFormsDBVarElement				= null;
			Element sentXFormsDBQueryElementXFormsDBVarElement				= null;
			for ( int i = 0; i < newXFormsDBQueryElementXFormsDBVarElements.size(); i++ ) {
				newXFormsDBQueryElementXFormsDBVarElement					= newXFormsDBQueryElementXFormsDBVarElements.get( i );
				for ( int j = 0; j < sentXFormsDBQueryElementXFormsDBVarElements.size(); j++ ) {
					sentXFormsDBQueryElementXFormsDBVarElement				= sentXFormsDBQueryElementXFormsDBVarElements.get( j );
					if ( newXFormsDBQueryElementXFormsDBVarElement.getAttributeValue( "name" ).equals( sentXFormsDBQueryElementXFormsDBVarElement.getAttributeValue( "name" ) ) == true ) {
						newXFormsDBQueryElementXFormsDBVarElement.getParent().replaceChild( newXFormsDBQueryElementXFormsDBVarElement, new Element( sentXFormsDBQueryElementXFormsDBVarElement ) );
						break;
					}
				}
			}

			// Copy/retrieve the values (text only) for the <xformsdb:secvar> elements
			Elements newXFormsDBQueryElementXFormsDBSecvarElements			= newXFormsDBQueryElement.getChildElements( "secvar", Constants.NAMESPACE_URI_XFORMSDB );
			Element newXFormsDBQueryElementXFormsDBSecvarElement			= null;
			Builder builder													= new Builder();
			String username													= null;
			String roles													= null;
			for ( int i = 0; i < newXFormsDBQueryElementXFormsDBSecvarElements.size(); i++ ) {
				newXFormsDBQueryElementXFormsDBSecvarElement				= newXFormsDBQueryElementXFormsDBSecvarElements.get( i );
				// Handle the attribute name "username" i.e. retrieve the value from the session
				if ( "username".equals( newXFormsDBQueryElementXFormsDBSecvarElement.getAttributeValue( "name" ) ) == true ) {
					username												= builder.build( IOUtils.convert( XFormsDBQueryTransformer.getLoggedInXFormsDBUser( session, encoding ), encoding ) ).getRootElement().getAttributeValue( "username" );
					newXFormsDBQueryElementXFormsDBSecvarElement.removeChildren();
					if ( username != null ) {
						newXFormsDBQueryElementXFormsDBSecvarElement.appendChild( username.trim() );
					}
					else {
						newXFormsDBQueryElementXFormsDBSecvarElement.appendChild( "" );
					}
				}				
				// Handle the attribute name "roles" i.e. retrieve the value from the session
				else if ( "roles".equals( newXFormsDBQueryElementXFormsDBSecvarElement.getAttributeValue( "name" ) ) == true ) {
					roles													= builder.build( IOUtils.convert( XFormsDBQueryTransformer.getLoggedInXFormsDBUser( session, encoding ), encoding ) ).getRootElement().getAttributeValue( "roles" );
					newXFormsDBQueryElementXFormsDBSecvarElement.removeChildren();
					if ( roles != null ) {
						newXFormsDBQueryElementXFormsDBSecvarElement.appendChild( roles.trim() );
					}
					else {
						newXFormsDBQueryElementXFormsDBSecvarElement.appendChild( "" );
					}
				}				
			}

			// Copy the <xformsdb:attachment> element and its children
			Element sentXFormsDBQueryElementXFormsDBAttachmentElement		= sentXFormsDBQueryElement.getFirstChildElement( "attachment", Constants.NAMESPACE_URI_XFORMSDB );
			if ( sentXFormsDBQueryElementXFormsDBAttachmentElement != null ) {
				Element newXFormsDBQueryElementXFormsDBAttachmentElement	= newXFormsDBQueryElement.getFirstChildElement( "attachment", Constants.NAMESPACE_URI_XFORMSDB );
				if ( newXFormsDBQueryElementXFormsDBAttachmentElement == null ) {
					// Does not have the <xformsdb:attachment> element
					newXFormsDBQueryElement.appendChild( new Element( sentXFormsDBQueryElementXFormsDBAttachmentElement ) );
				}
				else {
					// Already has the <xformsdb:attachment> element
					newXFormsDBQueryElementXFormsDBAttachmentElement.getParent().replaceChild( newXFormsDBQueryElementXFormsDBAttachmentElement, new Element( sentXFormsDBQueryElementXFormsDBAttachmentElement ) );
				}
			}

			// Update the <xformsdb:query> element (secured/sent)
			document.setRootElement( new Element( newXFormsDBQueryElement ) );
			logger.log( Level.DEBUG, "The <xformsdb:query> element has been successfully transformed." );
		} catch ( TransformerException tex ) {
			throw tex;
		} catch ( Exception ex ) {
			throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_70, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_70, ex );
		}
	}
}