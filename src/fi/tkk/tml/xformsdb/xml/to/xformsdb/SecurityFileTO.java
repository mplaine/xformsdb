package fi.tkk.tml.xformsdb.xml.to.xformsdb;

import java.io.Serializable;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fi.tkk.tml.xformsdb.core.Constants;


/**
 * The security file defined by extension to be protected from clients,
 * i.e. parsed content of the <security-file> element from the XFormsDB
 * configuration file (conf.xml).
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on October 21, 2009
 */
public class SecurityFileTO implements Serializable {

	
	
	// PRIVATE STATIC FINAL VARIABLES
	private static final long serialVersionUID		= 1;
	private static final Logger logger				= Logger.getLogger( SecurityFileTO.class );

	
	// PUBLIC STATIC FINAL VARIABLES
	public static final String LOCAL_NAME			= "security-file";

	
	// PRIVATE VARIABLES
	private String extension;
	
	
	// PUBLIC CONSTRUCTORS
	/**
	 * Default constructor.
	 */
	public SecurityFileTO() {
		logger.log( Level.DEBUG, "Constructor has been called." );
		this.extension								= Constants.XFORMSDB_CONFIG_DEFAULT_SECURITY_FILE_EXTENSION_XQ;
	}
	
	
	// PUBLIC METHODS
	/**
	 * Get the extension of the security file.
	 * 
	 * 
	 * @return extension	The extension of the security file.
	 */
	public String getExtension() {
		logger.log( Level.DEBUG, "Method has been called." );
		return this.extension;
	}

	
	/**
	 * Set the extension of the security file.
	 * 
	 * 
	 * @param extension		The extension of the security file.
	 */
	public void setExtension( String extension ) {
		logger.log( Level.DEBUG, "Method has been called." );
		this.extension	= extension;
	}
	
	
	/**
	 * Copy object without references.
	 * 
	 * 
	 * @param securityFileTO	The object to be copied from.
	 */
	public void copy( SecurityFileTO securityFileTO ) {
		logger.log( Level.DEBUG, "Method has been called." );
		if ( securityFileTO != null ) {
			this.extension	= securityFileTO.getExtension();
		}
	}

	
	/**
	 * String presentation of the object.
	 * 
	 * 
	 * @return result	String presentation of the object.
	 */
	public String toString() {
		logger.log( Level.DEBUG, "Method has been called." );
		String result	=  "SecurityFileTO:" + Constants.LINE_SEPARATOR;
		result			+= "---------------" + Constants.LINE_SEPARATOR;
		result			+= "extension: " + this.extension + Constants.LINE_SEPARATOR;
		
		
		return result;
	}
}