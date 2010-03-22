package fi.tkk.tml.xformsdb.xml.to.xformsdb;

import java.io.Serializable;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fi.tkk.tml.xformsdb.core.Constants;


/**
 * The data source configuration for files metadata used by the
 * application, i.e. parsed content of the <files-metadata> element
 * from the XFormsDB configuration file.
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on October 21, 2009
 */
public class FilesMetadataTO implements Serializable {
	
	
	
	// PRIVATE STATIC FINAL VARIABLES
	private static final long serialVersionUID		= 1;
	private static final Logger logger				= Logger.getLogger( FilesMetadataTO.class );
	
	
	// PUBLIC STATIC FINAL VARIABLES
	public static final String LOCAL_NAME			= "files-metadata";

	
	// PRIVATE VARIABLES
	private String type;
	private String uri;
	private String username;
	private String password;
	private String collection;
	
	
	// PUBLIC CONSTRUCTORS
	/**
	 * Default constructor.
	 */
	public FilesMetadataTO() {
		logger.log( Level.DEBUG, "Constructor has been called." );
		this.type		= Constants.XFORMSDB_CONFIG_DEFAULT_FILES_METADATA_TYPE;
		this.uri		= Constants.XFORMSDB_CONFIG_DEFAULT_FILES_METADATA_URI;
		this.username	= Constants.XFORMSDB_CONFIG_DEFAULT_FILES_METADATA_USERNAME;
		this.password	= Constants.XFORMSDB_CONFIG_DEFAULT_FILES_METADATA_PASSWORD;
		this.collection	= Constants.XFORMSDB_CONFIG_DEFAULT_FILES_METADATA_COLLECTION;
	}
	
	
	// PUBLIC METHODS
	/**
	 * Get the type of the data source for files metadata.
	 * 
	 * 
	 * @return type		The type of the data source for files metadata.
	 */
	public String getType() {
		logger.log( Level.DEBUG, "Method has been called." );
		return this.type;
	}


	/**
	 * Set the type of the data source for files metadata.
	 * 
	 * 
	 * @param type	The type of the data source for files metadata.
	 */
	public void setType( String type ) {
		logger.log( Level.DEBUG, "Method has been called." );
		this.type	= type;
	}


	/**
	 * Get the URI of the data source for files metadata.
	 * 
	 * 
	 * @return uri	The URI of the data source for files metadata.
	 */
	public String getUri() {
		logger.log( Level.DEBUG, "Method has been called." );
		return this.uri;
	}


	/**
	 * Set the URI of the data source for files metadata.
	 * 
	 * 
	 * @param uri	The URI of the data source for files metadata.
	 */
	public void setUri( String uri ) {
		logger.log( Level.DEBUG, "Method has been called." );
		this.uri	= uri;
	}


	/**
	 * Get the username of the data source for files metadata.
	 * 
	 * 
	 * @return username		The username of the data source for files metadata.
	 */
	public String getUsername() {
		logger.log( Level.DEBUG, "Method has been called." );
		return this.username;
	}


	/**
	 * Set the username of the data source for files metadata.
	 * 
	 * 
	 * @param username	The username of the data source for files metadata.
	 */
	public void setUsername( String username ) {
		logger.log( Level.DEBUG, "Method has been called." );
		this.username	= username;
	}
	
	
	/**
	 * Get the password of the data source for files metadata.
	 * 
	 * 
	 * @return password		The password of the data source for files metadata.
	 */
	public String getPassword() {
		logger.log( Level.DEBUG, "Method has been called." );
		return this.password;
	}


	/**
	 * Set the password of the data source for files metadata.
	 * 
	 * 
	 * @param password	The password of the data source for files metadata.
	 */
	public void setPassword( String password ) {
		logger.log( Level.DEBUG, "Method has been called." );
		this.password	= password;
	}
	
	
	/**
	 * Get the collection of the data source for files metadata.
	 * 
	 * 
	 * @return collection	The collection of the data source for files metadata.
	 */
	public String getCollection() {
		logger.log( Level.DEBUG, "Method has been called." );
		return this.collection;
	}


	/**
	 * Set the collection of the data source for files metadata.
	 * 
	 * 
	 * @param collection	The collection of the data source for files metadata.
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
		String result		=  "FilesMetadataTO:" + Constants.LINE_SEPARATOR;
		result				+= "----------------" + Constants.LINE_SEPARATOR;
		result				+= "type: " + this.type + Constants.LINE_SEPARATOR;
		result				+= "uri: " + this.uri + Constants.LINE_SEPARATOR;
		result				+= "username: " + this.username + Constants.LINE_SEPARATOR;
		result				+= "password: " + this.password + Constants.LINE_SEPARATOR;
		result				+= "collection: " + this.collection + Constants.LINE_SEPARATOR;
		
		
		return result;		
	}
}