package fi.tkk.tml.xformsdb.handler;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.xml.transform.Transformer;

import nu.xom.Attribute;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Serializer;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fi.tkk.tml.xformsdb.core.Constants;
import fi.tkk.tml.xformsdb.dao.DAOFactory;
import fi.tkk.tml.xformsdb.dao.XFormsDBDAO;
import fi.tkk.tml.xformsdb.error.DAOException;
import fi.tkk.tml.xformsdb.error.ErrorConstants;
import fi.tkk.tml.xformsdb.error.HandlerException;
import fi.tkk.tml.xformsdb.error.ManagerException;
import fi.tkk.tml.xformsdb.error.MergerException;
import fi.tkk.tml.xformsdb.manager.xformsdb.XFormsDBQueryManager;
import fi.tkk.tml.xformsdb.manager.xslt.TransformerManager;
import fi.tkk.tml.xformsdb.merger.XMLMerger;
import fi.tkk.tml.xformsdb.servlet.XFormsDBServlet;
import fi.tkk.tml.xformsdb.util.IOUtils;
import fi.tkk.tml.xformsdb.util.XMLUtils;
import fi.tkk.tml.xformsdb.xml.to.xformsdb.XFormsDBConfigTO;


/**
 * Handle the <xformsdb:query> UPDATE queries.
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on December 06, 2009
 */
