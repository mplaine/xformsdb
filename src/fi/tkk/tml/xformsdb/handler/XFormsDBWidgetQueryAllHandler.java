package fi.tkk.tml.xformsdb.handler;


import javax.servlet.http.HttpSession;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;
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
import fi.tkk.tml.xformsdb.manager.xformsdb.XFormsDBWidgetDataSourcesManager;
import fi.tkk.tml.xformsdb.servlet.XFormsDBServlet;
import fi.tkk.tml.xformsdb.util.IOUtils;
import fi.tkk.tml.xformsdb.xml.to.xformsdb.XFormsDBConfigTO;


/**
 * Handle the <xformsdb:widgetquery> ALL queries.
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on October 23, 2009
 */
public class XFormsDBWidgetQueryAllHandler extends ResponseHandler {
	
	
	
	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger					= Logger.getLogger( XFormsDBWidgetQueryAllHandler.class );

	
	// PUBLIC CONSTURCTORS
	public XFormsDBWidgetQueryAllHandler() {
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
				throw new DAOException( ErrorConstants.ERROR_CODE_DAO_130, ErrorConstants.ERROR_MESSAGE_DAO_130 );
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
			throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBWIDGETQUERYALLHANDLER_1, ErrorConstants.ERROR_MESSAGE_XFORMSDBWIDGETQUERYALLHANDLER_1, ex  );
		}
		
		
		return xformsdbDAO;
	}

	
	// PUBLIC METHODS
	public String handle( XFormsDBServlet xformsdbServlet, HttpSession session, Document document ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		XFormsDBConfigTO xformsDBConfigTO	= null;
		String xmlString					= null;
		XFormsDBDAO xformsdbDAO				= null;
		
		
		try {
			// Retrieve the XFormsDB configuration file
			xformsDBConfigTO				= xformsdbServlet.getXFormsDBConfigTO();
			
			// Retrieve the XFormsDB DAO of the appropriate DAO Factory implementation
			xformsdbDAO						= this.getXFormsDBDAO( xformsdbServlet, session, document, xformsDBConfigTO.getEncoding() );

			// Retrieve the XML document
			xmlString						= xformsdbDAO.all( document, xformsDBConfigTO.getEncoding() );
			logger.log( Level.DEBUG, "The ALL action of the XFormsDB widget query request has been successfully handled." );
		} catch ( DAOException daoex ) {
			throw new HandlerException( daoex.getCode(), daoex.getMessage(), daoex );
		} catch ( HandlerException hex ) {
			throw hex;
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBWIDGETQUERYALLHANDLER_2, ErrorConstants.ERROR_MESSAGE_XFORMSDBWIDGETQUERYALLHANDLER_2, ex );
		}
		
		
		return xmlString;
	}	
}