package fi.tkk.tml.xformsdb.xml.to;

import java.io.Serializable;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fi.tkk.tml.xformsdb.core.Constants;


/**
 * The request header transfer object.
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on October 26, 2009
 */
public class RequestHeaderTO implements Serializable {
	
	
	
	// PRIVATE STATIC FINAL VARIABLES
	private static final long serialVersionUID	= 1;
	private static final Logger logger			= Logger.getLogger( RequestHeaderTO.class );
	
	
	// PRIVATE VARIABLES
	private String name;
	private String value;
	
	
	// PUBLIC CONSTRUCTORS
	/**
	 * Default constructor.
	 */
	public RequestHeaderTO( String name, String value ) {
		logger.log( Level.DEBUG, "Constructor has been called." );
		this.name	= name;
		this.value	= value;
	}
	
	
	// PUBLIC METHODS
	/**
	 * Get the name.
	 * 
	 * 
	 * @return name		The name.
	 */
	public String getName() {
		logger.log( Level.DEBUG, "Method has been called." );
		return this.name;
	}

	
	/**
	 * Set the name.
	 * 
	 * 
	 * @param name	The name.
	 */
	public void setName( String name ) {
		logger.log( Level.DEBUG, "Method has been called." );
		this.name	= name;
	}

	
	/**
	 * Get the value.
	 * 
	 * 
	 * @return value	The value.
	 */
	public String getValue() {
		logger.log( Level.DEBUG, "Method has been called." );
		return this.value;
	}


	/**
	 * Set the value.
	 * 
	 * 
	 * @param value		The value.
	 */
	public void setValue( String value ) {
		logger.log( Level.DEBUG, "Method has been called." );
		this.value	= value;
	}

	
	/**
	 * String presentation of the object.
	 * 
	 * 
	 * @return result	String presentation of the object.
	 */
	public String toString() {
		logger.log( Level.DEBUG, "Method has been called." );
		String result		=  "RequestHeaderTO:" + Constants.LINE_SEPARATOR;
		result				+= "----------------" + Constants.LINE_SEPARATOR;
		result				+= "name: " + this.name + Constants.LINE_SEPARATOR;
		result				+= "value: " + this.value + Constants.LINE_SEPARATOR;
		
		
		return result;		
	}
}