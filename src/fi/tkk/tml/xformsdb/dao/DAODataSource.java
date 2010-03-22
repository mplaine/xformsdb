package fi.tkk.tml.xformsdb.dao;

import java.io.Serializable;
import java.sql.Connection;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;

import fi.tkk.tml.xformsdb.error.DAOException;
import fi.tkk.tml.xformsdb.error.ErrorConstants;


/**
 * Maintain DAO data source information.
 * 
 * Work as a gateway when connecting to any type
 * of data source, i.e. database or XML source.
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on August 28, 2009
 */
public class DAODataSource implements Serializable {

	
	
	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger			= Logger.getLogger( DAODataSource.class );
	private static final long serialVersionUID	= 1;

	
	// PRIVATE VARIABLES
	private String id;
	private int type;
	private String uri;
	private String username;
	private String password;
	private String collection;
	
	
	// PUBLIC CONSTRUCTORS
	/**
	 * Default constructor.
	 */
	public DAODataSource( int type ) {
		logger.log( Level.DEBUG, "Constructor has been called." );
		this.id			= null;
		this.type		= type;
		this.uri		= null;
		this.username	= null;
		this.password	= null;
		this.collection	= null;
	}


	// PUBLIC METHODS
	/**
	 * Get the ID of the data source.
	 * 
	 * 
	 * @return id	The ID of the data source.
	 */
	public String getId() {
		logger.log( Level.DEBUG, "Method has been called." );
		return this.id;
	}

	
	/**
	 * Set the ID of the data source.
	 * 
	 * 
	 * @param id	The ID of the data source.
	 */
	public void setId( String id ) {
		logger.log( Level.DEBUG, "Method has been called." );
		this.id	= id;
	}

	
	/**
	 * Get the type of the data source.
	 * 
	 * 
	 * @return type		The type of the data source.
	 */
	public int getType() {
		logger.log( Level.DEBUG, "Method has been called." );
		return this.type;
	}


	/**
	 * Set the type of the data source.
	 * 
	 * 
	 * @param type	The type of the data source.
	 */
	public void setType( int type ) {
		logger.log( Level.DEBUG, "Method has been called." );
		this.type	= type;
	}


	/**
	 * Get the URI of the data source.
	 * 
	 * 
	 * @return uri	The URI of the data source.
	 */
	public String getUri() {
		logger.log( Level.DEBUG, "Method has been called." );
		return this.uri;
	}


	/**
	 * Set the URI of the data source.
	 * 
	 * 
	 * @param uri	The URI of the data source.
	 */
	public void setUri( String uri ) {
		logger.log( Level.DEBUG, "Method has been called." );
		this.uri	= uri;
	}


	/**
	 * Get the username of the data source.
	 * 
	 * 
	 * @return username		The username of the data source.
	 */
	public String getUsername() {
		logger.log( Level.DEBUG, "Method has been called." );
		return this.username;
	}


	/**
	 * Set the username of the data source.
	 * 
	 * 
	 * @param username	The username of the data source.
	 */
	public void setUsername( String username ) {
		logger.log( Level.DEBUG, "Method has been called." );
		this.username	= username;
	}
	
	
	/**
	 * Get the password of the data source.
	 * 
	 * 
	 * @return password		The password of the data source.
	 */
	public String getPassword() {
		logger.log( Level.DEBUG, "Method has been called." );
		return this.password;
	}


	/**
	 * Set the password of the data source.
	 * 
	 * 
	 * @param password	The password of the data source.
	 */
	public void setPassword( String password ) {
		logger.log( Level.DEBUG, "Method has been called." );
		this.password	= password;
	}
	
	
	/**
	 * Get the collection of the data source.
	 * 
	 * 
	 * @return collection	The collection of the data source.
	 */
	public String getCollection() {
		logger.log( Level.DEBUG, "Method has been called." );
		return this.collection;
	}


