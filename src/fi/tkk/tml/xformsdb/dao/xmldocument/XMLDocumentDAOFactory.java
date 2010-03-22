package fi.tkk.tml.xformsdb.dao.xmldocument;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fi.tkk.tml.xformsdb.dao.DAOFactory;
import fi.tkk.tml.xformsdb.dao.XFormsDBDAO;


/**
 * XML document concrete DAO factory implementation.
 *  
 *
 * @author Markku Laine
 * @version 1.0	 Created on October 06, 2009
 */
public final class XMLDocumentDAOFactory extends DAOFactory {
	
	
		
	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger	= Logger.getLogger( XMLDocumentDAOFactory.class );

	
	// PUBLIC STATIC FINAL VARIABLES
	public static final String DRIVER	= null;

	
	// PUBLIC CONSTRUCTORS
	public XMLDocumentDAOFactory() {
		super( DAOFactory.XML_DOCUMENT );
		logger.log( Level.DEBUG, "Constructor has been called." );
	}
	
	
	// PUBLIC METHODS
	public XFormsDBDAO getXFormsDBDAO() {
		logger.log( Level.DEBUG, "Method has been called." );
		return new XMLDocumentXFormsDBDAO( super.getDAODataSource() );
	}
}