public class XFormsDBQueryUpdateHandler extends ResponseHandler {
	

	
	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger	= Logger.getLogger( XFormsDBQueryUpdateHandler.class );

	
	// PUBLIC CONSTURCTORS
	public XFormsDBQueryUpdateHandler() {
		super();
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
			
			// Retrieve the XFormsDB DAO of the appropriate DAO Factory implementation
			DAOFactory daoFactory				= null;
			String dataSrc						= document.getRootElement().getAttributeValue( "datasrc" );
			
			if ( dataSrc != null ) {
				daoFactory						= daoFactories.get( dataSrc );
			}
			// Use the default DAO Factory implementation
			else {
				daoFactory						= daoFactories.get( Constants.XFORMSDB_CONFIG_DEFAULT_DATA_SOURCE_ID );
			}

			if ( daoFactory == null) {
				throw new DAOException( ErrorConstants.ERROR_CODE_DAO_61, ErrorConstants.ERROR_MESSAGE_DAO_61 );
			}

			xformsdbDAO							= daoFactory.getXFormsDBDAO();
			logger.log( Level.DEBUG, "The XFormsDB DAO of the appropriate DAO factory implementation has been successfully retrieved." );
		} catch ( DAOException daoex ) {
			throw new HandlerException( daoex.getCode(), daoex.getMessage(), daoex );
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBQUERYUPDATEHANDLER_1, ErrorConstants.ERROR_MESSAGE_XFORMSDBQUERYUPDATEHANDLER_1, ex  );
		}
		
		
		return xformsdbDAO;
	}


	private static Transformer getTransformer( String xslFilePath ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		Transformer transformer	= null;

		
		try {
			// Retrieve the transformer
			transformer			= TransformerManager.getTransformer( xslFilePath );
			logger.log( Level.DEBUG, "The transformer has been successfully retrieved." );
		} catch ( ManagerException mex ) {
			throw new HandlerException( mex.getCode(), mex.getMessage(), mex );
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBQUERYUPDATEHANDLER_3, ErrorConstants.ERROR_MESSAGE_XFORMSDBQUERYUPDATEHANDLER_3, ex );
		}
		
		
		return transformer;
	}

	
	private String getSelect4UpdateQueryResult( HttpSession session, Document document ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		String select4UpdateQueryResult	= null;
		
		
		try {
			// Retrieve the select4update (stored) query result
			select4UpdateQueryResult	= XFormsDBQueryManager.getSelect4UpdateXFormsDBQueryResult( session, document.getRootElement().getAttributeValue( "id" ) );
			logger.log( Level.DEBUG, "The select4update (stored) query result has been retrieved." );
		} catch ( ManagerException mex ) {
			throw new HandlerException( mex.getCode(), mex.getMessage(), mex );
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBQUERYUPDATEHANDLER_2, ErrorConstants.ERROR_MESSAGE_XFORMSDBQUERYUPDATEHANDLER_2, ex  );
		}
		
		
		return select4UpdateQueryResult;
	}

	
	private void updateSelect4UpdateQueryResult( HttpSession session, Document document, String tm ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		
		
		try {
			// Update/replace the select4update (stored) query result
			XFormsDBQueryManager.addSelect4UpdateXFormsDBQueryResult( session, document.getRootElement().getAttributeValue( "id" ), tm );
			logger.log( Level.DEBUG, "The select4update (stored) query result has been successfully updated/replaced." );
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBQUERYUPDATEHANDLER_9, ErrorConstants.ERROR_MESSAGE_XFORMSDBQUERYUPDATEHANDLER_9, ex  );
		}
	}

	
	// PUBLIC METHODS
	public String handle( XFormsDBServlet xformsdbServlet, HttpSession session, Document document ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		XFormsDBConfigTO xformsDBConfigTO					= null;
		XFormsDBDAO xformsdbDAO								= null;
		Transformer indentTransformer						= null;
		Element xformsdbAttachmentElement					= null;
		Document xformsdbAttachmentElementContentsDocument	= null;
		ByteArrayOutputStream outputStreamAttachment		= null;
		Serializer serializer								= null;
		Document select4updateDocument						= null;
		Builder builder										= null;
		Element tmElement									= null;
		String t0											= null;
		String t1											= null;
		String t2											= null;
		String tm											= null;
		String tmFiltered									= null;
		String xmlString									= null;
		
		
		try {
			// Retrieve the XFormsDB configuration file
			xformsDBConfigTO								= xformsdbServlet.getXFormsDBConfigTO();
			
			// Retrieve the XFormsDB DAO of the appropriate DAO Factory implementation
			xformsdbDAO										= this.getXFormsDBDAO( xformsdbServlet, document );

			// Retrieve the indent transformer
			indentTransformer								= XFormsDBQueryUpdateHandler.getTransformer( xformsdbServlet.getIndentFilePath() );

			// Retrieve the XML document (Version T0, select4update (stored) query result)
			t0												= this.getSelect4UpdateQueryResult( session, document );
			// Reindent the XML document (Version T0, select4update (stored) query result) in order to get rid of extra new lines made by xforms:delete actions
			t0												= XMLUtils.indent( indentTransformer, t0, xformsDBConfigTO.getEncoding() );
			logger.log( Level.DEBUG, "The XML document (T0) has been successfully retrieved." );
			logger.log( Level.DEBUG, "T0:" + Constants.LINE_SEPARATOR + t0 );
			
			
			// Retrieve the XML document (Version T1, sent/updated version as attachment)
			// Retrieve the <xformsdb:attachment> element
			xformsdbAttachmentElement						= document.getRootElement().getFirstChildElement( "attachment", Constants.NAMESPACE_URI_XFORMSDB );

			// Check the contents of the <xformsdb:attachment> element
			if ( xformsdbAttachmentElement.getChildElements().size() == 0 ) {
				throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBQUERYUPDATEHANDLER_13, ErrorConstants.ERROR_MESSAGE_XFORMSDBQUERYUPDATEHANDLER_13 );
			}
			else {
				// Retrieve the contents of the <xformsdb:attachment> element
				xformsdbAttachmentElementContentsDocument	= new Document( new Element( xformsdbAttachmentElement.getChildElements().get( 0 ) ) );				
			}
			
			// Serialize the document
			outputStreamAttachment							= new ByteArrayOutputStream();
			serializer										= new Serializer( outputStreamAttachment, xformsDBConfigTO.getEncoding() );
			serializer.write( xformsdbAttachmentElementContentsDocument );			
			
			// Change the encoding
			t1												= outputStreamAttachment.toString( xformsDBConfigTO.getEncoding() );
			// Reindent the XML document (Version T1, sent/updated version as attachment) in order to get rid of extra new lines made by xforms:delete actions
			t1												= XMLUtils.indent( indentTransformer, t1, xformsDBConfigTO.getEncoding() );
			logger.log( Level.DEBUG, "The XML document (T1) has been successfully retrieved." );
			logger.log( Level.DEBUG, "T1:" + Constants.LINE_SEPARATOR + t1 );

			// Copy and update the query transfer object (update --> select4update)
			select4updateDocument							= new Document( document );
			select4updateDocument.getRootElement().addAttribute( new Attribute( "type", Constants.EXPRESSION_TYPE_SELECT4UPDATE ) );

			
			// Retrieve the XML document (Version T2, current version)
			t2												= xformsdbDAO.select( select4updateDocument, xformsDBConfigTO.getEncoding() );
			// Reindent the XML document  (Version T2, current version) in order to get rid of extra new lines made by xforms:delete actions
			t2												= XMLUtils.indent( indentTransformer, t2, xformsDBConfigTO.getEncoding() );
			logger.log( Level.DEBUG, "The XML document (T2) has been successfully retrieved." );
			logger.log( Level.DEBUG, "T2:" + Constants.LINE_SEPARATOR + t2 );

			
			// Merge the XML documents (T0, T1, and T2)
			tm												= XMLMerger.merge( t0, t1, t2, xformsDBConfigTO.getTDMConflictLevel(), xformsDBConfigTO.getEncoding() );
			logger.log( Level.DEBUG, "The XML documents (T0, T1, and T2) have been successfully merged." );
			logger.log( Level.DEBUG, "Tm:" + Constants.LINE_SEPARATOR + tm );

			// Update the <xformsdb:query> element
			// Create a temporary copy of the <xformsdb:login> element (secured/sent) and its children
			builder											= new Builder();
			tmElement										= new Element( builder.build( IOUtils.convert( tm, xformsDBConfigTO.getEncoding() ) ).getRootElement() );
			
			// Copy the XML Namespace declarations for the <xformsdb:query> element
			String tmElementNamespacePrefix					= null;
			String tmElementNamespaceURI					= null;
			for ( int i = 0; i < tmElement.getNamespaceDeclarationCount(); i++ ) {
				tmElementNamespacePrefix					= tmElement.getNamespacePrefix( i );
				tmElementNamespaceURI						= tmElement.getNamespaceURI( tmElementNamespacePrefix );
				document.getRootElement().addNamespaceDeclaration( tmElementNamespacePrefix, tmElementNamespaceURI );
			}
			logger.log( Level.DEBUG, "The <xformsdb:query> element has been successfully updated." );
			
			// Filter the XML document (Tm)
			tmFiltered										= XMLUtils.filterXMLDeclaration( tm );
			logger.log( Level.DEBUG, "The XML document (Tm) has been successfully filtered." );
			logger.log( Level.DEBUG, "Tm filtered:" + Constants.LINE_SEPARATOR + tmFiltered );

			// Update the XML document (Tm) into the database
			xmlString										= xformsdbDAO.update( document, tmFiltered, xformsDBConfigTO.getEncoding() );
			logger.log( Level.DEBUG, "The XML (Tm) has been successfully updated." );
			logger.log( Level.DEBUG, "Updated XML String:" + Constants.LINE_SEPARATOR + xmlString );

			// Update/replace the XML document (Version T0, select4update (stored) query result) with the merged XML document (Tm)
			this.updateSelect4UpdateQueryResult( session, document, xmlString );
			logger.log( Level.DEBUG, "The XML document (T0) has been successfully updated/replaced with the merged XML document (Tm)." );

			
			logger.log( Level.DEBUG, "The UPDATE action of the XFormsDB query request has been successfully handled." );
		} catch ( MergerException mex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBQUERYUPDATEHANDLER_11, ErrorConstants.ERROR_MESSAGE_XFORMSDBQUERYUPDATEHANDLER_11_1 + document.getRootElement().getAttributeValue( "id" ) + ErrorConstants.ERROR_MESSAGE_XFORMSDBQUERYUPDATEHANDLER_11_2, mex );
		} catch ( DAOException daoex ) {
			throw new HandlerException( daoex.getCode(), daoex.getMessage(), daoex );
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBQUERYUPDATEHANDLER_10, ErrorConstants.ERROR_MESSAGE_XFORMSDBQUERYUPDATEHANDLER_10, ex );
		}
		
		
		return xmlString;
	}	
}