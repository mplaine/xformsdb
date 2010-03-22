package fi.tkk.tml.xformsdb.xml.to;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fi.tkk.tml.xformsdb.core.Constants;


/**
 * The Web application file (web.xml), i.e. parsed
 * content of the <web-app> element.
 * 
 *
 * @author Markku Laine
 * @version 1.0	 Created on October 21, 2009
 */
public class WebAppTO implements Serializable {
	
	
	
	// PRIVATE STATIC FINAL VARIABLES
	private static final long serialVersionUID	= 1;
	private static final Logger logger			= Logger.getLogger( WebAppTO.class );

	
	// PUBLIC STATIC FINAL VARIABLES
	public static final String LOCAL_NAME		= "web-app";

	
	// PRIVATE VARIABLES
	private List<String> welcomeFiles;	
	private Map<String, MimeMappingTO> mimeMappingTOs;
	
	
	// PUBLIC CONSTRUCTORS
	/**
	 * Default constructor.
	 */
	public WebAppTO() {
		logger.log( Level.DEBUG, "Constructor has been called." );
		this.welcomeFiles	= new ArrayList<String>( 8 );
		this.mimeMappingTOs	= new HashMap<String, MimeMappingTO>( 128 );
	}
	
	
	// PUBLIC METHODS
	/**
	 * Get the welcome files.
	 * 
	 * 
	 * @return welcomeFiles		The welcome files.
	 */
	public List<String> getWelcomeFiles() {
		logger.log( Level.DEBUG, "Method has been called." );
		return this.welcomeFiles;
	}
	
	
	/**
	 * Set the welcome files.
	 * 
	 * 
	 * @param welcomeFiles	The welcome files.
	 */
	public void setWelcomeFiles( List<String> welcomeFiles ) {
		logger.log( Level.DEBUG, "Method has been called." );
		this.welcomeFiles	= welcomeFiles;
	}
	

	/**
	 * Get the MIME mappings (extension and MIME type).
	 * 
	 * 
	 * @return mimeMappingTOs	The MIME mappings (extension and MIME type).
	 */
	public Map<String, MimeMappingTO> getMimeMappingTOs() {
		logger.log( Level.DEBUG, "Method has been called." );
		return this.mimeMappingTOs;
	}

	
	/**
	 * Set the MIME mappings (extension and MIME type).
	 * 
	 * 
	 * @param mimeMappingTOs	The MIME mappings (extension and MIME type).
	 */
	public void setMimeMappingTOs( Map<String, MimeMappingTO> mimeMappingTOs ) {
		logger.log( Level.DEBUG, "Method has been called." );
		this.mimeMappingTOs	= mimeMappingTOs;
	}

	
	/**
	 * String presentation of the object.
	 * 
	 * 
	 * @return result	String presentation of the object.
	 */
	public String toString() {
		logger.log( Level.DEBUG, "Method has been called." );
		String result		=  "WebAppTO:" + Constants.LINE_SEPARATOR;
		result				+= "---------" + Constants.LINE_SEPARATOR;
		for ( int i = 0; i < this.welcomeFiles.size(); i++ ) {
			if ( this.welcomeFiles.get( i ) != null ) {
				result		+= "WelcomeFile:" + Constants.LINE_SEPARATOR;
				result		+= "------------" + Constants.LINE_SEPARATOR;
				result		+= this.welcomeFiles.get( i ).toString() + Constants.LINE_SEPARATOR + Constants.LINE_SEPARATOR;
			}
		}
		
		Set<String> keys	= this.mimeMappingTOs.keySet();
		Iterator it			= keys.iterator();
		String key			= null;
		MimeMappingTO value	= null;
		while ( it.hasNext() ) {
			key				= ( String ) it.next();
			value			= this.mimeMappingTOs.get( key );
			result			+= value.toString() + Constants.LINE_SEPARATOR;
		}
		
		
		return result;		
	}
}