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
import fi.tkk.tml.xformsdb.servlet.XFormsDBServlet;
import fi.tkk.tml.xformsdb.util.IOUtils;


/**
 * Transform the <xformsdb:file> element.
 * 
 * 
 * @author Markku Laine
 * @version 1.0	 Created on February 23, 2010
 */
public class XFormsDBFileTransformer {

	

	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger					= Logger.getLogger( XFormsDBFileTransformer.class );
	
	
	// PRIVATE CONSTURCTORS
	// Prevent instantiation of this class
	private XFormsDBFileTransformer() {
		logger.log( Level.DEBUG, "Constructor has been called." );
	}
	
	
	// PRIVATE STATIC METHODS
	private static String getUsernameOfTheLoggedInXFormsDBUser( HttpSession session, String encoding ) throws TransformerException {
		logger.log( Level.DEBUG, "Method has been called." );
		String xformsdbUser								= null;
		String usernameOfTheLoggedInXFormsDBUser		= null;
		
		
		try {
			// Retrieve the logged in user from XFormsDB
			xformsdbUser								= XFormsDBUserManager.getLoggedInXFormsDBUser( session );
			
			// XFormsDB does not hold the logged in user
			if ( xformsdbUser == null ) {
				usernameOfTheLoggedInXFormsDBUser		= "";
			}
			// Retrieve the username of the logged in XFormsDB user
			else {
				Builder builder							= new Builder();
				Document document						= builder.build( IOUtils.convert( xformsdbUser, encoding ) );
				usernameOfTheLoggedInXFormsDBUser		= document.getRootElement().getAttributeValue( "username" );
				usernameOfTheLoggedInXFormsDBUser		= usernameOfTheLoggedInXFormsDBUser.trim();
			}
			
			
			logger.log( Level.DEBUG, "The username of the logged in user have been successfully retrieved from XFormsDB." );
		} catch ( ManagerException mex ) {
			throw new TransformerException( mex.getCode(), mex.getMessage(), mex );
		} catch ( Exception ex ) {
			throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_92, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_92, ex  );
		}
		
		
		return usernameOfTheLoggedInXFormsDBUser;
	}

	
	private static String getRolesOfTheLoggedInXFormsDBUser( HttpSession session, String encoding ) throws TransformerException {
		logger.log( Level.DEBUG, "Method has been called." );
		String xformsdbUser								= null;
		String rolesOfTheLoggedInXFormsDBUser			= null;
		
		
		try {
			// Retrieve the logged in user from XFormsDB
			xformsdbUser								= XFormsDBUserManager.getLoggedInXFormsDBUser( session );
			
			// XFormsDB does not hold the logged in user
			if ( xformsdbUser == null ) {
				rolesOfTheLoggedInXFormsDBUser			= "";
			}
			// Retrieve the roles of the logged in XFormsDB user
			else {
				Builder builder							= new Builder();
				Document document						= builder.build( IOUtils.convert( xformsdbUser, encoding ) );
				rolesOfTheLoggedInXFormsDBUser			= document.getRootElement().getAttributeValue( "roles" );
				rolesOfTheLoggedInXFormsDBUser			= rolesOfTheLoggedInXFormsDBUser.trim();
			}
			
			
			logger.log( Level.DEBUG, "The roles of the logged in user have been successfully retrieved from XFormsDB." );
		} catch ( ManagerException mex ) {
			throw new TransformerException( mex.getCode(), mex.getMessage(), mex );
		} catch ( Exception ex ) {
			throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_77, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_77, ex  );
		}
		
		
		return rolesOfTheLoggedInXFormsDBUser;
	}

	
	// PUBLIC STATIC METHODS
	public static void transform( XFormsDBServlet xformsdbServlet, HttpSession session, Document document, Element activeXFormsDBFileElement, String fileType, String encoding ) throws TransformerException {
		logger.log( Level.DEBUG, "Method has been called." );

		
		try {
			// Validate
			if (	fileType == null || ( Constants.FILE_TYPE_SELECT.equals( fileType ) == false && Constants.FILE_TYPE_UPDATE.equals( fileType ) == false && Constants.FILE_TYPE_INSERT.equals( fileType ) == false && Constants.FILE_TYPE_DELETE.equals( fileType ) == false ) ) {
				throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_75, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_75 );
			}

			
			String namespacePrefixXFormsDBWithColon					= Constants.NAMESPACE_PREFIX_XFORMSDB + ( ( "".equals( Constants.NAMESPACE_PREFIX_XFORMSDB ) == true ) ? "" : ":" );
			
			// Create a temporary copy of the <xformsdb:file> element (secured/sent) and its children
			Element sentXFormsDBFileElement							= new Element( document.getRootElement() );

			// Create a copy of the <xformsdb:file> element (unsecured/stored)
			Element newXFormsDBFileElement							= new Element( activeXFormsDBFileElement );

			// Add type
			newXFormsDBFileElement.addAttribute( new Attribute( "type", fileType ) );

			// Add the <xformsdb:expression> element (if needed) and the resource attribute
			Element newXFormsDBFileElementXFormsDBExpressionElement	= newXFormsDBFileElement.getFirstChildElement( "expression", Constants.NAMESPACE_URI_XFORMSDB );
			if ( newXFormsDBFileElementXFormsDBExpressionElement == null ) {
				// Does not have the <xformsdb:expression> element
				String filePath										= null;
				if ( Constants.FILE_TYPE_INSERT.equals( fileType ) == true ) {
					filePath										= Constants.XQ_INSERT_FILES_FILE_PATH;
				}
				else if ( Constants.FILE_TYPE_DELETE.equals( fileType ) == true ) {
					filePath										= Constants.XQ_DELETE_FILES_FILE_PATH;
				}
				else if ( Constants.FILE_TYPE_UPDATE.equals( fileType ) == true ) {
					filePath										= Constants.XQ_UPDATE_FILES_FILE_PATH;
				}
				else {
					filePath										= Constants.XQ_SELECT_FILES_FILE_PATH;					
				}
				Element xformsdbExpressionElement					= new Element( namespacePrefixXFormsDBWithColon + "expression", Constants.NAMESPACE_URI_XFORMSDB );
				xformsdbExpressionElement.addAttribute( new Attribute( "resource", filePath ) );
				newXFormsDBFileElement.appendChild( xformsdbExpressionElement );
			}
			else {
				// Already has the <xformsdb:expression> element
				String filePath										= null;
				if ( Constants.FILE_TYPE_INSERT.equals( fileType ) == true ) {
					filePath										= Constants.XQ_INSERT_FILES_FILE_PATH;
				}
				else if ( Constants.FILE_TYPE_DELETE.equals( fileType ) == true ) {
					filePath										= Constants.XQ_DELETE_FILES_FILE_PATH;
				}
				else if ( Constants.FILE_TYPE_UPDATE.equals( fileType ) == true ) {
					filePath										= Constants.XQ_UPDATE_FILES_FILE_PATH;
				}
				else {
					filePath										= Constants.XQ_SELECT_FILES_FILE_PATH;					
				}
				Element xformsdbExpressionElement					= new Element( namespacePrefixXFormsDBWithColon + "expression", Constants.NAMESPACE_URI_XFORMSDB );
				xformsdbExpressionElement.addAttribute( new Attribute( "resource", filePath ) );
				newXFormsDBFileElement.appendChild( xformsdbExpressionElement );
				
				newXFormsDBFileElementXFormsDBExpressionElement.getParent().replaceChild( newXFormsDBFileElementXFormsDBExpressionElement, new Element( xformsdbExpressionElement ) );
			}
			
			// Copy the XML Namespace declarations for the <xformsdb:file> element
			String sentXFormsDBFileElementNamespacePrefix			= null;
			String sentXFormsDBFileElementNamespaceURI				= null;
			for ( int i = 0; i < sentXFormsDBFileElement.getNamespaceDeclarationCount(); i++ ) {
				sentXFormsDBFileElementNamespacePrefix				= sentXFormsDBFileElement.getNamespacePrefix( i );
				sentXFormsDBFileElementNamespaceURI					= sentXFormsDBFileElement.getNamespaceURI( sentXFormsDBFileElementNamespacePrefix );
				newXFormsDBFileElement.addNamespaceDeclaration( sentXFormsDBFileElementNamespacePrefix, sentXFormsDBFileElementNamespaceURI );
			}

			// Copy the values for the <xformsdb:var> elements
			Elements newXFormsDBFileElementXFormsDBVarElements		= newXFormsDBFileElement.getChildElements( "var", Constants.NAMESPACE_URI_XFORMSDB );
			Elements sentXFormsDBFileElementXFormsDBVarElements		= sentXFormsDBFileElement.getChildElements( "var", Constants.NAMESPACE_URI_XFORMSDB );
			Element newXFormsDBFileElementXFormsDBVarElement		= null;
			Element sentXFormsDBFileElementXFormsDBVarElement		= null;
			for ( int i = 0; i < newXFormsDBFileElementXFormsDBVarElements.size(); i++ ) {
				newXFormsDBFileElementXFormsDBVarElement			= newXFormsDBFileElementXFormsDBVarElements.get( i );
				for ( int j = 0; j < sentXFormsDBFileElementXFormsDBVarElements.size(); j++ ) {
					sentXFormsDBFileElementXFormsDBVarElement		= sentXFormsDBFileElementXFormsDBVarElements.get( j );
					if ( newXFormsDBFileElementXFormsDBVarElement.getAttributeValue( "name" ).equals( sentXFormsDBFileElementXFormsDBVarElement.getAttributeValue( "name" ) ) == true ) {
						newXFormsDBFileElementXFormsDBVarElement.getParent().replaceChild( newXFormsDBFileElementXFormsDBVarElement, new Element( sentXFormsDBFileElementXFormsDBVarElement ) );
						break;
					}
				}
			}

			// Copy/retrieve the values (text only) for the <xformsdb:secvar> elements
			Elements newXFormsDBFileElementXFormsDBSecvarElements	= newXFormsDBFileElement.getChildElements( "secvar", Constants.NAMESPACE_URI_XFORMSDB );
			Element newXFormsDBFileElementXFormsDBSecvarElement		= null;
			String usernameOfTheLoggedInXFormsDBUser				= null;
			String rolesOfTheLoggedInXFormsDBUser					= null;
			for ( int i = 0; i < newXFormsDBFileElementXFormsDBSecvarElements.size(); i++ ) {
				newXFormsDBFileElementXFormsDBSecvarElement			= newXFormsDBFileElementXFormsDBSecvarElements.get( i );
				// Handle the attribute name "username" i.e. retrieve the value from the session
				if ( "username".equals( newXFormsDBFileElementXFormsDBSecvarElement.getAttributeValue( "name" ) ) == true ) {
					usernameOfTheLoggedInXFormsDBUser					= XFormsDBFileTransformer.getUsernameOfTheLoggedInXFormsDBUser( session, encoding );
					newXFormsDBFileElementXFormsDBSecvarElement.removeChildren();
					newXFormsDBFileElementXFormsDBSecvarElement.appendChild( usernameOfTheLoggedInXFormsDBUser );
				}				
				// Handle the attribute name "roles" i.e. retrieve the value from the session
				else if ( "roles".equals( newXFormsDBFileElementXFormsDBSecvarElement.getAttributeValue( "name" ) ) == true ) {
					rolesOfTheLoggedInXFormsDBUser					= XFormsDBFileTransformer.getRolesOfTheLoggedInXFormsDBUser( session, encoding );
					newXFormsDBFileElementXFormsDBSecvarElement.removeChildren();
					newXFormsDBFileElementXFormsDBSecvarElement.appendChild( rolesOfTheLoggedInXFormsDBUser );
				}				
			}

			// Copy the <xformsdb:attachment> element and its children
			Element sentXFormsDBFileElementXFormsDBAttachmentElement	= sentXFormsDBFileElement.getFirstChildElement( "attachment", Constants.NAMESPACE_URI_XFORMSDB );
			if ( sentXFormsDBFileElementXFormsDBAttachmentElement != null ) {
				Element newXFormsDBFileElementXFormsDBAttachmentElement	= newXFormsDBFileElement.getFirstChildElement( "attachment", Constants.NAMESPACE_URI_XFORMSDB );
				if ( newXFormsDBFileElementXFormsDBAttachmentElement == null ) {
					// Does not have the <xformsdb:attachment> element
					newXFormsDBFileElement.appendChild( new Element( sentXFormsDBFileElementXFormsDBAttachmentElement ) );
				}
				else {
					// Already has the <xformsdb:attachment> element
					newXFormsDBFileElementXFormsDBAttachmentElement.getParent().replaceChild( newXFormsDBFileElementXFormsDBAttachmentElement, new Element( sentXFormsDBFileElementXFormsDBAttachmentElement ) );
				}
			}

			// Update the <xformsdb:file> element (secured/sent)
			document.setRootElement( new Element( newXFormsDBFileElement ) );						
			logger.log( Level.DEBUG, "The <xformsdb:file> element has been successfully transformed." );			
		} catch ( TransformerException tex ) {
			throw tex;
		} catch ( Exception ex ) {
			throw new TransformerException( ErrorConstants.ERROR_CODE_TRANSFORMATION_74, ErrorConstants.ERROR_MESSAGE_TRANSFORMATION_74, ex );
		}
	}	
}