	/**
	 * Set the collection of the data source.
	 * 
	 * 
	 * @param collection	The collection of the data source.
	 */
	public void setCollection( String collection ) {
		logger.log( Level.DEBUG, "Method has been called." );
		this.collection	= collection;
	}

		
	/**
	 * Retrieve the connection to the database.
	 * 
	 * 
	 * @param driver			The driver class.
	 * @return connection		The connection to the database.
	 * @throws DAOException		DAO exception.
	 */
	public Connection getConnection( String driver ) throws DAOException {
		logger.log( Level.DEBUG, "Method has been called." );
		return null;
	}
	
	
	/**
	 * Retrieve the collection from the database.
	 * 
	 * 
	 * @param driver			The driver class.
	 * @return collection		The collection from the database.
	 * @throws DAOException		DAO exception.
	 */
	public Collection getCollection( String driver ) throws DAOException {
		logger.log( Level.DEBUG, "Method has been called." );
		Collection collection	= null;
		Class cl				= null;
		Database database		= null;
		
		
		try {
			// Load the driver class
			cl					= Class.forName( driver );
			logger.log( Level.DEBUG, "The driver class '" + driver + "' has been loaded." );

			// Create an instance of the database class
			database			= ( Database ) cl.newInstance();
			logger.log( Level.DEBUG, "A new instance of the Database class has been created." );

			// Register the Database implementation
			DatabaseManager.registerDatabase( database );
			logger.log( Level.DEBUG, "A new Database implementation with the DatabaseManager has been registered." );

			// Retrieve the Collection instance
			collection	= DatabaseManager.getCollection( this.uri + this.collection, this.username, this.password );
			if ( collection == null ) {
				throw new DAOException( ErrorConstants.ERROR_CODE_DAO_33, ErrorConstants.ERROR_MESSAGE_DAO_33 );
			}
			logger.log( Level.DEBUG, "The Collection instance from the database has been retrieved." );
		} catch ( DAOException daoex ) {
			throw daoex;
		} catch ( Exception ex ) {
			throw new DAOException( ErrorConstants.ERROR_CODE_DAO_34, ErrorConstants.ERROR_MESSAGE_DAO_34, ex );
		}
		
		
		return collection;
	}
	
	
	/**
	 * Retrieve the collection from the database.
	 * 
	 * 
	 * @param driver			The driver class.
	 * @param fixedCollection	The fixedCollection.
	 * @return collection		The collection from the database.
	 * @throws DAOException		DAO exception.
	 */
	public Collection getCollection( String driver, String fixedCollection ) throws DAOException {
		logger.log( Level.DEBUG, "Method has been called." );
		Collection collection	= null;
		Class cl				= null;
		Database database		= null;
		
		
		try {
			// Load the driver class
			cl					= Class.forName( driver );
			logger.log( Level.DEBUG, "The driver class '" + driver + "' has been loaded." );

			// Create an instance of the database class
			database			= ( Database ) cl.newInstance();
			logger.log( Level.DEBUG, "A new instance of the Database class has been created." );

			// Register the Database implementation
			DatabaseManager.registerDatabase( database );
			logger.log( Level.DEBUG, "A new Database implementation with the DatabaseManager has been registered." );

			// Retrieve the Collection instance
			collection	= DatabaseManager.getCollection( this.uri + fixedCollection, this.username, this.password );
			if ( collection == null ) {
				throw new DAOException( ErrorConstants.ERROR_CODE_DAO_131, ErrorConstants.ERROR_MESSAGE_DAO_131 );
			}
			logger.log( Level.DEBUG, "The Collection instance from the database has been retrieved." );
		} catch ( DAOException daoex ) {
			throw daoex;
		} catch ( Exception ex ) {
			throw new DAOException( ErrorConstants.ERROR_CODE_DAO_132, ErrorConstants.ERROR_MESSAGE_DAO_132, ex );
		}
		
		
		return collection;
	}
}