package fi.tkk.tml.xformsdb.handler;

import java.io.ByteArrayOutputStream;

import javax.servlet.http.HttpSession;
import javax.xml.transform.Transformer;

import nu.xom.Attribute;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Nodes;
import nu.xom.Serializer;
import nu.xom.XPathContext;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fi.tkk.tml.xformsdb.core.Constants;
import fi.tkk.tml.xformsdb.dao.DAOFactory;
import fi.tkk.tml.xformsdb.dao.XFormsDBDAO;
import fi.tkk.tml.xformsdb.dao.exist.ExistDAOFactory;
import fi.tkk.tml.xformsdb.dao.xmldocument.XMLDocumentDAOFactory;
import fi.tkk.tml.xformsdb.error.DAOException;
import fi.tkk.tml.xformsdb.error.ErrorConstants;
import fi.tkk.tml.xformsdb.error.HandlerException;
import fi.tkk.tml.xformsdb.error.ManagerException;
import fi.tkk.tml.xformsdb.error.MergerException;
import fi.tkk.tml.xformsdb.manager.xformsdb.XFormsDBWidgetDataSourcesManager;
import fi.tkk.tml.xformsdb.manager.xformsdb.XFormsDBWidgetQueryManager;
import fi.tkk.tml.xformsdb.manager.xslt.TransformerManager;
import fi.tkk.tml.xformsdb.merger.XMLMerger;
import fi.tkk.tml.xformsdb.servlet.XFormsDBServlet;
import fi.tkk.tml.xformsdb.util.IOUtils;
import fi.tkk.tml.xformsdb.util.XMLUtils;
import fi.tkk.tml.xformsdb.xml.to.xformsdb.XFormsDBConfigTO;


/**
 * Handle the <xformsdb:widgetquery> UPDATE queries.
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on December 06, 2009
 */
