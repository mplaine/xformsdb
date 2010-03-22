package fi.tkk.tml.xformsdb.xml.to.xformsdb;

import java.io.Serializable;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fi.tkk.tml.xformsdb.core.Constants;


/**
 * The data source configuration used by the application,
 * i.e. parsed content of the <data-source> element from the
 * XFormsDB configuration file.
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on October 21, 2009
 */
public class DataSourceTO implements Serializable {
	
	
	
	// PRIVATE STATIC FINAL VARIABLES
	private static final long serialVersionUID		= 1;
	private static final Logger logger				= Logger.getLogger( DataSourceTO.class );
	
	
	// PUBLIC STATIC FINAL VARIABLES
	public static final String LOCAL_NAME			= "data-source";
	public static final String ATTRIBUTE_NAME_ID	= "id";

	
	// PRIVATE VARIABLES
	private String id;
	private String type;
	private String uri;
	private String username;
	private String password;
	private String collection;
	
	
	// PUBLIC CONSTRUCTORS
	/**
	 * Default constructor.
	 */
	public DataSourceTO() {
		logger.log( Level.DEBUG, "Constructor has been called." );
		this.id			= Constants.XFORMSDB_CONFIG_DEFAULT_DATA_SOURCE_ID;
		this.type		= Constants.XFORMSDB_CONFIG_DEFAULT_DATA_SOURCE_TYPE;
		this.uri		= Constants.XFORMSDB_CONFIG_DEFAULT_DATA_SOURCE_URI;
		this.username	= Constants.XFORMSDB_CONFIG_DEFAULT_DATA_SOURCE_USERNAME;
		this.password	= Constants.XFORMSDB_CONFIG_DEFAULT_DATA_SOURCE_PASSWORD;
		this.collection	= Constants.XFORMSDB_CONFIG_DEFAULT_DATA_SOURCE_COLLECTION;
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
	public String getType() {
		logger.log( Level.DEBUG, "Method has been called." );
		return this.type;
	}


	/**
	 * Set the type of the data source.
	 * 
	 * 
	 * @param type	The type of the data source.
	 */
	public void setType( String type ) {
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
	 * String presentation of the object.
	 * 
	 * 
	 * @return result	String presentation of the object.
	 */
	public String toString() {
		logger.log( Level.DEBUG, "Method has been called." );
		String result		=  "DataSourceTO:" + Constants.LINE_SEPARATOR;
		result				+= "-------------" + Constants.LINE_SEPARATOR;
		result				+= "id: " + this.id + Constants.LINE_SEPARATOR;
		result				+= "type: " + this.type + Constants.LINE_SEPARATOR;
		result				+= "uri: " + this.uri + Constants.LINE_SEPARATOR;
		result				+= "username: " + this.username + Constants.LINE_SEPARATOR;
		result				+= "password: " + this.password + Constants.LINE_SEPARATOR;
		result				+= "collection: " + this.collection + Constants.LINE_SEPARATOR;
		
		
		return result;		
	}
}