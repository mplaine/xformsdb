package fi.tkk.tml.xformsdb.handler;

import java.util.Map;

import javax.servlet.http.HttpSession;

import nu.xom.Document;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fi.tkk.tml.xformsdb.core.Constants;
import fi.tkk.tml.xformsdb.dao.DAOFactory;
import fi.tkk.tml.xformsdb.dao.XFormsDBDAO;
import fi.tkk.tml.xformsdb.error.DAOException;
import fi.tkk.tml.xformsdb.error.ErrorConstants;
import fi.tkk.tml.xformsdb.error.HandlerException;
import fi.tkk.tml.xformsdb.error.ManagerException;
import fi.tkk.tml.xformsdb.manager.xformsdb.XFormsDBQueryManager;
import fi.tkk.tml.xformsdb.servlet.XFormsDBServlet;
import fi.tkk.tml.xformsdb.xml.to.xformsdb.XFormsDBConfigTO;


/**
 * Handle the <xformsdb:query> SELECT4UPDATE queries.
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on October 15, 2009
 */
public class XFormsDBQuerySelect4UpdateHandler extends ResponseHandler {
	
	
	
	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger			= Logger.getLogger( XFormsDBQuerySelect4UpdateHandler.class );

	
	// PUBLIC CONSTURCTORS
	public XFormsDBQuerySelect4UpdateHandler() {
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
				throw new DAOException( ErrorConstants.ERROR_CODE_DAO_32, ErrorConstants.ERROR_MESSAGE_DAO_32 );
			}

			xformsdbDAO							= daoFactory.getXFormsDBDAO();
			logger.log( Level.DEBUG, "The XFormsDB DAO of the appropriate DAO factory implementation has been successfully retrieved." );
		} catch ( DAOException daoex ) {
			throw new HandlerException( daoex.getCode(), daoex.getMessage(), daoex );
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBQUERYSELECT4UPDATEHANDLER_1, ErrorConstants.ERROR_MESSAGE_XFORMSDBQUERYSELECT4UPDATEHANDLER_1, ex  );
		}
		
		
		return xformsdbDAO;
	}


	// PUBLIC METHODS
	public String handle( XFormsDBServlet xformsdbServlet, HttpSession session, Document document ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		XFormsDBConfigTO xformsDBConfigTO		= null;
		String xmlString						= null;
		XFormsDBDAO xformsdbDAO					= null;
		
		
		try {
			// Retrieve the XFormsDB configuration file
			xformsDBConfigTO					= xformsdbServlet.getXFormsDBConfigTO();
			
			// Retrieve the XFormsDB DAO of the appropriate DAO Factory implementation
			xformsdbDAO							= this.getXFormsDBDAO( xformsdbServlet, document );
			
			// Retrieve the XML document
			xmlString							= xformsdbDAO.select( document, xformsDBConfigTO.getEncoding() );
			
			// Store the query result into XFormsDB
			XFormsDBQueryManager.addSelect4UpdateXFormsDBQueryResult( session, document.getRootElement().getAttributeValue( "id" ), xmlString );
			logger.log( Level.DEBUG, "The SELECT4UPDATE action of the XFormsDB query request has been successfully handled." );
		} catch ( DAOException daoex ) {
			throw new HandlerException( daoex.getCode(), daoex.getMessage(), daoex );
		} catch ( ManagerException mex ) {
			throw new HandlerException( mex.getCode(), mex.getMessage(), mex );
		} catch ( HandlerException hex ) {
			throw hex;
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBQUERYSELECT4UPDATEHANDLER_2, ErrorConstants.ERROR_MESSAGE_XFORMSDBQUERYSELECT4UPDATEHANDLER_2, ex );
		}
		
		
		return xmlString;
	}	
}