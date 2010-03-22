package fi.tkk.tml.xformsdb.handler;

import java.util.Map;

import nu.xom.Document;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fi.tkk.tml.xformsdb.core.Constants;
import fi.tkk.tml.xformsdb.dao.DAOFactory;
import fi.tkk.tml.xformsdb.dao.XFormsDBDAO;
import fi.tkk.tml.xformsdb.error.DAOException;
import fi.tkk.tml.xformsdb.error.ErrorConstants;
import fi.tkk.tml.xformsdb.error.HandlerException;
import fi.tkk.tml.xformsdb.servlet.XFormsDBServlet;
import fi.tkk.tml.xformsdb.xml.to.xformsdb.XFormsDBConfigTO;


/**
 * Handle the <xformsdb:file> SELECT requests.
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on October 15, 2009
 */
public class XFormsDBFileSelectHandler extends ResponseHandler {
	
	
	
	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger			= Logger.getLogger( XFormsDBFileSelectHandler.class );

	
	// PUBLIC CONSTURCTORS
	public XFormsDBFileSelectHandler() {
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
				throw new DAOException( ErrorConstants.ERROR_CODE_DAO_62, ErrorConstants.ERROR_MESSAGE_DAO_62 );
			}
			
			xformsdbDAO							= daoFactory.getXFormsDBDAO();
			logger.log( Level.DEBUG, "The XFormsDB DAO of the files metadata DAO factory implementation has been successfully retrieved." );
		} catch ( DAOException daoex ) {
			throw new HandlerException( daoex.getCode(), daoex.getMessage(), daoex );
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBFILESELECTHANDLER_1, ErrorConstants.ERROR_MESSAGE_XFORMSDBFILESELECTHANDLER_1, ex  );
		}
		
		
		return xformsdbDAO;
	}

	
	// PUBLIC METHODS
	public String handle( XFormsDBServlet xformsdbServlet, String actionURL, Document document ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		XFormsDBConfigTO xformsDBConfigTO	= null;
		String xformsdbFiles				= null;
		XFormsDBDAO xformsdbDAO				= null;
		
		
		try {
			// Retrieve the XFormsDB configuration file
			xformsDBConfigTO				= xformsdbServlet.getXFormsDBConfigTO();

			// Retrieve the XFormsDB DAO of the appropriate DAO Factory implementation
			xformsdbDAO						= this.getXFormsDBDAO( xformsdbServlet, document );

			// Retrieve the XML document
			xformsdbFiles					= xformsdbDAO.selectFiles( document, actionURL, xformsDBConfigTO.getEncoding() );			
			logger.log( Level.DEBUG, "The SELECT action of the XFormsDB file request has been successfully handled." );
		} catch ( DAOException daoex ) {
			throw new HandlerException( daoex.getCode(), daoex.getMessage(), daoex );
		} catch ( HandlerException hex ) {
			throw hex;
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBFILESELECTHANDLER_2, ErrorConstants.ERROR_MESSAGE_XFORMSDBFILESELECTHANDLER_2, ex  );
		}
		
		
		return xformsdbFiles;
	}
	
	
	public String handle( XFormsDBServlet xformsdbServlet, Document document ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		XFormsDBConfigTO xformsDBConfigTO	= null;
		String xformsdbFile					= null;
		XFormsDBDAO xformsdbDAO				= null;
		
		
		try {
			// Retrieve the XFormsDB configuration file
			xformsDBConfigTO				= xformsdbServlet.getXFormsDBConfigTO();

			// Retrieve the XFormsDB DAO of the appropriate DAO Factory implementation
			xformsdbDAO						= this.getXFormsDBDAO( xformsdbServlet, document );

			// Retrieve the XML document
			xformsdbFile					= xformsdbDAO.selectFile( document, xformsDBConfigTO.getEncoding() );			
			logger.log( Level.DEBUG, "The SELECT action of the XFormsDB file request has been successfully handled." );
		} catch ( DAOException daoex ) {
			throw new HandlerException( daoex.getCode(), daoex.getMessage(), daoex );
		} catch ( HandlerException hex ) {
			throw hex;
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBFILESELECTHANDLER_3, ErrorConstants.ERROR_MESSAGE_XFORMSDBFILESELECTHANDLER_3, ex  );
		}
		
		
		return xformsdbFile;
	}
}