public class XFormsDBWidgetQueryUpdateHandler extends ResponseHandler {
	

	
	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger	= Logger.getLogger( XFormsDBWidgetQueryUpdateHandler.class );

	
	// PUBLIC CONSTURCTORS
	public XFormsDBWidgetQueryUpdateHandler() {
		super();
		logger.log( Level.DEBUG, "Constructor has been called." );
	}
	
	
	// PRIVATE METHODS
	private XFormsDBDAO getXFormsDBDAO( XFormsDBServlet xformsdbServlet, HttpSession session, Document document, String encoding ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		XFormsDBDAO xformsdbDAO							= null;
		
		
		try {
			// Retrieve the name of the widget data source to be used
			String dataSrc								= document.getRootElement().getAttributeValue( "datasrc" );

			// Retrieve XFormsDB widget data sources from the session
			String xformsdbWidgetDataSources			= XFormsDBWidgetDataSourcesManager.getXFormsDBWidgetDataSources( session );

			// Build a XOM document
			Builder builder								= new Builder();
			Document xformsdbWidgetDataSourcesDocument	= builder.build( IOUtils.convert( xformsdbWidgetDataSources, encoding ) );
			
			// Retrieve the correct widget data source
			Nodes xformsdbWidgetDataSourceNodes			= xformsdbWidgetDataSourcesDocument.query( "xformsdb:widget-data-sources/xformsdb:widget-data-source[ @id = '" + dataSrc + "' ]", new XPathContext( Constants.NAMESPACE_PREFIX_XFORMSDB, Constants.NAMESPACE_URI_XFORMSDB ) );

			if ( xformsdbWidgetDataSourceNodes == null || ( xformsdbWidgetDataSourceNodes != null && xformsdbWidgetDataSourceNodes.size() == 0 ) ) {
				throw new DAOException( ErrorConstants.ERROR_CODE_DAO_135, ErrorConstants.ERROR_MESSAGE_DAO_135 );
			}

			// Retrieve the correct widget data source
			Node xformsdbWidgetDataSourceNode			= xformsdbWidgetDataSourceNodes.get( 0 );
			
			// Retrieve values for the DAO data source
			Nodes typeNodes								= xformsdbWidgetDataSourceNode.query( "xformsdb:type/text()", new XPathContext( "xformsdb", "http://www.tml.tkk.fi/2007/xformsdb" ) );
			String type									= "";
			if ( typeNodes.size() > 0 ) {
				type									= typeNodes.get( 0 ).toXML();				
			}
			Nodes uriNodes								= xformsdbWidgetDataSourceNode.query( "xformsdb:uri/text()", new XPathContext( "xformsdb", "http://www.tml.tkk.fi/2007/xformsdb" ) );
			String uri									= "";
			if ( uriNodes.size() > 0 ) {
				uri										= uriNodes.get( 0 ).toXML();
			}
			Nodes usernameNodes							= xformsdbWidgetDataSourceNode.query( "xformsdb:username/text()", new XPathContext( "xformsdb", "http://www.tml.tkk.fi/2007/xformsdb" ) );
			String username								= "";
			if ( usernameNodes.size() > 0 ) {
				username								= usernameNodes.get( 0 ).toXML();
			}
			Nodes passwordNodes							= xformsdbWidgetDataSourceNode.query( "xformsdb:password/text()", new XPathContext( "xformsdb", "http://www.tml.tkk.fi/2007/xformsdb" ) );
			String password								= "";
			if ( passwordNodes.size() > 0 ) {
				password								= passwordNodes.get( 0 ).toXML();				
			}
			Nodes collectionNodes						= xformsdbWidgetDataSourceNode.query( "xformsdb:collection/text()", new XPathContext( "xformsdb", "http://www.tml.tkk.fi/2007/xformsdb" ) );
			String collection							= "";
			if ( collectionNodes.size() > 0 ) {
				collection								= collectionNodes.get( 0 ).toXML();				
			}
			
			// Retrieve the XFormsDB DAO of the appropriate DAO Factory implementation
			DAOFactory daoFactory						= null;
			// Set the DAO Factory
			int dataSourceType							= Integer.parseInt( type );
			switch ( dataSourceType ) {
				case DAOFactory.XML_DOCUMENT:
					daoFactory							= ( XMLDocumentDAOFactory ) DAOFactory.getDAOFactory( DAOFactory.XML_DOCUMENT );
					break;
				case DAOFactory.EXIST:
					daoFactory							= ( ExistDAOFactory ) DAOFactory.getDAOFactory( DAOFactory.EXIST );
					break;
				default:
					throw new DAOException( "Illegal widget data source type value '" + dataSourceType + "' in the <xformsdb:widget-data-source" + ( ( dataSrc != null ) ? " id=\"" + dataSrc + "\">" : ">" ) + " element." );
			}

			// Set DAO data Source
			daoFactory.getDAODataSource().setId( dataSrc );
			// Type has already been set
			daoFactory.getDAODataSource().setUri( ( ( "".equals( uri ) == false ) ? uri : xformsdbServlet.getServletContext().getRealPath( "/" ) ) );
			daoFactory.getDAODataSource().setUsername( username );
			daoFactory.getDAODataSource().setPassword( password );
			daoFactory.getDAODataSource().setCollection( collection );
			
			xformsdbDAO									= daoFactory.getXFormsDBDAO();
			logger.log( Level.DEBUG, "The XFormsDB DAO of the appropriate DAO factory implementation has been successfully retrieved." );
		} catch ( ManagerException mex ) {
			throw new HandlerException( mex.getCode(), mex.getMessage(), mex );
		} catch ( DAOException daoex ) {
			throw new HandlerException( daoex.getCode(), daoex.getMessage(), daoex );
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBWIDGETQUERYUPDATEHANDLER_1, ErrorConstants.ERROR_MESSAGE_XFORMSDBWIDGETQUERYUPDATEHANDLER_1, ex  );
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
			throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBWIDGETQUERYUPDATEHANDLER_3, ErrorConstants.ERROR_MESSAGE_XFORMSDBWIDGETQUERYUPDATEHANDLER_3, ex );
		}
		
		
		return transformer;
	}

	
	private String getSelect4UpdateWidgetQueryResult( HttpSession session, Document document ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		String select4UpdateWidgetQueryResult	= null;
		
		
		try {
			// Retrieve the select4update (stored) widget query result
			select4UpdateWidgetQueryResult		= XFormsDBWidgetQueryManager.getSelect4UpdateXFormsDBWidgetQueryResult( session, document.getRootElement().getAttributeValue( "id" ) );
			logger.log( Level.DEBUG, "The select4update (stored) widget query result has been retrieved." );
		} catch ( ManagerException mex ) {
			throw new HandlerException( mex.getCode(), mex.getMessage(), mex );
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBWIDGETQUERYUPDATEHANDLER_2, ErrorConstants.ERROR_MESSAGE_XFORMSDBWIDGETQUERYUPDATEHANDLER_2, ex  );
		}
		
		
		return select4UpdateWidgetQueryResult;
	}
	
	
	private void updateSelect4UpdateWidgetQueryResult( HttpSession session, Document document, String tm ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		
		
		try {
			// Update/replace the select4update (stored) widget query result
			XFormsDBWidgetQueryManager.addSelect4UpdateXFormsDBWidgetQueryResult( session, document.getRootElement().getAttributeValue( "id" ), tm );
			logger.log( Level.DEBUG, "The select4update (stored) widget query result has been successfully updated/replaced." );
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBWIDGETQUERYUPDATEHANDLER_9, ErrorConstants.ERROR_MESSAGE_XFORMSDBWIDGETQUERYUPDATEHANDLER_9, ex  );
		}
	}

	
	// PUBLIC METHODS
	public String handle( XFormsDBServlet xformsdbServlet, HttpSession session, Document document ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		XFormsDBConfigTO xformsdbConfigTO					= null;
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
			xformsdbConfigTO								= xformsdbServlet.getXFormsDBConfigTO();
			
			// Retrieve the XFormsDB DAO of the appropriate DAO Factory implementation
			xformsdbDAO										= this.getXFormsDBDAO( xformsdbServlet, session, document, xformsdbConfigTO.getEncoding() );

			// Retrieve the indent transformer
			indentTransformer								= XFormsDBWidgetQueryUpdateHandler.getTransformer( xformsdbServlet.getIndentFilePath() );

			// Retrieve the XML document (Version T0, select4update (stored) widget query result)
			t0												= this.getSelect4UpdateWidgetQueryResult( session, document );
			// Reindent the XML document (Version T0, select4update (stored) widget query result) in order to get rid of extra new lines made by xforms:delete actions
			t0												= XMLUtils.indent( indentTransformer, t0, xformsdbConfigTO.getEncoding() );
			logger.log( Level.DEBUG, "The XML document (T0) has been successfully retrieved." );
			logger.log( Level.DEBUG, "T0:" + Constants.LINE_SEPARATOR + t0 );
			
			
			// Retrieve the XML document (Version T1, sent/updated version as attachment)
			// Retrieve the <xformsdb:attachment> element
			xformsdbAttachmentElement						= document.getRootElement().getFirstChildElement( "attachment", Constants.NAMESPACE_URI_XFORMSDB );

			// Check the contents of the <xformsdb:attachment> element
			if ( xformsdbAttachmentElement.getChildElements().size() == 0 ) {
				throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBWIDGETQUERYUPDATEHANDLER_13, ErrorConstants.ERROR_MESSAGE_XFORMSDBWIDGETQUERYUPDATEHANDLER_13 );
			}
			else {
				// Retrieve the contents of the <xformsdb:attachment> element
				xformsdbAttachmentElementContentsDocument	= new Document( new Element( xformsdbAttachmentElement.getChildElements().get( 0 ) ) );				
			}
			
			// Serialize the document
			outputStreamAttachment							= new ByteArrayOutputStream();
			serializer										= new Serializer( outputStreamAttachment, xformsdbConfigTO.getEncoding() );
			serializer.write( xformsdbAttachmentElementContentsDocument );			
			
			// Change the encoding
			t1												= outputStreamAttachment.toString( xformsdbConfigTO.getEncoding() );
			// Reindent the XML document (Version T1, sent/updated version as attachment) in order to get rid of extra new lines made by xforms:delete actions
			t1												= XMLUtils.indent( indentTransformer, t1, xformsdbConfigTO.getEncoding() );
			logger.log( Level.DEBUG, "The XML document (T1) has been successfully retrieved." );
			logger.log( Level.DEBUG, "T1:" + Constants.LINE_SEPARATOR + t1 );

			// Copy and update the query transfer object (update --> select4update)
			select4updateDocument							= new Document( document );
			select4updateDocument.getRootElement().addAttribute( new Attribute( "type", Constants.EXPRESSION_TYPE_SELECT4UPDATE ) );

			
			// Retrieve the XML document (Version T2, current version)
			t2												= xformsdbDAO.select( select4updateDocument, xformsdbConfigTO.getEncoding() );
			// Reindent the XML document  (Version T2, current version) in order to get rid of extra new lines made by xforms:delete actions
			t2												= XMLUtils.indent( indentTransformer, t2, xformsdbConfigTO.getEncoding() );
			logger.log( Level.DEBUG, "The XML document (T2) has been successfully retrieved." );
			logger.log( Level.DEBUG, "T2:" + Constants.LINE_SEPARATOR + t2 );

			
			// Merge the XML documents (T0, T1, and T2)
			tm												= XMLMerger.merge( t0, t1, t2, xformsdbConfigTO.getTDMConflictLevel(), xformsdbConfigTO.getEncoding() );
			logger.log( Level.DEBUG, "The XML documents (T0, T1, and T2) have been successfully merged." );
			logger.log( Level.DEBUG, "Tm:" + Constants.LINE_SEPARATOR + tm );

			// Update the <xformsdb:widgetquery> element
			// Create a temporary copy of the <xformsdb:login> element (secured/sent) and its children
			builder											= new Builder();
			tmElement										= new Element( builder.build( IOUtils.convert( tm, xformsdbConfigTO.getEncoding() ) ).getRootElement() );
			
			// Copy the XML Namespace declarations for the <xformsdb:widgetquery> element
			String tmElementNamespacePrefix					= null;
			String tmElementNamespaceURI					= null;
			for ( int i = 0; i < tmElement.getNamespaceDeclarationCount(); i++ ) {
				tmElementNamespacePrefix					= tmElement.getNamespacePrefix( i );
				tmElementNamespaceURI						= tmElement.getNamespaceURI( tmElementNamespacePrefix );
				document.getRootElement().addNamespaceDeclaration( tmElementNamespacePrefix, tmElementNamespaceURI );
			}
			logger.log( Level.DEBUG, "The <xformsdb:widgetquery> element has been successfully updated." );
			
			// Filter the XML document (Tm)
			tmFiltered										= XMLUtils.filterXMLDeclaration( tm );
			logger.log( Level.DEBUG, "The XML document (Tm) has been successfully filtered." );
			logger.log( Level.DEBUG, "Tm filtered:" + Constants.LINE_SEPARATOR + tmFiltered );

			// Update the XML document (Tm) into the database
			xmlString										= xformsdbDAO.update( document, tmFiltered, xformsdbConfigTO.getEncoding() );
			logger.log( Level.DEBUG, "The XML (Tm) has been successfully updated." );
			logger.log( Level.DEBUG, "Updated XML String:" + Constants.LINE_SEPARATOR + xmlString );

			// Update/replace the XML document (Version T0, select4update (stored) wdget query result) with the merged XML document (Tm)
			this.updateSelect4UpdateWidgetQueryResult( session, document, xmlString );
			logger.log( Level.DEBUG, "The XML document (T0) has been successfully updated/replaced with the merged XML document (Tm)." );

			
			logger.log( Level.DEBUG, "The UPDATE action of the XFormsDB widget query request has been successfully handled." );
		} catch ( MergerException mex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBWIDGETQUERYUPDATEHANDLER_11, ErrorConstants.ERROR_MESSAGE_XFORMSDBWIDGETQUERYUPDATEHANDLER_11_1 + document.getRootElement().getAttributeValue( "id" ) + ErrorConstants.ERROR_MESSAGE_XFORMSDBWIDGETQUERYUPDATEHANDLER_11_2, mex );
		} catch ( DAOException daoex ) {
			throw new HandlerException( daoex.getCode(), daoex.getMessage(), daoex );
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBWIDGETQUERYUPDATEHANDLER_10, ErrorConstants.ERROR_MESSAGE_XFORMSDBWIDGETQUERYUPDATEHANDLER_10, ex );
		}
		
		
		return xmlString;
	}	
}