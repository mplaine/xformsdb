package fi.tkk.tml.xformsdb.xml.to;

import java.io.Serializable;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fi.tkk.tml.xformsdb.core.Constants;


/**
 * The MIME mapping (extension and MIME type), i.e. parsed content of
 * the <mime-mapping> element from the Web application file (web.xml).
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on October 21, 2009
 */
public class MimeMappingTO implements Serializable {
	
	
	
	// PRIVATE STATIC FINAL VARIABLES
	private static final long serialVersionUID	= 1;
	private static final Logger logger			= Logger.getLogger( MimeMappingTO.class );
	
	
	// PUBLIC STATIC FINAL VARIABLES
	public static final String LOCAL_NAME		= "mime-mapping";

	
	// PRIVATE VARIABLES
	private String extension;
	private String mimeType;
	
	
	// PUBLIC CONSTRUCTORS
	/**
	 * Default constructor.
	 */
	public MimeMappingTO() {
		logger.log( Level.DEBUG, "Constructor has been called." );
		this.extension	= null;
		this.mimeType	= null;
	}
	
	
	// PUBLIC METHODS
	/**
	 * Get the extension of the file.
	 * 
	 * 
	 * @return extension	The extension of the file.
	 */
	public String getExtension() {
		logger.log( Level.DEBUG, "Method has been called." );
		return this.extension;
	}

	
	/**
	 * Set the extension of the file.
	 * 
	 * 
	 * @param extension	The extension of the file.
	 */
	public void setExtension( String extension ) {
		logger.log( Level.DEBUG, "Method has been called." );
		this.extension	= extension;
	}

	
	/**
	 * Get the MIME type of the file.
	 * 
	 * 
	 * @return mimeType		The MIME type of the file.
	 */
	public String getMimeType() {
		logger.log( Level.DEBUG, "Method has been called." );
		return this.mimeType;
	}


	/**
	 * Set the MIME type of the file.
	 * 
	 * 
	 * @param mimeType		The MIME type of the file.
	 */
	public void setMimeType( String mimeType ) {
		logger.log( Level.DEBUG, "Method has been called." );
		this.mimeType	= mimeType;
	}

	
	/**
	 * String presentation of the object.
	 * 
	 * 
	 * @return result	String presentation of the object.
	 */
	public String toString() {
		logger.log( Level.DEBUG, "Method has been called." );
		String result		=  "MimeMappingTO:" + Constants.LINE_SEPARATOR;
		result				+= "--------------" + Constants.LINE_SEPARATOR;
		result				+= "extension: " + this.extension + Constants.LINE_SEPARATOR;
		result				+= "mimeType: " + this.mimeType + Constants.LINE_SEPARATOR;
		
		
		return result;		
	}
}