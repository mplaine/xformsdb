package fi.tkk.tml.xformsdb.dao;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fi.tkk.tml.xformsdb.dao.exist.ExistDAOFactory;
import fi.tkk.tml.xformsdb.dao.xmldocument.XMLDocumentDAOFactory;
import fi.tkk.tml.xformsdb.error.DAOException;
import fi.tkk.tml.xformsdb.servlet.XFormsDBServlet;
import fi.tkk.tml.xformsdb.xml.to.xformsdb.XFormsDBConfigTO;


/**
 * Abstract class DAO factory.
 * Define common functionalities and interfaces
 * for all DAO factory implementations.
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on October 15, 2009
 */
public abstract class DAOFactory {

	
	
	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger		= Logger.getLogger( DAOFactory.class );


	// PRIVATE VARIABLES
	private DAODataSource daoDataSource;

	
	// PUBLIC STATIC FINAL VARIABLES
	public static final int XML_DOCUMENT	= 1;
	public static final int EXIST			= 2;
		
	
	// PUBLIC CONSTRUCTORS
	public DAOFactory( int dataSourceType ) {
		logger.log( Level.DEBUG, "Constructor has been called." );
		this.daoDataSource					= new DAODataSource( dataSourceType );
	}

	
	// PUBLIC STATIC METHODS
	public static DAOFactory getDAOFactory( int dataSourceType ) {
		switch ( dataSourceType ) {
			case DAOFactory.XML_DOCUMENT:
				return new XMLDocumentDAOFactory();
			case DAOFactory.EXIST:
				return new ExistDAOFactory();
			default:
				return null;
		}
	}
	
	
	// PUBLIC ABSTRACT METHODS
	public abstract XFormsDBDAO getXFormsDBDAO();
	
	
	// PUBLIC METHODS
	public XFormsDBConfigTO getXFormsDBConfigTO( XFormsDBServlet xformsdbServlet ) throws DAOException {
		logger.log( Level.DEBUG, "Method has been called." );
		XFormsDBConfigTO xformsDBConfigTO	= null;
		
		
		try {
			// Retrieve the XFormsDB configuration file from the servlet context
			xformsDBConfigTO				= xformsdbServlet.getXFormsDBConfigTO();
			logger.log( Level.DEBUG, "The the XFormsDB configuration file has been retrieved from the servlet context." );
		} catch ( Exception ex ) {
			throw new DAOException( "Failed to retrieve the XFormsDB configuration file from the servlet context.", ex  );
		}
		
		
		return xformsDBConfigTO;
	}
	public DAODataSource getDAODataSource() {
		logger.log( Level.DEBUG, "Method has been called." );
		return this.daoDataSource;
	}
	public void setDAODataSource( DAODataSource daoDataSource ) {
		logger.log( Level.DEBUG, "Method has been called." );
		this.daoDataSource					= daoDataSource;
	}
}