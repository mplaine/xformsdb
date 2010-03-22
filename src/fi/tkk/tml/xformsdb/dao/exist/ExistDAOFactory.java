package fi.tkk.tml.xformsdb.dao.exist;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fi.tkk.tml.xformsdb.dao.DAOFactory;
import fi.tkk.tml.xformsdb.dao.XFormsDBDAO;


/**
 * Exist concrete DAO Factory implementation.
 *  
 *
 * @author Markku Laine
 * @version 1.0	 Created on October 06, 2009
 */
public final class ExistDAOFactory extends DAOFactory {
	
	
		
	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger	= Logger.getLogger( ExistDAOFactory.class );

	
	// PUBLIC STATIC FINAL VARIABLES
	public static final String DRIVER	= "org.exist.xmldb.DatabaseImpl";

	
	// PUBLIC CONSTRUCTORS
	public ExistDAOFactory() {
		super( DAOFactory.EXIST );
		logger.log( Level.DEBUG, "Constructor has been called." );
	}
	

	// PUBLIC METHODS
	public XFormsDBDAO getXFormsDBDAO() {
		logger.log( Level.DEBUG, "Method has been called." );
		return new ExistXFormsDBDAO( super.getDAODataSource() );
	}
}