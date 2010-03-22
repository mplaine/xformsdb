package fi.tkk.tml.xformsdb.handler;

import java.io.File;
import java.util.Map;

import nu.xom.Attribute;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fi.tkk.tml.xformsdb.core.Constants;
import fi.tkk.tml.xformsdb.dao.DAOFactory;
import fi.tkk.tml.xformsdb.dao.XFormsDBDAO;
import fi.tkk.tml.xformsdb.error.DAOException;
import fi.tkk.tml.xformsdb.error.ErrorConstants;
import fi.tkk.tml.xformsdb.error.HandlerException;
import fi.tkk.tml.xformsdb.servlet.XFormsDBServlet;
import fi.tkk.tml.xformsdb.util.IOUtils;
import fi.tkk.tml.xformsdb.xml.to.xformsdb.XFormsDBConfigTO;


/**
 * Handle the <xformsdb:file> DELETE requests.
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on October 15, 2009
 */
public class XFormsDBFileDeleteHandler extends ResponseHandler {
	
	
	
	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger			= Logger.getLogger( XFormsDBFileDeleteHandler.class );

	
	// PUBLIC CONSTURCTORS
	public XFormsDBFileDeleteHandler() {
		logger.log( Level.DEBUG, "Constructor has been called." );
	}
	
	
	// PRIVATE METHODS	
	private XFormsDBDAO getXFormsDBDAO( XFormsDBServlet xformsdbServlet, Document document ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		Map<String, DAOFactory> daoFactories	= null;
		XFormsDBDAO xformsdbDAO					= null;
		
		
		try {
			// Retrieve the DAO Factories from the servlet context
			daoFactories						= xformsdbServlet.getDAOFactories();
			
			// Retrieve the files metadata DAO Factory implementation
			DAOFactory daoFactory				= daoFactories.get( Constants.XFORMSDB_CONFIG_DEFAULT_FILES_METADATA_ID );
			
			if ( daoFactory == null) {
				throw new DAOException( ErrorConstants.ERROR_CODE_DAO_101, ErrorConstants.ERROR_MESSAGE_DAO_101 );
			}
			
			xformsdbDAO							= daoFactory.getXFormsDBDAO();
			logger.log( Level.DEBUG, "The XFormsDB DAO of the files metadata DAO factory implementation has been successfully retrieved." );
		} catch ( DAOException daoex ) {
			throw new HandlerException( daoex.getCode(), daoex.getMessage(), daoex );
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBFILEDELETEHANDLER_1, ErrorConstants.ERROR_MESSAGE_XFORMSDBFILEDELETEHANDLER_1, ex  );
		}
		
		
		return xformsdbDAO;
	}

	
	private void updateDocument( XFormsDBConfigTO xformsdbConfigTO, Document document ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		String deletedFileIDsStr						= null;
		
		
		try {
			// Retrieve the <xformsdb:attachment> element
			Element xformsdbAttachmentElement			= document.getRootElement().getFirstChildElement( "attachment", Constants.NAMESPACE_URI_XFORMSDB );
			// Retrieve the <xformsdb:delete> element
			Element xformsdbDeleteElement				= xformsdbAttachmentElement.getFirstChildElement( "delete", Constants.NAMESPACE_URI_XFORMSDB );
			
			// Validate the <xformsdb:delete> element
			if ( xformsdbDeleteElement == null ) {
				throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBFILEDELETEHANDLER_5, ErrorConstants.ERROR_MESSAGE_XFORMSDBFILEDELETEHANDLER_5 );
			}
			else {
				// Validate child elements of the <xformsdb:delete> element
				Elements xformsdbFileElements			= xformsdbDeleteElement.getChildElements( "file", Constants.NAMESPACE_URI_XFORMSDB );
				if ( xformsdbFileElements.size() != xformsdbDeleteElement.getChildElements().size() ) {
					throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBFILEDELETEHANDLER_6, ErrorConstants.ERROR_MESSAGE_XFORMSDBFILEDELETEHANDLER_6 );					
				}
				else if ( xformsdbFileElements.size() == xformsdbDeleteElement.getChildElements().size() && xformsdbFileElements.size() > 0 ) {
					Element xformsdbFileElement			= null;
					Attribute idAttribute				= null;
					for ( int i = 0; i < xformsdbFileElements.size(); i++ ) {
						xformsdbFileElement				= xformsdbFileElements.get( i );
						idAttribute						= xformsdbFileElement.getAttribute( "id" );
						if ( idAttribute == null ) {
							throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBFILEDELETEHANDLER_7, ErrorConstants.ERROR_MESSAGE_XFORMSDBFILEDELETEHANDLER_7 );							
						}
					}
				}
			}
			
			String deletedAttributeValue				= xformsdbDeleteElement.getAttributeValue( "deleted" );
			if ( deletedAttributeValue == null ) {
				deletedFileIDsStr						= "";
			}
			else {
				deletedFileIDsStr						= deletedAttributeValue.trim();
			}
						
			String namespacePrefixXFormsDBWithColon		= Constants.NAMESPACE_PREFIX_XFORMSDB + ( ( "".equals( Constants.NAMESPACE_PREFIX_XFORMSDB ) == true ) ? "" : ":" );
			// Create the <xformsdb:var> element (xformsdb:delete)
			Element xformsdbVar1						= new Element( namespacePrefixXFormsDBWithColon + "var", Constants.NAMESPACE_URI_XFORMSDB );
			xformsdbVar1.addAttribute( new Attribute( "name", "xformsdbdelete" ) );
			xformsdbVar1.appendChild( new Element( xformsdbDeleteElement ) );
			document.getRootElement().appendChild( xformsdbVar1 );

			// Create the <xformsdb:var> element (deleted file IDs)
			Element xformsdbVar2						= new Element( namespacePrefixXFormsDBWithColon + "var", Constants.NAMESPACE_URI_XFORMSDB );
			xformsdbVar2.addAttribute( new Attribute( "name", "deletedfileids" ) );
			xformsdbVar2.appendChild( deletedFileIDsStr.toString() );
			document.getRootElement().appendChild( xformsdbVar2 );

			
			logger.log( Level.DEBUG, "The XOM document has been successfully updated." );
		} catch ( HandlerException hex ) {
			throw hex;
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBFILEDELETEHANDLER_3, ErrorConstants.ERROR_MESSAGE_XFORMSDBFILEDELETEHANDLER_3, ex  );
		}
	}
	
	
	private void deleteFiles( XFormsDBConfigTO xformsdbConfigTO, String xformsdbDelete ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		Builder builder									= null;
		Document document								= null;
		int filesLength									= 0;
		String fileName									= null;
		String fileId									= null;
		File deleteFile									= null;
		String deleteFilePath							= null;


		
		try {
			// Parse the extracted XML document in order to build the XOM document object			
			builder										= new Builder();
			document									= builder.build( IOUtils.convert( xformsdbDelete, xformsdbConfigTO.getEncoding() ) );
			logger.log( Level.DEBUG, "The XOM document object (xformsdb:delete) has been successfully built." );
						
			// Iterate over files to be deleted
			Element xformsdbDeleteElement				= document.getRootElement();
			Elements xformsdbFileElements				= xformsdbDeleteElement.getChildElements( "file", Constants.NAMESPACE_URI_XFORMSDB );
			filesLength									= xformsdbFileElements.size();
			Element xformsdbFileElement					= null;
			for ( int i = 0; i < filesLength; i++ ) {
				xformsdbFileElement						= xformsdbFileElements.get( i );
				
				// Retrieve file information
				fileName								= xformsdbFileElement.getAttributeValue( "filename" );
				fileId									= xformsdbFileElement.getAttributeValue( "id" );
				
				// Generate file path
				deleteFilePath							= xformsdbConfigTO.getFilesFolder() + File.separator + fileId + "_" + fileName;

				// Delete files
				if ( deleteFilePath != null ) {
					deleteFile							= new File( deleteFilePath );
					deleteFile.delete();
				}
			}
			
			
			logger.log( Level.DEBUG, "The files have been successfully deleted from XFormsDB." );
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBFILEDELETEHANDLER_4, ErrorConstants.ERROR_MESSAGE_XFORMSDBFILEDELETEHANDLER_4, ex  );
		}
	}
	
	
	// PUBLIC METHODS
	public String handle( XFormsDBServlet xformsdbServlet, String actionURL, Document document ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		XFormsDBConfigTO xformsdbConfigTO	= null;
		String xformsdbDelete				= null;
		XFormsDBDAO xformsdbDAO				= null;
		
		
		try {
			// Retrieve the XFormsDB configuration file
			xformsdbConfigTO				= xformsdbServlet.getXFormsDBConfigTO();

			// Retrieve the XFormsDB DAO of the appropriate DAO Factory implementation
			xformsdbDAO						= this.getXFormsDBDAO( xformsdbServlet, document );
			
			// Update the XOM document (<xformsdb:file> element)
			this.updateDocument( xformsdbConfigTO, document );
			
			// Delete files and retrieve the XML document
			xformsdbDelete					= xformsdbDAO.deleteFiles( document, xformsdbConfigTO.getEncoding() );
			
			// Delete the files from XFormsDB
			this.deleteFiles( xformsdbConfigTO, xformsdbDelete );
			logger.log( Level.DEBUG, "The DELETE action of the XFormsDB file request has been successfully handled." );
		} catch ( DAOException daoex ) {
			throw new HandlerException( daoex.getCode(), daoex.getMessage(), daoex );
		} catch ( HandlerException hex ) {
			throw hex;
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBFILEDELETEHANDLER_2, ErrorConstants.ERROR_MESSAGE_XFORMSDBFILEDELETEHANDLER_2, ex  );
		}
		
		
		return xformsdbDelete;
	}
}