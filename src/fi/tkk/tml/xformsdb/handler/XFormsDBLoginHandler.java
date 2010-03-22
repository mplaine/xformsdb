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
import fi.tkk.tml.xformsdb.manager.xformsdb.XFormsDBUserManager;
import fi.tkk.tml.xformsdb.servlet.XFormsDBServlet;
import fi.tkk.tml.xformsdb.xml.to.xformsdb.XFormsDBConfigTO;


/**
 * Handle the <xformsdb:login> requests.
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on October 15, 2009
 */
public class XFormsDBLoginHandler extends ResponseHandler {
	
	
	
	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger	= Logger.getLogger( XFormsDBLoginHandler.class );

	
	// PUBLIC CONSTURCTORS
	public XFormsDBLoginHandler() {
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
			
			// Retrieve the appropriate DAO Factory implementation
			DAOFactory daoFactory				= null;
			String dataSrc						= document.getRootElement().getAttributeValue( "datasrc" );
			
			if ( dataSrc != null ) {
				daoFactory						= daoFactories.get( dataSrc );
			}
			// Use the default DAO factory implementation
			else {
				daoFactory						= daoFactories.get( Constants.XFORMSDB_CONFIG_DEFAULT_DATA_SOURCE_ID );
			}
			
			if ( daoFactory == null) {
				throw new DAOException( ErrorConstants.ERROR_CODE_DAO_35, ErrorConstants.ERROR_MESSAGE_DAO_35 );
			}
			
			xformsdbDAO							= daoFactory.getXFormsDBDAO();
			logger.log( Level.DEBUG, "The XFormsDB DAO of the appropriate DAO factory implementation has been successfully retrieved." );
		} catch ( DAOException daoex ) {
			throw new HandlerException( daoex.getCode(), daoex.getMessage(), daoex );
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBLOGINHANDLER_2, ErrorConstants.ERROR_MESSAGE_XFORMSDBLOGINHANDLER_2, ex  );
		}
		
		
		return xformsdbDAO;
	}

	
	// PUBLIC METHODS
	public String handle( XFormsDBServlet xformsdbServlet, HttpSession session, Document document ) throws HandlerException {
		logger.log( Level.DEBUG, "Method has been called." );
		XFormsDBConfigTO xformsDBConfigTO	= null;
		String xformsdbLogin				= null;
		XFormsDBDAO xformsdbDAO				= null;
		
		
		try {
			// Retrieve the XFormsDB configuration file
			xformsDBConfigTO				= xformsdbServlet.getXFormsDBConfigTO();

			// Retrieve the XFormsDB DAO of the appropriate DAO Factory implementation
			xformsdbDAO						= this.getXFormsDBDAO( xformsdbServlet, document );

			// Retrieve the information of the authenticated user
			xformsdbLogin					= xformsdbDAO.authenticateUser( document, xformsDBConfigTO.getEncoding() );

			// Store the logged in XFormsDB user into the session
			XFormsDBUserManager.setLoggedInXFormsDBUser( session, xformsdbLogin );
			logger.log( Level.DEBUG, "The user has been successfully logged in to XFormsDB." );
		} catch ( DAOException daoex ) {
			throw new HandlerException( daoex.getCode(), daoex.getMessage(), daoex );
		} catch ( ManagerException mex ) {
			throw new HandlerException( mex.getCode(), mex.getMessage(), mex );
		} catch ( HandlerException hex ) {
			throw hex;
		} catch ( Exception ex ) {
			throw new HandlerException( ErrorConstants.ERROR_CODE_XFORMSDBLOGINHANDLER_3, ErrorConstants.ERROR_MESSAGE_XFORMSDBLOGINHANDLER_3, ex  );
		}
		
		
		return xformsdbLogin;
	}	
}